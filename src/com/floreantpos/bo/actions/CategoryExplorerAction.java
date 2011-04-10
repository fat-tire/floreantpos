package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.CategoryExplorer;
import com.floreantpos.main.Application;

public class CategoryExplorerAction extends AbstractAction {

	public CategoryExplorerAction() {
		super(com.floreantpos.POSConstants.MENU_CATEGORIES);
	}

	public CategoryExplorerAction(String name) {
		super(name);
	}

	public CategoryExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow backOfficeWindow = Application.getInstance().getBackOfficeWindow();
		
		CategoryExplorer explorer = null;
		JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.CATEGORY_EXPLORER);
		if (index == -1) {
			explorer = new CategoryExplorer();
			tabbedPane.addTab(com.floreantpos.POSConstants.CATEGORY_EXPLORER, explorer);
		}
		else {
			explorer = (CategoryExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}

}
