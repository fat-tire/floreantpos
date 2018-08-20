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

import javax.swing.JPanel;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.ModifierGroup;
import com.floreantpos.model.Multiplier;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;
import net.miginfocom.swing.MigLayout;

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
		ticketItemModifierView = new TicketItemModifierTableView(modifierSelectionModel, this);
		buttonPanel = new com.floreantpos.swing.TransparentPanel();
		buttonPanel.setLayout(new MigLayout("fill, ins 1 4 8 4", "fill", ""));

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
		Dimension preferredButtonSize = new Dimension(100, 53);

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
	public void modifierGroupSelected(ModifierGroup menuModifierGroup) {
		modifierView.setModifierGroup(menuModifierGroup);
	}

	@Override
	public void modifierSelected(MenuModifier modifier, Multiplier multiplier) {
		//		if (addOnMode) {
		//			modifierSelectionModel.getTicketItem().addAddOn(modifier);
		//			updateView();
		//			return;
		//		}

		TicketItem ticketItem = modifierSelectionModel.getTicketItem();
		MenuItemModifierGroup menuItemModifierGroup = modifier.getMenuItemModifierGroup();

		int numModifiers = ticketItem.countModifierFromGroup(menuItemModifierGroup);
		int minQuantity = menuItemModifierGroup.getMinQuantity();
		int maxQuantity = menuItemModifierGroup.getMaxQuantity();

		if (maxQuantity < minQuantity) {
			maxQuantity = minQuantity;
		}

		if (numModifiers >= maxQuantity) {
			POSMessageDialog.showError("You have added maximum number of allowed modifiers from group " + modifier.getModifierGroup().getDisplayName());
			//			return;
			//			if (Application.getInstance().getRestaurant().isAllowModifierMaxExceed()) {
			//				ticketItem.addAddOn(modifier);
			//			}
			//			updateView();
			return;
		}

		TicketItemModifier ticketItemModifier = ticketItem.findTicketItemModifierFor(modifier, multiplier);
		if (ticketItemModifier == null) {
			OrderType type = ticketItem.getTicket().getOrderType();
			ticketItem.addTicketItemModifier(modifier, TicketItemModifier.NORMAL_MODIFIER, type, multiplier);
		}
		else {
			ticketItemModifier.setItemCount(ticketItemModifier.getItemCount() + 1);
		}
		updateView();
		if ((numModifiers + 1) == maxQuantity) {
			modifierGroupSelectionDone(modifier.getModifierGroup());
		}
	}

	private void updateView() {
		modifierSelectionModel.getTicketItem().calculatePrice();
		modifierView.updateView();
		ticketItemModifierView.updateView();
	}

	@Override
	public void clearModifiers(ModifierGroup modifierGroup) {
		TicketItem ticketItem = modifierSelectionModel.getTicketItem();
		List<TicketItemModifier> ticketItemModifiers = ticketItem.getTicketItemModifiers();
		if (ticketItemModifiers != null) {
			for (Iterator iterator = ticketItemModifiers.iterator(); iterator.hasNext();) {
				TicketItemModifier ticketItemModifier = (TicketItemModifier) iterator.next();
				if (!ticketItemModifier.isPrintedToKitchen()) {
					iterator.remove();
				}
			}
		}

		List<TicketItemModifier> addOnsList = ticketItem.getAddOns();
		if (addOnsList != null) {
			for (Iterator iterator = addOnsList.iterator(); iterator.hasNext();) {
				TicketItemModifier addOns = (TicketItemModifier) iterator.next();
				if (!addOns.isPrintedToKitchen()) {
					iterator.remove();
				}
			}
		}

		updateView();
	}

	@Override
	public void modifierGroupSelectionDone(ModifierGroup modifierGroup) {
		MenuItemModifierGroup menuItemModifierGroup = modifierGroup.getMenuItemModifierGroup();
		if (!isRequiredModifiersAdded(modifierSelectionModel.getTicketItem(), menuItemModifierGroup)) {
			showModifierSelectionMessage(menuItemModifierGroup);
			modifierGroupView.setSelectedModifierGroup(menuItemModifierGroup.getModifierGroup());
			return;
		}

		if (modifierGroupView.hasNextMandatoryGroup()) {
			modifierGroupView.selectNextGroup();
		}
	}

	public ModifierSelectionModel getModifierSelectionModel() {
		return modifierSelectionModel;
	}

	public void setModifierSelectionModel(ModifierSelectionModel modifierSelectionModel) {
		this.modifierSelectionModel = modifierSelectionModel;
	}

	public static boolean isRequiredModifiersAdded(TicketItem ticketItem, MenuItemModifierGroup menuItemModifierGroup) {
		return ticketItem.requiredModifiersAdded(menuItemModifierGroup);
	}

	private void showModifierSelectionMessage(MenuItemModifierGroup menuItemModifierGroup) {
		String displayName = menuItemModifierGroup.getModifierGroup().getDisplayName();
		int minQuantity = menuItemModifierGroup.getMinQuantity();
		POSMessageDialog.showError("You must select at least " + minQuantity + " modifiers from group " + displayName);
	}

	@Override
	public void modifierRemoved(TicketItemModifier modifier) {
		updateView();
	}

	public void finishModifierSelection() {
		TicketItem ticketItem = modifierSelectionModel.getTicketItem();
		List<MenuItemModifierGroup> menuItemModiferGroups = modifierSelectionModel.getMenuItem().getMenuItemModiferGroups();
		if (menuItemModiferGroups == null) {
			setCanceled(false);
			dispose();
			return;
		}
		if (!menuItemModiferGroups.isEmpty()) {

			for (Iterator iterator = menuItemModiferGroups.iterator(); iterator.hasNext();) {
				MenuItemModifierGroup ticketItemModifierGroup = (MenuItemModifierGroup) iterator.next();
				boolean hasModifiers = !ticketItemModifierGroup.getModifierGroup().getModifiers().isEmpty();
				if (!ticketItem.requiredModifiersAdded(ticketItemModifierGroup) && hasModifiers) {
					modifierGroupSelected(ticketItemModifierGroup.getModifierGroup());
					POSMessageDialog.showMessage(POSUtil.getFocusedWindow(), "Please select minimum quantity of each group!");
					return;
				}
				//				if (!ticketItemModifiers.isEmpty()) {
				//					int size = ticketItemModifiers.size();
				//					if (size < minQuantity) {
				//						POSMessageDialog.showMessage(POSUtil.getFocusedWindow(), "Please select minimum quantity of each group!");
				//						return;
				//					}
				//					else if (size > maxQuantity) {
				//						POSMessageDialog.showMessage(POSUtil.getFocusedWindow(), "Please select quantity below the maximum quantity of each group!");
				//						return;
				//					}
				//					else {
				//						setCanceled(false);
				//						dispose();
				//					}
				//				}
			}
		}
		setCanceled(false);
		dispose();
	}

}
