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
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.bo.ui.CustomCellRenderer;
import com.floreantpos.model.Multiplier;
import com.floreantpos.model.dao.MultiplierDAO;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.MultiplierForm;
import com.floreantpos.util.POSUtil;

public class MultiplierExplorer extends TransparentPanel {
	private List<Multiplier> multiplierList;
	private JTable table;
	private MultiplierExplorerTableModel tableModel;

	public MultiplierExplorer() {
		multiplierList = MultiplierDAO.getInstance().findAll();

		tableModel = new MultiplierExplorerTableModel();
		table = new JTable(tableModel);
		table.setDefaultRenderer(Object.class, new CustomCellRenderer());
		table.getColumnModel().getColumn(6).setCellRenderer(new PosTableRenderer());

		setLayout(new BorderLayout(5, 5));
		add(new JScrollPane(table));

		JButton addButton = new JButton(com.floreantpos.POSConstants.ADD);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					MultiplierForm editor = new MultiplierForm();
					BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
					dialog.open();
					if (dialog.isCanceled())
						return;

					tableModel.addMultiplier((Multiplier) editor.getBean());
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

					Multiplier multiplier = multiplierList.get(index);

					MultiplierForm multiplierForm = new MultiplierForm(multiplier);
					BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), multiplierForm);
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

					if (ConfirmDeleteDialog.showMessage(MultiplierExplorer.this, com.floreantpos.POSConstants.CONFIRM_DELETE,
							com.floreantpos.POSConstants.DELETE) == ConfirmDeleteDialog.YES) {
						Multiplier multiplier = multiplierList.get(index);
						MultiplierDAO.getInstance().delete(multiplier);
						tableModel.deleteMultiplier(multiplier, index);
					}
				} catch (Exception x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		JButton btnSetAsDefault = new JButton("Set default");
		btnSetAsDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;
					Multiplier selectedMultiplier = multiplierList.get(index);
					for (Multiplier item : multiplierList) {
						if (selectedMultiplier.getName().equals(item.getName()))
							item.setDefaultMultiplier(true);
						else
							item.setDefaultMultiplier(false);
					}
					MultiplierDAO.getInstance().saveOrUpdateMultipliers(multiplierList);
					tableModel.fireTableDataChanged();
				} catch (Exception x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		TransparentPanel panel = new TransparentPanel();
		panel.add(addButton);
		panel.add(editButton);
		panel.add(deleteButton);
		panel.add(btnSetAsDefault);
		add(panel, BorderLayout.SOUTH);
	}

	class MultiplierExplorerTableModel extends AbstractTableModel {
		String[] columnNames = { POSConstants.NAME, "Prefix", POSConstants.RATE, POSConstants.SORT_ORDER, POSConstants.BUTTON_COLOR, POSConstants.TEXT_COLOR,
				"Default" };

		public int getRowCount() {
			if (multiplierList == null) {
				return 0;
			}
			return multiplierList.size();
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
			if (multiplierList == null)
				return ""; //$NON-NLS-1$

			Multiplier multiplier = multiplierList.get(rowIndex);

			switch (columnIndex) {
				case 0:
					return multiplier.getName();
				case 1:
					return multiplier.getTicketPrefix();
				case 2:
					return multiplier.getRate();
				case 3:
					return multiplier.getSortOrder();
				case 4:
					if (multiplier.getButtonColor() != null) {
						return new Color(multiplier.getButtonColor());
					}

					return null;
				case 5:
					if (multiplier.getButtonColor() != null) {
						return new Color(multiplier.getButtonColor());
					}

					return null;
				case 6:
					return multiplier.isDefaultMultiplier();

			}
			return null;
		}

		public void addMultiplier(Multiplier multiplier) {
			int size = multiplierList.size();
			multiplierList.add(multiplier);
			fireTableRowsInserted(size, size);
		}

		public void deleteMultiplier(Multiplier multiplier, int index) {
			multiplierList.remove(multiplier);
			fireTableRowsDeleted(index, index);
		}
	}
}
