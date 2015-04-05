package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.report.SalesDetailReportView;

public class SalesDetailReportAction extends AbstractAction {

	public SalesDetailReportAction() {
		super(com.floreantpos.POSConstants.SALES_DETAILED_REPORT);
	}

	public SalesDetailReportAction(String name) {
		super(name);
	}

	public SalesDetailReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = com.floreantpos.util.POSUtil.getBackOfficeWindow();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		SalesDetailReportView reportView = null;
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.SALES_DETAILED_REPORT);
		if (index == -1) {
			reportView = new SalesDetailReportView();
			tabbedPane.addTab(com.floreantpos.POSConstants.SALES_DETAILED_REPORT, reportView);
		}
		else {
			reportView = (SalesDetailReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
