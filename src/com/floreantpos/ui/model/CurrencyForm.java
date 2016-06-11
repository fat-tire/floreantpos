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
/*
 * CurrencyEditor.java
 *
 * Created on August 3, 2006, 1:49 AM
 */

package com.floreantpos.ui.model;

import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.Currency;
import com.floreantpos.model.dao.CurrencyDAO;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.util.POSUtil;

/**
 *
 * @author  MShahriar
 */
public class CurrencyForm extends BeanEditor {

	private JLabel lblName;
	private JLabel lblExchangeRate;
	private JLabel lblSymbol;
	private FixedLengthTextField tfName;
	private JTextField tfSymbol;
	private DoubleTextField tfExchangeRate;
	private JCheckBox chkMain;

	public CurrencyForm() {
		this(new Currency());
	}

	public CurrencyForm(Currency currency) {
		initComponents();

		setBean(currency);
	}

	private void initComponents() {
		JPanel contentPanel = new JPanel(new MigLayout("fill"));
		lblName = new JLabel(com.floreantpos.POSConstants.NAME + ":");
		tfName = new FixedLengthTextField();

		lblExchangeRate = new JLabel("Exchange Rate:");
		tfExchangeRate = new DoubleTextField();

		lblSymbol = new JLabel("Symbol");
		tfSymbol = new JTextField();

		chkMain = new JCheckBox("Main");

		contentPanel.add(lblName, "cell 0 0");
		contentPanel.add(tfName, "cell 1 0");
		contentPanel.add(lblSymbol, "cell 0 1");
		contentPanel.add(tfSymbol, "grow,cell 1 1");
		contentPanel.add(lblExchangeRate, "cell 0 2");
		contentPanel.add(tfExchangeRate, "grow,cell 1 2");
		contentPanel.add(chkMain, "cell 1 3");

		add(contentPanel);
	}

	@Override
	public boolean save() {

		try {
			if (!updateModel())
				return false;

			Currency currency = (Currency) getBean();
			CurrencyDAO dao = new CurrencyDAO();
			dao.saveOrUpdate(currency);
		} catch (Exception e) {
			MessageDialog.showError(e);
			return false;
		}

		return true;
	}

	@Override
	protected void updateView() {
		Currency currency = (Currency) getBean();
		if (currency == null) {
			return;
		}
		tfName.setText(currency.getName());
		tfSymbol.setText(currency.getSymbol());
		tfExchangeRate.setText("" + currency.getExchangeRate()); //$NON-NLS-1$
		chkMain.setSelected(currency.isMain());
	}

	@Override
	protected boolean updateModel() {
		Currency currency = (Currency) getBean();

		String name = tfName.getText();
		if (POSUtil.isBlankOrNull(name)) {
			MessageDialog.showError(com.floreantpos.POSConstants.NAME_REQUIRED);
			return false;
		}
		currency.setName(name);
		currency.setSymbol(tfSymbol.getText());
		currency.setExchangeRate(tfExchangeRate.getDouble());
		currency.setMain(chkMain.isSelected());

		if (chkMain.isSelected()) {
			CurrencyDAO dao = new CurrencyDAO();
			List<Currency> currencyList = dao.findAll();

			Session session = null;
			Transaction transaction = null;
			try {
				session = CurrencyDAO.getInstance().createNewSession();
				transaction = session.beginTransaction();

				for (Currency curr : currencyList) {
					curr.setMain(false);
					session.saveOrUpdate(curr);
				}
				transaction.commit();
			} catch (Exception ex) {
				transaction.rollback();
				BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, ex);
			} finally {
				session.close();
			}
		}

		return true;
	}

	public String getDisplayText() {
		Currency currency = (Currency) getBean();
		if (currency.getId() == null) {
			return "New Currency";
		}
		return "Edit Currency";
	}
}
