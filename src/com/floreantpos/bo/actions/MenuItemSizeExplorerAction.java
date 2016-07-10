package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.MenuItemSizeExplorer;
import com.floreantpos.bo.ui.explorer.PizzaCrustExplorer;

public class MenuItemSizeExplorerAction extends AbstractAction {

	public MenuItemSizeExplorerAction() {
		super("Size");
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
		int index = tabbedPane.indexOfTab("Size");
		if (index == -1) {
			explorer = new MenuItemSizeExplorer();
			tabbedPane.addTab("Size", explorer);
		}
		else {
			explorer = (MenuItemSizeExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}

}
