
package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.MenuItemExplorer;
import com.floreantpos.bo.ui.explorer.PizzaItemExplorer;

public class PizzaItemExplorerAction extends AbstractAction {

	public PizzaItemExplorerAction() {
		super("Pizza Items");
	}

	public PizzaItemExplorerAction(String name) {
		super(name);
	}

	public PizzaItemExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow backOfficeWindow = com.floreantpos.util.POSUtil.getBackOfficeWindow();
		JTabbedPane tabbedPane;
		PizzaItemExplorer item;
		tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab("Pizza Item Explorer");
		if (index == -1) {
			item = new PizzaItemExplorer();
			tabbedPane.addTab("Pizza Item Explorer", item);
		}
		else {
			item = (PizzaItemExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(item);
	}

}
