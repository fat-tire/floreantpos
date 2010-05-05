package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.CookingInstructionExplorer;
import com.floreantpos.main.Application;

public class CookingInstructionExplorerAction extends AbstractAction {

	public CookingInstructionExplorerAction() {
		super(com.floreantpos.POSConstants.COOKING_INSTRUCTIONS);
	}

	public CookingInstructionExplorerAction(String name) {
		super(name);
	}

	public CookingInstructionExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow backOfficeWindow = Application.getInstance().getBackOfficeWindow();
		
		CookingInstructionExplorer explorer = null;
		JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.COOKING_INSTRUCTIONS);
		if (index == -1) {
			explorer = new CookingInstructionExplorer();
			tabbedPane.addTab(com.floreantpos.POSConstants.COOKING_INSTRUCTIONS, explorer);
		}
		else {
			explorer = (CookingInstructionExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}

}
