package com.floreantpos.ui.views.order;

import java.util.Calendar;

import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.util.PosGuiUtil;
import com.floreantpos.util.TicketAlreadyExistsException;

public class DefaultOrderServiceExtension implements OrderServiceExtension {

	@Override
	public String getName() {
		return "Order Handler";
	}

	@Override
	public String getDescription() {
		return "Default order handler";
	}
	
	@Override
	public void init() {
	}

	@Override
	public void createNewTicket(String ticketType) throws TicketAlreadyExistsException {
		int tableNumber = PosGuiUtil.captureTableNumber();
		if (tableNumber == -1) {
			return;
		}

		TicketDAO dao = TicketDAO.getInstance();

		Ticket ticket = dao.findTicketByTableNumber(tableNumber);
		if (ticket != null) {
			throw new TicketAlreadyExistsException(ticket);
		}

		int numberOfGuests = PosGuiUtil.captureGuestNumber();
		if (numberOfGuests == -1) {
			return;
		}

		Application application = Application.getInstance();

		ticket = new Ticket();
		ticket.setTicketType(ticketType);
		ticket.setTableNumber(tableNumber);
		ticket.setNumberOfGuests(numberOfGuests);
		ticket.setTerminal(application.getTerminal());
		ticket.setOwner(Application.getCurrentUser());
		ticket.setShift(application.getCurrentShift());

		Calendar currentTime = Calendar.getInstance();
		ticket.setCreateDate(currentTime.getTime());
		ticket.setCreationHour(currentTime.get(Calendar.HOUR_OF_DAY));

		OrderView.getInstance().setCurrentTicket(ticket);
		RootView.getInstance().showView(OrderView.VIEW_NAME);
	}

	@Override
	public void setCustomerToTicket(Ticket ticket) {
	}
	
	public void setDeliveryDate(Ticket ticket) {}

	@Override
	public void assignDriver(Ticket ticket) {
		
	};
}
