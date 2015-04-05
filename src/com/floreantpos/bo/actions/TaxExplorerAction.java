package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.TaxExplorer;

public class TaxExplorerAction extends AbstractAction {

	public TaxExplorerAction() {
		super(com.floreantpos.POSConstants.TAX);
	}

	public TaxExplorerAction(String name) {
		super(name);
	}

	public TaxExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow backOfficeWindow = com.floreantpos.util.POSUtil.getBackOfficeWindow();
		
		TaxExplorer explorer = null;
		JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab(POSConstants.TAX_EXPLORER);
		if (index == -1) {
			explorer = new TaxExplorer();
			tabbedPane.addTab(POSConstants.TAX_EXPLORER, explorer);
		}
		else {
			explorer = (TaxExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}

}
