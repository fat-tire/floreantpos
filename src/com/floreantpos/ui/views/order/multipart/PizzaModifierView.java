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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemSize;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.ModifierGroup;
import com.floreantpos.model.Multiplier;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.dao.MultiplierDAO;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.ScrollableFlowPanel;
import com.floreantpos.ui.views.order.modifier.ModifierGroupSelectionListener;
import com.floreantpos.ui.views.order.modifier.ModifierGroupView;
import com.floreantpos.ui.views.order.modifier.ModifierSelectionListener;
import com.floreantpos.ui.views.order.modifier.ModifierSelectionModel;

/**
 * 
 * @author MShahriar
 */
public class PizzaModifierView extends JPanel implements ModifierGroupSelectionListener {
	private ModifierSelectionListener modifierSelectionListener;

	//private ModifierSelectionModel modifierSelectionModel;

	private PosButton btnClear = new PosButton(POSConstants.CLEAR);

	private HashMap<String, ModifierButton> buttonMap = new HashMap<String, ModifierButton>();
	private Multiplier selectedMultiplier;
	private MultiplierButton defaultMultiplierButton;

	private ModifierGroupView modifierGroupView;

	private JPanel mainPanel;

	private JPanel contentPanel;
	private PizzaModifierSelectionDialog pizzaModifierSelectionDialog;
	private ModifierGroup menuModifierGroup;
	private ScrollableFlowPanel groupPanel;

	public PizzaModifierView(TicketItem ticketItem, MenuItem menuItem, PizzaModifierSelectionDialog pizzaModifierSelectionDialog) {
		ModifierSelectionModel modifierSelectionModel = new ModifierSelectionModel(ticketItem, menuItem);
		//this.modifierSelectionModel = modifierSelectionModel;
		this.pizzaModifierSelectionDialog = pizzaModifierSelectionDialog;
		setLayout(new BorderLayout());
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(new TitledBorder(null, "MODIFIERS", TitledBorder.CENTER, TitledBorder.CENTER));
		contentPanel = new JPanel();
		contentPanel.setLayout(new MigLayout("fillx, aligny top"));
		modifierGroupView = new com.floreantpos.ui.views.order.modifier.ModifierGroupView(modifierSelectionModel);
		add(modifierGroupView, BorderLayout.EAST);
		add(mainPanel, BorderLayout.CENTER);
		addMultiplierButtons();
		modifierGroupView.addModifierGroupSelectionListener(this);

		modifierGroupView.selectFirst();
	}

	private void addActionButtons() {
		//actionButtonPanel.add(btnClear);
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
	}

	private void addMultiplierButtons() {
		JPanel multiplierPanel = new JPanel(new MigLayout("fillx,center"));
		List<Multiplier> multiplierList = MultiplierDAO.getInstance().findAll();
		ButtonGroup group = new ButtonGroup();
		if (multiplierList != null) {
			for (Multiplier multiplier : multiplierList) {
				MultiplierButton btnMultiplier = new MultiplierButton(multiplier);
				if (multiplier.isDefaultMultiplier()) {
					selectedMultiplier = multiplier;
					defaultMultiplierButton = btnMultiplier;
					btnMultiplier.setSelected(true);
				}
				multiplierPanel.add(btnMultiplier, "grow");
				group.add(btnMultiplier);
			}
		}
		mainPanel.add(multiplierPanel, BorderLayout.SOUTH);
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
		ModifierButton modifierButton = new ModifierButton(modifier, null, null);
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
		contentPanel.removeAll();
		groupPanel = new ScrollableFlowPanel();
		groupPanel.setPreferredSize(new Dimension(PosUIManager.getSize(500, 0)));
		JScrollPane js = new JScrollPane(groupPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		js.setBorder(null);

		Set<MenuModifier> modifiers = menuModifierGroup.getModifiers();
		for (MenuModifier menuModifier : modifiers) {
			if (!menuModifier.isPizzaModifier()) {
				continue;
			}
			menuModifier.setMenuItemModifierGroup(menuModifierGroup.getMenuItemModifierGroup());
			groupPanel.getContentPane().add(new ModifierButton(menuModifier, selectedMultiplier, pizzaModifierSelectionDialog.getSelectedSize()));
		}
		contentPanel.add(js, "newline,top,center");
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		contentPanel.repaint();
		mainPanel.repaint();

	}

	private class ModifierButton extends PosButton implements ActionListener {
		private MenuModifier menuModifier;

		public ModifierButton(MenuModifier modifier, Multiplier multiplier, MenuItemSize menuItemSize) {
			this.menuModifier = modifier;

			setText("<html><center>" + modifier.getDisplayName() + "<br/>" + modifier.getPriceForSizeAndMultiplier(menuItemSize, true, multiplier) + "</center></html>"); //$NON-NLS-1$ //$NON-NLS-2$

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
			modifierSelectionListener.modifierSelected(menuModifier, selectedMultiplier);

		//	defaultMultiplierButton.setSelected(true);
		//	selectedMultiplier = defaultMultiplierButton.getMultiplier();
			
			groupPanel.getContentPane().removeAll();
			Set<MenuModifier> modifiers = menuModifierGroup.getModifiers();
			for (MenuModifier menuModifier : modifiers) {
				if (!menuModifier.isPizzaModifier()) {
					continue;
				}
				menuModifier.setMenuItemModifierGroup(menuModifierGroup.getMenuItemModifierGroup());
				groupPanel.getContentPane().add(new ModifierButton(menuModifier, selectedMultiplier, pizzaModifierSelectionDialog.getSelectedSize()));
			}
			contentPanel.repaint();
			mainPanel.repaint();
		}
	}

	public void setActionButtonsVisible(boolean b) {
		btnClear.setVisible(b);
	}

	private class MultiplierButton extends POSToggleButton implements ActionListener {
		private Multiplier multiplier;

		public MultiplierButton(Multiplier multiplier) {
			this.multiplier = multiplier;
			setText(multiplier.getName());
			Integer buttonColor = multiplier.getButtonColor();
			if (buttonColor != null) {
				setBackground(new Color(buttonColor));
			}
			Integer textColor = multiplier.getTextColor();
			if (textColor != null) {
				setForeground(new Color(textColor));
			}
			setBorder(BorderFactory.createLineBorder(Color.GRAY));
			addActionListener(this);
		}

		public Multiplier getMultiplier() {
			return multiplier;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			selectedMultiplier = multiplier;
			updateModifierPrice();
		}

		private void updateModifierPrice() {
			groupPanel.getContentPane().removeAll();
			Set<MenuModifier> modifiers = menuModifierGroup.getModifiers();
			for (MenuModifier menuModifier : modifiers) {
				if (!menuModifier.isPizzaModifier()) {
					continue;
				}
				menuModifier.setMenuItemModifierGroup(menuModifierGroup.getMenuItemModifierGroup());
				groupPanel.getContentPane().add(new ModifierButton(menuModifier, selectedMultiplier, pizzaModifierSelectionDialog.getSelectedSize()));
			}
			contentPanel.repaint();
			mainPanel.repaint();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			if (isSelected())
				setBorder(BorderFactory.createLineBorder(new Color(255, 128, 0), 1));
			else
				setBorder(BorderFactory.createLineBorder(Color.GRAY));

		}
	}

	@Override
	public void modifierGroupSelected(ModifierGroup menuModifierGroup) {
		this.menuModifierGroup = menuModifierGroup;
		contentPanel.repaint();
		contentPanel.revalidate();
		updateView();
	}

	public ModifierGroupView getModifierGroupView() {
		return modifierGroupView;
	}

}
