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
package com.floreantpos.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXCollapsiblePane;

import com.floreantpos.ITicketList;
import com.floreantpos.Messages;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.OrderTypeFilter;
import com.floreantpos.model.PaymentStatusFilter;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.PasswordEntryDialog;

public class OrderFilterPanel extends JXCollapsiblePane {
	private ITicketList ticketList;
	private TicketListView ticketLists;
	POSToggleButton btnFilterByOpenStatus;
	POSToggleButton btnFilterByPaidStatus;
	POSToggleButton btnFilterByUnPaidStatus;

	//POSToggleButton btnFilterByUnPaidStatus ;
	//ButtonGroup paymentGroup;

	public OrderFilterPanel(ITicketList ticketList) {
		this.ticketList = ticketList;
		this.ticketLists = (TicketListView) ticketList;

		setCollapsed(true);
		getContentPane().setLayout(new MigLayout("fill", "fill, grow", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		createPaymentStatusFilterPanel();
		createOrderTypeFilterPanel();
	}

	private void createPaymentStatusFilterPanel() {
		btnFilterByOpenStatus = new POSToggleButton(PaymentStatusFilter.OPEN.toString());
		btnFilterByPaidStatus = new POSToggleButton(PaymentStatusFilter.PAID.toString());
		btnFilterByUnPaidStatus = new POSToggleButton(PaymentStatusFilter.CLOSED.toString());

		final ButtonGroup paymentGroup = new ButtonGroup();
		paymentGroup.add(btnFilterByOpenStatus);
		paymentGroup.add(btnFilterByPaidStatus);
		paymentGroup.add(btnFilterByUnPaidStatus);

		PaymentStatusFilter paymentStatusFilter = TerminalConfig.getPaymentStatusFilter();

		switch (paymentStatusFilter) {
			case OPEN:
				btnFilterByOpenStatus.setSelected(true);
				break;

			case PAID:
				btnFilterByPaidStatus.setSelected(true);
				break;

			case CLOSED:
				btnFilterByUnPaidStatus.setSelected(true);
				break;

		}

		ActionListener psFilterHandler = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String actionCommand = e.getActionCommand();

				if (actionCommand.equals("CLOSED") && !Application.getCurrentUser().hasPermission(UserPermission.VIEW_ALL_CLOSE_TICKETS)) {

					//
					String password = PasswordEntryDialog.show(Application.getPosWindow(), "Please enter privileged password");
					if (StringUtils.isEmpty(password)) {
						updateButton();
						return;
					}

					User user2 = UserDAO.getInstance().findUserBySecretKey(password);
					if (user2 == null) {
						POSMessageDialog.showError(Application.getPosWindow(), "No user found with that secret key");
						updateButton();
						return;
					}
					else {
						if (!user2.hasPermission(UserPermission.VIEW_ALL_CLOSE_TICKETS)) {
							POSMessageDialog.showError(Application.getPosWindow(), "No permission");
							updateButton();
							return;
						}
					}
				}

				String filter = actionCommand.replaceAll("\\s", "_"); //$NON-NLS-1$ //$NON-NLS-2$
				TerminalConfig.setPaymentStatusFilter(filter);

				ticketList.updateTicketList();
				ticketLists.updateButtonStatus();

			}
		};

		btnFilterByOpenStatus.addActionListener(psFilterHandler);
		btnFilterByPaidStatus.addActionListener(psFilterHandler);
		btnFilterByUnPaidStatus.addActionListener(psFilterHandler);

		JPanel filterByPaymentStatusPanel = new JPanel(new MigLayout("", "fill, grow", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		filterByPaymentStatusPanel.setBorder(new TitledBorder(Messages.getString("SwitchboardView.3"))); //$NON-NLS-1$
		filterByPaymentStatusPanel.add(btnFilterByOpenStatus);
		filterByPaymentStatusPanel.add(btnFilterByPaidStatus);
		filterByPaymentStatusPanel.add(btnFilterByUnPaidStatus);

		getContentPane().add(filterByPaymentStatusPanel);
	}

	private void createOrderTypeFilterPanel() {
		POSToggleButton btnFilterByOrderTypeALL = new POSToggleButton(OrderTypeFilter.ALL.toString());
		POSToggleButton btnFilterByDineIn = new POSToggleButton(OrderTypeFilter.DINE_IN.toString());
		POSToggleButton btnFilterByTakeOut = new POSToggleButton(OrderTypeFilter.TAKE_OUT.toString());
		POSToggleButton btnFilterByPickup = new POSToggleButton(OrderTypeFilter.PICKUP.toString());
		POSToggleButton btnFilterByHomeDeli = new POSToggleButton(OrderTypeFilter.HOME_DELIVERY.toString());
		POSToggleButton btnFilterByDriveThru = new POSToggleButton(OrderTypeFilter.DRIVE_THRU.toString());
		POSToggleButton btnFilterByBarTab = new POSToggleButton(OrderTypeFilter.BAR_TAB.toString());

		ButtonGroup orderTypeGroup = new ButtonGroup();
		orderTypeGroup.add(btnFilterByOrderTypeALL);
		orderTypeGroup.add(btnFilterByDineIn);
		orderTypeGroup.add(btnFilterByTakeOut);
		orderTypeGroup.add(btnFilterByPickup);
		orderTypeGroup.add(btnFilterByHomeDeli);
		orderTypeGroup.add(btnFilterByDriveThru);
		orderTypeGroup.add(btnFilterByBarTab);

		OrderTypeFilter orderTypeFilter = TerminalConfig.getOrderTypeFilter();
		switch (orderTypeFilter) {
			case ALL:
				btnFilterByOrderTypeALL.setSelected(true);
				break;

			case DINE_IN:
				btnFilterByDineIn.setSelected(true);
				break;

			case TAKE_OUT:
				btnFilterByTakeOut.setSelected(true);
				break;

			case PICKUP:
				btnFilterByPickup.setSelected(true);
				break;

			case HOME_DELIVERY:
				btnFilterByHomeDeli.setSelected(true);
				break;

			case DRIVE_THRU:
				btnFilterByDriveThru.setSelected(true);
				break;

			case BAR_TAB:
				btnFilterByBarTab.setSelected(true);
				break;
		}

		JPanel filterByOrderPanel = new JPanel(new MigLayout("", "fill, grow", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		filterByOrderPanel.setBorder(new TitledBorder(Messages.getString("SwitchboardView.4"))); //$NON-NLS-1$
		filterByOrderPanel.add(btnFilterByOrderTypeALL);
		filterByOrderPanel.add(btnFilterByDineIn);
		filterByOrderPanel.add(btnFilterByTakeOut);
		filterByOrderPanel.add(btnFilterByPickup);
		filterByOrderPanel.add(btnFilterByHomeDeli);
		filterByOrderPanel.add(btnFilterByDriveThru);
		filterByOrderPanel.add(btnFilterByBarTab);

		getContentPane().add(filterByOrderPanel);

		ActionListener orderTypeFilterHandler = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String actionCommand = e.getActionCommand();
				String filter = actionCommand.replaceAll("\\s", "_"); //$NON-NLS-1$ //$NON-NLS-2$
				TerminalConfig.setOrderTypeFilter(filter);

				//ticketLists.setCurrentRowIndexZero();
				ticketList.updateTicketList();
				ticketLists.updateButtonStatus();
			}
		};

		btnFilterByOrderTypeALL.addActionListener(orderTypeFilterHandler);
		btnFilterByDineIn.addActionListener(orderTypeFilterHandler);
		btnFilterByTakeOut.addActionListener(orderTypeFilterHandler);
		btnFilterByPickup.addActionListener(orderTypeFilterHandler);
		btnFilterByHomeDeli.addActionListener(orderTypeFilterHandler);
		btnFilterByDriveThru.addActionListener(orderTypeFilterHandler);
		btnFilterByBarTab.addActionListener(orderTypeFilterHandler);
	}

	private void updateButton() {
		PaymentStatusFilter paymentStatusFilter = TerminalConfig.getPaymentStatusFilter();
		if (paymentStatusFilter.name().equals("OPEN")) {
			btnFilterByOpenStatus.setSelected(true);
		}
		else if (paymentStatusFilter.name().equals("PAID")) {
			btnFilterByPaidStatus.setSelected(true);
		}
		else if (paymentStatusFilter.name().equals("CLOSE")) {
			btnFilterByUnPaidStatus.setSelected(true);
		}
	}
}
