package com.floreantpos.ui.views.order;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.floreantpos.ui.HeaderPanel;
import com.floreantpos.ui.views.LoginView;
import com.floreantpos.ui.views.SwitchboardView;
import com.floreantpos.ui.views.payment.SettleTicketDialog;

public class RootView extends com.floreantpos.swing.TransparentPanel {
	private CardLayout cards = new CardLayout();
	
	private HeaderPanel headerPanel = new HeaderPanel();
	private JPanel contentPanel = new JPanel(cards);
	private LoginView loginScreen;
	private SwitchboardView switchboardView;
	private OrderView orderView;
	private SettleTicketDialog paymentView;
	
	private Set<String> viewNames = new HashSet<String>();

	
	private static RootView instance;
	
	private RootView() {
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(3,3,3,3));
		
		headerPanel.setVisible(false);
		add(headerPanel, BorderLayout.NORTH);
		
		add(contentPanel);
		
		loginScreen = new LoginView();
		addView(LoginView.VIEW_NAME, loginScreen);
		
		switchboardView = new SwitchboardView();
		addView(SwitchboardView.VIEW_NAME, switchboardView);
		
		orderView = OrderView.getInstance();
		orderView.init();
		addView(OrderView.VIEW_NAME, orderView);
		
		showView(LoginView.VIEW_NAME);
	}
	
	public void addView(String viewName, Component view) {
		viewNames.add(viewName);
		contentPanel.add(view, viewName);
	}
	
	public void showView(String viewName) {
		cards.show(contentPanel, viewName);
		if(LoginView.VIEW_NAME.equals(viewName)) {
			headerPanel.stopTimer();
			headerPanel.setVisible(false);
		}
		else {
			headerPanel.startTimer();
			headerPanel.setVisible(true);
		}
	}
	
	public boolean hasView(String viewName) {
		return viewNames.contains(viewName);
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
