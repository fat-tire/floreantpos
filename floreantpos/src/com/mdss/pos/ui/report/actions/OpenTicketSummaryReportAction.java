package com.mdss.pos.ui.report.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.mdss.pos.bo.ui.BackOfficeWindow;
import com.mdss.pos.main.Application;
import com.mdss.pos.ui.report.ReportViewer;
import com.mdss.pos.ui.report.OpenTicketSummaryReport;

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
