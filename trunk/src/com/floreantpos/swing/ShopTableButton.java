package com.floreantpos.swing;

import java.awt.Color;

import com.floreantpos.model.ShopTable;

public class ShopTableButton extends PosButton {
	private ShopTable shopTable;
	
	public ShopTableButton(ShopTable shopTable) {
		this.shopTable = shopTable;
		setText(shopTable.toString());
		
		update();
	}

	public void setShopTable(ShopTable shopTable) {
		this.shopTable = shopTable;
	}
	
	public ShopTable getShopTable() {
		return shopTable;
	}
	
	@Override
	public String toString() {
		return shopTable.toString();
	}
	
	public void update() {
		if(shopTable != null && shopTable.isOccupied()) {
			setEnabled(false);
			setBackground(Color.red);
			setForeground(Color.black);
		}
		else if(shopTable != null && shopTable.isBooked()) {
			setEnabled(false);
			setBackground(Color.orange);
			setForeground(Color.black);
		}
		else {
			setEnabled(true);
			setBackground(Color.green);
			setForeground(Color.black);
		}
	}
}
