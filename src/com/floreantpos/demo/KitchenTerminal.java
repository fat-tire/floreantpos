package com.floreantpos.demo;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.ExperienceBlue;

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
		kitchenTicketView_2.setTitle("");
		add(kitchenTicketView_2);
		
		kitchenTicketView_4 = new KitchenTicketView();
		kitchenTicketView_4.setTitle("");
		add(kitchenTicketView_4);
		
		kitchenTicketView_3 = new KitchenTicketView();
		kitchenTicketView_3.setTitle("");
		add(kitchenTicketView_3);
		
		kitchenTicketView_6 = new KitchenTicketView();
		kitchenTicketView_6.setTitle("");
		add(kitchenTicketView_6);
		
		kitchenTicketView_5 = new KitchenTicketView();
		kitchenTicketView_5.setTitle("");
		add(kitchenTicketView_5);
		
		kitchenTicketView_7 = new KitchenTicketView();
		kitchenTicketView_7.setTitle("");
		add(kitchenTicketView_7);
		
		kitchenTicketView_1.setTicket(createTicket(new String[] {"Hote Cake", "Small Burger", "Small coke"}, new int[]{5, 5, 5}, new double[] {5, 7, 2}));
		kitchenTicketView.setTicket(createTicket(new String[] {"Big Chicken Burger", "French Fries"}, new int[]{3, 3}, new double[] {6, 2}));
	}

	private Ticket createTicket(String[] itemNames, int[] itemCounts, double[] unitPrices) {
		Ticket ticket1 = new Ticket();
		
		for (int i = 0; i < itemNames.length; i++) {
			TicketItem ticketItem = new TicketItem(1);
			ticketItem.setName(itemNames[i]);
			ticketItem.setItemCount(itemCounts[i]);
			ticketItem.setUnitPrice(unitPrices[i]);
			ticket1.addToticketItems(ticketItem);
		}
		
		ticket1.calculatePrice();
		return ticket1;
	}
	
	public static void main(String[] args) {
		try {
			PlasticXPLookAndFeel.setPlasticTheme(new ExperienceBlue());
			UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
			UIManager.put("ComboBox.is3DEnabled", Boolean.FALSE);
		} catch (Exception ignored) {
		}
		
		JFrame frame = new JFrame();
		frame.setTitle("Floreant POS - Kitchen Terminal");
		frame.add(new KitchenTerminal());
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
