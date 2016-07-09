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

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuItemSize;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.PizzaPrice;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.dao.MenuModifierDAO;
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
	private Pizza pizza;

	public PizzaModifierSelectionDialog(ModifierSelectionModel modifierSelectionModel) {
		this.modifierSelectionModel = modifierSelectionModel;

		initComponents();
	}

	private void initComponents() {
		setTitle("MODIFY PIZZA");

		setLayout(new java.awt.BorderLayout(10, 10));
		JPanel panel = (JPanel) getContentPane();
		panel.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));

		createSectionPanel();

		sizeAndCrustPanel = new SizeAndCrustSelectionPane();
		add(sizeAndCrustPanel, BorderLayout.NORTH);

		modifierView = new PizzaModifierView(modifierSelectionModel);
		modifierView.addModifierSelectionListener(this);
		PosScrollPane modifierSc = new PosScrollPane(modifierView);
		modifierSc.setBorder(null);
		add(modifierSc);

		createButtonPanel();
	}

	private void createSectionPanel() {
		JPanel westPanel = new JPanel(new BorderLayout(5, 5));

		JPanel sectionView = new JPanel(new MigLayout("fill", "[][][]", ""));
		sectionView.setBorder(BorderFactory.createTitledBorder(null, "SECTIONS", TitledBorder.CENTER, TitledBorder.CENTER));

		sectionList = new ArrayList<>();

		Section sectionWhole = new Section("WHOLE", 0, true);
		sectionWhole.modifierGroup.setShowSectionName(false);

		Section sectionQuarter1 = new Section("Quarter 1", 1, false);
		Section sectionQuarter2 = new Section("Quarter 2", 2, false);
		Section sectionQuarter3 = new Section("Quarter 3", 3, false);
		Section sectionQuarter4 = new Section("Quarter 4", 4, false);
		Section sectionHalf1 = new Section("Half 1", 5, false);
		Section sectionHalf2 = new Section("Half 2", 6, false);

		sectionList.add(sectionWhole);

		sectionList.add(sectionQuarter1);
		sectionList.add(sectionQuarter2);
		sectionList.add(sectionQuarter3);
		sectionList.add(sectionQuarter4);

		sectionList.add(sectionHalf1);
		sectionList.add(sectionHalf2);

		pizza = new Pizza("");

		sectionView.add(sectionQuarter1, "grow,cell 0 0");
		sectionView.add(sectionQuarter3, "grow,cell 0 1");
		sectionView.add(sectionQuarter2, "grow,cell 2 0");
		sectionView.add(sectionQuarter4, "grow,cell 2 1");
		sectionView.add(sectionHalf1, "grow,cell 0 2");
		sectionView.add(sectionHalf2, "grow,cell 2 2");
		sectionView.add(sectionWhole, "grow,cell 1 2");
		sectionView.add(pizza, "gapleft 20,gapright 20,cell 1 0 1 2");

		westPanel.add(sectionView);
		add(westPanel, BorderLayout.WEST);
	}

	public void createButtonPanel() {
		TransparentPanel buttonPanel = new com.floreantpos.swing.TransparentPanel();
		buttonPanel.setLayout(new MigLayout("fill, ins 4", "fill", ""));

		PosButton btnClear = new PosButton("CLEAR");
		btnClear.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Section section = getSelectedSection();
				if (section == null) {
					return;
				}
				section.clearItems();
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

		buttonPanel.add(btnClear);
		buttonPanel.add(btnCancel);
		buttonPanel.add(btnSave);

		add(buttonPanel, java.awt.BorderLayout.SOUTH);
	}

	private boolean doFinishModifierSelection() {
		if (!crustSelected) {
			POSMessageDialog.showError("Please select size and crust.");
			return false;
		}

		TicketItem ticketItem = modifierSelectionModel.getTicketItem();

		if (ticketItem.getTicketItemModifierGroups() != null) {
			ticketItem.getTicketItemModifierGroups().clear();
		}
		for (Section section : sectionList) {
			if (!section.isEmpty()) {
				ticketItem.addToticketItemModifierGroups(section.modifierGroup);
			}
		}

		updatePrices(getSelectedSize());

		return true;
	}

	@Override
	public void modifierSelected(MenuModifier modifier) {
		Section section = getSelectedSection();
		OrderType type = modifierSelectionModel.getTicketItem().getTicket().getOrderType();
		TicketItemModifier itemModifier = convertToTicketItemModifier(modifier, type);
		itemModifier.setTicketItem(modifierSelectionModel.getTicketItem());
		section.addItem(itemModifier);
	}

	@Override
	public void clearModifiers(MenuModifierGroup modifierGroup) {
	}

	@Override
	public void modifierGroupSelectionDone(MenuModifierGroup modifierGroup) {
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

	private TicketItemModifier convertToTicketItemModifier(MenuModifier menuModifier, OrderType type) {
		TicketItemModifier ticketItemModifier = new TicketItemModifier();
		ticketItemModifier.setItemId(menuModifier.getId());
		ticketItemModifier.setGroupId(menuModifier.getModifierGroup().getId());
		ticketItemModifier.setItemCount(1);
		ticketItemModifier.setName(menuModifier.getDisplayName());
		ticketItemModifier.setUnitPrice(menuModifier.getPriceForSize(getSelectedSize(), false));
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

		TicketItemModifierGroup modifierGroup;

		public Section(String sectionName, int sortOrder, boolean main) {
			modifierGroup = getGroupForSection(sectionName);
			if (modifierGroup == null) {
				modifierGroup = new TicketItemModifierGroup();
				modifierGroup.setSectionName(sectionName);
			}

			modifierGroup.setSortOrder(sortOrder);
			modifierGroup.setMainSection(main);
			modifierGroup.setShowSectionName(true);

			setLayout(new BorderLayout());
			JLabel lblTitle = new JLabel(sectionName);
			lblTitle.setBackground(Color.LIGHT_GRAY);
			lblTitle.setHorizontalAlignment(JLabel.CENTER);
			lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 20));
			lblTitle.setOpaque(true);

			add(lblTitle, BorderLayout.NORTH);
			setPreferredSize(PosUIManager.getSize(160, 170));
			setBackground(Color.white);
			setBorder(BorderFactory.createLineBorder(Color.GRAY));

			list = new JList();
			list.setFixedCellHeight(PosUIManager.getSize(35));
			model = new DefaultListModel<TicketItemModifier>();
			list.setModel(model);
			add(new JScrollPane(list), BorderLayout.CENTER);
			addMouseListener(this);
			list.addMouseListener(this);

			List<TicketItemModifier> ticketItemModifiers = modifierGroup.getTicketItemModifiers();
			if (ticketItemModifiers != null) {
				for (TicketItemModifier ticketItemModifier : ticketItemModifiers) {
					if (ticketItemModifier.getModifierType() != TicketItemModifier.CRUST) {
						model.addElement(ticketItemModifier);
					}
				}
			}
		}

		public void clearItems() {
			model.clear();
			List<TicketItemModifier> ticketItemModifiers = modifierGroup.getTicketItemModifiers();
			if (ticketItemModifiers != null) {
				for (Iterator iterator = ticketItemModifiers.iterator(); iterator.hasNext();) {
					TicketItemModifier ticketItemModifier = (TicketItemModifier) iterator.next();
					if (ticketItemModifier.getModifierType() != TicketItemModifier.CRUST) {
						iterator.remove();
					}
				}
			}
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		public boolean isSelected() {
			return selected;
		}

		TicketItemModifierGroup getGroupForSection(String sectionName) {
			TicketItem ticketItem = modifierSelectionModel.getTicketItem();
			List<TicketItemModifierGroup> ticketItemModifierGroups = ticketItem.getTicketItemModifierGroups();
			if (ticketItemModifierGroups == null) {
				return null;
			}

			for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
				if (sectionName.equalsIgnoreCase(ticketItemModifierGroup.getSectionName())) {
					return ticketItemModifierGroup;
				}
			}

			return null;
		}

		public void addItem(TicketItemModifier newModifier) {
			Enumeration<TicketItemModifier> elements = model.elements();
			while (elements.hasMoreElements()) {
				TicketItemModifier existingModifier = elements.nextElement();
				if (existingModifier.getItemId().intValue() == newModifier.getItemId().intValue()) {
					return;
				}
			}
			model.addElement(newModifier);
			modifierGroup.addToticketItemModifiers(newModifier);
		}

		public boolean isEmpty() {
			return modifierGroup.getTicketItemModifiers() != null && modifierGroup.getTicketItemModifiers().isEmpty();
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
	}

	public void setSelectedSection(Section section) {
		if (section.isSelected()) {
			section.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
			section.setSelected(false);
			pizza.repaint();
			return;
		}

		for (Section sec : sectionList) {
			if (sec == section) {
				sec.setBorder(BorderFactory.createLineBorder(Color.blue, 4));
				sec.setSelected(true);
			} else {
				sec.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
				sec.setSelected(false);
			}
		}
		pizza.repaint();
	}

	public Section getSelectedSection() {
		Section mainSection = null;
		for (Section sec : sectionList) {
			if (sec.isSelected()) {
				return sec;
			}
			if (sec.modifierGroup.isMainSection()) {
				mainSection = sec;
			}
		}
		return mainSection;
	}

	public Section getMainSection() {
		for (Section sec : sectionList) {
			if (sec.modifierGroup.isMainSection()) {
				return sec;
			}
		}

		return null;
	}

	private void updatePrices(MenuItemSize itemSize) {
		TicketItem ticketItem = modifierSelectionModel.getTicketItem();
		List<TicketItemModifierGroup> ticketItemModifierGroups = ticketItem.getTicketItemModifierGroups();
		if (ticketItemModifierGroups == null) {
			return;
		}

		Set<MenuModifier> addedModifiers = new HashSet<MenuModifier>();

		for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
			List<TicketItemModifier> ticketItemModifiers = ticketItemModifierGroup.getTicketItemModifiers();
			if (ticketItemModifiers == null) {
				continue;
			}

			for (TicketItemModifier ticketItemModifier : ticketItemModifiers) {
				if (ticketItemModifier.getModifierType() == TicketItemModifier.CRUST) {
					continue;
				}

				MenuModifier menuModifier = MenuModifierDAO.getInstance().get(ticketItemModifier.getItemId());
				if (menuModifier == null) {
					continue;

				}

				if (addedModifiers.contains(menuModifier)) {
					ticketItemModifier.setUnitPrice(0.0);
					ticketItemModifier.setInfoOnly(true);
					continue;
				}

				if (ticketItemModifier.getModifierType() == TicketItemModifier.NORMAL_MODIFIER) {
					ticketItemModifier.setUnitPrice(menuModifier.getPriceForSize(itemSize, false));
				} else if (ticketItemModifier.getModifierType() == TicketItemModifier.EXTRA_MODIFIER) {
					ticketItemModifier.setUnitPrice(menuModifier.getPriceForSize(itemSize, true));
				}

				addedModifiers.add(menuModifier);
			}
		}
	}

	public class Pizza extends JPanel {
		int size;

		public Pizza(String name) {
			setBackground(Color.white);
			setPreferredSize(PosUIManager.getSize(250, 250));
		}

		public void setSize(int size) {
			this.size = size;
		}

		@Override
		public void paint(Graphics g) {
			int width = getWidth();
			int height = getHeight();
			g.setColor(Color.WHITE);
			drawCircleByCenter(g, width / 2, height / 2, width / 2);

			g.setColor(new Color(255, 251, 211));
			g.fillOval(2, 2, getWidth() - 4, getHeight() - 4);
			g.setColor(Color.LIGHT_GRAY);
			g.drawLine(0, getWidth() / 2, getWidth(), getHeight() / 2);
			g.setColor(Color.lightGray);
			g.drawLine(getHeight() / 2, 0, getWidth() / 2, getHeight());

			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.green);
			Section selectedSection = getSelectedSection();
			if (selectedSection == null) {
				return;
			}

			if (selectedSection.modifierGroup.getNameDisplay().equalsIgnoreCase("Quarter 1")) {
				fillQuarter1(g2);
			} else if (selectedSection.modifierGroup.getNameDisplay().equalsIgnoreCase("Quarter 2")) {
				fillQuarter2(g2);
			} else if (selectedSection.modifierGroup.getNameDisplay().equalsIgnoreCase("Quarter 3")) {
				fillQuarter3(g2);
			} else if (selectedSection.modifierGroup.getNameDisplay().equalsIgnoreCase("Quarter 4")) {
				fillQuarter4(g2);
			} else if (selectedSection.modifierGroup.getNameDisplay().equalsIgnoreCase("Half 1")) {
				fillHalf1(g2);
			} else if (selectedSection.modifierGroup.getNameDisplay().equalsIgnoreCase("Half 2")) {
				fillHalf2(g2);
			}
		}

		void drawCircleByCenter(Graphics g, int x, int y, int radius) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(2.0f));
			g2.setColor(Color.lightGray);
			g.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);

		}

		void fillQuarter1(Graphics2D g2) {
			g2.fillArc(4, 4, getWidth() - 16, getHeight() - 16, 90, 90);
		}

		void fillQuarter2(Graphics2D g2) {
			g2.fillArc(10, 4, getWidth() - 16, getHeight() - 16, 360, 90);
		}

		void fillQuarter3(Graphics2D g2) {
			g2.fillArc(10, 10, getWidth() - 16, getHeight() - 16, 270, 90);
		}

		void fillQuarter4(Graphics2D g2) {
			g2.fillArc(4, 10, getWidth() - 16, getHeight() - 16, 180, 90);
		}

		void fillHalf1(Graphics2D g2) {
			g2.fillArc(4, 4, getWidth() - 16, getHeight() - 11, 90,180);
		}

		void fillHalf2(Graphics2D g2) {
			g2.fillArc(10, 4, getWidth() - 16, getHeight() - 11, 270, 180);
		}
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

			} else {
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
				crustButton.setText("<html><center>" + pizzaPrice.getCrust().getName() + "<br/>" + CurrencyUtil.getCurrencySymbol() + pizzaPrice.getPrice() + "</center></html>");
				crustButton.putClientProperty(PROP_PIZZA_PRICE, pizzaPrice);
				crustButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						POSToggleButton button = (POSToggleButton) e.getSource();
						PizzaPrice pizzaPrice = (PizzaPrice) button.getClientProperty(PROP_PIZZA_PRICE);

						TicketItem ticketItem = modifierSelectionModel.getTicketItem();

						ticketItem.setUnitPrice(pizzaPrice.getPrice());
						crustSelected = true;

						TicketItemModifier modifier = new TicketItemModifier();
						modifier.setName(pizzaPrice.getSize().getName() + ", " + pizzaPrice.getCrust() + " crust");
						modifier.setModifierType(TicketItemModifier.CRUST);
						modifier.setInfoOnly(true);
						modifier.setTicketItem(ticketItem);

						boolean crustFound = false;
						TicketItemModifierGroup modifierGroup = getMainSection().modifierGroup;
						List<TicketItemModifier> ticketItemModifiers = modifierGroup.getTicketItemModifiers();
						if (ticketItemModifiers != null) {
							for (TicketItemModifier ticketItemModifier : ticketItemModifiers) {
								if (ticketItemModifier.getModifierType() == TicketItemModifier.CRUST) {
									ticketItemModifier.setName(pizzaPrice.getSize().getName() + ", " + pizzaPrice.getCrust() + " crust");
									crustFound = true;
								}
							}
							if (!crustFound) {
								ticketItemModifiers.add(0, modifier);
							}
						} else {
							modifierGroup.addToticketItemModifiers(modifier);
						}
					}
				});
				crustBtnGroup.add(crustButton);
				crustButtonList.add(crustButton);
				crustPanel.add(crustButton);
			}
			crustPanel.revalidate();
			crustPanel.repaint();
		}

		TicketItemModifier getSizeAndCrustModifer() {
			List<TicketItemModifier> ticketItemModifiers = getMainSection().modifierGroup.getTicketItemModifiers();
			if (ticketItemModifiers != null) {
				for (TicketItemModifier ticketItemModifier : ticketItemModifiers) {
					if (ticketItemModifier.getModifierType() == TicketItemModifier.CRUST) {
						return ticketItemModifier;
					}
				}
			}

			return null;
		}
	}
}
