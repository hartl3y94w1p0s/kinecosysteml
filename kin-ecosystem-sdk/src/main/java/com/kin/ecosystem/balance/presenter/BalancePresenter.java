package com.kin.ecosystem.balance.presenter;

import static com.kin.ecosystem.main.view.EcosystemActivity.MARKETPLACE;
import static com.kin.ecosystem.main.view.EcosystemActivity.ORDER_HISTORY;
import static kin.ecosystem.core.util.StringUtil.getAmountFormatted;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.kin.ecosystem.balance.view.IBalanceView;
import com.kin.ecosystem.base.BasePresenter;
import com.kin.ecosystem.base.Observer;
import com.kin.ecosystem.data.blockchain.BlockchainSource;
import com.kin.ecosystem.data.model.Balance;
import com.kin.ecosystem.data.offer.OfferDataSource;
import com.kin.ecosystem.data.order.OrderDataSource;
import com.kin.ecosystem.main.view.EcosystemActivity.ScreenId;
import com.kin.ecosystem.network.model.Offer;
import com.kin.ecosystem.network.model.Offer.OfferType;
import com.kin.ecosystem.network.model.Order;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class BalancePresenter extends BasePresenter<IBalanceView> implements IBalancePresenter {

	private static final String BALANCE_ZERO_TEXT = "0.00";

	private final BlockchainSource blockchainSource;
	private final OfferDataSource offerRepository;
	private final OrderDataSource orderRepository;

	private Observer<Balance> balanceObserver;
	private Observer<Offer> pendingOfferObserver;
	private Observer<Order> completedOrderObserver;
	private BalanceClickListener balanceClickListener;

	private String currentPendingOfferID;
	private @OrderStatus int status;

	public static final int PENDING = 0x00000001;
	public static final int TIMEOUT = 0x00000002;
	public static final int COMPLETED = 0x00000003;
	public static final int FAILED = 0x00000004;

	@IntDef({PENDING, TIMEOUT, COMPLETED, FAILED})
	@Retention(RetentionPolicy.SOURCE)
	public @interface OrderStatus {

	}

	public static final int EARN = 0x00000001;
	public static final int SPEND = 0x00000002;

	@IntDef({EARN, SPEND})
	@Retention(RetentionPolicy.SOURCE)
	public @interface OrderType {

	}


	public BalancePresenter(@NonNull IBalanceView view, @Nullable final BlockchainSource blockchainSource,
		@NonNull final OfferDataSource offerRepository,
		@NonNull final OrderDataSource orderRepository) {
		this.view = view;
		this.blockchainSource = blockchainSource;
		this.offerRepository = offerRepository;
		this.orderRepository = orderRepository;
		createBalanceObserver();
		createPendingOfferObserver();
		createCompletedOrderObserver();
	}

	private void createBalanceObserver() {
		balanceObserver = new Observer<Balance>() {
			@Override
			public void onChanged(Balance balance) {
				updateBalance(balance);
			}
		};
	}

	private void updateBalance(Balance balance) {
		int balanceValue = balance.getAmount().intValue();
		String balanceString;
		if (balanceValue == 0) {
			balanceString = BALANCE_ZERO_TEXT;
		} else {
			balanceString = getAmountFormatted(balanceValue);
		}
		if (view != null) {
			view.updateBalance(balanceString);
		}
	}

	private void createPendingOfferObserver() {
		Offer offer = offerRepository.getPendingOffer().getValue();
		updatePendingOffer(offer);
		pendingOfferObserver = new Observer<Offer>() {
			@Override
			public void onChanged(Offer offer) {
				updatePendingOffer(offer);
			}
		};
	}

	private void updatePendingOffer(Offer offer) {
		if (offer != null) {
			currentPendingOfferID = offer.getId();
			status = PENDING;
			updateSubTitle(offer.getAmount(), status, getType(offer.getOfferType()));
		}
	}

	private int getType(OfferType offerType) {
		switch (offerType) {
			case SPEND:
				return SPEND;
			default:
			case EARN:
				return EARN;
		}
	}

	private void createCompletedOrderObserver() {
		completedOrderObserver = new Observer<Order>() {
			@Override
			public void onChanged(Order order) {
				if (order != null && currentPendingOfferID.equals(order.getOfferId())) {
					status = getStatus(order);
					updateSubTitle(order.getAmount(), status, getType(order.getOfferType()));
				}
			}
		};
	}

	private int getStatus(Order order) {
		switch (order.getStatus()) {
			case COMPLETED:
				return COMPLETED;
			case FAILED:
				return FAILED;
			default:
			case PENDING:
				return PENDING;
		}
	}


	private void updateSubTitle(int amount, @OrderStatus int status, @OrderType int offerType) {
		if (view != null) {
			view.updateSubTitle(amount, status, offerType);
		}
	}

	@Override
	public void onAttach(IBalanceView view) {
		super.onAttach(view);
		view.attachPresenter(this);
		showWelcomeToKin();
		addObservers();
	}

	private void showWelcomeToKin() {
		if (view != null) {
			view.setWelcomeSubtitle();
		}
	}

	private void addObservers() {
		offerRepository.getPendingOffer().addObserver(pendingOfferObserver);
		orderRepository.addCompletedOrderObserver(completedOrderObserver);
		blockchainSource.addBalanceObserver(balanceObserver);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		removeObservers();
	}

	private void removeObservers() {
		offerRepository.getPendingOffer().removeObserver(pendingOfferObserver);
		orderRepository.removeCompletedOrderObserver(completedOrderObserver);
		blockchainSource.removeBalanceObserver(balanceObserver);
	}

	@Override
	public void balanceClicked() {
		this.balanceClickListener.onClick();
	}

	@Override
	public void setClickListener(BalanceClickListener balanceClickListener) {
		this.balanceClickListener = balanceClickListener;
	}

	@Override
	public void visibleScreen(@ScreenId int id) {
		switch (id){
			case ORDER_HISTORY:
				if (status == COMPLETED) {
					clearSubTitle();
				}
				break;
			case MARKETPLACE:
				if (status == COMPLETED) {
					showWelcomeToKin();
				}
				break;
		}
	}

	private void clearSubTitle() {
		if (view != null) {
			view.clearSubTitle();
		}
	}
}
