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
 * Discount.java
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.Discount;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketCouponAndDiscount;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemDiscount;
import com.floreantpos.model.dao.DiscountDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.swing.ScrollableFlowPanel;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;

/**
 * 
 * @author MShahriar
 */
public class DiscountSelectionDialog extends POSDialog implements ActionListener {

	private ScrollableFlowPanel buttonsPanel;

	private DefaultListModel<DiscountButton> addTicketItemDiscountListModel = new DefaultListModel<DiscountButton>();
	private DefaultListModel<DiscountButton> addedTicketDiscountListModel = new DefaultListModel<DiscountButton>();

	private HashMap<Integer, DiscountButton> buttonMap = new HashMap<Integer, DiscountButton>();

	private TicketItem ticketItem;
	private Ticket ticket;

	private ButtonGroup btnGroup;
	private POSToggleButton btnItem;
	private POSToggleButton btnOrder;

	public DiscountSelectionDialog(TicketItem ticketItem, Ticket ticket) {
		this.ticketItem = ticketItem;
		this.ticket = ticket;

		initializeComponent();
	}

	private void initializeComponent() {
		setTitle("Select Discount"); //$NON-NLS-1$
		setLayout(new BorderLayout());

		JPanel headerPanel = new JPanel(new BorderLayout());

		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle("Select Discount");//$NON-NLS-1$ 

		JPanel toggleBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));

		btnItem = new POSToggleButton("ITEM");
		btnItem.setSelected(true);
		btnItem.addActionListener(this);

		btnOrder = new POSToggleButton("ORDER");
		btnOrder.addActionListener(this);

		btnGroup = new ButtonGroup();
		btnGroup.add(btnItem);
		btnGroup.add(btnOrder);

		toggleBtnPanel.add(btnItem);
		toggleBtnPanel.add(btnOrder);

		headerPanel.add(titlePanel, BorderLayout.NORTH);
		headerPanel.add(toggleBtnPanel, BorderLayout.SOUTH);

		add(headerPanel, BorderLayout.NORTH);

		JPanel buttonActionPanel = new JPanel(new MigLayout("fill")); //$NON-NLS-1$

		PosButton btnOk = new PosButton(Messages.getString("TicketSelectionDialog.3")); //$NON-NLS-1$
		btnOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doFinishDiscountSelection();
			}
		});

		PosButton btnCancel = new PosButton(POSConstants.CANCEL.toUpperCase());
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addTicketItemDiscountListModel.clear();
				addedTicketDiscountListModel.clear();
				setCanceled(true);
				dispose();
			}
		});

		buttonActionPanel.add(btnOk, "w 80!,split 2,align center"); //$NON-NLS-1$
		buttonActionPanel.add(btnCancel, "w 80!"); //$NON-NLS-1$

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

		setSize(1024, 720);
		setResizable(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (btnItem.isSelected()) {
			rendererTicketItemDiscounts();
		}
		else {
			rendererTicketDiscounts();
		}
	}

	private void rendererTicketItemDiscounts() {
		buttonMap.clear();
		buttonsPanel.getContentPane().removeAll();

		if (ticketItem == null) {
			return;
		}

	
		Integer itemId = Integer.parseInt(ticketItem.getItemCode());
		MenuItem menuItem = MenuItemDAO.getInstance().get(itemId);
		List<Discount> discounts = DiscountDAO.getInstance().getValidCoupon(menuItem);

		for (Discount discount : discounts) {
			DiscountButton btnDiscount = new DiscountButton(discount);
			btnDiscount.setSelected(false);
			buttonsPanel.add(btnDiscount);
			buttonMap.put(discount.getId(), btnDiscount);
		}

		if (ticketItem.getDiscounts() != null) {
			for (TicketItemDiscount ticketItemDiscount : ticketItem.getDiscounts()) {
				DiscountButton ticketItemDiscountButton = buttonMap.get(ticketItemDiscount.getDiscountId());

				if (ticketItemDiscountButton != null) {
					ticketItemDiscountButton.setSelected(true);
					addTicketItemDiscountListModel.addElement(ticketItemDiscountButton);
				}
			}
		}
		buttonsPanel.repaint();
		buttonsPanel.revalidate();
	}

	private void rendererTicketDiscounts() {
		buttonMap.clear();
		buttonsPanel.getContentPane().removeAll();

		List<Discount> discounts= DiscountDAO.getInstance().getTicketValidCoupon();

		if (discounts.isEmpty() || discounts == null) {
			return;
		}
		for (Discount discount : discounts) {
			DiscountButton btnDiscount = new DiscountButton(discount);
			btnDiscount.setSelected(false);
			buttonsPanel.add(btnDiscount);
			buttonMap.put(discount.getId(), btnDiscount);
		}

		if (ticket.getCouponAndDiscounts() != null) {
			for (TicketCouponAndDiscount TicketCouponAndDiscount : ticket.getCouponAndDiscounts()) {
				DiscountButton ticketDiscountButton = buttonMap.get(TicketCouponAndDiscount.getCouponAndDiscountId());

				if (ticketDiscountButton != null) {
					ticketDiscountButton.setSelected(true);
					addedTicketDiscountListModel.addElement(ticketDiscountButton);
				}
			}
		}
		buttonsPanel.repaint();
		buttonsPanel.revalidate();
	}

	protected void doFinishDiscountSelection() {
		setCanceled(false);
		dispose();
	}

	public List<Discount> getSelectedTicketItemDiscounts() {
		Enumeration<DiscountButton> elements = addTicketItemDiscountListModel.elements();
		List<Discount> ticketItemDiscounts = new ArrayList<Discount>();

		while (elements.hasMoreElements()) {
			DiscountButton discountButton = (DiscountButton) elements.nextElement();
			if (discountButton.isSelected()) {
				ticketItemDiscounts.add(discountButton.getDiscount());
			}
		}
		return ticketItemDiscounts;
	}

	public List<Discount> getSelectedTicketDiscounts() {
		Enumeration<DiscountButton> elements = addedTicketDiscountListModel.elements();
		List<Discount> ticketDiscounts = new ArrayList<Discount>();

		while (elements.hasMoreElements()) {
			DiscountButton discountButton = (DiscountButton) elements.nextElement();
			if (discountButton.isSelected()) {
				ticketDiscounts.add(discountButton.getDiscount());
			}
		}
		return ticketDiscounts;
	}

	private class DiscountButton extends POSToggleButton implements ActionListener {
		private static final int BUTTON_SIZE = 119;
		Discount discount;

		DiscountButton(Discount discount) {
			this.discount = discount;
			setFocusable(true);
			setFocusPainted(true);
			setVerticalTextPosition(SwingConstants.BOTTOM);
			setHorizontalTextPosition(SwingConstants.CENTER);
			setFont(getFont().deriveFont(18.0f));

			setText("<html><body><center>" + discount.getName() + "<br>" /*+ "Value" + ":" + discount.getValue() */+ "</center></body></html>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

			setPreferredSize(new Dimension(BUTTON_SIZE, TerminalConfig.getMenuItemButtonHeight()));
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			if (btnItem.isSelected()) {
				if (isSelected()) {
					addTicketItemDiscountListModel.addElement(this);
				}
				else {
					addTicketItemDiscountListModel.removeElement(this);
				}
			}
			else {
				if (isSelected()) {
					addedTicketDiscountListModel.addElement(this);
				}
				else {
					addedTicketDiscountListModel.removeElement(this);
				}
			}

		}

		public Discount getDiscount() {
			return this.discount;
		}
	}

	public void setSelectedItem(boolean enabled) {
		if (enabled) {
			btnItem.setSelected(enabled);
			btnOrder.setVisible(false); 
			rendererTicketItemDiscounts();
		}
		else {
			btnOrder.setSelected(true);
			btnItem.setVisible(false); 
			rendererTicketDiscounts();
		}
	}
}
