package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.Messages;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.report.InventoryOnHandReportView;

public class InventoryOnHandReportAction extends AbstractAction {

	public InventoryOnHandReportAction() {
		super(Messages.getString("InventoryOnHandReportAction.0")); //$NON-NLS-1$
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
		int index = tabbedPane.indexOfTab(Messages.getString("InventoryOnHandReportAction.0")); //$NON-NLS-1$
		if (index == -1) {
			reportView = new InventoryOnHandReportView();
			tabbedPane.addTab(Messages.getString("InventoryOnHandReportAction.0"), reportView); //$NON-NLS-1$
		}
		else {
			reportView = (InventoryOnHandReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
