package com.floreantpos.ui.views.order;

import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.border.EmptyBorder;

import com.floreantpos.ui.views.LoginScreen;
import com.floreantpos.ui.views.OpenTicketView;
import com.floreantpos.ui.views.SettleTicketView;
import com.floreantpos.ui.views.SwitchboardView;

public class RootView extends com.floreantpos.swing.TransparentPanel {
	private CardLayout layout = new CardLayout();
	
	private LoginScreen loginScreen;
	private SwitchboardView switchboardView;
	private OrderView orderView;
	private SettleTicketView paymentView;
	private OpenTicketView openTicketView;

	
	private static RootView instance;
	
	private RootView() {
		setLayout(layout);
		setBorder(new EmptyBorder(3,3,3,3));
		
		loginScreen = new LoginScreen();
		addView(LoginScreen.VIEW_NAME, loginScreen);
		
		switchboardView = new SwitchboardView();
		addView(SwitchboardView.VIEW_NAME, switchboardView);
		
		orderView = OrderView.getInstance();
		orderView.init();
		addView(OrderView.VIEW_NAME, orderView);
		
		paymentView = SettleTicketView.getInstance();
		addView(SettleTicketView.VIEW_NAME, paymentView);
		
		openTicketView = new OpenTicketView();
		addView(OpenTicketView.VIEW_NAME, openTicketView);
		
		showView(LoginScreen.VIEW_NAME);
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

	public SettleTicketView getPaymentView() {
		return paymentView;
	}
}
