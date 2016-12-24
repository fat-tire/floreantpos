/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
/*
 * PizzaCrustEditor.java
 *
 * Created on August 3, 2006, 1:49 AM
 */

package com.floreantpos.ui.model;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.model.PizzaCrust;
import com.floreantpos.model.dao.PizzaCrustDAO;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.util.POSUtil;

/**
 *
 * @author  MShahriar
 */
public class PizzaCrustForm extends BeanEditor {

	private FixedLengthTextField tfName;
	private FixedLengthTextField tfDescription;
	private FixedLengthTextField tfTranslatedName;
	private FixedLengthTextField tfSortOrder;


	public PizzaCrustForm() {
		this(new PizzaCrust());
	}

	public PizzaCrustForm(PizzaCrust pizzaCrust) {
		initComponents();

		setBean(pizzaCrust);
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

			PizzaCrust pizzaCrust = (PizzaCrust) getBean();
			PizzaCrustDAO dao = new PizzaCrustDAO();
			dao.saveOrUpdate(pizzaCrust);
		} catch (Exception e) {
			MessageDialog.showError(e);
			return false;
		}

		return true;
	}

	@Override
	protected void updateView() {
		PizzaCrust pizzaCrust = (PizzaCrust) getBean();
		if (pizzaCrust == null) {
			return;
		}
		tfName.setText(pizzaCrust.getName());
		tfDescription.setText(pizzaCrust.getDescription());
		tfTranslatedName.setText(pizzaCrust.getTranslatedName());
		tfSortOrder.setText(String.valueOf(pizzaCrust.getSortOrder()));

	}

	@Override
	protected boolean updateModel() {
		PizzaCrust pizzaCrust = (PizzaCrust) getBean();

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
		}
		*/

		pizzaCrust.setName(name);
		pizzaCrust.setDescription(description);
		pizzaCrust.setTranslatedName(translatedName);
		pizzaCrust.setSortOrder(Integer.valueOf(sortOrder));

		PizzaCrustDAO dao = new PizzaCrustDAO();
		dao.saveOrUpdate(pizzaCrust);

		return true;
	}

	public String getDisplayText() {
		PizzaCrust pizzaCrust = (PizzaCrust) getBean();
		if (pizzaCrust.getId() == null) {
			return "New Pizza Crust";
		}
		return "Edit Pizza Crust";
	}
}
