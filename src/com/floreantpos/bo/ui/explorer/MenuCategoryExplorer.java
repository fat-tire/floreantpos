package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXTable;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.swing.BeanTableModel;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.MenuCategoryForm;

public class MenuCategoryExplorer extends TransparentPanel {
	
	private JXTable table;
	private BeanTableModel<MenuCategory> tableModel;
	
	public MenuCategoryExplorer() {
		tableModel = new BeanTableModel<MenuCategory>(MenuCategory.class);
		tableModel.addColumn(POSConstants.ID.toUpperCase(), "id"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.NAME.toUpperCase(), "name"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.TRANSLATED_NAME.toUpperCase(), "translatedName"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.BEVERAGE.toUpperCase(), "beverage"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.VISIBLE.toUpperCase(), "visible"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.SORT_ORDER.toUpperCase(), "sortOrder"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.BUTTON_COLOR.toUpperCase(), "buttonColor"); //$NON-NLS-1$
		
		tableModel.addRows(MenuCategoryDAO.getInstance().findAll());
		
		table = new JXTable(tableModel);
		
		setLayout(new BorderLayout(5,5));
		add(new JScrollPane(table));
		
		addButtonPanel();
	}

	private void addButtonPanel() {
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
					tableModel.addRow(foodCategory);
					
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
					MenuCategory category = tableModel.getRow(index);

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
						MenuCategory category = tableModel.getRow(index);
						MenuCategoryDAO dao = new MenuCategoryDAO();
						dao.delete(category);
						tableModel.removeRow(index);
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
}
