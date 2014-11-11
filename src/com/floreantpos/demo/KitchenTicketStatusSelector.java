package com.floreantpos.demo;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.floreantpos.model.KitchenTicket.KitchenTicketStatus;
import com.floreantpos.model.KitchenTicketItem;
import com.floreantpos.model.dao.KitchenTicketItemDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class KitchenTicketStatusSelector extends POSDialog implements ActionListener {
	private PosButton btnWaiting = new PosButton(KitchenTicketStatus.WAITING.name());
	private PosButton btnVoid= new PosButton(KitchenTicketStatus.VOID.name());
	private PosButton btnReady = new PosButton(KitchenTicketStatus.DONE.name());
	
	private KitchenTicketItem ticketItem;
	
	public KitchenTicketStatusSelector(Frame parent) {
		super(parent, true);
		setTitle("Kitchen item status selector");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle("Select item status");
		add(titlePanel, BorderLayout.NORTH);
		
		JPanel panel = new JPanel(new GridLayout(1, 0, 10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(btnWaiting);
		panel.add(btnVoid);
		panel.add(btnReady);
		
		add(panel);
		
		btnWaiting.addActionListener(this);
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
