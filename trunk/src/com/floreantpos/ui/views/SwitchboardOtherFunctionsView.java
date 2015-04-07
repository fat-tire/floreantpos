package com.floreantpos.ui.views;

import com.floreantpos.ui.views.order.ViewPanel;

public class SwitchboardOtherFunctionsView extends ViewPanel {
	public static final String VIEW_NAME="afv";
	private static SwitchboardOtherFunctionsView instance;
	
	
	public static SwitchboardOtherFunctionsView getInstance() {
		if(instance == null) {
			instance = new SwitchboardOtherFunctionsView();
		}
		
		return instance;
	}
	
	@Override
	public String getViewName() {
		return VIEW_NAME;
	}

}
