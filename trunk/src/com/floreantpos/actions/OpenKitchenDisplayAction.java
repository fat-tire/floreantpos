package com.floreantpos.actions;

import java.awt.Window;

import com.floreantpos.demo.KitchenDisplayWindow;

public class OpenKitchenDisplayAction extends PosAction {

	public OpenKitchenDisplayAction() {
		super("KITCHEN ORDERS");
	}
	
	@Override
	public void execute() {
		Window[] windows = Window.getWindows();
		for (Window window : windows) {
			if(window instanceof KitchenDisplayWindow) {
				window.toFront();
				return;
			}
		}
		
		KitchenDisplayWindow window = new KitchenDisplayWindow();
		window.setVisible(true);
	}

}
