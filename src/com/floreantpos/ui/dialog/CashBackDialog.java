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
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.model.CashDrawer;
import com.floreantpos.model.Currency;
import com.floreantpos.model.CurrencyBalance;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.NumericKeypad;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.util.NumberUtil;

public class CashBackDialog extends OkCancelOptionDialog implements FocusListener {
	private double dueAmount;
	private double totalCashBackAmount;

	private List<CurrencyRow> currencyRows = new ArrayList();
	private CashDrawer cashDrawer;
	private double ticketDueAmount;

	public CashBackDialog(double dueAmount, CashDrawer cashDrawer) {
		super();
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

		JPanel inputPanel = new JPanel();
		GridLayout gridLayout = new GridLayout(0, 3, 5, 5);
		inputPanel.setLayout(gridLayout);

		JLabel lblRemainingAmount = getJLabel("Remaining Amount", Font.BOLD, 16, JLabel.CENTER);
		JLabel lblCurrency = getJLabel("Currency", Font.BOLD, 16, JLabel.CENTER);
		JLabel lblTendered = getJLabel("Cash Back", Font.BOLD, 16, JLabel.CENTER);

		inputPanel.add(lblRemainingAmount);
		inputPanel.add(lblCurrency);
		inputPanel.add(lblTendered);

		for (CurrencyBalance currencyBalance : cashDrawer.getCurrencyBalanceList()) {
			String dueAmountByCurrency = NumberUtil.formatNumber(currencyBalance.getCurrency().getExchangeRate() * dueAmount);
			JLabel lblRemainingBalance = getJLabel(dueAmountByCurrency, Font.PLAIN, 16, JLabel.LEFT);
			JLabel currencyName = getJLabel(currencyBalance.getCurrency().getName(), Font.PLAIN, 16, JLabel.LEFT);
			DoubleTextField tfCashBackAmount = getDoubleTextField("", Font.PLAIN, 16, JTextField.RIGHT);

			inputPanel.add(lblRemainingBalance);
			inputPanel.add(currencyName);
			inputPanel.add(tfCashBackAmount);

			tfCashBackAmount.addFocusListener(this);

			CurrencyRow item = new CurrencyRow(currencyBalance.getCurrency(), lblRemainingBalance, tfCashBackAmount);
			currencyRows.add(item);
		}
		contentPane.add(inputPanel, "cell 0 0,alignx left,aligny top"); //$NON-NLS-1$

		NumericKeypad numericKeypad = new NumericKeypad();
		contentPane.add(numericKeypad, "cell 0 1"); //$NON-NLS-1$
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

	private class CurrencyRow {
		Currency currency;
		JLabel lblRemainingBalance;
		DoubleTextField tfCashBackAmount;
		double cashBackAmount = 0;

		public CurrencyRow(Currency currency, JLabel lblRemainingBalance, DoubleTextField tfCashBackAmount) {
			this.currency = currency;
			this.lblRemainingBalance = lblRemainingBalance;
			this.tfCashBackAmount = tfCashBackAmount;
		}

		void setCashBackAmount(double cashBackAmount) {
			this.cashBackAmount = cashBackAmount;
		}
	}

	@Override
	public void doOk() {
		if (isOver()) {
			POSMessageDialog.showMessage("Cash back amount is over");
			return;
		}
		for (CurrencyRow rowItem : currencyRows) {
			CurrencyBalance item = cashDrawer.getCurrencyBalance(rowItem.currency);
			double cashBackAmount = rowItem.cashBackAmount;
			item.setCashBackAmount(cashBackAmount);
			item.setBalance(NumberUtil.roundToTwoDigit(item.getBalance()- item.getCashBackAmount()));
		}
		setCanceled(false);
		dispose();
	}

	@Override
	public void focusGained(FocusEvent e) {
	}

	@Override
	public void focusLost(FocusEvent e) {
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
			rowItem.lblRemainingBalance.setText(NumberUtil.formatNumber(remainingBalance));
		}
	}

	private boolean isOver() {
		totalCashBackAmount = NumberUtil.roundToTwoDigit(totalCashBackAmount);
		dueAmount = NumberUtil.roundToTwoDigit(dueAmount);
		if (totalCashBackAmount > dueAmount) {
			return true;
		}
		return false;
	}

	public double getTenderedAmount() {
		return totalCashBackAmount;
	}

	public void setTotalDueAmount(double totalDueAmount) {
		this.ticketDueAmount = totalDueAmount;
	}
}
