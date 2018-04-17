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

import java.awt.Component;
import java.util.Calendar;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JOptionPane;

import com.floreantpos.Messages;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.customer.CustomerSelector;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.main.Application;
import com.floreantpos.model.Customer;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.IView;
import com.floreantpos.util.DrawerUtil;
import com.floreantpos.util.POSUtil;
import com.floreantpos.util.PosGuiUtil;
import com.floreantpos.util.TicketAlreadyExistsException;

public class DefaultOrderServiceExtension extends OrderServiceExtension {

	@Override
	public String getProductName() {
		return Messages.getString("DefaultOrderServiceExtension.0"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return Messages.getString("DefaultOrderServiceExtension.1"); //$NON-NLS-1$
	}

	@Override
	public void initUI() {
	}

	@Override
	public void createNewTicket(OrderType ticketType, List<ShopTable> selectedTables, Customer customer) throws TicketAlreadyExistsException {
		int numberOfGuests = 0;

		if (ticketType.isShowGuestSelection()) {
			numberOfGuests = PosGuiUtil.captureGuestNumber();
		}
		
		if (TerminalConfig.isActiveCustomerDisplay()) {
			DrawerUtil.setCustomerDisplayMessage(TerminalConfig.getCustomerDisplayPort(), "Welcome");
		}

		if (ticketType.isRequiredCustomerData() && customer == null) {
			customer = PosGuiUtil.captureCustomer(ticketType);
			if (customer == null) {
				return;
			}
		}

		Application application = Application.getInstance();

		Ticket ticket = new Ticket();
		ticket.setPriceIncludesTax(application.isPriceIncludesTax());
		ticket.setOrderType(ticketType);
		ticket.setNumberOfGuests(numberOfGuests);
		ticket.setCustomer(customer);
		if (customer != null)
			ticket.setDeliveryAddress(customer.getAddress());
		ticket.setTerminal(application.getTerminal());
		ticket.setOwner(Application.getCurrentUser());
		ticket.setShift(application.getCurrentShift());

		if (selectedTables != null) {
			for (ShopTable shopTable : selectedTables) {
				shopTable.setServing(true);
				ticket.addTable(shopTable.getTableNumber());
			}
		}

		Calendar currentTime = Calendar.getInstance();
		ticket.setCreateDate(currentTime.getTime());
		ticket.setCreationHour(currentTime.get(Calendar.HOUR_OF_DAY));

		OrderView.getInstance().setCurrentTicket(ticket);
		RootView.getInstance().showView(OrderView.VIEW_NAME);
		OrderView.getInstance().getTicketView().getTxtSearchItem().requestFocus();
	}

	@Override
	public void setCustomerToTicket(int ticketId) {
	}

	public void setDeliveryDate(int ticketId) {
	}

	@Override
	public void assignDriver(int ticketId) {

	};

	@Override
	public boolean finishOrder(int ticketId) {
		Ticket ticket = TicketDAO.getInstance().get(ticketId);

		//		if (ticket.getType() == TicketType.DINE_IN) {
		//			POSMessageDialog.showError(Application.getPosWindow(), "Please select tickets of type HOME DELIVERY or PICKUP or DRIVE THRU");
		//			return false;
		//		}

		int due = (int) POSUtil.getDouble(ticket.getDueAmount());
		if (due != 0) {
			POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("DefaultOrderServiceExtension.2")); //$NON-NLS-1$
			return false;
		}

		int option = JOptionPane
				.showOptionDialog(
						Application.getPosWindow(),
						Messages.getString("DefaultOrderServiceExtension.3") + ticket.getId() + Messages.getString("DefaultOrderServiceExtension.4"), Messages.getString("DefaultOrderServiceExtension.5"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

		if (option != JOptionPane.OK_OPTION) {
			return false;
		}

		OrderController.closeOrder(ticket);

		return true;
	}

	@Override
	public void createCustomerMenu(JMenu menu) {
	}

	@Override
	public void initBackoffice() {

	}

	@Override
	public void initConfigurationView(JDialog dialog) {

	}

	@Override
	public String getId() {
		return "DefaultOrderServiceExtension"; //$NON-NLS-1$
	}

	@Override
	public String getSecurityCode() {
		return "-400343452";
	}

	@Override
	public IView getDeliveryDispatchView(OrderType orderType) {
		return null;
	}

	@Override
	public CustomerSelector createNewCustomerSelector() {
		return null;
	}

	@Override
	public CustomerSelector createCustomerSelectorView() {
		return null;
	}

	@Override
	public void openDeliveryDispatchDialog(OrderType orderType) {

	}

	@Override
	public IView getDriverView() {
		return null;
	}

	@Override
	public List<AbstractAction> getSpecialFunctionActions() {
		return null;
	}

	@Override
	public String getProductVersion() {
		return null;
	}

	@Override
	public Component getParent() {
		return null;
	}

	@Override
	public boolean requireLicense() {
		return false;
	}

	@Override
	public void showDeliveryInfo(OrderType orderType, Customer customer) {

	}
}