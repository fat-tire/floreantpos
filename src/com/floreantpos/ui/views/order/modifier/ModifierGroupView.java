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
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.ui.views.order.SelectionView;

/**
 * 
 * @author MShahriar
 */
public class ModifierGroupView extends SelectionView {
	private Vector<ModifierGroupSelectionListener> listenerList = new Vector<ModifierGroupSelectionListener>();

	private ModifierSelectionModel modifierSelectionModel;

	private ButtonGroup modifierGroupButtonGroup;

	public static final String VIEW_NAME = "MODIFIER_GROUP_VIEW"; //$NON-NLS-1$

	/** Creates new form CategoryView */
	public ModifierGroupView(ModifierSelectionModel modifierSelectionModel) {
		super(com.floreantpos.POSConstants.MODIFIER_GROUP, 100, 80);
		this.modifierSelectionModel = modifierSelectionModel;

		setBackVisible(false);

		modifierGroupButtonGroup = new ButtonGroup();
		setPreferredSize(new Dimension(120, 100));
		
		init();
	}

	@Override
	public void reset() {
		super.reset();

		modifierGroupButtonGroup = new ButtonGroup();
	}

	private void init() {
		List itemList = new ArrayList();

		List<MenuItemModifierGroup> modifierGroups = modifierSelectionModel.getMenuItem().getMenuItemModiferGroups();

		for (Iterator<MenuItemModifierGroup> iter = modifierGroups.iterator(); iter.hasNext();) {
			MenuItemModifierGroup menuItemModifierGroup = iter.next();
			MenuModifierGroup group = menuItemModifierGroup.getModifierGroup();
			group.setMenuItemModifierGroup(menuItemModifierGroup);

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

	public void addModifierGroupSelectionListener(ModifierGroupSelectionListener listener) {
		listenerList.add(listener);
	}

	public void removeModifierGroupSelectionListener(ModifierGroupSelectionListener listener) {
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

	private class ModifierGroupButton extends POSToggleButton implements ActionListener {
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
}
