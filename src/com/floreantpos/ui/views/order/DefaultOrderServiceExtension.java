package com.floreantpos.ui.views.order;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JOptionPane;

import com.floreantpos.extension.FloorLayoutPlugin;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.main.Application;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;
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
	public void createNewTicket(TicketType ticketType) throws TicketAlreadyExistsException {
		List<ShopTable> tables = null;
		
		FloorLayoutPlugin floorLayoutPlugin = Application.getPluginManager().getPlugin(FloorLayoutPlugin.class);
		if(floorLayoutPlugin != null) {
			tables = floorLayoutPlugin.captureTableNumbers(null);
		}
		else {
			tables = PosGuiUtil.captureTable(null);
		}

		if(tables == null) {
			return;
		}
		
		int numberOfGuests = PosGuiUtil.captureGuestNumber();
		if (numberOfGuests == -1) {
			return;
		}

		Application application = Application.getInstance();
		
		Ticket ticket = new Ticket();
		ticket.setPriceIncludesTax(application.isPriceIncludesTax());
		ticket.setType(ticketType);
		ticket.setNumberOfGuests(numberOfGuests);
		ticket.setTerminal(application.getTerminal());
		ticket.setOwner(Application.getCurrentUser());
		ticket.setShift(application.getCurrentShift());
		
		for (ShopTable shopTable : tables) {
			shopTable.setOccupied(true);
			ticket.addTotables(shopTable);
		}

		Calendar currentTime = Calendar.getInstance();
		ticket.setCreateDate(currentTime.getTime());
		ticket.setCreationHour(currentTime.get(Calendar.HOUR_OF_DAY));

		OrderView.getInstance().setCurrentTicket(ticket);
		RootView.getInstance().showView(OrderView.VIEW_NAME);
	}

	@Override
	public void setCustomerToTicket(int ticketId) {
	}

	public void setDeliveryDate(int ticketId) {
	}

	@Override
	public void assignDriver(int ticketId) {

	};

	@Override
	public boolean finishOrder(int ticketId) {
		Ticket ticket = TicketDAO.getInstance().get(ticketId);

//		if (ticket.getType() == TicketType.DINE_IN) {
//			POSMessageDialog.showError("Please select tickets of type HOME DELIVERY or PICKUP or DRIVE THRU");
//			return false;
//		}

		int due = (int) POSUtil.getDouble(ticket.getDueAmount());
		if (due != 0) {
			POSMessageDialog.showError("Ticket is not fully paid");
			return false;
		}

		int option = JOptionPane.showOptionDialog(Application.getPosWindow(), "Ticket# " + ticket.getId() + " will be closed.", "Confirm",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

		if (option != JOptionPane.OK_OPTION) {
			return false;
		}

		ticket.setClosed(true);
		ticket.setClosingDate(new Date());
		TicketDAO.getInstance().saveOrUpdate(ticket);

		User driver = ticket.getAssignedDriver();
		if (driver != null) {
			driver.setAvailableForDelivery(true);
			UserDAO.getInstance().saveOrUpdate(driver);
		}

		return true;
	}

	@Override
	public void createCustomerMenu(JMenu menu) {
	}
}
