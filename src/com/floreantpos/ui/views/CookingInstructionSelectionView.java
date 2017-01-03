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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang3.StringUtils;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.CookingInstruction;
import com.floreantpos.model.TicketItemCookingInstruction;
import com.floreantpos.model.dao.CookingInstructionDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.ui.dialog.OkCancelOptionDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class CookingInstructionSelectionView extends OkCancelOptionDialog {
	private JTable table;

	private List<TicketItemCookingInstruction> ticketItemCookingInstructions;
	JTextField tfCookingInstruction = new JTextField();

	private List<CookingInstruction> cookingInstructions;

	public CookingInstructionSelectionView() {
		super(Application.getPosWindow(), true);
		createUI();
		updateView();
	}

	private void createUI() {
		setTitle(Messages.getString("CookingInstructionSelectionView.1"));
		setTitlePaneText(Messages.getString("CookingInstructionSelectionView.1")); //$NON-NLS-1$
		getContentPanel().setBorder(new EmptyBorder(10, 20, 0, 20));

		JScrollPane scrollPane = new JScrollPane();
		table = new JTable();
		table.setRowHeight(35);
		scrollPane.setViewportView(table);

		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				int index = table.getSelectedRow();
				if (index < 0)
					return;
				CookingInstructionTableModel model = (CookingInstructionTableModel) table.getModel();
				CookingInstruction cookingInstruction = model.rowsList.get(index);
				tfCookingInstruction.setText(cookingInstruction.getDescription());
			}
		});

		tfCookingInstruction.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				doFilter();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				doFilter();
			}

			@Override
			public void keyPressed(KeyEvent e) {

			}
		});

		PosButton btnSave = new PosButton(IconFactory.getIcon("save.png"));
		btnSave.setText(POSConstants.SAVE.toUpperCase());
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String instruction = tfCookingInstruction.getText();
				if (instruction == null || instruction.isEmpty()) {
					POSMessageDialog.showMessage(Application.getPosWindow(), "Instruction cannot be empty.");
					return;
				}
				CookingInstruction cookingInstruction = new CookingInstruction();
				cookingInstruction.setDescription(instruction);
				CookingInstructionDAO.getInstance().save(cookingInstruction);
				updateView();
				CookingInstructionTableModel model = (CookingInstructionTableModel) table.getModel();
				table.getSelectionModel().addSelectionInterval(model.getRowCount() - 1, model.getRowCount() - 1);
			}
		});
		JPanel contentPanel = new JPanel(new MigLayout("fill,wrap 1,inset 0"));
		contentPanel.add(scrollPane, "grow");
		contentPanel.add(tfCookingInstruction, "h 35!,split 2,grow");
		contentPanel.add(btnSave, "h 35!,w 90!");
		QwertyKeyPad keyPad = new QwertyKeyPad();
		contentPanel.add(keyPad, "grow");
		getContentPanel().add(contentPanel);
	}

	private void doFilter() {
		String text = tfCookingInstruction.getText().toLowerCase();
		List<CookingInstruction> filteredInstructions = new ArrayList<>();
		for (CookingInstruction i : cookingInstructions) {
			String description = i.getDescription().toLowerCase();
			if (description.contains(text)) {
				filteredInstructions.add(i);
			}
		}
		CookingInstructionTableModel model = (CookingInstructionTableModel) table.getModel();
		model.rowsList = filteredInstructions;
		model.fireTableDataChanged();
	}

	@Override
	public void doOk() {
		int[] selectedRows = table.getSelectedRows();

		/*if (selectedRows.length == 0) {
			POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("CookingInstructionSelectionView.0")); //$NON-NLS-1$
			return;
		}*/

		if (ticketItemCookingInstructions == null) {
			ticketItemCookingInstructions = new ArrayList<TicketItemCookingInstruction>(selectedRows.length);
		}

		CookingInstructionTableModel model = (CookingInstructionTableModel) table.getModel();
		String inputInstruction = tfCookingInstruction.getText();
		if (selectedRows.length == 1) {
			if (inputInstruction == null || inputInstruction.isEmpty()) {
				POSMessageDialog.showMessage(Application.getPosWindow(), "Instruction cannot be empty.");
				return;
			}
			CookingInstruction ci = model.rowsList.get(selectedRows[0]);
			if (!inputInstruction.equals(ci.getDescription())) {
				TicketItemCookingInstruction cookingInstruction = new TicketItemCookingInstruction();
				cookingInstruction.setDescription(inputInstruction);
				ticketItemCookingInstructions.add(cookingInstruction);
			}
			else {
				TicketItemCookingInstruction cookingInstruction = new TicketItemCookingInstruction();
				cookingInstruction.setDescription(ci.getDescription());
				ticketItemCookingInstructions.add(cookingInstruction);
			}
		}
		else if (selectedRows.length == 0) {
			if (inputInstruction == null || inputInstruction.isEmpty()) {
				POSMessageDialog.showMessage(Application.getPosWindow(), "Instruction cannot be empty.");
				return;
			}
			TicketItemCookingInstruction cookingInstruction = new TicketItemCookingInstruction();
			cookingInstruction.setDescription(inputInstruction);
			ticketItemCookingInstructions.add(cookingInstruction);
		}
		else {
			boolean newInstruction = false;
			for (int i = 0; i < selectedRows.length; i++) {
				CookingInstruction ci = model.rowsList.get(selectedRows[i]);
				TicketItemCookingInstruction cookingInstruction = new TicketItemCookingInstruction();
				cookingInstruction.setDescription(ci.getDescription());
				if (StringUtils.isNotEmpty(inputInstruction) && !ci.getDescription().equals(inputInstruction)) {
					newInstruction = true;
				}
				ticketItemCookingInstructions.add(cookingInstruction);
			}
			if (newInstruction) {
				TicketItemCookingInstruction cookingInstruction = new TicketItemCookingInstruction();
				cookingInstruction.setDescription(inputInstruction);
				ticketItemCookingInstructions.add(cookingInstruction);
			}
		}
		setCanceled(false);
		dispose();
	}

	protected void updateView() {
		cookingInstructions = CookingInstructionDAO.getInstance().findAll();
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
