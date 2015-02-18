package com.floreantpos.ui.views.order;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.floreantpos.main.Application;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.OpenTicketsListDialog;
import com.floreantpos.ui.dialog.POSDialog;

public class CashierModeNextActionDialog extends POSDialog implements ActionListener {
	PosButton btnNew = new PosButton("NEW TICKET");
	PosButton btnOpen = new PosButton("OPEN TICKET");
	PosButton btnLogout = new PosButton("LOG OUT");
	
	public CashierModeNextActionDialog(Frame owner) {
		super(owner, true);
		
		setTitle("SELECT NEXT ACTION");
		
		JPanel contentPane = new JPanel(new GridLayout(1, 0, 5, 5));
		contentPane.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
		contentPane.add(btnNew);
		contentPane.add(btnOpen);
		contentPane.add(btnLogout);
		
		setContentPane(contentPane);
		
		btnNew.addActionListener(this);
		btnOpen.addActionListener(this);
		btnLogout.addActionListener(this);
		
		setSize(550, 180);
		setResizable(false);
		
		Application.getPosWindow().setGlassPaneVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == btnNew) {
			
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
