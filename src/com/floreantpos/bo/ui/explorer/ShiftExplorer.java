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

import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.Shift;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.ShiftDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.swing.ListTableModel;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.ShiftEntryDialog;
import com.floreantpos.util.ShiftUtil;

public class ShiftExplorer extends TransparentPanel {
	
	private JTable table;
	private ShiftTableModel tableModel;
	
	public ShiftExplorer() {
		List<Shift> shifts = new ShiftDAO().findAll();
		
		tableModel = new ShiftTableModel(shifts);
		table = new JTable(tableModel);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		
		setLayout(new BorderLayout(5,5));
		add(new JScrollPane(table));
		
		JButton addButton = new JButton(com.floreantpos.POSConstants.ADD);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ShiftEntryDialog dialog = new ShiftEntryDialog();
					dialog.open();
					if (dialog.isCanceled())
						return;
					Shift shift = dialog.getShift();
					tableModel.addItem(shift);
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

					Shift shift = (Shift) tableModel.getRowData(index);
					ShiftEntryDialog dialog = new ShiftEntryDialog();
					dialog.setShift(shift);
					dialog.open();
					if (dialog.isCanceled())
						return;

					tableModel.updateItem(index);
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

					if (ConfirmDeleteDialog.showMessage(ShiftExplorer.this, com.floreantpos.POSConstants.CONFIRM_DELETE, com.floreantpos.POSConstants.DELETE) == ConfirmDeleteDialog.YES) {
						User user = (User) tableModel.getRowData(index);
						UserDAO.getInstance().delete(user);
						tableModel.deleteItem(index);
					}
				} catch (Exception x) {
				BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}
			
		});

		TransparentPanel panel = new TransparentPanel();
		panel.add(addButton);
		panel.add(editButton);
		//panel.add(deleteButton);
		add(panel, BorderLayout.SOUTH);
	}
	
	class ShiftTableModel extends ListTableModel {
		
		ShiftTableModel(List list){
			super(new String[] {com.floreantpos.POSConstants.ID, com.floreantpos.POSConstants.NAME, com.floreantpos.POSConstants.START_TIME, com.floreantpos.POSConstants.END_TIME}, list);
		}
		

		public Object getValueAt(int rowIndex, int columnIndex) {
			Shift shift = (Shift) rows.get(rowIndex);
			
			switch(columnIndex) {
				case 0:
					return String.valueOf(shift.getId());
					
				case 1:
					return shift.getName();
					
				case 2:
					return ShiftUtil.buildShiftTimeRepresentation(shift.getStartTime());
					
				case 3:
					return ShiftUtil.buildShiftTimeRepresentation(shift.getEndTime());
			}
			return null;
		}
	}
}
