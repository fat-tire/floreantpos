package com.floreantpos.ui.views.order;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;

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
	private SettleTicketDialog paymentView;
	
	private Map<String, IView> views = new HashMap<String, IView>();
	
	private static RootView instance;
	
	private RootView() {
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(3,3,3,3));
		
		headerPanel.setVisible(false);
		add(headerPanel, BorderLayout.NORTH);
		
		add(contentPanel);
		
		loginScreen = new LoginView();
		addView(loginScreen);
		
		showView(LoginView.VIEW_NAME);
	}
	
	public void addView(IView iView) {
		views.put(iView.getViewName(), iView);
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
		if(!views.containsKey(view.getViewName())) {
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
