package com.floreantpos.ui.report.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.main.Application;
import com.floreantpos.ui.report.MenuUsageReportView;

public class MenuUsageReportAction extends AbstractAction {

	public MenuUsageReportAction() {
		super(com.floreantpos.POSConstants.MENU_USAGE_REPORT);
	}

	public MenuUsageReportAction(String name) {
		super(name);
	}

	public MenuUsageReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = Application.getInstance().getBackOfficeWindow();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		MenuUsageReportView reportView = null;
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.MENU_USAGE_REPORT);
		if (index == -1) {
			reportView = new MenuUsageReportView();
			tabbedPane.addTab(com.floreantpos.POSConstants.MENU_USAGE_REPORT, reportView);
		}
		else {
			reportView = (MenuUsageReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
