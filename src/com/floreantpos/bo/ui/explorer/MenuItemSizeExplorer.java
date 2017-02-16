package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXTable;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.MenuItemSize;
import com.floreantpos.model.dao.MenuItemSizeDAO;
import com.floreantpos.swing.BeanTableModel;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.MenuItemSizeForm;
import com.floreantpos.util.POSUtil;

public class MenuItemSizeExplorer extends TransparentPanel {
	private JXTable table;

	private BeanTableModel<MenuItemSize> tableModel;

	private List<MenuItemSize> menuItemSizeList;

	public MenuItemSizeExplorer() {
		tableModel = new BeanTableModel<MenuItemSize>(MenuItemSize.class);
		tableModel.addColumn(POSConstants.ID.toUpperCase(), "id"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.NAME.toUpperCase(), "name"); //$NON-NLS-1$
		tableModel.addColumn("TRANSLATED NAME", "translatedName"); //$NON-NLS-1$
		tableModel.addColumn("DESCRIPTION", "description"); //$NON-NLS-1$
		tableModel.addColumn("SIZE (in Inch)", "sizeInInch"); //$NON-NLS-1$
		tableModel.addColumn("SORT", "sortOrder"); //$NON-NLS-1$
		tableModel.addColumn("DEFAULT", "defaultSize"); //$NON-NLS-1$

		menuItemSizeList = MenuItemSizeDAO.getInstance().findAll();
		tableModel.addRows(menuItemSizeList);
		table = new JXTable(tableModel);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());

		setLayout(new BorderLayout(5, 5));
		add(new JScrollPane(table));

		JButton addButton = new JButton(com.floreantpos.POSConstants.ADD);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					MenuItemSizeForm editor = new MenuItemSizeForm();
					BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
					dialog.open();

					if (dialog.isCanceled())
						return;

					MenuItemSize foodCategory = (MenuItemSize) editor.getBean();
					tableModel.addRow(foodCategory);
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

					index = table.convertRowIndexToModel(index);
					MenuItemSize menuItemSize = tableModel.getRow(index);

					MenuItemSizeForm editor = new MenuItemSizeForm(menuItemSize);
					BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
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

					if (ConfirmDeleteDialog.showMessage(MenuItemSizeExplorer.this, com.floreantpos.POSConstants.CONFIRM_DELETE,
							com.floreantpos.POSConstants.DELETE) == ConfirmDeleteDialog.YES) {
						MenuItemSize menuItemSize = tableModel.getRow(index);
						MenuItemSizeDAO dao = new MenuItemSizeDAO();
						dao.delete(menuItemSize);
						tableModel.removeRow(index);
					}
				} catch (Exception x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		JButton defaultButton = new JButton("Set Default");
		defaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					index = table.convertRowIndexToModel(index);
					MenuItemSize menuItemSize = tableModel.getRow(index);

					for (MenuItemSize item : menuItemSizeList) {
						if (menuItemSize.getId() == item.getId())
							item.setDefaultSize(true);
						else
							item.setDefaultSize(false);
					}

					MenuItemSizeDAO dao = new MenuItemSizeDAO();
					dao.setDefault(menuItemSizeList);
					tableModel.fireTableDataChanged();
					table.revalidate();
					table.repaint();

				} catch (Exception x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		TransparentPanel panel = new TransparentPanel();
		panel.add(addButton);
		panel.add(editButton);
		panel.add(deleteButton);
		panel.add(defaultButton);
		add(panel, BorderLayout.SOUTH);
	}
}
