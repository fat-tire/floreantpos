package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXTable;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.BeanTableModel;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.TransparentPanel;
//import com.floreantpos.test.MyTableModel;
//import com.floreantpos.test.TablePanel;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.MenuItemForm;

public class MenuItemExplorer extends TransparentPanel {

	private JXTable table;
	private BeanTableModel<MenuItem> tableModel;

	// my code
	// private List<MenuItem> menuItem;
	// private List<MenuGroup> grp;

	//

	public MenuItemExplorer() {
		tableModel = new BeanTableModel<MenuItem>(MenuItem.class);
		tableModel.addColumn(POSConstants.ID.toUpperCase(), "id");
		tableModel.addColumn(POSConstants.NAME.toUpperCase(), "name");
		tableModel.addColumn(POSConstants.TRANSLATED_NAME.toUpperCase(), "translatedName");
		tableModel.addColumn(POSConstants.PRICE.toUpperCase() + " (" + Application.getCurrencySymbol() + ")", "price");
		tableModel.addColumn(POSConstants.VISIBLE.toUpperCase(), "visible");
		tableModel.addColumn(POSConstants.DISCOUNT.toUpperCase() + "(%)", "discountRate");
		tableModel.addColumn(POSConstants.FOOD_GROUP.toUpperCase(), "parent");
		tableModel.addColumn(POSConstants.TAX.toUpperCase(), "tax");
		tableModel.addColumn(POSConstants.SORT_ORDER.toUpperCase(), "sortOrder");
		tableModel.addColumn(POSConstants.BUTTON_COLOR.toUpperCase(), "buttonAsColor");
		// my temp code
		tableModel.addColumn("image", "imageAsIcon");

		tableModel.addRows(MenuItemDAO.getInstance().findAll());

		table = new JXTable(tableModel);

		table.setRowHeight(120);
		// table.getColumnModel().getColumn(4).setPreferredWidth(150);

		setLayout(new BorderLayout(5, 5));
		add(new JScrollPane(table));

		add(createButtonPanel(), BorderLayout.SOUTH);
		add(buildSearchForm(), BorderLayout.NORTH);
	}

	// my code
	// public void setMenu(List<MenuItem> menuItem) {
	// this.menuItem = menuItem;
	// fireTableDataChanged();
	// }

	private JPanel buildSearchForm() {
		// List<MenuItem> menuItem;
		List<MenuGroup> grpName;
		// JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("", "[][]30[][]30[]", "[]20[]"));

		// JLabel searchlable = new JLabel("Search");
		JLabel nameLabel = new JLabel("Name : ");
		JLabel groupLabel = new JLabel("Group : ");
		final JTextField nameField = new JTextField(15);
		// gap.add("full list");
		grpName = MenuGroupDAO.getInstance().findAll();
		// final JComboBox comboGroup = new JComboBox(grp.toArray());
		final JComboBox cbGroup = new JComboBox();
		// DefaultComboBoxModel<List> model = new DefaultComboBoxModel<List>();
		// DefaultComboBoxModel<String> model = new
		// DefaultComboBoxModel<String>();
		// comboGroup.setModel(model);
		cbGroup.addItem("FULL  LIST");
		for (MenuGroup s : grpName) {
			cbGroup.addItem(s);
		}
		// comboGroup.addItem("egList<MenuGroup> grpName;g");
		// for(String s : grp.toArray()){

		// }
		// model.addElement(grpName);

		// model.addElement("FULL  LIST");
		// model.addElement("egg")
		// comboGroup.addItem("egg");

		// comboGroup.addItem("FULL  LIST");
		// comboGroup.addItem("egg");
		// String [] stockArr = (String[])grp.toArray();
		// String[] stockArr = new String[grp.size()];
		// stockArr = grp.toArray(stockArr);

		// model.addElement(grp);
		// comboGroup.setModel(new DefaultComboBoxModel(grp.toArray()));

		// model.addElement(menuItem);
		// model.addElement(grp);

		// comboGroup.addItem(grp.toArray());
		// comboGroup.addItem(grp);

		// comboGroup.addItemListener(new ItemListener() {
		//
		// @Override
		// public void itemStateChanged(ItemEvent e) {
		// //if (e.getStateChange() == ItemEvent.SELECTED) {
		// //int id = e.getStateChange();
		// // int groupName = (int)e.getItem();
		// Object selectedItem = comboGroup.getSelectedItem();
		// //if(selectedItem instanceof String) {

		// if(selectedItem instanceof String) {
		// //String cbGroupName = (String)e.getSelectedItem();
		// List<MenuItem> similarGroup =
		// MenuItemDAO.getInstance().getAsGroup(selectedItem);
		// //MenuItemDAO.getInstance().findByParent(selectedItem);
		// tableModel.removeAll();
		// tableModel.addRows(similarGroup);
		// }
		// else if (selectedItem instanceof MenuGroup) {
		//
		// }
		// List<MenuItem> similarGroup =
		// MenuItemDAO.getInstance().getAsGroup(groupName);
		// List<MenuItem> similarGroup = MenuItemDAO.getInstance()
		// .getAsGroup(id);
		// List<MenuItem> similarGroup =
		// MenuItemDAO.getInstance().findByParent(groupName);

		// tableModel.removeAll();
		// tableModel.addRows(similarGroup);

		// }
		// });
		// JButton searchBttn = new JButton("Search");
		JButton searchBttn = new JButton("Search");

		// optional code
		// JComboBox comboGroup2 = new JComboBox();
		// comboGroup2.addItem("full");
		// comboGroup2.addItem("egg");
		// comboGroup2.addItem("milk");
		// comboGroup2.addItem("soda");
		// comboGroup2.addItem("FAVOURITE");
		// comboGroup2.addItemListener(new ItemListener() {
		//
		// @Override
		// public void itemStateChanged(ItemEvent e) {
		// if (e.getStateChange() == ItemEvent.SELECTED) {
		// String name = (String) e.getItem();
		// // String groupName = (String)comboGroup.getSelectedItem();
		// List<MenuItem> similarName = MenuItemDAO.getInstance()
		// .getSimilar(name);
		//
		// tableModel.removeAll();
		// tableModel.addRows(similarName);
		// }
		// }
		// });
		//

		// panel.add(searchlable, "span, center, gapbottom 10");
		panel.add(nameLabel, "align label");
		panel.add(nameField);
		panel.add(groupLabel);
		// panel.add(groupLabel, "align label");
		panel.add(cbGroup);
		panel.add(searchBttn);
		// panel.add(comboGroup2);

		TitledBorder title;
		Border loweredetched;
		loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		title = BorderFactory.createTitledBorder(loweredetched, "Search");
		title.setTitleJustification(TitledBorder.LEFT);
		panel.setBorder(title);

		// my next code

		// final TablePanel tablePanel = new TablePanel();
		// panel.add(tablePanel);
		searchBttn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// my extra
				// if (nameField.getText() != null) {

				// MenuGroup cbName =(MenuGroup) .getSelectedItem();
				// Object selectedItem = menuGroup.getSelectedItem();
				// if(selectedItem instanceof String) {
				//
				// }

				// String p = cbName.toString();
				// if(p=="FULL LIST"){
				// tableModel.addRows(MenuItemDAO.getInstance().findAll());
				// }
				// String cbGroupName = (string)comboGroup.getSelectedItem();
				// MenuItem menuItem = getBean();
				// MenuGroup menuGroup = MenuGroup.getParent(cbName);
				// cbGroup.setSelectedItem(menuItem.getParent());
				// menuItem.setParent((MenuGroup) cbGroup.getSelectedItem());
				String txName = nameField.getText();
				Object selectedItem = cbGroup.getSelectedItem();

				List<MenuItem> similarItem = null;
				if (selectedItem instanceof MenuGroup) {
					similarItem = MenuItemDAO.getInstance().getSimilar(txName, (MenuGroup) selectedItem);
				} else {
					similarItem = MenuItemDAO.getInstance().getSimilar(txName, null);
				}

				tableModel.removeAll();
				tableModel.addRows(similarItem);
				// }
				// else{
				// JFrame frame2 = new JFrame("Clicked");
				// frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				// frame2.setVisible(true);
				// frame2.setSize(300, 300);
				// JLabel label = new JLabel("nothing selected for search");
				// JPanel panel2 = new JPanel();
				// panel2.add(label);
				// frame2.add(panel2);
				// }
				//

			}
		});
		//
		return panel;
	}

	//

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
