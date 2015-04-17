package com.floreantpos.actions;

import javax.swing.Action;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.UserPermission;

public class ShowBackofficeAction extends PosAction {

	public ShowBackofficeAction() {
		super(POSConstants.BACK_OFFICE_BUTTON_TEXT); //$NON-NLS-1$
		setRequiredPermission(UserPermission.VIEW_BACK_OFFICE);
	}

	public ShowBackofficeAction(boolean showText, boolean showIcon) {
		if (showText) {
			putValue(Action.NAME, UserPermission.VIEW_BACK_OFFICE); //$NON-NLS-1$
		}
		if (showIcon) {
			putValue(Action.SMALL_ICON, IconFactory.getIcon("backoffice.png")); //$NON-NLS-1$
		}
		
		setRequiredPermission(UserPermission.VIEW_BACK_OFFICE);
	}

	@Override
	public void execute() {
		BackOfficeWindow window = com.floreantpos.util.POSUtil.getBackOfficeWindow();
		if (window == null) {
			window = new BackOfficeWindow();
		}
		window.setVisible(true);
		window.toFront();
	}

}
