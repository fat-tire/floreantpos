package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.report.ServerProductivityReportView;

public class ServerProductivityReportAction extends AbstractAction {

	public ServerProductivityReportAction() {
		super(com.floreantpos.POSConstants.SERVER_PRODUCTIVITY_REPORT);
	}

	public ServerProductivityReportAction(String name) {
		super(name);
	}

	public ServerProductivityReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = com.floreantpos.util.POSUtil.getBackOfficeWindow();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		ServerProductivityReportView reportView = null;
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.SERVER_PRODUCTIVITY_REPORT);
		if (index == -1) {
			reportView = new ServerProductivityReportView();
			tabbedPane.addTab(com.floreantpos.POSConstants.SERVER_PRODUCTIVITY_REPORT, reportView);
		}
		else {
			reportView = (ServerProductivityReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
