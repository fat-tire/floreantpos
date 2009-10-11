package com.mdss.pos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.mdss.pos.bo.ui.BackOfficeWindow;
import com.mdss.pos.bo.ui.explorer.ModifierExplorer;
import com.mdss.pos.main.Application;

public class ModifierExplorerAction extends AbstractAction {

	public ModifierExplorerAction() {
		super("Menu Modifiers");
	}

	public ModifierExplorerAction(String name) {
		super(name);
	}

	public ModifierExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		//BackOfficeWindow backOfficeWindow = Application.getInstance().getBackOfficeWindow();
		//backOfficeWindow.getTabbedPane().addTab("Modifier exploere", new ModifierExplorer());

		BackOfficeWindow backOfficeWindow = Application.getInstance().getBackOfficeWindow();
		JTabbedPane tabbedPane;
		ModifierExplorer modifier;
		tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab("Modifier exploere");
		if (index == -1) {
			modifier = new ModifierExplorer();
			tabbedPane.addTab("Modifier exploere", modifier);
		}
		else {
			modifier = (ModifierExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(modifier);
	}

}
