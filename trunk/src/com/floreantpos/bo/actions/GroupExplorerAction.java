package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.MenuGroupExplorer;

public class GroupExplorerAction extends AbstractAction {

	public GroupExplorerAction() {
		super(com.floreantpos.POSConstants.MENU_GROUPS);
	}

	public GroupExplorerAction(String name) {
		super(name);
	}

	public GroupExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow backOfficeWindow = com.floreantpos.util.POSUtil.getBackOfficeWindow();
		JTabbedPane tabbedPane;
		MenuGroupExplorer group;
		tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.GROUP_EXPLORER);
		if (index == -1) {
			group = new MenuGroupExplorer();
			tabbedPane.addTab(com.floreantpos.POSConstants.GROUP_EXPLORER, group);
		}
		else {
			group = (MenuGroupExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(group);

	}

}
