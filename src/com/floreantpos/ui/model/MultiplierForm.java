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
 * MultiplierEditor.java
 *
 * Created on August 3, 2006, 1:49 AM
 */

package com.floreantpos.ui.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.model.Multiplier;
import com.floreantpos.model.dao.MultiplierDAO;
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
public class MultiplierForm extends BeanEditor {
	private FixedLengthTextField tfName;
	private FixedLengthTextField tfTicketPrefix;
	private DoubleTextField tfRate;
	private IntegerTextField tfSortOrder;
	private JButton btnButtonColor;
	private JButton btnTextColor;

	public MultiplierForm() {
		this(new Multiplier());
	}

	public MultiplierForm(Multiplier multiplier) {
		initComponents();
		setBean(multiplier);
	}

	private void initComponents() {
		tfName = new com.floreantpos.swing.FixedLengthTextField();
		tfName.setLength(20);
		tfRate = new DoubleTextField();

		tfTicketPrefix = new com.floreantpos.swing.FixedLengthTextField();
		tfTicketPrefix.setLength(20);

		tfRate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		tfSortOrder = new IntegerTextField();

		JLabel lblButtonColor = new JLabel(Messages.getString("MenuModifierForm.1")); //$NON-NLS-1$
		JLabel lblTextColor = new JLabel(Messages.getString("MenuModifierForm.27")); //$NON-NLS-1$

		btnButtonColor = new JButton(""); //$NON-NLS-1$
		btnButtonColor.setPreferredSize(new Dimension(140, 40));

		btnTextColor = new JButton(Messages.getString("MenuModifierForm.29")); //$NON-NLS-1$
		btnTextColor.setPreferredSize(new Dimension(140, 40));

		btnButtonColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(POSUtil.getBackOfficeWindow(), Messages.getString("MenuModifierForm.39"), btnButtonColor.getBackground()); //$NON-NLS-1$
				btnButtonColor.setBackground(color);
				btnTextColor.setBackground(color);
			}
		});

		btnTextColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(POSUtil.getBackOfficeWindow(), Messages.getString("MenuModifierForm.40"), btnTextColor.getForeground()); //$NON-NLS-1$
				btnTextColor.setForeground(color);
			}
		});

		JPanel contentPanel = new JPanel(new MigLayout("fillx"));

		contentPanel.add(new javax.swing.JLabel("Name"));
		contentPanel.add(tfName, "grow,wrap");

		contentPanel.add(new javax.swing.JLabel("Ticket prefix"));
		contentPanel.add(tfTicketPrefix, "grow,wrap");

		contentPanel.add(new javax.swing.JLabel("Percentage (%)"));
		contentPanel.add(tfRate, "grow,wrap");

		contentPanel.add(new javax.swing.JLabel("Sort Order"));
		contentPanel.add(tfSortOrder, "grow,wrap");

		contentPanel.add(lblButtonColor);
		contentPanel.add(btnButtonColor, "wrap");

		contentPanel.add(lblTextColor);
		contentPanel.add(btnTextColor, "wrap");

		add(contentPanel);
	}

	@Override
	public boolean save() {

		try {
			if (!updateModel())
				return false;

			Multiplier multiplier = (Multiplier) getBean();
			MultiplierDAO dao = new MultiplierDAO();
			if (dao.get(multiplier.getName()) == null) {
				dao.save(multiplier);
			}
			else
				dao.update(multiplier);
		} catch (Exception e) {
			MessageDialog.showError(e);
			return false;
		}

		return true;
	}

	@Override
	protected void updateView() {
		Multiplier multiplier = (Multiplier) getBean();
		tfName.setText(multiplier.getName());
		tfTicketPrefix.setText(multiplier.getTicketPrefix());
		tfRate.setText("" + multiplier.getRate()); //$NON-NLS-1$
		tfSortOrder.setText(String.valueOf(multiplier.getSortOrder()));

		if (multiplier.getButtonColor() != null) {
			Color color = new Color(multiplier.getButtonColor());
			btnButtonColor.setBackground(color);
			btnTextColor.setBackground(color);
		}

		if (multiplier.getTextColor() != null) {
			Color color = new Color(multiplier.getTextColor());
			btnTextColor.setForeground(color);
		}
	}

	@Override
	protected boolean updateModel() {
		String name = tfName.getText();
		Multiplier multiplier = (Multiplier) getBean();

		if (POSUtil.isBlankOrNull(name)) {
			MessageDialog.showError(com.floreantpos.POSConstants.NAME_REQUIRED);
			return false;
		}

		multiplier.setName(name);
		multiplier.setRate(tfRate.getDouble());
		multiplier.setTicketPrefix(tfTicketPrefix.getText());
		multiplier.setSortOrder(tfSortOrder.getInteger());
		multiplier.setButtonColor(btnButtonColor.getBackground().getRGB());
		multiplier.setTextColor(btnTextColor.getForeground().getRGB());

		return true;
	}

	public String getDisplayText() {
		Multiplier multiplier = (Multiplier) getBean();
		if (multiplier.getName() == null) {
			return "New multiplier";
		}
		return "Edit multiplier";
	}
}
