package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.jdesktop.swingx.JXTable;

import com.floreantpos.main.Application;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.dao.ModifierDAO;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.MenuModifierForm;

public class ModifierExplorer extends TransparentPanel {
	private List<MenuModifier> modifierList;
	ModifierExplorerTableModel tableModel;
	private String currencySymbol;
	private JTable table;

	public ModifierExplorer() {
		currencySymbol = Application.getCurrencySymbol();
		
		ModifierDAO dao = new ModifierDAO();
		modifierList = dao.findAll();

		tableModel = new ModifierExplorerTableModel();
		table = new JTable(tableModel);
		table.setAutoResizeMode(JXTable.AUTO_RESIZE_OFF);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		//table.packAll();
		
		setLayout(new BorderLayout(5, 5));
		add(new JScrollPane(table));

		TransparentPanel panel = new TransparentPanel();
		ExplorerButtonPanel explorerButton = new ExplorerButtonPanel();
		JButton editButton = explorerButton.getEditButton();
		JButton addButton = explorerButton.getAddButton();
		JButton deleteButton = explorerButton.getDeleteButton();

		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;
					MenuModifier modifier = modifierList.get(index);

					MenuModifierForm editor = new MenuModifierForm(modifier);
					BeanEditorDialog dialog = new BeanEditorDialog(editor, Application.getInstance().getBackOfficeWindow(), true);
					dialog.open();
					if (dialog.isCanceled())
						return;

					table.repaint();
				} catch (Throwable x) {
					MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}
		});

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					MenuModifierForm editor = new MenuModifierForm();
					BeanEditorDialog dialog = new BeanEditorDialog(editor, Application.getInstance().getBackOfficeWindow(), true);
					dialog.open();
					if (dialog.isCanceled())
						return;
					MenuModifier modifier = (MenuModifier) editor.getBean();
					tableModel.addModifier(modifier);
				} catch (Throwable x) {
					MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;
					if (ConfirmDeleteDialog.showMessage(ModifierExplorer.this, com.floreantpos.POSConstants.CONFIRM_DELETE, com.floreantpos.POSConstants.DELETE) != ConfirmDeleteDialog.NO) {
						MenuModifier category = modifierList.get(index);
						ModifierDAO modifierDAO = new ModifierDAO();
						modifierDAO.delete(category);
						tableModel.deleteModifier(category, index);
					}
				} catch (Throwable x) {
					MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}

			}

		});

		panel.add(addButton);
		panel.add(editButton);
		panel.add(deleteButton);
		add(panel, BorderLayout.SOUTH);
	}

	class ModifierExplorerTableModel extends AbstractTableModel {
		private static final String UNASSIGNED = "<unassigned>";
		String[] columnNames = {com.floreantpos.POSConstants.ID, com.floreantpos.POSConstants.NAME, com.floreantpos.POSConstants.PRICE + " (" + currencySymbol + ")", com.floreantpos.POSConstants.EXTRA_PRICE, com.floreantpos.POSConstants.TAX + "(%)", com.floreantpos.POSConstants.MODIFIER_GROUP };

		public int getRowCount() {
			if (modifierList == null) {
				return 0;
			}
			return modifierList.size();
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
			if (modifierList == null)
				return "";

			MenuModifier modifier = modifierList.get(rowIndex);

			switch (columnIndex) {
				case 0:
					return String.valueOf(modifier.getId());

				case 1:
					return String.valueOf(modifier.getName());

				case 2:
					return Double.valueOf(modifier.getPrice());
					
				case 3:
					return Double.valueOf(modifier.getExtraPrice());
					
				case 4:
					if(modifier.getTax() == null) {
						return UNASSIGNED;
					}
					return Double.valueOf(modifier.getTax().getRate());
					
				case 5:
					if(modifier.getModifierGroup() == null) {
						return UNASSIGNED;
					}
					return modifier.getModifierGroup().getName();
			}
			return null;
		}

		public void addModifier(MenuModifier category) {
			int size = modifierList.size();
			modifierList.add(category);
			fireTableRowsInserted(size, size);

		}

		public void deleteModifier(MenuModifier category, int index) {
			modifierList.remove(category);
			fireTableRowsDeleted(index, index);
		}
	}
}
