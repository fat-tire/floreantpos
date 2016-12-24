
package com.floreantpos.ui.model;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.model.MenuItemSize;
import com.floreantpos.model.dao.MenuItemSizeDAO;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.IntegerTextField;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.util.POSUtil;

/**
 *
 * @author  MShahriar
 */
public class MenuItemSizeForm extends BeanEditor {

	private FixedLengthTextField tfName;
	private FixedLengthTextField tfDescription;
	private FixedLengthTextField tfTranslatedName;
	private IntegerTextField tfSortOrder;
	private DoubleTextField tfSizeInInch;

	public MenuItemSizeForm() {
		this(new MenuItemSize());
	}

	public MenuItemSizeForm(MenuItemSize menuItemSize) {
		initComponents();

		setBean(menuItemSize);

	}

	private void initComponents() {
		JPanel contentPanel = new JPanel(new MigLayout("", "[][]", ""));

		JLabel lblName = new JLabel(com.floreantpos.POSConstants.NAME + ":");
		JLabel lblDescription = new JLabel("Description");
		JLabel lblTranslatedName = new JLabel("Translated Name");
		JLabel lblSortOrder = new JLabel("Sort Order");
		JLabel lblSize = new JLabel("Size in Inch");

		tfName = new FixedLengthTextField(60);
		tfName.setColumns(30);
		tfDescription = new FixedLengthTextField(120);
		tfDescription.setColumns(30);
		tfTranslatedName = new FixedLengthTextField(60);
		tfTranslatedName.setColumns(30);
		tfSortOrder = new IntegerTextField();
		tfSortOrder.setColumns(10);
		tfSizeInInch = new DoubleTextField();
		tfSizeInInch.setColumns(10);

		contentPanel.add(lblName, "");
		contentPanel.add(tfName, "");
		contentPanel.add(lblTranslatedName, "newline");
		contentPanel.add(tfTranslatedName, "");
		contentPanel.add(lblDescription, "newline");
		contentPanel.add(tfDescription, "");
		contentPanel.add(lblSize, "newline");
		contentPanel.add(tfSizeInInch, "");
		contentPanel.add(lblSortOrder, "newline");
		contentPanel.add(tfSortOrder, "");

		add(contentPanel);
	}

	@Override
	public boolean save() {

		try {
			if (!updateModel())
				return false;

			MenuItemSize menuItemSize = (MenuItemSize) getBean();
			MenuItemSizeDAO dao = new MenuItemSizeDAO();
			dao.saveOrUpdate(menuItemSize);
		} catch (Exception e) {
			MessageDialog.showError(e);
			return false;
		}

		return true;
	}

	@Override
	protected void updateView() {
		MenuItemSize menuItemSize = (MenuItemSize) getBean();
		if (menuItemSize == null) {
			return;
		}
		tfName.setText(menuItemSize.getName());
		tfDescription.setText(menuItemSize.getDescription());
		tfTranslatedName.setText(menuItemSize.getTranslatedName());
		tfSortOrder.setText(String.valueOf(menuItemSize.getSortOrder()));
		tfSizeInInch.setText(String.valueOf(menuItemSize.getSizeInInch()));

	}

	@Override
	protected boolean updateModel() {
		MenuItemSize menuItemSize = (MenuItemSize) getBean();

		String name = tfName.getText();
		String description = tfDescription.getText();
		String translatedName = tfTranslatedName.getText();
		int sortOrder = tfSortOrder.getInteger();
		double size = tfSizeInInch.getDouble();
		if (POSUtil.isBlankOrNull(name)) {
			MessageDialog.showError("Name is required");
			return false;
		}
		/*if (POSUtil.isBlankOrNull(description)) {
			description = "";
		}
		if (POSUtil.isBlankOrNull(translatedName)) {
			translatedName = "";
		}
		if (POSUtil.isBlankOrNull(sortOrder)) {
			sortOrder = "";
		}*/

		menuItemSize.setName(name);
		menuItemSize.setDescription(description);
		menuItemSize.setTranslatedName(translatedName);
		menuItemSize.setSortOrder(sortOrder);
		menuItemSize.setSizeInInch(size);

		MenuItemSizeDAO dao = new MenuItemSizeDAO();
		dao.saveOrUpdate(menuItemSize);

		return true;
	}

	public String getDisplayText() {
		MenuItemSize menuItemSize = (MenuItemSize) getBean();
		if (menuItemSize.getId() == null) {
			return "New Size";
		}
		return "Edit Size";
	}
}
