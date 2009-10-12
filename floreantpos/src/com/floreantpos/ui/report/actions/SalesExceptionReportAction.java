package com.floreantpos.ui.report.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.main.Application;
import com.floreantpos.ui.report.SalesExceptionReportView;

public class SalesExceptionReportAction extends AbstractAction {

	public SalesExceptionReportAction() {
		super("Sales Exception Report");
	}

	public SalesExceptionReportAction(String name) {
		super(name);
	}

	public SalesExceptionReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = Application.getInstance().getBackOfficeWindow();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		SalesExceptionReportView reportView = null;
		int index = tabbedPane.indexOfTab("Sales Exception Report");
		if (index == -1) {
			reportView = new SalesExceptionReportView();
			tabbedPane.addTab("Sales Exception Report", reportView);
		}
		else {
			reportView = (SalesExceptionReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
