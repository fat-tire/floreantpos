package com.floreantpos.actions;

import java.awt.Window;

import com.floreantpos.POSConstants;
import com.floreantpos.demo.KitchenDisplayWindow;

public class ShowKitchenDisplayAction extends PosAction {

	public ShowKitchenDisplayAction() {
		super(POSConstants.KITCHEN_DISPLAY_BUTTON_TEXT); //$NON-NLS-1$
	}

	@Override
	public void execute() {
		Window[] windows = Window.getWindows();
		for (Window window : windows) {
			if(window instanceof KitchenDisplayWindow) {
				window.setVisible(true);
				window.toFront();
				return;
			}
		}
		
		KitchenDisplayWindow window = new KitchenDisplayWindow();
		window.setVisible(true);
	}

}
