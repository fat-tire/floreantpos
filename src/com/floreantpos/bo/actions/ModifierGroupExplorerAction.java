package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.ModifierGroupExplorer;
import com.floreantpos.main.Application;

public class ModifierGroupExplorerAction extends AbstractAction {

	public ModifierGroupExplorerAction() {
		super(com.floreantpos.POSConstants.MENU_MODIFIER_GROUPS);
	}

	public ModifierGroupExplorerAction(String name) {
		super(name);
	}

	public ModifierGroupExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow backOfficeWindow = Application.getInstance().getBackOfficeWindow();
		JTabbedPane tabbedPane;
		ModifierGroupExplorer mGroup;
		tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.MODIFIER_GROUP_EXPLORER);
		if (index == -1) {
			mGroup = new ModifierGroupExplorer();
			tabbedPane.addTab(com.floreantpos.POSConstants.MODIFIER_GROUP_EXPLORER, mGroup);
		}
		else {
			mGroup = (ModifierGroupExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(mGroup);
	}

}
