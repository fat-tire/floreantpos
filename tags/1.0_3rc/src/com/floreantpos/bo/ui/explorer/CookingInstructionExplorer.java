package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.floreantpos.main.Application;
import com.floreantpos.model.CookingInstruction;
import com.floreantpos.model.dao.CookingInstructionDAO;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class CookingInstructionExplorer extends TransparentPanel {
	private List<CookingInstruction> categoryList;
	
	private JTable table;

	private CookingInstructionTableModel tableModel;
	CookingInstructionDAO dao = new CookingInstructionDAO();
	
	public CookingInstructionExplorer() {
		categoryList = dao.findAll();
		
		tableModel = new CookingInstructionTableModel();
		table = new JTable(tableModel);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		
		setLayout(new BorderLayout(5,5));
		add(new JScrollPane(table));
		
		JButton addButton = new JButton(com.floreantpos.POSConstants.ADD);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String instruction = JOptionPane.showInputDialog(Application.getInstance().getBackOfficeWindow(), com.floreantpos.POSConstants.ENTER_INSTRUCTION_DESCRIPTION);
					if(instruction == null) {
						POSMessageDialog.showError(Application.getInstance().getBackOfficeWindow(), com.floreantpos.POSConstants.INSTRUCTION_CANNOT_BE_EMPTY);
						return;
					}
					if(instruction.length() > 60) {
						POSMessageDialog.showError(Application.getInstance().getBackOfficeWindow(), com.floreantpos.POSConstants.LONG_INSTRUCTION_ERROR);
						return;
					}
					
					CookingInstruction cookingInstruction = new CookingInstruction();
					cookingInstruction.setDescription(instruction);
					dao.save(cookingInstruction);
					
					tableModel.add(cookingInstruction);
				} catch (Exception x) {
					MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
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
					
					CookingInstruction cookingInstruction = categoryList.get(index);
					String instruction = JOptionPane.showInputDialog(Application.getInstance().getBackOfficeWindow(), com.floreantpos.POSConstants.ENTER_INSTRUCTION_DESCRIPTION, cookingInstruction.getDescription());

					if(instruction == null) {
						POSMessageDialog.showError(Application.getInstance().getBackOfficeWindow(), com.floreantpos.POSConstants.INSTRUCTION_CANNOT_BE_EMPTY);
						return;
					}
					if(instruction.length() > 60) {
						POSMessageDialog.showError(Application.getInstance().getBackOfficeWindow(), com.floreantpos.POSConstants.LONG_INSTRUCTION_ERROR);
						return;
					}
					cookingInstruction.setDescription(instruction);
					dao.saveOrUpdate(cookingInstruction);
					table.repaint();
				} catch (Throwable x) {
					MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
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

					if (ConfirmDeleteDialog.showMessage(CookingInstructionExplorer.this, com.floreantpos.POSConstants.CONFIRM_DELETE, com.floreantpos.POSConstants.DELETE) == ConfirmDeleteDialog.YES) {
						CookingInstruction cookingInstruction = categoryList.get(index);
						dao.delete(cookingInstruction);
						tableModel.delete(cookingInstruction, index);
					}
				} catch (Exception x) {
					MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}
			
		});

		TransparentPanel panel = new TransparentPanel();
		panel.add(addButton);
		panel.add(editButton);
		panel.add(deleteButton);
		add(panel, BorderLayout.SOUTH);
	}
	
	class CookingInstructionTableModel extends AbstractTableModel {
		String[] columnNames = {com.floreantpos.POSConstants.ID, com.floreantpos.POSConstants.DESCRIPTION};
		
		public int getRowCount() {
			if(categoryList == null) {
				return 0;
			}
			return categoryList.size();
		}

		public int getColumnCount() {
			return 2;
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
			if(categoryList == null)
				return "";
			
			CookingInstruction cookingInstruction = categoryList.get(rowIndex);
			
			switch(columnIndex) {
				case 0:
					return String.valueOf(cookingInstruction.getId());
					
				case 1:
					return cookingInstruction.getDescription();
					
			}
			return null;
		}

		public void add(CookingInstruction instruction) {
			int size = categoryList.size();
			categoryList.add(instruction);
			fireTableRowsInserted(size, size);
		}
		
		public void delete(CookingInstruction instruction, int index) {
			categoryList.remove(instruction);
			fireTableRowsDeleted(index, index);
		}
	}
}
