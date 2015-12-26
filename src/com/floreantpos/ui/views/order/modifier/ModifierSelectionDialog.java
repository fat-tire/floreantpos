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
 * OrderView.java
 *
 * Created on August 4, 2006, 6:58 PM
 */

package com.floreantpos.ui.views.order.modifier;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JSeparator;

import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

/**
 *
 * @author  MShahriar
 */
public class ModifierSelectionDialog extends POSDialog implements ModifierGroupSelectionListener, ModifierSelectionListener {
	private ModifierSelectionModel modifierSelectionModel;

	private ModifierGroupView modifierGroupView;
	private ModifierView modifierView;
	private TicketItemModifierTableView ticketItemModifierView;

	private com.floreantpos.swing.TransparentPanel buttonPanel;

	public com.floreantpos.swing.POSToggleButton btnAddsOn;
	private com.floreantpos.swing.PosButton btnSave;
	private com.floreantpos.swing.PosButton btnCancel;
	
	public ModifierSelectionDialog(ModifierSelectionModel modifierSelectionModel) {
		this.modifierSelectionModel = modifierSelectionModel;

		initComponents();
	}

	private void initComponents() {
		setTitle("MODIFIERS and ADD-ONs");
		setLayout(new java.awt.BorderLayout(10, 10));

		Dimension screenSize = Application.getPosWindow().getSize();

		modifierGroupView = new com.floreantpos.ui.views.order.modifier.ModifierGroupView(modifierSelectionModel);
		modifierView = new ModifierView(modifierSelectionModel);
		ticketItemModifierView = new TicketItemModifierTableView(modifierSelectionModel);
		buttonPanel = new com.floreantpos.swing.TransparentPanel();
		buttonPanel.setLayout(new BorderLayout());

		add(ticketItemModifierView, java.awt.BorderLayout.WEST);
		add(modifierGroupView, java.awt.BorderLayout.EAST);
		add(modifierView);


		createButtonPanel();

		setSize(screenSize);

		ticketItemModifierView.addModifierSelectionListener(this);
		modifierGroupView.addModifierGroupSelectionListener(this);
		modifierView.addModifierSelectionListener(this);
		
		modifierGroupView.selectFirst();
	}

	public void createButtonPanel() {

		TransparentPanel panel2 = new TransparentPanel(new FlowLayout(FlowLayout.CENTER));

		btnAddsOn = new POSToggleButton("ADD-ONs");
		btnAddsOn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				modifierView.setAddOnMode(btnAddsOn.isSelected());
			}
		});
		btnAddsOn.setPreferredSize(new Dimension(100, TerminalConfig.getTouchScreenButtonHeight()));

		btnSave = new PosButton("DONE");
		btnSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doFinishModifierSelection();
			}
		});
		btnSave.setPreferredSize(new Dimension(100, TerminalConfig.getTouchScreenButtonHeight()));

		btnCancel = new PosButton(POSConstants.CANCEL.toUpperCase());
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setCanceled(true);
				dispose();
			}
		});
		btnCancel.setPreferredSize(new Dimension(100, TerminalConfig.getTouchScreenButtonHeight()));
		
		panel2.add(btnAddsOn);
		panel2.add(btnSave);
		panel2.add(btnCancel);

		buttonPanel.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.NORTH);

		buttonPanel.add(panel2, BorderLayout.CENTER);

		add(buttonPanel, java.awt.BorderLayout.SOUTH);
	}

	public com.floreantpos.ui.views.order.modifier.ModifierGroupView getModifierGroupView() {
		return modifierGroupView;
	}

	public void setModifierGroupView(com.floreantpos.ui.views.order.modifier.ModifierGroupView modifierGroupView) {
		this.modifierGroupView = modifierGroupView;
	}

	public ModifierView getModifierView() {
		return modifierView;
	}

	public void setModifierView(ModifierView modifierView) {
		this.modifierView = modifierView;
	}

	private void doFinishModifierSelection() {
		List<MenuItemModifierGroup> menuItemModiferGroups = modifierSelectionModel.getMenuItem().getMenuItemModiferGroups();
		if(menuItemModiferGroups == null) {
			dispose();
			return;
		}
		
		for (MenuItemModifierGroup menuItemModifierGroup : menuItemModiferGroups) {
			Integer minQuantity = menuItemModifierGroup.getMinQuantity();
			if(minQuantity > 0) {
				TicketItemModifierGroup ticketItemModifierGroup = modifierSelectionModel.getTicketItem().findTicketItemModifierGroup(menuItemModifierGroup.getId());
				if(ticketItemModifierGroup == null || ticketItemModifierGroup.countFreeModifiers() < minQuantity) {
					POSMessageDialog.showError("You must select at least " + minQuantity + " modifiers from group " + menuItemModifierGroup.getModifierGroup().getDisplayName());
					modifierGroupView.setSelectedModifierGroup(menuItemModifierGroup.getModifierGroup());
					return;
				}
			}
		}
		
		setCanceled(false);
		dispose();
	}

	@Override
	public void modifierGroupSelected(MenuModifierGroup menuModifierGroup) {
		modifierView.setModifierGroup(menuModifierGroup);
	}

	@Override
	public void modifierSelected(MenuModifier modifier) {
		TicketItemModifierGroup ticketItemModifierGroup = modifierSelectionModel.getTicketItem().findTicketItemModifierGroup(modifier, false);
		
		boolean addOn = btnAddsOn.isSelected();
		if(!addOn) {
			int freeModifiers = ticketItemModifierGroup.countFreeModifiers();
			int minQuantity = ticketItemModifierGroup.getMinQuantity();
			int maxQuantity = ticketItemModifierGroup.getMaxQuantity();
			
			if(maxQuantity < minQuantity) {
				maxQuantity = minQuantity;
			}
			
			if(freeModifiers >= maxQuantity) {
				POSMessageDialog.showError("You have added maximum number of allowed modifiers from group " + modifier.getModifierGroup().getDisplayName());
				return;
			}
		}
		
		TicketItemModifier ticketItemModifier = ticketItemModifierGroup.findTicketItemModifier(modifier, addOn);
		if(ticketItemModifier == null) {
			ticketItemModifierGroup.addTicketItemModifier(modifier, addOn);
		}
		else {
			ticketItemModifier.setItemCount(ticketItemModifier.getItemCount() + 1);
		}
		
		modifierSelectionModel.getTicketItem().calculatePrice();
		ticketItemModifierView.updateView();
	}

	public ModifierSelectionModel getModifierSelectionModel() {
		return modifierSelectionModel;
	}

	public void setModifierSelectionModel(ModifierSelectionModel modifierSelectionModel) {
		this.modifierSelectionModel = modifierSelectionModel;
	}

}
