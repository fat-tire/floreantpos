package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.UserExplorer;
import com.floreantpos.main.Application;

public class UserExplorerAction extends AbstractAction {

	public UserExplorerAction() {
		super(com.floreantpos.POSConstants.USERS);
	}

	public UserExplorerAction(String name) {
		super(name);
	}

	public UserExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow backOfficeWindow = Application.getInstance().getBackOfficeWindow();
		
		UserExplorer explorer = null;
		JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.USERS);
		if (index == -1) {
			explorer = new UserExplorer();
			tabbedPane.addTab(com.floreantpos.POSConstants.USERS, explorer);
		}
		else {
			explorer = (UserExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}

}
