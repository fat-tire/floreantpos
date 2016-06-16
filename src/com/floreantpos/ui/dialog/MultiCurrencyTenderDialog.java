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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.CashDrawer;
import com.floreantpos.model.Currency;
import com.floreantpos.model.CurrencyBalance;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.CashDrawerDAO;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.NumericKeypad;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.POSUtil;

public class MultiCurrencyTenderDialog extends OkCancelOptionDialog implements DocumentListener {
	private List<Currency> currencyList;
	private double dueAmount;
	private double totalTenderedAmount;

	private List<CurrencyRow> currencyRows = new ArrayList();
	private CashDrawer cashDrawer;
	private Ticket ticket;

	public MultiCurrencyTenderDialog(Ticket ticket, List<Currency> currencyList) {
		super();
		this.ticket = ticket;
		this.currencyList = currencyList;
		this.dueAmount = ticket.getDueAmount();
		init();
	}

	private void init() {
		JPanel contentPane = getContentPanel();
		setOkButtonText(POSConstants.SAVE_BUTTON_TEXT);
		setTitle("Enter tendered amount");
		setTitlePaneText("Enter tendered amount");
		setResizable(false);

		MigLayout layout = new MigLayout("inset 0", "[grow,fill]", "[grow,fill]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		contentPane.setLayout(layout);

		JLabel lblDueAmount = getJLabel("Due Amount: " + dueAmount, Font.BOLD, 16, JLabel.LEFT);
		contentPane.add(lblDueAmount, "cell 0 0,alignx left,aligny top"); //$NON-NLS-1$
		contentPane.add(new JSeparator(), "gapbottom 5,gaptop 10,cell 0 1");//$NON-NLS-1$

		JPanel inputPanel = new JPanel(new MigLayout("fill,inset 0,wrap 3", "[center][right][100px,right][][]", ""));

		JLabel lblCurrency = getJLabel("Currency", Font.BOLD, 16, JLabel.CENTER);
		JLabel lblRemainingAmount = getJLabel("Remaining Amount", Font.BOLD, 16, JLabel.CENTER);
		JLabel lblTendered = getJLabel("Tender", Font.BOLD, 16, JLabel.CENTER);

		inputPanel.add(lblCurrency);
		inputPanel.add(lblRemainingAmount, "gapleft 20");
		inputPanel.add(lblTendered, "center");
		//inputPanel.add(new JLabel(""));
		//inputPanel.add(new JLabel(""));

		for (Currency currency : currencyList) {
			String dueAmountByCurrency = NumberUtil.format3DigitNumber(currency.getExchangeRate() * dueAmount);
			JLabel lblRemainingBalance = getJLabel(dueAmountByCurrency, Font.PLAIN, 16, JLabel.RIGHT);
			JLabel currencyName = getJLabel(currency.getName(), Font.PLAIN, 16, JLabel.CENTER);
			DoubleTextField tfTenderedAmount = getDoubleTextField("", Font.PLAIN, 16, JTextField.RIGHT);
			PosButton btnExact = new PosButton("EXACT");
			PosButton btnRound = new PosButton("ROUND");

			inputPanel.add(currencyName);
			inputPanel.add(lblRemainingBalance);
			inputPanel.add(tfTenderedAmount, "grow");
			//inputPanel.add(btnExact, "h 30!");
			//inputPanel.add(btnRound, "h 30!");

			tfTenderedAmount.getDocument().addDocumentListener(this);

			CurrencyRow item = new CurrencyRow(currency, lblRemainingBalance, tfTenderedAmount, btnExact, btnRound);
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
		DoubleTextField tfTenderdAmount;
		PosButton btnExact;
		PosButton btnRound;
		double creditAmount = 0;
		double cashBackAmount = 0;
		double tenderAmount = 0;

		public CurrencyRow(Currency currency, JLabel lblRemainingBalance, DoubleTextField tfTenderedAmount, PosButton btnExact, PosButton btnRound) {
			this.currency = currency;
			this.lblRemainingBalance = lblRemainingBalance;
			this.tfTenderdAmount = tfTenderedAmount;
			this.btnExact = btnExact;
			this.btnRound = btnRound;

			btnExact.addActionListener(this);
			btnRound.addActionListener(this);
		}

		void setCashBackAmount(double cashBackAmount) {
			this.cashBackAmount = cashBackAmount;
		}

		void setTenderAmount(double tenderAmount) {
			this.tenderAmount = tenderAmount;
		}

		void setCreditAmount(double creditAmount) {
			this.creditAmount = creditAmount;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source == btnExact) {
				tfTenderdAmount.setText(NumberUtil.format3DigitNumber(dueAmount * currency.getExchangeRate()));
			}
			else {
				tfTenderdAmount.setText(NumberUtil.format3DigitNumber(Math.ceil(dueAmount * currency.getExchangeRate())));
			}
		}
	}

	@Override
	public void doOk() {
		if (totalTenderedAmount <= 0) {
			POSMessageDialog.showMessage(POSUtil.getFocusedWindow(), "Invalid Amount");//$NON-NLS-1$
			return;
		}

		Terminal terminal = Application.getInstance().getTerminal();

		cashDrawer = CashDrawerDAO.getInstance().findByTerminal(terminal);
		if (cashDrawer == null) {
			cashDrawer = new CashDrawer();
			cashDrawer.setTerminal(terminal);

			if (cashDrawer.getCurrencyBalanceList() == null) {
				cashDrawer.setCurrencyBalanceList(new HashSet());
			}
		}

		for (CurrencyRow rowItem : currencyRows) {
			CurrencyBalance item = cashDrawer.getCurrencyBalance(rowItem.currency);
			if (item == null) {
				item = new CurrencyBalance();
				item.setCurrency(rowItem.currency);
				item.setCashDrawer(cashDrawer);
				cashDrawer.addTocurrencyBalanceList(item);
			}
			double tenderAmount = rowItem.tenderAmount;
			double cashBackAmount = rowItem.cashBackAmount;
			item.setCashCreditAmount(tenderAmount);
			if (tenderAmount > 0) {
				ticket.addProperty(rowItem.currency.getName(), String.valueOf(tenderAmount));
			}
			item.setCashBackAmount(cashBackAmount);
			item.setBalance(item.getBalance() + item.getCashCreditAmount());
		}
		setCanceled(false);
		dispose();
	}

	public CashDrawer getCashDrawer() {
		return cashDrawer;
	}

	private void updateView() {
		totalTenderedAmount = 0;
		for (CurrencyRow rowItem : currencyRows) {
			double value = rowItem.tfTenderdAmount.getDouble();
			if (Double.isNaN(value)) {
				value = 0.0;
			}
			rowItem.setTenderAmount(value);
			totalTenderedAmount += (value / rowItem.currency.getExchangeRate());
			if (totalTenderedAmount <= dueAmount) {
				rowItem.setCreditAmount(value);
			}
			else {
				double remainingBalance = (dueAmount - totalTenderedAmount) * rowItem.currency.getExchangeRate();
				remainingBalance = Math.abs(remainingBalance);
				rowItem.setCreditAmount(value - remainingBalance);
				rowItem.setCashBackAmount(remainingBalance);
				break;
			}
		}
		for (CurrencyRow currInput : currencyRows) {
			double remainingBalance = (dueAmount - totalTenderedAmount) * currInput.currency.getExchangeRate();
			currInput.lblRemainingBalance.setText(NumberUtil.format3DigitNumber(remainingBalance));
		}
	}

	public boolean hasCashBack() {
		if (dueAmount < totalTenderedAmount) {
			return true;
		}
		return false;
	}

	public double getTenderedAmount() {
		return totalTenderedAmount;
	}

	public double getChangeDueAmount() {
		return totalTenderedAmount - dueAmount;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		updateView();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		updateView();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		updateView();
	}
}
