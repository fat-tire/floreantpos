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

import org.jdesktop.swingx.JXTable;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.MenuCategoryForm;

public class MenuCategoryExplorer extends TransparentPanel {
	private List<MenuCategory> categoryList;
	
	private JTable table;

	private CategoryExplorerTableModel tableModel;
	
	public MenuCategoryExplorer() {
		MenuCategoryDAO dao = new MenuCategoryDAO();
		categoryList = dao.findAll();
		
		tableModel = new CategoryExplorerTableModel();
		table = new JXTable(tableModel);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		
		setLayout(new BorderLayout(5,5));
		add(new JScrollPane(table));
		
		JButton addButton = new JButton(POSConstants.ADD);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					
					MenuCategoryForm editor = new MenuCategoryForm();
					BeanEditorDialog dialog = new BeanEditorDialog(editor);
					dialog.open();
					
					if (dialog.isCanceled())
						return;
					
					MenuCategory foodCategory = (MenuCategory) editor.getBean();
					tableModel.addCategory(foodCategory);
					
				} catch (Exception x) {
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}
			
		});
		
		JButton editButton = new JButton(POSConstants.EDIT);
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					index = table.convertRowIndexToModel(index);
					MenuCategory category = categoryList.get(index);

					MenuCategoryForm editor = new MenuCategoryForm(category);
					BeanEditorDialog dialog = new BeanEditorDialog(editor);
					dialog.open();
					if (dialog.isCanceled())
						return;

					table.repaint();
				} catch (Throwable x) {
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}
			
		});
		JButton deleteButton = new JButton(POSConstants.DELETE);
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					index = table.convertRowIndexToModel(index);
					if (ConfirmDeleteDialog.showMessage(MenuCategoryExplorer.this, POSConstants.CONFIRM_DELETE, POSConstants.DELETE) == ConfirmDeleteDialog.YES) {
						MenuCategory category = categoryList.get(index);
						MenuCategoryDAO dao = new MenuCategoryDAO();
						dao.delete(category);
						tableModel.deleteCategory(category, index);
					}
				} catch (Exception x) {
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}
			
		});

		TransparentPanel panel = new TransparentPanel();
		panel.add(addButton);
		panel.add(editButton);
		panel.add(deleteButton);
		add(panel, BorderLayout.SOUTH);
	}
	
	class CategoryExplorerTableModel extends AbstractTableModel {
		String[] columnNames = {POSConstants.ID, POSConstants.NAME, 
				POSConstants.TRANSLATED_NAME, POSConstants.BEVERAGE, 
				POSConstants.VISIBLE, POSConstants.SORT_ORDER, POSConstants.BUTTON_COLOR
		};
		
		public int getRowCount() {
			if(categoryList == null) {
				return 0;
			}
			return categoryList.size();
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
			if(categoryList == null)
				return ""; //$NON-NLS-1$
			
			MenuCategory category = categoryList.get(rowIndex);
			
			switch(columnIndex) {
				case 0:
					return String.valueOf(category.getId());
					
				case 1:
					return category.getName();
					
				case 2:
					return category.getTranslatedName();
					
				case 3:
					return Boolean.valueOf(category.isBeverage());
					
				case 4:
					return Boolean.valueOf(category.isVisible());
					
				case 5:
					return category.getSortOrder();
					
				case 6:
					if(category.getButtonColor() != null) {
						return new Color(category.getButtonColor());
					}
					
					return null;
			}
			return null;
		}

		public void addCategory(MenuCategory category) {
			int size = categoryList.size();
			categoryList.add(category);
			fireTableRowsInserted(size, size);
		}
		
		public void deleteCategory(MenuCategory category, int index) {
			categoryList.remove(category);
			fireTableRowsDeleted(index, index);
		}
	}
}
