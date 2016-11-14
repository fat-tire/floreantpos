package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.Messages;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.MenuItemSizeExplorer;

public class MenuItemSizeExplorerAction extends AbstractAction {

	public MenuItemSizeExplorerAction() {
		super(Messages.getString("MenuItemSizeExplorerAction.0")); //$NON-NLS-1$
	}

	public MenuItemSizeExplorerAction(String name) {
		super(name);
	}

	public MenuItemSizeExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow backOfficeWindow = com.floreantpos.util.POSUtil.getBackOfficeWindow();

		MenuItemSizeExplorer explorer = null;
		JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab(Messages.getString("MenuItemSizeExplorerAction.0")); //$NON-NLS-1$
		if (index == -1) {
			explorer = new MenuItemSizeExplorer();
			tabbedPane.addTab(Messages.getString("MenuItemSizeExplorerAction.0"), explorer); //$NON-NLS-1$
		}
		else {
			explorer = (MenuItemSizeExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}

}
