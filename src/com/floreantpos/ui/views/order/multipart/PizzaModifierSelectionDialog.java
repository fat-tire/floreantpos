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

package com.floreantpos.ui.views.order.multipart;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JViewport;
import javax.swing.border.TitledBorder;

import com.floreantpos.POSConstants;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuItemSize;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.model.Multiplier;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.PizzaPrice;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.order.modifier.ModifierSelectionListener;
import com.floreantpos.ui.views.order.modifier.ModifierSelectionModel;
import com.floreantpos.util.CurrencyUtil;
import com.floreantpos.util.POSUtil;

import net.miginfocom.swing.MigLayout;

/**
 * 
 * @author MShahriar
 */
public class PizzaModifierSelectionDialog extends POSDialog implements ModifierSelectionListener {
	private static final String PROP_PIZZA_PRICE = "pizzaPrice";
	private ModifierSelectionModel modifierSelectionModel;
	private SizeAndCrustSelectionPane sizeAndCrustPanel;
	private PizzaModifierView modifierView;

	private List<Section> sectionList;
	private boolean crustSelected = false;
	private Section sectionQuarter1;
	private Section sectionQuarter2;
	private Section sectionQuarter3;
	private Section sectionQuarter4;
	private Section sectionHalf1;
	private Section sectionHalf2;
	private Section sectionWhole;
	private TicketItemModifier crustModifier;

	private CardLayout sectionLayout = new CardLayout();
	JPanel sectionView = new Pizza(this.sectionLayout);
	private JPanel halfSectionLayout = new TransparentPanel(new GridLayout(1, 2, 2, 2));
	private JPanel quarterSectionLayout = new TransparentPanel(new GridLayout(2, 2, 2, 2));

	public PizzaModifierSelectionDialog(ModifierSelectionModel modifierSelectionModel) {
		this.modifierSelectionModel = modifierSelectionModel;

		initComponents();
		updateView();
	}

	private void initComponents() {
		setTitle("MODIFY PIZZA");

		setLayout(new java.awt.BorderLayout(10, 10));
		JPanel panel = (JPanel) getContentPane();
		panel.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
		createSectionPanel();

		//JPanel centerPanel = new JPanel(new BorderLayout());

		sizeAndCrustPanel = new SizeAndCrustSelectionPane();
		//centerPanel.add(sizeAndCrustPanel, BorderLayout.NORTH);
		add(sizeAndCrustPanel, BorderLayout.NORTH);

		modifierView = new PizzaModifierView(modifierSelectionModel);
		modifierView.addModifierSelectionListener(this);
		PosScrollPane modifierSc = new PosScrollPane(modifierView);
		modifierSc.setBorder(null);
		//centerPanel.add(modifierSc, BorderLayout.CENTER);
		add(modifierSc, BorderLayout.CENTER);
		//add(centerPanel, BorderLayout.CENTER);

		createButtonPanel();
	}

	private void updateView() {
		TicketItem ticketItem = modifierSelectionModel.getTicketItem();
		List<TicketItemModifier> ticketItemModifiers = ticketItem.getTicketItemModifiers();

		if (ticketItemModifiers == null) {
			return;
		}
		for (Section section : sectionList) {
			for (Iterator iterator = ticketItemModifiers.iterator(); iterator.hasNext();) {
				TicketItemModifier ticketItemModifier = (TicketItemModifier) iterator.next();
				if (!ticketItemModifier.isInfoOnly() && section.getSectionName().equals(ticketItemModifier.getSectionName())) {
					section.model.addElement(ticketItemModifier);
				}
			}
		}
	}

	private void createSectionPanel() {
		JPanel westPanel = new JPanel(new BorderLayout(5, 5));

		sectionView.setBorder(BorderFactory.createTitledBorder(null, "SECTIONS", TitledBorder.CENTER, TitledBorder.CENTER));

		sectionList = new ArrayList<>();

		sectionWhole = new Section("WHOLE", "WHOLE", 0, true);
		sectionQuarter1 = new Section("Quarter 1", "Quarter 1", 1, false);
		sectionQuarter2 = new Section("Quarter 2", "Quarter 2", 2, false);
		sectionQuarter3 = new Section("Quarter 3", "Quarter 3", 3, false);
		sectionQuarter4 = new Section("Quarter 4", "Quarter 4", 4, false);
		sectionHalf1 = new Section("Half 1", "Half 1", 5, false);
		sectionHalf2 = new Section("Half 2", "Half 2", 6, false);

		sectionList.add(sectionWhole);

		sectionList.add(sectionQuarter1);
		sectionList.add(sectionQuarter2);
		sectionList.add(sectionQuarter3);
		sectionList.add(sectionQuarter4);

		sectionList.add(sectionHalf1);
		sectionList.add(sectionHalf2);

		halfSectionLayout.add(sectionHalf1);
		halfSectionLayout.add(sectionHalf2);

		quarterSectionLayout.add(sectionQuarter1);
		quarterSectionLayout.add(sectionQuarter2);
		quarterSectionLayout.add(sectionQuarter3);
		quarterSectionLayout.add(sectionQuarter4);

		sectionView.add(halfSectionLayout, "half");
		sectionView.add(quarterSectionLayout, "quarter");
		sectionLayout.show(sectionView, "half");
		halfSectionLayout.add(sectionHalf1);
		halfSectionLayout.add(sectionHalf2);

		quarterSectionLayout.add(sectionQuarter1);
		quarterSectionLayout.add(sectionQuarter2);
		quarterSectionLayout.add(sectionQuarter3);
		quarterSectionLayout.add(sectionQuarter4);

		//		sectionView.add(sectionQuarter1, "grow,cell 0 0");
		//		sectionView.add(sectionQuarter3, "grow,cell 0 1");
		//		sectionView.add(sectionQuarter2, "grow,cell 2 0");
		//		sectionView.add(sectionQuarter4, "grow,cell 2 1");
		//		sectionView.add(sectionHalf1, "grow,cell 0 0");
		//		sectionView.add(sectionHalf2, "grow,cell 1 0");
		sectionView.add(halfSectionLayout, "half");
		sectionView.add(quarterSectionLayout, "quarter");
		sectionLayout.show(sectionView, "half");

		//		sectionView.add(sectionQuarter1, "grow,cell 0 0");
		//		sectionView.add(sectionQuarter3, "grow,cell 0 1");
		//		sectionView.add(sectionQuarter2, "grow,cell 2 0");
		//		sectionView.add(sectionQuarter4, "grow,cell 2 1");
		//		sectionView.add(sectionHalf1, "grow,cell 0 0");
		//		sectionView.add(sectionHalf2, "grow,cell 1 0");
		JPanel wholeSectionView = new JPanel(new MigLayout("fill"));
		wholeSectionView.add(sectionWhole, "grow");
		//sectionView.add(pizza, "gapleft 20,gapright 20,cell 1 0 1 2");

		//		sectionQuarter1.setOpaque(false);
		//		sectionQuarter2.setOpaque(false);
		//		sectionQuarter3.setOpaque(false);
		//		sectionQuarter4.setOpaque(false);
		//		sectionHalf1.setOpaque(false);
		//		sectionHalf2.setOpaque(false);
		//		sectionWhole.setOpaque(false);

		sectionView.setOpaque(false);
		westPanel.setOpaque(false);
		westPanel.add(sectionView, BorderLayout.CENTER);
		westPanel.add(wholeSectionView, BorderLayout.SOUTH);
		add(westPanel, BorderLayout.WEST);
	}

	public void createButtonPanel() {
		TransparentPanel buttonPanel = new com.floreantpos.swing.TransparentPanel();
		buttonPanel.setLayout(new MigLayout("fill, ins 4", "", ""));

		ButtonGroup btnGroup = new ButtonGroup();
		POSToggleButton btnHalf = new POSToggleButton("HALF");
		btnHalf.setSelected(true);

		btnHalf.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//				sectionQuarter1.setVisible(false);
				//				sectionQuarter2.setVisible(false);
				//				sectionQuarter3.setVisible(false);
				//				sectionQuarter4.setVisible(false);
				//
				//				sectionHalf1.setVisible(true);
				//				sectionHalf2.setVisible(true);

				for (Iterator iterator = sectionList.iterator(); iterator.hasNext();) {
					Section section = (Section) iterator.next();
					section.clearItems();
					clearTicketItemModifierGroups();
				}
				sectionLayout.show(sectionView, "half");
			}
		});
		POSToggleButton btnQuarter = new POSToggleButton("QUARTER");

		btnQuarter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//				sectionQuarter1.setVisible(true);
				//				sectionQuarter2.setVisible(true);
				//				sectionQuarter3.setVisible(true);
				//				sectionQuarter4.setVisible(true);
				//
				//				sectionHalf1.setVisible(false);
				//				sectionHalf2.setVisible(false);

				for (Iterator iterator = sectionList.iterator(); iterator.hasNext();) {
					Section section = (Section) iterator.next();
					section.clearItems();
					clearTicketItemModifierGroups();
				}
				sectionLayout.show(sectionView, "quarter");
			}
		});

		btnGroup.add(btnHalf);
		btnGroup.add(btnQuarter);

		PosButton btnClear = new PosButton("CLEAR");
		btnClear.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Section section = getSelectedSection();
				if (section == null) {
					return;
				}
				section.clearItems();
				clearTicketItemModifierGroups();
			}
		});

		PosButton btnSave = new PosButton("DONE");
		btnSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (doFinishModifierSelection()) {
					setCanceled(false);
					dispose();
				}
			}
		});

		PosButton btnCancel = new PosButton(POSConstants.CANCEL.toUpperCase());
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setCanceled(true);
				dispose();
			}
		});
		int width = PosUIManager.getSize(170);
		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		buttonPanel.add(btnHalf, "w " + width + "!, split 4");
		buttonPanel.add(btnQuarter, "w " + width + "!");
		buttonPanel.add(separator, "growy");
		buttonPanel.add(btnClear, "gapright 20,grow");
		buttonPanel.add(btnCancel, "grow");
		buttonPanel.add(btnSave, "grow");

		add(buttonPanel, java.awt.BorderLayout.SOUTH);
	}

	private boolean doFinishModifierSelection() {
		if (!crustSelected) {
			POSMessageDialog.showError("Please select size and crust.");
			return false;
		}
		TicketItem ticketItem = modifierSelectionModel.getTicketItem();
		List<TicketItemModifier> ticketItemModifiers = ticketItem.getTicketItemModifiers();
		List<TicketItemModifier> allTicketItemModifiers = new ArrayList<TicketItemModifier>();

		boolean isRequiredModifierAdded = false;

		List<MenuItemModifierGroup> menuItemModiferGroups = ticketItem.getMenuItem().getMenuItemModiferGroups();
		for (Iterator iterator = menuItemModiferGroups.iterator(); iterator.hasNext();) {
			MenuItemModifierGroup menuItemModifierGroup = (MenuItemModifierGroup) iterator.next();
			if (ticketItem.requiredModifiersAdded(menuItemModifierGroup)) {
				isRequiredModifierAdded = true;
				continue;
			}
			isRequiredModifierAdded = false;
		}

		if (!isRequiredModifierAdded) {
			POSMessageDialog.showMessage(POSUtil.getFocusedWindow(), "Required modifiers for group not added!");
			return false;
		}

		allTicketItemModifiers.add(crustModifier);

		for (Section section : sectionList) {
			Enumeration<TicketItemModifier> elements = section.model.elements();
			if (elements.hasMoreElements()) {

				TicketItemModifier ticketItemModifier = new TicketItemModifier();
				ticketItemModifier.setName(" = " + section.getSectionName());
				ticketItemModifier.setModifierType(TicketItemModifier.SEPERATOR);
				ticketItemModifier.setInfoOnly(true);
				ticketItemModifier.setTicketItem(ticketItem);

				allTicketItemModifiers.add(ticketItemModifier);

				for (Iterator iterator = ticketItemModifiers.iterator(); iterator.hasNext();) {
					TicketItemModifier existingModifier = (TicketItemModifier) iterator.next();
					if (existingModifier.getSectionName() == null) {
						continue;
					}
					if (existingModifier.getSectionName().equals(section.getSectionName())) {
						allTicketItemModifiers.add(existingModifier);
					}
				}
			}
		}

		if (ticketItemModifiers != null) {
			ticketItem.getTicketItemModifiers().clear();
		}

		for (Iterator iterator = allTicketItemModifiers.iterator(); iterator.hasNext();) {
			TicketItemModifier ticketItemModifier = (TicketItemModifier) iterator.next();
			ticketItem.addToticketItemModifiers(ticketItemModifier);
		}

		return true;
	}

	@Override
	public void modifierSelected(MenuModifier modifier, Multiplier multiplier) {
		TicketItem ticketItem = modifierSelectionModel.getTicketItem();

		MenuItemModifierGroup menuItemModifierGroup = modifier.getMenuItemModifierGroup();
		int countModifier = ticketItem.countModifierFromGroup(menuItemModifierGroup);

		int minQuantity = menuItemModifierGroup.getMinQuantity();
		int maxQuantity = menuItemModifierGroup.getMaxQuantity();

		if (maxQuantity < minQuantity) {
			maxQuantity = minQuantity;
		}

		if (countModifier >= maxQuantity) {
			POSMessageDialog.showError("You have added maximum number of allowed modifiers from group " + modifier.getModifierGroup().getDisplayName());
			modifierGroupSelectionDone(menuItemModifierGroup.getModifierGroup());
			return;
		}
		Section section = getSelectedSection();
		TicketItemModifier ticketItemModifier = ticketItem.findTicketItemModifierFor(modifier, section.getSectionName());
		if (ticketItemModifier == null) {
			OrderType orderType = ticketItem.getTicket().getOrderType();
			ticketItemModifier = convertToTicketItemModifier(ticketItem, modifier, orderType, multiplier);

			ticketItemModifier.setSectionName(section.getSectionName());
			section.addItem(ticketItemModifier);
			ticketItem.addToticketItemModifiers(ticketItemModifier);
		}
		else {
			ticketItemModifier.setItemCount(ticketItemModifier.getItemCount() + 1);
			section.repaint();
		}
		if (countModifier + 1 >= minQuantity) {
			modifierGroupSelectionDone(menuItemModifierGroup.getModifierGroup());
		}
	}

	@Override
	public void clearModifiers(MenuModifierGroup modifierGroup) {
	}

	private void clearTicketItemModifierGroups() {
		//		List<TicketItemModifierGroup> ticketItemModifierGroups = modifierSelectionModel.getTicketItem().getTicketItemModifierGroups();
		//		if (ticketItemModifierGroups == null) {
		//			return;
		//		}
		//		if (!ticketItemModifierGroups.isEmpty()) {
		//
		//			for (Iterator iterator = ticketItemModifierGroups.iterator(); iterator.hasNext();) {
		//				TicketItemModifierGroup ticketItemModifierGroup = (TicketItemModifierGroup) iterator.next();
		//				List<TicketItemModifier> ticketItemModifiers = ticketItemModifierGroup.getTicketItemModifiers();
		//				if (ticketItemModifiers != null) {
		//					for (Iterator iterator2 = ticketItemModifiers.iterator(); iterator2.hasNext();) {
		//						TicketItemModifier ticketItemModifier = (TicketItemModifier) iterator2.next();
		//						if (ticketItemModifier.getModifierType() != TicketItemModifier.CRUST) {
		//							iterator2.remove();
		//						}
		//					}
		//				}
		//			}
		//		}
	}

	@Override
	public void modifierGroupSelectionDone(MenuModifierGroup modifierGroup) {
		MenuItemModifierGroup menuItemModifierGroup = modifierGroup.getMenuItemModifierGroup();
		if (!isRequiredModifiersAdded(modifierSelectionModel.getTicketItem(), menuItemModifierGroup)) {
			showModifierSelectionMessage(menuItemModifierGroup);
			modifierView.getModifierGroupView().setSelectedModifierGroup(menuItemModifierGroup.getModifierGroup());
			return;
		}

		if (modifierView.getModifierGroupView().hasNextMandatoryGroup()) {
			modifierView.getModifierGroupView().selectNextGroup();
		}
		else {
			//	setCanceled(false);
			//	dispose();
		}
	}

	public ModifierSelectionModel getModifierSelectionModel() {
		return modifierSelectionModel;
	}

	public void setModifierSelectionModel(ModifierSelectionModel modifierSelectionModel) {
		this.modifierSelectionModel = modifierSelectionModel;
	}

	public static boolean isRequiredModifiersAdded(TicketItem ticketItem, MenuItemModifierGroup menuItemModifierGroup) {
		//		int minQuantity = menuItemModifierGroup.getMinQuantity();
		//		if (minQuantity <= 0) {
		//			return true;
		//		}
		//
		//		Set<MenuModifier> modifiers = menuItemModifierGroup.getModifierGroup().getModifiers();
		//		if (modifiers == null || modifiers.size() == 0) {
		//			return true;
		//		}
		//
		//		TicketItemModifierGroup ticketItemModifierGroup = ticketItem.findTicketItemModifierGroup(menuItemModifierGroup.getId());
		//
		//		if (ticketItemModifierGroup == null || ticketItemModifierGroup.countFreeModifiers() < minQuantity) {
		//			return false;
		//		}
		return true;
	}

	private TicketItemModifier convertToTicketItemModifier(TicketItem ticketItem, MenuModifier menuModifier, OrderType type, Multiplier multiplier) {
		TicketItemModifier ticketItemModifier = new TicketItemModifier();
		ticketItemModifier.setMenuItemId(menuModifier.getId());
		MenuItemModifierGroup menuItemModifierGroup = menuModifier.getMenuItemModifierGroup();
		if (menuItemModifierGroup != null) {
			ticketItemModifier.setMenuItemModifierGroupId(menuItemModifierGroup.getId());
		}
		ticketItemModifier.setItemCount(1);
		ticketItemModifier.setName(menuModifier.getDisplayName());
		ticketItemModifier.setTicketItem(ticketItem);
		double priceForSize = menuModifier.getPriceForSizeAndMultiplier(getSelectedSize(), false, multiplier);
		if (multiplier != null) {
			ticketItemModifier.setName(multiplier.getTicketPrefix() + " " + menuModifier.getDisplayName());
			//priceForSize = menuModifier.getPriceForMultiplier(multiplier);
		}
		ticketItemModifier.setUnitPrice(priceForSize);
		ticketItemModifier.setTaxRate(menuModifier.getTaxByOrderType(type));
		ticketItemModifier.setModifierType(TicketItemModifier.NORMAL_MODIFIER);
		ticketItemModifier.setShouldPrintToKitchen(menuModifier.isShouldPrintToKitchen());

		return ticketItemModifier;
	}

	private MenuItemSize getSelectedSize() {
		List<POSToggleButton> sizeButtonList = sizeAndCrustPanel.sizeButtonList;
		for (POSToggleButton posToggleButton : sizeButtonList) {
			if (posToggleButton.isSelected()) {
				PizzaPrice pizzaPrice = (PizzaPrice) posToggleButton.getClientProperty(PROP_PIZZA_PRICE);
				return pizzaPrice.getSize();
			}
		}

		return null;
	}

	private class Section extends JPanel implements MouseListener {
		JList<TicketItemModifier> list;
		DefaultListModel<TicketItemModifier> model;
		boolean selected;
		private boolean mainSection;
		private JLabel lblTitle;
		private final String sectionName;
		private final int sortOrder;
		private final String displayTitle;

		public Section(String sectionName, String displayTitle, int sortOrder, boolean main) {
			this.sectionName = sectionName;
			this.displayTitle = displayTitle;
			this.sortOrder = sortOrder;
			this.mainSection = main;

			setLayout(new BorderLayout());
			lblTitle = new JLabel(sectionName);
			lblTitle.setBackground(Color.LIGHT_GRAY);
			lblTitle.setHorizontalAlignment(JLabel.CENTER);
			lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 20));
			lblTitle.setOpaque(true);

			add(lblTitle, BorderLayout.NORTH);
			setOpaque(false);
			setPreferredSize(PosUIManager.getSize(160, 170));
			//setBackground(Color.white);

			setBorder(BorderFactory.createLineBorder(Color.GRAY));

			list = new JList();
			list.setCellRenderer(new TransparentListCellRenderer());
			list.setOpaque(false);
			list.setFixedCellHeight(PosUIManager.getSize(35));
			model = new DefaultListModel<TicketItemModifier>();
			list.setModel(model);
			JScrollPane scrollPane = new JScrollPane(list);
			JViewport viewPort = scrollPane.getViewport();
			viewPort.setOpaque(false);
			scrollPane.setOpaque(false);
			scrollPane.setBorder(null);
			add(scrollPane, BorderLayout.CENTER);
			addMouseListener(this);
			list.addMouseListener(this);

			//			List<TicketItemModifier> ticketItemModifiers = modifierGroup.getTicketItemModifiers();
			//			if (ticketItemModifiers != null) {
			//				for (TicketItemModifier ticketItemModifier : ticketItemModifiers) {
			//					if (ticketItemModifier.getModifierType() != TicketItemModifier.CRUST) {
			//						model.addElement(ticketItemModifier);
			//					}
			//				}
			//			}
		}

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D graphics2d = (Graphics2D) g;
			AlphaComposite composite = (AlphaComposite) graphics2d.getComposite();
			AlphaComposite composite2 = composite.derive(0.75f);
			graphics2d.setComposite(composite2);
			super.paintComponent(g);
		}

		public void clearItems() {
			//model.clear();
			//			List<TicketItemModifier> ticketItemModifiers = modifierGroup.getTicketItemModifiers();
			//			if (ticketItemModifiers != null) {
			//				for (Iterator iterator = ticketItemModifiers.iterator(); iterator.hasNext();) {
			//					TicketItemModifier ticketItemModifier = (TicketItemModifier) iterator.next();
			//					if (ticketItemModifier.getModifierType() != TicketItemModifier.CRUST) {
			//						iterator.remove();
			//					}
			//				}
			//			}

			Enumeration<TicketItemModifier> elements = model.elements();
			int index = 0;
			int modifierNumberSentToKitchen = 0;

			List<Integer> toBeDeleted = new ArrayList<Integer>();

			while (elements.hasMoreElements()) {
				TicketItemModifier existingModifier = elements.nextElement();

				if (existingModifier.isPrintedToKitchen()) {
					index++;
					modifierNumberSentToKitchen++;
					continue;
				}
				toBeDeleted.add(index);
				index++;
			}

			Collections.reverse(toBeDeleted);

			for (Iterator iterator = toBeDeleted.iterator(); iterator.hasNext();) {
				Integer integer = (Integer) iterator.next();
				model.removeElementAt(integer);
			}
			TicketItem ticketItem = modifierSelectionModel.getTicketItem();
			List<TicketItemModifier> ticketItemModifiers = ticketItem.getTicketItemModifiers();
			if (ticketItemModifiers != null) {
				for (Iterator iterator = ticketItemModifiers.iterator(); iterator.hasNext();) {
					TicketItemModifier ticketItemModifier = (TicketItemModifier) iterator.next();
					if (ticketItemModifier.isInfoOnly()) {
						continue;
					}
					if (!ticketItemModifier.isPrintedToKitchen()) {
						iterator.remove();
						ticketItem.removeTicketItemModifier(ticketItemModifier);
					}

				}
			}

			if (modifierNumberSentToKitchen > 0)
				POSMessageDialog.showMessage(POSUtil.getFocusedWindow(), "Modifiers that sent to kitchen can not be deleted!");
		}

		//		List<TicketItemModifier> getTicketItemModifiers() {
		//			return model.e
		//		}

		public void setSelected(boolean selected) {
			this.selected = selected;
			repaint();
		}

		public boolean isSelected() {
			return selected;
		}

		public void addItem(TicketItemModifier newModifier) {
			Enumeration<TicketItemModifier> elements = model.elements();
			while (elements.hasMoreElements()) {
				TicketItemModifier existingModifier = elements.nextElement();
				if (existingModifier.getMenuItemId().intValue() == newModifier.getMenuItemId().intValue()) {
					return;
				}
			}

			model.addElement(newModifier);
			//modifierGroup.addToticketItemModifiers(newModifier);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			setSelectedSection(this);
		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}

		public String getSectionName() {
			return sectionName;
		}
	}

	public void setSelectedSection(Section section) {
		if (section.isSelected()) {
			//section.lblTitle.setBackground(Color.lightGray);
			//section.setSelected(false);
			return;
		}
		for (Section sec : sectionList) {
			sec.lblTitle.setBackground(Color.lightGray);
			sec.setSelected(false);
		}
		section.lblTitle.setBackground(Color.yellow);
		section.setSelected(true);
	}

	public Section getSelectedSection() {
		for (Section sec : sectionList) {
			if (sec.isSelected()) {
				return sec;
			}
		}
		return getMainSection();
	}

	public Section getMainSection() {
		for (Section sec : sectionList) {
			if (sec.mainSection) {
				return sec;
			}
		}

		return null;
	}

	private void updatePrices(MenuItemSize itemSize) {
		//		TicketItem ticketItem = modifierSelectionModel.getTicketItem();
		//		List<TicketItemModifierGroup> ticketItemModifierGroups = ticketItem.getTicketItemModifierGroups();
		//		if (ticketItemModifierGroups == null) {
		//			return;
		//		}
		//
		//		Set<MenuModifier> addedModifiers = new HashSet<MenuModifier>();
		//
		//		for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
		//			List<TicketItemModifier> ticketItemModifiers = ticketItemModifierGroup.getTicketItemModifiers();
		//			if (ticketItemModifiers == null) {
		//				continue;
		//			}
		//
		//			for (TicketItemModifier ticketItemModifier : ticketItemModifiers) {
		//				if (ticketItemModifier.getModifierType() == TicketItemModifier.CRUST) {
		//					continue;
		//				}
		//
		//				MenuModifier menuModifier = MenuModifierDAO.getInstance().get(ticketItemModifier.getItemId());
		//				if (menuModifier == null) {
		//					continue;
		//
		//				}
		//
		//				if (addedModifiers.contains(menuModifier)) {
		//					ticketItemModifier.setUnitPrice(0.0);
		//					ticketItemModifier.setInfoOnly(true);
		//					continue;
		//				}
		//
		//				if (ticketItemModifier.getModifierType() == TicketItemModifier.NORMAL_MODIFIER) {
		//					ticketItemModifier.setUnitPrice(menuModifier.getPriceForSize(itemSize, false));
		//				}
		//				else if (ticketItemModifier.getModifierType() == TicketItemModifier.EXTRA_MODIFIER) {
		//					ticketItemModifier.setUnitPrice(menuModifier.getPriceForSize(itemSize, true));
		//				}
		//
		//				addedModifiers.add(menuModifier);
		//			}
		//		}
	}

	public class Pizza extends JPanel {
		int size;

		public Pizza(LayoutManager layoutManager) {
			super(layoutManager);
			setOpaque(false);
			setBackground(Color.white);
			setPreferredSize(PosUIManager.getSize(250, 250));
		}

		public void setSize(int size) {
			this.size = size;
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			int x = 15;
			int width = getWidth() - 30;
			int y = (getHeight() + 40) / 2 - width / 2;
			g.setColor(Color.WHITE);

			Graphics2D g2d = (Graphics2D) g;
			g.setColor(new Color(255, 251, 211));
			Ellipse2D.Double circle = new Ellipse2D.Double(x, y, width, width);
			g2d.fill(circle);
			//			drawCircleByCenter(g, getWidth() / 2, getHeight() / 2, width / 2);
			//
			//			g.setColor(new Color(255, 251, 211));
			//			g.fillOval(2, getHeight() / 2, getWidth() - 4, getHeight() - 4);
			//			g.setColor(Color.LIGHT_GRAY);
			//			g.drawLine(0, getWidth() / 2, getWidth(), height / 2);
			//			g.setColor(Color.lightGray);
			//			g.drawLine(height / 2, 0, getWidth() / 2, height);

			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.green);
			Section selectedSection = getSelectedSection();
			if (selectedSection == null) {
				return;
			}

			//			if (selectedSection.modifierGroup.getNameDisplay().equalsIgnoreCase("Quarter 1")) {
			//				fillQuarter1(g2, x, y, width);
			//			}
			//			else if (selectedSection.modifierGroup.getNameDisplay().equalsIgnoreCase("Quarter 2")) {
			//				fillQuarter2(g2, x, y, width);
			//			}
			//			else if (selectedSection.modifierGroup.getNameDisplay().equalsIgnoreCase("Quarter 3")) {
			//				fillQuarter3(g2, x, y, width);
			//			}
			//			else if (selectedSection.modifierGroup.getNameDisplay().equalsIgnoreCase("Quarter 4")) {
			//				fillQuarter4(g2, x, y, width);
			//			}
			//			else if (selectedSection.modifierGroup.getNameDisplay().equalsIgnoreCase("Half 1")) {
			//				fillHalf1(g2, x, y, width);
			//			}
			//			else if (selectedSection.modifierGroup.getNameDisplay().equalsIgnoreCase("Half 2")) {
			//				fillHalf2(g2, x, y, width);
			//			}
		}

		void drawCircleByCenter(Graphics g, int x, int y, int radius) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(2.0f));
			g2.setColor(Color.lightGray);
			g.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);

		}

		void fillQuarter1(Graphics2D g2, int x, int y, int width) {
			g2.fillArc(x, y, width, width, 90, 90);
		}

		void fillQuarter2(Graphics2D g2, int x, int y, int width) {
			g2.fillArc(x, y, width, width, 360, 90);
		}

		void fillQuarter3(Graphics2D g2, int x, int y, int width) {
			g2.fillArc(x, y, width, width, 180, 90);
		}

		void fillQuarter4(Graphics2D g2, int x, int y, int width) {
			g2.fillArc(x, y, width, width, 270, 90);
		}

		void fillHalf1(Graphics2D g2, int x, int y, int width) {
			g2.fillArc(x, y, width, width, 90, 180);
		}

		void fillHalf2(Graphics2D g2, int x, int y, int width) {
			g2.fillArc(x, y, width, width, 270, 180);
		}
	}

	private TicketItemModifier getSizeAndCrustModifer() {
		//		Section mainSection = getMainSection();
		//		for (int i = 0; i < mainSection.model.getSize(); i++) {
		//			TicketItemModifier ticketItemModifier = mainSection.model.get(i);
		//			if (ticketItemModifier.getModifierType() == TicketItemModifier.CRUST) {
		//				return ticketItemModifier;
		//			}
		//		}
		List<TicketItemModifier> ticketItemModifiers = modifierSelectionModel.getTicketItem().getTicketItemModifiers();
		if (ticketItemModifiers == null) {
			crustModifier = null;
			return crustModifier;
		}
		for (Iterator iterator = ticketItemModifiers.iterator(); iterator.hasNext();) {
			TicketItemModifier ticketItemModifier = (TicketItemModifier) iterator.next();
			if (ticketItemModifier.getModifierType() == TicketItemModifier.CRUST) {
				crustModifier = ticketItemModifier;
			}
		}
		return crustModifier;
	}

	class SizeAndCrustSelectionPane extends JPanel {
		List<PizzaPrice> priceList;

		List<POSToggleButton> sizeButtonList = new ArrayList<POSToggleButton>();
		List<POSToggleButton> crustButtonList = new ArrayList<POSToggleButton>();

		JPanel sizePanel = new JPanel();
		JPanel crustPanel = new JPanel();

		ButtonGroup sizeBtnGroup = new ButtonGroup();
		ButtonGroup crustBtnGroup = new ButtonGroup();

		public SizeAndCrustSelectionPane() {
			priceList = modifierSelectionModel.getMenuItem().getPizzaPriceList();

			setLayout(new BorderLayout());

			sizePanel.setBorder(BorderFactory.createTitledBorder(null, "SIZE", TitledBorder.CENTER, TitledBorder.TOP));

			crustPanel.setBorder(BorderFactory.createTitledBorder(null, "CRUST", TitledBorder.CENTER, TitledBorder.TOP));
			crustPanel.setLayout(new java.awt.FlowLayout());

			Set<MenuItemSize> uniqueSizeList = new HashSet<MenuItemSize>();

			for (PizzaPrice pizzaPrice : priceList) {
				MenuItemSize size = pizzaPrice.getSize();
				if (uniqueSizeList.contains(size)) {
					continue;
				}

				uniqueSizeList.add(size);
				addSizeButton(pizzaPrice, size);
			}

			selectExistingSizeAndCrust();

			add(sizePanel, BorderLayout.WEST);
			add(crustPanel);
		}

		private void selectExistingSizeAndCrust() {
			TicketItemModifier sizeAndCrustModifer = getSizeAndCrustModifer();
			if (sizeAndCrustModifer != null) {
				String sizeAndCrustName = sizeAndCrustModifer.getName();
				String[] split = sizeAndCrustName.split(",");
				String sizeName = split[0];
				String crustName = split[1].replaceAll("\\s?crust", "").trim();

				for (POSToggleButton sizeButton : sizeButtonList) {
					PizzaPrice pizzaPrice = (PizzaPrice) sizeButton.getClientProperty(PROP_PIZZA_PRICE);
					if (pizzaPrice.getSize().getName().equalsIgnoreCase(sizeName)) {
						sizeButton.setSelected(true);
						renderCrusts(pizzaPrice.getSize());
						break;
					}
				}

				for (POSToggleButton crustButton : crustButtonList) {
					PizzaPrice pizzaPrice = (PizzaPrice) crustButton.getClientProperty(PROP_PIZZA_PRICE);
					if (pizzaPrice.getCrust().getName().equalsIgnoreCase(crustName)) {
						crustButton.setSelected(true);
						crustSelected = true;
					}
				}

			}
			else {
				if (!sizeButtonList.isEmpty()) {
					POSToggleButton button = sizeButtonList.get(0);
					PizzaPrice pizzaPrice = (PizzaPrice) button.getClientProperty(PROP_PIZZA_PRICE);
					renderCrusts(pizzaPrice.getSize());

					button.setSelected(true);
				}
			}
		}

		private void addSizeButton(PizzaPrice pizzaPrice, MenuItemSize size) {
			POSToggleButton sizeButton = new POSToggleButton(size.getName());
			sizeButton.putClientProperty(PROP_PIZZA_PRICE, pizzaPrice);
			sizeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					POSToggleButton button = (POSToggleButton) e.getSource();
					PizzaPrice pizzaPrice = (PizzaPrice) button.getClientProperty(PROP_PIZZA_PRICE);
					renderCrusts(pizzaPrice.getSize());

				}
			});
			sizeBtnGroup.add(sizeButton);
			sizeButtonList.add(sizeButton);
			sizePanel.add(sizeButton);
		}

		protected void renderCrusts(MenuItemSize size) {
			for (POSToggleButton component : crustButtonList) {
				crustBtnGroup.remove(component);
			}
			crustPanel.removeAll();

			Set<PizzaPrice> availablePrices = modifierSelectionModel.getMenuItem().getAvailablePrices(size);
			for (PizzaPrice pizzaPrice : availablePrices) {
				POSToggleButton crustButton = new POSToggleButton();
				crustButton.setText("<html><center>" + pizzaPrice.getCrust().getName() + "<br/>" + CurrencyUtil.getCurrencySymbol()
						+ pizzaPrice.getPrice(modifierSelectionModel.getMenuItem().getDefaultSellPortion()) + "</center></html>");
				crustButton.putClientProperty(PROP_PIZZA_PRICE, pizzaPrice);
				if (availablePrices.size() == 1) {
					crustSelected = true;
					crustButton.setSelected(true);
					pizzaCrustSelected(crustButton);
				}
				crustButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						POSToggleButton button = (POSToggleButton) e.getSource();
						pizzaCrustSelected(button);
					}
				});
				crustBtnGroup.add(crustButton);
				crustButtonList.add(crustButton);
				crustPanel.add(crustButton);
			}
			crustPanel.revalidate();
			crustPanel.repaint();
		}
	}

	@Override
	public void modifierRemoved(TicketItemModifier modifier) {
	}

	public class TransparentListCellRenderer extends DefaultListCellRenderer {
		public TransparentListCellRenderer() {
			//setOpaque(false);
		}

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

			JLabel rendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (value instanceof TicketItemModifier) {
				TicketItemModifier ticketItemModifier = (TicketItemModifier) value;
				if (ticketItemModifier.isPrintedToKitchen()) {
					rendererComponent.setFont(rendererComponent.getFont().deriveFont(Font.BOLD));
					rendererComponent.setBackground(Color.YELLOW);
					rendererComponent.setForeground(Color.BLACK);
				}
			}
			return rendererComponent;
		}

		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D graphics2d = (Graphics2D) g;
			AlphaComposite composite = (AlphaComposite) graphics2d.getComposite();
			AlphaComposite composite2 = composite.derive(0.75f);
			graphics2d.setComposite(composite2);
			super.paintComponent(g);
		}
	}

	private void showModifierSelectionMessage(MenuItemModifierGroup menuItemModifierGroup) {
		String displayName = menuItemModifierGroup.getModifierGroup().getDisplayName();
		int minQuantity = menuItemModifierGroup.getMinQuantity();
		POSMessageDialog.showError("You must select at least " + minQuantity + " modifiers from group " + displayName);
	}

	@Override
	public void finishModifierSelection() {

	}

	private void pizzaCrustSelected(POSToggleButton button) {
		PizzaPrice pizzaPrice = (PizzaPrice) button.getClientProperty(PROP_PIZZA_PRICE);

		TicketItem ticketItem = modifierSelectionModel.getTicketItem();

		ticketItem.setUnitPrice(pizzaPrice.getPrice(modifierSelectionModel.getMenuItem().getDefaultSellPortion()));
		crustSelected = true;

		TicketItemModifier modifier = new TicketItemModifier();
		modifier.setName(pizzaPrice.getSize().getName() + ", " + pizzaPrice.getCrust() + " crust");
		modifier.setModifierType(TicketItemModifier.CRUST);
		modifier.setInfoOnly(true);
		modifier.setTicketItem(ticketItem);

		TicketItemModifier sizeAndCrustModifer = getSizeAndCrustModifer();
		if (sizeAndCrustModifer != null) {
			sizeAndCrustModifer.setName(pizzaPrice.getSize().getName() + ", " + pizzaPrice.getCrust() + " crust");
			crustModifier = sizeAndCrustModifer;
		}
		else {
			crustModifier = modifier;
		}
		//		TicketItemModifierGroup modifierGroup = getMainSection().modifierGroup;
		//		List<TicketItemModifier> ticketItemModifiers = modifierGroup.getTicketItemModifiers();
		//		if (ticketItemModifiers != null) {
		//			for (TicketItemModifier ticketItemModifier : ticketItemModifiers) {
		//				if (ticketItemModifier.getModifierType() == TicketItemModifier.CRUST) {
		//					ticketItemModifier.setName(pizzaPrice.getSize().getName() + ", " + pizzaPrice.getCrust() + " crust");
		//					crustFound = true;
		//				}
		//			}
		//			if (!crustFound) {
		//				ticketItemModifiers.add(0, modifier);
		//			}
		//		}
		//		else {
		//			modifierGroup.addToticketItemModifiers(modifier);
		//		}
	}
}
