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
 * TicketView.java
 *
 * Created on August 4, 2006, 3:42 PM
 */

package com.floreantpos.ui.views.order.modifier;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang3.StringUtils;

import com.floreantpos.IconFactory;
import com.floreantpos.model.ITicketItem;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.ui.views.order.actions.OrderListener;

/**
 * 
 * @author MShahriar
 */
public class TicketItemModifierTableView extends JPanel {
	private java.util.Vector<OrderListener> orderListeners = new java.util.Vector<OrderListener>();

	public final static String VIEW_NAME = "TICKET_MODIFIER_VIEW"; //$NON-NLS-1$

	private Vector<ModifierSelectionListener> listenerList = new Vector<ModifierSelectionListener>();

	private ModifierSelectionModel modifierSelectionModel;

	private com.floreantpos.swing.TransparentPanel ticketActionPanel = new com.floreantpos.swing.TransparentPanel();

	//private com.floreantpos.swing.PosButton btnDecreaseAmount;
	private com.floreantpos.swing.PosButton btnDelete = new PosButton(IconFactory.getIcon("/ui_icons/", "delete.png")); //$NON-NLS-1$ //$NON-NLS-2$	
	//private com.floreantpos.swing.PosButton btnIncreaseAmount = new PosButton(IconFactory.getIcon("/ui_icons/", "add_user.png")); //$NON-NLS-1$ //$NON-NLS-2$
	private com.floreantpos.swing.PosButton btnScrollDown;
	private com.floreantpos.swing.PosButton btnScrollUp = new PosButton(IconFactory.getIcon("/ui_icons/", "up.png")); //$NON-NLS-1$ //$NON-NLS-2$

	private com.floreantpos.swing.TransparentPanel ticketItemActionPanel;

	private javax.swing.JScrollPane ticketScrollPane;
	private com.floreantpos.ui.views.order.modifier.ModifierViewerTable modifierViewerTable;

	private TitledBorder titledBorder = new TitledBorder(""); //$NON-NLS-1$
	private Border border = new CompoundBorder(titledBorder, new EmptyBorder(5, 5, 5, 5));
	private ModifierSelectionListener listener;

	public TicketItemModifierTableView(ModifierSelectionModel modifierSelectionModel, ModifierSelectionListener listener) {
		this.modifierSelectionModel = modifierSelectionModel;
		this.listener = listener;
		initComponents();
	}

	private void initComponents() {
		titledBorder.setTitle(modifierSelectionModel.getTicketItem().getName());
		titledBorder.setTitleJustification(TitledBorder.CENTER);
		setBorder(border);
		setLayout(new java.awt.BorderLayout(5, 5));

		ticketItemActionPanel = new com.floreantpos.swing.TransparentPanel();
		//btnDecreaseAmount = new com.floreantpos.swing.PosButton();
		btnScrollDown = new com.floreantpos.swing.PosButton();
		modifierViewerTable = new com.floreantpos.ui.views.order.modifier.ModifierViewerTable(modifierSelectionModel.getTicketItem());
		ticketScrollPane = new PosScrollPane(modifierViewerTable);
		ticketScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		ticketScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		ticketScrollPane.setPreferredSize(new java.awt.Dimension(180, 200));

		createTicketActionPanel();
		createTicketItemControlPanel();

		JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
		centerPanel.add(ticketScrollPane);

		centerPanel.add(createItemDescriptionPanel(), BorderLayout.NORTH);
		add(centerPanel);
		add(ticketActionPanel, BorderLayout.SOUTH);
		centerPanel.add(ticketItemActionPanel, BorderLayout.EAST);

		modifierViewerTable.getRenderer().setInTicketScreen(true);
		modifierViewerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					updateSelectionView();
				}
			}
		});

		modifierViewerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {

				Object selected = modifierViewerTable.getSelected();
				if (!(selected instanceof ITicketItem)) {
					return;
				}

				ITicketItem item = (ITicketItem) selected;

				Boolean printedToKitchen = item.isPrintedToKitchen();
				btnDelete.setEnabled(!printedToKitchen);
			}

		});

		setPreferredSize(new java.awt.Dimension(360, 463));

	}// </editor-fold>//GEN-END:initComponents

	private JPanel createItemDescriptionPanel() {
		MenuItem menuItem = modifierSelectionModel.getMenuItem();
		JPanel itemDescriptionPanel = new JPanel(new MigLayout("inset 0,center"));
		String description = menuItem.getDescription();
		if (StringUtils.isEmpty(description) && menuItem.getImage() == null) {
			return itemDescriptionPanel;
		}
		itemDescriptionPanel.setBorder(BorderFactory.createTitledBorder("-"));
		JLabel lblDescription = new JLabel();
		lblDescription.setText("<html><body>" + description + "</body></html>");
		JLabel pictureLabel = new JLabel(menuItem.getImage());

		itemDescriptionPanel.add(pictureLabel);
		itemDescriptionPanel.add(lblDescription);

		return itemDescriptionPanel;
	}

	private void createTicketActionPanel() {
		ticketActionPanel.setLayout(new GridLayout(1, 0, 5, 5));
	}

	private void createTicketItemControlPanel() {
		ticketItemActionPanel.setLayout(new GridLayout(0, 1, 5, 5));

		btnScrollUp.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doScrollUp(evt);
			}
		});

		//		btnIncreaseAmount.addActionListener(new java.awt.event.ActionListener() {
		//			public void actionPerformed(java.awt.event.ActionEvent evt) {
		//				doIncreaseAmount(evt);
		//			}
		//		});
		//
		//		btnDecreaseAmount.setIcon(IconFactory.getIcon("/ui_icons/", "minus.png")); //$NON-NLS-1$ //$NON-NLS-2$
		//		btnDecreaseAmount.addActionListener(new java.awt.event.ActionListener() {
		//			public void actionPerformed(java.awt.event.ActionEvent evt) {
		//				doDecreaseAmount(evt);
		//			}
		//		});

		btnScrollDown.setIcon(IconFactory.getIcon("/ui_icons/", "down.png")); //$NON-NLS-1$ //$NON-NLS-2$
		btnScrollDown.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doScrollDown(evt);
			}
		});

		btnDelete.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doDeleteSelection(evt);
			}
		});

		ticketItemActionPanel.add(btnScrollUp);
		//		ticketItemActionPanel.add(btnIncreaseAmount);
		//		ticketItemActionPanel.add(btnDecreaseAmount);
		ticketItemActionPanel.add(btnDelete);
		ticketItemActionPanel.add(btnScrollDown);

		ticketItemActionPanel.setPreferredSize(new Dimension(60, 360));
	}

	public void addModifierSelectionListener(ModifierSelectionListener listener) {
		listenerList.add(listener);
	}

	public void removeModifierSelectionListener(ModifierSelectionListener listener) {
		listenerList.remove(listener);
	}

	private void doDeleteSelection(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_doDeleteSelection
		Object object = modifierViewerTable.deleteSelectedItem();
		if (object != null) {
			updateView();
			listener.modifierRemoved((TicketItemModifier) object);
		}
	}

	private void doScrollDown(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_doScrollDown
		modifierViewerTable.scrollDown();
	}

	private void doScrollUp(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_doScrollUp
		modifierViewerTable.scrollUp();
	}

	public void removeModifier(TicketItem parent, TicketItemModifier modifier) {
		modifier.setItemCount(0);
		//modifier.setModifierType(TicketItemModifier.MODIFIER_NOT_INITIALIZED);
		modifierViewerTable.removeModifier(parent, modifier);
	}

	public void updateAllView() {
		modifierViewerTable.updateView();
		updateView();
	}

	public void selectRow(int rowIndex) {
		modifierViewerTable.selectRow(rowIndex);
	}

	public void updateView() {
		modifierViewerTable.updateView();
		//		if (ticket == null) {
		//			titledBorder.setTitle("Modifiers #"); //$NON-NLS-1$
		//			return;
		//		}
		//
		//		ticket.calculatePrice();
		//		
		//
		//		if (Application.getInstance().isPriceIncludesTax()) {
		//		}
		//		else {
		//		}
		//
		//		if (ticket.getId() == null) {
		//			titledBorder.setTitle("ITEM"); //$NON-NLS-1$
		//		}
		//		else {
		//			titledBorder.setTitle("ITEM #" + ticketItem.getName()); //$NON-NLS-1$
		//		}

	}

	public void addOrderListener(OrderListener listenre) {
		orderListeners.add(listenre);
	}

	public void removeOrderListener(OrderListener listenre) {
		orderListeners.remove(listenre);
	}

	public void setControlsVisible(boolean visible) {
		if (visible) {
			ticketActionPanel.setVisible(true);
			//			btnIncreaseAmount.setEnabled(true);
			//			btnDecreaseAmount.setEnabled(true);
			btnDelete.setEnabled(true);
		}
		else {
			ticketActionPanel.setVisible(false);
			//			btnIncreaseAmount.setEnabled(false);
			//			btnDecreaseAmount.setEnabled(false);
			btnDelete.setEnabled(false);
		}
	}

	private void updateSelectionView() {
		//		Object selectedObject = modifierViewerTable.getSelected();
		//
		//		ModifierSelectionDialog modifierSelectionView = ModifierSelectionDialog.getInstance();
		//
		//		TicketItem selectedTicketItem = null;
		//		
		//		if (selectedObject instanceof TicketItemModifier) {
		//			selectedTicketItem = ((TicketItemModifier) selectedObject).getParent().getParent();
		//			if (selectedTicketItem == null)
		//				return;
		//			
		//			TicketItemModifier ticketItemModifier = (TicketItemModifier) selectedObject;
		//			
		//		}
	}

	public com.floreantpos.ui.views.order.modifier.ModifierViewerTable getTicketViewerTable() {
		return modifierViewerTable;
	}

}
