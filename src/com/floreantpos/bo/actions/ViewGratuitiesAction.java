package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.PosException;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.GratuityViewer2;

public class ViewGratuitiesAction extends AbstractAction {

	public ViewGratuitiesAction() {
		super(com.floreantpos.POSConstants.GRATUITY_ADMINISTRATION);
	}

	public ViewGratuitiesAction(String name) {
		super(name);
	}

	public ViewGratuitiesAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow backOfficeWindow = com.floreantpos.util.POSUtil.getBackOfficeWindow();
		
		try {
			GratuityViewer2 explorer = null;
			JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
			int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.GRATUITY_ADMINISTRATION);
			if (index == -1) {
				explorer = new GratuityViewer2();
				tabbedPane.addTab(com.floreantpos.POSConstants.GRATUITY_ADMINISTRATION, explorer);
			}
			else {
				explorer = (GratuityViewer2) tabbedPane.getComponentAt(index);
			}
			tabbedPane.setSelectedComponent(explorer);
			
		} catch(PosException x) {
			BOMessageDialog.showError(backOfficeWindow, x.getMessage(), x);
		} catch (Exception ex) {
			BOMessageDialog.showError(backOfficeWindow, com.floreantpos.POSConstants.ERROR_MESSAGE, ex);
		}
	}

}
