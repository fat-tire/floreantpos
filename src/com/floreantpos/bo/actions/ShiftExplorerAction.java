package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.ShiftExplorer;
import com.floreantpos.main.Application;

public class ShiftExplorerAction extends AbstractAction {

	public ShiftExplorerAction() {
		super(com.floreantpos.POSConstants.SHIFTS);
	}

	public ShiftExplorerAction(String name) {
		super(name);
	}

	public ShiftExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow backOfficeWindow = Application.getInstance().getBackOfficeWindow();
		
		ShiftExplorer explorer = null;
		JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.SHIFTS);
		if (index == -1) {
			explorer = new ShiftExplorer();
			tabbedPane.addTab(com.floreantpos.POSConstants.SHIFTS, explorer);
		}
		else {
			explorer = (ShiftExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}

}
