package com.floreantpos.table;

import javax.swing.JTabbedPane;

import com.floreantpos.actions.PosAction;
import com.floreantpos.bo.ui.BackOfficeWindow;

public class ShowTableBrowserAction extends PosAction {

	public ShowTableBrowserAction() {
		super("Tables");
	}

	@Override
	public void execute() {
		BackOfficeWindow backOfficeWindow = com.floreantpos.util.POSUtil.getBackOfficeWindow();

		ShopTableBrowser explorer = null;
		JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab("Tables");
		if (index == -1) {
			explorer = new ShopTableBrowser();
			tabbedPane.addTab("Tables", explorer);
		}
		else {
			explorer = (ShopTableBrowser) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}

}
