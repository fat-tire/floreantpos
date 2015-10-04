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
