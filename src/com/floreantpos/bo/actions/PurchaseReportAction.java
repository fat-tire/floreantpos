
package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.report.PurchaseReportView;

public class PurchaseReportAction extends AbstractAction {

	public PurchaseReportAction() {
		super("Purchase Report");
	}

	public PurchaseReportAction(String name) {
		super(name);
	}

	public PurchaseReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = com.floreantpos.util.POSUtil.getBackOfficeWindow();
		JTabbedPane tabbedPane = window.getTabbedPane();

		PurchaseReportView reportView = null;
		int index = tabbedPane.indexOfTab("Purchase Report");
		if (index == -1) {
			reportView = new PurchaseReportView();
			tabbedPane.addTab("Purchase Report", reportView);
		}
		else {
			reportView = (PurchaseReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
