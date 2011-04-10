package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.ModifierExplorer;
import com.floreantpos.main.Application;

public class ModifierExplorerAction extends AbstractAction {

	public ModifierExplorerAction() {
		super(com.floreantpos.POSConstants.MENU_MODIFIERS);
	}

	public ModifierExplorerAction(String name) {
		super(name);
	}

	public ModifierExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow backOfficeWindow = Application.getInstance().getBackOfficeWindow();
		JTabbedPane tabbedPane;
		ModifierExplorer modifier;
		tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.MODIFIER_EXPLORER);
		if (index == -1) {
			modifier = new ModifierExplorer();
			tabbedPane.addTab(com.floreantpos.POSConstants.MODIFIER_EXPLORER, modifier);
		}
		else {
			modifier = (ModifierExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(modifier);
	}

}
