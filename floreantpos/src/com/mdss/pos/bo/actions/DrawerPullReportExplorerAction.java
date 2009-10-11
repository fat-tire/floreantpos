package com.mdss.pos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.mdss.pos.bo.ui.BackOfficeWindow;
import com.mdss.pos.bo.ui.explorer.DrawerPullReportExplorer;
import com.mdss.pos.main.Application;

public class DrawerPullReportExplorerAction extends AbstractAction {

	public DrawerPullReportExplorerAction() {
		super("Drawer Pull Reports");
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
		int index = tabbedPane.indexOfTab("Drawer Pull Reports");
		if (index == -1) {
			explorer = new DrawerPullReportExplorer();
			tabbedPane.addTab("Drawer Pull Reports", explorer);
		}
		else {
			explorer = (DrawerPullReportExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}

}
