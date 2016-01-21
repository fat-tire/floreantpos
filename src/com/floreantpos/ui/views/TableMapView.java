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
 * SwitchboardView.java
 *
 * Created on August 14, 2006, 11:45 PM
 */

package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.extension.ExtensionManager;
import com.floreantpos.extension.FloorLayoutPlugin;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.main.Application;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.PasswordEntryDialog;
import com.floreantpos.ui.dialog.TableSelectionView;
import com.floreantpos.ui.views.order.CustomOrderServiceExtension;
import com.floreantpos.ui.views.order.DefaultOrderServiceExtension;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.ui.views.order.RootView;
import com.floreantpos.ui.views.order.ViewPanel;
import com.floreantpos.util.TicketAlreadyExistsException;

/**
 * 
 * @author MShahriar
 */
public class TableMapView extends ViewPanel {

	public final static String VIEW_NAME = "TABLE_MAP"; //$NON-NLS-1$

	public static POSToggleButton btnAttach;
	public static POSToggleButton btnDetach;
	public static POSToggleButton btnTransfer;
	public static PosButton btnDone;

	private ButtonGroup btnGroup;

	private PosButton btnRefresh;

	private TableSelectionView tableView;
	private JPanel tableSelectorPanel;

	private OrderServiceExtension orderServiceExtension;
	private static TableMapView instance;

	private TableMapView() {
		initComponents();

		orderServiceExtension = (OrderServiceExtension) ExtensionManager.getPlugin(OrderServiceExtension.class);

		if (orderServiceExtension == null) {
			orderServiceExtension = new DefaultOrderServiceExtension();
		}

		applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

	}

	private void initComponents() {
		setLayout(new java.awt.BorderLayout(10, 10));

		TitledBorder titledBorder1 = BorderFactory.createTitledBorder(null, POSConstants.DINE_IN_BUTTON_TEXT, TitledBorder.CENTER,
				TitledBorder.DEFAULT_POSITION);
		TitledBorder titledBorder2 = BorderFactory.createTitledBorder(null, "-", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION); //$NON-NLS-1$

		JPanel centerPanel = new JPanel(new java.awt.BorderLayout(5, 5));

		tableSelectorPanel = new JPanel(new java.awt.BorderLayout(5, 5));
		tableSelectorPanel.setBorder(new CompoundBorder(titledBorder1, new EmptyBorder(2, 2, 2, 2)));

		tableView = new TableSelectionView();

		FloorLayoutPlugin floorLayoutPlugin = (FloorLayoutPlugin) ExtensionManager.getPlugin(FloorLayoutPlugin.class);
		if (floorLayoutPlugin == null) {
			tableSelectorPanel.add(tableView, java.awt.BorderLayout.CENTER);
		}

		JPanel rightPanel = new JPanel(new BorderLayout(20, 20));
		rightPanel.setBorder(new CompoundBorder(titledBorder2, new EmptyBorder(2, 2, 6, 2)));

		JPanel actionBtnPanel = new JPanel(new MigLayout("ins 2 2 0 2, hidemode 3, flowy", "grow", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		btnGroup = new ButtonGroup();

		btnAttach = new POSToggleButton(Messages.getString("TableMapView.1")); //$NON-NLS-1$
		btnDetach = new POSToggleButton(Messages.getString("TableMapView.2")); //$NON-NLS-1$
		btnTransfer = new POSToggleButton(Messages.getString("TableMapView.3")); //$NON-NLS-1$
		btnDone = new PosButton(Messages.getString("TableMapView.4")); //$NON-NLS-1$

		btnDone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (btnAttach.isSelected() && tableView.getTables().size() > 0) {
					doCreateNewTicket();
				}
			}
		});

		btnAttach.setIcon(new ImageIcon(getClass().getResource("/images/plus.png"))); //$NON-NLS-1$
		btnDetach.setIcon(new ImageIcon(getClass().getResource("/images/minus2.png"))); //$NON-NLS-1$
		//btnTransfer.setIcon(new ImageIcon(getClass().getResource("/images/transfer.png")));

		btnGroup.add(btnAttach);
		btnGroup.add(btnDetach);
		btnGroup.add(btnTransfer);
		btnGroup.add(btnDone);

		actionBtnPanel.add(btnAttach, "grow"); //$NON-NLS-1$
		actionBtnPanel.add(btnDetach, "grow"); //$NON-NLS-1$
		//actionBtnPanel.add(btnTransfer, "grow");
		actionBtnPanel.add(btnDone, "grow"); //$NON-NLS-1$

		rightPanel.add(actionBtnPanel);

		btnRefresh = new PosButton(POSConstants.REFRESH);
		btnRefresh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateTableView();
				btnGroup.clearSelection();

			}
		});
		rightPanel.add(btnRefresh, BorderLayout.SOUTH);

		centerPanel.add(rightPanel, java.awt.BorderLayout.EAST);
		centerPanel.add(tableSelectorPanel, java.awt.BorderLayout.CENTER);

		add(centerPanel, java.awt.BorderLayout.CENTER);
	}

	public void doCreateNewTicket() {
		try {
			List<ShopTable> selectedTables = tableView.getTables();
			updateTableView();

			OrderServiceExtension orderService = new CustomOrderServiceExtension(selectedTables);
			orderService.createNewTicket(OrderType.DINE_IN);

		} catch (TicketAlreadyExistsException e) {

			int option = JOptionPane.showOptionDialog(Application.getPosWindow(), POSConstants.EDIT_TICKET_CONFIRMATION, POSConstants.CONFIRM,
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			if (option == JOptionPane.YES_OPTION) {
				editTicket(e.getTicket());
				return;
			}
		}
	}

	public void editTicket(Ticket ticket) {
		if (ticket.isPaid()) {
			POSMessageDialog.showMessage(this, Messages.getString("SwitchboardView.14")); //$NON-NLS-1$
			return;
		}

		Ticket ticketToEdit = TicketDAO.getInstance().loadFullTicket(ticket.getId());

		OrderView.getInstance().setCurrentTicket(ticketToEdit);
		RootView.getInstance().showView(OrderView.VIEW_NAME);
		OrderView.getInstance().getTicketView().getTxtSearchItem().requestFocus();
	}

	public boolean editTicket(Integer shopTableNumber) {
		List<Ticket> openTickets = TicketDAO.getInstance().findOpenTickets();
		Integer ticketId = null;
		for (Ticket ticket : openTickets) {
			if (ticket.getTableNumbers().contains(shopTableNumber)) {
				ticketId = ticket.getId();
				if (ticketId == null) {
					return false;
				}
				UserPermission requiredPermission = UserPermission.EDIT_TICKET;
				User user = Application.getCurrentUser();

				if (requiredPermission == null) {
					return false;
				}

				if (!user.hasPermission(requiredPermission)) {
					String password = PasswordEntryDialog.show(Application.getPosWindow(), Messages.getString("PosAction.0")); //$NON-NLS-1$
					if (StringUtils.isEmpty(password)) {
						return false;
					}

					User user2 = UserDAO.getInstance().findUserBySecretKey(password);
					if (user2 == null) {
						POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("PosAction.1")); //$NON-NLS-1$
						return false;
					}
					else if (!user2.hasPermission(requiredPermission)) {
						POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("PosAction.2")); //$NON-NLS-1$
						return false;
					}
				}
			}
		}

		Ticket ticketToEdit = null;
		if (ticketId != null) {
			ticketToEdit = TicketDAO.getInstance().loadFullTicket(ticketId);
		}
		else {
			return false;
		}
		OrderView.getInstance().setCurrentTicket(ticketToEdit);
		RootView.getInstance().showView(OrderView.VIEW_NAME);
		OrderView.getInstance().getTicketView().getTxtSearchItem().requestFocus();
		return true;
	}

	public synchronized void updateTableView() {
		tableView.redererTable();
	}

	public static TableMapView getInstance() {
		if (instance == null) {
			instance = new TableMapView();
		}

		return instance;
	}

	public JPanel getTableSelectorPanel() {
		return tableSelectorPanel;
	}

	@Override
	public String getViewName() {
		return VIEW_NAME;
	}

}