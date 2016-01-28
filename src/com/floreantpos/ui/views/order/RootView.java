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

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.demo.KitchenDisplayView;
import com.floreantpos.model.OrderType;
import com.floreantpos.ui.HeaderPanel;
import com.floreantpos.ui.views.IView;
import com.floreantpos.ui.views.LoginView;
import com.floreantpos.ui.views.SwitchboardOtherFunctionsView;
import com.floreantpos.ui.views.SwitchboardView;
import com.floreantpos.ui.views.TableMapView;
import com.floreantpos.ui.views.payment.SettleTicketDialog;
import com.floreantpos.util.OrderUtil;

public class RootView extends com.floreantpos.swing.TransparentPanel {
	private CardLayout cards = new CardLayout();

	private HeaderPanel headerPanel = new HeaderPanel();
	private JPanel contentPanel = new JPanel(cards);
	private LoginView loginScreen;
	private SettleTicketDialog paymentView;
	private String currentView;

	private Map<String, IView> views = new HashMap<String, IView>();

	private static RootView instance;

	private RootView() {
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(3, 3, 3, 3));

		initView();

	}

	private void initView() {
		headerPanel.setVisible(false);
		add(headerPanel, BorderLayout.NORTH);

		add(contentPanel);
		loginScreen = LoginView.getInstance();
		addView(loginScreen);
	}

	public void addView(IView iView) {
		views.put(iView.getViewName(), iView);
		contentPanel.add(iView.getViewName(), iView.getViewComponent());
	}

	public void showView(String viewName) {
		if (LoginView.VIEW_NAME.equals(viewName)) {
			headerPanel.setVisible(false);
		}
		else {
			headerPanel.setVisible(true);
		}

		currentView = viewName;
		cards.show(contentPanel, viewName);
	}

	public void showView(IView view) {
		if (!views.containsKey(view.getViewName())) {
			addView(view);
		}
		showView(view.getViewName());
	}

	public boolean hasView(String viewName) {
		return views.containsKey(viewName);
	}

	public boolean hasView(IView view) {
		return views.containsKey(view.getViewName());
	}

	public OrderView getOrderView() {
		return (OrderView) views.get(OrderView.VIEW_NAME);
	}

	public LoginView getLoginScreen() {
		return loginScreen;
	}

	//	public SwitchboardView getSwitchboadView() {
	//		return switchboardView;
	//	}
	//
	//	public void setSwitchboardView(SwitchboardView switchboardView) {
	//		this.switchboardView = switchboardView;
	//	}

	public synchronized static RootView getInstance() {
		if (instance == null) {
			instance = new RootView();
		}
		return instance;
	}

	public SettleTicketDialog getPaymentView() {
		return paymentView;
	}

	public HeaderPanel getHeaderPanel() {
		return headerPanel;
	}

	public String getCurrentView() {

		return currentView;
	}

	public void showDefaultView() {
		String defaultViewName = TerminalConfig.getDefaultView();

		if (defaultViewName.equals(OrderType.DINE_IN.toString())) {
			showView(TableMapView.getInstance());
			TableMapView.getInstance().updateTableView();
		}
		else if (defaultViewName.equals(OrderType.TAKE_OUT.toString())) {
			OrderUtil.createNewTakeOutOrder(OrderType.TAKE_OUT);
			showView(OrderView.getInstance());
		}
		else if (defaultViewName.equals(OrderType.RETAIL.toString())) {
			OrderUtil.createNewTakeOutOrder(OrderType.RETAIL);
			showView(OrderView.getInstance());
		}
		else if (defaultViewName.equals(OrderType.FOR_HERE.toString())) {
			OrderUtil.createNewTakeOutOrder(OrderType.FOR_HERE);
			showView(OrderView.getInstance());
		}
		else if (defaultViewName.equals(SwitchboardOtherFunctionsView.VIEW_NAME)) { //$NON-NLS-1$
			showView(SwitchboardOtherFunctionsView.getInstance());
		}
		else if (defaultViewName.equals(OrderType.BAR_TAB.toString())) {
			showView(SwitchboardView.getInstance());
		}
		else if (defaultViewName.equals(OrderType.DRIVE_THRU.toString())) {
			showView(OrderView.getInstance());
			OrderUtil.createNewTakeOutOrder(OrderType.DRIVE_THRU);
		}
		else if (defaultViewName.equals(OrderType.HOME_DELIVERY)) {
			showView(SwitchboardView.getInstance());
			SwitchboardView.getInstance().doHomeDelivery(OrderType.HOME_DELIVERY);
		}
		else if (defaultViewName.equals(OrderType.PICKUP.toString())) {
			showView(OrderView.getInstance());
			OrderUtil.createNewTakeOutOrder(OrderType.PICKUP);
		}
		else if (defaultViewName.equals(KitchenDisplayView.VIEW_NAME)) {
			if (!hasView(KitchenDisplayView.getInstance())) {
				addView(KitchenDisplayView.getInstance());
			}
			showView(KitchenDisplayView.VIEW_NAME);
		}
		else if (defaultViewName.equals(SwitchboardView.VIEW_NAME)) {
			showView(SwitchboardView.getInstance());
		}
	}
}
