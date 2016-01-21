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
import java.util.List;

import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;

public class ShopTableButton extends PosButton {
	private ShopTable shopTable;
	
	public ShopTableButton(ShopTable shopTable) {
		this.shopTable = shopTable;
		setText(shopTable.toString());
		
		update();
	}
	
	public int getId(){
		return shopTable.getId(); 
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
			
			//setEnabled(false);
			setBackground(Color.red);
			setForeground(Color.BLACK);
		}
		else if(shopTable != null && shopTable.isBooked()) {
			
			setEnabled(false);
			setOpaque(true); 
			setBackground(Color.orange);
			setForeground(Color.BLACK);
		}
		else {
			
			setEnabled(true);
			setBackground(Color.white);
			setForeground(Color.black);
		}
	}
	
	public Ticket getTicket(){
		List<Ticket> openTickets = TicketDAO.getInstance().findOpenTickets();
		Ticket selectedTicket=null; 
		for (Ticket ticket : openTickets) {
			if (ticket.getTableNumbers().contains(getId())) {
				selectedTicket=ticket; 

			}
		}
		return selectedTicket;
	}
}
