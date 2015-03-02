package com.floreantpos.ui.views.order;

import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.border.EmptyBorder;

import com.floreantpos.demo.KitchenDisplayView;
import com.floreantpos.ui.views.LoginView;
import com.floreantpos.ui.views.SwitchboardView;
import com.floreantpos.ui.views.payment.SettleTicketDialog;

public class RootView extends com.floreantpos.swing.TransparentPanel {
	private CardLayout layout = new CardLayout();
	
	private LoginView loginScreen;
	private SwitchboardView switchboardView;
	private OrderView orderView;
	private SettleTicketDialog paymentView;

	
	private static RootView instance;
	
	private RootView() {
		setLayout(layout);
		setBorder(new EmptyBorder(3,3,3,3));
		
		loginScreen = new LoginView();
		addView(LoginView.VIEW_NAME, loginScreen);
		
		switchboardView = new SwitchboardView();
		addView(SwitchboardView.VIEW_NAME, switchboardView);
		
		orderView = OrderView.getInstance();
		orderView.init();
		addView(OrderView.VIEW_NAME, orderView);
		//addView("KitchenView", new KitchenDisplayView());
		
		//paymentView = SettleTicketView.getInstance();
		//addView(SettleTicketView.VIEW_NAME, paymentView);
		
		showView(LoginView.VIEW_NAME);
	}
	
	public void addView(String viewName, Component view) {
		add(view, viewName);
	}
	
	public void showView(String viewName) {
		layout.show(this, viewName);
	}
	
	public OrderView getOrderView() {
		return orderView;
	}
	
	public void setOrderView(OrderView orderView) {
		this.orderView = orderView;
	}
	
	public LoginView getLoginScreen() {
		return loginScreen;
	}

	public SwitchboardView getSwitchboadView() {
		return switchboardView;
	}

	public void setSwitchboardView(SwitchboardView switchboardView) {
		this.switchboardView = switchboardView;
	}

	public synchronized static RootView getInstance() {
		if(instance == null) {
			instance = new RootView();
		}
		return instance;
	}

	public SettleTicketDialog getPaymentView() {
		return paymentView;
	}
}
