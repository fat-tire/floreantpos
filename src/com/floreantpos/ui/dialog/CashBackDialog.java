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
package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.model.CashDrawer;
import com.floreantpos.model.Currency;
import com.floreantpos.model.CurrencyBalance;
import com.floreantpos.model.Ticket;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.NumericKeypad;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.util.CurrencyUtil;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.POSUtil;

public class CashBackDialog extends OkCancelOptionDialog implements FocusListener {
	private double dueAmount;
	private double totalCashBackAmount;

	private List<CurrencyRow> currencyRows = new ArrayList();
	private CashDrawer cashDrawer;
	private Ticket ticket;
	

	public CashBackDialog(Ticket ticket, double dueAmount, CashDrawer cashDrawer) {
		super();
		this.ticket=ticket; 
		this.dueAmount = dueAmount;
		this.cashDrawer = cashDrawer;
		init();
	}

	private void init() {
		JPanel contentPane = getContentPanel();
		setOkButtonText(POSConstants.SAVE_BUTTON_TEXT);
		setTitle("Enter cash back amount");
		setTitlePaneText("Enter cash back amount");

		MigLayout layout = new MigLayout("inset 0", "[grow,fill]", "[grow,fill]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		contentPane.setLayout(layout);

		JLabel lblDueAmount = getJLabel("Change Due Amount: " + NumberUtil.roundToTwoDigit(dueAmount), Font.BOLD, 16, JLabel.LEFT);
		contentPane.add(lblDueAmount, "cell 0 0,alignx left,aligny top"); //$NON-NLS-1$
		contentPane.add(new JSeparator(), "gapbottom 5,gaptop 10,cell 0 1");//$NON-NLS-1$

		JPanel inputPanel = new JPanel(new MigLayout("fill,inset 0,wrap 3", "[center][right][100px,right][][]", ""));

		JLabel lblCurrency = getJLabel("Currency", Font.BOLD, 16, JLabel.CENTER);
		JLabel lblRemainingAmount = getJLabel("Remaining Amount", Font.BOLD, 16, JLabel.CENTER);
		JLabel lblTendered = getJLabel("Cash Back", Font.BOLD, 16, JLabel.CENTER);

		inputPanel.add(lblCurrency);
		inputPanel.add(lblRemainingAmount, "gapleft 20");
		inputPanel.add(lblTendered, "center");
		//inputPanel.add(new JLabel(""));
		//inputPanel.add(new JLabel(""));

		List<Currency> currencyList = CurrencyUtil.getAllCurrency();
		for (Currency currency : currencyList) {
			String dueAmountByCurrency = NumberUtil.format3DigitNumber(currency.getExchangeRate() * dueAmount);
			JLabel currencyName = getJLabel(currency.getName(), Font.PLAIN, 16, JLabel.CENTER);
			JLabel lblRemainingBalance = getJLabel(dueAmountByCurrency, Font.PLAIN, 16, JLabel.RIGHT);
			DoubleTextField tfCashBackAmount = getDoubleTextField("", Font.PLAIN, 16, JTextField.RIGHT);
			PosButton btnExact = new PosButton("EXACT");
			PosButton btnRound = new PosButton("ROUND");

			inputPanel.add(currencyName);
			inputPanel.add(lblRemainingBalance);
			inputPanel.add(tfCashBackAmount, "grow");
			//inputPanel.add(btnExact, "h 30!");
			//inputPanel.add(btnRound, "h 30!");

			tfCashBackAmount.addFocusListener(this);

			CurrencyRow item = new CurrencyRow(currency, lblRemainingBalance, tfCashBackAmount, btnExact, btnRound);
			currencyRows.add(item);
		}
		contentPane.add(inputPanel, "cell 0 2,alignx left,aligny top"); //$NON-NLS-1$

		NumericKeypad numericKeypad = new NumericKeypad();
		contentPane.add(new JSeparator(), "gapbottom 5,gaptop 10,cell 0 3");//$NON-NLS-1$
		contentPane.add(numericKeypad, "cell 0 4"); //$NON-NLS-1$
	}

	private JLabel getJLabel(String text, int bold, int fontSize, int align) {
		JLabel lbl = new JLabel(text);
		lbl.setFont(lbl.getFont().deriveFont(bold, PosUIManager.getSize(fontSize)));
		lbl.setHorizontalAlignment(align);
		return lbl;
	}

	private DoubleTextField getDoubleTextField(String text, int bold, int fontSize, int align) {
		DoubleTextField tf = new DoubleTextField();
		tf.setText(text);
		tf.setFont(tf.getFont().deriveFont(bold, PosUIManager.getSize(fontSize)));
		tf.setHorizontalAlignment(align);
		tf.setBackground(Color.WHITE);
		return tf;
	}

	private class CurrencyRow implements ActionListener {
		Currency currency;
		JLabel lblRemainingBalance;
		DoubleTextField tfCashBackAmount;
		PosButton btnExact;
		PosButton btnRound;
		double cashBackAmount = 0;

		public CurrencyRow(Currency currency, JLabel lblRemainingBalance, DoubleTextField tfCashBackAmount, PosButton btnExact, PosButton btnRound) {
			this.currency = currency;
			this.lblRemainingBalance = lblRemainingBalance;
			this.tfCashBackAmount = tfCashBackAmount;
			this.btnExact = btnExact;
			this.btnRound = btnRound;

			btnExact.addActionListener(this);
			btnRound.addActionListener(this);
		}

		void setCashBackAmount(double cashBackAmount) {
			this.cashBackAmount = cashBackAmount;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source == btnExact) {
				tfCashBackAmount.setText(NumberUtil.format3DigitNumber(dueAmount * currency.getExchangeRate()));
			}
			else {
				tfCashBackAmount.setText(NumberUtil.format3DigitNumber(Math.ceil(dueAmount * currency.getExchangeRate())));
			}
		}
	}

	@Override
	public void doOk() {
		updateView();

		if (isCashBackNotEqual()) {
			POSMessageDialog.showMessage(POSUtil.getFocusedWindow(), "Invalid cash back amount.");//$NON-NLS-1$
			return;
		}
		for (CurrencyRow rowItem : currencyRows) {
			CurrencyBalance item = cashDrawer.getCurrencyBalance(rowItem.currency);
			double cashBackAmount = rowItem.cashBackAmount;
			if(rowItem.cashBackAmount>0){
				ticket.addProperty(rowItem.currency.getName()+"_CASH_BACK",String.valueOf(rowItem.cashBackAmount)); 
			}
			item.setCashBackAmount(cashBackAmount);
			item.setBalance(NumberUtil.roundToThreeDigit(item.getBalance() - item.getCashBackAmount()));
		}
		setCanceled(false);
		dispose();
	}

	@Override
	public void focusGained(FocusEvent e) {
	}

	@Override
	public void focusLost(FocusEvent e) {
		updateView();
	}

	private void updateView() {
		totalCashBackAmount = 0;
		for (CurrencyRow rowItem : currencyRows) {
			double value = rowItem.tfCashBackAmount.getDouble();
			if (Double.isNaN(value)) {
				value = 0.0;
			}
			rowItem.setCashBackAmount(value);
			totalCashBackAmount += (value / rowItem.currency.getExchangeRate());
		}
		for (CurrencyRow rowItem : currencyRows) {
			double remainingBalance = (dueAmount - totalCashBackAmount) * rowItem.currency.getExchangeRate();
			rowItem.lblRemainingBalance.setText(NumberUtil.format3DigitNumber(remainingBalance));
		}
	}

	private boolean isCashBackNotEqual() {
		totalCashBackAmount = NumberUtil.roundToThreeDigit(totalCashBackAmount);
		dueAmount = NumberUtil.roundToThreeDigit(dueAmount);
		if (totalCashBackAmount != dueAmount) {
			return true;
		}
		return false;
	}

	public double getTenderedAmount() {
		return totalCashBackAmount;
	}
}
