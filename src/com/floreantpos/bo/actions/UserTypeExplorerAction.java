package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.UserTypeExplorer;
import com.floreantpos.main.Application;

public class UserTypeExplorerAction extends AbstractAction {

	public UserTypeExplorerAction() {
		super(com.floreantpos.POSConstants.USER_TYPES);
	}

	public UserTypeExplorerAction(String name) {
		super(name);
	}

	public UserTypeExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow backOfficeWindow = Application.getInstance().getBackOfficeWindow();
		
		UserTypeExplorer explorer = null;
		JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.USER_TYPE_EXPLORER);
		if (index == -1) {
			explorer = new UserTypeExplorer();
			tabbedPane.addTab(com.floreantpos.POSConstants.USER_TYPE_EXPLORER, explorer);
		}
		else {
			explorer = (UserTypeExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}

}
