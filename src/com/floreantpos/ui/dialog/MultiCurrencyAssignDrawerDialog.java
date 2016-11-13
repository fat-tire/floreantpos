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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.CashDrawer;
import com.floreantpos.model.Currency;
import com.floreantpos.model.CurrencyBalance;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.dao.CashDrawerDAO;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.NumericKeypad;
import com.floreantpos.swing.PosUIManager;

public class MultiCurrencyAssignDrawerDialog extends OkCancelOptionDialog {
	private List<Currency> currencyList;
	private double initialAmount;
	private double totalAmount;

	private List<CurrencyRow> currencyRows = new ArrayList();
	private CashDrawer cashDrawer;

	public MultiCurrencyAssignDrawerDialog(double initialAmount, List<Currency> currencyList) {
		super(Application.getPosWindow());
		this.initialAmount = initialAmount;
		this.currencyList = currencyList;
		init();
	}

	private void init() {
		JPanel contentPane = getContentPanel();
		setOkButtonText(POSConstants.SAVE_BUTTON_TEXT);
		setTitle("Enter drawer amount");
		setTitlePaneText("Enter drawer amount");
		setResizable(false);

		MigLayout layout = new MigLayout("inset 0", "[grow,fill]", "[grow,fill]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		contentPane.setLayout(layout);

		JPanel inputPanel = new JPanel();
		GridLayout gridLayout = new GridLayout(0, 2, 10, 5);
		inputPanel.setLayout(gridLayout);

		JLabel lblCurrency = getJLabel("Currency", Font.BOLD, 16, JLabel.CENTER);
		JLabel lblAmount = getJLabel("Amount", Font.BOLD, 16, JLabel.CENTER);

		inputPanel.add(lblCurrency);
		inputPanel.add(lblAmount);

		for (Currency currency : currencyList) {
			JLabel currencyName = getJLabel(currency.getName(), Font.PLAIN, 16, JLabel.CENTER);
			DoubleTextField tfTenderedAmount = getDoubleTextField("", Font.PLAIN, 16, JTextField.RIGHT);

			inputPanel.add(currencyName);
			inputPanel.add(tfTenderedAmount);

			CurrencyRow item = new CurrencyRow(currency, tfTenderedAmount);
			currencyRows.add(item);
		}
		contentPane.add(inputPanel, "cell 0 0,alignx left,aligny top"); //$NON-NLS-1$

		NumericKeypad numericKeypad = new NumericKeypad();
		contentPane.add(new JSeparator(), "gapbottom 5,gaptop 5,cell 0 1");//$NON-NLS-1$
		contentPane.add(numericKeypad, "cell 0 2"); //$NON-NLS-1$
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
		DoubleTextField tfAmount;
		double initialAmount = 0;

		public CurrencyRow(Currency currency, DoubleTextField tfAmount) {
			this.currency = currency;
			this.tfAmount = tfAmount;
		}

		void setInitialAmount(double initialAmount) {
			this.initialAmount = initialAmount;
		}
	}

	@Override
	public void doOk() {
		Terminal terminal = Application.getInstance().getTerminal();

		cashDrawer = CashDrawerDAO.getInstance().findByTerminal(terminal);
		if (cashDrawer == null) {
			cashDrawer = new CashDrawer();
			cashDrawer.setTerminal(terminal);

			if (cashDrawer.getCurrencyBalanceList() == null) {
				cashDrawer.setCurrencyBalanceList(new HashSet());
			}
		}

		totalAmount = 0;
		for (CurrencyRow rowItem : currencyRows) {
			CurrencyBalance item = cashDrawer.getCurrencyBalance(rowItem.currency);
			if (item == null) {
				item = new CurrencyBalance();
				item.setCurrency(rowItem.currency);
				item.setCashDrawer(cashDrawer);
				cashDrawer.addTocurrencyBalanceList(item);
			}
			double amount = rowItem.tfAmount.getDouble();
			if (Double.isNaN(amount)) {
				amount = 0;
			}
			item.setBalance(amount);
			totalAmount += (amount / rowItem.currency.getExchangeRate());
		}
		setCanceled(false);
		dispose();
	}

	public CashDrawer getCashDrawer() {
		return cashDrawer;
	}

	public double getTotalAmount() {
		return totalAmount;
	}
}
