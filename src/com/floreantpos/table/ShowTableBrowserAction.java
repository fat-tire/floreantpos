package com.floreantpos.table;

import javax.swing.JTabbedPane;

import com.floreantpos.Messages;
import com.floreantpos.actions.PosAction;
import com.floreantpos.bo.ui.BackOfficeWindow;

public class ShowTableBrowserAction extends PosAction {

	public ShowTableBrowserAction() {
		super(Messages.getString("ShowTableBrowserAction.0")); //$NON-NLS-1$
	}

	@Override
	public void execute() {
		BackOfficeWindow backOfficeWindow = com.floreantpos.util.POSUtil.getBackOfficeWindow();

		ShopTableBrowser explorer = null;
		JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab(Messages.getString("ShowTableBrowserAction.1")); //$NON-NLS-1$
		if (index == -1) {
			explorer = new ShopTableBrowser();
			tabbedPane.addTab(Messages.getString("ShowTableBrowserAction.2"), explorer); //$NON-NLS-1$
		}
		else {
			explorer = (ShopTableBrowser) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}

}
