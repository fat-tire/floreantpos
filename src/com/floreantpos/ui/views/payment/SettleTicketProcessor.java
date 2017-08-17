/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.ui.views.payment;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.Set;
import java.util.Vector;

import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.PosLog;
import com.floreantpos.config.CardConfig;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.extension.InginicoPlugin;
import com.floreantpos.extension.PaymentGatewayPlugin;
import com.floreantpos.main.Application;
import com.floreantpos.model.CardReader;
import com.floreantpos.model.CashTransaction;
import com.floreantpos.model.CustomPayment;
import com.floreantpos.model.Discount;
import com.floreantpos.model.GiftCertificateTransaction;
import com.floreantpos.model.Gratuity;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketDiscount;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.report.ReceiptPrintService;
import com.floreantpos.services.PosTransactionService;
import com.floreantpos.ui.dialog.DiscountSelectionDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.TransactionCompletionDialog;
import com.floreantpos.ui.views.order.OrderController;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.util.DrawerUtil;
import com.floreantpos.util.POSUtil;

public class SettleTicketProcessor implements CardInputListener {
	public static PosPaymentWaitDialog waitDialog = new PosPaymentWaitDialog();
	private Vector<PaymentListener> paymentListeners = new Vector<PaymentListener>(3);
	private double tenderAmount;
	private PaymentType paymentType;
	private Ticket ticket;
	private String cardName;
	private User currentUser;

	public SettleTicketProcessor(User currentUser) {
		super();
		this.currentUser = currentUser;
	}

	public void doSettle(PaymentType paymentType, double tenderAmount) {
		doSettle(paymentType, tenderAmount, null);
	}

	public void doSettle(PaymentType paymentType, double tenderAmount, CustomPayment customPayment) {
		try {
			this.tenderAmount = tenderAmount;
			this.paymentType = paymentType;
			if (ticket == null)
				return;
			if (ticket.getTicketItems() == null || ticket.getTicketItems().size() == 0) {
				POSMessageDialog.showError(POSUtil.getFocusedWindow(), com.floreantpos.POSConstants.TICKET_IS_EMPTY_);
				return;
			}

			/*if (ticket.getOrderType().isBarTab()) { //fix
				doSettleBarTabTicket(ticket);
				return;
			}*/

			if (ticket.getId() == null) {
				OrderController.saveOrder(ticket);
			}

			cardName = paymentType.getDisplayString();
			PosTransaction transaction = null;

			switch (paymentType) {
				case CASH:
					if (!confirmPayment()) {
						return;
					}

					transaction = paymentType.createTransaction();
					transaction.setTicket(ticket);
					transaction.setCaptured(true);
					setTransactionAmounts(transaction);

					settleTicket(transaction);
					break;

				case CUSTOM_PAYMENT:

					CustomPaymentSelectionDialog customPaymentDialog = new CustomPaymentSelectionDialog();
					customPaymentDialog.setTitle(Messages.getString("SettleTicketDialog.8")); //$NON-NLS-1$
					customPaymentDialog.pack();
					customPaymentDialog.open();

					if (customPaymentDialog.isCanceled())
						return;

					if (!confirmPayment()) {
						return;
					}

					transaction = paymentType.createTransaction();
					transaction.setCustomPaymentFieldName(customPaymentDialog.getPaymentFieldName());
					transaction.setCustomPaymentName(customPaymentDialog.getPaymentName());
					transaction.setCustomPaymentRef(customPaymentDialog.getPaymentRef());
					transaction.setTicket(ticket);
					transaction.setCaptured(true);
					setTransactionAmounts(transaction);

					settleTicket(transaction);
					break;

				case CREDIT_CARD:
				case CREDIT_VISA:
				case CREDIT_MASTER_CARD:
				case CREDIT_AMEX:
				case CREDIT_DISCOVERY:
					payUsingCard(cardName, tenderAmount);
					break;

				case DEBIT_CARD:
				case DEBIT_VISA:
				case DEBIT_MASTER_CARD:
					payUsingCard(cardName, tenderAmount);
					break;

				case GIFT_CERTIFICATE:
					GiftCertDialog giftCertDialog = new GiftCertDialog();
					giftCertDialog.pack();
					giftCertDialog.open();

					if (giftCertDialog.isCanceled())
						return;

					transaction = new GiftCertificateTransaction();
					transaction.setPaymentType(PaymentType.GIFT_CERTIFICATE.name());
					transaction.setTicket(ticket);
					transaction.setCaptured(true);
					setTransactionAmounts(transaction);

					double giftCertFaceValue = giftCertDialog.getGiftCertFaceValue();
					double giftCertCashBackAmount = 0;
					transaction.setTenderAmount(giftCertFaceValue);

					if (giftCertFaceValue >= ticket.getDueAmount()) {
						transaction.setAmount(ticket.getDueAmount());
						giftCertCashBackAmount = giftCertFaceValue - ticket.getDueAmount();
					}
					else {
						transaction.setAmount(giftCertFaceValue);
					}

					transaction.setGiftCertNumber(giftCertDialog.getGiftCertNumber());
					transaction.setGiftCertFaceValue(giftCertFaceValue);
					transaction.setGiftCertPaidAmount(transaction.getAmount());
					transaction.setGiftCertCashBackAmount(giftCertCashBackAmount);

					settleTicket(transaction);
					break;

				default:
					break;
			}

		} catch (Exception e) {
			PosLog.error(getClass(), e);
		}
	}

	public void doApplyCoupon() {// GEN-FIRST:event_btnApplyCoupondoApplyCoupon
		try {
			if (ticket == null) {
				return;
			}
			if (ticket.getId() == null) {
				OrderController.saveOrder(ticket);
			}
			//			ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());

			if (!Application.getCurrentUser().hasPermission(UserPermission.ADD_DISCOUNT)) {
				POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("SettleTicketDialog.7")); //$NON-NLS-1$
				return;
			}
			DiscountSelectionDialog dialog = new DiscountSelectionDialog(ticket);
			dialog.open();

			if (dialog.isCanceled()) {
				return;
			}

			updateModel();
			doInformListenerPaymentUpdate();
			OrderController.saveOrder(ticket);
			if (OrderView.getInstance().isVisible())
				OrderView.getInstance().setCurrentTicket(ticket);
		} catch (Exception e) {
			POSMessageDialog.showError(POSUtil.getFocusedWindow(), com.floreantpos.POSConstants.ERROR_MESSAGE, e);
		}
	}

	private void doInformListenerPaymentUpdate() {
		for (PaymentListener paymentListener : paymentListeners) {
			paymentListener.paymentDataChanged();
		}
	}

	public void doSetGratuity() {
		if (ticket == null)
			return;

		GratuityInputDialog d = new GratuityInputDialog();
		d.pack();
		d.setResizable(false);
		d.open();

		if (d.isCanceled()) {
			return;
		}

		double gratuityAmount = d.getGratuityAmount();
		Gratuity gratuity = ticket.createGratuity();
		gratuity.setAmount(gratuityAmount);

		ticket.setGratuity(gratuity);
		ticket.calculatePrice();
		OrderController.saveOrder(ticket);
		doInformListenerPaymentUpdate();
	}

	public void doSettleBarTabTicket(Ticket ticket) {
		try {
			String msg = "Do you want to settle ticket?"; //$NON-NLS-1$
			int option1 = POSMessageDialog.showYesNoQuestionDialog(null, msg, Messages.getString("NewBarTabAction.4")); //$NON-NLS-1$
			if (option1 != JOptionPane.YES_OPTION) {
				return;
			}
			else {
				for (PosTransaction barTabTransaction : ticket.getTransactions()) {
					barTabTransaction.setAmount(ticket.getDueAmount());
					barTabTransaction.setTenderAmount(ticket.getDueAmount());
					barTabTransaction.setAuthorizable(true);
					settleTicket(barTabTransaction);
				}
			}
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage(), e);
		}
	}

	public static void showTransactionCompleteMsg(final double dueAmount, final double tenderedAmount, Ticket ticket, PosTransaction transaction) {
		TransactionCompletionDialog dialog = new TransactionCompletionDialog(transaction);
		dialog.setCompletedTransaction(transaction);
		dialog.setTenderedAmount(tenderedAmount);
		dialog.setTotalAmount(dueAmount);
		dialog.setPaidAmount(transaction.getAmount());
		dialog.setDueAmount(ticket.getDueAmount());
		if (tenderedAmount > transaction.getAmount()) {
			dialog.setChangeAmount(tenderedAmount - transaction.getAmount());
		}
		else {
			dialog.setChangeAmount(0);
		}

		dialog.updateView();
		dialog.pack();
		dialog.open();
	}

	public static void printTicket(Ticket ticket, PosTransaction transaction) {
		try {
			if (ticket.getOrderType().isShouldPrintToKitchen()) {
				if (ticket.needsKitchenPrint()) {
					ReceiptPrintService.printToKitchen(ticket);
				}
			}

			ReceiptPrintService.printTransaction(transaction);

			if (transaction instanceof CashTransaction) {
				DrawerUtil.kickDrawer();
			}
		} catch (Exception ee) {
			POSMessageDialog.showError(Application.getPosWindow(), com.floreantpos.POSConstants.PRINT_ERROR, ee);
		}
	}

	private void addCoupon(Ticket ticket, JsonObject jsonObject) {
		Set<String> keys = jsonObject.keySet();
		for (String key : keys) {
			JsonNumber jsonNumber = jsonObject.getJsonNumber(key);
			double doubleValue = jsonNumber.doubleValue();

			TicketDiscount coupon = new TicketDiscount();
			coupon.setName(key);
			coupon.setType(Discount.FIXED_PER_ORDER);
			coupon.setValue(doubleValue);

			ticket.addTodiscounts(coupon);
		}
	}

	private void updateModel() {
		if (ticket == null) {
			return;
		}
		ticket.calculatePrice();
	}

	private void payUsingCard(String cardName, final double tenderedAmount) throws Exception {
		try {
			//		if (!CardConfig.getMerchantGateway().isCardTypeSupported(cardName)) {
			//			POSMessageDialog.showError(Application.getPosWindow(), "<html>Card <b>" + cardName + "</b> not supported.</html>");
			//			return;
			//		}

			PaymentGatewayPlugin paymentGateway = CardConfig.getPaymentGateway();

			if (paymentGateway instanceof InginicoPlugin) {
				waitDialog.setVisible(true);
				if (!waitDialog.isCanceled()) {
					doInformListenerPaymentDone();
				}
				return;
			}
			if (!paymentGateway.shouldShowCardInputProcessor()) {

				PosTransaction transaction = paymentType.createTransaction();
				transaction.setTicket(ticket);

				if (!confirmPayment()) {
					return;
				}

				//transaction.setCardType(cardName);
				transaction.setCaptured(false);
				transaction.setCardMerchantGateway(paymentGateway.getProductName());

				setTransactionAmounts(transaction);

				if (ticket.getOrderType().isPreAuthCreditCard()) {
					paymentGateway.getProcessor().preAuth(transaction);
				}
				else {
					paymentGateway.getProcessor().chargeAmount(transaction);
				}

				settleTicket(transaction);

				return;
			}

			CardReader cardReader = CardConfig.getCardReader();
			switch (cardReader) {
				case SWIPE:
					SwipeCardDialog swipeCardDialog = new SwipeCardDialog(this);
					swipeCardDialog.pack();
					swipeCardDialog.open();
					break;

				case MANUAL:
					ManualCardEntryDialog dialog = new ManualCardEntryDialog(this);
					dialog.pack();
					dialog.open();
					break;

				case EXTERNAL_TERMINAL:
					AuthorizationCodeDialog authorizationCodeDialog = new AuthorizationCodeDialog(this);
					authorizationCodeDialog.pack();
					authorizationCodeDialog.open();
					break;

				default:
					break;
			}
		} catch (Exception e) {
			POSMessageDialog.showError(POSUtil.getFocusedWindow(), e.getMessage(), e);
		}

	}

	public void settleTicket(PosTransaction transaction) {
		try {
			final double dueAmount = ticket.getDueAmount();

			confirmLoyaltyDiscount(ticket);

			PosTransactionService transactionService = PosTransactionService.getInstance();
			transactionService.settleTicket(ticket, transaction);

			//FIXME
			printTicket(ticket, transaction);

			showTransactionCompleteMsg(dueAmount, transaction.getTenderAmount(), ticket, transaction);

			if (ticket.getDueAmount() > 0.0) {
				int option = JOptionPane.showConfirmDialog(Application.getPosWindow(), POSConstants.CONFIRM_PARTIAL_PAYMENT, POSConstants.MDS_POS,
						JOptionPane.YES_NO_OPTION);

				if (option != JOptionPane.YES_OPTION) {

					doInformListenerPaymentDone();
				}

				setTicket(ticket);
				doInformListenerPaymentUpdate();
			}
			else {
				doInformListenerPaymentDone();
			}
		} catch (UnknownHostException e) {
			POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("SettleTicketDialog.12")); //$NON-NLS-1$
		} catch (Exception e) {
			POSMessageDialog.showError(POSUtil.getFocusedWindow(), POSConstants.ERROR_MESSAGE, e);
		}
	}

	public void confirmLoyaltyDiscount(Ticket ticket) throws IOException, MalformedURLException {
		//		try {
		//			if (ticket.hasProperty(LOYALTY_ID)) {
		//				String url = buildLoyaltyApiURL(ticket, ticket.getProperty(LOYALTY_ID));
		//				url += "&paid=1"; //$NON-NLS-1$
		//
		//				IOUtils.toString(new URL(url).openStream());
		//			}
		//		} catch (Exception e) {
		//			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage(), e);
		//		}
	}

	private void setTransactionAmounts(PosTransaction transaction) {
		transaction.setTenderAmount(tenderAmount);

		if (tenderAmount >= ticket.getDueAmount()) {
			transaction.setAmount(ticket.getDueAmount());
		}
		else {
			transaction.setAmount(tenderAmount);
		}
	}

	private boolean confirmPayment() {
		if (!TerminalConfig.isUseSettlementPrompt()) {
			return true;
		}

		ConfirmPayDialog confirmPayDialog = new ConfirmPayDialog();
		confirmPayDialog.setAmount(tenderAmount);
		confirmPayDialog.open();

		if (confirmPayDialog.isCanceled()) {
			return false;
		}

		return true;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	@Override
	public void cardInputted(CardInputProcessor inputter, PaymentType selectedPaymentType) {
		//authorize only, do not capture
		PaymentProcessWaitDialog waitDialog = new PaymentProcessWaitDialog(Application.getPosWindow());
		try {

			waitDialog.setVisible(true);

			PosTransaction transaction = paymentType.createTransaction();
			transaction.setTicket(ticket);

			PaymentGatewayPlugin paymentGateway = CardConfig.getPaymentGateway();
			CardProcessor cardProcessor = paymentGateway.getProcessor();

			if (inputter instanceof SwipeCardDialog) {

				SwipeCardDialog swipeCardDialog = (SwipeCardDialog) inputter;
				String cardString = swipeCardDialog.getCardString();

				if (StringUtils.isEmpty(cardString) || cardString.length() < 16) {
					throw new RuntimeException(Messages.getString("SettleTicketDialog.16")); //$NON-NLS-1$
				}

				if (!confirmPayment()) {
					return;
				}
				transaction.setCardType(paymentType.getDisplayString());
				transaction.setCardTrack(cardString);
				transaction.setCaptured(false);
				transaction.setCardMerchantGateway(paymentGateway.getProductName());
				transaction.setCardReader(CardReader.SWIPE.name());
				setTransactionAmounts(transaction);

				if (ticket.getOrderType().isPreAuthCreditCard()) {//OK
					cardProcessor.preAuth(transaction);
				}
				else {
					cardProcessor.chargeAmount(transaction);
				}

				settleTicket(transaction);
			}
			else if (inputter instanceof ManualCardEntryDialog) {

				ManualCardEntryDialog mDialog = (ManualCardEntryDialog) inputter;

				transaction.setCaptured(false);
				transaction.setCardMerchantGateway(paymentGateway.getProductName());
				transaction.setCardReader(CardReader.MANUAL.name());
				transaction.setCardNumber(mDialog.getCardNumber());
				transaction.setCardExpMonth(mDialog.getExpMonth());
				transaction.setCardExpYear(mDialog.getExpYear());
				setTransactionAmounts(transaction);

				//cardProcessor.preAuth(transaction);
				if (ticket.getOrderType().isPreAuthCreditCard()) {//OK
					cardProcessor.preAuth(transaction);
				}
				else {
					cardProcessor.chargeAmount(transaction);
				}

				settleTicket(transaction);
			}
			else if (inputter instanceof AuthorizationCodeDialog) {

				PosTransaction selectedTransaction = selectedPaymentType.createTransaction();
				selectedTransaction.setTicket(ticket);

				AuthorizationCodeDialog authDialog = (AuthorizationCodeDialog) inputter;
				String authorizationCode = authDialog.getAuthorizationCode();
				if (StringUtils.isEmpty(authorizationCode)) {
					throw new PosException(Messages.getString("SettleTicketDialog.17")); //$NON-NLS-1$
				}

				selectedTransaction.setCardType(selectedPaymentType.getDisplayString());
				selectedTransaction.setCaptured(true);
				selectedTransaction.setCardReader(CardReader.EXTERNAL_TERMINAL.name());
				selectedTransaction.setCardAuthCode(authorizationCode);
				setTransactionAmounts(selectedTransaction);

				settleTicket(selectedTransaction);
			}
		} catch (Exception e) {
			PosLog.error(getClass(), e);
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage());
		} finally {
			waitDialog.setVisible(false);
		}
	}

	public void addPaymentListener(PaymentListener paymentListener) {
		this.paymentListeners.add(paymentListener);
	}

	public void removePaymentListener(PaymentListener paymentListener) {
		this.paymentListeners.remove(paymentListener);
	}

	public void cancelPayment() {
		for (PaymentListener paymentListener : paymentListeners) {
			paymentListener.paymentCanceled();
		}
	}

	private void doInformListenerPaymentDone() {
		for (PaymentListener paymentListener : paymentListeners) {
			paymentListener.paymentDone();
		}
	}

}
