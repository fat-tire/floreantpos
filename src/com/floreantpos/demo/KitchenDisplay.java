package com.floreantpos.demo;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.floreantpos.model.KitchenTicket;

public class KitchenDisplay extends JFrame {
	
	public static final KitchenDisplay instance = new KitchenDisplay();
	
	JPanel ticketPanel = new JPanel(new GridLayout(1, 0, 10, 10));
	
	public KitchenDisplay() {
		setTitle("Kitchen Display");
		
		ticketPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(new JScrollPane(ticketPanel));
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public void addTicket(KitchenTicket ticket) {
		KitchenTicketView view = new KitchenTicketView(this, ticket);
		ticketPanel.add(view);
		ticketPanel.revalidate();
		ticketPanel.repaint();
		setVisible(true);
	}
	
	public void removeTicket(KitchenTicketView view) {
		ticketPanel.remove(view);
		ticketPanel.revalidate();
		ticketPanel.repaint();
		
//		if(ticketPanel.getComponents().length == 0) {
//			setVisible(false);
//		}
	}
}
