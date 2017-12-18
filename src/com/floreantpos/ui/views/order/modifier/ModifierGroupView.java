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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.ModifierGroup;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.ScrollableFlowPanel;
import com.jidesoft.swing.SimpleScrollPane;

/**
 * 
 * @author MShahriar
 */
public class ModifierGroupView extends JPanel implements ComponentListener {
	private Vector<ModifierGroupSelectionListener> listenerList = new Vector<ModifierGroupSelectionListener>();

	private ModifierSelectionModel modifierSelectionModel;

	private ButtonGroup modifierGroupButtonGroup;

	private SimpleScrollPane simpleScrollPane;
	private ScrollableFlowPanel contentPanel;

	public static final String VIEW_NAME = "MODIFIER_GROUP_VIEW"; //$NON-NLS-1$

	/** Creates new form CategoryView */
	public ModifierGroupView(ModifierSelectionModel modifierSelectionModel) {
		this.modifierSelectionModel = modifierSelectionModel;

		setLayout(new BorderLayout());
		TitledBorder border = new TitledBorder(POSConstants.GROUPS);
		border.setTitleJustification(TitledBorder.CENTER);
		setBorder(border);

		contentPanel = new ScrollableFlowPanel();
		simpleScrollPane = new SimpleScrollPane(contentPanel);
		simpleScrollPane.setBorder(null);
		simpleScrollPane.setAutoscrolls(false);
		simpleScrollPane.setScrollOnRollover(false);
		simpleScrollPane.setVerticalUnitIncrement(TerminalConfig.getTouchScreenButtonHeight());
		simpleScrollPane.getScrollUpButton().setPreferredSize(new Dimension(100, TerminalConfig.getTouchScreenButtonHeight()));
		simpleScrollPane.getScrollDownButton().setPreferredSize(new Dimension(100, TerminalConfig.getTouchScreenButtonHeight()));

		add(simpleScrollPane);

		modifierGroupButtonGroup = new ButtonGroup();
		setPreferredSize(new Dimension(120, 100));

		init();

		addComponentListener(this);
	}

	public void reset() {
		modifierGroupButtonGroup = new ButtonGroup();
	}

	private void init() {
		List<MenuItemModifierGroup> modifierGroups = modifierSelectionModel.getMenuItem().getMenuItemModiferGroups();
		Collections.sort(modifierGroups, new Comparator<MenuItemModifierGroup>() {
			@Override
			public int compare(MenuItemModifierGroup o1, MenuItemModifierGroup o2) {
				return o2.getMinQuantity() - o1.getMinQuantity();
			}
		});

		for (Iterator<MenuItemModifierGroup> iter = modifierGroups.iterator(); iter.hasNext();) {
			MenuItemModifierGroup menuItemModifierGroup = iter.next();
			ModifierGroup menuModifierGroup = menuItemModifierGroup.getModifierGroup();
			Set<MenuModifier> modifiers = menuModifierGroup.getModifiers();
			if (modifiers == null || modifiers.size() == 0) {
				continue;
			}

			menuModifierGroup.setMenuItemModifierGroup(menuItemModifierGroup);

			contentPanel.add(createItemButton(menuModifierGroup));
		}
		contentPanel.revalidate();
		contentPanel.repaint();
	}

	protected AbstractButton createItemButton(Object item) {
		ModifierGroup menuModifierGroup = (ModifierGroup) item;

		ModifierGroupButton button = new ModifierGroupButton(menuModifierGroup);
		button.setPreferredSize(new Dimension(100, 80));
		modifierGroupButtonGroup.add(button);

		return button;
	}

	public void addModifierGroupSelectionListener(ModifierGroupSelectionListener listener) {
		listenerList.add(listener);
	}

	public void removeModifierGroupSelectionListener(ModifierGroupSelectionListener listener) {
		listenerList.remove(listener);
	}

	private void fireModifierGroupSelected(ModifierGroup foodModifierGroup) {
		for (ModifierGroupSelectionListener listener : listenerList) {
			listener.modifierGroupSelected(foodModifierGroup);
		}
	}

	public void setSelectedModifierGroup(ModifierGroup modifierGroup) {
		Component[] components = contentPanel.getContentPane().getComponents();
		if (components != null && components.length > 0) {
			for (Component component : components) {
				ModifierGroupButton button = (ModifierGroupButton) component;
				if (button.menuModifierGroup.getId() == modifierGroup.getId()) {
					button.setSelected(true);
					Rectangle bounds = button.getBounds();
					bounds.height = bounds.height * 2;
					simpleScrollPane.scrollRectToVisible(bounds);
					fireModifierGroupSelected(button.menuModifierGroup);
					break;
				}
			}
		}
	}

	public void selectFirst() {
		Component[] components = contentPanel.getContentPane().getComponents();
		if (components != null && components.length > 0) {
			ModifierGroupButton button = (ModifierGroupButton) components[0];
			button.setSelected(true);
			fireModifierGroupSelected(button.menuModifierGroup);
		}
	}

	public void selectNextGroup() {
		ModifierGroupButton button = getNextMandatoryGroup();
		button.setSelected(true);
		fireModifierGroupSelected(button.menuModifierGroup);
	}

	public boolean hasNextMandatoryGroup() {
		return getNextMandatoryGroup() != null;
	}

	private ModifierGroupButton getNextMandatoryGroup() {
		Component[] components = contentPanel.getContentPane().getComponents();
		if (components != null && components.length > 0) {
			for (int i = 0; i < components.length; i++) {
				ModifierGroupButton button = (ModifierGroupButton) components[i];
				if (button.isSelected() && i < (components.length - 1)) {
					ModifierGroupButton modifierGroupButton = (ModifierGroupButton) components[i + 1];
					if (modifierGroupButton.menuModifierGroup.getMenuItemModifierGroup().getMinQuantity() > 0) {
						return modifierGroupButton;
					}
				}
			}
		}

		return null;
	}

	private class ModifierGroupButton extends POSToggleButton implements ActionListener {
		ModifierGroup menuModifierGroup;

		ModifierGroupButton(ModifierGroup menuModifierGroup) {
			this.menuModifierGroup = menuModifierGroup;
			updateButtonText();
			addActionListener(this);
		}

		private void updateButtonText() {
			String string = "";
			//			if (addOnMode) {
			//				string = "<html><body><center>" + menuModifierGroup.getDisplayName() + "</center></body></html>";//$NON-NLS-1$ //$NON-NLS-2$ 
			//			}
			//			else {
			string = "<html><body><center> "
					+ menuModifierGroup.getDisplayName()
					+ "<br/>"
					+ "<strong><span style='color:white;background-color:orange;margin:0;" + "'>&nbsp; " + menuModifierGroup.getMenuItemModifierGroup().getMinQuantity() + "&nbsp; </span></center></body></html>"; //$NON-NLS-1$ //$NON-NLS-2$ 
			//			}

			setText(string);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (isSelected()) {
				fireModifierGroupSelected(menuModifierGroup);
			}
		}
	}

	@Override
	public void componentResized(ComponentEvent e) {
		int verticalUnitIncrement = simpleScrollPane.getViewport().getVisibleRect().height - TerminalConfig.getTouchScreenButtonHeight();
		if (verticalUnitIncrement < TerminalConfig.getTouchScreenButtonHeight()) {
			verticalUnitIncrement = TerminalConfig.getTouchScreenButtonHeight();
		}
		simpleScrollPane.setVerticalUnitIncrement(verticalUnitIncrement);
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}
}
