package kin.devplatform;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.UUID;
import kin.devplatform.accountmanager.AccountManager;
import kin.devplatform.accountmanager.AccountManager.AccountState;
import kin.devplatform.accountmanager.AccountManagerImpl;
import kin.devplatform.accountmanager.AccountManagerLocal;
import kin.devplatform.base.Observer;
import kin.devplatform.bi.EventLogger;
import kin.devplatform.bi.EventLoggerImpl;
import kin.devplatform.bi.events.GeneralEcosystemSdkError;
import kin.devplatform.bi.events.KinSdkInitiated;
import kin.devplatform.core.util.DeviceUtils;
import kin.devplatform.core.util.ExecutorsUtil;
import kin.devplatform.data.auth.AuthLocalData;
import kin.devplatform.data.auth.AuthRemoteData;
import kin.devplatform.data.auth.AuthRepository;
import kin.devplatform.data.blockchain.BlockchainSourceImpl;
import kin.devplatform.data.blockchain.BlockchainSourceLocal;
import kin.devplatform.data.offer.OfferRemoteData;
import kin.devplatform.data.offer.OfferRepository;
import kin.devplatform.data.order.OrderLocalData;
import kin.devplatform.data.order.OrderRemoteData;
import kin.devplatform.data.order.OrderRepository;
import kin.devplatform.exception.BlockchainException;
import kin.devplatform.exception.ClientException;
import kin.devplatform.exception.KinEcosystemException;
import kin.devplatform.network.model.AuthToken;
import kin.devplatform.network.model.KinVersionProvider;
import kin.devplatform.network.model.SignInData;
import kin.devplatform.network.model.SignInData.SignInTypeEnum;
import kin.devplatform.util.ErrorUtil;
import kin.devplatform.util.JwtBody;
import kin.devplatform.util.JwtDecoder;
import kin.sdk.migration.MigrationManager;
import kin.sdk.migration.MigrationNetworkInfo;
import kin.sdk.migration.exception.MigrationInProcessException;
import kin.sdk.migration.interfaces.IKinClient;
import kin.sdk.migration.interfaces.IMigrationManagerCallbacks;
import org.json.JSONException;

public final class KinEcosystemInitiator {

	private static final String TAG = KinEcosystemInitiator.class.getSimpleName();
	private static final String KIN_ECOSYSTEM_STORE_PREFIX_KEY = "kinecosystem_store";
	private static KinEcosystemInitiator instance;

	private final ExecutorsUtil executorsUtil;
	private volatile boolean isInitialized = false;
	private volatile boolean isLoggedIn = false;

	public static KinEcosystemInitiator getInstance() {
		if (instance == null) {
			synchronized (KinEcosystemInitiator.class) {
				if (instance == null) {
					instance = new KinEcosystemInitiator();
				}
			}
		}
		return instance;
	}

	private KinEcosystemInitiator() {
		this.executorsUtil = new ExecutorsUtil();
	}

	/**
	 * Uses for internal initialization after process restart
	 */
	public void internalInit(Context context) {
		if (!isInitialized) {
			initConfiguration(context, null);
			initEventManagerRelatedServices(context, null, null);
			MigrationManager migrationManager = getMigrationManager(context, null);
			try {
				handleKinClientReady(migrationManager.getCurrentKinClient(), context, null, null,
					false, null);
			} catch (BlockchainException e) {
				EventLoggerImpl.getInstance().send(GeneralEcosystemSdkError.create(
					ErrorUtil.getPrintableStackTrace(e), String.valueOf(e.getCode()),
					"KinEcosystemInitiator internalInit failed"
				));
				e.printStackTrace();
			}
		}
	}

	/**
	 * Uses for external (public API) initialization and jwt login.
	 */
	public void externalInit(Context context, KinEnvironment environment, @NonNull String jwt,
		final KinCallback<Void> loginCallback, @Nullable final KinMigrationListener migrationProcessCallback) {
		SignInData signInData;
		try {
			signInData = getJwtSignInData(jwt);
			initEventManagerRelatedServices(context, signInData, environment);
		} catch (JSONException | IllegalArgumentException e) {
			fireStartError(ErrorUtil.getClientException(ClientException.BAD_CONFIGURATION, e), loginCallback);
			return;
		}
		if (isInitialized && isLoggedIn) {
			fireStartCompleted(loginCallback);
			return;
		}

		init(context, signInData.getAppId(), signInData, loginCallback, migrationProcessCallback);
		// If initialized then do the login and if not then will do login at end of migration which happens inside init.
		if (isInitialized) {
			login(signInData, loginCallback);
		}
	}

	private void initEventManagerRelatedServices(Context context, SignInData signInData,
		KinEnvironment environment) {
		initConfiguration(context, environment);
		AuthRepository
			.init(AuthLocalData.getInstance(context, executorsUtil), AuthRemoteData.getInstance(executorsUtil));
		if (signInData != null) {
			String deviceID = AuthRepository.getInstance().getDeviceID();
			signInData.setDeviceId(deviceID != null ? deviceID : UUID.randomUUID().toString());
			AuthRepository.getInstance().setSignInData(signInData);
		}
		EventCommonDataUtil.setBaseData(context);
	}

	private SignInData getJwtSignInData(@NonNull final String jwt) throws JSONException {
		JwtBody jwtBody = JwtDecoder.getJwtBody(jwt);
		if (jwtBody == null) {
			throw new IllegalArgumentException("jwt is empty");
		}
		return new SignInData()
			.signInType(SignInTypeEnum.JWT)
			.appId(jwtBody.getAppId())
			.userId(jwtBody.getUserId())
			.jwt(jwt);
	}

	private void init(Context context, String appId, SignInData signInData, KinCallback<Void> loginCallback,
		KinMigrationListener migrationCallback) {
		if (!isInitialized) {
			MigrationManager migrationManager = getMigrationManager(context, appId);
			try {
				handleMigration(context, appId, migrationManager, signInData, loginCallback, migrationCallback);
			} catch (MigrationInProcessException e) {
				Logger.log(new Log().priority(Log.WARN).withTag(TAG)
					.text("Start migration when it was already in migration process"));
			}
		}
	}

	private MigrationManager getMigrationManager(Context context, String appId) {
		AuthRepository
			.init(AuthLocalData.getInstance(context, executorsUtil),
				AuthRemoteData.getInstance(executorsUtil));
		EventCommonDataUtil.setBaseData(context);
		KinEnvironment kinEnvironment = ConfigurationImpl.getInstance().getEnvironment();

		final String oldNetworkUrl = kinEnvironment.getOldBlockchainNetworkUrl();
		final String oldNetworkId = kinEnvironment.getOldBlockchainPassphrase();
		final String newNetworkUrl = kinEnvironment.getNewBlockchainNetworkUrl();
		final String newNetworkId = kinEnvironment.getNewBlockchainPassphrase();
		final String issuer = kinEnvironment.getOldBlockchainIssuer();

		if (appId == null) {
			appId = BlockchainSourceLocal.getInstance(context).getAppId();
		}

		MigrationNetworkInfo migrationNetworkInfo = new MigrationNetworkInfo(oldNetworkUrl, oldNetworkId,
			newNetworkUrl, newNetworkId, issuer);

		MigrationManager migrationManager = new MigrationManager(context, appId, migrationNetworkInfo,
			new KinVersionProvider(appId),
			new MigrationEventsListener(EventLoggerImpl.getInstance()), KIN_ECOSYSTEM_STORE_PREFIX_KEY);
		migrationManager.enableLogs(Logger.isEnabled());
		return migrationManager;
	}

	private void initConfiguration(Context context, KinEnvironment environment) {
		ConfigurationLocal configurationLocal = ConfigurationLocal.getInstance(context);
		if (environment != null) {
			configurationLocal.setEnvironment(environment);
		}

		ConfigurationImpl.init(configurationLocal);
	}

	private void handleMigration(final Context context, final String appId, final MigrationManager migrationManager,
		final SignInData signInData, final KinCallback<Void> loginCallback,
		final KinMigrationListener migrationCallback) throws
		MigrationInProcessException {
		migrationManager.start(new IMigrationManagerCallbacks() {

			private boolean didMigrationStarted;

			@Override
			public void onMigrationStart() {
				Logger.log(new Log().priority(Log.DEBUG).withTag(TAG).text("onMigrationStart"));
				didMigrationStarted = true;
				if (migrationCallback != null) {
					migrationCallback.onStart();
				}
			}

			@Override
			public void onReady(IKinClient kinClient) {
				Logger.log(new Log().priority(Log.DEBUG).withTag(TAG).text("onReady"));
				try {
					handleKinClientReady(kinClient, context, appId, signInData, true, loginCallback);
					if (migrationCallback != null && didMigrationStarted) {
						didMigrationStarted = false;
						migrationCallback.onFinish();
					}
				} catch (BlockchainException e) {
					didMigrationStarted = false;
					fireStartError(e, loginCallback);
				}
			}

			@Override
			public void onError(Exception e) {
				Logger.log(new Log().priority(Log.DEBUG).withTag(TAG).text("onError"));
				if (migrationCallback != null) {
					migrationCallback.onError(e);
				}
				fireStartError(ErrorUtil.createMigrationFailureException(e), loginCallback);
			}
		});
	}

	private void handleKinClientReady(IKinClient kinClient, Context context, String appId, SignInData signInData,
		boolean withLogin, KinCallback<Void> loginCallback) throws BlockchainException {
		final EventLogger eventLogger = EventLoggerImpl.getInstance();
		BlockchainSourceImpl.init(eventLogger, kinClient, BlockchainSourceLocal.getInstance(context));
		if (appId != null) {
			BlockchainSourceImpl.getInstance().setAppID(appId);
		}

		AccountManagerImpl
			.init(AccountManagerLocal.getInstance(context), eventLogger, AuthRepository.getInstance(),
				BlockchainSourceImpl.getInstance());

		OrderRepository.init(BlockchainSourceImpl.getInstance(),
			eventLogger,
			OrderRemoteData.getInstance(executorsUtil),
			OrderLocalData.getInstance(context, executorsUtil));

		OfferRepository
			.init(OfferRemoteData.getInstance(executorsUtil), OrderRepository.getInstance());

		DeviceUtils.init(context);

		eventLogger.send(KinSdkInitiated.create());
		isInitialized = true;
		if (withLogin) {
			login(signInData, loginCallback);
		}
	}

	private void login(@NonNull SignInData signInData, final KinCallback<Void> loginCallback) {
		setAuthRepositoryData(signInData);
		performLogin(loginCallback);
	}

	private void setAuthRepositoryData(@NonNull SignInData signInData) {
		String publicAddress = BlockchainSourceImpl.getInstance().getPublicAddress();
		String deviceID = AuthRepository.getInstance().getDeviceID();
		signInData.setDeviceId(deviceID != null ? deviceID : UUID.randomUUID().toString());
		signInData.setWalletAddress(publicAddress);
		AuthRepository.getInstance().setSignInData(signInData);
	}

	private void performLogin(final KinCallback<Void> loginCallback) {
		AuthRepository.getInstance().getAuthToken(new KinCallback<AuthToken>() {
			@Override
			public void onResponse(AuthToken response) {
				final AccountManager accountManager = AccountManagerImpl.getInstance();
				if (accountManager.isAccountCreated()) {
					activateAccount(loginCallback);
				} else {
					handleAccountNotCreatedState(accountManager, loginCallback);
				}
			}

			@Override
			public void onFailure(final KinEcosystemException exception) {
				fireStartError(exception, loginCallback);
			}
		});
	}

	private void handleAccountNotCreatedState(final AccountManager accountManager,
		final KinCallback<Void> loginCallback) {
		final Observer<Integer> accountStateObserver = new Observer<Integer>() {
			@Override
			public void onChanged(@AccountState Integer value) {
				if (value == AccountManager.ERROR) {
					accountManager.removeAccountStateObserver(this);
					fireStartError(accountManager.getError(), loginCallback);
				} else if (value == AccountManager.CREATION_COMPLETED) {
					accountManager.removeAccountStateObserver(this);
					activateAccount(loginCallback);
				}
			}
		};
		accountManager.addAccountStateObserver(accountStateObserver);
		accountManager.start();
	}

	private void activateAccount(final KinCallback<Void> callback) {
		if (AuthRepository.getInstance().isActivated()) {
			fireStartCompleted(callback);
		} else {
			AuthRepository.getInstance().activateAccount(new KinCallback<Void>() {
				@Override
				public void onResponse(Void response) {
					fireStartCompleted(callback);
				}

				@Override
				public void onFailure(KinEcosystemException error) {
					fireStartError(error, callback);
				}
			});
		}
	}

	private void fireStartCompleted(final KinCallback<Void> loginCallback) {
		isLoggedIn = true;
		executorsUtil.mainThread().execute(new Runnable() {
			@Override
			public void run() {
				loginCallback.onResponse(null);
			}
		});
	}

	private void fireStartError(final KinEcosystemException exception, final KinCallback<Void> loginCallback) {
		isLoggedIn = false;
		executorsUtil.mainThread().execute(new Runnable() {
			@Override
			public void run() {
				loginCallback.onFailure(exception);
			}
		});
	}

	public boolean isInitialized() {
		return isInitialized && isLoggedIn;
	}

}
