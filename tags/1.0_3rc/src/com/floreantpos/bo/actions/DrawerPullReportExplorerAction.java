package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.DrawerPullReportExplorer;
import com.floreantpos.main.Application;

public class DrawerPullReportExplorerAction extends AbstractAction {

	public DrawerPullReportExplorerAction() {
		super(com.floreantpos.POSConstants.DRAWER_PULL_REPORTS);
	}

	public DrawerPullReportExplorerAction(String name) {
		super(name);
	}

	public DrawerPullReportExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow backOfficeWindow = Application.getInstance().getBackOfficeWindow();
		
		DrawerPullReportExplorer explorer = null;
		JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.DRAWER_PULL_REPORTS);
		if (index == -1) {
			explorer = new DrawerPullReportExplorer();
			tabbedPane.addTab(com.floreantpos.POSConstants.DRAWER_PULL_REPORTS, explorer);
		}
		else {
			explorer = (DrawerPullReportExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}

}
