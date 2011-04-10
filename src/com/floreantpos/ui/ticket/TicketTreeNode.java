package com.floreantpos.ui.ticket;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;

public class TicketTreeNode extends DefaultMutableTreeNode {
	
	public TicketTreeNode() {
		
	}
	
	public TicketTreeNode(Ticket ticket) {
		super(ticket);
	}
	
	@Override
	public int getChildCount() {
		Ticket ticket = (Ticket) getUserObject();
		if(ticket == null) {
			return 0;
		}
		
		List<TicketItem> ticketItems = ticket.getTicketItems();
		if(ticketItems == null) {
			return 0;
		}
		int size = ticketItems.size();
		//if size is less than 30, then return 30. this is required to show grid.
		if(size < 30) {
			return 30;
		}
		return size;
	}

	@Override
	public TreeNode getChildAt(int index) {
		Ticket ticket = (Ticket) getUserObject();
		if(ticket == null) {
			return null;
		}
		
		List<TicketItem> ticketItems = ticket.getTicketItems();
		if(ticketItems == null) {
			return null;
		}
		
		return new DefaultMutableTreeNode(ticketItems.get(index));
	}
	
	public Ticket getTicket() {
		return (Ticket) getUserObject();
	}
	
	public void setTicket(Ticket ticket) {
		setUserObject(ticket);
	}
}
