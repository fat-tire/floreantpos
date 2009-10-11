package com.mdss.pos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.mdss.pos.bo.ui.BackOfficeWindow;
import com.mdss.pos.bo.ui.explorer.GroupExplorer;
import com.mdss.pos.main.Application;

public class GroupExplorerAction extends AbstractAction {

	public GroupExplorerAction() {
		super("Menu Groups");
	}

	public GroupExplorerAction(String name) {
		super(name);
	}

	public GroupExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		//BackOfficeWindow backOfficeWindow = Application.getInstance().getBackOfficeWindow();
		//backOfficeWindow.getTabbedPane().addTab("Group exploere", new GroupExplorer());

		BackOfficeWindow backOfficeWindow = Application.getInstance().getBackOfficeWindow();
		JTabbedPane tabbedPane;
		GroupExplorer group;
		tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab("Group exploere");
		if (index == -1) {
			group = new GroupExplorer();
			tabbedPane.addTab("Group exploere", group);
		}
		else {
			group = (GroupExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(group);

	}

}
