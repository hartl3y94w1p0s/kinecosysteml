package com.kin.ecosystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.kin.ecosystem.base.ObservableData;
import com.kin.ecosystem.base.Observer;
import com.kin.ecosystem.data.auth.AuthLocalData;
import com.kin.ecosystem.data.auth.AuthRemoteData;
import com.kin.ecosystem.data.auth.AuthRepository;
import com.kin.ecosystem.data.blockchain.BlockchainSourceImpl;
import com.kin.ecosystem.data.blockchain.BlockchainSourceLocal;
import com.kin.ecosystem.data.model.Balance;
import com.kin.ecosystem.data.model.OrderConfirmation;
import com.kin.ecosystem.data.model.WhitelistData;
import com.kin.ecosystem.data.offer.OfferRemoteData;
import com.kin.ecosystem.data.offer.OfferRepository;
import com.kin.ecosystem.data.order.OrderLocalData;
import com.kin.ecosystem.data.order.OrderRemoteData;
import com.kin.ecosystem.data.order.OrderRepository;
import com.kin.ecosystem.exception.InitializeException;
import com.kin.ecosystem.exception.TaskFailedException;
import com.kin.ecosystem.marketplace.model.NativeSpendOffer;
import com.kin.ecosystem.marketplace.view.MarketplaceActivity;
import com.kin.ecosystem.network.model.SignInData;
import com.kin.ecosystem.network.model.SignInData.SignInTypeEnum;
import com.kin.ecosystem.splash.view.SplashViewActivity;
import com.kin.ecosystem.util.DeviceUtils;
import com.kin.ecosystem.util.ExecutorsUtil;
import java.util.UUID;
import kin.core.KinClient;
import kin.core.ServiceProvider;


public class Kin {

	private static final String KIN_ECOSYSTEM_STORE_PREFIX_KEY = "kinecosystem_store";
	private static Kin instance;

    private final ExecutorsUtil executorsUtil;

    private Kin() {
        executorsUtil = new ExecutorsUtil();

    }

    private static Kin getInstance() {
        if (instance == null) {
            synchronized (Kin.class) {
                instance = new Kin();
            }
        }

        return instance;
    }

    public static void start(@NonNull Context appContext, @NonNull WhitelistData whitelistData)
        throws InitializeException {
        if (isInstanceNull()) {
            SignInData signInData = getWhiteListSignInData(whitelistData);
            init(appContext, signInData);
        }
    }

	/**
	 * Configure the environment you want to use.
	 * Default environment is {@link KinEnvironment#PLAYGROUND}
	 *
	 * @param environment {@link KinEnvironment#PRODUCTION} or {@link KinEnvironment#PLAYGROUND}
	 */
	public static void setEnvironment(@NonNull KinEnvironment environment) {
		if (isInstanceNull()) {
			Configuration.setEnvironment(environment);
		}
	}

    public static void start(@NonNull Context appContext, @NonNull String jwt) throws InitializeException {
        if (isInstanceNull()) {
            SignInData signInData = getJwtSignInData(jwt);
            init(appContext, signInData);
        }
    }

    private static SignInData getWhiteListSignInData(@NonNull final WhitelistData whitelistData) {
        SignInData signInData = new SignInData()
            .signInType(SignInTypeEnum.WHITELIST)
            .userId(whitelistData.getUserID())
            .appId(whitelistData.getAppID())
            .apiKey(whitelistData.getApiKey());

        return signInData;
    }

    private static SignInData getJwtSignInData(@NonNull final String jwt) {
        SignInData signInData = new SignInData()
            .signInType(SignInTypeEnum.JWT)
            .jwt(jwt);

        return signInData;
    }

    private static void init(@NonNull Context appContext, @NonNull SignInData signInData) throws InitializeException {
        instance = getInstance();
        appContext = appContext.getApplicationContext(); // use application context to avoid leaks.
        DeviceUtils.init(appContext);
        initBlockchain(appContext);
        registerAccount(appContext, signInData);
        initOfferRepository();
        initOrderRepository(appContext);
        setAppID();
    }


    private static void setAppID() {
        ObservableData<String> observableData = AuthRepository.getInstance().getAppID();
        String appID = observableData.getValue();
        observableData.addObserver(new Observer<String>() {
            @Override
            public void onChanged(String appID) {
                BlockchainSourceImpl.getInstance().setAppID(appID);
            }
        });

        BlockchainSourceImpl.getInstance().setAppID(appID);
    }

	private static void initBlockchain(Context context) throws InitializeException {
		final String networkUrl = Configuration.getEnvironment().getBlockchainNetworkUrl();
		final String networkId = Configuration.getEnvironment().getBlockchainPassphrase();
		KinClient kinClient = new KinClient(context, new ServiceProvider(networkUrl, networkId) {
			@Override
			protected String getIssuerAccountId() {
				return Configuration.getEnvironment().getIssuer();
			}
		}, KIN_ECOSYSTEM_STORE_PREFIX_KEY);
		BlockchainSourceImpl.init(kinClient, BlockchainSourceLocal.getInstance(context));
	}

    private static void registerAccount(@NonNull final Context context, @NonNull final SignInData signInData)
        throws InitializeException {
        String publicAddress;
        try {
            AuthRepository.init(AuthLocalData.getInstance(context, instance.executorsUtil),
                AuthRemoteData.getInstance(instance.executorsUtil));
            if (AuthRepository.getInstance().getDeviceID() == null) {
                signInData.setDeviceId(UUID.randomUUID().toString());
            }
            publicAddress = getPublicAddress();
            signInData.setWalletAddress(publicAddress);
            AuthRepository.getInstance().setSignInData(signInData);
        } catch (TaskFailedException e) {
            throw new InitializeException(e.getMessage());
        }
    }

    private static void initOfferRepository() {
        OfferRepository.init(OfferRemoteData.getInstance(instance.executorsUtil));
        OfferRepository.getInstance().getOffers(null);
    }

    private static void initOrderRepository(@NonNull final Context context) {
        OrderRepository.init(BlockchainSourceImpl.getInstance(), OfferRepository.getInstance(),
            OrderRemoteData.getInstance(instance.executorsUtil),
            OrderLocalData.getInstance(context, instance.executorsUtil));
    }

    private static boolean isInstanceNull() {
        return instance == null;
    }

    private static void checkInstanceNotNull() throws TaskFailedException {
        if (isInstanceNull()) {
            throw new TaskFailedException("Kin.start(...) should be called first");
        }
    }

    /**
     * Launch Kin Marketplace if the user is activated, otherwise it will launch Welcome to Kin page.
     *
     * @param activity the activity user can go back to.
     * @throws TaskFailedException
     */
    public static void launchMarketplace(@NonNull final Activity activity) throws TaskFailedException {
        checkInstanceNotNull();
        boolean isActivated = AuthRepository.getInstance().isActivated();
        if (isActivated) {
            navigateToMarketplace(activity);
        } else {
            navigateToSplash(activity);
        }
    }

    private static void navigateToSplash(@NonNull final Activity activity) {
        activity.startActivity(new Intent(activity, SplashViewActivity.class));
        activity.overridePendingTransition(R.anim.kinecosystem_slide_in_right, R.anim.kinecosystem_slide_out_left);
    }

    private static void navigateToMarketplace(@NonNull final Activity activity) {
        activity.startActivity(new Intent(activity, MarketplaceActivity.class));
        activity.overridePendingTransition(R.anim.kinecosystem_slide_in_right, R.anim.kinecosystem_slide_out_left);
    }

    /**
     * @return The account public address
     * @throws TaskFailedException
     */
    public static String getPublicAddress() throws TaskFailedException {
        checkInstanceNotNull();
        return BlockchainSourceImpl.getInstance().getPublicAddress();
    }

    /**
     * Get the cached balance, can be different from the current balance on the network.
     *
     * @return balance amount
     * @throws TaskFailedException
     */
    public static Balance getCachedBalance() throws TaskFailedException {
        checkInstanceNotNull();
        return BlockchainSourceImpl.getInstance().getBalance();
    }

    /**
     * Get the current account balance from the network.
     *
     * @param callback balance amount
     * @throws TaskFailedException
     */
    public static void getBalance(@NonNull final Callback<Balance> callback) throws TaskFailedException {
        checkInstanceNotNull();
        BlockchainSourceImpl.getInstance().getBalance(callback);
    }

    /**
     * Add balance observer to start getting notified when the balance is changed on the blockchain network.
     * On balance changes you will get {@link Balance} with the balance amount.
     *
     * Take in consideration that on adding this observer, a live network connection will be open to the blockchain network,
     * In order to close the connection use {@link #removeBalanceObserver(Observer)} with the same observer.
     * If no other observers on this connection, the connection will be closed.
     *
     * @param observer
     * @throws TaskFailedException
     */
    public static void addBalanceObserver(@NonNull final Observer<Balance> observer) throws TaskFailedException {
        checkInstanceNotNull();
        BlockchainSourceImpl.getInstance().addBalanceObserverAndStartListen(observer);
    }

    /**
     *  Remove the balance observer, this method will close the live network connection to the blockchain network
     *  if there is no more observers.
     *
     * @param observer
     * @throws TaskFailedException
     */
    public static void removeBalanceObserver(@NonNull final Observer<Balance> observer) throws TaskFailedException {
        checkInstanceNotNull();
        BlockchainSourceImpl.getInstance().removeBalanceObserverAndStopListen(observer);
    }

    /**
     * Allowing your users to purchase virtual goods you define within your app, using KIN.
     * This call might take time, due to transaction validation on the blockchain network.
     *
     * @param offerJwt Represents the offer in a JWT manner.
     * @param callback Confirmation callback, the result will be a failure or a succeed with a jwt confirmation.
     */
    public static void purchase(String offerJwt, @Nullable Callback<OrderConfirmation> callback)
        throws TaskFailedException {
        checkInstanceNotNull();
        OrderRepository.getInstance().purchase(offerJwt, callback);
    }

    /**
     * Allowing your users to earn Kin as a reward for native task you define.
     * This call might take time, due to transaction validation on the blockchain network.
     *
     * @param offerJwt the offer details represented in a JWT manner.
     * @param callback after validating the info and sending the payment to the user, you will receive {@link OrderConfirmation},
     * with the jwtConfirmation and you can validate the order when the order status is completed.
     * @throws TaskFailedException
     */
    public static void requestPayment(String offerJwt, @Nullable Callback<OrderConfirmation> callback)
        throws TaskFailedException {
        checkInstanceNotNull();
        OrderRepository.getInstance().requestPayment(offerJwt, callback);
    }

    /**
     * Returns a {@link OrderConfirmation}, with the order status and a jwtConfirmation if the order is completed.
     *
     * @param offerID The offerID that this order created from
     */
    public static void getOrderConfirmation(@NonNull String offerID, @NonNull Callback<OrderConfirmation> callback)
        throws TaskFailedException {
        checkInstanceNotNull();
        OrderRepository.getInstance().getExternalOrderStatus(offerID, callback);
    }

    /**
     * Add a native offer {@link Observer} to receive a trigger when you native offers on Kin Marketplace are clicked.
     */
    public static void addNativeOfferClickedObserver(@NonNull Observer<NativeSpendOffer> observer)
        throws TaskFailedException {
        checkInstanceNotNull();
        OfferRepository.getInstance().addNativeOfferClickedObserver(observer);
    }

    /**
     * Remove the callback if you no longer want to get triggered when your offer on Kin marketplace are clicked.
     */
    public static void removeNativeOfferClickedObserver(@NonNull Observer<NativeSpendOffer> observer)
        throws TaskFailedException {
        checkInstanceNotNull();
        OfferRepository.getInstance().removeNativeOfferClickedObserver(observer);
    }

    /**
     * Adds an {@link NativeSpendOffer} to spend offer list on Kin Marketplace activity.
     * The offer will be added at index 0 in the spend list.
     *
     * @param nativeSpendOffer The spend offer you want to add to the spend list.
     * @return true if the offer added successfully, the list was changed.
     * @throws TaskFailedException Could not add the offer to the list.
     */
    public static boolean addNativeOffer(@NonNull NativeSpendOffer nativeSpendOffer) throws TaskFailedException {
        checkInstanceNotNull();
        return OfferRepository.getInstance().addNativeOffer(nativeSpendOffer);
    }

    /**
     * Removes a {@link NativeSpendOffer} from the spend list on Kin Marketplace activity.
     *
     * @param nativeSpendOffer The spend offer you want to remove from the spend list.
     * @return true if the offer removed successfully, the list was changed.
     * @throws TaskFailedException Could not remove the offer from the list.
     */
    public static boolean removeNativeOffer(@NonNull NativeSpendOffer nativeSpendOffer) throws TaskFailedException {
        checkInstanceNotNull();
        return OfferRepository.getInstance().removeNativeOffer(nativeSpendOffer);
    }
}
