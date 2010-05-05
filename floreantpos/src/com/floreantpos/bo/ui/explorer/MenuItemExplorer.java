package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.floreantpos.main.Application;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.MenuItemForm;

public class MenuItemExplorer extends TransparentPanel {
	private List<MenuItem> itemList;

	private JTable table;
	private MenuItemExplorerTableModel tableModel;
	private String currencySymbol;
	
	public MenuItemExplorer() {
		currencySymbol = Application.getCurrencySymbol();
		
		MenuItemDAO dao = new MenuItemDAO();
		itemList = dao.findAll();

		tableModel = new MenuItemExplorerTableModel();
		tableModel.setRows(itemList);
		table = new JTable(tableModel);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());

		setLayout(new BorderLayout(5, 5));
		add(new JScrollPane(table));
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

					MenuItem menuItem = itemList.get(index);
					menuItem = MenuItemDAO.getInstance().initialize(menuItem);
					itemList.set(index, menuItem);
					
					MenuItemForm editor = new MenuItemForm(menuItem);
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
					MenuItemForm editor = new MenuItemForm();
					BeanEditorDialog dialog = new BeanEditorDialog(editor, Application.getInstance().getBackOfficeWindow(), true);
					dialog.open();
					if (dialog.isCanceled())
						return;
					MenuItem foodItem = (MenuItem) editor.getBean();
					tableModel.addMenuItem(foodItem);
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

					if (ConfirmDeleteDialog.showMessage(MenuItemExplorer.this, com.floreantpos.POSConstants.CONFIRM_DELETE, com.floreantpos.POSConstants.DELETE) != ConfirmDeleteDialog.NO) {
						MenuItem category = itemList.get(index);
						MenuItemDAO foodItemDAO = new MenuItemDAO();
						foodItemDAO.delete(category);
						tableModel.deleteMenuItem(category, index);
					}
				} catch (Throwable x) {
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
	
	class MenuItemExplorerTableModel extends ListTableModel {
		String[] columnNames = { com.floreantpos.POSConstants.ID, com.floreantpos.POSConstants.NAME, com.floreantpos.POSConstants.PRICE + " (" + currencySymbol + ")", com.floreantpos.POSConstants.VISIBLE, com.floreantpos.POSConstants.DISCOUNT + "(%)", com.floreantpos.POSConstants.FOOD_GROUP, com.floreantpos.POSConstants.TAX + " (%)" };

		MenuItemExplorerTableModel(){
			setColumnNames(columnNames);
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			MenuItem item = (MenuItem) rows.get(rowIndex);

			switch (columnIndex) {
				case 0:
					return String.valueOf(item.getId());

				case 1:
					return item.getName();

				case 2:
					return Double.valueOf(item.getPrice());
					
				case 3:
					return item.isVisible();

				case 4:
					return Double.valueOf(item.getDiscountRate());

				case 5:
					if (item.getParent() != null) {
						return item.getParent().getName();
					}
					return "<unassigned>";

				case 6:
					if (item.getTax() != null) {
						return Double.valueOf(item.getTax().getRate());
					}
					return "<unassigned>";

			}
			return null;
		}

		public void addMenuItem(MenuItem menuItem) {
			super.addItem(menuItem);

		}

		public void deleteMenuItem(MenuItem category, int index) {
			super.deleteItem(index);
		}
	}
}
