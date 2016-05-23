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
import com.floreantpos.model.DeliveryCharge;
import com.floreantpos.model.dao.DeliveryChargeDAO;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.DeliveryChargeForm;

public class DeliveryChargeExplorer extends TransparentPanel {
	private List<DeliveryCharge> deliveryChargeList;

	private JTable table;

	private DeliveryChargeExplorerTableModel tableModel;

	public DeliveryChargeExplorer() {
		deliveryChargeList = DeliveryChargeDAO.getInstance().findAll();

		tableModel = new DeliveryChargeExplorerTableModel();
		table = new JTable(tableModel);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());

		setLayout(new BorderLayout(5, 5));
		add(new JScrollPane(table));

		JButton addButton = new JButton(com.floreantpos.POSConstants.ADD);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					DeliveryChargeForm editor = new DeliveryChargeForm();
					BeanEditorDialog dialog = new BeanEditorDialog(editor);
					dialog.open();
					if (dialog.isCanceled())
						return;

					tableModel.addDeliveryCharge((DeliveryCharge) editor.getBean());
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

					DeliveryCharge deliveryCharge = deliveryChargeList.get(index);

					DeliveryChargeForm deliveryChargeForm = new DeliveryChargeForm(deliveryCharge);
					BeanEditorDialog dialog = new BeanEditorDialog(deliveryChargeForm);
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

					if (ConfirmDeleteDialog.showMessage(DeliveryChargeExplorer.this, com.floreantpos.POSConstants.CONFIRM_DELETE,
							com.floreantpos.POSConstants.DELETE) == ConfirmDeleteDialog.YES) {
						DeliveryCharge deliveryCharge = deliveryChargeList.get(index);
						DeliveryChargeDAO.getInstance().delete(deliveryCharge);
						tableModel.deleteDeliveryCharge(deliveryCharge, index);
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

	class DeliveryChargeExplorerTableModel extends AbstractTableModel {
		String[] columnNames = { POSConstants.ID, "Start Range", "End Range", "Charge Amount" };

		public int getRowCount() {
			if (deliveryChargeList == null) {
				return 0;
			}
			return deliveryChargeList.size();
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
			if (deliveryChargeList == null)
				return ""; //$NON-NLS-1$

			DeliveryCharge deliveryCharge = deliveryChargeList.get(rowIndex);

			switch (columnIndex) {
				case 0:
					return String.valueOf(deliveryCharge.getId());
				case 1:
					return deliveryCharge.getStartRange();

				case 2:
					return deliveryCharge.getEndRange();
				case 3:
					return deliveryCharge.getChargeAmount();

			}
			return null;
		}

		public void addDeliveryCharge(DeliveryCharge deliveryCharge) {
			int size = deliveryChargeList.size();
			deliveryChargeList.add(deliveryCharge);
			fireTableRowsInserted(size, size);
		}

		public void deleteDeliveryCharge(DeliveryCharge deliveryCharge, int index) {
			deliveryChargeList.remove(deliveryCharge);
			fireTableRowsDeleted(index, index);
		}
	}
}
