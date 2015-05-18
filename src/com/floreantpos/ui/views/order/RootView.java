package com.floreantpos.ui.views.order;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.floreantpos.ui.HeaderPanel;
import com.floreantpos.ui.views.IView;
import com.floreantpos.ui.views.LoginView;
import com.floreantpos.ui.views.payment.SettleTicketDialog;

public class RootView extends com.floreantpos.swing.TransparentPanel {
	private CardLayout cards = new CardLayout();
	
	private HeaderPanel headerPanel = new HeaderPanel();
	private JPanel contentPanel = new JPanel(cards);
	private LoginView loginScreen;
	//private SwitchboardView switchboardView;
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
		addView(loginScreen);
		
//		switchboardView = new SwitchboardView();
//		addView(switchboardView);
		
		
		
		showView(LoginView.VIEW_NAME);
	}
	
	public void addView(IView iView) {
		viewNames.add(iView.getViewName());
		contentPanel.add(iView.getViewName(), iView.getViewComponent());
	}
	
	public void showView(String viewName) {
		if(LoginView.VIEW_NAME.equals(viewName)) {
			headerPanel.setVisible(false);
		}
		else {
			headerPanel.setVisible(true);
		}
		
		cards.show(contentPanel, viewName);
	}
	
	public void showView(IView view) {
		if(!viewNames.contains(view.getViewName())) {
			addView(view);
		}
		
		showView(view.getViewName());
	}
	
	public boolean hasView(String viewName) {
		return viewNames.contains(viewName);
	}
	
	public boolean hasView(IView view) {
		return viewNames.contains(view.getViewName());
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

//	public SwitchboardView getSwitchboadView() {
//		return switchboardView;
//	}
//
//	public void setSwitchboardView(SwitchboardView switchboardView) {
//		this.switchboardView = switchboardView;
//	}

	public synchronized static RootView getInstance() {
		if(instance == null) {
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
}
