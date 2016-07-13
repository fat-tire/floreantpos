
package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.report.InventoryOnHandReportView;
import com.floreantpos.report.PurchaseReportView;

public class InventoryOnHandReportAction extends AbstractAction {

	public InventoryOnHandReportAction() {
		super("Inventory On Hand Report");
	}

	public InventoryOnHandReportAction(String name) {
		super(name);
	}

	public InventoryOnHandReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = com.floreantpos.util.POSUtil.getBackOfficeWindow();
		JTabbedPane tabbedPane = window.getTabbedPane();

		InventoryOnHandReportView reportView = null;
		int index = tabbedPane.indexOfTab("Inventory On Hand Report");
		if (index == -1) {
			reportView = new InventoryOnHandReportView();
			tabbedPane.addTab("Inventory On Hand Report", reportView);
		}
		else {
			reportView = (InventoryOnHandReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
