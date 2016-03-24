package com.floreantpos.ui.tableselection;

import java.awt.HeadlessException;
import java.util.List;

import com.floreantpos.main.Application;
import com.floreantpos.main.PosWindow;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.ui.dialog.POSDialog;

public class TableSelectorDialog extends POSDialog {

	private final TableSelector tableSelector;

	public TableSelectorDialog(TableSelector tableSelector) throws HeadlessException {
		super(Application.getPosWindow(), true);
		this.tableSelector = tableSelector;
		
		getContentPane().add(tableSelector);
		
		PosWindow window = Application.getPosWindow();
		setSize(window.getSize());
		setLocation(window.getLocation());
	}
	
	public void setCreateNewTicket(boolean createNewTicket) {
		tableSelector.setCreateNewTicket(createNewTicket);
	}
	
	public void updateView(boolean update) {
		tableSelector.updateView(update);
	}
	
	public List<ShopTable> getSelectedTables() {
		return tableSelector.getSelectedTables();
	}

	public void setTicket(Ticket thisTicket) {
		tableSelector.setTicket(thisTicket); 
		
	}
}
