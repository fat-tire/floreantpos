package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXTable;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.swing.BeanTableModel;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.MenuItemForm;

public class MenuItemExplorer extends TransparentPanel {

	private JXTable table;
	private BeanTableModel<MenuItem> tableModel;

	public MenuItemExplorer() {
		tableModel = new BeanTableModel<MenuItem>(MenuItem.class);
		tableModel.addColumn(POSConstants.ID.toUpperCase(), "id"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.NAME.toUpperCase(), "name"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.TRANSLATED_NAME.toUpperCase(), "translatedName"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.PRICE.toUpperCase() + " (" + Application.getCurrencySymbol() + ")", "price"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		tableModel.addColumn(POSConstants.VISIBLE.toUpperCase(), "visible"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.DISCOUNT.toUpperCase() + "(%)", "discountRate"); //$NON-NLS-1$ //$NON-NLS-2$
		tableModel.addColumn(POSConstants.FOOD_GROUP.toUpperCase(), "parent"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.TAX.toUpperCase(), "tax"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.SORT_ORDER.toUpperCase(), "sortOrder"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.BUTTON_COLOR.toUpperCase(), "buttonColor"); //$NON-NLS-1$
		tableModel.addColumn(POSConstants.IMAGE.toUpperCase(), "image");

		tableModel.addRows(MenuItemDAO.getInstance().findAll());

		//set the cell renderers -->
		
		table = new JXTable(tableModel);
		table.setRowHeight(120);
		
		table.setDefaultRenderer(Color.class, new ColorCellRenderer());
	

		setLayout(new BorderLayout(5, 5));
		add(new JScrollPane(table));

		add(createButtonPanel(), BorderLayout.SOUTH);
		add(buildSearchForm(), BorderLayout.NORTH);
	}

	private JPanel buildSearchForm() {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("", "[][]30[][]30[]", "[]20[]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		JLabel nameLabel = new JLabel(Messages.getString("MenuItemExplorer.0")); //$NON-NLS-1$
		JLabel groupLabel = new JLabel(Messages.getString("MenuItemExplorer.1")); //$NON-NLS-1$
		final JTextField nameField = new JTextField(15);

		try {

			List<MenuGroup> menuGroupList = MenuGroupDAO.getInstance().findAll();

			final JComboBox cbGroup = new JComboBox();

			cbGroup.addItem(Messages.getString("MenuItemExplorer.2")); //$NON-NLS-1$
			for (MenuGroup s : menuGroupList) {
				cbGroup.addItem(s);
			}

			JButton searchBttn = new JButton(Messages.getString("MenuItemExplorer.3")); //$NON-NLS-1$

			panel.add(nameLabel, "align label"); //$NON-NLS-1$
			panel.add(nameField);
			panel.add(groupLabel);
			panel.add(cbGroup);
			panel.add(searchBttn);

			Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder title = BorderFactory.createTitledBorder(loweredetched, "Search"); //$NON-NLS-1$
			title.setTitleJustification(TitledBorder.LEFT);
			panel.setBorder(title);

			searchBttn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					String txName = nameField.getText();
					Object selectedItem = cbGroup.getSelectedItem();

					List<MenuItem> similarItem = null;
					if (selectedItem instanceof MenuGroup) {
						similarItem = MenuItemDAO.getInstance().getSimilar(txName, (MenuGroup) selectedItem);
					}
					else {
						similarItem = MenuItemDAO.getInstance().getSimilar(txName, null);
					}

					tableModel.removeAll();
					tableModel.addRows(similarItem);

				}
			});

		} catch (Throwable x) {
			BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
		}

		return panel;
	}

	private TransparentPanel createButtonPanel() {
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

					MenuItem menuItem = tableModel.getRow(index);
					menuItem = MenuItemDAO.getInstance().initialize(menuItem);

					tableModel.setRow(index, menuItem);

					MenuItemForm editor = new MenuItemForm(menuItem);
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

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					MenuItemForm editor = new MenuItemForm();
					BeanEditorDialog dialog = new BeanEditorDialog(editor);
					dialog.open();
					if (dialog.isCanceled())
						return;
					MenuItem foodItem = (MenuItem) editor.getBean();
					tableModel.addRow(foodItem);
				} catch (Throwable x) {
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
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

					if (ConfirmDeleteDialog.showMessage(MenuItemExplorer.this, POSConstants.CONFIRM_DELETE, POSConstants.DELETE) != ConfirmDeleteDialog.NO) {
						MenuItem item = tableModel.getRow(index);
						MenuItemDAO foodItemDAO = new MenuItemDAO();
						foodItemDAO.delete(item);

						tableModel.removeRow(index);
					}
				} catch (Throwable x) {
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		TransparentPanel panel = new TransparentPanel();
		panel.add(addButton);
		panel.add(editButton);
		panel.add(deleteButton);
		return panel;
	}
}
