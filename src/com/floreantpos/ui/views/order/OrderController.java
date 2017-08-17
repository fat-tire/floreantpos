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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.POSConstants;
import com.floreantpos.PosLog;
import com.floreantpos.actions.SettleTicketAction;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.ActionHistory;
import com.floreantpos.model.ITicketItem;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.ActionHistoryDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.ui.dialog.AutomatedWeightInputDialog;
import com.floreantpos.ui.dialog.BasicWeightInputDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.order.actions.CategorySelectionListener;
import com.floreantpos.ui.views.order.actions.GroupSelectionListener;
import com.floreantpos.ui.views.order.actions.ItemSelectionListener;
import com.floreantpos.ui.views.order.actions.OrderListener;
import com.floreantpos.ui.views.order.actions.TicketEditListener;
import com.floreantpos.ui.views.order.modifier.ModifierSelectionDialog;
import com.floreantpos.ui.views.order.modifier.ModifierSelectionModel;
import com.floreantpos.ui.views.order.multipart.PizzaModifierSelectionDialog;

public class OrderController implements OrderListener, CategorySelectionListener, GroupSelectionListener, ItemSelectionListener {
	private OrderView orderView;
	private List<TicketEditListener> ticketEditListenerList;

	public OrderController(OrderView orderView) {
		this.orderView = orderView;

		orderView.getCategoryView().addCategorySelectionListener(this);
		orderView.getGroupView().addGroupSelectionListener(this);
		orderView.getItemView().addItemSelectionListener(this);
		orderView.getTicketView().addOrderListener(this);
		ticketEditListenerList = new ArrayList<TicketEditListener>();
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

		if (orderView.isVisible() && RootView.getInstance().isMaintenanceMode()) {
			return;
		}

		double itemQuantity = 0;
		if (menuItem.isFractionalUnit()) {
			if (TerminalConfig.getScaleActivationValue().equals("cas10")) {
				itemQuantity = AutomatedWeightInputDialog.takeDoubleInput(menuItem.getName(), 1);
			}
			else {
				itemQuantity = BasicWeightInputDialog.takeDoubleInput("Please enter item weight or quantity.", 1);
			}
			if (itemQuantity <= -1) {
				return;
			}

			if (itemQuantity == 0) {
				POSMessageDialog.showError("Unit can not be zero");
				return;
			}
		}

		TicketItem ticketItem = menuItem.convertToTicketItem(orderView.getTicketView().getTicket().getOrderType(), itemQuantity);

		if (!orderView.getTicketView().isStockAvailable(menuItem, ticketItem, -1)) {
			POSMessageDialog.showError("Items are not available in stock");
			//Disable the menu button when stock finish
			orderView.getItemView().disableItemButton(menuItem);
			return;
		}

		ticketItem.setTicket(orderView.getTicketView().getTicket());
		ticketItem.setSeatNumber(orderView.getSelectedSeatNumber());

		if (menuItem.isPizzaType()) {
			PizzaModifierSelectionDialog dialog = new PizzaModifierSelectionDialog(ticketItem, menuItem, false);
			dialog.openFullScreen();

			if (dialog.isCanceled()) {
				return;
			}
		}
		else if (menuItem.hasMandatoryModifiers()) {
			ModifierSelectionDialog dialog = new ModifierSelectionDialog(new ModifierSelectionModel(ticketItem, menuItem));
			dialog.open();
			if (!dialog.isCanceled()) {
				orderView.getTicketView().addTicketItem(ticketItem);
			}
		}
		else {
			orderView.getTicketView().addTicketItem(ticketItem);
		}
		fireTicketItemUpdated(orderView.getTicketView().getTicket(), ticketItem);
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

		// if (TerminalConfig.isCashierMode()) {
		//			String message = Messages.getString("OrderController.0") + ticket.getId() + Messages.getString("OrderController.1"); //$NON-NLS-1$ //$NON-NLS-2$
		// if (ticket.isPaid()) {
		//				message = Messages.getString("OrderController.2") + ticket.getId() + Messages.getString("OrderController.3"); //$NON-NLS-1$ //$NON-NLS-2$
		// }
		//
		// OrderUtil.createNewTakeOutOrder(ticket.getType()); //fix
		// CashierModeNextActionDialog dialog = new
		// CashierModeNextActionDialog(message);
		// dialog.open();
		// }
		// else {
		RootView.getInstance().showDefaultView();
		// }
	}

	public static void openModifierDialog(ITicketItem ticketItemObject) {
		try {
			TicketItem ticketItem = null;
			if (ticketItemObject instanceof TicketItem) {
				ticketItem = ((TicketItem) ticketItemObject);
			}
			else if (ticketItemObject instanceof TicketItemModifier) {
				TicketItemModifier ticketItemModifier = (TicketItemModifier) ticketItemObject;
				ticketItem = ticketItemModifier.getTicketItem();
				if (ticketItem == null) {
					ticketItem = ticketItemModifier.getTicketItem();
				}
			}

			MenuItem menuItem = ticketItem.getMenuItem();

			List<TicketItemModifier> ticketItemModifiers = ticketItem.getTicketItemModifiers();
			if (ticketItemModifiers == null) {
				ticketItemModifiers = new ArrayList<>();
			}
			TicketItem cloneTicketItem = ticketItem.clone(ticketItem);

			boolean pizzaType = ticketItem.isPizzaType();
			if (pizzaType) {
				PizzaModifierSelectionDialog dialog = new PizzaModifierSelectionDialog(cloneTicketItem, menuItem, true);
				dialog.openFullScreen();

				if (dialog.isCanceled()) {
					return;
				}
				TicketItemModifier sizeModifier = cloneTicketItem.getSizeModifier();
				sizeModifier.setTicketItem(ticketItem);
				ticketItem.setSizeModifier(sizeModifier);
				ticketItem.setItemCount(cloneTicketItem.getItemCount());
				ticketItem.setUnitPrice(cloneTicketItem.getUnitPrice());
			}
			else {
				ModifierSelectionDialog dialog = new ModifierSelectionDialog(new ModifierSelectionModel(cloneTicketItem, menuItem));
				dialog.open();

				if (dialog.isCanceled()) {
					return;
				}
			}
			List<TicketItemModifier> addedTicketItemModifiers = cloneTicketItem.getTicketItemModifiers();
			if (addedTicketItemModifiers == null) {
				addedTicketItemModifiers = new ArrayList<>();
			}
			//double defaultSellPortion = menuItem.getDefaultSellPortion();
			ticketItemModifiers.clear();
			for (TicketItemModifier ticketItemModifier : addedTicketItemModifiers) {
				ticketItemModifier.setTicketItem(ticketItem);
				/*if (pizzaType && !ticketItemModifier.isInfoOnly()) {
					ticketItemModifier.setUnitPrice(ticketItemModifier.getUnitPrice() * defaultSellPortion / 100);
				}*/
				ticketItem.addToticketItemModifiers(ticketItemModifier);
			}
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage(), e);
		}
	}

	// VERIFIED
	public synchronized static void saveOrder(Ticket ticket) {
		if (ticket == null)
			return;

		boolean newTicket = ticket.getId() == null;

		TicketDAO ticketDAO = new TicketDAO();
		ticketDAO.saveOrUpdate(ticket);

		// save the action
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
		if (ticket.getOrderType().isCloseOnPaid() || ticket.isPaid()) {
			ticket.setClosed(true);
			ticket.setClosingDate(new Date());
		}

		TicketDAO ticketDAO = new TicketDAO();
		ticketDAO.saveOrUpdate(ticket);

		User driver = ticket.getAssignedDriver();
		if (driver != null) {
			driver.setAvailableForDelivery(true);
			UserDAO.getInstance().saveOrUpdate(driver);
		}
	}

	public synchronized static void closeDeliveryOrders(List<Ticket> tickets) {
		Session session = TicketDAO.getInstance().createNewSession();
		Transaction transaction = null;

		try {

			transaction = session.beginTransaction();

			for (Ticket ticket : tickets) {
				ticket.setClosed(true);
				ticket.setClosingDate(new Date());

				session.saveOrUpdate(ticket);
			}
			transaction.commit();
		} catch (Exception e) {
			PosLog.error(OrderController.class, e.getMessage());
			POSMessageDialog.showError(e.getMessage());

			if (transaction != null)
				transaction.rollback();
		} finally {
			session.close();
		}
	}
	
	public void addTicketUpdateListener(TicketEditListener l) {
		ticketEditListenerList.add(l);
	}

	public void removeTicketUpdateListener(TicketEditListener l) {
		ticketEditListenerList.remove(l);
	}
	
	public void fireTicketItemUpdated(Ticket ticket, TicketItem ticketItem) {
		for (TicketEditListener listener : this.ticketEditListenerList) {
			listener.itemAdded(ticket, ticketItem);
		}
	}

}
