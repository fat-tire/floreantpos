package com.floreantpos.ui.tableselection;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.main.PosWindow;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSDialog;

public class TableSelectorDialog extends POSDialog {

	private final TableSelector tableSelector;

	public TableSelectorDialog(TableSelector tableSelector) throws HeadlessException {
		super(Application.getPosWindow(), true);
		this.tableSelector = tableSelector;
		
		getContentPane().add(tableSelector);
		
		JPanel buttonPanel = new JPanel();
		PosButton btnCancel = new PosButton(POSConstants.CANCEL);
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		
		buttonPanel.add(btnCancel);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		PosWindow window = Application.getPosWindow();
		setSize(window.getSize());
		setLocation(window.getLocation());
	}
	
	public void setCreateNewTicket(boolean createNewTicket) {
		tableSelector.setCreateNewTicket(createNewTicket);
	}
	
	public List<ShopTable> getSelectedTables() {
		return tableSelector.getSelectedTables();
	}

	public void setTicket(Ticket thisTicket) {
		tableSelector.setTicket(thisTicket); 
		
	}
}
