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
package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemDiscount;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.report.ReceiptPrintService;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.ui.views.order.RootView;

public class OrderInfoDialog extends POSDialog {
	OrderInfoView view;
	private boolean reorder = false;
	private PosButton btnReOrder;
	private PosButton btnTransferUser;
	private PosButton btnPrint;
	private PosButton btnPrintDriverCopy;

	public OrderInfoDialog(OrderInfoView view) {
		this.view = view;
		setTitle(Messages.getString("OrderInfoDialog.0")); //$NON-NLS-1$

		createUI();
	}

	public void createUI() {
		add(view);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);

		btnReOrder = new PosButton("Reorder");

		btnReOrder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (Iterator iter = view.getTickets().iterator(); iter.hasNext();) {

					Ticket ticket = (Ticket) iter.next();

					createReOrder(ticket);
					setCanceled(true);
					dispose();

				}
			}
		});

		panel.add(btnReOrder);

		btnTransferUser = new PosButton();
		btnTransferUser.setText(Messages.getString("OrderInfoDialog.3")); //$NON-NLS-1$
		btnTransferUser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				User currentUser = Application.getCurrentUser();

				for (Iterator iter = view.getTickets().iterator(); iter.hasNext();) {

					Ticket ticket = (Ticket) iter.next();

					if (!currentUser.equals(ticket.getOwner())) {

						if (!currentUser.hasPermission(UserPermission.TRANSFER_TICKET)) {
							POSMessageDialog.showError(getParent(), Messages.getString("OrderInfoDialog.4") + ticket.getId()); //$NON-NLS-1$
							return;
						}
					}
				}

				UserTransferDialog dialog = new UserTransferDialog(view);
				dialog.setSize(PosUIManager.getSize(360, 555));
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setLocationRelativeTo(Application.getPosWindow());
				dialog.setVisible(true);
			}
		});

		panel.add(btnTransferUser);

		btnPrint = new PosButton();
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doPrint();
			}
		});
		btnPrint.setText(Messages.getString("OrderInfoDialog.1")); //$NON-NLS-1$
		panel.add(btnPrint);

		btnPrintDriverCopy = new PosButton();
		btnPrintDriverCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doPrintDriverCopy();
			}
		});
		btnPrintDriverCopy.setText("Print (Driver Copy)"); //$NON-NLS-1$
		btnPrintDriverCopy.setVisible(false);
		panel.add(btnPrintDriverCopy);

		PosButton btnClose = new PosButton();
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnClose.setText(Messages.getString("OrderInfoDialog.2")); //$NON-NLS-1$
		panel.add(btnClose);
	}

	private void doPrintDriverCopy() {
		try {
			view.printCopy(ReceiptPrintService.DRIVER_COPY);
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage());
		}
	}

	public void updateView() {
		btnTransferUser.setVisible(false);
		btnReOrder.setVisible(false);
		btnPrintDriverCopy.setVisible(true);
		btnPrint.setText("Print (Customer Copy)");
	}

	protected void doPrint() {
		try {
			view.printCopy(ReceiptPrintService.CUSTOMER_COPY);
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage());
		}
	}

	private void createReOrder(Ticket oldticket) {
		Ticket ticket = new Ticket();
		ticket.setPriceIncludesTax(oldticket.isPriceIncludesTax());
		ticket.setOrderType(oldticket.getOrderType());
		ticket.setProperties(oldticket.getProperties());
		ticket.setTerminal(Application.getInstance().getTerminal());
		ticket.setOwner(Application.getCurrentUser());
		ticket.setShift(Application.getInstance().getCurrentShift());
		ticket.setNumberOfGuests(oldticket.getNumberOfGuests());

		Calendar currentTime = Calendar.getInstance();
		ticket.setCreateDate(currentTime.getTime());
		ticket.setCreationHour(currentTime.get(Calendar.HOUR_OF_DAY));

		List<TicketItem> newTicketItems = new ArrayList<TicketItem>();
		for (TicketItem oldTicketItem : oldticket.getTicketItems()) {
			TicketItem newTicketItem = new TicketItem();
			newTicketItem.setItemCount(oldTicketItem.getItemCount());
			newTicketItem.setItemQuantity(oldTicketItem.getItemQuantity());
			newTicketItem.setItemId(oldTicketItem.getItemId());
			newTicketItem.setHasModifiers(oldTicketItem.isHasModifiers());
			newTicketItem.setName(oldTicketItem.getName());
			newTicketItem.setGroupName(oldTicketItem.getGroupName());
			newTicketItem.setCategoryName(oldTicketItem.getCategoryName());
			newTicketItem.setUnitPrice(oldTicketItem.getUnitPrice());
			newTicketItem.setFractionalUnit(oldTicketItem.isFractionalUnit());
			newTicketItem.setItemUnitName(oldTicketItem.getItemUnitName());

			List<TicketItemDiscount> discounts = oldTicketItem.getDiscounts();
			if (discounts != null) {
				List<TicketItemDiscount> newDiscounts = new ArrayList<TicketItemDiscount>();
				for (TicketItemDiscount ticketItemDiscount : discounts) {
					TicketItemDiscount newDiscount = new TicketItemDiscount(ticketItemDiscount);
					newDiscount.setTicketItem(newTicketItem);
					newDiscounts.add(newDiscount);
				}
				newTicketItem.setDiscounts(newDiscounts);
			}

			List<TicketItemModifier> ticketItemModifiers = oldTicketItem.getTicketItemModifiers();
			if (ticketItemModifiers != null) {
				for (TicketItemModifier ticketItemModifier : ticketItemModifiers) {
					TicketItemModifier newModifier = new TicketItemModifier();
					newModifier.setModifierId(ticketItemModifier.getModifierId());
					newModifier.setMenuItemModifierGroupId(ticketItemModifier.getMenuItemModifierGroupId());
					newModifier.setItemCount(ticketItemModifier.getItemCount());
					newModifier.setName(ticketItemModifier.getName());
					newModifier.setUnitPrice(ticketItemModifier.getUnitPrice());
					newModifier.setTaxRate(ticketItemModifier.getTaxRate());
					newModifier.setModifierType(ticketItemModifier.getModifierType());
					newModifier.setPrintedToKitchen(false);
					newModifier.setShouldPrintToKitchen(ticketItemModifier.isShouldPrintToKitchen());
					newModifier.setTicketItem(newTicketItem);
					newTicketItem.addToticketItemModifiers(newModifier);
				}
			}
			List<TicketItemModifier> addOnsList = oldTicketItem.getAddOns();
			if (addOnsList != null) {
				for (TicketItemModifier addOns : oldTicketItem.getAddOns()) {
					TicketItemModifier newAddOns = new TicketItemModifier();
					newAddOns.setModifierId(addOns.getModifierId());
					newAddOns.setMenuItemModifierGroupId(addOns.getMenuItemModifierGroupId());
					newAddOns.setItemCount(addOns.getItemCount());
					newAddOns.setName(addOns.getName());
					newAddOns.setUnitPrice(addOns.getUnitPrice());
					newAddOns.setTaxRate(addOns.getTaxRate());
					newAddOns.setModifierType(addOns.getModifierType());
					newAddOns.setPrintedToKitchen(false);
					newAddOns.setShouldPrintToKitchen(addOns.isShouldPrintToKitchen());
					newTicketItem.addToaddOns(newAddOns);
				}
			}

			newTicketItem.setTaxRate(oldTicketItem.getTaxRate());
			newTicketItem.setBeverage(oldTicketItem.isBeverage());
			newTicketItem.setShouldPrintToKitchen(oldTicketItem.isShouldPrintToKitchen());
			newTicketItem.setPrinterGroup(oldTicketItem.getPrinterGroup());
			newTicketItem.setPrintedToKitchen(false);

			newTicketItem.setTicket(ticket);
			newTicketItems.add(newTicketItem);
		}
		ticket.getTicketItems().addAll(newTicketItems);

		OrderView.getInstance().setCurrentTicket(ticket);
		RootView.getInstance().showView(OrderView.VIEW_NAME);

		reorder = true;
	}

	public boolean isReorder() {
		return reorder;
	}

	public void showOnlyPrintButton() {
		btnReOrder.setVisible(false);
		btnTransferUser.setVisible(false);
	}
}
