package com.floreantpos.ui.report.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.main.Application;
import com.floreantpos.ui.report.HourlyLaborReportView;

public class HourlyLaborReportAction extends AbstractAction {

	public HourlyLaborReportAction() {
		super("Hourly Labor Report");
	}

	public HourlyLaborReportAction(String name) {
		super(name);
	}

	public HourlyLaborReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = Application.getInstance().getBackOfficeWindow();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		HourlyLaborReportView reportView = null;
		int index = tabbedPane.indexOfTab("Hourly Labor Report");
		if (index == -1) {
			reportView = new HourlyLaborReportView();
			tabbedPane.addTab("Hourly Labor Report", reportView);
		}
		else {
			reportView = (HourlyLaborReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
