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

package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.Discount;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemDiscount;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.swing.ScrollableFlowPanel;
import com.floreantpos.ui.TitlePanel;

/**
 * 
 * @author MShahriar
 */
public class TicketItemDiscountSelectionDialog extends POSDialog {

	private ScrollableFlowPanel buttonsPanel;
	private Ticket ticket;
	private Discount discount;

	private List<TicketItemButton> addedTicketItemList = new ArrayList<TicketItemButton>();

	public TicketItemDiscountSelectionDialog(Ticket ticket, Discount discount) {
		this.ticket = ticket;
		this.discount = discount;
		initializeComponent();
		rendererTicketItems();

		setResizable(true);
	}

	private void initializeComponent() {
		setTitle("SELECT ITEMS"); //$NON-NLS-1$
		setLayout(new BorderLayout());

		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle("SELECT ITEMS");//$NON-NLS-1$
		add(titlePanel, BorderLayout.NORTH);

		JPanel buttonActionPanel = new JPanel(new MigLayout("fill")); //$NON-NLS-1$

		PosButton btnOk = new PosButton("DONE"); //$NON-NLS-1$
		btnOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doApplyDiscount();
			}
		});

		PosButton btnCancel = new PosButton(POSConstants.CANCEL.toUpperCase());
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addedTicketItemList.clear();
				setCanceled(true);
				dispose();
			}
		});

		buttonActionPanel.add(btnOk, "w 120!,split 3,align center"); //$NON-NLS-1$
		buttonActionPanel.add(btnCancel, "w 120!"); //$NON-NLS-1$

		JPanel footerPanel = new JPanel(new BorderLayout());
		footerPanel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		footerPanel.add(new JSeparator(), BorderLayout.NORTH);
		footerPanel.add(buttonActionPanel);

		add(footerPanel, BorderLayout.SOUTH);

		buttonsPanel = new ScrollableFlowPanel(FlowLayout.LEADING);

		JScrollPane scrollPane = new PosScrollPane(buttonsPanel, PosScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, PosScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(80, 0));
		scrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), scrollPane.getBorder()));

		add(scrollPane, BorderLayout.CENTER);

		setSize(600, 500);
	}

	private void rendererTicketItems() {
		buttonsPanel.getContentPane().removeAll();

		List<TicketItem> ticketItems = ticket.getTicketItems();
		try {
			for (TicketItem ticketItem : ticketItems) {
				Integer itemId = Integer.parseInt(ticketItem.getItemId().toString());
				MenuItem menuItem = MenuItemDAO.getInstance().get(itemId);

				List<MenuItem> menuItems = discount.getMenuItems();
				if (menuItem != null) {
					if (discount.isApplyToAll() || menuItems.contains(menuItem)) {
						TicketItemButton ticketItemButton = new TicketItemButton(ticketItem);
						for (TicketItemDiscount ticketItemDiscount : ticketItem.getDiscounts()) {
							if (discount.getId().intValue() == ticketItemDiscount.getDiscountId().intValue()) {
								ticketItemButton.setSelected(true);
							}
						}
						buttonsPanel.add(ticketItemButton);
						addedTicketItemList.add(ticketItemButton);
					}
				}
			}
			buttonsPanel.repaint();
			buttonsPanel.revalidate();

		} catch (PosException e) {
			POSMessageDialog.showError(TicketItemDiscountSelectionDialog.this, e.getLocalizedMessage(), e);
		}
	}

	protected void doApplyDiscount() {
		for (TicketItemButton ticketItemButton : addedTicketItemList) {
			TicketItem ticketItem = ticketItemButton.getTicketItem();
			ticketItem.getDiscounts().clear();
			ticketItem.getDiscounts().addAll(ticketItemButton.ticketItemDiscounts);
		}
		setCanceled(false);
		dispose();
	}

	private class TicketItemButton extends POSToggleButton implements ActionListener {
		private static final int BUTTON_SIZE = 119;
		TicketItem ticketItem;
		List<TicketItemDiscount> ticketItemDiscounts = new ArrayList<TicketItemDiscount>();

		TicketItemButton(TicketItem ticketItem) {
			this.ticketItem = ticketItem;
			setFocusable(true);
			setFocusPainted(true);
			setVerticalTextPosition(SwingConstants.BOTTOM);
			setHorizontalTextPosition(SwingConstants.CENTER);
			ticketItemDiscounts.addAll(ticketItem.getDiscounts());

			setText("<html><body><center>#" + ticketItem.getName() + "</center></body></html>"); //$NON-NLS-1$ //$NON-NLS-2$ 

			setPreferredSize(new Dimension(BUTTON_SIZE, TerminalConfig.getMenuItemButtonHeight()));
			addActionListener(this);
		}

		public TicketItem getTicketItem() {
			return ticketItem;
		}

		public void actionPerformed(ActionEvent e) {
			if (isSelected()) {
				ticketItemDiscounts.add(MenuItem.convertToTicketItemDiscount(discount, ticketItem));
			}
			else {
				for (Iterator iterator = ticketItemDiscounts.iterator(); iterator.hasNext();) {
					TicketItemDiscount ticketItemDiscount = (TicketItemDiscount) iterator.next();
					if (ticketItemDiscount.getDiscountId().intValue() == discount.getId().intValue()) {
						iterator.remove();
					}
				}
			}
		}
	}
}
