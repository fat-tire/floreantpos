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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.commons.collections.CollectionUtils;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.Discount;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketDiscount;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemDiscount;
import com.floreantpos.model.dao.DiscountDAO;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.ScrollableFlowPanel;
import com.floreantpos.util.POSUtil;

/**
 * 
 * @author MShahriar
 */
public class DiscountSelectionDialog extends OkCancelOptionDialog implements ActionListener {

	private ScrollableFlowPanel buttonsPanel;

	private HashMap<Integer, TicketDiscount> addedTicketDiscounts = new HashMap<Integer, TicketDiscount>();
	private List<Integer> clearTicketItemDiscounts = new ArrayList<Integer>();
	private HashMap<Integer, DiscountButton> buttonMap = new HashMap<Integer, DiscountButton>();

	private Ticket ticket;

	private JPanel itemSearchPanel;
	private JTextField txtSearchItem;

	public DiscountSelectionDialog(Ticket ticket) {
		super(POSUtil.getFocusedWindow(), Messages.getString("DiscountSelectionDialog.0"));
		this.ticket = ticket;
		initComponent();

		if (ticket.getDiscounts() != null) {
			for (TicketDiscount ticketDiscount : ticket.getDiscounts()) {
				addedTicketDiscounts.put(ticketDiscount.getDiscountId(), ticketDiscount);
			}
		}
	}

	private void initComponent() {
		setOkButtonText(POSConstants.SAVE_BUTTON_TEXT);
		createCouponSearchPanel();
		getContentPanel().add(itemSearchPanel, BorderLayout.NORTH);

		buttonsPanel = new ScrollableFlowPanel(FlowLayout.LEADING);

		JScrollPane scrollPane = new PosScrollPane(buttonsPanel, PosScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, PosScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(80, 0));
		scrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), scrollPane.getBorder()));

		getContentPanel().add(scrollPane, BorderLayout.CENTER);

		rendererDiscounts();

		setSize(1024, 720);
	}

	private void createCouponSearchPanel() {
		itemSearchPanel = new JPanel(new BorderLayout(5, 5));
		PosButton btnSearch = new PosButton("...");
		btnSearch.setPreferredSize(new Dimension(60, 40));

		JLabel lblCoupon = new JLabel("Enter Coupon Number");

		txtSearchItem = new JTextField();
		txtSearchItem.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				txtSearchItem.setText("Scan barcode");
				txtSearchItem.setForeground(Color.gray);
			}

			@Override
			public void focusGained(FocusEvent e) {
				txtSearchItem.setForeground(Color.black);
				txtSearchItem.setText("");
			}
		});

		txtSearchItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (txtSearchItem.getText().equals("")) {
					POSMessageDialog.showMessage("Please enter coupon number or barcode ");
					return;
				}
				if (!addCouponByBarcode(txtSearchItem.getText())) {
					addCouponById(txtSearchItem.getText());
				}
				txtSearchItem.setText("");
			}
		});

		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ItemSearchDialog dialog = new ItemSearchDialog(Application.getPosWindow());
				dialog.setTitle("Search Coupon");
				dialog.pack();
				dialog.open();
				if (dialog.isCanceled()) {
					return;
				}

				txtSearchItem.requestFocus();

				if (!addCouponByBarcode(dialog.getValue())) {
					if (!addCouponById(dialog.getValue())) {
						POSMessageDialog.showError(Application.getPosWindow(), "Coupon not found");
					}
				}
			}
		});
		itemSearchPanel.add(lblCoupon, BorderLayout.WEST);
		itemSearchPanel.add(txtSearchItem);
		itemSearchPanel.add(btnSearch, BorderLayout.EAST);
	}

	private static boolean isParsable(String input) {
		boolean parsable = true;
		try {
			Integer.parseInt(input);
		} catch (NumberFormatException e) {
			parsable = false;
		}
		return parsable;
	}

	private boolean addCouponById(String id) {

		if (!isParsable(id)) {
			return false;
		}

		Integer itemId = Integer.parseInt(id);
		Discount discount = DiscountDAO.getInstance().get(itemId);

		if (discount == null) {
			return false;
		}

		if (discount.getQualificationType() == Discount.QUALIFICATION_TYPE_ITEM) {
			DiscountButton discountButton = buttonMap.get(discount.getId());
			applyDiscountToTicketItems(discountButton);
		}
		else {
			if (discount.isModifiable()) {
				double newValue = getModifiedValue(discount);
				if (newValue <= 0) {
					newValue = discount.getValue();
				}
				discount.setValue(newValue);
			}
			addedTicketDiscounts.put(discount.getId(), Ticket.convertToTicketDiscount(discount, ticket));
		}

		DiscountButton button = buttonMap.get(discount.getId());
		if (button != null) {
			button.setSelected(true);
		}

		return true;
	}

	private boolean addCouponByBarcode(String barcode) {

		Discount discount = DiscountDAO.getInstance().getDiscountByBarcode(barcode);

		if (discount == null) {
			return false;
		}

		if (discount.getQualificationType() == Discount.QUALIFICATION_TYPE_ITEM) {
			DiscountButton discountButton = buttonMap.get(discount.getId());
			applyDiscountToTicketItems(discountButton);
		}
		else {
			if (discount.isModifiable()) {
				double newValue = getModifiedValue(discount);
				if (newValue <= 0) {
					newValue = discount.getValue();
				}
				discount.setValue(newValue);
			}
			addedTicketDiscounts.put(discount.getId(), Ticket.convertToTicketDiscount(discount, ticket));
		}

		DiscountButton button = buttonMap.get(discount.getId());
		button.setSelected(true);

		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		rendererDiscounts();
	}

	private void rendererDiscounts() {
		buttonMap.clear();
		buttonsPanel.getContentPane().removeAll();

		List<Discount> discounts = DiscountDAO.getInstance().findAllValidCoupons();

		Dimension size = PosUIManager.getSize(115, 80);
		for (Discount discount : discounts) {
			DiscountButton btnDiscount = new DiscountButton(discount);
			btnDiscount.setSelected(false);
			btnDiscount.setPreferredSize(size);
			buttonsPanel.add(btnDiscount);
			buttonMap.put(discount.getId(), btnDiscount);
		}

		if (ticket.getDiscounts() != null) {
			for (TicketDiscount ticketCouponAndDiscount : ticket.getDiscounts()) {
				DiscountButton ticketDiscountButton = buttonMap.get(ticketCouponAndDiscount.getDiscountId());

				if (ticketDiscountButton != null) {
					ticketDiscountButton.setSelected(true);
				}
			}
		}

		for (TicketItem ticketItem : ticket.getTicketItems()) {
			if (ticketItem.getDiscounts() == null) {
				continue;
			}
			for (TicketItemDiscount ticketItemDiscount : ticketItem.getDiscounts()) {
				if (ticketItemDiscount == null) {
					continue;
				}
				DiscountButton ticketDiscountButton = buttonMap.get(ticketItemDiscount.getDiscountId());
				if (ticketDiscountButton != null) {
					ticketDiscountButton.setSelected(true);
				}
			}
		}

		buttonsPanel.repaint();
		buttonsPanel.revalidate();
	}

	@Override
	public void doOk() {
		List<TicketDiscount> couponAndDiscounts = ticket.getDiscounts();
		if (couponAndDiscounts == null)
			couponAndDiscounts = new ArrayList<TicketDiscount>();
		if (!CollectionUtils.isEqualCollection(couponAndDiscounts, addedTicketDiscounts.values())) {
			couponAndDiscounts.clear();

			for (TicketDiscount ticketDiscount : addedTicketDiscounts.values()) {
				ticket.addTodiscounts(ticketDiscount);
			}
		}

		for (Iterator iterator = ticket.getTicketItems().iterator(); iterator.hasNext();) {
			TicketItem ticketItem = (TicketItem) iterator.next();
			if (ticketItem.getDiscounts() == null) {
				continue;
			}
			for (Iterator iterator2 = ticketItem.getDiscounts().iterator(); iterator2.hasNext();) {
				TicketItemDiscount ticketItemDiscount = (TicketItemDiscount) iterator2.next();
				if (clearTicketItemDiscounts.contains((Integer) ticketItemDiscount.getDiscountId())) {
					iterator2.remove();
				}

			}
		}

		for (TicketItem ticketItem : ticket.getTicketItems()) {
			for (DiscountButton discountButton : buttonMap.values()) {
				if (discountButton.ticketItems.contains(ticketItem)) {
					ticketItem.getDiscounts().add(MenuItem.convertToTicketItemDiscount(discountButton.discount, ticketItem));
				}
			}
		}

		setCanceled(false);
		dispose();
	}

	public void doCancel() {
		addedTicketDiscounts.clear();
		buttonMap.clear();
		setCanceled(true);
		dispose();
	}

	private double getModifiedValue(Discount discount) {
		Double newValue = NumberSelectionDialog2.takeDoubleInput("Enter Amount", "Enter Amount", discount.getValue()); //$NON-NLS-1$ //$NON-NLS-2$
		if (newValue > 0) {
			return newValue;
		}
		return 0;
	}

	private class DiscountButton extends POSToggleButton implements ActionListener {
		Discount discount;
		List<TicketItem> ticketItems;

		DiscountButton(Discount discount) {
			this.discount = discount;
			ticketItems = new ArrayList<TicketItem>();

			setFont(getFont().deriveFont(Font.BOLD, PosUIManager.getFontSize(18)));
			setText("<html><body><center>" + discount.getName() + "<br></center></body></html>"); //$NON-NLS-1$ //$NON-NLS-2$ 

			if (discount.getQualificationType() == Discount.QUALIFICATION_TYPE_ITEM) {
				setBackground(Color.CYAN);
			}
			else {
				setBackground(Color.MAGENTA);
			}
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			if (isSelected()) {
				if (discount.getQualificationType() == Discount.QUALIFICATION_TYPE_ITEM) {
					applyDiscountToTicketItems(this);
				}
				else {
					if (discount.isModifiable()) {
						double newValue = getModifiedValue(discount);
						if (newValue <= 0) {
							newValue = discount.getValue();
						}
						discount.setValue(newValue);
					}
					addedTicketDiscounts.put(discount.getId(), Ticket.convertToTicketDiscount(discount, ticket));
				}
			}
			else {
				if (discount.getQualificationType() == Discount.QUALIFICATION_TYPE_ITEM) {
					clearTicketItemDiscounts.add(discount.getId());
				}
				else {
					addedTicketDiscounts.remove(discount.getId());
				}
			}
		}
	}

	private void applyDiscountToTicketItems(DiscountButton discountButton) {
		TicketItemDiscountSelectionDialog dialog = new TicketItemDiscountSelectionDialog(ticket, discountButton.discount);
		dialog.open();
		if (!dialog.isCanceled()) {
			discountButton.ticketItems = dialog.getSelectedTicketItems();
			discountButton.setSelected(true);
		}
		else {
			discountButton.setSelected(false);
		}
	}
}
