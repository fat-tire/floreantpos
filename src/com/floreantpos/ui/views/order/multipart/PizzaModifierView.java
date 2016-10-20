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

package com.floreantpos.ui.views.order.multipart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.ScrollableFlowPanel;
import com.floreantpos.ui.views.order.modifier.ModifierSelectionListener;
import com.floreantpos.ui.views.order.modifier.ModifierSelectionModel;
import com.jidesoft.swing.TitledSeparator;

/**
 * 
 * @author MShahriar
 */
public class PizzaModifierView extends JPanel {
	private ModifierSelectionListener modifierSelectionListener;

	private ModifierSelectionModel modifierSelectionModel;

	private PosButton btnClear = new PosButton(POSConstants.CLEAR);

	private HashMap<String, ModifierButton> buttonMap = new HashMap<String, ModifierButton>();

	/** Creates new form GroupView */
	public PizzaModifierView(ModifierSelectionModel modifierSelectionModel) {
		//super(com.floreantpos.POSConstants.MODIFIERS);

		this.modifierSelectionModel = modifierSelectionModel;

		setLayout(new MigLayout("fillx, aligny top"));
		setBorder(new TitledBorder(null, "MODIFIERS", TitledBorder.CENTER, TitledBorder.CENTER));
		updateView();
	}

	private void addActionButtons() {
		//actionButtonPanel.add(btnClear);
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
	}

	//	public void setModifiers(Collection<MenuModifier> modifiers) {
	//		buttonMap.clear();
	//
	//		try {
	//
	//			List itemList = new ArrayList();
	//
	//			for (MenuModifier modifier : modifiers) {
	//				itemList.add(modifier);
	//			}
	//
	//			setItems(itemList);
	//		} catch (PosException e) {
	//			POSMessageDialog.showError(this, com.floreantpos.POSConstants.ERROR_MESSAGE, e);
	//		}
	//	}
	//
	//	@Override
	//	protected void renderItems() {
	//		super.renderItems();
	//		updateView();
	//	}

	protected AbstractButton createItemButton(Object item) {
		MenuModifier modifier = (MenuModifier) item;
		ModifierButton modifierButton = new ModifierButton(modifier);
		String key = modifier.getId() + "_" + modifier.getModifierGroup().getId(); //$NON-NLS-1$
		buttonMap.put(key, modifierButton);

		return modifierButton;
	}

	public void addModifierSelectionListener(ModifierSelectionListener listener) {
		this.modifierSelectionListener = listener;
	}

	public void removeModifierSelectionListener(ModifierSelectionListener listener) {
		this.modifierSelectionListener = null;
	}

	public void updateView() {
		MenuItem menuItem = modifierSelectionModel.getMenuItem();
		List<MenuItemModifierGroup> modiferGroups = menuItem.getMenuItemModiferGroups();

		for (MenuItemModifierGroup menuItemModifierGroup : modiferGroups) {
			/*MenuModifierGroup modifierGroup = menuItemModifierGroup.getModifierGroup();
			TitledSeparator separator = new TitledSeparator(modifierGroup.getDisplayName(), SwingConstants.CENTER);
			add(separator, "newline, grow");
			JPanel groupPanel = new JPanel();
			
			Set<MenuModifier> modifiers = modifierGroup.getModifiers();
			for (MenuModifier menuModifier : modifiers) {
				groupPanel.add(new ModifierButton(menuModifier));
			}
			add(groupPanel, "newline, grow");*/
			Font myFont = new Font("Serif", Font.BOLD, PosUIManager.getFontSize(16));
			JLabel groupName = new JLabel(menuItemModifierGroup.getModifierGroup().getName());
			groupName.setFont(myFont);
			TitledSeparator separator = new TitledSeparator(groupName, SwingConstants.CENTER);
			add(separator, "newline, grow");
			// JPanel mainPanel=new JPanel();
			ScrollableFlowPanel groupPanel = new ScrollableFlowPanel();
			groupPanel.setPreferredSize(new Dimension(PosUIManager.getSize(630, 0)));
			JScrollPane js = new JScrollPane(groupPanel);
			js.setBorder(null);

			// groupPanel.getContentPane().setSize(new Dimension(100, 0));

			Set<MenuModifier> modifiers = menuItemModifierGroup.getModifierGroup().getModifiers();
			for (MenuModifier menuModifier : modifiers) {
				groupPanel.getContentPane().add(new ModifierButton(menuModifier));
			}
			// mainPanel.add(groupPanel,BorderLayout.NORTH);
			add(js, "newline");
		}

		//		JPanel activePanel = getActivePanel();
		//		if (activePanel == null) {
		//			return;
		//		}
		//		Component[] components = activePanel.getComponents();
		//		if (components == null || components.length == 0)
		//			return;
		//
		//		TicketItem ticketItem = modifierSelectionModel.getTicketItem();
		//
		//		for (Component component : components) {
		//			ModifierButton modifierButton = (ModifierButton) component;
		//			MenuModifier modifier = modifierButton.menuModifier;
		//
		//			/*TicketItemModifierGroup ticketItemModifierGroup = ticketItem.findTicketItemModifierGroup(modifier, false);
		//			TicketItemModifier ticketItemModifier = ticketItemModifierGroup.findTicketItemModifier(modifier, false);
		//			if (ticketItemModifier != null) {
		//				modifierButton.setText("<html><center>" + modifier.getDisplayName() + "<br/><span style='color:green;"
		//						+ "'>(" + ticketItemModifier.getItemCount() + ")</span></center></html>"); //$NON-NLS-1$ //$NON-NLS-2$
		//			}
		//			else {
		//				modifierButton.setText("<html><center>" + modifier.getDisplayName() + "</center></html>"); //$NON-NLS-1$ //$NON-NLS-2$
		//			}*/
		//		}

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
			modifierSelectionListener.modifierSelected(menuModifier);
		}
	}

	public void setActionButtonsVisible(boolean b) {
		btnClear.setVisible(b);
	}
}
