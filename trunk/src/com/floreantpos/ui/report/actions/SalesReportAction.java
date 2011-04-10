package com.floreantpos.ui.report.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.main.Application;
import com.floreantpos.ui.report.ReportViewer;
import com.floreantpos.ui.report.SalesReport;

public class SalesReportAction extends AbstractAction {

	public SalesReportAction() {
		super(com.floreantpos.POSConstants.SALES_REPORT);
	}

	public SalesReportAction(String name) {
		super(name);
	}

	public SalesReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = Application.getInstance().getBackOfficeWindow();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		ReportViewer viewer = null;
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.SALES_REPORT);
		if (index == -1) {
			viewer = new ReportViewer(new SalesReport());
			tabbedPane.addTab(com.floreantpos.POSConstants.SALES_REPORT, viewer);
		}
		else {
			viewer = (ReportViewer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(viewer);
	}

}
