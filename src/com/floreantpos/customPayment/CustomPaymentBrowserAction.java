package com.floreantpos.customPayment;

import javax.swing.JTabbedPane;

import com.floreantpos.Messages;
import com.floreantpos.actions.PosAction;
import com.floreantpos.bo.ui.BackOfficeWindow;

public class CustomPaymentBrowserAction extends PosAction {

	public CustomPaymentBrowserAction() {
		super(Messages.getString("CustomPaymentBrowserAction.0")); //$NON-NLS-1$
	}

	@Override
	public void execute() {
		BackOfficeWindow backOfficeWindow = com.floreantpos.util.POSUtil.getBackOfficeWindow();

		CustomPaymentBrowser explorer = null;
		JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab(Messages.getString("CustomPaymentBrowserAction.1")); //$NON-NLS-1$
		if (index == -1) {
			explorer = new CustomPaymentBrowser();
			tabbedPane.addTab(Messages.getString("CustomPaymentBrowserAction.2"), explorer); //$NON-NLS-1$
		}
		else {
			explorer = (CustomPaymentBrowser) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}

}
