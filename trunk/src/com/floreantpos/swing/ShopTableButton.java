package com.floreantpos.swing;

import java.awt.Color;

import com.floreantpos.model.ShopTable;

public class ShopTableButton extends PosButton {
	private ShopTable shopTable;
	
	public ShopTableButton(ShopTable shopTable) {
		this.shopTable = shopTable;
		setText(shopTable.toString());
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
	
	@Override
	public void repaint() {
		if(shopTable != null && shopTable.isOccupied()) {
			setBackground(Color.red);
			setForeground(Color.black);
		}
		else if(shopTable != null && shopTable.isBooked()) {
			setBackground(Color.orange);
			setForeground(Color.black);
		}
		else {
			setBackground(Color.green);
			setForeground(Color.black);
		}
		super.repaint();
	}
}
