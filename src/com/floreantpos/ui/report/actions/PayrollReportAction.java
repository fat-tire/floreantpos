package com.floreantpos.ui.report.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.main.Application;
import com.floreantpos.ui.report.PayrollReportView;

public class PayrollReportAction extends AbstractAction {

	public PayrollReportAction() {
		super(com.floreantpos.POSConstants.PAYROLL_REPORT);
	}

	public PayrollReportAction(String name) {
		super(name);
	}

	public PayrollReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = Application.getInstance().getBackOfficeWindow();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		PayrollReportView reportView = null;
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.PAYROLL_REPORT);
		if (index == -1) {
			reportView = new PayrollReportView();
			tabbedPane.addTab(com.floreantpos.POSConstants.PAYROLL_REPORT, reportView);
		}
		else {
			reportView = (PayrollReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
