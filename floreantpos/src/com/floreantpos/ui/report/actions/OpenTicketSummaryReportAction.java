package com.floreantpos.ui.report.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.main.Application;
import com.floreantpos.ui.report.OpenTicketSummaryReport;
import com.floreantpos.ui.report.ReportViewer;

public class OpenTicketSummaryReportAction extends AbstractAction {

	public OpenTicketSummaryReportAction() {
		super("Open Ticket Summary");
	}

	public OpenTicketSummaryReportAction(String name) {
		super(name);
	}

	public OpenTicketSummaryReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = Application.getInstance().getBackOfficeWindow();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		ReportViewer viewer = null;
		int index = tabbedPane.indexOfTab("Open Ticket Summary");
		if (index == -1) {
			viewer = new ReportViewer(new OpenTicketSummaryReport());
			tabbedPane.addTab("Open Ticket Summary Report", viewer);
		}
		else {
			viewer = (ReportViewer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(viewer);
	}

}
