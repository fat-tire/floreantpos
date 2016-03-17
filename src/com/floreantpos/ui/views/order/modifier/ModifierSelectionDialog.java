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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.model.OrderType;

/**
 *
 * @author  MShahriar
 */
public class ModifierSelectionDialog extends POSDialog implements ModifierGroupSelectionListener, ModifierSelectionListener {
	private ModifierSelectionModel modifierSelectionModel;

	private ModifierGroupView modifierGroupView;
	private ModifierView modifierView;
	private TicketItemModifierTableView ticketItemModifierView;

	private JPanel westPanel = new JPanel(new BorderLayout(5, 5));

	private com.floreantpos.swing.TransparentPanel buttonPanel;

	private com.floreantpos.swing.PosButton btnSave;
	private com.floreantpos.swing.PosButton btnCancel;

	public ModifierSelectionDialog(ModifierSelectionModel modifierSelectionModel) {
		this.modifierSelectionModel = modifierSelectionModel;

		initComponents();
	}

	private void initComponents() {
		setTitle("MODIFIERS");

		setLayout(new java.awt.BorderLayout(10, 10));

		Dimension screenSize = Application.getPosWindow().getSize();

		modifierGroupView = new com.floreantpos.ui.views.order.modifier.ModifierGroupView(modifierSelectionModel);
		modifierView = new ModifierView(modifierSelectionModel);
		ticketItemModifierView = new TicketItemModifierTableView(modifierSelectionModel);
		buttonPanel = new com.floreantpos.swing.TransparentPanel();
		buttonPanel.setLayout(new MigLayout("fill, ins 4", "fill", ""));

		westPanel.add(ticketItemModifierView);
		add(modifierGroupView, java.awt.BorderLayout.EAST);
		add(modifierView);
		add(westPanel, BorderLayout.WEST);

		createButtonPanel();

		setSize(screenSize);

		ticketItemModifierView.addModifierSelectionListener(this);
		modifierGroupView.addModifierGroupSelectionListener(this);
		modifierView.addModifierSelectionListener(this);

		modifierGroupView.selectFirst();
	}

	public void createButtonPanel() {
		Dimension preferredButtonSize = new Dimension(100, TerminalConfig.getTouchScreenButtonHeight());

		btnSave = new PosButton("DONE");
		btnSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doFinishModifierSelection();
			}
		});
		btnSave.setPreferredSize(preferredButtonSize);

		btnCancel = new PosButton(POSConstants.CANCEL.toUpperCase());
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setCanceled(true);
				dispose();
			}
		});
		btnCancel.setPreferredSize(preferredButtonSize);

		buttonPanel.add(btnCancel);
		buttonPanel.add(btnSave);

		westPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);
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
		if (menuItemModiferGroups == null) {
			dispose();
			return;
		}

		for (MenuItemModifierGroup menuItemModifierGroup : menuItemModiferGroups) {
			if (!isRequiredModifiersAdded(modifierSelectionModel.getTicketItem(), menuItemModifierGroup)) {
				showModifierSelectionMessage(menuItemModifierGroup);
				modifierGroupView.setSelectedModifierGroup(menuItemModifierGroup.getModifierGroup());
				return;
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
		//		if (addOnMode) {
		//			modifierSelectionModel.getTicketItem().addAddOn(modifier);
		//			updateView();
		//			return;
		//		}

		TicketItemModifierGroup ticketItemModifierGroup = modifierSelectionModel.getTicketItem().findTicketItemModifierGroup(modifier, false);

		int freeModifiers = ticketItemModifierGroup.countFreeModifiers();
		int minQuantity = ticketItemModifierGroup.getMinQuantity();
		int maxQuantity = ticketItemModifierGroup.getMaxQuantity();

		if (maxQuantity < minQuantity) {
			maxQuantity = minQuantity;
		}

		if (freeModifiers >= maxQuantity) {
			//			POSMessageDialog.showError("You have added maximum number of allowed modifiers from group " + modifier.getModifierGroup().getDisplayName());
			//			return;

			modifierSelectionModel.getTicketItem().addAddOn(modifier);
			updateView();
			return;
		}

		TicketItemModifier ticketItemModifier = ticketItemModifierGroup.findTicketItemModifier(modifier, false);
		if (ticketItemModifier == null) {
			OrderType type = modifierSelectionModel.getTicketItem().getTicket().getOrderType();
			ticketItemModifierGroup.addTicketItemModifier(modifier, TicketItemModifier.NORMAL_MODIFIER, type);
		}
		else {
			ticketItemModifier.setItemCount(ticketItemModifier.getItemCount() + 1);
		}
		updateView();
	}

	private void updateView() {
		modifierSelectionModel.getTicketItem().calculatePrice();
		modifierView.updateView();
		ticketItemModifierView.updateView();
	}

	@Override
	public void clearModifiers(MenuModifierGroup modifierGroup) {
		List<TicketItemModifierGroup> ticketItemModifierGroups = modifierSelectionModel.getTicketItem().getTicketItemModifierGroups();
		if (ticketItemModifierGroups == null) {
			return;
		}

		for (Iterator iterator = ticketItemModifierGroups.iterator(); iterator.hasNext();) {
			TicketItemModifierGroup ticketItemModifierGroup = (TicketItemModifierGroup) iterator.next();
			for (Iterator iterator2 = ticketItemModifierGroup.getTicketItemModifiers().iterator(); iterator2.hasNext();) {
				TicketItemModifier ticketItemModifier = (TicketItemModifier) iterator2.next();
				if (!ticketItemModifier.isPrintedToKitchen()) {
					iterator2.remove();
				}
			}
			if (ticketItemModifierGroup.getTicketItemModifiers().size() < 0) {
				iterator.remove();
			}
		}

		List<TicketItemModifier> addOnsList = modifierSelectionModel.getTicketItem().getAddOns();
		for (Iterator iterator3 = addOnsList.iterator(); iterator3.hasNext();) {
			TicketItemModifier addOns = (TicketItemModifier) iterator3.next();
			if (!addOns.isPrintedToKitchen()) {
				iterator3.remove();
			}
		}

		updateView();
	}

	@Override
	public void modifierGroupSelectionDone(MenuModifierGroup modifierGroup) {
		MenuItemModifierGroup menuItemModifierGroup = modifierGroup.getMenuItemModifierGroup();
		if (!isRequiredModifiersAdded(modifierSelectionModel.getTicketItem(), menuItemModifierGroup)) {
			showModifierSelectionMessage(menuItemModifierGroup);
			modifierGroupView.setSelectedModifierGroup(menuItemModifierGroup.getModifierGroup());
			return;
		}

		if (modifierGroupView.hasNextMandatoryGroup()) {
			modifierGroupView.selectNextGroup();
		}
		else {
			setCanceled(false);
			dispose();
		}
	}

	public ModifierSelectionModel getModifierSelectionModel() {
		return modifierSelectionModel;
	}

	public void setModifierSelectionModel(ModifierSelectionModel modifierSelectionModel) {
		this.modifierSelectionModel = modifierSelectionModel;
	}

	public static boolean isRequiredModifiersAdded(TicketItem ticketItem, MenuItemModifierGroup menuItemModifierGroup) {
		int minQuantity = menuItemModifierGroup.getMinQuantity();
		if (minQuantity <= 0) {
			return true;
		}

		Set<MenuModifier> modifiers = menuItemModifierGroup.getModifierGroup().getModifiers();
		if (modifiers == null || modifiers.size() == 0) {
			return true;
		}

		TicketItemModifierGroup ticketItemModifierGroup = ticketItem.findTicketItemModifierGroup(menuItemModifierGroup.getId());

		if (ticketItemModifierGroup == null || ticketItemModifierGroup.countFreeModifiers() < minQuantity) {
			return false;
		}

		return true;
	}

	private void showModifierSelectionMessage(MenuItemModifierGroup menuItemModifierGroup) {
		String displayName = menuItemModifierGroup.getModifierGroup().getDisplayName();
		int minQuantity = menuItemModifierGroup.getMinQuantity();
		POSMessageDialog.showError("You must select at least " + minQuantity + " modifiers from group " + displayName);
	}
}
