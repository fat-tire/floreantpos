package com.floreantpos.demo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import com.floreantpos.model.Ticket;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.ticket.TicketViewerTable;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class KitchenTicketView extends JPanel {
	private JLabel lblNewLabel;
	private TicketViewerTable ticketViewerTable;
	
	public KitchenTicketView() {
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new GridLayout(1, 0, 10, 0));
		
		PosButton psbtnDone = new PosButton();
		psbtnDone.setPreferredSize(new Dimension(0, 40));
		psbtnDone.setText("Done");
		panel.add(psbtnDone);
		
		PosButton psbtnCancel = new PosButton();
		psbtnCancel.setPreferredSize(new Dimension(0, 40));
		psbtnCancel.setText("Cancel");
		panel.add(psbtnCancel);
		
		lblNewLabel = new JLabel("New label");
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setPreferredSize(new Dimension(0, 40));
		add(lblNewLabel, BorderLayout.NORTH);
		
		ticketViewerTable = new TicketViewerTable();
		add(ticketViewerTable, BorderLayout.CENTER);
		
	}

	public void setTicket(Ticket ticket) {
		ticketViewerTable.setTicket(ticket);
	}
	
	public void setTitle(String title) {
		lblNewLabel.setText(title);
	}

}
