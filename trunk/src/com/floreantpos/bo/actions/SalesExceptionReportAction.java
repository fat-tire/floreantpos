package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.report.SalesExceptionReportView;

public class SalesExceptionReportAction extends AbstractAction {

	public SalesExceptionReportAction() {
		super(com.floreantpos.POSConstants.SALES_EXCEPTION_REPORT);
	}

	public SalesExceptionReportAction(String name) {
		super(name);
	}

	public SalesExceptionReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = com.floreantpos.util.POSUtil.getBackOfficeWindow();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		SalesExceptionReportView reportView = null;
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.SALES_EXCEPTION_REPORT);
		if (index == -1) {
			reportView = new SalesExceptionReportView();
			tabbedPane.addTab(com.floreantpos.POSConstants.SALES_EXCEPTION_REPORT, reportView);
		}
		else {
			reportView = (SalesExceptionReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
