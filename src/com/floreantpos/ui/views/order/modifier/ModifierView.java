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
 * GroupView.java
 *
 * Created on August 5, 2006, 9:29 PM
 */

package com.floreantpos.ui.views.order.modifier;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.order.SelectionView;

/**
 * 
 * @author MShahriar
 */
public class ModifierView extends SelectionView {
	private Vector<ModifierSelectionListener> listenerList = new Vector<ModifierSelectionListener>();

	private ModifierSelectionModel modifierSelectionModel;
	private MenuModifierGroup modifierGroup;

	private PosButton btnClear = new PosButton(POSConstants.CLEAR);
	private PosButton btnDone = new PosButton(POSConstants.GROUP.toUpperCase() + " " + "DONE");

	private HashMap<String, ModifierButton> buttonMap = new HashMap<String, ModifierButton>();

	/** Creates new form GroupView */
	public ModifierView(ModifierSelectionModel modifierSelectionModel) {
		super(com.floreantpos.POSConstants.MODIFIERS);

		this.modifierSelectionModel = modifierSelectionModel;

		addActionButtons();
	}

	private void addActionButtons() {
		actionButtonPanel.add(btnClear);
		actionButtonPanel.add(btnDone);

		btnDone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (ModifierSelectionListener listener : ModifierView.this.listenerList) {
					listener.modifierGroupSelectionDone(modifierGroup);
				}
			}
		});
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (ModifierSelectionListener listener : ModifierView.this.listenerList) {
					listener.clearModifiers(modifierGroup);
				}
			}
		});
	}

	public void setModifierGroup(MenuModifierGroup modifierGroup) {
		this.modifierGroup = modifierGroup;
		buttonMap.clear();

		if (modifierGroup == null) {
			return;
		}

		renderTitle();

		try {

			List itemList = new ArrayList();

			Set<MenuModifier> modifiers = modifierGroup.getModifiers();
			for (MenuModifier modifier : modifiers) {
				modifier.setMenuItemModifierGroup(modifierGroup.getMenuItemModifierGroup());
//				if (isAddOnMode()) {
//					if (modifier.getExtraPrice() > 0) {
//						itemList.add(modifier);
//					}
//				}
//				else {
//					if (modifier.getPrice() == 0) {
//						itemList.add(modifier);
//					}
//				}
				itemList.add(modifier);
			}

			setItems(itemList);
		} catch (PosException e) {
			POSMessageDialog.showError(this, com.floreantpos.POSConstants.ERROR_MESSAGE, e);
		}
	}

	@Override
	protected void renderItems() {
		super.renderItems();
		updateView();
	}

	private void renderTitle() {
			String displayName = modifierGroup.getDisplayName();
			int minQuantity = modifierGroup.getMenuItemModifierGroup().getMinQuantity();
			int maxQuantity = modifierGroup.getMenuItemModifierGroup().getMaxQuantity();
			setTitle(displayName + ", Min: " + minQuantity + ", Max: " + maxQuantity);
	}

	@Override
	protected AbstractButton createItemButton(Object item) {
		MenuModifier modifier = (MenuModifier) item;
		ModifierButton modifierButton = new ModifierButton(modifier);
		String key = modifier.getId() + "_" + modifier.getModifierGroup().getId(); //$NON-NLS-1$
		buttonMap.put(key, modifierButton);

		return modifierButton;
	}

	public void addModifierSelectionListener(ModifierSelectionListener listener) {
		listenerList.add(listener);
	}

	public void removeModifierSelectionListener(ModifierSelectionListener listener) {
		listenerList.remove(listener);
	}

	public void updateView() {
		JPanel activePanel = getActivePanel();
		if (activePanel == null) {
			return;
		}
		Component[] components = activePanel.getComponents();
		if (components == null || components.length == 0)
			return;

		TicketItem ticketItem = modifierSelectionModel.getTicketItem();

		for (Component component : components) {
			ModifierButton modifierButton = (ModifierButton) component;
			MenuModifier modifier = modifierButton.menuModifier;

			TicketItemModifierGroup ticketItemModifierGroup = ticketItem.findTicketItemModifierGroup(modifier, false);
			TicketItemModifier ticketItemModifier = ticketItemModifierGroup.findTicketItemModifier(modifier, false);
			if (ticketItemModifier != null) {
				modifierButton.setText("<html><center>" + modifier.getDisplayName() + "<br/><span style='color:green;"
						+ "'>(" + ticketItemModifier.getItemCount() + ")</span></center></html>"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			else {
				modifierButton.setText("<html><center>" + modifier.getDisplayName() + "</center></html>"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		if (ModifierSelectionDialog.isRequiredModifiersAdded(ticketItem, modifierGroup.getMenuItemModifierGroup())) {
			btnDone.setBackground(Color.green);
		}
		else {
			btnDone.setBackground(UIManager.getColor("Control"));
		}
	}

	private class ModifierButton extends PosButton implements ActionListener {
		private MenuModifier menuModifier;

		public ModifierButton(MenuModifier modifier) {
			this.menuModifier = modifier;

			setText("<html><center>" + modifier.getDisplayName() + "</center></html>"); //$NON-NLS-1$ //$NON-NLS-2$

			if (modifier.getButtonColor() != null) {
				setBackground(new Color(modifier.getButtonColor()));
			}

			if (modifier.getTextColor() != null) {
				setForeground(new Color(modifier.getTextColor()));
			}

			setFocusable(true);
			setFocusPainted(true);
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			for (ModifierSelectionListener listener : ModifierView.this.listenerList) {
				listener.modifierSelected(menuModifier);
			}
		}
	}
}
