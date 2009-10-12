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
		super("Shifts");
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
		int index = tabbedPane.indexOfTab("Shifts");
		if (index == -1) {
			explorer = new ShiftExplorer();
			tabbedPane.addTab("Shifts", explorer);
		}
		else {
			explorer = (ShiftExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}

}
