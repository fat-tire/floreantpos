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
package com.floreantpos.demo;

import java.awt.Component;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.model.KitchenTicket;

public class KitchenTicketListPanel extends JPanel {
	private Set<KitchenTicket> existingTickets = new HashSet<KitchenTicket>();
	
	public KitchenTicketListPanel() {
		super(new MigLayout("filly")); //$NON-NLS-1$
	}

	public boolean addTicket(KitchenTicket ticket) {
		if(existingTickets.contains(ticket)) {
			return false;
		}
		
		existingTickets.add(ticket);
		super.add(new KitchenTicketView(ticket), "growy, width pref!"); //$NON-NLS-1$
		
		return true;
	}
	
	@Override
	public void remove(Component comp) {
		if(comp instanceof KitchenTicketView) {
			KitchenTicketView view = (KitchenTicketView) comp;
			existingTickets.remove(view.getTicket());
		}
		
		super.remove(comp);
	}
	
	@Override
	public void removeAll() {
		existingTickets.clear();
		
		Component[] components = getComponents();
		for (Component component : components) {
			if (component instanceof KitchenTicketView) {
				KitchenTicketView kitchenTicketView = (KitchenTicketView) component;
				kitchenTicketView.stopTimer();
			}
		}
		
		super.removeAll();
	}
}
