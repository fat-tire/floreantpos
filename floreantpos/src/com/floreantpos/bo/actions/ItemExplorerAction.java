package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.MenuItemExplorer;
import com.floreantpos.main.Application;

public class ItemExplorerAction extends AbstractAction {

	public ItemExplorerAction() {
		super("Menu Items");
	}

	public ItemExplorerAction(String name) {
		super(name);
	}

	public ItemExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		//BackOfficeWindow backOfficeWindow = Application.getInstance().getBackOfficeWindow();
		//backOfficeWindow.getTabbedPane().addTab("Item exploere", new ItemExplorer());

		BackOfficeWindow backOfficeWindow = Application.getInstance().getBackOfficeWindow();
		JTabbedPane tabbedPane;
		MenuItemExplorer item;
		tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab("Item explorer");
		if (index == -1) {
			item = new MenuItemExplorer();
			tabbedPane.addTab("Item explorer", item);
		}
		else {
			item = (MenuItemExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(item);
	}

}
