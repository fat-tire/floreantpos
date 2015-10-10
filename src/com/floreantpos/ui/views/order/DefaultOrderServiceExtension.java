package com.floreantpos.ui.views.order;

import java.util.Calendar;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import com.floreantpos.Messages;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.extension.FloorLayoutPlugin;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.main.Application;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;
import com.floreantpos.util.PosGuiUtil;
import com.floreantpos.util.TicketAlreadyExistsException;

public class DefaultOrderServiceExtension implements OrderServiceExtension {

	@Override
	public String getName() {
		return Messages.getString("DefaultOrderServiceExtension.0"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return Messages.getString("DefaultOrderServiceExtension.1"); //$NON-NLS-1$
	}

	@Override
	public void init() {
	}

	@Override
	public void createNewTicket(OrderType ticketType) throws TicketAlreadyExistsException {
		List<ShopTable> tables = null;

		if (TerminalConfig.isShouldShowTableSelection()) {
			FloorLayoutPlugin floorLayoutPlugin = Application.getPluginManager().getPlugin(FloorLayoutPlugin.class);
			if (floorLayoutPlugin != null) {
				tables = floorLayoutPlugin.captureTableNumbers(null);
			}
			else {
				tables = PosGuiUtil.captureTable(null);
			}

			if (tables == null) {
				return;
			}
		}

		int numberOfGuests = 0;
		if (TerminalConfig.isShouldShowGuestSelection()) {
			numberOfGuests = PosGuiUtil.captureGuestNumber();
			if (numberOfGuests == -1) {
				return;
			}
		}

		Application application = Application.getInstance();

		Ticket ticket = new Ticket();
		ticket.setPriceIncludesTax(application.isPriceIncludesTax());
		ticket.setType(ticketType);
		ticket.setNumberOfGuests(numberOfGuests);
		ticket.setTerminal(application.getTerminal());
		ticket.setOwner(Application.getCurrentUser());
		ticket.setShift(application.getCurrentShift());

		if (tables != null) {
			for (ShopTable shopTable : tables) {
				shopTable.setServing(true);
				ticket.addTable(shopTable.getTableNumber());
			}
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
		//			POSMessageDialog.showError(Application.getPosWindow(), "Please select tickets of type HOME DELIVERY or PICKUP or DRIVE THRU");
		//			return false;
		//		}

		int due = (int) POSUtil.getDouble(ticket.getDueAmount());
		if (due != 0) {
			POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("DefaultOrderServiceExtension.2")); //$NON-NLS-1$
			return false;
		}

		int option = JOptionPane.showOptionDialog(Application.getPosWindow(), Messages.getString("DefaultOrderServiceExtension.3") + ticket.getId() + Messages.getString("DefaultOrderServiceExtension.4"), Messages.getString("DefaultOrderServiceExtension.5"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

		if (option != JOptionPane.OK_OPTION) {
			return false;
		}

		OrderController.closeOrder(ticket);

		return true;
	}

	@Override
	public void createCustomerMenu(JMenu menu) {
	}

	@Override
	public void configureBackofficeMenuBar(JMenuBar menuBar) {
	}
}
