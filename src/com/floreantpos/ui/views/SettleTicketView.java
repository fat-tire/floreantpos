package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.border.EmptyBorder;

import com.floreantpos.model.Ticket;

public class SettleTicketView extends com.floreantpos.swing.TransparentPanel {
	public final static String VIEW_NAME = "PAYMENT_VIEW";
	
	private String previousViewName = SwitchboardView.VIEW_NAME;
	private static SettleTicketView instance;
	
	private CardLayout leftPanelLayout = new CardLayout(); 
	
	private com.floreantpos.swing.TransparentPanel leftPanel = new com.floreantpos.swing.TransparentPanel(leftPanelLayout);
	private com.floreantpos.swing.TransparentPanel rightPanel = new com.floreantpos.swing.TransparentPanel(new BorderLayout());
	
	private TicketDetailView ticketDetailView;
	private PaymentView paymentView;
	protected List<Ticket> ticketsToSettle;
	
	private SettleTicketView() {
		super(new BorderLayout(5,5));
		
		setBorder(new EmptyBorder(5,5,5,5));
		
		ticketDetailView = new TicketDetailView();
		ticketDetailView.setSettleTicketView(this);
		
		leftPanel.add(ticketDetailView, TicketDetailView.VIEW_NAME);
		add(leftPanel, BorderLayout.CENTER);
		
		add(rightPanel, BorderLayout.EAST);
	}
	
	public synchronized static SettleTicketView getInstance() {
		if (instance == null) {
			instance = new SettleTicketView();
		}
		return instance;
	}

	public void setCurrentTicket(Ticket currentTicket) {
		ticketsToSettle = new ArrayList<Ticket>();
		ticketsToSettle.add(currentTicket);
		
		ticketDetailView.setTickets(getTicketsToSettle());
		paymentView.updateView();
	}
	
	public void updatePaymentView() {
		paymentView.updateView();
	}
	
	public void setPaymentView(PaymentView paymentView) {
		rightPanel.removeAll();
		
		if(paymentView != null) {
			rightPanel.add(paymentView);
			rightPanel.setPreferredSize(paymentView.getPreferredSize());
			paymentView.setSettleTicketView(this);
		}
		this.paymentView = paymentView;
		revalidate();
	}

	public String getPreviousViewName() {
		return previousViewName;
	}

	public void setPreviousViewName(String previousViewName) {
		this.previousViewName = previousViewName;
	}

	public List<Ticket> getTicketsToSettle() {
		return ticketsToSettle;
	}

	public void setTicketsToSettle(List<Ticket> ticketsToSettle) {
		this.ticketsToSettle = ticketsToSettle;
		
		ticketDetailView.setTickets(getTicketsToSettle());
		paymentView.updateView();
	}
}
