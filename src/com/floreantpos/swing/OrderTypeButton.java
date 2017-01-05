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
package com.floreantpos.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.PosLog;
import com.floreantpos.bo.ui.explorer.QuickMaintenanceExplorer;
import com.floreantpos.customer.CustomerSelectorDialog;
import com.floreantpos.customer.CustomerSelectorFactory;
import com.floreantpos.extension.OrderServiceFactory;
import com.floreantpos.main.Application;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.UserType;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.tableselection.TableSelectorDialog;
import com.floreantpos.ui.tableselection.TableSelectorFactory;
import com.floreantpos.ui.views.order.RootView;
import com.floreantpos.util.TicketAlreadyExistsException;

public class OrderTypeButton extends PosButton implements ActionListener {
	private OrderType orderType;

	public OrderTypeButton() {
		super("");
	}

	public OrderTypeButton(OrderType orderType) {
		super();
		this.orderType = orderType;
		if (orderType != null) {
			if (orderType.getId() == null) {
				setIcon(IconFactory.getIcon("/ui_icons/", "add+user.png"));
			}
			else
				setText(orderType.name());
		}
		else {
			setText(POSConstants.TAKE_OUT_BUTTON_TEXT);
		}
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!hasPermission()) {
			POSMessageDialog.showError("You do not have permission to create order");
			return;
		}
		if (RootView.getInstance().isMaintenanceMode()) {
			QuickMaintenanceExplorer.quickMaintain(orderType);
			return;
		}
		/*if (orderType.isBarTab()) {
			new NewBarTabAction(orderType, Application.getPosWindow()).actionPerformed(e);
		}
		else */
		if (orderType.isShowTableSelection()) {
			TableSelectorDialog dialog = TableSelectorFactory.createTableSelectorDialog(orderType);
			dialog.setCreateNewTicket(true);
			dialog.updateView(true);
			dialog.openUndecoratedFullScreen();

			if (!dialog.isCanceled()) {
				return;
			}
		}
		else if (orderType.isRequiredCustomerData()) {
			CustomerSelectorDialog dialog = CustomerSelectorFactory.createCustomerSelectorDialog(orderType);
			dialog.setCreateNewTicket(true);
			dialog.updateView(true);
			dialog.openUndecoratedFullScreen();

			if (!dialog.isCanceled()) {
				return;
			}
		}
		else {
			try {
				OrderServiceFactory.getOrderService().createNewTicket(orderType, null, null);
			} catch (TicketAlreadyExistsException e1) {
				PosLog.error(getClass(), e1);
			}
		}
	}

	private boolean hasPermission() {
		User user = Application.getCurrentUser();
		UserType userType = user.getType();
		if (userType != null) {
			Set<UserPermission> permissions = userType.getPermissions();
			for (UserPermission permission : permissions) {
				if (permission.equals(UserPermission.CREATE_TICKET)) {
					return true;
				}
			}
		}
		return false;
	}
}
