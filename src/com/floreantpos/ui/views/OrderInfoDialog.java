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
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.order.OrderController;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.ui.views.order.RootView;

public class OrderInfoDialog extends POSDialog {
	OrderInfoView view;

	public OrderInfoDialog(OrderInfoView view) {
		this.view = view;
		setTitle(Messages.getString("OrderInfoDialog.0")); //$NON-NLS-1$

		createUI();
	}

	public void createUI() {
		add(view);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);

		PosButton btnReOrder = new PosButton("Reorder");

		btnReOrder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (Iterator iter = view.getTickets().iterator(); iter.hasNext();) {

					Ticket ticket = (Ticket) iter.next();

					createReOrder(ticket);

				}
			}
		});

		panel.add(btnReOrder);

		PosButton btnTransferUser = new PosButton();
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
				dialog.setSize(360, 555);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setLocationRelativeTo(Application.getPosWindow());
				dialog.setVisible(true);
			}
		});

		panel.add(btnTransferUser);

		PosButton btnPrint = new PosButton();
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doPrint();
			}
		});
		btnPrint.setText(Messages.getString("OrderInfoDialog.1")); //$NON-NLS-1$
		panel.add(btnPrint);

		PosButton btnClose = new PosButton();
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnClose.setText(Messages.getString("OrderInfoDialog.2")); //$NON-NLS-1$
		panel.add(btnClose);
	}

	protected void doPrint() {
		try {
			view.print();
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage());
		}
	}

	/*private void createReOrder(Ticket oldticket) {
		Ticket ticket = new Ticket();
		ticket.setPriceIncludesTax(oldticket.isPriceIncludesTax());
		ticket.setType(oldticket.getType());
		ticket.setTerminal(Application.getInstance().getTerminal());
		ticket.setOwner(Application.getCurrentUser());
		ticket.setShift(Application.getInstance().getCurrentShift());
		
		ticket.setActiveDate(oldticket.getActiveDate());
		ticket.setAdvanceAmount(oldticket.getAdvanceAmount());
		ticket.setAssignedDriver(oldticket.getAssignedDriver());
		ticket.setClosed(oldticket.isClosed());
		ticket.setClosingDate(oldticket.getClosingDate());
		ticket.setCustomerWillPickup(oldticket.isCustomerWillPickup());
		ticket.setDeliveryAddress(oldticket.getDeliveryAddress());
		ticket.setDeliveryCharge(oldticket.getDeliveryCharge());
		ticket.setDeliveryDate(oldticket.getDeliveryDate());
		ticket.setDiscountAmount(oldticket.getDiscountAmount());
		ticket.setDueAmount(oldticket.getDueAmount());
		ticket.setExtraDeliveryInfo(oldticket.getExtraDeliveryInfo());
		ticket.setGratuity(oldticket.getGratuity());
		
		ticket.setNumberOfGuests(oldticket.getNumberOfGuests());
		
		ticket.setPaid(oldticket.isPaid());
		ticket.setPaidAmount(oldticket.getPaidAmount());
		ticket.setProperties(oldticket.getProperties());
		
		
		//ticket.setDiscounts(oldticket.getDiscounts());
		//ticket.setDrawerResetted(oldticket.getd)

		//ticket.setTicketItems(oldticket.getTicketItems());

	//	ticket.setCustomer(oldticket.getProperty(Ticket.CUSTOMER_NAME));

		Calendar currentTime = Calendar.getInstance();
		ticket.setCreateDate(currentTime.getTime());
		ticket.setCreationHour(currentTime.get(Calendar.HOUR_OF_DAY));

		//OrderView.getInstance().setCurrentTicket(ticket);
		//OrderView.getInstance().getTicketView().doFinishOrder();
		TicketDAO.getInstance().saveOrUpdate(ticket);

	}*/
	
	private void createReOrder(Ticket oldticket) {
		Ticket ticket = new Ticket();
		ticket.setPriceIncludesTax(oldticket.isPriceIncludesTax());
		ticket.setType(oldticket.getType());
		ticket.setTerminal(Application.getInstance().getTerminal());
		ticket.setOwner(Application.getCurrentUser());
		ticket.setShift(Application.getInstance().getCurrentShift());
		ticket.setNumberOfGuests(oldticket.getNumberOfGuests());

		//Table numbers missing
		
		

		Calendar currentTime = Calendar.getInstance();
		ticket.setCreateDate(currentTime.getTime());
		ticket.setCreationHour(currentTime.get(Calendar.HOUR_OF_DAY));
		
		ticket.setTicketItems(oldticket.getTicketItems());
		OrderView.getInstance().setCurrentTicket(ticket);
		RootView.getInstance().showView(OrderView.VIEW_NAME);
	}
}
