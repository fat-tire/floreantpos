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
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.config.CardConfig;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.extension.PaymentGatewayPlugin;
import com.floreantpos.main.Application;
import com.floreantpos.model.CardReader;
import com.floreantpos.model.CashTransaction;
import com.floreantpos.model.CouponAndDiscount;
import com.floreantpos.model.CreditCardTransaction;
import com.floreantpos.model.GiftCertificateTransaction;
import com.floreantpos.model.Gratuity;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketCouponAndDiscount;
import com.floreantpos.model.TransactionType;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.ReceiptPrintService;
import com.floreantpos.services.PosTransactionService;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.ui.dialog.CouponAndDiscountDialog;
import com.floreantpos.ui.dialog.DiscountListDialog;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.TransactionCompletionDialog;
import com.floreantpos.ui.ticket.TicketViewerTable;
import com.floreantpos.ui.views.SwitchboardView;
import com.floreantpos.ui.views.order.OrderController;
import com.floreantpos.util.DrawerUtil;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.POSUtil;

public class SettleTicketDialog extends POSDialog implements CardInputListener {
	public static final String LOYALTY_DISCOUNT_PERCENTAGE = "loyalty_discount_percentage"; //$NON-NLS-1$
	public static final String LOYALTY_POINT = "loyalty_point"; //$NON-NLS-1$
	public static final String LOYALTY_COUPON = "loyalty_coupon"; //$NON-NLS-1$
	public static final String LOYALTY_DISCOUNT = "loyalty_discount"; //$NON-NLS-1$
	public static final String LOYALTY_ID = "loyalty_id"; //$NON-NLS-1$

	public final static String VIEW_NAME = "PAYMENT_VIEW"; //$NON-NLS-1$

	private String previousViewName = SwitchboardView.VIEW_NAME;

	private com.floreantpos.swing.TransparentPanel leftPanel = new com.floreantpos.swing.TransparentPanel(new BorderLayout());
	private com.floreantpos.swing.TransparentPanel rightPanel = new com.floreantpos.swing.TransparentPanel(new BorderLayout());

	private PaymentView paymentView;
	private TicketViewerTable ticketViewerTable;
	private javax.swing.JScrollPane ticketScrollPane;
	private Ticket ticket;
	private double tenderAmount;
	private PaymentType paymentType;
	private String cardName;
	JTextField tfSubtotal;
	JTextField tfDiscount;
	JTextField tfTax;
	JTextField tfTotal;

	public SettleTicketDialog() {
		/*super();
		setTitle(Messages.getString("SettleTicketDialog.6")); //$NON-NLS-1$

		getContentPane().setLayout(new BorderLayout(5, 5));

		paymentView = new PaymentView(this);
		ticketViewerTable = new TicketViewerTable(ticket);
		ticketScrollPane = new PosScrollPane(ticketViewerTable);
		leftPanel.add(ticketScrollPane);
		rightPanel.add(paymentView);

		getContentPane().add(leftPanel, BorderLayout.CENTER);
		getContentPane().add(rightPanel, BorderLayout.EAST);*/

	}

	public SettleTicketDialog(Ticket ticket) {
		super();
		this.ticket = ticket;

		setTitle(Messages.getString("SettleTicketDialog.6")); //$NON-NLS-1$

		getContentPane().setLayout(new BorderLayout(5, 5));

		paymentView = new PaymentView(this);
		ticketViewerTable = new TicketViewerTable(ticket);
		ticketScrollPane = new PosScrollPane(ticketViewerTable);

		JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 5));

		centerPanel.add(createTicketInfoPanel(), BorderLayout.NORTH);
		centerPanel.add(ticketScrollPane, BorderLayout.CENTER);
		centerPanel.add(createTotalViewerPanel(), BorderLayout.SOUTH);

		leftPanel.add(centerPanel, BorderLayout.CENTER);

		rightPanel.add(paymentView);

		getContentPane().add(leftPanel, BorderLayout.CENTER);
		getContentPane().add(rightPanel, BorderLayout.EAST);
		paymentView.updateView();
		updateView();
	}

	public void updateView() {
		if (ticket == null) {
			tfSubtotal.setText(""); //$NON-NLS-1$
			tfDiscount.setText(""); //$NON-NLS-1$
			tfTax.setText(""); //$NON-NLS-1$
			tfTotal.setText(""); //$NON-NLS-1$
			return;
		}
		tfSubtotal.setText(NumberUtil.formatNumber(ticket.getSubtotalAmount()));
		tfDiscount.setText(NumberUtil.formatNumber(ticket.getDiscountAmount()));

		if (Application.getInstance().isPriceIncludesTax()) {
			tfTax.setText(Messages.getString("TicketView.35")); //$NON-NLS-1$
		}
		else {
			tfTax.setText(NumberUtil.formatNumber(ticket.getTaxAmount()));
		}

		tfTotal.setText(NumberUtil.formatNumber(ticket.getTotalAmount()));

	}

	/*public JPanel buttonPanel() {

		JPanel ticketAmountPanel = new com.floreantpos.swing.TransparentPanel(new MigLayout("fill","sg","sg")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		PosButton btnGratuity = new PosButton("GRATUITY");
		PosButton btnDiscount = new PosButton("DISCOUNTS");

		ticketAmountPanel.add(btnGratuity,"grow"); //$NON-NLS-1$
		ticketAmountPanel.add(btnDiscount,"grow"); //$NON-NLS-1$
		btnGratuity.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doSetGratuity();
			}
		});

		btnDiscount.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doViewDiscounts();
			}
		});

		return ticketAmountPanel;
	}*/
	private JPanel createTicketInfoPanel() {

		JLabel lblTicket = new javax.swing.JLabel();
		lblTicket.setText("Ticket #");

		JLabel labelTicketNumber = new JLabel();
		labelTicketNumber.setText(String.valueOf(ticket.getId()));

		JLabel lblTable = new javax.swing.JLabel();
		lblTable.setText("Table #");

		JLabel labelTableNumber = new JLabel();
		labelTableNumber.setText(getTableNumbers(ticket.getTableNumbers()));

		if (ticket.getTableNumbers().isEmpty()) {
			labelTableNumber.setVisible(false);
			lblTable.setVisible(false);
		}

		JLabel lblCustomer = new javax.swing.JLabel();
		lblCustomer.setText("Customer :");

		JLabel labelCustomer = new JLabel();
		labelCustomer.setText(ticket.getProperty(Ticket.CUSTOMER_NAME));

		if (ticket.getProperty(Ticket.CUSTOMER_NAME) == null) {
			labelCustomer.setVisible(false);
			lblCustomer.setVisible(false);
		}

		JPanel ticketInfoPanel = new com.floreantpos.swing.TransparentPanel(new MigLayout("wrap 2,fill, hidemode 3", "[][grow]", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		ticketInfoPanel.add(lblTicket, "");
		ticketInfoPanel.add(labelTicketNumber, "grow");
		ticketInfoPanel.add(lblTable, "");
		ticketInfoPanel.add(labelTableNumber, "grow");
		ticketInfoPanel.add(lblCustomer, "");
		ticketInfoPanel.add(labelCustomer, "grow");

		return ticketInfoPanel;
	}

	private String getTableNumbers(List<Integer> numbers) {

		String tableNumbers = "";

		for (Iterator iterator = numbers.iterator(); iterator.hasNext();) {
			Integer n = (Integer) iterator.next();
			tableNumbers += n;

			if (iterator.hasNext()) {
				tableNumbers += ", ";
			}
		}
		return tableNumbers;
	}

	private JPanel createTotalViewerPanel() {

		JLabel lblSubtotal = new javax.swing.JLabel();
		lblSubtotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblSubtotal.setText(com.floreantpos.POSConstants.SUBTOTAL + ":"); //$NON-NLS-1$

		tfSubtotal = new javax.swing.JTextField(10);
		tfSubtotal.setFont(tfSubtotal.getFont().deriveFont(Font.PLAIN, 16));
		tfSubtotal.setHorizontalAlignment(SwingConstants.TRAILING);
		tfSubtotal.setEditable(false);

		JLabel lblDiscount = new javax.swing.JLabel();

		lblDiscount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblDiscount.setText(Messages.getString("TicketView.9")); //$NON-NLS-1$

		tfDiscount = new javax.swing.JTextField(10);
		tfDiscount.setFont(tfDiscount.getFont().deriveFont(Font.PLAIN, 16));
		tfDiscount.setHorizontalAlignment(SwingConstants.TRAILING);
		tfDiscount.setEditable(false);
		tfDiscount.setText(ticket.getDiscountAmount().toString());

		JLabel lblTax = new javax.swing.JLabel();
		lblTax.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblTax.setText(com.floreantpos.POSConstants.TAX + ":"); //$NON-NLS-1$

		tfTax = new javax.swing.JTextField();
		tfTax.setFont(tfTax.getFont().deriveFont(Font.PLAIN, 16));
		tfTax.setEditable(false);
		tfTax.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel lblTotal = new javax.swing.JLabel();
		lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblTotal.setText(com.floreantpos.POSConstants.TOTAL + ":"); //$NON-NLS-1$

		tfTotal = new javax.swing.JTextField(10);
		tfTotal.setFont(tfTotal.getFont().deriveFont(Font.BOLD, 18));
		tfTotal.setHorizontalAlignment(SwingConstants.TRAILING);
		tfTotal.setEditable(false);

		JPanel ticketAmountPanel = new com.floreantpos.swing.TransparentPanel(new MigLayout("ins 2 2 3 2,alignx trailing,fill", "[grow][]", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		ticketAmountPanel.add(lblSubtotal, "growx,aligny center"); //$NON-NLS-1$
		ticketAmountPanel.add(tfSubtotal, "growx,aligny center"); //$NON-NLS-1$
		ticketAmountPanel.add(lblDiscount, "newline,growx,aligny center"); //$NON-NLS-1$ //$NON-NLS-2$
		ticketAmountPanel.add(tfDiscount, "growx,aligny center"); //$NON-NLS-1$
		ticketAmountPanel.add(lblTax, "newline,growx,aligny center"); //$NON-NLS-1$
		ticketAmountPanel.add(tfTax, "growx,aligny center"); //$NON-NLS-1$
		ticketAmountPanel.add(lblTotal, "newline,growx,aligny center"); //$NON-NLS-1$
		ticketAmountPanel.add(tfTotal, "growx,aligny center"); //$NON-NLS-1$

		return ticketAmountPanel;
	}

	private void updateModel() {
		if (ticket == null) {
			return;
		}
		ticket.calculatePrice();
	}

	public void doApplyCoupon() {// GEN-FIRST:event_btnApplyCoupondoApplyCoupon
		try {
			if (ticket == null)
				return;

			if (!Application.getCurrentUser().hasPermission(UserPermission.ADD_DISCOUNT)) {
				POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("SettleTicketDialog.7")); //$NON-NLS-1$
				return;
			}

			if (ticket.getCouponAndDiscounts() != null && ticket.getCouponAndDiscounts().size() > 0) {
				POSMessageDialog.showError(Application.getPosWindow(), com.floreantpos.POSConstants.DISCOUNT_COUPON_LIMIT_);
				return;
			}

			CouponAndDiscountDialog dialog = new CouponAndDiscountDialog();
			dialog.setTicket(ticket);
			dialog.initData();
			dialog.open();
			if (!dialog.isCanceled()) {
				TicketCouponAndDiscount coupon = dialog.getSelectedCoupon();
				ticket.addTocouponAndDiscounts(coupon);

				updateModel();

				OrderController.saveOrder(ticket);
				//ticketDetailView.updateView();
				paymentView.updateView();
			}
		} catch (Exception e) {
			POSMessageDialog.showError(this, com.floreantpos.POSConstants.ERROR_MESSAGE, e);
		}
	}// GEN-LAST:event_btnApplyCoupondoApplyCoupon

	//	public void doTaxExempt(boolean taxExempt) {// GEN-FIRST:event_doTaxExempt
	//		if (ticket == null)
	//			return;
	//
	//		boolean setTaxExempt = taxExempt;
	//		if (setTaxExempt) {
	//			int option = JOptionPane.showOptionDialog(this, POSConstants.CONFIRM_SET_TAX_EXEMPT, POSConstants.CONFIRM, JOptionPane.YES_NO_OPTION,
	//					JOptionPane.QUESTION_MESSAGE, null, null, null);
	//			if (option != JOptionPane.YES_OPTION) {
	//				return;
	//			}
	//
	//			ticket.setTaxExempt(true);
	//			ticket.calculatePrice();
	//			TicketDAO.getInstance().saveOrUpdate(ticket);
	//		}
	//		else {
	//			ticket.setTaxExempt(false);
	//			ticket.calculatePrice();
	//			TicketDAO.getInstance().saveOrUpdate(ticket);
	//		}
	//
	//		ticketDetailView.updateView();
	//		paymentView.updateView();
	//	}// GEN-LAST:event_doTaxExempt

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

		//ticketDetailView.updateView();
		paymentView.updateView();
	}

	public void doViewDiscounts() {// GEN-FIRST:event_btnViewDiscountsdoViewDiscounts
		try {

			if (ticket == null)
				return;

			DiscountListDialog dialog = new DiscountListDialog(Arrays.asList(ticket));
			dialog.open();

			if (!dialog.isCanceled() && dialog.isModified()) {
				updateModel();

				TicketDAO.getInstance().saveOrUpdate(ticket);

				//	ticketDetailView.updateView();
				paymentView.updateView();
			}

		} catch (Exception e) {
			POSMessageDialog.showError(this, com.floreantpos.POSConstants.ERROR_MESSAGE, e);
		}
	}// GEN-LAST:event_btnViewDiscountsdoViewDiscounts

	public void doSettle(PaymentType paymentType) {
		try {
			if (ticket == null)
				return;
			this.paymentType = paymentType;
			tenderAmount = paymentView.getTenderedAmount();

			if (ticket.getType() == OrderType.BAR_TAB) {
				doSettleBarTabTicket(ticket);
				return;
			}

			cardName = paymentType.getDisplayString();
			PosTransaction transaction = null;

			switch (paymentType) {
				case CASH:
					if (!confirmPayment()) {
						return;
					}

					transaction = new CashTransaction();
					transaction.setPaymentType(paymentType.name());
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

				case DEBIT_VISA:
				case DEBIT_MASTER_CARD:
					payUsingCard(cardName, tenderAmount);
					break;

				case GIFT_CERTIFICATE:
					GiftCertDialog giftCertDialog = new GiftCertDialog(this);
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
			e.printStackTrace();
		}
	}

	/*public void doSettle() {
		try {
			if (ticket == null)
				return;

			tenderAmount = paymentView.getTenderedAmount();

			if (ticket.getType() == OrderType.BAR_TAB) {
				doSettleBarTabTicket(ticket);
				return;
			}

			PaymentTypeSelectionDialog paymentTypeSelectionDialog = new PaymentTypeSelectionDialog();
			paymentTypeSelectionDialog.setResizable(false);
			paymentTypeSelectionDialog.pack();
			paymentTypeSelectionDialog.open();
			if (paymentTypeSelectionDialog.isCanceled()) {
				return;
			}

			paymentType = paymentTypeSelectionDialog.getSelectedPaymentType();
			cardName = paymentType.getDisplayString();
			PosTransaction transaction = null;

			switch (paymentType) {
				case CASH:
					if (!confirmPayment()) {
						return;
					}

					transaction = new CashTransaction();
					transaction.setPaymentType(paymentType.name());
					transaction.setTicket(ticket);
					transaction.setCaptured(true);
					setTransactionAmounts(transaction);

					settleTicket(transaction);
					break;

				case CREDIT_VISA:
				case CREDIT_MASTER_CARD:
				case CREDIT_AMEX:
				case CREDIT_DISCOVERY:
					payUsingCard(cardName, tenderAmount);
					break;

				case DEBIT_VISA:
				case DEBIT_MASTER_CARD:
					payUsingCard(cardName, tenderAmount);
					break;

				case GIFT_CERTIFICATE:
					GiftCertDialog giftCertDialog = new GiftCertDialog(this);
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
			e.printStackTrace();
		}
	}*/

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

	private void doSettleBarTabTicket(Ticket ticket) {
		if (!confirmPayment()) {
			return;
		}

		PaymentProcessWaitDialog waitDialog = new PaymentProcessWaitDialog(this);
		waitDialog.setVisible(true);

		try {
			PaymentGatewayPlugin paymentGateway = CardConfig.getPaymentGateway();

			String transactionId = ticket.getProperty(Ticket.PROPERTY_CARD_TRANSACTION_ID);

			CreditCardTransaction transaction = new CreditCardTransaction();
			transaction.setPaymentType(ticket.getProperty(Ticket.PROPERTY_PAYMENT_METHOD));
			transaction.setTransactionType(TransactionType.CREDIT.name());
			transaction.setTicket(ticket);
			transaction.setCardType(ticket.getProperty(Ticket.PROPERTY_CARD_NAME));
			transaction.setCaptured(false);
			transaction.setCardMerchantGateway(paymentGateway.getName());
			transaction.setCardAuthCode(ticket.getProperty("AuthCode")); //$NON-NLS-1$
			transaction.addProperty("AcqRefData", ticket.getProperty("AcqRefData")); //$NON-NLS-1$ //$NON-NLS-2$

			CardReader cardReader = CardReader.valueOf(ticket.getProperty(Ticket.PROPERTY_CARD_READER));

			if (cardReader == CardReader.SWIPE) {
				transaction.setCardReader(CardReader.SWIPE.name());
				transaction.setCardTrack(ticket.getProperty(Ticket.PROPERTY_CARD_TRACKS));
				transaction.setCardTransactionId(transactionId);
			}
			else if (cardReader == CardReader.MANUAL) {
				transaction.setCardReader(CardReader.MANUAL.name());
				transaction.setCardTransactionId(transactionId);
				transaction.setCardNumber(ticket.getProperty(Ticket.PROPERTY_CARD_NUMBER));
				transaction.setCardExpiryMonth(ticket.getProperty(Ticket.PROPERTY_CARD_EXP_MONTH));
				transaction.setCardExpiryYear(ticket.getProperty(Ticket.PROPERTY_CARD_EXP_YEAR));
			}
			else {
				transaction.setCardReader(CardReader.EXTERNAL_TERMINAL.name());
				transaction.setCardAuthCode(ticket.getProperty(Ticket.PROPERTY_CARD_AUTH_CODE));
			}

			setTransactionAmounts(transaction);

			if (cardReader == CardReader.SWIPE || cardReader == CardReader.MANUAL) {
				double advanceAmount = Double.parseDouble(ticket.getProperty(Ticket.PROPERTY_ADVANCE_PAYMENT, paymentGateway.getName())); //$NON-NLS-1$

				CardProcessor cardProcessor = paymentGateway.getProcessor();
				if (tenderAmount > advanceAmount) {
					cardProcessor.voidAmount(transactionId, advanceAmount);
				}

				cardProcessor.authorizeAmount(transaction);
			}

			settleTicket(transaction);

		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage(), e);
		} finally {
			waitDialog.setVisible(false);
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

					setCanceled(false);
					dispose();
				}

				setTicket(ticket);
			}
			else {
				setCanceled(false);
				dispose();
			}
		} catch (UnknownHostException e) {
			POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("SettleTicketDialog.12")); //$NON-NLS-1$
		} catch (Exception e) {
			POSMessageDialog.showError(this, POSConstants.ERROR_MESSAGE, e);
		}
	}

	private void showTransactionCompleteMsg(final double dueAmount, final double tenderedAmount, Ticket ticket, PosTransaction transaction) {
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

		// dialog.setGratuityAmount(gratuityAmount);
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

	private void printTicket(Ticket ticket, PosTransaction transaction) {
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
			//		if (!CardConfig.getMerchantGateway().isCardTypeSupported(cardName)) {
			//			POSMessageDialog.showError(Application.getPosWindow(), "<html>Card <b>" + cardName + "</b> not supported.</html>");
			//			return;
			//		}

			PaymentGatewayPlugin paymentGateway = CardConfig.getPaymentGateway();
			if (!paymentGateway.shouldShowCardInputProcessor()) {

				PosTransaction transaction = paymentType.createTransaction();
				transaction.setTicket(ticket);

				if (!confirmPayment()) {
					return;
				}

				transaction.setCardType(cardName);
				transaction.setCaptured(false);
				transaction.setCardMerchantGateway(paymentGateway.getName());

				setTransactionAmounts(transaction);

				paymentGateway.getProcessor().authorizeAmount(transaction);

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

	public void updatePaymentView() {
		paymentView.updateView();
	}

	public String getPreviousViewName() {
		return previousViewName;
	}

	public void setPreviousViewName(String previousViewName) {
		this.previousViewName = previousViewName;
	}

	/*public TicketDetailView getTicketDetailView() {
		return ticketDetailView;
	}*/

	@Override
	public void open() {
		super.open();
	}

	@Override
	public void cardInputted(CardInputProcessor inputter) {
		//authorize only, do not capture
		PaymentProcessWaitDialog waitDialog = new PaymentProcessWaitDialog(this);

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

				transaction.setCardType(cardName);
				transaction.setCardTrack(cardString);
				transaction.setCaptured(false);
				transaction.setCardMerchantGateway(paymentGateway.getName());
				transaction.setCardReader(CardReader.SWIPE.name());
				setTransactionAmounts(transaction);

				cardProcessor.authorizeAmount(transaction);

				settleTicket(transaction);
			}
			else if (inputter instanceof ManualCardEntryDialog) {
				ManualCardEntryDialog mDialog = (ManualCardEntryDialog) inputter;

				transaction.setCardType(cardName);
				transaction.setCaptured(false);
				transaction.setCardMerchantGateway(paymentGateway.getName());
				transaction.setCardReader(CardReader.MANUAL.name());
				transaction.setCardNumber(mDialog.getCardNumber());
				transaction.setCardExpiryMonth(mDialog.getExpMonth());
				transaction.setCardExpiryYear(mDialog.getExpYear());
				setTransactionAmounts(transaction);

				cardProcessor.authorizeAmount(transaction);

				settleTicket(transaction);
			}
			else if (inputter instanceof AuthorizationCodeDialog) {
				AuthorizationCodeDialog authDialog = (AuthorizationCodeDialog) inputter;
				String authorizationCode = authDialog.getAuthorizationCode();
				if (StringUtils.isEmpty(authorizationCode)) {
					throw new PosException(Messages.getString("SettleTicketDialog.17")); //$NON-NLS-1$
				}

				transaction.setCardType(cardName);
				transaction.setCaptured(false);
				transaction.setCardReader(CardReader.EXTERNAL_TERMINAL.name());
				transaction.setCardAuthCode(authorizationCode);
				setTransactionAmounts(transaction);

				settleTicket(transaction);
			}
		} catch (Exception e) {
			e.printStackTrace();
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage());
		} finally {
			waitDialog.setVisible(false);
		}
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

	public boolean hasMyKalaId() {
		if (ticket == null)
			return false;

		if (ticket.hasProperty(LOYALTY_ID)) {
			return true;
		}

		return false;
	}

	public void submitMyKalaDiscount() {
		if (ticket.hasProperty(LOYALTY_ID)) {
			POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("SettleTicketDialog.18")); //$NON-NLS-1$
			return;
		}

		try {
			String loyaltyid = JOptionPane.showInputDialog(Messages.getString("SettleTicketDialog.19")); //$NON-NLS-1$

			if (StringUtils.isEmpty(loyaltyid)) {
				return;
			}

			ticket.addProperty(LOYALTY_ID, loyaltyid);

			String transactionURL = buildLoyaltyApiURL(ticket, loyaltyid);

			String string = IOUtils.toString(new URL(transactionURL).openStream());

			JsonReader reader = Json.createReader(new StringReader(string));
			JsonObject object = reader.readObject();
			JsonArray jsonArray = (JsonArray) object.get("discounts"); //$NON-NLS-1$
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonObject jsonObject = (JsonObject) jsonArray.get(i);
				addCoupon(ticket, jsonObject);
			}

			updateModel();

			OrderController.saveOrder(ticket);

			POSMessageDialog.showMessage(Application.getPosWindow(), Messages.getString("SettleTicketDialog.21")); //$NON-NLS-1$

			//ticketDetailView.updateView();
			paymentView.updateView();

			//			if (string.contains("\"success\":false")) {
			//				POSMessageDialog.showError(Application.getPosWindow(), "Coupon already used.");
			//			}
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("SettleTicketDialog.22"), e); //$NON-NLS-1$
		}
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

	private void addCoupon(Ticket ticket, JsonObject jsonObject) {
		Set<String> keys = jsonObject.keySet();
		for (String key : keys) {
			JsonNumber jsonNumber = jsonObject.getJsonNumber(key);
			double doubleValue = jsonNumber.doubleValue();

			TicketCouponAndDiscount coupon = new TicketCouponAndDiscount();
			coupon.setName(key);
			coupon.setType(CouponAndDiscount.FIXED_PER_ORDER);
			coupon.setValue(doubleValue);

			ticket.addTocouponAndDiscounts(coupon);
		}
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;

		//ticketDetailView.setTickets(Arrays.asList(ticket));
		paymentView.updateView();
	}
}
