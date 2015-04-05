package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;

import org.jdesktop.swingx.JXTable;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.dao.ModifierDAO;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.MenuModifierForm;

public class ModifierExplorer extends TransparentPanel {
	private List<MenuModifier> modifierList;
	ModifierExplorerTableModel tableModel;
	private String currencySymbol;
	private JXTable table;

	public ModifierExplorer() {
		currencySymbol = Application.getCurrencySymbol();
		
		ModifierDAO dao = new ModifierDAO();
		modifierList = dao.findAll();

		tableModel = new ModifierExplorerTableModel();
		table = new JXTable(tableModel);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		
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
					
					index = table.convertRowIndexToModel(index);
					MenuModifier modifier = modifierList.get(index);

					MenuModifierForm editor = new MenuModifierForm(modifier);
					BeanEditorDialog dialog = new BeanEditorDialog(editor);
					dialog.open();
					if (dialog.isCanceled())
						return;

					table.repaint();
				} catch (Throwable x) {
				BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}
		});

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					MenuModifierForm editor = new MenuModifierForm();
					BeanEditorDialog dialog = new BeanEditorDialog(editor);
					dialog.open();
					if (dialog.isCanceled())
						return;
					MenuModifier modifier = (MenuModifier) editor.getBean();
					tableModel.addModifier(modifier);
				} catch (Throwable x) {
				BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;
					
					index = table.convertRowIndexToModel(index);
					
					if (ConfirmDeleteDialog.showMessage(ModifierExplorer.this, com.floreantpos.POSConstants.CONFIRM_DELETE, com.floreantpos.POSConstants.DELETE) != ConfirmDeleteDialog.NO) {
						MenuModifier category = modifierList.get(index);
						ModifierDAO modifierDAO = new ModifierDAO();
						modifierDAO.delete(category);
						tableModel.deleteModifier(category, index);
					}
				} catch (Throwable x) {
				BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}

			}

		});

		panel.add(addButton);
		panel.add(editButton);
		panel.add(deleteButton);
		add(panel, BorderLayout.SOUTH);
	}

	class ModifierExplorerTableModel extends AbstractTableModel {
		String[] columnNames = {com.floreantpos.POSConstants.ID, com.floreantpos.POSConstants.NAME, POSConstants.TRANSLATED_NAME,
				com.floreantpos.POSConstants.PRICE + " (" + currencySymbol + ")", 
				com.floreantpos.POSConstants.EXTRA_PRICE, com.floreantpos.POSConstants.TAX + "(%)", 
				com.floreantpos.POSConstants.MODIFIER_GROUP,
				POSConstants.BUTTON_COLOR, POSConstants.SORT_ORDER}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

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
				return ""; //$NON-NLS-1$

			MenuModifier modifier = modifierList.get(rowIndex);

			switch (columnIndex) {
				case 0:
					return String.valueOf(modifier.getId());

				case 1:
					return modifier.getName();
					
				case 2:
					return modifier.getTranslatedName();

				case 3:
					return Double.valueOf(modifier.getPrice());
					
				case 4:
					return Double.valueOf(modifier.getExtraPrice());
					
				case 5:
					if(modifier.getTax() == null) {
						return ""; //$NON-NLS-1$
					}
					return Double.valueOf(modifier.getTax().getRate());
					
				case 6:
					if(modifier.getModifierGroup() == null) {
						return ""; //$NON-NLS-1$
					}
					return modifier.getModifierGroup().getName();
					
				case 7:
					if(modifier.getButtonColor() != null) {
						return new Color(modifier.getButtonColor());
					}
					
					return null;
					
				case 8:
					return modifier.getSortOrder();
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
