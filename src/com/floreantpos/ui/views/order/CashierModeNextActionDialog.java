/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.ui.views.order;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.OpenTicketsListDialog;
import com.floreantpos.ui.dialog.POSDialog;

public class CashierModeNextActionDialog extends POSDialog implements ActionListener {
	PosButton btnNew = new PosButton(Messages.getString("CashierModeNextActionDialog.0")); //$NON-NLS-1$
	PosButton btnOpen = new PosButton(Messages.getString("CashierModeNextActionDialog.1")); //$NON-NLS-1$
	PosButton btnLogout = new PosButton(Messages.getString("CashierModeNextActionDialog.2")); //$NON-NLS-1$
	
	JLabel messageLabel = new JLabel("", JLabel.CENTER); //$NON-NLS-1$
	
	public CashierModeNextActionDialog(String message) {
		super(Application.getPosWindow(), true);
		
		setTitle(Messages.getString("CashierModeNextActionDialog.4")); //$NON-NLS-1$
		
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
			//FIXME: ORDER TYPE
			//OrderUtil.createNewTakeOutOrder(new OrderType(1, "TAKE OUT"));
		}
		else if(e.getSource() == btnLogout) {
			Application.getInstance().doLogout();
		}
		else if(e.getSource() == btnOpen) {
			OpenTicketsListDialog dialog = new OpenTicketsListDialog();
			dialog.open();
		}
		
		Application.getPosWindow().setGlassPaneVisible(false);
		dispose();
	}

}
