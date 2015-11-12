package com.floreantpos.actions;

import javax.swing.Action;

import com.floreantpos.IconFactory;
import com.floreantpos.ui.views.SwitchboardOtherFunctionsView;
import com.floreantpos.ui.views.order.RootView;

public class ShowOtherFunctionsAction extends PosAction {

	public ShowOtherFunctionsAction() {
		super(); 
	}

	public ShowOtherFunctionsAction(boolean showText, boolean showIcon) {
		if (showIcon) {
			putValue(Action.SMALL_ICON, IconFactory.getIcon("other.png")); //$NON-NLS-1$
		}
	}

	@Override
	public void execute() {
		SwitchboardOtherFunctionsView view = SwitchboardOtherFunctionsView.getInstance();
		RootView.getInstance().showView(view);	
	}
}