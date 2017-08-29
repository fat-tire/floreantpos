package com.floreantpos.model.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

import com.floreantpos.model.ShopTableStatus;
import com.floreantpos.model.ShopTableTicket;
import com.floreantpos.model.TableStatus;
import com.floreantpos.model.Ticket;

public class ShopTableStatusDAO extends BaseShopTableStatusDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public ShopTableStatusDAO() {
	}

	public void addTicketsToShopTableStatus(List<Integer> tableNumbers, List<Ticket> tickets, Session session) {
		if (tableNumbers == null || tableNumbers.isEmpty() || tickets == null)
			return;
		for (Integer tableNumber : tableNumbers) {
			ShopTableStatus shopTableStatus = get(tableNumber);
			if (shopTableStatus == null) {
				shopTableStatus = new ShopTableStatus();
				shopTableStatus.setId(tableNumber);
			}
			shopTableStatus.setTableStatus(TableStatus.Seat);
			shopTableStatus.addToTableTickets(tickets);
			if (session == null)
				saveOrUpdate(shopTableStatus);
			else
				session.saveOrUpdate(shopTableStatus);
		}
	}

	public void removeTicketFromShopTableStatus(Ticket ticket, Session session) {
		if (ticket == null)
			return;

		List<Integer> tableNumbers = ticket.getTableNumbers();
		if (tableNumbers == null || tableNumbers.isEmpty())
			return;

		for (Integer tableNumber : tableNumbers) {
			ShopTableStatus shopTableStatus = get(tableNumber);
			if (shopTableStatus == null)
				return;
			List<ShopTableTicket> ticketNumbers = shopTableStatus.getTicketNumbers();
			if (ticketNumbers != null) {
				for (Iterator iterator = ticketNumbers.iterator(); iterator.hasNext();) {
					ShopTableTicket shopTableTicket = (ShopTableTicket) iterator.next();
					if (shopTableTicket.getTicketId().equals(ticket.getId())) {
						iterator.remove();
					}
				}
			}
			if (ticketNumbers == null || ticketNumbers.isEmpty()) {
				shopTableStatus.setTicketNumbers(null);
				shopTableStatus.setTableStatus(TableStatus.Available);
			}
			if (session == null)
				saveOrUpdate(shopTableStatus);
			else
				session.saveOrUpdate(shopTableStatus);
		}
	}
}