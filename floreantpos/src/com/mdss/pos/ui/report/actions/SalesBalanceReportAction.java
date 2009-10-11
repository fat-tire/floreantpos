package com.mdss.pos.ui.report.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.mdss.pos.bo.ui.BackOfficeWindow;
import com.mdss.pos.main.Application;
import com.mdss.pos.ui.report.SalesBalanceReportView;

public class SalesBalanceReportAction extends AbstractAction {

	public SalesBalanceReportAction() {
		super("Sales Balance Report");
	}

	public SalesBalanceReportAction(String name) {
		super(name);
	}

	public SalesBalanceReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = Application.getInstance().getBackOfficeWindow();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		SalesBalanceReportView reportView = null;
		int index = tabbedPane.indexOfTab("Sales Balance Report");
		if (index == -1) {
			reportView = new SalesBalanceReportView();
			tabbedPane.addTab("Sales Balance Report", reportView);
		}
		else {
			reportView = (SalesBalanceReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
