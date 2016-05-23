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
 * DeliveryChargeEditor.java
 *
 * Created on August 3, 2006, 1:49 AM
 */

package com.floreantpos.ui.model;

import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.model.DeliveryCharge;
import com.floreantpos.model.dao.DeliveryChargeDAO;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.util.POSUtil;

/**
 *
 * @author  MShahriar
 */
public class DeliveryChargeForm extends BeanEditor {
	private DoubleTextField tfStartRange;
	private DoubleTextField tfEndRange;
	private DoubleTextField tfChargeAmount;

	public DeliveryChargeForm() {
		this(new DeliveryCharge());
	}

	public DeliveryChargeForm(DeliveryCharge deliveryCharge) {
		initComponents();

		setBean(deliveryCharge);
	}

	private void initComponents() {
		setLayout(new MigLayout("fill"));

		JLabel lblStartRange = new JLabel();
		JLabel lblEndRange = new JLabel();
		JLabel lblChargeAmount = new JLabel();

		tfStartRange = new DoubleTextField();
		tfEndRange = new DoubleTextField();
		tfChargeAmount = new DoubleTextField();

		lblStartRange.setText("START RANGE" + ":"); //$NON-NLS-1$
		lblEndRange.setText("END RANGE" + ":"); //$NON-NLS-1$

		tfStartRange.setHorizontalAlignment(JTextField.RIGHT);
		tfEndRange.setHorizontalAlignment(JTextField.RIGHT);
		tfChargeAmount.setHorizontalAlignment(JTextField.RIGHT);

		lblChargeAmount.setText("CHARGE AMOUNT :"); //$NON-NLS-1$

		add(lblStartRange, "grow"); //$NON-NLS-1$ 
		add(tfStartRange, "grow, wrap"); //$NON-NLS-1$ //$NON-NLS-2$

		add(lblEndRange, "grow");//$NON-NLS-1$ 
		add(tfEndRange, "grow, wrap"); //$NON-NLS-1$ //$NON-NLS-2$

		add(lblChargeAmount, "grow");//$NON-NLS-1$ 
		add(tfChargeAmount, "grow, wrap"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public boolean save() {

		try {
			if (!updateModel())
				return false;

			DeliveryCharge deliveryCharge = (DeliveryCharge) getBean();
			DeliveryChargeDAO dao = new DeliveryChargeDAO();
			dao.saveOrUpdate(deliveryCharge);
		} catch (Exception e) {
			MessageDialog.showError(e);
			return false;
		}

		return true;
	}

	@Override
	protected void updateView() {
		DeliveryCharge deliveryCharge = (DeliveryCharge) getBean();
		tfStartRange.setText(String.valueOf(deliveryCharge.getStartRange()));
		tfEndRange.setText(String.valueOf(deliveryCharge.getEndRange()));
		tfChargeAmount.setText(String.valueOf(deliveryCharge.getChargeAmount()));
	}

	@Override
	protected boolean updateModel() {
		DeliveryCharge deliveryCharge = (DeliveryCharge) getBean();

		String startRange = tfStartRange.getText();
		String endRange = tfEndRange.getText();
		String chargeAmount = tfChargeAmount.getText();

		if (POSUtil.isBlankOrNull(startRange)) {
			MessageDialog.showError("Start Range is required");
			return false;
		}
		if (POSUtil.isBlankOrNull(endRange)) {
			MessageDialog.showError("End Range is required");
			return false;
		}
		if (POSUtil.isBlankOrNull(chargeAmount)) {
			MessageDialog.showError("Charge Amount is required");
			return false;
		}

		deliveryCharge.setStartRange(tfStartRange.getDouble());
		deliveryCharge.setEndRange(tfEndRange.getDouble());
		deliveryCharge.setChargeAmount(tfChargeAmount.getDouble());

		return true;
	}

	public String getDisplayText() {
		DeliveryCharge deliveryCharge = (DeliveryCharge) getBean();
		if (deliveryCharge.getId() == null) {
			return "New Delivery Charge";
		}
		return "Edit Delivery Charge";
	}
}
