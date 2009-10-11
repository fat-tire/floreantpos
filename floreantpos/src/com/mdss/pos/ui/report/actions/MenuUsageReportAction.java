package com.mdss.pos.ui.report.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.mdss.pos.bo.ui.BackOfficeWindow;
import com.mdss.pos.main.Application;
import com.mdss.pos.ui.report.MenuUsageReportView;

public class MenuUsageReportAction extends AbstractAction {

	public MenuUsageReportAction() {
		super("Menu Usage Report");
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
		int index = tabbedPane.indexOfTab("Menu Usage Report");
		if (index == -1) {
			reportView = new MenuUsageReportView();
			tabbedPane.addTab("Menu Usage Report", reportView);
		}
		else {
			reportView = (MenuUsageReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
