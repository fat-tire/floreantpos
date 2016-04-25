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
package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.CookingInstruction;
import com.floreantpos.model.TicketItemCookingInstruction;
import com.floreantpos.model.dao.CookingInstructionDAO;
import com.floreantpos.ui.dialog.OkCancelOptionDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class CookingInstructionSelectionView extends OkCancelOptionDialog {
	private JTable table;

	private List<TicketItemCookingInstruction> ticketItemCookingInstructions;

	public CookingInstructionSelectionView() {
		super(Application.getPosWindow(), true);
		createUI();
		updateView();
	}

	private void createUI() {
		setTitle(Messages.getString("CookingInstructionSelectionView.1"));
		setTitlePaneText(Messages.getString("CookingInstructionSelectionView.1")); //$NON-NLS-1$

		JScrollPane scrollPane = new JScrollPane();
		table = new JTable();
		table.setRowHeight(35);
		scrollPane.setViewportView(table);
		getContentPanel().add(scrollPane, BorderLayout.CENTER);
	}

	@Override
	public void doOk() {
		int[] selectedRows = table.getSelectedRows();

		if (selectedRows.length == 0) {
			POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("CookingInstructionSelectionView.0")); //$NON-NLS-1$
			return;
		}

		if (ticketItemCookingInstructions == null) {
			ticketItemCookingInstructions = new ArrayList<TicketItemCookingInstruction>(selectedRows.length);
		}

		CookingInstructionTableModel model = (CookingInstructionTableModel) table.getModel();
		for (int i = 0; i < selectedRows.length; i++) {
			CookingInstruction ci = model.rowsList.get(selectedRows[i]);
			TicketItemCookingInstruction cookingInstruction = new TicketItemCookingInstruction();
			cookingInstruction.setDescription(ci.getDescription());
			ticketItemCookingInstructions.add(cookingInstruction);
		}
		setCanceled(false);
		dispose();
	}

	protected void updateView() {
		List<CookingInstruction> cookingInstructions = CookingInstructionDAO.getInstance().findAll();
		table.setModel(new CookingInstructionTableModel(cookingInstructions));
	}

	public List<TicketItemCookingInstruction> getTicketItemCookingInstructions() {
		return ticketItemCookingInstructions;
	}

	class CookingInstructionTableModel extends AbstractTableModel {
		private final String[] columns = { Messages.getString("CookingInstructionSelectionView.2") }; //$NON-NLS-1$

		private List<CookingInstruction> rowsList;

		public CookingInstructionTableModel() {
		}

		public CookingInstructionTableModel(List<CookingInstruction> rows) {
			this.rowsList = rows;
		}

		@Override
		public int getRowCount() {
			if (rowsList == null) {
				return 0;
			}

			return rowsList.size();
		}

		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public String getColumnName(int column) {
			return columns[column];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (rowsList == null) {
				return null;
			}

			CookingInstruction row = rowsList.get(rowIndex);

			switch (columnIndex) {
				case 0:
					return row.getDescription();
			}
			return null;
		}
	}
}
