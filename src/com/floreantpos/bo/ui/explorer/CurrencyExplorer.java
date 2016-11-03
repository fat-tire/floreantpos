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

import org.jdesktop.swingx.JXTable;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.Currency;
import com.floreantpos.model.dao.CurrencyDAO;
import com.floreantpos.swing.BeanTableModel;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.CurrencyForm;
import com.floreantpos.util.POSUtil;

public class CurrencyExplorer extends TransparentPanel {
	private JXTable table;
	private BeanTableModel<Currency> tableModel;

	public CurrencyExplorer() {
		tableModel = new BeanTableModel<Currency>(Currency.class);

		tableModel.addColumn(POSConstants.ID.toUpperCase(), "id"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.NAME.toUpperCase(), "name"); //$NON-NLS-1$
		tableModel.addColumn("CODE", "code"); //$NON-NLS-1$
		tableModel.addColumn("SYMBOL", "symbol"); //$NON-NLS-1$
		tableModel.addColumn("RATE", "exchangeRate"); //$NON-NLS-1$
		tableModel.addColumn("MAIN", "main"); //$NON-NLS-1$
		tableModel.addColumn("TOLERANCE", "tolerance"); //$NON-NLS-1$

		tableModel.addRows(CurrencyDAO.getInstance().findAll());
		table = new JXTable(tableModel);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());

		setLayout(new BorderLayout(5, 5));
		add(new JScrollPane(table));

		JButton addButton = new JButton(com.floreantpos.POSConstants.ADD);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					CurrencyForm editor = new CurrencyForm();
					BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
					dialog.open();
					if (dialog.isCanceled())
						return;

					tableModel.addRow((Currency) editor.getBean());
					refresh();
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

					index = table.convertColumnIndexToModel(index);
					Currency currency = tableModel.getRow(index);

					CurrencyForm currencyForm = new CurrencyForm(currency);
					BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), currencyForm);
					dialog.open();
					if (dialog.isCanceled())
						return;
					refresh();
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

					index = table.convertColumnIndexToModel(index);

					if (ConfirmDeleteDialog.showMessage(POSUtil.getBackOfficeWindow(), com.floreantpos.POSConstants.CONFIRM_DELETE,
							com.floreantpos.POSConstants.DELETE) == ConfirmDeleteDialog.YES) {
						Currency currency = tableModel.getRow(index);
						CurrencyDAO.getInstance().delete(currency);
						tableModel.removeRow(currency);
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

	protected BeanTableModel<Currency> getModel() {
		return tableModel;
	}

	private void refresh() {
		List<Currency> currencyList = CurrencyDAO.getInstance().findAll();
		tableModel.getRows().clear();
		tableModel.addRows(currencyList);
		table.repaint();
	}
}
