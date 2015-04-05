package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.report.OpenTicketSummaryReport;
import com.floreantpos.report.ReportViewer;

public class OpenTicketSummaryReportAction extends AbstractAction {

	public OpenTicketSummaryReportAction() {
		super(com.floreantpos.POSConstants.OPEN_TICKET_SUMMARY);
	}

	public OpenTicketSummaryReportAction(String name) {
		super(name);
	}

	public OpenTicketSummaryReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = com.floreantpos.util.POSUtil.getBackOfficeWindow();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		ReportViewer viewer = null;
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.OPEN_TICKET_SUMMARY);
		if (index == -1) {
			viewer = new ReportViewer(new OpenTicketSummaryReport());
			tabbedPane.addTab(com.floreantpos.POSConstants.OPEN_TICKET_SUMMARY_REPORT, viewer);
		}
		else {
			viewer = (ReportViewer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(viewer);
	}

}
