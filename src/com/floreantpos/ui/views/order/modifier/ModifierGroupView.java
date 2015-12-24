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
 * CategoryView.java
 *
 * Created on August 5, 2006, 2:21 AM
 */

package com.floreantpos.ui.views.order.modifier;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

import org.apache.log4j.Logger;

import com.floreantpos.Messages;
import com.floreantpos.PosException;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.MenuModifierGroupDAO;
import com.floreantpos.model.dao._RootDAO;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.order.ModifierView;
import com.floreantpos.ui.views.order.SelectionView;
import com.floreantpos.ui.views.order.actions.CategorySelectionListener;

/**
 * 
 * @author MShahriar
 */
public class ModifierGroupView extends SelectionView {
	private Vector<ModifierGroupSelectionListener> listenerList = new Vector<ModifierGroupSelectionListener>();

	private boolean addOnMode;

	private MenuItem menuItem;
	private TicketItem ticketItem;

	private ButtonGroup modifierGroupButtonGroup;

	public static final String VIEW_NAME = "MODIFIER_GROUP_VIEW"; //$NON-NLS-1$

	// private int panelCount = 0;

	/** Creates new form CategoryView */
	public ModifierGroupView() {
		super(com.floreantpos.POSConstants.MODIFIER_GROUP, 100, 80);

		setBackVisible(false);

		modifierGroupButtonGroup = new ButtonGroup();
		setPreferredSize(new Dimension(120, 100));
	}

	@Override
	public void reset() {
		super.reset();

		modifierGroupButtonGroup = new ButtonGroup();
	}

	public void setModifierGroups(MenuItem menuItem, TicketItem ticketItem) {
		this.menuItem = menuItem;
		this.ticketItem = ticketItem;

		reset();

		List itemList = new ArrayList();

		List<MenuItemModifierGroup> modifierGroups = menuItem
				.getMenuItemModiferGroups();

		for (Iterator<MenuItemModifierGroup> iter = modifierGroups.iterator(); iter
				.hasNext();) {
			MenuItemModifierGroup menuItemModifierGroup = iter.next();
			MenuModifierGroup group = menuItemModifierGroup.getModifierGroup();

			itemList.add(group);

		}

		setItems(itemList);

	}

	@Override
	protected AbstractButton createItemButton(Object item) {
		MenuModifierGroup menuModifierGroup = (MenuModifierGroup) item;

		ModifierGroupButton button = new ModifierGroupButton(menuModifierGroup);
		modifierGroupButtonGroup.add(button);

		return button;
	}

	public void addModifierGroupSelectionListener(
			ModifierGroupSelectionListener listener) {
		listenerList.add(listener);
	}

	public void removeModifierGroupSelectionListener(
			ModifierGroupSelectionListener listener) {
		listenerList.remove(listener);
	}

	private void fireModifierGroupSelected(MenuModifierGroup foodModifierGroup) {
		for (ModifierGroupSelectionListener listener : listenerList) {
			listener.modifierGroupSelected(foodModifierGroup);
		}
	}

	public void setSelectedModifierGroup(MenuModifierGroup modifierGroup) {
//		ModifierGroupButton button = buttonMap.get(String.valueOf(modifierGroup
//				.getId()));
//		if (button != null) {
//			button.setSelected(true);
//		}
	}

	private class ModifierGroupButton extends POSToggleButton implements
			ActionListener {
		MenuModifierGroup menuModifierGroup;

		ModifierGroupButton(MenuModifierGroup menuModifierGroup) {
			this.menuModifierGroup = menuModifierGroup;
			setText("<html><body><center>" + menuModifierGroup.getDisplayName() + "</center></body></html>"); //$NON-NLS-1$ //$NON-NLS-2$

			addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (isSelected()) {
				fireModifierGroupSelected(menuModifierGroup);
			}
		}
	}

	@Override
	public void doGoBack() {
	}

	public boolean isAddOnMode() {
		return addOnMode;
	}

	public void setAddOnMode(boolean addOnMode) {
		this.addOnMode = addOnMode;
	}
}
