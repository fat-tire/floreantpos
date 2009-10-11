package com.mdss.pos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.mdss.pos.bo.ui.BackOfficeWindow;
import com.mdss.pos.bo.ui.explorer.UserTypeExplorer;
import com.mdss.pos.main.Application;

public class UserTypeExplorerAction extends AbstractAction {

	public UserTypeExplorerAction() {
		super("User Types");
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
		int index = tabbedPane.indexOfTab("User Type explorer");
		if (index == -1) {
			explorer = new UserTypeExplorer();
			tabbedPane.addTab("User Type explorer", explorer);
		}
		else {
			explorer = (UserTypeExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}

}
