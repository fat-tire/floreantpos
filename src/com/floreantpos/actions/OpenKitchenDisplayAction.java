package com.floreantpos.actions;

import com.floreantpos.demo.KitchenDisplayWindow;

public class OpenKitchenDisplayAction extends PosAction {

	public OpenKitchenDisplayAction() {
		super("KITCHEN ORDERS");
	}
	
	@Override
	public void execute() {
		KitchenDisplayWindow.instance.setVisible(true);
	}

}
