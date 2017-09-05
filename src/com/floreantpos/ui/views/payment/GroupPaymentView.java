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
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.CashDrawer;
import com.floreantpos.model.Currency;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TerminalDAO;
import com.floreantpos.report.ReceiptPrintService;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.MultiCurrencyTenderDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.CurrencyUtil;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.POSUtil;

public class GroupPaymentView extends JPanel {
	private static final String ZERO = "0"; //$NON-NLS-1$

	private static final String REMOVE = "1"; //$NON-NLS-1$

	protected GroupSettleTicketDialog groupSettleTicketView;

	private com.floreantpos.swing.PosButton btnCancel;
	private com.floreantpos.swing.PosButton btnCash;
	private com.floreantpos.swing.PosButton btnCreditCard;
	private com.floreantpos.swing.PosButton btnGift;
	private com.floreantpos.swing.PosButton btnOther;

	private com.floreantpos.swing.TransparentPanel calcButtonPanel;
	private javax.swing.JLabel labelDueAmount;
	private javax.swing.JLabel labelTenderedAmount;
	private com.floreantpos.swing.TransparentPanel actionButtonPanel;
	private com.floreantpos.swing.PosButton btn7;
	private com.floreantpos.swing.PosButton btnDot;
	private com.floreantpos.swing.PosButton btn0;
	private com.floreantpos.swing.PosButton btnClear;
	private com.floreantpos.swing.PosButton btn8;
	private com.floreantpos.swing.PosButton btn9;
	private com.floreantpos.swing.PosButton btn4;
	private com.floreantpos.swing.PosButton btn5;
	private com.floreantpos.swing.PosButton btn6;
	private com.floreantpos.swing.PosButton btn3;
	private com.floreantpos.swing.PosButton btn2;
	private com.floreantpos.swing.PosButton btn1;
	private com.floreantpos.swing.PosButton btn00;
	private com.floreantpos.swing.PosButton btnNextAmount;

	private com.floreantpos.swing.PosButton btnAmount1;
	private com.floreantpos.swing.PosButton btnAmount2;
	private com.floreantpos.swing.PosButton btnAmount5;
	private com.floreantpos.swing.PosButton btnAmount10;
	private com.floreantpos.swing.PosButton btnAmount20;
	private com.floreantpos.swing.PosButton btnAmount50;
	private com.floreantpos.swing.PosButton btnAmount100;
	private com.floreantpos.swing.PosButton btnExactAmount;

	private JTextField txtTenderedAmount;
	private JTextField txtDueAmount;

	private boolean clearPreviousAmount = true;

	public GroupPaymentView(GroupSettleTicketDialog groupSettleTicketView) {
		this.groupSettleTicketView = groupSettleTicketView;

		initComponents();
	}

	private void initComponents() {
		setLayout(new MigLayout("fill", "[grow][grow]", ""));

		JPanel centerPanel = new JPanel(new BorderLayout(5, 5));

		TransparentPanel transparentPanel1 = new TransparentPanel(new BorderLayout());

		labelDueAmount = new javax.swing.JLabel();
		labelTenderedAmount = new javax.swing.JLabel();
		txtDueAmount = new JTextField();
		txtTenderedAmount = new JTextField();

		Font font1 = new java.awt.Font("Tahoma", 1, PosUIManager.getFontSize(20)); // NOI18N //$NON-NLS-1$
		Font font2 = new java.awt.Font("Arial", Font.PLAIN, PosUIManager.getFontSize(34)); // NOI18N //$NON-NLS-1$

		labelTenderedAmount.setFont(font1);
		labelTenderedAmount.setText(Messages.getString("PaymentView.54") + " " + CurrencyUtil.getCurrencySymbol()); //$NON-NLS-1$ //$NON-NLS-2$
		labelTenderedAmount.setForeground(Color.gray);

		txtTenderedAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		txtTenderedAmount.setFont(font1);

		labelDueAmount.setFont(font1);
		labelDueAmount.setText(Messages.getString("PaymentView.52") + " " + CurrencyUtil.getCurrencySymbol()); //$NON-NLS-1$ //$NON-NLS-2$
		labelDueAmount.setForeground(Color.gray);

		txtDueAmount.setFont(font1);
		txtDueAmount.setEditable(false);
		txtDueAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		transparentPanel1.setLayout(new MigLayout("", "[][grow,fill]", "[19px][][19px]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		transparentPanel1.add(labelDueAmount, "cell 0 0,alignx right,aligny center"); //$NON-NLS-1$

		transparentPanel1.add(labelTenderedAmount, "cell 0 2,alignx left,aligny center"); //$NON-NLS-1$
		transparentPanel1.add(txtDueAmount, "cell 1 0,growx,aligny top"); //$NON-NLS-1$
		transparentPanel1.add(txtTenderedAmount, "cell 1 2,growx,aligny top"); //$NON-NLS-1$

		centerPanel.add(transparentPanel1, BorderLayout.NORTH);

		calcButtonPanel = new com.floreantpos.swing.TransparentPanel();
		calcButtonPanel.setLayout(new MigLayout("wrap 4,fill, ins 0", "sg, fill", "sg, fill")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		btnNextAmount = new com.floreantpos.swing.PosButton();
		btnAmount1 = new com.floreantpos.swing.PosButton();
		btnAmount1.setFont(font2);

		btnAmount2 = new com.floreantpos.swing.PosButton();
		btnAmount2.setFont(font2);

		btnAmount5 = new com.floreantpos.swing.PosButton();
		btnAmount5.setFont(font2);

		btnAmount10 = new com.floreantpos.swing.PosButton();
		btnAmount10.setFont(font2);

		btnAmount20 = new com.floreantpos.swing.PosButton();
		btnAmount20.setFont(font2);

		btnAmount50 = new com.floreantpos.swing.PosButton();
		btnAmount50.setFont(font2);

		btnAmount100 = new com.floreantpos.swing.PosButton();
		btnAmount100.setFont(font2);

		btnExactAmount = new com.floreantpos.swing.PosButton();

		btn7 = new com.floreantpos.swing.PosButton();
		btn8 = new com.floreantpos.swing.PosButton();
		btn9 = new com.floreantpos.swing.PosButton();
		btn4 = new com.floreantpos.swing.PosButton();
		btn5 = new com.floreantpos.swing.PosButton();
		btn6 = new com.floreantpos.swing.PosButton();
		btn1 = new com.floreantpos.swing.PosButton();
		btn2 = new com.floreantpos.swing.PosButton();
		btn3 = new com.floreantpos.swing.PosButton();
		btn0 = new com.floreantpos.swing.PosButton();
		btnDot = new com.floreantpos.swing.PosButton();
		btnClear = new com.floreantpos.swing.PosButton();
		btn00 = new com.floreantpos.swing.PosButton();

		btnAmount1.setForeground(Color.blue);
		btnAmount1.setAction(nextButtonAction);
		btnAmount1.setText(Messages.getString("PaymentView.1")); //$NON-NLS-1$
		btnAmount1.setActionCommand("1"); //$NON-NLS-1$
		btnAmount1.setFocusable(false);
		calcButtonPanel.add(btnAmount1);

		btn7.setAction(calAction);
		btn7.setText("7");
		btn7.setFont(font2);
		//btn7.setIcon(IconFactory.getIcon("/ui_icons/", "7.png")); // NOI18N //$NON-NLS-1$ //$NON-NLS-2$
		btn7.setActionCommand("7"); //$NON-NLS-1$
		btn7.setFocusable(false);
		calcButtonPanel.add(btn7);

		btn8.setAction(calAction);
		btn8.setText("8");
		btn8.setFont(font2);
		//btn8.setIcon(IconFactory.getIcon("/ui_icons/", "8.png")); // NOI18N //$NON-NLS-1$ //$NON-NLS-2$
		btn8.setActionCommand("8"); //$NON-NLS-1$
		btn8.setFocusable(false);
		calcButtonPanel.add(btn8);

		btn9.setAction(calAction);
		btn9.setText("9");
		btn9.setFont(font2);
		//btn9.setIcon(IconFactory.getIcon("/ui_icons/", "9.png")); // NOI18N //$NON-NLS-1$ //$NON-NLS-2$
		btn9.setActionCommand("9"); //$NON-NLS-1$
		btn9.setFocusable(false);
		calcButtonPanel.add(btn9);

		btnAmount2.setForeground(Color.blue);
		btnAmount2.setAction(nextButtonAction);
		btnAmount2.setText(Messages.getString("PaymentView.10")); //$NON-NLS-1$
		btnAmount2.setActionCommand("2"); //$NON-NLS-1$
		btnAmount2.setFocusable(false);

		calcButtonPanel.add(btnAmount2);

		btn4.setAction(calAction);
		btn4.setText("4");
		btn4.setFont(font2);
		//btn4.setIcon(IconFactory.getIcon("/ui_icons/", "4.png")); // NOI18N //$NON-NLS-1$ //$NON-NLS-2$
		btn4.setActionCommand("4"); //$NON-NLS-1$
		btn4.setFocusable(false);
		calcButtonPanel.add(btn4);

		btn5.setAction(calAction);
		btn5.setText("5");
		btn5.setFont(font2);
		//btn5.setIcon(IconFactory.getIcon("/ui_icons/", "5.png")); // NOI18N //$NON-NLS-1$ //$NON-NLS-2$
		btn5.setActionCommand("5"); //$NON-NLS-1$
		btn5.setFocusable(false);
		calcButtonPanel.add(btn5);

		btn6.setAction(calAction);
		btn6.setText("6");
		btn6.setFont(font2);
		//btn6.setIcon(IconFactory.getIcon("/ui_icons/", "6.png")); // NOI18N //$NON-NLS-1$ //$NON-NLS-2$
		btn6.setActionCommand("6"); //$NON-NLS-1$
		btn6.setFocusable(false);
		calcButtonPanel.add(btn6);

		btnAmount5.setForeground(Color.blue);
		btnAmount5.setAction(nextButtonAction);
		btnAmount5.setText(Messages.getString("PaymentView.12")); //$NON-NLS-1$
		btnAmount5.setActionCommand("5"); //$NON-NLS-1$
		btnAmount5.setFocusable(false);

		calcButtonPanel.add(btnAmount5);

		btn1.setAction(calAction);
		btn1.setText("1");
		btn1.setFont(font2);
		//btn1.setIcon(IconFactory.getIcon("/ui_icons/", "1.png")); // NOI18N //$NON-NLS-1$ //$NON-NLS-2$
		btn1.setActionCommand(REMOVE);
		btn1.setFocusable(false);
		calcButtonPanel.add(btn1);

		btn2.setAction(calAction);
		btn2.setText("2");
		btn2.setFont(font2);
		//btn2.setIcon(IconFactory.getIcon("/ui_icons/", "2.png")); // NOI18N //$NON-NLS-1$ //$NON-NLS-2$
		btn2.setActionCommand("2"); //$NON-NLS-1$
		btn2.setFocusable(false);
		calcButtonPanel.add(btn2);

		btn3.setAction(calAction);
		btn3.setText("3");
		btn3.setFont(font2);
		//btn3.setIcon(IconFactory.getIcon("/ui_icons/", "3.png")); // NOI18N //$NON-NLS-1$ //$NON-NLS-2$
		btn3.setActionCommand("3"); //$NON-NLS-1$
		btn3.setFocusable(false);
		calcButtonPanel.add(btn3);

		btnAmount10.setForeground(Color.blue);
		btnAmount10.setAction(nextButtonAction);
		btnAmount10.setText(Messages.getString("PaymentView.14")); //$NON-NLS-1$
		btnAmount10.setActionCommand("10"); //$NON-NLS-1$
		btnAmount10.setFocusable(false);
		calcButtonPanel.add(btnAmount10, "grow"); //$NON-NLS-1$

		btn0.setAction(calAction);
		btn0.setText("0");
		btn0.setFont(font2);
		//btn0.setIcon(IconFactory.getIcon("/ui_icons/", "0.png")); // NOI18N //$NON-NLS-1$ //$NON-NLS-2$
		btn0.setActionCommand(ZERO);
		btn0.setFocusable(false);
		calcButtonPanel.add(btn0);

		btn00.setFont(new Font("Arial", Font.PLAIN, 30)); //$NON-NLS-1$
		btn00.setAction(calAction);
		btn00.setText(Messages.getString("PaymentView.18")); //$NON-NLS-1$
		btn00.setActionCommand("00"); //$NON-NLS-1$
		btn00.setFocusable(false);
		calcButtonPanel.add(btn00);

		btnDot.setAction(calAction);
		btnDot.setIcon(IconFactory.getIcon("/ui_icons/", "dot.png")); // NOI18N //$NON-NLS-1$ //$NON-NLS-2$
		btnDot.setActionCommand("."); //$NON-NLS-1$
		btnDot.setFocusable(false);
		calcButtonPanel.add(btnDot);

		btnAmount20.setForeground(Color.BLUE);
		btnAmount20.setAction(nextButtonAction);
		btnAmount20.setText("20"); //$NON-NLS-1$
		btnAmount20.setActionCommand("20"); //$NON-NLS-1$
		btnAmount20.setFocusable(false);
		calcButtonPanel.add(btnAmount20, "grow"); //$NON-NLS-1$

		btnAmount50.setForeground(Color.blue);
		btnAmount50.setAction(nextButtonAction);
		btnAmount50.setText("50"); //$NON-NLS-1$
		btnAmount50.setActionCommand("50"); //$NON-NLS-1$
		btnAmount50.setFocusable(false);
		calcButtonPanel.add(btnAmount50, "grow"); //$NON-NLS-1$

		btnAmount100.setForeground(Color.blue);
		btnAmount100.setAction(nextButtonAction);
		btnAmount100.setText("100"); //$NON-NLS-1$
		btnAmount100.setActionCommand("100"); //$NON-NLS-1$
		btnAmount100.setFocusable(false);
		calcButtonPanel.add(btnAmount100, "grow"); //$NON-NLS-1$

		btnClear.setAction(calAction);
		btnClear.setIcon(IconFactory.getIcon("/ui_icons/", "clear.png")); // NOI18N //$NON-NLS-1$ //$NON-NLS-2$
		btnClear.setText(Messages.getString("PaymentView.38")); //$NON-NLS-1$
		btnClear.setFocusable(false);
		calcButtonPanel.add(btnClear);

		btnExactAmount.setAction(nextButtonAction);
		btnExactAmount.setText(Messages.getString("PaymentView.20")); //$NON-NLS-1$
		btnExactAmount.setActionCommand("exactAmount"); //$NON-NLS-1$
		btnExactAmount.setFocusable(false);
		calcButtonPanel.add(btnExactAmount, "span 2,grow"); //$NON-NLS-1$

		btnNextAmount.setAction(nextButtonAction);
		btnNextAmount.setText(Messages.getString("PaymentView.23")); //$NON-NLS-1$
		btnNextAmount.setActionCommand("nextAmount"); //$NON-NLS-1$
		btnNextAmount.setFocusable(false);
		calcButtonPanel.add(btnNextAmount, "span 2,grow"); //$NON-NLS-1$

		PosButton btnPrint = new com.floreantpos.swing.PosButton(POSConstants.PRINT_TICKET);
		btnPrint.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				for (Ticket ticket : groupSettleTicketView.getTickets()) {
					ReceiptPrintService.printTicket(ticket);
				}
			}
		});

		calcButtonPanel.add(btnPrint, "span 4,grow"); //$NON-NLS-1$

		centerPanel.add(calcButtonPanel, BorderLayout.CENTER);

		actionButtonPanel = new com.floreantpos.swing.TransparentPanel();
		actionButtonPanel.setLayout(new MigLayout("wrap 1, ins 0 20 0 0,hidemode 3, fill", "sg, fill", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		//actionButtonPanel.setPreferredSize(PosUIManager.getSize(180, 380));

		int width = PosUIManager.getSize(160);

		btnCash = new com.floreantpos.swing.PosButton(Messages.getString("PaymentView.31")); //$NON-NLS-1$
		actionButtonPanel.add(btnCash, "grow,w " + width + "!"); //$NON-NLS-1$ //$NON-NLS-2$ 
		btnCash.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					double x = NumberUtil.parse(txtTenderedAmount.getText()).doubleValue();
					if (x <= 0) {
						POSMessageDialog.showError(Messages.getString("PaymentView.32")); //$NON-NLS-1$
						return;
					}
					groupSettleTicketView.doGroupSettle(PaymentType.CASH);
				} catch (Exception e) {
					org.apache.commons.logging.LogFactory.getLog(getClass()).error(e);
				}
			}
		});

		PosButton btnMultiCurrencyCash = new com.floreantpos.swing.PosButton("MULTI CURRENCY CASH"); //$NON-NLS-1$
		actionButtonPanel.add(btnMultiCurrencyCash, "grow,w " + width + "!"); //$NON-NLS-1$ //$NON-NLS-2$
		btnMultiCurrencyCash.setVisible(TerminalConfig.isEnabledMultiCurrency());
		btnMultiCurrencyCash.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					List<Currency> currencyList = CurrencyUtil.getAllCurrency();
					if (currencyList.size() > 1) {
						if (!adjustCashDrawerBalance(currencyList)) {
							return;
						}
					}
					double x = NumberUtil.parse(txtTenderedAmount.getText()).doubleValue();

					if (x <= 0) {
						POSMessageDialog.showError(Messages.getString("PaymentView.32")); //$NON-NLS-1$
						return;
					}
					groupSettleTicketView.doGroupSettle(PaymentType.CASH);
				} catch (Exception e) {
					org.apache.commons.logging.LogFactory.getLog(getClass()).error(e);
				}
			}
		});

		btnCreditCard = new PosButton(Messages.getString("PaymentView.33")); //$NON-NLS-1$
		actionButtonPanel.add(btnCreditCard, "grow,w " + width + "!"); //$NON-NLS-1$ //$NON-NLS-2$ 
		btnCreditCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				groupSettleTicketView.doGroupSettle(PaymentType.CREDIT_CARD);
			}
		});

		btnGift = new PosButton(Messages.getString("PaymentView.35")); //$NON-NLS-1$
		actionButtonPanel.add(btnGift, "grow,w " + width + "!"); //$NON-NLS-1$ //$NON-NLS-2$ 
		btnGift.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				groupSettleTicketView.doGroupSettle(PaymentType.GIFT_CERTIFICATE);
			}
		});

		btnOther = new PosButton("OTHER"); //$NON-NLS-1$
		actionButtonPanel.add(btnOther, "grow,w " + width + "!"); //$NON-NLS-1$ //$NON-NLS-2$ 
		btnOther.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				groupSettleTicketView.doGroupSettle(PaymentType.CUSTOM_PAYMENT);
			}
		});

		btnCancel = new com.floreantpos.swing.PosButton(POSConstants.CANCEL.toUpperCase());
		actionButtonPanel.add(btnCancel, "grow,w " + width + "!"); //$NON-NLS-1$ //$NON-NLS-2$ 
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCancelActionPerformed(evt);
			}
		});

		//rightPanel.add(actionButtonPanel, BorderLayout.EAST);

		add(centerPanel, "cell 0 0,grow");
		add(actionButtonPanel, "cell 1 0,grow");
	}// </editor-fold>//GEN-END:initComponents

	protected boolean adjustCashDrawerBalance(List<Currency> currencyList) {
		MultiCurrencyTenderDialog dialog = new MultiCurrencyTenderDialog(groupSettleTicketView.getTickets(), currencyList);
		dialog.pack();
		dialog.open();

		if (dialog.isCanceled()) {
			return false;
		}
		txtTenderedAmount.setText(NumberUtil.format3DigitNumber(dialog.getTenderedAmount()));
		CashDrawer cashDrawer = dialog.getCashDrawer();

		try {
			TerminalDAO.getInstance().performBatchSave(cashDrawer);
		} catch (Exception ex) {
			POSMessageDialog.showError(POSUtil.getFocusedWindow(), ex.getMessage());
			return false;
		}
		return true;
	}

	protected void doSetGratuity() {
		groupSettleTicketView.doSetGratuity();
	}

	protected void doTaxExempt() {
	}

	private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
		groupSettleTicketView.setCanceled(true);
		groupSettleTicketView.dispose();
	}//GEN-LAST:event_btnCancelActionPerformed

	// End of variables declaration//GEN-END:variables

	Action calAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {

			JTextField textField = txtTenderedAmount;

			PosButton button = (PosButton) e.getSource();
			String command = button.getActionCommand();
			if (command.equals(Messages.getString("PaymentView.66"))) { //$NON-NLS-1$
				textField.setText(ZERO);
			}
			else if (command.equals(".")) { //$NON-NLS-1$
				if (textField.getText().indexOf('.') < 0) {
					textField.setText(textField.getText() + "."); //$NON-NLS-1$
				}
			}
			else {
				if (clearPreviousAmount) {
					textField.setText(""); //$NON-NLS-1$
					clearPreviousAmount = false;
				}
				String string = textField.getText();
				int index = string.indexOf('.');
				if (index < 0) {
					double value = 0;
					try {
						value = Double.parseDouble(string);
					} catch (NumberFormatException x) {
						Toolkit.getDefaultToolkit().beep();
					}
					if (value == 0) {
						textField.setText(command);
					}
					else {
						textField.setText(string + command);
					}
				}
				else {
					textField.setText(string + command);
				}
			}
		}
	};

	Action nextButtonAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {

			try {
				DecimalFormat format = new DecimalFormat("##.00"); //$NON-NLS-1$

				PosButton button = (PosButton) e.getSource();

				String command = button.getActionCommand();

				if (command.equals("exactAmount")) { //$NON-NLS-1$
					double dueAmount = getDueAmount();
					txtTenderedAmount.setText(NumberUtil.formatNumber(dueAmount));
				}
				else if (command.equals("nextAmount")) { //$NON-NLS-1$

					double dd = NumberUtil.parse(txtDueAmount.getText()).doubleValue();
					double amount = Math.ceil(dd);

					txtTenderedAmount.setText(String.valueOf(format.format(amount)));
				}
				else {
					if (clearPreviousAmount) {
						txtTenderedAmount.setText("0"); //$NON-NLS-1$
						clearPreviousAmount = false;
					}

					double x = NumberUtil.parse(txtTenderedAmount.getText()).doubleValue();//FIXME: what if exception occurs?
					double y = Double.parseDouble(command);
					double z = x + y;
					txtTenderedAmount.setText(String.valueOf(format.format(z)));
				}
			} catch (Exception e2) {
				org.apache.commons.logging.LogFactory.getLog(getClass()).error(e2);
			}
		}
	};

	public void updateView() {
		double dueAmount = getDueAmount();

		txtDueAmount.setText(NumberUtil.formatNumber(dueAmount));
		txtTenderedAmount.setText(NumberUtil.formatNumber(dueAmount));
	}

	public double getTenderedAmount() throws ParseException {
		double doubleValue = NumberUtil.parse(txtTenderedAmount.getText()).doubleValue();
		return doubleValue;
	}

	public GroupSettleTicketDialog getSettleTicketView() {
		return groupSettleTicketView;
	}

	public void setSettleTicketView(GroupSettleTicketDialog groupSettleTicketView) {
		this.groupSettleTicketView = groupSettleTicketView;
	}

	protected double getDueAmount() {
		return groupSettleTicketView.getDueAmount();
	}

	public void setDefaultFocus() {
		txtTenderedAmount.requestFocus();
	}

}
