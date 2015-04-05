package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.report.CreditCardReportView;

public class CreditCardReportAction extends AbstractAction {

	public CreditCardReportAction() {
		super(com.floreantpos.POSConstants.CREDIT_CARD_REPORT);
	}

	public CreditCardReportAction(String name) {
		super(name);
	}

	public CreditCardReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = com.floreantpos.util.POSUtil.getBackOfficeWindow();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		CreditCardReportView reportView = null;
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.CREDIT_CARD_REPORT);
		if (index == -1) {
			reportView = new CreditCardReportView();
			tabbedPane.addTab(com.floreantpos.POSConstants.CREDIT_CARD_REPORT, reportView);
		}
		else {
			reportView = (CreditCardReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
