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
package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.Currency;
import com.floreantpos.model.dao.CurrencyDAO;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.CurrencyForm;

public class CurrencyExplorer extends TransparentPanel {
	private List<Currency> currencyList;

	private JTable table;
	private CurrencyExplorerTableModel tableModel;

	public CurrencyExplorer() {
		currencyList = CurrencyDAO.getInstance().findAll();

		tableModel = new CurrencyExplorerTableModel();
		table = new JTable(tableModel);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());

		setLayout(new BorderLayout(5, 5));
		add(new JScrollPane(table));

		JButton addButton = new JButton(com.floreantpos.POSConstants.ADD);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					CurrencyForm editor = new CurrencyForm();
					BeanEditorDialog dialog = new BeanEditorDialog(editor);
					dialog.open();
					if (dialog.isCanceled())
						return;

					tableModel.addCurrency((Currency) editor.getBean());
				} catch (Exception x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		JButton editButton = new JButton(com.floreantpos.POSConstants.EDIT);
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					Currency currency = currencyList.get(index);

					CurrencyForm currencyForm = new CurrencyForm(currency);
					BeanEditorDialog dialog = new BeanEditorDialog(currencyForm);
					dialog.open();
					if (dialog.isCanceled())
						return;

					table.repaint();
				} catch (Throwable x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});
		JButton deleteButton = new JButton(com.floreantpos.POSConstants.DELETE);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					if (ConfirmDeleteDialog
							.showMessage(CurrencyExplorer.this, com.floreantpos.POSConstants.CONFIRM_DELETE, com.floreantpos.POSConstants.DELETE) == ConfirmDeleteDialog.YES) {
						Currency currency = currencyList.get(index);
						CurrencyDAO.getInstance().delete(currency);
						tableModel.deleteCurrency(currency, index);
					}
				} catch (Exception x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		TransparentPanel panel = new TransparentPanel();
		panel.add(addButton);
		panel.add(editButton);
		panel.add(deleteButton);
		add(panel, BorderLayout.SOUTH);
	}

	class CurrencyExplorerTableModel extends AbstractTableModel {
		String[] columnNames = { POSConstants.ID, POSConstants.NAME, "SYMBOL", POSConstants.RATE ,"MAIN"};

		public int getRowCount() {
			if (currencyList == null) {
				return 0;
			}
			return currencyList.size();
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			if (currencyList == null)
				return ""; //$NON-NLS-1$

			Currency currency = currencyList.get(rowIndex);

			switch (columnIndex) {
				case 0:
					return String.valueOf(currency.getId());

				case 1:
					return currency.getName();

				case 2:
					return currency.getSymbol();

				case 3:
					return currency.getExchangeRate();
					
				case 4:
					return currency.isMain();
			}

			return null;
		}

		public void addCurrency(Currency currency) {
			int size = currencyList.size();
			currencyList.add(currency);
			fireTableRowsInserted(size, size);
		}

		public void deleteCurrency(Currency currency, int index) {
			currencyList.remove(currency);
			fireTableRowsDeleted(index, index);
		}
	}
}
