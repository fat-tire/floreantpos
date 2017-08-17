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

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SerializationUtils;
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
import com.floreantpos.model.GiftCertificateTransaction;
import com.floreantpos.model.Gratuity;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Ticket;
import com.floreantpos.report.ReceiptPrintService;
import com.floreantpos.services.PosTransactionService;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.TransactionCompletionDialog;
import com.floreantpos.ui.views.TicketDetailView;
import com.floreantpos.ui.views.order.OrderController;
import com.floreantpos.util.CurrencyUtil;
import com.floreantpos.util.DrawerUtil;
import com.floreantpos.util.GlobalIdGenerator;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.POSUtil;

//TODO: REVISE CODE
public class GroupSettleTicketDialog extends POSDialog implements CardInputListener {
	public static final String LOYALTY_DISCOUNT_PERCENTAGE = "loyalty_discount_percentage"; //$NON-NLS-1$
	public static final String LOYALTY_POINT = "loyalty_point"; //$NON-NLS-1$
	public static final String LOYALTY_COUPON = "loyalty_coupon"; //$NON-NLS-1$
	public static final String LOYALTY_DISCOUNT = "loyalty_discount"; //$NON-NLS-1$
	public static final String LOYALTY_ID = "loyalty_id"; //$NON-NLS-1$

	public final static String VIEW_NAME = "PAYMENT_VIEW"; //$NON-NLS-1$

	private GroupPaymentView paymentView;
	private List<Ticket> tickets;
	private TicketDetailView ticketDetailView;
	private javax.swing.JScrollPane ticketScrollPane;
	private Ticket ticket;
	private double totalTenderAmount;
	private PaymentType paymentType;
	private String cardName;
	private JTextField tfSubtotal;
	private JTextField tfDiscount;
	private JTextField tfDeliveryCharge;
	private JTextField tfTax;
	private JTextField tfTotal;
	private JTextField tfGratuity;

	private String ticketNumbers = "";
	private List<Integer> tableNumbers = new ArrayList<Integer>();
	private String customerName;
	private double totalDueAmount;

	private JLabel lblCustomer;
	private JLabel lblTable;

	private JLabel labelTicketNumber;
	private JLabel labelTableNumber;
	private JLabel labelCustomer;

	public static PosPaymentWaitDialog waitDialog = new PosPaymentWaitDialog();

	public GroupSettleTicketDialog(List<Ticket> tickets) {
		super();
		this.tickets = tickets;

		for (Ticket ticket : tickets) {
			if (ticket.getOrderType().isConsolidateItemsInReceipt()) {
				ticket.consolidateTicketItems();
			}
		}

		setTitle(Messages.getString("SettleTicketDialog.6")); //$NON-NLS-1$
		getContentPane().setLayout(new BorderLayout());

		ticketDetailView = new TicketDetailView();
		ticketScrollPane = new PosScrollPane(ticketDetailView);

		JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0));

		centerPanel.add(createTicketInfoPanel(), BorderLayout.NORTH);
		centerPanel.add(ticketScrollPane, BorderLayout.CENTER);
		centerPanel.add(createTotalViewerPanel(), BorderLayout.SOUTH);

		paymentView = new GroupPaymentView(this);
		paymentView.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		getContentPane().add(centerPanel, BorderLayout.CENTER);
		getContentPane().add(paymentView, BorderLayout.EAST);

		setSize(Application.getPosWindow().getSize());
		updateView();
		paymentView.updateView();
		paymentView.setDefaultFocus();

	}

	public void updateView() {
		if (tickets == null && !tickets.isEmpty()) {
			tfSubtotal.setText(""); //$NON-NLS-1$
			tfDiscount.setText(""); //$NON-NLS-1$
			tfDeliveryCharge.setText(""); //$NON-NLS-1$
			tfTax.setText(""); //$NON-NLS-1$
			tfTotal.setText(""); //$NON-NLS-1$
			tfGratuity.setText(""); //$NON-NLS-1$
			return;
		}
		double subtotalAmount = 0;
		double discountAmount = 0;
		double deliveryCharge = 0;
		double taxAmount = 0;
		double gratuityAmount = 0;
		double totalAmount = 0;

		for (Ticket ticket : tickets) {
			subtotalAmount += ticket.getSubtotalAmount();
			discountAmount += ticket.getDiscountAmount();
			deliveryCharge += ticket.getDeliveryCharge();
			taxAmount += ticket.getTaxAmount();
			if (ticket.getGratuity() != null) {
				gratuityAmount = +ticket.getGratuity().getAmount();
			}
			totalAmount += ticket.getTotalAmount();

			totalDueAmount += ticket.getDueAmount();

			ticketNumbers += "[" + ticket.getId().toString() + "], ";
			for (Integer tableNumber : ticket.getTableNumbers()) {
				if (!tableNumbers.contains(tableNumber)) {
					tableNumbers.add(tableNumber);
				}
			}
			customerName = ticket.getProperty(Ticket.CUSTOMER_NAME);
		}

		tfSubtotal.setText(NumberUtil.formatNumber(subtotalAmount));
		tfDiscount.setText(NumberUtil.formatNumber(discountAmount));

		tfDeliveryCharge.setText(NumberUtil.formatNumber(deliveryCharge));

		if (Application.getInstance().isPriceIncludesTax()) {
			tfTax.setText(Messages.getString("TicketView.35")); //$NON-NLS-1$
		}
		else {
			tfTax.setText(NumberUtil.formatNumber(taxAmount));
		}
		if (gratuityAmount > 0) {
			tfGratuity.setText(NumberUtil.formatNumber(gratuityAmount));
		}
		else {
			tfGratuity.setText("0.00"); //$NON-NLS-1$
		}
		tfTotal.setText(NumberUtil.formatNumber(totalAmount));

		labelTicketNumber.setText(ticketNumbers.substring(0, ticketNumbers.length() - 2));
		labelTableNumber.setText(tableNumbers.toString());

		if (tableNumbers.isEmpty()) {
			labelTableNumber.setVisible(false);
			lblTable.setVisible(false);
		}

		labelCustomer.setText(customerName);

		if (customerName == null) {
			labelCustomer.setVisible(false);
			lblCustomer.setVisible(false);
		}

		ticketDetailView.setTickets(tickets);

	}

	private JPanel createTicketInfoPanel() {

		JLabel lblTicket = new javax.swing.JLabel();
		lblTicket.setText(Messages.getString("SettleTicketDialog.0")); //$NON-NLS-1$

		labelTicketNumber = new JLabel();

		lblTable = new javax.swing.JLabel();
		lblTable.setText(POSConstants.TABLES); //$NON-NLS-1$

		labelTableNumber = new JLabel();

		lblCustomer = new javax.swing.JLabel();
		lblCustomer.setText("Customer:"); //$NON-NLS-1$

		labelCustomer = new JLabel();

		JPanel ticketInfoPanel = new com.floreantpos.swing.TransparentPanel(new MigLayout("wrap 2,fill, hidemode 3", "[][grow]", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		ticketInfoPanel.add(lblTicket);
		ticketInfoPanel.add(labelTicketNumber, "grow"); //$NON-NLS-1$
		ticketInfoPanel.add(lblTable);
		ticketInfoPanel.add(labelTableNumber, "grow"); //$NON-NLS-1$
		ticketInfoPanel.add(lblCustomer);
		ticketInfoPanel.add(labelCustomer, "grow"); //$NON-NLS-1$

		return ticketInfoPanel;
	}

	private JPanel createTotalViewerPanel() {

		JLabel lblSubtotal = new javax.swing.JLabel();
		lblSubtotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblSubtotal.setText(com.floreantpos.POSConstants.SUBTOTAL + ":" + " " + CurrencyUtil.getCurrencySymbol()); //$NON-NLS-1$ //$NON-NLS-2$

		tfSubtotal = new javax.swing.JTextField(10);
		tfSubtotal.setHorizontalAlignment(SwingConstants.TRAILING);
		tfSubtotal.setEditable(false);

		JLabel lblDiscount = new javax.swing.JLabel();
		lblDiscount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblDiscount.setText(Messages.getString("TicketView.9") + " " + CurrencyUtil.getCurrencySymbol()); //$NON-NLS-1$ //$NON-NLS-2$

		tfDiscount = new javax.swing.JTextField(10);
		tfDiscount.setHorizontalAlignment(SwingConstants.TRAILING);
		tfDiscount.setEditable(false);

		JLabel lblDeliveryCharge = new javax.swing.JLabel();
		lblDeliveryCharge.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblDeliveryCharge.setText("Delivery Charge:" + " " + CurrencyUtil.getCurrencySymbol()); //$NON-NLS-1$ //$NON-NLS-2$

		tfDeliveryCharge = new JTextField(10);
		tfDeliveryCharge.setHorizontalAlignment(SwingConstants.TRAILING);
		tfDeliveryCharge.setEditable(false);

		JLabel lblTax = new javax.swing.JLabel();
		lblTax.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblTax.setText(com.floreantpos.POSConstants.TAX + ":" + " " + CurrencyUtil.getCurrencySymbol()); //$NON-NLS-1$ //$NON-NLS-2$

		tfTax = new javax.swing.JTextField();
		tfTax.setEditable(false);
		tfTax.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel lblGratuity = new javax.swing.JLabel();
		lblGratuity.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblGratuity.setText(Messages.getString("SettleTicketDialog.5") + ":" + " " + CurrencyUtil.getCurrencySymbol()); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

		tfGratuity = new javax.swing.JTextField();
		tfGratuity.setEditable(false);
		tfGratuity.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel lblTotal = new javax.swing.JLabel();
		lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD, 18));
		lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblTotal.setText(com.floreantpos.POSConstants.TOTAL + ":" + " " + CurrencyUtil.getCurrencySymbol()); //$NON-NLS-1$ //$NON-NLS-2$

		tfTotal = new javax.swing.JTextField(10);
		tfTotal.setFont(tfTotal.getFont().deriveFont(Font.BOLD, 18));
		tfTotal.setHorizontalAlignment(SwingConstants.TRAILING);
		tfTotal.setEditable(false);

		JPanel ticketAmountPanel = new com.floreantpos.swing.TransparentPanel(new MigLayout("hidemode 3,ins 2 2 3 2,alignx trailing,fill", "[grow][]", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		ticketAmountPanel.add(lblSubtotal, "growx,aligny center"); //$NON-NLS-1$
		ticketAmountPanel.add(tfSubtotal, "growx,aligny center"); //$NON-NLS-1$
		ticketAmountPanel.add(lblDiscount, "newline,growx,aligny center"); //$NON-NLS-1$ //$NON-NLS-2$
		ticketAmountPanel.add(tfDiscount, "growx,aligny center"); //$NON-NLS-1$
		ticketAmountPanel.add(lblTax, "newline,growx,aligny center"); //$NON-NLS-1$
		ticketAmountPanel.add(tfTax, "growx,aligny center"); //$NON-NLS-1$
		ticketAmountPanel.add(lblDeliveryCharge, "newline,growx,aligny center"); //$NON-NLS-1$
		ticketAmountPanel.add(tfDeliveryCharge, "growx,aligny center"); //$NON-NLS-1$
		ticketAmountPanel.add(lblGratuity, "newline,growx,aligny center"); //$NON-NLS-1$
		ticketAmountPanel.add(tfGratuity, "growx,aligny center"); //$NON-NLS-1$
		ticketAmountPanel.add(lblTotal, "newline,growx,aligny center"); //$NON-NLS-1$
		ticketAmountPanel.add(tfTotal, "growx,aligny center"); //$NON-NLS-1$

		return ticketAmountPanel;
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
		paymentView.updateView();
		updateView();

	}

	public void doGroupSettle(PaymentType paymentType) {
		try {
			if (tickets == null)
				return;
			this.paymentType = paymentType;
			totalTenderAmount = paymentView.getTenderedAmount();
			totalDueAmount = NumberUtil.roundToTwoDigit(totalDueAmount);

			if (totalTenderAmount < totalDueAmount) {
				POSMessageDialog.showMessage("Partial payment not allowed."); //$NON-NLS-1$
				return;
			}

			/*if (orderType == OrderType.BAR_TAB) {
				doSettleBarTabTicket(ticket);
				return;
			}*/

			cardName = paymentType.getDisplayString();
			PosTransaction transaction = null;

			switch (paymentType) {
				case CASH:
					if (!confirmPayment()) {
						return;
					}

					transaction = paymentType.createTransaction();
					transaction.setCaptured(true);

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
					transaction.setCaptured(true);

					settleTicket(transaction);
					break;

				case CREDIT_CARD:
				case CREDIT_VISA:
				case CREDIT_MASTER_CARD:
				case CREDIT_AMEX:
				case CREDIT_DISCOVERY:
					payUsingCard(cardName, totalTenderAmount);
					break;

				case DEBIT_VISA:
				case DEBIT_MASTER_CARD:
					payUsingCard(cardName, totalTenderAmount);
					break;

				case GIFT_CERTIFICATE:
					GiftCertDialog giftCertDialog = new GiftCertDialog();
					giftCertDialog.pack();
					giftCertDialog.open();

					if (giftCertDialog.isCanceled())
						return;

					transaction = new GiftCertificateTransaction();
					transaction.setPaymentType(PaymentType.GIFT_CERTIFICATE.name());
					transaction.setCaptured(true);

					double giftCertFaceValue = giftCertDialog.getGiftCertFaceValue();
					double giftCertCashBackAmount = 0;
					transaction.setTenderAmount(giftCertFaceValue);

					if (giftCertFaceValue >= totalDueAmount) {
						transaction.setAmount(totalDueAmount);
						giftCertCashBackAmount = giftCertFaceValue - totalDueAmount;
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

	private boolean confirmPayment() {
		if (!TerminalConfig.isUseSettlementPrompt()) {
			return true;
		}

		ConfirmPayDialog confirmPayDialog = new ConfirmPayDialog();
		confirmPayDialog.setAmount(totalTenderAmount);
		confirmPayDialog.open();

		if (confirmPayDialog.isCanceled()) {
			return false;
		}

		return true;
	}

	public void settleTicket(PosTransaction posTransaction) {
		try {
			List<PosTransaction> transactionList = new ArrayList<PosTransaction>();
			totalTenderAmount = paymentView.getTenderedAmount();

			for (Ticket ticket : tickets) {
				PosTransaction transaction = null;
				if (totalTenderAmount <= 0) {
					break;
				}

				transaction = (PosTransaction) SerializationUtils.clone(posTransaction);
				transaction.setGlobalId(GlobalIdGenerator.generateGlobalId());
				transaction.setTicket(ticket);
				setTransactionAmounts(transaction);

				confirmLoyaltyDiscount(ticket);

				PosTransactionService transactionService = PosTransactionService.getInstance();
				transactionService.settleTicket(ticket, transaction);

				transactionList.add(transaction);
				printTicket(ticket, transaction);
			}

			//FIXME
			showTransactionCompleteMsg(totalDueAmount, totalTenderAmount, tickets, transactionList);

			setCanceled(false);
			dispose();

		} catch (UnknownHostException e) {
			POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("SettleTicketDialog.12")); //$NON-NLS-1$
		} catch (Exception e) {
			POSMessageDialog.showError(this, POSConstants.ERROR_MESSAGE, e);
		}
	}

	public void showTransactionCompleteMsg(final double dueAmount, final double tenderedAmount, List<Ticket> ticket, List<PosTransaction> transactions) {
		TransactionCompletionDialog dialog = new TransactionCompletionDialog(transactions);

		double paidAmount = 0;
		double ticketsDueAmount = 0;
		for (PosTransaction transaction : transactions) {
			paidAmount += transaction.getAmount();
			dialog.setCard(transaction.isCard());
		}
		dialog.setTenderedAmount(tenderedAmount);
		dialog.setTotalAmount(dueAmount);
		dialog.setPaidAmount(paidAmount);
		for (Ticket tTicket : tickets) {
			ticketsDueAmount += tTicket.getDueAmount();
		}
		dialog.setDueAmount(ticketsDueAmount);
		if (tenderedAmount > paidAmount) {
			dialog.setChangeAmount(tenderedAmount - paidAmount);
		}
		else {
			dialog.setChangeAmount(0);
		}

		dialog.updateView();
		dialog.pack();
		dialog.open();
	}

	public void confirmLoyaltyDiscount(Ticket ticket) throws IOException, MalformedURLException {
		try {
			if (ticket.hasProperty(LOYALTY_ID)) {
				String url = buildLoyaltyApiURL(ticket, ticket.getProperty(LOYALTY_ID));
				url += "&paid=1"; //$NON-NLS-1$

				IOUtils.toString(new URL(url).openStream());
			}
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage(), e);
		}
	}

	public static void printTicket(Ticket ticket, PosTransaction transaction) {
		try {
			if (ticket.needsKitchenPrint()) {
				ReceiptPrintService.printToKitchen(ticket);
			}

			ReceiptPrintService.printTransaction(transaction);

			if (transaction instanceof CashTransaction) {
				DrawerUtil.kickDrawer();
			}
		} catch (Exception ee) {
			POSMessageDialog.showError(Application.getPosWindow(), com.floreantpos.POSConstants.PRINT_ERROR, ee);
		}
	}

	private void payUsingCard(String cardName, final double tenderedAmount) throws Exception {
		try {
			PaymentGatewayPlugin paymentGateway = CardConfig.getPaymentGateway();

			if (paymentGateway instanceof InginicoPlugin) {
				waitDialog.setVisible(true);
				if (!waitDialog.isCanceled()) {
					dispose();
				}
				return;
			}
			if (!paymentGateway.shouldShowCardInputProcessor()) {

				PosTransaction transaction = paymentType.createTransaction();

				if (!confirmPayment()) {
					return;
				}

				transaction.setCaptured(false);
				transaction.setCardMerchantGateway(paymentGateway.getProductName());

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
			POSMessageDialog.showError(this, e.getMessage(), e);
		}

	}

	@Override
	public void open() {
		super.open();
	}

	@Override
	public void cardInputted(CardInputProcessor inputter, PaymentType selectedPaymentType) {
		//authorize only, do not capture
		PaymentProcessWaitDialog waitDialog = new PaymentProcessWaitDialog(this);
		try {

			waitDialog.setVisible(true);

			PosTransaction transaction = paymentType.createTransaction();

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

				settleTicket(cardProcessor, transaction);
			}
			else if (inputter instanceof ManualCardEntryDialog) {

				ManualCardEntryDialog mDialog = (ManualCardEntryDialog) inputter;

				transaction.setCaptured(false);
				transaction.setCardMerchantGateway(paymentGateway.getProductName());
				transaction.setCardReader(CardReader.MANUAL.name());
				transaction.setCardNumber(mDialog.getCardNumber());
				transaction.setCardExpMonth(mDialog.getExpMonth());
				transaction.setCardExpYear(mDialog.getExpYear());

				settleTicket(cardProcessor, transaction);
			}
			else if (inputter instanceof AuthorizationCodeDialog) {

				PosTransaction selectedTransaction = selectedPaymentType.createTransaction();

				AuthorizationCodeDialog authDialog = (AuthorizationCodeDialog) inputter;
				String authorizationCode = authDialog.getAuthorizationCode();
				if (StringUtils.isEmpty(authorizationCode)) {
					throw new PosException(Messages.getString("SettleTicketDialog.17")); //$NON-NLS-1$
				}

				selectedTransaction.setCardType(selectedPaymentType.getDisplayString());
				selectedTransaction.setCaptured(false);
				selectedTransaction.setCardReader(CardReader.EXTERNAL_TERMINAL.name());
				selectedTransaction.setCardAuthCode(authorizationCode);

				settleTicket(selectedTransaction);
			}
		} catch (Exception e) {
			PosLog.error(getClass(), e);
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage());
		} finally {
			waitDialog.setVisible(false);
		}
	}

	private void settleTicket(CardProcessor cardProcessor, PosTransaction transaction) {
		try {
			List<PosTransaction> transactionList = new ArrayList<PosTransaction>();
			totalTenderAmount = paymentView.getTenderedAmount();

			for (Ticket ticket : tickets) {
				PosTransaction cardTransaction = new PosTransaction();
				if (totalTenderAmount <= 0) {
					break;
				}

				cardTransaction = (PosTransaction) SerializationUtils.clone(transaction);
				cardTransaction.setId(null);
				cardTransaction.setGlobalId(GlobalIdGenerator.generateGlobalId());

				cardTransaction.setTicket(ticket);
				setTransactionAmounts(cardTransaction);

				if (ticket.getOrderType().isPreAuthCreditCard()) {// authorize onlly do not capture
					cardProcessor.preAuth(transaction);
				}
				else {
					cardProcessor.chargeAmount(transaction);
				}

				confirmLoyaltyDiscount(ticket);

				PosTransactionService transactionService = PosTransactionService.getInstance();
				transactionService.settleTicket(ticket, cardTransaction);

				transactionList.add(cardTransaction);
				printTicket(ticket, cardTransaction);
			}

			//FIXME
			showTransactionCompleteMsg(totalDueAmount, totalTenderAmount, tickets, transactionList);

			setCanceled(false);
			dispose();
		} catch (UnknownHostException e) {
			POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("SettleTicketDialog.12")); //$NON-NLS-1$
		} catch (Exception e) {
			POSMessageDialog.showError(this, POSConstants.ERROR_MESSAGE, e);
		}
	}

	private void setTransactionAmounts(PosTransaction transaction) {
		if (tickets.get(tickets.size() - 1).getId() == transaction.getTicket().getId()) {
			if (totalTenderAmount > totalDueAmount) {
				transaction.setTenderAmount(totalTenderAmount - totalDueAmount + transaction.getTicket().getDueAmount());
				transaction.setAmount(transaction.getTicket().getDueAmount());
			}
			else {
				transaction.setTenderAmount(transaction.getTicket().getDueAmount());
				transaction.setAmount(transaction.getTicket().getDueAmount());
			}
			String ticketNumbers = "";
			for (Ticket ticket : tickets) {
				ticketNumbers += "[" + ticket.getId() + "]";
			}
			transaction.getTicket().addProperty("GROUP_SETTLE_TICKETS", "#CHK " + ticketNumbers);
		}
		else {
			transaction.setTenderAmount(transaction.getTicket().getDueAmount());
			transaction.setAmount(transaction.getTicket().getDueAmount());
		}
	}

	public boolean hasMyKalaId() {
		if (ticket == null)
			return false;

		if (ticket.hasProperty(LOYALTY_ID)) {
			return true;
		}

		return false;
	}

	public String buildLoyaltyApiURL(Ticket ticket, String loyaltyid) {
		Restaurant restaurant = Application.getInstance().getRestaurant();

		String transactionURL = "http://cloud.floreantpos.org/tri2/kala_api?"; //$NON-NLS-1$
		transactionURL += "kala_id=" + loyaltyid; //$NON-NLS-1$
		transactionURL += "&store_id=" + restaurant.getUniqueId(); //$NON-NLS-1$
		transactionURL += "&store_name=" + POSUtil.encodeURLString(restaurant.getName()); //$NON-NLS-1$
		transactionURL += "&store_zip=" + restaurant.getZipCode(); //$NON-NLS-1$
		transactionURL += "&terminal=" + ticket.getTerminal().getId(); //$NON-NLS-1$
		transactionURL += "&server=" + POSUtil.encodeURLString(ticket.getOwner().getFirstName() + " " + ticket.getOwner().getLastName()); //$NON-NLS-1$ //$NON-NLS-2$
		transactionURL += "&" + ticket.toURLForm(); //$NON-NLS-1$

		return transactionURL;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public double getDueAmount() {
		return totalDueAmount;
	}

}
