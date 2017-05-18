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
package com.floreantpos.extension;

import java.util.List;

import javax.swing.JMenu;

import com.floreantpos.customer.CustomerSelector;
import com.floreantpos.model.Customer;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.ShopTable;
import com.floreantpos.ui.views.IView;
import com.floreantpos.util.TicketAlreadyExistsException;

public abstract class OrderServiceExtension extends AbstractFloreantPlugin {
	public abstract String getProductName();

	public abstract String getDescription();

	public abstract void initUI();

	public abstract void createNewTicket(OrderType ticketType, List<ShopTable> selectedTables, Customer selectedCustomer) throws TicketAlreadyExistsException;

	public abstract void setCustomerToTicket(int ticketId);

	public abstract void setDeliveryDate(int ticketId);

	public abstract void assignDriver(int ticketId);

	public abstract boolean finishOrder(int ticketId);

	public abstract void createCustomerMenu(JMenu menu);

	public abstract CustomerSelector createNewCustomerSelector();

	public abstract CustomerSelector createCustomerSelectorView();

	public abstract IView getDeliveryDispatchView(OrderType orderType);

	public abstract IView getDriverView();

	public abstract void openDeliveryDispatchDialog(OrderType orderType);
	
	public abstract void showDeliveryInfo(OrderType orderType, Customer customer);

}
