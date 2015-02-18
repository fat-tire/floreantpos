package com.floreantpos.ui.views.order;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.floreantpos.main.Application;
import com.floreantpos.model.TicketType;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.OpenTicketsListDialog;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.views.SwitchboardView;

public class CashierModeNextActionDialog extends POSDialog implements ActionListener {
	PosButton btnNew = new PosButton("NEW TICKET");
	PosButton btnOpen = new PosButton("OPEN TICKET");
	PosButton btnLogout = new PosButton("LOG OUT");
	
	JLabel messageLabel = new JLabel("", JLabel.CENTER);
	
	public CashierModeNextActionDialog(Frame owner, String message) {
		super(owner, true);
		
		setTitle("SELECT NEXT ACTION");
		
		JPanel contentPane = new JPanel(new BorderLayout(10, 20));
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
		contentPane.add(messageLabel, BorderLayout.NORTH);
		
		JPanel buttonPanel = new JPanel(new GridLayout(1, 0, 5, 5));
		
		buttonPanel.add(btnNew);
		buttonPanel.add(btnOpen);
		buttonPanel.add(btnLogout);
		contentPane.add(buttonPanel);
		
		setContentPane(contentPane);
		
		btnNew.addActionListener(this);
		btnOpen.addActionListener(this);
		btnLogout.addActionListener(this);
		
		messageLabel.setFont(messageLabel.getFont().deriveFont(Font.BOLD, 16));
		messageLabel.setText(message);
		
		setSize(550, 180);
		setResizable(false);
		
		Application.getPosWindow().setGlassPaneVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == btnNew) {
			SwitchboardView.doTakeout(TicketType.TAKE_OUT);
		}
		else if(e.getSource() == btnLogout) {
			Application.getInstance().logout();
		}
		else if(e.getSource() == btnOpen) {
			OpenTicketsListDialog dialog = new OpenTicketsListDialog((Frame) SwingUtilities.getWindowAncestor(this), true);
			dialog.open();
		}
		
		Application.getPosWindow().setGlassPaneVisible(false);
		dispose();
	}

}
