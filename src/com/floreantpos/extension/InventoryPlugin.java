package com.floreantpos.extension;

import javax.swing.AbstractAction;
import javax.swing.JTabbedPane;

public interface InventoryPlugin extends FloreantPlugin {
//	void showInventoryItemEntryDialog();
//	void showInventoryGroupEntryDialog();
//	void showInventoryLocationEntryDialog();
//	void showInventoryMetacodeEntryDialog();
//	void showInventoryVendorEntryDialog();
//	void showInventoryWarehouseEntryDialog();
	
	AbstractAction[] getActions();
	void addRecepieView(JTabbedPane tabbedPane);
}
