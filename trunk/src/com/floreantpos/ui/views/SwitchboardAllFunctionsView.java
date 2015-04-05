package com.floreantpos.ui.views;

import com.floreantpos.ui.views.order.ViewPanel;

public class SwitchboardAllFunctionsView extends ViewPanel {
	public static final String VIEW_NAME="afv";
	private static SwitchboardAllFunctionsView instance;
	
	
	public static SwitchboardAllFunctionsView getInstance() {
		if(instance == null) {
			instance = new SwitchboardAllFunctionsView();
		}
		
		return instance;
	}
	
	@Override
	public String getViewName() {
		return VIEW_NAME;
	}

}
