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

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.demo.KitchenDisplayView;
import com.floreantpos.extension.ExtensionManager;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.extension.OrderServiceFactory;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.dao.OrderTypeDAO;
import com.floreantpos.ui.HeaderPanel;
import com.floreantpos.ui.views.CustomerView;
import com.floreantpos.ui.views.IView;
import com.floreantpos.ui.views.LoginView;
import com.floreantpos.ui.views.SwitchboardOtherFunctionsView;
import com.floreantpos.ui.views.SwitchboardView;
import com.floreantpos.ui.views.TableMapView;
import com.floreantpos.ui.views.payment.SettleTicketDialog;
import com.floreantpos.util.TicketAlreadyExistsException;

public class RootView extends com.floreantpos.swing.TransparentPanel {
	private CardLayout cards = new CardLayout();

	private HeaderPanel headerPanel = new HeaderPanel();
	private JPanel contentPanel = new JPanel(cards);
	private LoginView loginScreen;
	private SettleTicketDialog paymentView;
	private String currentViewName;
	private IView homeView;

	private Map<String, IView> views = new HashMap<String, IView>();

	private boolean maintenanceMode;

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
	}
	
	public void initializeViews() {
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

		currentViewName = viewName;
		cards.show(contentPanel, viewName);

		headerPanel.updateHomeView(!homeView.getViewName().equals(currentViewName));

		headerPanel.updateOthersFunctionsView(!currentViewName.equals(SwitchboardOtherFunctionsView.VIEW_NAME));
		headerPanel.updateSwitchBoardView(!currentViewName.equals(SwitchboardView.VIEW_NAME));
	}

	public void showView(IView view) {
		if (!views.containsKey(view.getViewName())) {
			addView(view);
		}
		currentViewName = view.getViewName();
		showView(currentViewName);
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

	public boolean isMaintenanceMode() {
		return maintenanceMode;
	}

	public void setMaintenanceMode(boolean b) {
		this.maintenanceMode = b;
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

	public String getCurrentViewName() {

		return currentViewName;
	}

	public IView getCurrentView() {
		return views.get(currentViewName);
	}

	public void showDefaultView() {

		String defaultViewName = TerminalConfig.getDefaultView();

		if (defaultViewName.equals(SwitchboardOtherFunctionsView.VIEW_NAME)) { //$NON-NLS-1$
			setAndShowHomeScreen(SwitchboardOtherFunctionsView.getInstance());
		}
		else if (defaultViewName.equals(KitchenDisplayView.VIEW_NAME)) {
			if (!hasView(KitchenDisplayView.getInstance())) {
				addView(KitchenDisplayView.getInstance());
			}

			headerPanel.setVisible(false);
			setAndShowHomeScreen(KitchenDisplayView.getInstance());
		}
		else if (defaultViewName.equals(SwitchboardView.VIEW_NAME)) {
			if (loginScreen.isBackOfficeLogin()) {
				showBackOffice();
			}
			setAndShowHomeScreen(SwitchboardView.getInstance());
		}
		else {
			OrderType orderType = OrderTypeDAO.getInstance().findByName(defaultViewName);

			if (orderType.isShowTableSelection()) {
				TableMapView tableMapView = TableMapView.getInstance(orderType);
				tableMapView.updateView();
				setAndShowHomeScreen(tableMapView);
			}
			else if (orderType.isRequiredCustomerData()) {
				OrderServiceExtension orderServicePlugin = (OrderServiceExtension) ExtensionManager.getPlugin(OrderServiceExtension.class);
				if (orderServicePlugin != null) {
					if (orderType.isDelivery()) {
						setAndShowHomeScreen(orderServicePlugin.getDeliveryDispatchView(orderType));
					}
					else {
						CustomerView customerView = CustomerView.getInstance(orderType);
						customerView.updateView();
						setAndShowHomeScreen(customerView);
					}
				}
				else {
					CustomerView customerView = CustomerView.getInstance(orderType);
					customerView.updateView();
					setAndShowHomeScreen(customerView);
				}
			}
			else {
				try {
					homeView = OrderView.getInstance();
					OrderServiceFactory.getOrderService().createNewTicket(orderType, null, null);

				} catch (TicketAlreadyExistsException e1) {
				}
			}
		}

	}

	/**
	 * @return the loginView
	 */
	public IView getHomeView() {
		return homeView;
	}

	public void setAndShowHomeScreen(IView homeScreen) {
		homeView = homeScreen;
		showHomeScreen();

	}

	public void showHomeScreen() {
		showView(getHomeView());
	}

	public void showBackOffice() {

		BackOfficeWindow window = com.floreantpos.util.POSUtil.getBackOfficeWindow();
		if (window == null) {
			window = new BackOfficeWindow();
		}
		window.setVisible(true);
		window.toFront();

		loginScreen.setBackOfficeLogin(false);
	}
}
