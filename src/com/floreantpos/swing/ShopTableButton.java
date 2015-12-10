/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
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
	public boolean equals(Object obj) {
		if(!(obj instanceof ShopTableButton)) {
			return false;
		}
		
		ShopTableButton that = (ShopTableButton) obj;
		
		return this.shopTable.equals(that.shopTable);
	}
	
	@Override
	public int hashCode() {
		return this.shopTable.hashCode();
	}
	
	@Override
	public String toString() {
		return shopTable.toString();
	}
	
	public void update() {
		if(shopTable != null && shopTable.isServing()) {
			setEnabled(false);
			setBackground(Color.red);
			setForeground(Color.black);
		}
		else if(shopTable != null && shopTable.isBooked()) {
			setEnabled(false);
			setOpaque(true); 
			setBackground(Color.gray);
			setForeground(Color.black);
		}
		else {
			setEnabled(true);
			setBackground(Color.green);
			setForeground(Color.black);
		}
	}
}
