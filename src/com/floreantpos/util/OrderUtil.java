package com.floreantpos.util;

import java.util.Calendar;

import com.floreantpos.main.Application;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.Ticket;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.ui.views.order.RootView;

public class OrderUtil {

	public static void createNewTakeOutOrder(OrderType titcketType) {
		Application application = Application.getInstance();
	
		Ticket ticket = new Ticket();
		ticket.setPriceIncludesTax(application.isPriceIncludesTax());
		ticket.setType(titcketType);
		ticket.setTerminal(application.getTerminal());
		ticket.setOwner(Application.getCurrentUser());
		ticket.setShift(application.getCurrentShift());
	
		Calendar currentTime = Calendar.getInstance();
		ticket.setCreateDate(currentTime.getTime());
		ticket.setCreationHour(currentTime.get(Calendar.HOUR_OF_DAY));
	
		OrderView.getInstance().setCurrentTicket(ticket);
		RootView.getInstance().showView(OrderView.VIEW_NAME);
	}

}
