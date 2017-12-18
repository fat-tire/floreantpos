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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumnModel;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXCollapsiblePane;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.model.ITicketItem;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuItemSize;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.ModifierGroup;
import com.floreantpos.model.Multiplier;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.PizzaCrust;
import com.floreantpos.model.PizzaPrice;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItem.PIZZA_SECTION_MODE;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.swing.ListTableModel;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.NumberSelectionDialog2;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.ui.views.order.modifier.ModifierSelectionListener;
import com.floreantpos.util.CurrencyUtil;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.POSUtil;

/**
 * 
 * @author MShahriar
 */
public class PizzaModifierSelectionDialog extends POSDialog implements ModifierSelectionListener, ActionListener {
	private static final String PROP_PIZZA_PRICE = "pizzaPrice";
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
	private JPanel fullSectionLayout = new TransparentPanel(new GridLayout(1, 1, 2, 2));
	private JPanel halfSectionLayout = new TransparentPanel(new GridLayout(1, 2, 2, 2));
	private JPanel quarterSectionLayout = new TransparentPanel(new GridLayout(2, 2, 2, 2));
	private JTable table;
	private MenuItemSize previousMenuItemSize;
	private MenuItemSize itemSize;
	private TicketItem ticketItem;
	private JPanel wholeSectionView;
	private final MenuItem menuItem;
	private PizzaTicketItemTableModel ticketItemViewerModel;
	private boolean editMode;
	private PosButton btnCustomQuantity;
	private int pizzaQuantity;
	private POSToggleButton btnFull;
	private POSToggleButton btnHalf;
	private JToggleButton btnQuarter;
	private ButtonGroup btnGroup;
	private JToggleButton currentButton;

	public PizzaModifierSelectionDialog(TicketItem cloneTicketItem, MenuItem menuItem, boolean editMode) {
		this.menuItem = menuItem;
		this.ticketItem = cloneTicketItem;
		this.editMode = editMode;
		resetPizzaQuantityAndPrice();
		initComponents();
		updateView();
	}

	private void initComponents() {
		setTitle("MODIFY PIZZA");

		setLayout(new java.awt.BorderLayout(10, 10));
		JPanel panel = (JPanel) getContentPane();
		panel.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));

		JPanel westPanel = new JPanel(new BorderLayout());
		westPanel.add(createSectionPanel(), BorderLayout.CENTER);
		JPanel centerPanel = new JPanel(new BorderLayout());

		JPanel ticketItemTableViewPanel = new JPanel(new BorderLayout());
		ticketItemTableViewPanel.setPreferredSize(PosUIManager.getSize(0, 200));

		table = new JTable();
		ticketItemViewerModel = new PizzaTicketItemTableModel();
		table.setModel(ticketItemViewerModel);
		table.setRowHeight(30);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		final TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(200);
		columnModel.getColumn(1).setPreferredWidth(50);

		JScrollPane scrollPane = new JScrollPane(table);
		ticketItemTableViewPanel.add(scrollPane);

		int size = PosUIManager.getSize(40);

		JXCollapsiblePane pizzaItemActionPanel = new JXCollapsiblePane();
		pizzaItemActionPanel.setBackground(Color.WHITE);
		pizzaItemActionPanel.setLayout(new MigLayout("fillx,ins 2 0 2 0", "[" + size + "px][grow][" + size + "px]", "[" + size + "]"));
		pizzaItemActionPanel.setAnimated(true);
		pizzaItemActionPanel.setCollapsed(false);
		pizzaItemActionPanel.setVisible(true);

		PosButton btnIncreaseQuantity = new PosButton();
		btnIncreaseQuantity.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pizzaQuantity++;
				btnCustomQuantity.setText(String.valueOf(pizzaQuantity));
			}
		});
		btnCustomQuantity = new PosButton(String.valueOf(pizzaQuantity));
		btnCustomQuantity.setForeground(Color.BLUE);
		btnCustomQuantity.setBackground(Color.WHITE);
		Border lineBorder = BorderFactory.createLineBorder(new Color(0, 0, 0, .1f), 1);
		btnCustomQuantity.setBorder(lineBorder);
		btnCustomQuantity.setFont(new Font(btnCustomQuantity.getFont().getName(), Font.BOLD, 20));
		btnCustomQuantity.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				NumberSelectionDialog2 dialog = new NumberSelectionDialog2();
				dialog.setTitle("Enter quantity");
				dialog.setFloatingPoint(false);
				dialog.pack();
				dialog.open();

				if (dialog.isCanceled()) {
					return;
				}
				pizzaQuantity = (int) dialog.getValue();
				btnCustomQuantity.setText(String.valueOf(pizzaQuantity));
			}
		});
		PosButton btnDecreaseQuantity = new PosButton();
		btnDecreaseQuantity.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (pizzaQuantity == 1)
					return;
				pizzaQuantity--;
				btnCustomQuantity.setText(String.valueOf(pizzaQuantity));
			}
		});
		btnIncreaseQuantity.setIcon(IconFactory.getIcon("/ui_icons/", "plus_32.png"));//$NON-NLS-1$ //$NON-NLS-2$
		btnDecreaseQuantity.setIcon(IconFactory.getIcon("/ui_icons/", "minus_32.png"));//$NON-NLS-1$ //$NON-NLS-2$

		pizzaItemActionPanel.add(btnDecreaseQuantity);
		pizzaItemActionPanel.add(btnCustomQuantity, "grow");
		pizzaItemActionPanel.add(btnIncreaseQuantity);

		ticketItemTableViewPanel.add(pizzaItemActionPanel, BorderLayout.SOUTH);

		westPanel.add(ticketItemTableViewPanel, BorderLayout.NORTH);
		sizeAndCrustPanel = new SizeAndCrustSelectionPane();
		centerPanel.add(sizeAndCrustPanel, BorderLayout.NORTH);

		modifierView = new PizzaModifierView(ticketItem, menuItem, this);
		modifierView.addModifierSelectionListener(this);
		PosScrollPane modifierSc = new PosScrollPane(modifierView);
		modifierSc.setBorder(null);
		centerPanel.add(modifierSc, BorderLayout.CENTER);
		add(centerPanel, BorderLayout.CENTER);
		add(westPanel, BorderLayout.WEST);

		createButtonPanel();

	}

	private void updateView() {
		if (ticketItem.getSizeModifier() == null) {
			if (crustModifier != null) {
				ticketItem.setSizeModifier(crustModifier);
				ticketItem.getSizeModifier().calculatePrice();
			}
		}
		else {
			ticketItem.getSizeModifier().calculatePrice();
		}
		ticketItemViewerModel.setTicketItem(ticketItem);
		ticketItemViewerModel.updateView();

		List<TicketItemModifier> ticketItemModifiers = ticketItem.getTicketItemModifiers();
		if (ticketItemModifiers == null) {
			return;
		}
		for (Section section : sectionList) {
			for (Iterator iterator = ticketItemModifiers.iterator(); iterator.hasNext();) {
				TicketItemModifier ticketItemModifier = (TicketItemModifier) iterator.next();
				if (!ticketItemModifier.isInfoOnly() && section.getSectionName().equals(ticketItemModifier.getSectionName())) {
					section.sectionModifierTableModel.addItem(ticketItemModifier);
				}
			}
		}

		if (ticketItem.getPizzaSectionMode() != null) {
			if (ticketItem.getPizzaSectionMode() == PIZZA_SECTION_MODE.FULL) {
				btnFull.setSelected(true);
				currentButton = btnFull;
				doFullSectionMode();
				if (ticketItem.isPrintedToKitchen()) {
					btnHalf.setEnabled(false);
					btnQuarter.setEnabled(false);
				}
			}

			else if (ticketItem.getPizzaSectionMode() == PIZZA_SECTION_MODE.HALF) {
				btnHalf.setSelected(true);
				currentButton = btnHalf;
				doHalfSectionMode();
				if (ticketItem.isPrintedToKitchen()) {
					btnFull.setEnabled(false);
					btnQuarter.setEnabled(false);
				}
			}
			else if (ticketItem.getPizzaSectionMode() == PIZZA_SECTION_MODE.QUARTER) {
				btnQuarter.setSelected(true);
				currentButton = btnQuarter;
				doQuarterSectionMode();
				if (ticketItem.isPrintedToKitchen()) {
					btnHalf.setEnabled(false);
					btnFull.setEnabled(false);
				}
			}
		}

	}

	private JPanel createSectionPanel() {
		JPanel westPanel = new JPanel(new BorderLayout(5, 5));
		sectionList = new ArrayList<>();

		sectionWhole = new Section("WHOLE", "WHOLE", 0, true, 1);
		sectionQuarter1 = new Section("Quarter 1", "Quarter 1", 1, false, 0.25);
		sectionQuarter2 = new Section("Quarter 2", "Quarter 2", 2, false, 0.25);
		sectionQuarter3 = new Section("Quarter 3", "Quarter 3", 3, false, 0.25);
		sectionQuarter4 = new Section("Quarter 4", "Quarter 4", 4, false, 0.25);
		sectionHalf1 = new Section("Half 1", "Half 1", 5, false, 0.5);
		sectionHalf2 = new Section("Half 2", "Half 2", 6, false, 0.5);

		sectionList.add(sectionWhole);

		sectionList.add(sectionQuarter1);
		sectionList.add(sectionQuarter2);
		sectionList.add(sectionQuarter3);
		sectionList.add(sectionQuarter4);

		sectionList.add(sectionHalf1);
		sectionList.add(sectionHalf2);

		fullSectionLayout.add(sectionWhole);

		halfSectionLayout.add(sectionHalf1);
		halfSectionLayout.add(sectionHalf2);

		quarterSectionLayout.add(sectionQuarter1);
		quarterSectionLayout.add(sectionQuarter2);
		quarterSectionLayout.add(sectionQuarter3);
		quarterSectionLayout.add(sectionQuarter4);

		sectionView.add(fullSectionLayout, "full");
		sectionView.add(halfSectionLayout, "half");
		sectionView.add(quarterSectionLayout, "quarter");
		sectionLayout.show(sectionView, "full");

		wholeSectionView = new JPanel(new MigLayout("fill,ins 0 0 0 0"));
		sectionView.setOpaque(false);
		westPanel.setOpaque(false);

		westPanel.add(sectionView, BorderLayout.CENTER);
		westPanel.add(wholeSectionView, BorderLayout.SOUTH);

		return westPanel;
	}

	public void createButtonPanel() {
		TransparentPanel buttonPanel = new com.floreantpos.swing.TransparentPanel();
		buttonPanel.setLayout(new MigLayout("fill, ins 2", "", ""));

		btnGroup = new ButtonGroup();

		btnFull = new POSToggleButton("FULL");
		btnFull.setSelected(true);
		currentButton = btnFull;
		btnFull.addActionListener(this);
		btnHalf = new POSToggleButton("HALF");
		btnHalf.addActionListener(this);

		btnQuarter = new POSToggleButton("QUARTER");
		btnQuarter.addActionListener(this);

		btnGroup.add(btnFull);
		btnGroup.add(btnHalf);
		btnGroup.add(btnQuarter);

		PosButton btnClear = new PosButton("CLEAR");
		btnClear.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Section section = getSelectedSection();
				if (section == null) {
					return;
				}
				section.clearSelectedItem();
			}
		});

		PosButton btnClearAll = new PosButton("CLEAR ALL");
		btnClearAll.addActionListener(new java.awt.event.ActionListener() {
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
		int width = PosUIManager.getSize(80);
		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		buttonPanel.add(btnFull, "w " + width + "!, split 5");
		buttonPanel.add(btnHalf, "w " + width + "!");
		buttonPanel.add(btnQuarter, "w " + width + "!");
		buttonPanel.add(separator, "growy");
		buttonPanel.add(btnClear, "grow");
		buttonPanel.add(btnClearAll, "grow");
		buttonPanel.add(btnCancel, "grow");
		buttonPanel.add(btnSave, "grow");

		add(buttonPanel, java.awt.BorderLayout.SOUTH);
	}

	private boolean doFinishModifierSelection() {
		if (!crustSelected) {
			POSMessageDialog.showError("Please select size and crust.");
			return false;
		}
		List<MenuItemModifierGroup> menuItemModiferGroups = ticketItem.getMenuItem().getMenuItemModiferGroups();
		for (Iterator iterator = menuItemModiferGroups.iterator(); iterator.hasNext();) {
			MenuItemModifierGroup menuItemModifierGroup = (MenuItemModifierGroup) iterator.next();
			if (!ticketItem.requiredModifiersAdded(menuItemModifierGroup)) {
				POSMessageDialog.showMessage(POSUtil.getFocusedWindow(),
						String.format("Required modifiers for group %s not added!", menuItemModifierGroup.getModifierGroup().getDisplayName()));
				return false;
			}
		}

		updatePizzaQuantityAndPrice();

		if (!editMode) {
			OrderView.getInstance().getTicketView().addTicketItem(ticketItem);

			int showYesNoQuestionDialog = POSMessageDialog.showYesNoQuestionDialog(POSUtil.getFocusedWindow(), "Do you want to create more pizza?",
					"More Pizza");
			if (showYesNoQuestionDialog == 0) {
				TicketItem newTicketItem = ticketItem.clone(ticketItem);
				newTicketItem.setId(null);
				this.ticketItem = newTicketItem;
				reset();
				return false;
			}
		}
		return true;
	}

	private void updatePizzaQuantityAndPrice() {
		ticketItem.setItemCount(pizzaQuantity);

		List<TicketItemModifier> ticketItemModifiers = ticketItem.getTicketItemModifiers();
		if (ticketItemModifiers != null)
			for (TicketItemModifier ticketItemModifier : ticketItemModifiers) {
				if (ticketItemModifier.isInfoOnly())
					continue;
				ticketItemModifier.setItemCount(ticketItemModifier.getItemCount() * pizzaQuantity);
			}
		ticketItem.calculatePrice();
	}

	private void resetPizzaQuantityAndPrice() {
		pizzaQuantity = ticketItem.getItemCount();
		ticketItem.setItemCount(1);
		List<TicketItemModifier> ticketItemModifiers = ticketItem.getTicketItemModifiers();
		if (ticketItemModifiers != null)
			for (TicketItemModifier ticketItemModifier : ticketItemModifiers) {
				if (ticketItemModifier.isInfoOnly())
					continue;
				ticketItemModifier.setItemCount(ticketItemModifier.getItemCount() / pizzaQuantity);
			}
		ticketItem.calculatePrice();
	}

	private void reset() {
		for (Iterator iterator = sectionList.iterator(); iterator.hasNext();) {
			Section section = (Section) iterator.next();
			if (section.sectionModifierTableModel.getRows() == null) {
				continue;
			}
			for (Iterator iterator2 = section.sectionModifierTableModel.getRows().iterator(); iterator2.hasNext();) {
				TicketItemModifier ticketItemModifier = (TicketItemModifier) iterator2.next();
				if (ticketItemModifier != null) {
					iterator2.remove();
				}
			}
			section.sectionModifierTableModel.fireTableDataChanged();
			section.repaint();
		}
		modifierView.getModifierGroupView().selectFirst();
		ticketItem.setSizeModifier(getSizeAndCrustModifer());
		ticketItem.getSizeModifier().calculatePrice();
		if (ticketItem.getTicketItemModifiers() != null) {
			ticketItem.getTicketItemModifiers().clear();
		}
		ticketItemViewerModel.setTicketItem(ticketItem);
		ticketItemViewerModel.updateView();
	}

	private boolean isMaxModifierAddedFromGroup(MenuItemModifierGroup menuItemModifierGroup, int currentModifierCount) {
		int minQuantity = menuItemModifierGroup.getMinQuantity();
		int maxQuantity = menuItemModifierGroup.getMaxQuantity();

		if (maxQuantity < minQuantity) {
			maxQuantity = minQuantity;
		}

		if (currentModifierCount >= maxQuantity) {
			return true;
		}
		return false;
	}

	@Override
	public void modifierSelected(MenuModifier modifier, Multiplier multiplier) {

		MenuItemModifierGroup menuItemModifierGroup = modifier.getMenuItemModifierGroup();
		int countModifier = ticketItem.countModifierFromGroup(menuItemModifierGroup);
		if (isMaxModifierAddedFromGroup(menuItemModifierGroup, countModifier)) {
			POSMessageDialog.showError("You have added maximum number of allowed modifiers from group " + modifier.getModifierGroup().getDisplayName());
			modifierGroupSelectionDone(menuItemModifierGroup.getModifierGroup());
			return;
		}

		Section selectedSection = getSelectedSection();
		boolean itemSizeSame = false;
		itemSize = sizeAndCrustPanel.getMenuItemSize();
		if (itemSize != null) {
			if (previousMenuItemSize != null) {
				if (previousMenuItemSize == itemSize) {
					itemSizeSame = true;
				}
			}
			else {
				previousMenuItemSize = itemSize;
				itemSizeSame = true;
			}
		}

		TicketItemModifier findTicketItemModifierForWholeSection = ticketItem.findTicketItemModifierFor(modifier, getMainSection().getSectionName(), null);
		TicketItemModifier ticketItemModifier = ticketItem.findTicketItemModifierFor(modifier, selectedSection.getSectionName(), null);

		if (!selectedSection.mainSection) {
			int countSection = 0;
			for (Section sec : sectionList) {

				if (sec.getSectionName().startsWith("Half")) {
					if (sec == selectedSection) {
						continue;
					}
					TicketItemModifier ticItemModifier = ticketItem.findTicketItemModifierFor(modifier, sec.getSectionName(), null);
					if (ticItemModifier != null && ticketItemModifier == null) {
						if (findTicketItemModifierForWholeSection != null) {
							POSMessageDialog.showMessage(POSUtil.getFocusedWindow(), "Item already added in WHOLE section!");
							return;
						}
						int questionDialog = POSMessageDialog.showYesNoQuestionDialog(POSUtil.getFocusedWindow(),
								"Would you like to add this item in WHOLE section?", "Add Modifier");
						if (questionDialog != JOptionPane.YES_OPTION) {
							break;
						}
						sec.clearItem(ticItemModifier, sec.sectionModifierTableModel);
						selectedSection = getMainSection();
					}
				}
				else if (sec.getSectionName().startsWith("Quarter")) {
					if (sec == selectedSection) {
						continue;
					}
					TicketItemModifier ticItemModifier = ticketItem.findTicketItemModifierFor(modifier, sec.getSectionName(), null);
					if (ticItemModifier != null && ticketItemModifier == null) {

						if (countSection == 2) {
							if (findTicketItemModifierForWholeSection != null) {
								POSMessageDialog.showMessage(POSUtil.getFocusedWindow(), "Item already added in WHOLE section!");
								return;
							}
							int questionDialog = POSMessageDialog.showYesNoQuestionDialog(POSUtil.getFocusedWindow(),
									"Would you like to add this item in WHOLE section?", "Add Modifier");
							if (questionDialog == 0) {
								for (Section sectionClear : sectionList) {
									if (sectionClear.getSectionName().startsWith("Quarter")) {
										sectionClear.clearItem(ticItemModifier, sectionClear.sectionModifierTableModel);
									}
									sectionClear.repaint();
								}

								selectedSection = getMainSection();
							}
						}
						++countSection;
					}

				}

			}
		}
		//////----------------------------------------
		if (findTicketItemModifierForWholeSection != null && !selectedSection.mainSection && ticketItemModifier == null) {
			int questionDialog = POSMessageDialog.showYesNoQuestionDialog(POSUtil.getFocusedWindow(),
					"Item already added in pizza, Would you like to add again?", "Add Modifier");
			if (questionDialog == 1) {
				return;
			}
		}

		if (ticketItemModifier == null || ticketItemModifier.isPrintedToKitchen() || !itemSizeSame) {
			OrderType orderType = ticketItem.getTicket().getOrderType();
			ticketItemModifier = convertToTicketItemModifier(ticketItem, modifier, orderType, multiplier);

			ticketItemModifier.setSectionName(selectedSection.getSectionName());

			if (ticketItemModifier.isShouldSectionWisePrice()) {
				ticketItemModifier.setUnitPrice(selectedSection.calculatePrice(ticketItemModifier.getUnitPrice()));
			}
			selectedSection.addItem(ticketItemModifier);
			TicketItemModifier separator = getSeparatorIfNeeded(selectedSection.getSectionName());
			if (separator != null) {
				ticketItem.addToticketItemModifiers(separator);
			}

			ticketItem.addToticketItemModifiers(ticketItemModifier);
			if (!ticketItemModifier.isInfoOnly()) {
				double defaultSellPortion = menuItem.getDefaultSellPortion();
				ticketItem.updateModifiersUnitPrice(defaultSellPortion);
			}
			//ticketItemModifierView.getTicketItemViewerModel().addItem(ticketItemModifier);
		}
		else {
			//ticketItemModifier.setItemCount(ticketItemModifier.getItemCount() + 1);
			POSMessageDialog.showMessage(POSUtil.getFocusedWindow(), "Item already added!");
			return;
			//section.repaint();
			//ticketItemModifierView.getTicketItemViewerModel().updateModifier(ticketItemModifier);
		}
		if (countModifier + 1 >= menuItemModifierGroup.getMinQuantity()) {
			modifierGroupSelectionDone(menuItemModifierGroup.getModifierGroup());
		}
		//List rows = ticketItemViewerModel.getRows();
		//ticketItemViewerModel.setRows(rows);
		ticketItemViewerModel.updateView();
		//ticketItemModifierView.repaint();
	}

	private TicketItemModifier getSeparatorIfNeeded(String sectionName) {
		List<TicketItemModifier> ticketItemModifiers = ticketItem.getTicketItemModifiers();
		if (ticketItemViewerModel.getRowCount() == 1 && sectionName.equals("WHOLE")) {
			return null;
		}
		if (ticketItemModifiers != null && !ticketItemModifiers.isEmpty()) {
			TicketItemModifier lastItem = ticketItemModifiers.get(ticketItemModifiers.size() - 1);
			if (sectionName.equals(lastItem.getSectionName())) {
				return null;
			}
		}
		TicketItemModifier ticketItemModifier = new TicketItemModifier();
		ticketItemModifier.setName("== " + sectionName + " ==");
		ticketItemModifier.setModifierType(TicketItemModifier.SEPERATOR);
		ticketItemModifier.setInfoOnly(true);
		ticketItemModifier.setSectionName(sectionName);
		ticketItemModifier.setTicketItem(ticketItem);
		return ticketItemModifier;
	}

	@Override
	public void clearModifiers(ModifierGroup modifierGroup) {
	}

	@Override
	public void modifierGroupSelectionDone(ModifierGroup modifierGroup) {
		MenuItemModifierGroup menuItemModifierGroup = modifierGroup.getMenuItemModifierGroup();
		if (!isRequiredModifiersAdded(ticketItem, menuItemModifierGroup)) {
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

	public static boolean isRequiredModifiersAdded(TicketItem ticketItem, MenuItemModifierGroup menuItemModifierGroup) {
		return true;
	}

	private TicketItemModifier convertToTicketItemModifier(TicketItem ticketItem, MenuModifier menuModifier, OrderType type, Multiplier multiplier) {
		TicketItemModifier ticketItemModifier = new TicketItemModifier();
		ticketItemModifier.setModifierId(menuModifier.getId());
		MenuItemModifierGroup menuItemModifierGroup = menuModifier.getMenuItemModifierGroup();
		if (menuItemModifierGroup != null) {
			ticketItemModifier.setMenuItemModifierGroupId(menuItemModifierGroup.getId());
		}
		ticketItemModifier.setItemCount(1);
		ticketItemModifier.setName(menuModifier.getDisplayName().trim());
		ticketItemModifier.setTicketItem(ticketItem);
		double priceForSize = menuModifier.getPriceForSizeAndMultiplier(getSelectedSize(), false, multiplier);
		if (multiplier != null) {
			ticketItemModifier.setMultiplierName(multiplier.getName());
			ticketItemModifier.setName(multiplier.getTicketPrefix() + " " + menuModifier.getDisplayName());
		}
		ticketItemModifier.setUnitPrice(priceForSize);
		ticketItemModifier.setTaxRate(menuModifier.getTaxByOrderType(type));
		ticketItemModifier.setModifierType(TicketItemModifier.NORMAL_MODIFIER);
		ticketItemModifier.setShouldPrintToKitchen(menuModifier.isShouldPrintToKitchen());
		ticketItemModifier.setShouldSectionWisePrice(menuModifier.isShouldSectionWisePrice());

		return ticketItemModifier;
	}

	public MenuItemSize getSelectedSize() {
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
		//JList<TicketItemModifier> list;
		//ListBasedListModel<TicketItemModifier> model;
		boolean selected;
		private boolean mainSection;
		private JLabel lblTitle;
		private final String sectionName;
		private final int sortOrder;
		private final String displayTitle;
		private final double price;
		private JTable sectionTable;
		private SectionModifierTableModel sectionModifierTableModel;

		public Section(String sectionName, String displayTitle, int sortOrder, boolean main, double price) {
			this.sectionName = sectionName;
			this.displayTitle = displayTitle;
			this.sortOrder = sortOrder;
			this.mainSection = main;
			this.price = price;

			setLayout(new BorderLayout());
			lblTitle = new JLabel(sectionName);
			lblTitle.setBackground(Color.LIGHT_GRAY);
			lblTitle.setHorizontalAlignment(JLabel.CENTER);
			lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 14));
			lblTitle.setOpaque(true);

			add(lblTitle, BorderLayout.NORTH);
			setOpaque(false);
			setPreferredSize(PosUIManager.getSize(160, 170));
			//setBackground(Color.white);

			setBorder(BorderFactory.createLineBorder(Color.GRAY));

			//			list = new JList();
			//			list.setCellRenderer(new TransparentListCellRenderer());
			//			list.setOpaque(false);
			//			list.setFixedCellHeight(PosUIManager.getSize(35));
			//			//list.setFixedCellWidth(300);
			//			model = new ListBasedListModel<TicketItemModifier>();
			//			list.setModel(model);

			sectionTable = new JTable();
			sectionTable.setTableHeader(null);
			sectionTable.setRowHeight(PosUIManager.getSize(30));
			sectionModifierTableModel = new SectionModifierTableModel();
			sectionTable.setDefaultRenderer(Object.class, new ModifierTableCellRenderer());
			sectionTable.setModel(sectionModifierTableModel);
			JScrollPane scrollPane = new JScrollPane(sectionTable);
			scrollPane.setBorder(null);
			JViewport viewPort = scrollPane.getViewport();
			viewPort.setOpaque(false);
			scrollPane.setOpaque(false);
			scrollPane.setBorder(null);
			add(scrollPane, BorderLayout.CENTER);
			addMouseListener(this);
			viewPort.addMouseListener(this);
			sectionTable.addMouseListener(this);
			resizeColumnWidth(sectionTable);
		}

		public void resizeColumnWidth(JTable table) {
			final TableColumnModel columnModel = table.getColumnModel();
			for (int column = 0; column < table.getColumnCount(); column++) {
				columnModel.getColumn(column).setPreferredWidth((Integer) getColumnWidth().get(column));
			}
		}

		private List getColumnWidth() {
			List<Integer> columnWidth = new ArrayList();
			columnWidth.add(70);
			columnWidth.add(30);
			return columnWidth;
		}

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D graphics2d = (Graphics2D) g;
			AlphaComposite composite = (AlphaComposite) graphics2d.getComposite();
			AlphaComposite composite2 = composite.derive(0.75f);
			graphics2d.setComposite(composite2);
			super.paintComponent(g);
		}

		public boolean isEmpty() {
			return sectionModifierTableModel.getRows().size() <= 0;
		}

		public void clearItems() {
			boolean isPrintedModifierExist = false;
			for (Iterator iterator = ticketItem.getTicketItemModifiers().iterator(); iterator.hasNext();) {
				TicketItemModifier ticketItemModifier = (TicketItemModifier) iterator.next();
				if (!ticketItemModifier.isPrintedToKitchen()) {
					if (ticketItemModifier.getSectionName().equals(getSectionName())) {
						iterator.remove();
						ticketItem.deleteTicketItemModifier(ticketItemModifier);
						sectionModifierTableModel.deleteGivenItem(ticketItemModifier);
					}
				}
				else {
					isPrintedModifierExist = true;
				}
			}

			ticketItemViewerModel.updateView();
			sectionTable.repaint();
			repaint();
			if (isPrintedModifierExist) {
				POSMessageDialog.showMessage(POSUtil.getFocusedWindow(), "Modifiers that sent to kitchen can not be deleted!");
			}
			previousMenuItemSize = null;
		}

		public void clearSelectedItem() {
			boolean isPrintedModifierExist = false;
			if (sectionModifierTableModel.getRows() == null) {
				return;
			}
			if (sectionModifierTableModel.getRows().size() == 0) {
				return;
			}
			int index = sectionTable.getSelectedRow();
			if (index < 0) {
				return;
			}
			TicketItemModifier selectedModifier = sectionModifierTableModel.getRowData(index);
			if (selectedModifier == null) {
				return;
			}
			if (!selectedModifier.isPrintedToKitchen()) {
				ticketItem.deleteTicketItemModifier(selectedModifier);
				sectionModifierTableModel.deleteGivenItem(selectedModifier);
				if (sectionModifierTableModel.getRows().size() == 0) {
					clearItems();
				}
			}
			else {
				isPrintedModifierExist = true;
			}

			ticketItemViewerModel.updateView();
			repaint();
			if (isPrintedModifierExist) {
				POSMessageDialog.showMessage(POSUtil.getFocusedWindow(), "Modifiers that sent to kitchen can not be deleted!");
			}
			previousMenuItemSize = null;
		}

		public void clearItem(TicketItemModifier ticketItemModifier, SectionModifierTableModel sectionModifierTableModel) {
			boolean isPrintedModifierExist = false;
			if (sectionModifierTableModel.getRows() == null) {
				return;
			}
			if (sectionModifierTableModel.getRows().size() == 0) {
				return;
			}
			if (!ticketItemModifier.isPrintedToKitchen()) {
				ticketItem.deleteTicketItemModifierByName(ticketItemModifier);
				sectionModifierTableModel.deleteGivenItemByName(ticketItemModifier);
				if (sectionModifierTableModel.getRows().size() == 0) {
					clearItems();
				}
			}
			else {
				isPrintedModifierExist = true;
			}

			ticketItemViewerModel.updateView();
			repaint();
			if (isPrintedModifierExist) {
				POSMessageDialog.showMessage(POSUtil.getFocusedWindow(), "Modifiers that sent to kitchen can not be deleted!");
			}
			previousMenuItemSize = null;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
			repaint();
		}

		public boolean isSelected() {
			return selected;
		}

		public void addItem(TicketItemModifier newModifier) {
			//model.addElement(newModifier);
			sectionModifierTableModel.addItem(newModifier);
			repaint();
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

		public double getPrice() {
			return price;
		}

		public double calculatePrice(double modifierPrice) {
			return modifierPrice * getPrice();
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
		section.lblTitle.setBackground(Color.green);
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

			if (selectedSection.getSectionName().equalsIgnoreCase("Quarter 1")) {
				fillQuarter1(g2, x, y, width);
			}
			else if (selectedSection.getSectionName().equalsIgnoreCase("Quarter 2")) {
				fillQuarter2(g2, x, y, width);
			}
			else if (selectedSection.getSectionName().equalsIgnoreCase("Quarter 3")) {
				fillQuarter3(g2, x, y, width);
			}
			else if (selectedSection.getSectionName().equalsIgnoreCase("Quarter 4")) {
				fillQuarter4(g2, x, y, width);
			}
			else if (selectedSection.getSectionName().equalsIgnoreCase("Half 1")) {
				fillHalf1(g2, x, y, width);
			}
			else if (selectedSection.getSectionName().equalsIgnoreCase("Half 2")) {
				fillHalf2(g2, x, y, width);
			}
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

		if (ticketItem != null) {
			if (ticketItem.getSizeModifier() != null)
				return crustModifier = ticketItem.getSizeModifier();
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

		MenuItemSize menuItemSize;
		PizzaCrust pizzaCrust;

		public SizeAndCrustSelectionPane() {
			priceList = menuItem.getPizzaPriceList();

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
				String[] split = sizeAndCrustName.split(" ");
				String sizeName = split[0];
				String crustName = split[1];//.replaceAll("\\s", "").trim();

				for (POSToggleButton sizeButton : sizeButtonList) {
					PizzaPrice pizzaPrice = (PizzaPrice) sizeButton.getClientProperty(PROP_PIZZA_PRICE);
					if (pizzaPrice.getSize().getName().equalsIgnoreCase(sizeName)) {
						sizeButton.setSelected(true);
						renderCrusts(pizzaPrice.getSize());
						continue;
					}
					else {
						if (ticketItem.isPrintedToKitchen()) {
							sizeButton.setEnabled(false);
						}
					}
				}

				for (POSToggleButton crustButton : crustButtonList) {
					PizzaPrice pizzaPrice = (PizzaPrice) crustButton.getClientProperty(PROP_PIZZA_PRICE);
					if (pizzaPrice.getCrust().getName().startsWith(crustName)) {
						crustButton.setSelected(true);
						crustSelected = true;
					}
					else {
						if (ticketItem.isPrintedToKitchen()) {
							crustButton.setEnabled(false);
						}
					}
				}

			}
			else {
				if (!sizeButtonList.isEmpty()) {
					List<Boolean> isBtnSelected = new ArrayList<Boolean>();
					for (Iterator iterator = sizeButtonList.iterator(); iterator.hasNext();) {
						POSToggleButton button = (POSToggleButton) iterator.next();
						if (button.isSelected()) {
							PizzaPrice pizzaPrice = (PizzaPrice) button.getClientProperty(PROP_PIZZA_PRICE);
							renderCrusts(pizzaPrice.getSize());
							isBtnSelected.add(true);
						}

					}
					if (isBtnSelected.isEmpty() || isBtnSelected.contains(false)) {
						POSToggleButton button = sizeButtonList.get(0);
						PizzaPrice pizzaPrice = (PizzaPrice) button.getClientProperty(PROP_PIZZA_PRICE);
						renderCrusts(pizzaPrice.getSize());

						button.setSelected(true);
					}
				}
			}
		}

		private void addSizeButton(PizzaPrice pizzaPrice, MenuItemSize size) {
			POSToggleButton sizeButton = new POSToggleButton(size.getName());
			sizeButton.putClientProperty(PROP_PIZZA_PRICE, pizzaPrice);
			if (size.isDefaultSize()) {
				sizeButton.setSelected(true);
			}
			sizeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					POSToggleButton button = (POSToggleButton) e.getSource();
					PizzaPrice pizzaPrice = (PizzaPrice) button.getClientProperty(PROP_PIZZA_PRICE);
					renderCrusts(pizzaPrice.getSize());

					if (getSizeAndCrustModifer() != null) {
						ticketItem.setSizeModifier(getSizeAndCrustModifer());
						ticketItem.getSizeModifier().calculatePrice();
						//ticketItemModifierView.getTicketItemViewerModel().getRows();
						ticketItemViewerModel.updateView();
						//ticketItemModifierView.repaint();
					}
					modifierView.updateView();
				}
			});
			sizeBtnGroup.add(sizeButton);
			sizeButtonList.add(sizeButton);
			sizePanel.add(sizeButton);
		}

		protected void renderCrusts(MenuItemSize size) {
			setMenuItemSize(size);
			for (POSToggleButton component : crustButtonList) {
				crustBtnGroup.remove(component);
			}
			crustPanel.removeAll();

			Set<PizzaPrice> availablePrices = menuItem.getAvailablePrices(size);
			TicketItemModifier sizeAndCrustModifer = getSizeAndCrustModifer();
			for (final PizzaPrice pizzaPrice : availablePrices) {
				POSToggleButton crustButton = new POSToggleButton();
				crustButton.setText("<html><center>" + pizzaPrice.getCrust().getName() + "<br/>" + CurrencyUtil.getCurrencySymbol()
						+ pizzaPrice.getPrice(menuItem.getDefaultSellPortion()) + "</center></html>");
				crustButton.putClientProperty(PROP_PIZZA_PRICE, pizzaPrice);
				if (availablePrices.size() == 1) {
					crustSelected = true;
					crustButton.setSelected(true);
					pizzaCrustSelected(crustButton);
					setPizzaCrust(pizzaPrice.getCrust());
				}
				if (pizzaPrice.getCrust().isDefaultCrust() && sizeAndCrustModifer == null) {
					crustSelected = true;
					crustButton.setSelected(true);
					pizzaCrustSelected(crustButton);
					setPizzaCrust(pizzaPrice.getCrust());
				}
				if (sizeAndCrustModifer != null) {
					String sizeAndCrustName = sizeAndCrustModifer.getName();
					String[] split = sizeAndCrustName.split(" ");
					String crustName = split[1];
					if (pizzaPrice.getCrust().getName().startsWith(crustName)) {
						crustSelected = true;
						crustButton.setSelected(true);
						pizzaCrustSelected(crustButton);
						setPizzaCrust(pizzaPrice.getCrust());
					}
				}
				crustButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						POSToggleButton button = (POSToggleButton) e.getSource();
						pizzaCrustSelected(button);
						setPizzaCrust(pizzaPrice.getCrust());

						if (getSizeAndCrustModifer() != null) {
							ticketItem.setSizeModifier(getSizeAndCrustModifer());
							ticketItem.getSizeModifier().calculatePrice();
							//ticketItemModifierView.getTicketItemViewerModel().getRows();
							ticketItemViewerModel.updateView();
							//ticketItemModifierView.repaint();
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

		public MenuItemSize getMenuItemSize() {
			return menuItemSize;
		}

		public void setMenuItemSize(MenuItemSize menuItemSize) {
			this.menuItemSize = menuItemSize;
		}

		public PizzaCrust getPizzaCrust() {
			return pizzaCrust;
		}

		public void setPizzaCrust(PizzaCrust pizzaCrust) {
			this.pizzaCrust = pizzaCrust;
		}
	}

	@Override
	public void modifierRemoved(TicketItemModifier modifier) {
	}

	public class ModifierTableCellRenderer extends PosTableRenderer {

		private boolean inTicketScreen = false;

		//MultiLineTableCellRenderer multiLineTableCellRenderer = new MultiLineTableCellRenderer();

		public ModifierTableCellRenderer() {
			super();
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component rendererComponent = null;

			SectionModifierTableModel model = (SectionModifierTableModel) table.getModel();
			Object object = model.getRowData(row);
			if (column == 1) {
				rendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
			else {
				rendererComponent = super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
				if (column == 0) {
					setHorizontalAlignment(SwingConstants.CENTER);
				}
				else
					setHorizontalAlignment(SwingConstants.RIGHT);
			}

			if (isSelected) {
				rendererComponent.setBackground(Color.BLUE);
				rendererComponent.setForeground(Color.WHITE);
				return rendererComponent;
			}

			rendererComponent.setBackground(table.getBackground());
			rendererComponent.setForeground(Color.BLACK);

			if (object instanceof TicketItemModifier) {
				ITicketItem ticketItem = (ITicketItem) object;
				if (ticketItem.isPrintedToKitchen()) {
					rendererComponent.setBackground(Color.YELLOW);
					rendererComponent.setForeground(Color.BLACK);
				}
			}

			return rendererComponent;
		}

	}

	//	public class TransparentListCellRenderer extends DefaultListCellRenderer {
	//		public TransparentListCellRenderer() {
	//			//setOpaque(false);
	//		}
	//
	//		@Override
	//		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	//
	//			JLabel rendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	//			if (value instanceof TicketItemModifier) {
	//				TicketItemModifier ticketItemModifier = (TicketItemModifier) value;
	//				if (ticketItemModifier.isPrintedToKitchen()) {
	//					rendererComponent.setFont(rendererComponent.getFont().deriveFont(Font.BOLD));
	//					rendererComponent.setBackground(Color.YELLOW);
	//					rendererComponent.setForeground(Color.BLACK);
	//				}
	//				//								String text = "<html><body style= \"width:100%;\"><table width=\"100%\" border=\"0\" style=\" table-layout : fixed;\"><tr><td align=\"left\" style= \"width:100%;\">"
	//				//										+ ticketItemModifier.getName()
	//				//										+ "</td><td align=\"right\">"
	//				//										+ ticketItemModifier.getItemCount()
	//				//										* ticketItemModifier.getUnitPrice()
	//				//										+ "</td></tr></table></body></html>";
	//
	//				//				String text = "<html><body>" + ticketItemModifier.getName() + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
	//				//						+ +ticketItemModifier.getUnitPrice() + " </body></html>";
	//				//				rendererComponent.setText(text);
	//				/*	<p style="text-align:left;">
	//					ticketItemModifier.getName()
	//					<span style="float:right;">ticketItemModifier.getUnitPrice()</span>
	//					</p>*/
	//
	//				String text = "<html><div style=\"width:100%; border:1;\"><p style=\" text-align:center; \">" + ticketItemModifier.getName() + "<span> "
	//						+ ticketItemModifier.getUnitPrice() + "</span></p></div></html>";
	//
	//				//				String text =ticketItemModifier.getName()+" "+ticketItemModifier.getUnitPrice();
	//				rendererComponent.setText(text);
	//			}
	//
	//			return rendererComponent;
	//		}
	//
	//		@Override
	//		protected void paintComponent(Graphics g) {
	//			Graphics2D graphics2d = (Graphics2D) g;
	//			AlphaComposite composite = (AlphaComposite) graphics2d.getComposite();
	//			AlphaComposite composite2 = composite.derive(0.75f);
	//			graphics2d.setComposite(composite2);
	//			super.paintComponent(g);
	//		}
	//	}

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

		ticketItem.setUnitPrice(pizzaPrice.getPrice(menuItem.getDefaultSellPortion()));

		TicketItemModifier sizeAndCrustModifer = getSizeAndCrustModifer();
		if (sizeAndCrustModifer != null) {
			sizeAndCrustModifer.setName(pizzaPrice.getSize().getName() + " " + pizzaPrice.getCrust());
			crustModifier = sizeAndCrustModifer;
		}
		else {
			crustModifier = new TicketItemModifier();
			crustModifier.setName(pizzaPrice.getSize().getName() + " " + pizzaPrice.getCrust());
			crustModifier.setModifierType(TicketItemModifier.CRUST);
			crustModifier.setInfoOnly(true);
			crustModifier.setTicketItem(ticketItem);
		}
		crustSelected = true;
	}

	private void doFullSectionMode() {
		wholeSectionView.removeAll();
		fullSectionLayout.removeAll();
		fullSectionLayout.add(sectionWhole);
		sectionLayout.show(sectionView, "full");
		ticketItem.setPizzaSectionMode(PIZZA_SECTION_MODE.FULL);
	}

	private void allSectionModifierClear() {
		for (Section section : sectionList) {
			if (section.sectionModifierTableModel.getRows() == null) {
				continue;
			}
			section.clearItems();
		}
	}

	private void doHalfSectionMode() {
		wholeSectionView.add(sectionWhole, "grow");
		sectionLayout.show(sectionView, "half");
		ticketItem.setPizzaSectionMode(PIZZA_SECTION_MODE.HALF);
	}

	private void doQuarterSectionMode() {
		wholeSectionView.add(sectionWhole, "grow");
		sectionLayout.show(sectionView, "quarter");
		ticketItem.setPizzaSectionMode(PIZZA_SECTION_MODE.QUARTER);
	}

	class SectionModifierTableModel extends ListTableModel<TicketItemModifier> {

		public SectionModifierTableModel() {
			super(new String[] { "Item", "Price" });
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			TicketItemModifier item = (TicketItemModifier) rows.get(rowIndex);
			if (item instanceof TicketItemModifier) {
				((TicketItemModifier) item).calculatePrice();
			}

			switch (columnIndex) {
				case 0:
					if (item instanceof TicketItemModifier) {
						return ((TicketItemModifier) item).getName();
					}
					return " " + item.getNameDisplay();

				case 1:
					Double total = null;
					if (item instanceof TicketItemModifier) {
						total = item.getUnitPrice();
						return NumberUtil.roundToTwoDigit(total);
					}
					return null;

			}

			return null;
		}

		public void deleteGivenItem(TicketItemModifier ticketItemModifier) {
			for (Iterator iterator = rows.iterator(); iterator.hasNext();) {
				TicketItemModifier tModifier = (TicketItemModifier) iterator.next();

				if (ticketItemModifier == tModifier) {
					iterator.remove();
				}

			}
			fireTableDataChanged();
		}

		public void deleteGivenItemByName(TicketItemModifier ticketItemModifier) {
			for (Iterator iterator = rows.iterator(); iterator.hasNext();) {
				TicketItemModifier tModifier = (TicketItemModifier) iterator.next();

				if (ticketItemModifier.getName().equals(tModifier.getName())) {
					iterator.remove();
				}

			}
			fireTableDataChanged();
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "FULL") {

			if (ticketItem.getTicketItemModifiers() != null && ticketItem.getTicketItemModifiers().size() > 0) {
				int questionDialog = POSMessageDialog.showYesNoQuestionDialog(POSUtil.getFocusedWindow(),
						"Items of the section will be deleted, Are you sure to change section mode?", "Change Section Mode");
				if (questionDialog != JOptionPane.YES_OPTION) {
					currentButton.setSelected(true);
					return;
				}
				allSectionModifierClear();
			}
			doFullSectionMode();
		}
		else if (e.getActionCommand() == "HALF") {

			if (ticketItem.getTicketItemModifiers() != null && ticketItem.getTicketItemModifiers().size() > 0) {
				int questionDialog = POSMessageDialog.showYesNoQuestionDialog(POSUtil.getFocusedWindow(),
						"Items of the section will be deleted, Are you sure to change section mode?", "Change Section Mode");
				if (questionDialog != JOptionPane.YES_OPTION) {
					currentButton.setSelected(true);
					return;
				}
				allSectionModifierClear();
			}
			doHalfSectionMode();
		}
		else if (e.getActionCommand() == "QUARTER") {
			if (ticketItem.getTicketItemModifiers() != null && ticketItem.getTicketItemModifiers().size() > 0) {
				int questionDialog = POSMessageDialog.showYesNoQuestionDialog(POSUtil.getFocusedWindow(),
						"Items of the section will be deleted, Are you sure to change section mode?", "Change Section Mode");
				if (questionDialog != JOptionPane.YES_OPTION) {
					currentButton.setSelected(true);
					return;
				}
				allSectionModifierClear();
			}
			doQuarterSectionMode();
		}
		currentButton = (JToggleButton) e.getSource();
	}
}
