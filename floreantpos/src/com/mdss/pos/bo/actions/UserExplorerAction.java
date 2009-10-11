package com.mdss.pos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.mdss.pos.bo.ui.BackOfficeWindow;
import com.mdss.pos.bo.ui.explorer.UserExplorer;
import com.mdss.pos.main.Application;

public class UserExplorerAction extends AbstractAction {

	public UserExplorerAction() {
		super("Users");
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
		int index = tabbedPane.indexOfTab("Users");
		if (index == -1) {
			explorer = new UserExplorer();
			tabbedPane.addTab("Users", explorer);
		}
		else {
			explorer = (UserExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}

}
