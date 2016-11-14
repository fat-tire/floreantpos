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
package com.floreantpos.customer;

import java.awt.BorderLayout;
import java.awt.HeadlessException;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.main.PosWindow;
import com.floreantpos.model.Customer;
import com.floreantpos.model.Ticket;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;

public class CustomerSelectorDialog extends POSDialog {

	private final CustomerSelector customerSelector;

	public CustomerSelectorDialog(CustomerSelector customerSelector) throws HeadlessException {
		super(Application.getPosWindow(), true);
		this.customerSelector = customerSelector;

		TitlePanel titlePane = new TitlePanel();
		titlePane.setTitle(Messages.getString("CustomerSelectorDialog.0")); //$NON-NLS-1$

		getContentPane().add(titlePane, BorderLayout.NORTH);
		getContentPane().add(customerSelector);

		PosWindow window = Application.getPosWindow();
		setSize(window.getSize());
		setLocation(window.getLocation());
	}

	public void setCreateNewTicket(boolean createNewTicket) {
		customerSelector.setCreateNewTicket(createNewTicket);
	}

	public void updateView(boolean update) {
		customerSelector.updateView(update);
	}

	public Customer getSelectedCustomer() {
		return customerSelector.getSelectedCustomer();
	}

	public void setTicket(Ticket thisTicket) {
		customerSelector.setTicket(thisTicket);
	}

	public void setCustomer(Customer customer) {
		customerSelector.setCustomer(customer); 
	}

	public void setCallerId(String callerId) {
		customerSelector.setCallerId(callerId); 
	}
}