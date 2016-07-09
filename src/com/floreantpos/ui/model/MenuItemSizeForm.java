
package com.floreantpos.ui.model;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SortOrder;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.model.MenuItemSize;
import com.floreantpos.model.dao.MenuItemSizeDAO;
import com.floreantpos.swing.FixedLengthTextField;
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
	private FixedLengthTextField tfSortOrder;

	public MenuItemSizeForm() {
		this(new MenuItemSize());
	}

	public MenuItemSizeForm(MenuItemSize menuItemSize) {
		initComponents();

		setBean(menuItemSize);

	}

	private void initComponents() {
		JPanel contentPanel = new JPanel(new MigLayout("fill"));

		JLabel lblName = new JLabel(com.floreantpos.POSConstants.NAME + ":");
		JLabel lblDescription = new JLabel("Description");
		JLabel lblTranslatedName = new JLabel("Translated Name");
		JLabel lblSortOrder = new JLabel("Sort Order");

		tfName = new FixedLengthTextField();
		tfDescription = new FixedLengthTextField();
		tfTranslatedName = new FixedLengthTextField();
		tfSortOrder = new FixedLengthTextField();

		contentPanel.add(lblName, "cell 0 0");
		contentPanel.add(tfName, "cell 1 0");
		contentPanel.add(lblTranslatedName, "cell 0 1");
		contentPanel.add(tfTranslatedName, "cell 1 1");
		contentPanel.add(lblDescription, "cell 0 2");
		contentPanel.add(tfDescription, "cell 1 2");
		contentPanel.add(lblSortOrder, "cell 0 3");
		contentPanel.add(tfSortOrder, "cell 1 3");

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

	}

	@Override
	protected boolean updateModel() {
		MenuItemSize menuItemSize = (MenuItemSize) getBean();

		String name = tfName.getText();
		String description = tfDescription.getText();
		String translatedName = tfTranslatedName.getText();
		String sortOrder = tfSortOrder.getText();
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
		menuItemSize.setSortOrder(Integer.valueOf(sortOrder));

		MenuItemSizeDAO dao = new MenuItemSizeDAO();
		dao.saveOrUpdate(menuItemSize);

		return true;
	}

	public String getDisplayText() {
		MenuItemSize menuItemSize = (MenuItemSize) getBean();
		if (menuItemSize.getId() == null) {
			return "New Menu Item Size";
		}
		return "Edit Menu Item Size";
	}
}
