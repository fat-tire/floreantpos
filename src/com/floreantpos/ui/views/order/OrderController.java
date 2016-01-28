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
package com.floreantpos.ui.views.order;

import java.util.Date;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.actions.SettleTicketAction;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.ActionHistory;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.ActionHistoryDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.order.actions.CategorySelectionListener;
import com.floreantpos.ui.views.order.actions.GroupSelectionListener;
import com.floreantpos.ui.views.order.actions.ItemSelectionListener;
import com.floreantpos.ui.views.order.actions.OrderListener;
import com.floreantpos.ui.views.order.modifier.ModifierSelectionDialog;
import com.floreantpos.ui.views.order.modifier.ModifierSelectionModel;
import com.floreantpos.util.OrderUtil;

public class OrderController implements OrderListener, CategorySelectionListener, GroupSelectionListener, ItemSelectionListener {
	private OrderView orderView;

	public OrderController(OrderView orderView) {
		this.orderView = orderView;

		orderView.getCategoryView().addCategorySelectionListener(this);
		orderView.getGroupView().addGroupSelectionListener(this);
		orderView.getItemView().addItemSelectionListener(this);
		orderView.getTicketView().addOrderListener(this);
	}

	public void categorySelected(MenuCategory foodCategory) {
		orderView.showView(GroupView.VIEW_NAME);
		orderView.getGroupView().setMenuCategory(foodCategory);
		orderView.getTicketView().getTxtSearchItem().requestFocus();
	}

	public void groupSelected(MenuGroup foodGroup) {
		orderView.showView(MenuItemView.VIEW_NAME);
		orderView.getItemView().setMenuGroup(foodGroup);
		orderView.getTicketView().getTxtSearchItem().requestFocus();
	}

	public void itemSelected(MenuItem menuItem) {
		MenuItemDAO dao = new MenuItemDAO();
		menuItem = dao.initialize(menuItem);

		TicketItem ticketItem = menuItem.convertToTicketItem();
		ticketItem.setTicket(orderView.getTicketView().getTicket());

		if (menuItem.hasMandatoryModifiers()) {
			ModifierSelectionDialog dialog = new ModifierSelectionDialog(new ModifierSelectionModel(ticketItem, menuItem));
			dialog.open();
			if (!dialog.isCanceled()) {
				orderView.getTicketView().addTicketItem(ticketItem);
			}
		}
		else {
			orderView.getTicketView().addTicketItem(ticketItem);
		}
	}

	public void itemSelectionFinished(MenuGroup parent) {
		MenuCategory menuCategory = parent.getParent();
		GroupView groupView = orderView.getGroupView();
		if (!menuCategory.equals(groupView.getMenuCategory())) {
			groupView.setMenuCategory(menuCategory);
		}
		orderView.showView(GroupView.VIEW_NAME);
	}

	public void payOrderSelected(Ticket ticket) {
		if (!new SettleTicketAction(ticket.getId()).execute()) {
			return;
		}

		if (TerminalConfig.isCashierMode()) {
			String message = Messages.getString("OrderController.0") + ticket.getId() + Messages.getString("OrderController.1"); //$NON-NLS-1$ //$NON-NLS-2$
			if (ticket.isPaid()) {
				message = Messages.getString("OrderController.2") + ticket.getId() + Messages.getString("OrderController.3"); //$NON-NLS-1$ //$NON-NLS-2$
			}

			OrderUtil.createNewTakeOutOrder(OrderType.TAKE_OUT);
			CashierModeNextActionDialog dialog = new CashierModeNextActionDialog(message);
			dialog.open();
		}
		else {
			RootView.getInstance().showDefaultView();
		}
	}

	public static void openModifierDialog(TicketItem ticketItem) {
		try {
			MenuItem menuItem = MenuItemDAO.getInstance().get(ticketItem.getItemId());
			menuItem = MenuItemDAO.getInstance().initialize(menuItem);

			ModifierSelectionDialog dialog = new ModifierSelectionDialog(new ModifierSelectionModel(ticketItem, menuItem));
			dialog.open();
			OrderView.getInstance().getTicketView().updateView();
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage(), e);
		}
	}

	public static void openModifierDialog(TicketItemModifier ticketItemModifier) {
		try {
			TicketItem ticketItem = ticketItemModifier.getTicketItem();

			//for backward compatibility
			if (ticketItem == null) {
				ticketItem = ticketItemModifier.getParent().getParent();
			}
			MenuItem menuItem = MenuItemDAO.getInstance().get(ticketItem.getItemId());
			menuItem = MenuItemDAO.getInstance().initialize(menuItem);

			ModifierSelectionDialog dialog = new ModifierSelectionDialog(new ModifierSelectionModel(ticketItem, menuItem));
			dialog.open();
			OrderView.getInstance().getTicketView().updateView();
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage(), e);
		}
	}

	//VERIFIED
	public synchronized static void saveOrder(Ticket ticket) {
		if (ticket == null)
			return;

		boolean newTicket = ticket.getId() == null;

		TicketDAO ticketDAO = new TicketDAO();
		ticketDAO.saveOrUpdate(ticket);

		//			save the action
		ActionHistoryDAO actionHistoryDAO = ActionHistoryDAO.getInstance();
		User user = Application.getCurrentUser();

		if (newTicket) {
			ShopTableDAO.getInstance().occupyTables(ticket);

			actionHistoryDAO.saveHistory(user, ActionHistory.NEW_CHECK, POSConstants.RECEIPT_REPORT_TICKET_NO_LABEL + ":" + ticket.getId()); //$NON-NLS-1$
		}
		else {
			actionHistoryDAO.saveHistory(user, ActionHistory.EDIT_CHECK, POSConstants.RECEIPT_REPORT_TICKET_NO_LABEL + ":" + ticket.getId()); //$NON-NLS-1$
		}
	}

	public synchronized static void closeOrder(Ticket ticket) {
		ticket.setClosed(true);
		ticket.setClosingDate(new Date());

		TicketDAO ticketDAO = new TicketDAO();
		ticketDAO.saveOrUpdate(ticket);

		User driver = ticket.getAssignedDriver();
		if (driver != null) {
			driver.setAvailableForDelivery(true);
			UserDAO.getInstance().saveOrUpdate(driver);
		}
	}
}
