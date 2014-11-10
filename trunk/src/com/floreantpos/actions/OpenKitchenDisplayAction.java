package com.floreantpos.actions;

import com.floreantpos.demo.KitchenDisplay;

public class OpenKitchenDisplayAction extends PosAction {

	public OpenKitchenDisplayAction() {
		super("KITCHEN ORDERS");
	}
	
	@Override
	public void execute() {
		KitchenDisplay.instance.setVisible(true);
	}

}
