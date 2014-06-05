package com.floreantpos.demo;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.border.EmptyBorder;

import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;

public class KitchenTerminal extends JPanel {
	private KitchenTicketView kitchenTicketView;
	private KitchenTicketView kitchenTicketView_1;
	private KitchenTicketView kitchenTicketView_2;
	private KitchenTicketView kitchenTicketView_4;
	private KitchenTicketView kitchenTicketView_3;
	private KitchenTicketView kitchenTicketView_6;
	private KitchenTicketView kitchenTicketView_5;
	private KitchenTicketView kitchenTicketView_7;
	
	public KitchenTerminal() {
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new GridLayout(2, 4, 10, 10));
		
		kitchenTicketView = new KitchenTicketView();
		kitchenTicketView.setTitle("Terminal #1/Ticket #22");
		add(kitchenTicketView);
		
		kitchenTicketView_1 = new KitchenTicketView();
		kitchenTicketView_1.setTitle("Terminal #1/Ticket #5");
		add(kitchenTicketView_1);
		
		kitchenTicketView_2 = new KitchenTicketView();
		kitchenTicketView_2.setTitle("Terminal #2/Ticket #34");
		add(kitchenTicketView_2);
		
		kitchenTicketView_4 = new KitchenTicketView();
		kitchenTicketView_4.setTitle("Terminal #3/Ticket #34");
		add(kitchenTicketView_4);
		
		kitchenTicketView_3 = new KitchenTicketView();
		kitchenTicketView_3.setTitle("Terminal #2/Ticket #36");
		add(kitchenTicketView_3);
		
		kitchenTicketView_6 = new KitchenTicketView();
		kitchenTicketView_6.setTitle("Terminal #6/Ticket #34");
		add(kitchenTicketView_6);
		
		kitchenTicketView_5 = new KitchenTicketView();
		kitchenTicketView_5.setTitle("");
		add(kitchenTicketView_5);
		
		kitchenTicketView_7 = new KitchenTicketView();
		kitchenTicketView_7.setTitle("");
		add(kitchenTicketView_7);
		
		Ticket ticket1 = new Ticket();
		TicketItem ticketItem = new TicketItem(1);
		ticketItem.setName("Hot cake");
		ticketItem.setItemCount(5);
		ticketItem.setUnitPrice(3.0);
		ticket1.addToticketItems(ticketItem);
		kitchenTicketView_1.setTicket(ticket1);
	}
}
