package kin.devplatform.data.blockchain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.kin.ecosystem.recovery.KeyStoreProvider;
import java.math.BigDecimal;
import kin.core.KinAccount;
import kin.devplatform.KinCallback;
import kin.devplatform.base.Observer;
import kin.devplatform.data.model.Balance;
import kin.devplatform.data.model.Payment;
import kin.devplatform.exception.BlockchainException;
import kin.devplatform.exception.ClientException;
import kin.devplatform.network.model.Offer.OfferType;

public interface BlockchainSource {

	/**
	 * Create account if there is no accounts in local
	 *
	 * @throws BlockchainException could not load the account, or could not create a new account.
	 */
	void loadOrCreateAccount() throws BlockchainException;

	/**
	 * Getting the current account.
	 */
	@Nullable
	KinAccount getKinAccount();

	/**
	 * @param appID - appID - will be included in the memo for each transaction.
	 */
	void setAppID(String appID);

	/**
	 * Send transaction to the network
	 *
	 * @param publicAddress the recipient address
	 * @param amount the amount to send
	 * @param orderID the orderID to be included in the memo of the transaction
	 */
	void sendTransaction(@NonNull String publicAddress, @NonNull BigDecimal amount,
		@NonNull String orderID, @NonNull String offerID, OfferType offerType);

	/**
	 * @return the cached balance.
	 */
	Balance getBalance();

	/**
	 * Get balance from network
	 */
	void getBalance(@Nullable final KinCallback<Balance> callback);

	/**
	 * Add balance observer in order to start receive balance updates
	 */
	void addBalanceObserver(@NonNull final Observer<Balance> observer);

	/**
	 * Add balance observer that will keep a connection on account balance updates from the blockchain network.
	 */
	void addBalanceObserverAndStartListen(@NonNull final Observer<Balance> observer);

	/**
	 * Remove the balance observer in order to stop receiving balance updates.
	 */
	void removeBalanceObserver(@NonNull final Observer<Balance> observer);

	/**
	 * Remove the balance observer, and close the connection if no other observers.
	 */
	void removeBalanceObserverAndStopListen(@NonNull final Observer<Balance> observer);

	/**
	 * @return the public address of the initiated account
	 */
	String getPublicAddress() throws ClientException, BlockchainException;

	/**
	 * @return the public address of the account with {@param accountIndex}
	 */
	String getPublicAddress(final int accountIndex);

	/**
	 * Add {@link Payment} completed observer.
	 */
	void addPaymentObservable(Observer<Payment> observer);

	/**
	 * Remove the payment observer to stop listening for completed payments.
	 */
	void removePaymentObserver(Observer<Payment> observer);

	/**
	 * Create trustline polling call, so it will try few time before failure.
	 */
	void createTrustLine(@NonNull final KinCallback<Void> callback);

	/**
	 * Creates the {@link KeyStoreProvider} to use in backup and restore flow.
	 *
	 * @return {@link KeyStoreProvider}
	 */
	KeyStoreProvider getKeyStoreProvider();

	boolean updateActiveAccount(int accountIndex) throws BlockchainException;

	interface Local {

		int getBalance();

		void setBalance(int balance);

		int getAccountIndex();

		void setAccountIndex(int index);
	}
}
