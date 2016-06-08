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
import com.floreantpos.model.User;
import com.floreantpos.ui.views.IView;
import com.floreantpos.util.TicketAlreadyExistsException;

public interface OrderServiceExtension extends FloreantPlugin {
	String getName();
	String getDescription();
	
	void init();
	void createNewTicket(OrderType ticketType, List<ShopTable> selectedTables, Customer selectedCustomer) throws TicketAlreadyExistsException;
	void setCustomerToTicket(int ticketId);
	void setDeliveryDate(int ticketId);
	void assignDriver(int ticketId);
	boolean finishOrder(int ticketId);
	void createCustomerMenu(JMenu menu);
	CustomerSelector createNewCustomerSelector();
	CustomerSelector createCustomerSelectorView();
	IView getDeliveryDispatchView(OrderType orderType);
	IView getDriverView();
	void openDeliveryDispatchDialog(OrderType orderType);
	
}
