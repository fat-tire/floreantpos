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
package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class OrderInfoDialog extends POSDialog {
	OrderInfoView view;
	
	public OrderInfoDialog(OrderInfoView view) {
		this.view = view;
		setTitle(Messages.getString("OrderInfoDialog.0")); //$NON-NLS-1$
		
		createUI();
	}

	public void createUI() {
		add(view);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		PosButton btnTransferUser = new PosButton();
		btnTransferUser.setText(Messages.getString("OrderInfoDialog.3")); //$NON-NLS-1$
		btnTransferUser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				User currentUser=Application.getCurrentUser();
				
				for (Iterator iter = view.getTickets().iterator(); iter.hasNext();) {
					
					Ticket ticket = (Ticket) iter.next();
					
					if(!currentUser.equals(ticket.getOwner())) {
						
						if(!currentUser.hasPermission(UserPermission.TRANSFER_TICKET)) {
							POSMessageDialog.showError(getParent(), Messages.getString("OrderInfoDialog.4")+ticket.getId()); //$NON-NLS-1$
							return;
						}
					}
				}
				
				UserTransferDialog dialog=new UserTransferDialog(view);
				dialog.setSize(360, 555);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setLocationRelativeTo(Application.getPosWindow());
				dialog.setVisible(true);
			}
		});

		panel.add(btnTransferUser);
		
		PosButton btnPrint = new PosButton();
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doPrint();
			}
		});
		btnPrint.setText(Messages.getString("OrderInfoDialog.1")); //$NON-NLS-1$
		panel.add(btnPrint);
		
		PosButton btnClose = new PosButton();
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnClose.setText(Messages.getString("OrderInfoDialog.2")); //$NON-NLS-1$
		panel.add(btnClose);
	}

	protected void doPrint() {
		try {
			view.print();
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage());
		}
	}
}
