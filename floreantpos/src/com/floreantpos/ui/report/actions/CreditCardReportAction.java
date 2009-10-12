package com.floreantpos.ui.report.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.main.Application;
import com.floreantpos.ui.report.CreditCardReportView;

public class CreditCardReportAction extends AbstractAction {

	public CreditCardReportAction() {
		super("Credit Card Report");
	}

	public CreditCardReportAction(String name) {
		super(name);
	}

	public CreditCardReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = Application.getInstance().getBackOfficeWindow();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		CreditCardReportView reportView = null;
		int index = tabbedPane.indexOfTab("Credit Card Report");
		if (index == -1) {
			reportView = new CreditCardReportView();
			tabbedPane.addTab("Credit Card Report", reportView);
		}
		else {
			reportView = (CreditCardReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
