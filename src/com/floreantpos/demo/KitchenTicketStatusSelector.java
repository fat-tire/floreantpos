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
package com.floreantpos.demo;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.floreantpos.Messages;
import com.floreantpos.model.KitchenTicket.KitchenTicketStatus;
import com.floreantpos.model.KitchenTicketItem;
import com.floreantpos.model.dao.KitchenTicketItemDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class KitchenTicketStatusSelector extends POSDialog implements ActionListener {
	private PosButton btnVoid= new PosButton(KitchenTicketStatus.VOID.name());
	private PosButton btnReady = new PosButton(Messages.getString("KitchenTicketView.11"));
	
	private KitchenTicketItem ticketItem;
	
	public KitchenTicketStatusSelector(Frame parent) {
		super(parent, true);
		setTitle(Messages.getString("KitchenTicketStatusSelector.0")); //$NON-NLS-1$
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle(Messages.getString("KitchenTicketStatusSelector.1")); //$NON-NLS-1$
		add(titlePanel, BorderLayout.NORTH);
		
		JPanel panel = new JPanel(new GridLayout(1, 0, 10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(btnVoid);
		panel.add(btnReady);
		
		add(panel);
		
		btnReady.setActionCommand(KitchenTicketStatus.DONE.name());
		btnVoid.addActionListener(this);
		btnReady.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			KitchenTicketStatus status = KitchenTicketStatus.valueOf(e.getActionCommand());
			ticketItem.setStatus(status.name());

			KitchenTicketItemDAO.getInstance().saveOrUpdate(ticketItem);

			dispose();
		} catch (Exception e2) {
			POSMessageDialog.showError(this, e2.getMessage(), e2);
		}
	}

	public KitchenTicketItem getTicketItem() {
		return ticketItem;
	}

	public void setTicketItem(KitchenTicketItem ticketItem) {
		this.ticketItem = ticketItem;
	}
}
