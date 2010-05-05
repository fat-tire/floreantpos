package com.floreantpos.ui.report.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.main.Application;
import com.floreantpos.ui.report.SalesBalanceReportView;

public class SalesBalanceReportAction extends AbstractAction {

	public SalesBalanceReportAction() {
		super(com.floreantpos.POSConstants.SALES_BALANCE_REPORT);
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
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.SALES_BALANCE_REPORT);
		if (index == -1) {
			reportView = new SalesBalanceReportView();
			tabbedPane.addTab(com.floreantpos.POSConstants.SALES_BALANCE_REPORT, reportView);
		}
		else {
			reportView = (SalesBalanceReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
