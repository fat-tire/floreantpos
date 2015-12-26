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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.UIManager;

import com.floreantpos.PosException;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.order.SelectionView;
import com.floreantpos.util.ModifierStateChangeListener;

/**
 * 
 * @author MShahriar
 */
public class ModifierView extends SelectionView implements ModifierStateChangeListener {
	private Vector<ModifierSelectionListener> listenerList = new Vector<ModifierSelectionListener>();

	private MenuModifierGroup modifierGroup;

	private ModifierButton currentSelectedButton;

	private boolean addOnMode;

	/** Creates new form GroupView */
	public ModifierView() {
		super(com.floreantpos.POSConstants.MODIFIERS);
	}

	public void setModifierGroup(MenuModifierGroup modifierGroup) {
		this.modifierGroup = modifierGroup;
		if (modifierGroup == null) {
			return;
		}

		try {

			List itemList = new ArrayList();

			Set<MenuModifier> modifiers = modifierGroup.getModifiers();
			for (MenuModifier modifier : modifiers) {
				modifier.setMenuItemModifierGroup(modifierGroup.getMenuItemModifierGroup());
				if (isAddOnMode()) {
					if (modifier.getPrice() > 0 || modifier.getExtraPrice() > 0) {
						itemList.add(modifier);
					}
				}
				else {
					if (modifier.getPrice() == 0) {
						itemList.add(modifier);
					}
				}

			}

			setItems(itemList);

		} catch (PosException e) {
			POSMessageDialog.showError(this, com.floreantpos.POSConstants.ERROR_MESSAGE, e);
		}
	}

	@Override
	protected AbstractButton createItemButton(Object item) {
		MenuModifier modifier = (MenuModifier) item;
		ModifierButton modifierButton = new ModifierButton(modifier);
		String key = modifier.getId() + "_" + modifier.getModifierGroup().getId(); //$NON-NLS-1$
		// buttonMap.put(key, modifierButton);

		return modifierButton;
	}

	public void addModifierSelectionListener(ModifierSelectionListener listener) {
		listenerList.add(listener);
	}

	public void removeModifierSelectionListener(ModifierSelectionListener listener) {
		listenerList.remove(listener);
	}

	private void fireModifierSelectionFinished() {
		for (ModifierSelectionListener listener : listenerList) {
			//listener.modifierSelectionFiniched(parentMenuItem);
		}
	}

	public void updateVisualRepresentation() {
		// List<TicketItemModifierGroup> ticketItemModifierGroups =
		// parentTicketItem.getTicketItemModifierGroups();
		// if (ticketItemModifierGroups != null) {
		// for (TicketItemModifierGroup ticketItemModifierGroup :
		// ticketItemModifierGroups) {
		// List<TicketItemModifier> ticketItemModifiers =
		// ticketItemModifierGroup.getTicketItemModifiers();
		// if (ticketItemModifiers != null) {
		// int total = 0;
		// int max = ticketItemModifierGroup.getMaxQuantity();
		// for (TicketItemModifier ticketItemModifier : ticketItemModifiers) {
		//						String key = ticketItemModifier.getItemId() + "_" + ticketItemModifier.getGroupId(); //$NON-NLS-1$
		// ModifierButton button = buttonMap.get(key);
		// if (ticketItemModifier.getModifierType() !=
		// TicketItemModifier.NO_MODIFIER) {
		// total += ticketItemModifier.getItemCount();
		// if (total > max) {
		// ticketItemModifier.setModifierType(TicketItemModifier.EXTRA_MODIFIER);
		// }
		// else {
		// ticketItemModifier.setModifierType(TicketItemModifier.NORMAL_MODIFIER);
		// }
		// }
		// button.updateView(ticketItemModifier);
		// }
		// }
		// }
		// }
	}

	@Override
	public void doGoBack() {

	}

	public boolean isRequiredModifiersAdded(List<MenuItemModifierGroup> menuItemModifierGroups, List<TicketItemModifierGroup> ticketItemModifierGroups) {
		boolean requiredModifierAdded = true;
		if (menuItemModifierGroups != null) {
			outer: for (MenuItemModifierGroup menuItemModifierGroup : menuItemModifierGroups) {
				int minQuantity = menuItemModifierGroup.getMinQuantity();
				if (minQuantity == 0)
					continue;

				if (ticketItemModifierGroups == null) {
					requiredModifierAdded = false;
					break outer;
				}

				for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
					if (ticketItemModifierGroup.countItems(false) < minQuantity) {
						requiredModifierAdded = false;
						break outer;
					}
				}
			}
		}
		return requiredModifierAdded;
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

		void updateView(TicketItemModifier ticketItemModifier) {
			Integer itemCount = ticketItemModifier.getItemCount();

			String text = menuModifier.getDisplayName();
			String style = ""; //$NON-NLS-1$

			if (ticketItemModifier == null || ticketItemModifier.getModifierType() == TicketItemModifier.MODIFIER_NOT_INITIALIZED) {
			}
			else if (ticketItemModifier.getModifierType() == TicketItemModifier.NORMAL_MODIFIER) {
				style = "color: green;"; //$NON-NLS-1$
			}
			// else if (ticketItemModifier.getModifierType() ==
			// TicketItemModifier.NO_MODIFIER) {
			// //setIcon(noIcon);
			// setBackground(Color.RED.darker());
			// }
			else if (ticketItemModifier.getModifierType() == TicketItemModifier.EXTRA_MODIFIER) {
				style = "color: red;"; //$NON-NLS-1$
			}

			StringBuilder sb = new StringBuilder();
			sb.append("<html>"); //$NON-NLS-1$
			sb.append("<center>"); //$NON-NLS-1$
			sb.append(text);

			if (itemCount != 0) {
				sb.append("<h2 style='" + style + "'>"); //$NON-NLS-1$ //$NON-NLS-2$
				sb.append("(" + itemCount + ")"); //$NON-NLS-1$ //$NON-NLS-2$
				sb.append("</h2>"); //$NON-NLS-1$
			}
			sb.append("</center>"); //$NON-NLS-1$
			sb.append("</html>"); //$NON-NLS-1$

			setText(sb.toString());
		}

		public void actionPerformed(ActionEvent e) {
			for (ModifierSelectionListener listener : ModifierView.this.listenerList) {
				listener.modifierSelected(null, menuModifier);
			}
		}
	}

	@Override
	public void modifierStateChanged() {
		updateVisualRepresentation();
	}

	@Override
	public void updateView(TicketItemModifier modifier) {
		//		String key = modifier.getItemId() + "_" + modifier.getGroupId(); //$NON-NLS-1$
		// ModifierButton modifierButton = buttonMap.get(key);
		// if (modifierButton == null) {
		// return;
		// }
		//
		// modifierButton.updateView(modifier);
	}

	@Override
	public void select(TicketItemModifier modifier) {
		//		String key = modifier.getItemId() + "_" + modifier.getGroupId(); //$NON-NLS-1$
		// ModifierButton modifierButton = buttonMap.get(key);
		// if (modifierButton == null) {
		// return;
		// }
		//
		// if(currentSelectedButton != null) {
		//			currentSelectedButton.setBorder(UIManager.getBorder("Button.border")); //$NON-NLS-1$
		// }
		// currentSelectedButton = modifierButton;
		// //modifierButton.setBorder(BorderFactory.createLineBorder(Color.blue.brighter(),
		// 2));
		// modifierButton.requestFocus(true);
	}

	@Override
	public void clearSelection() {
		if (currentSelectedButton != null) {
			currentSelectedButton.setBorder(UIManager.getBorder("Button.border")); //$NON-NLS-1$
		}
		currentSelectedButton = null;
	}

	public boolean isAddOnMode() {
		return addOnMode;
	}

	public void setAddOnMode(boolean addOnMode) {
		this.addOnMode = addOnMode;
		setModifierGroup(modifierGroup);
	}
}
