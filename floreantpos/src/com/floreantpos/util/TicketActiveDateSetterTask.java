package com.floreantpos.util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import com.floreantpos.main.Application;
import com.floreantpos.main.PosWindow;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.GlassPane;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class TicketActiveDateSetterTask extends TimerTask {
	TicketDAO ticketDAO = new TicketDAO();
	
	@Override
	public void run() {
		PosWindow posWindow = Application.getPosWindow();
		GlassPane glassPane = (GlassPane) posWindow.getGlassPane();
		try {
			glassPane.setMessage(com.floreantpos.POSConstants.UPDATING_SYSTEM_PLEASE_WAIT_);
			glassPane.setVisible(true);
			
			Calendar currentTime = Calendar.getInstance();
			int currentDay = currentTime.get(Calendar.DAY_OF_MONTH);

			List<Ticket> openTickets = ticketDAO.findOpenTickets();
			for (Ticket ticket : openTickets) {
				Date activeDate = ticket.getActiveDate();
				if(activeDate == null) {
					activeDate = ticket.getCreateDate();
				}
				currentTime.setTime(activeDate);
				int activeDay = currentTime.get(Calendar.DAY_OF_MONTH);
				if (currentDay > activeDay) {
					currentTime.set(Calendar.DAY_OF_MONTH, currentDay);
					ticket.setActiveDate(currentTime.getTime());

					ticketDAO.update(ticket);
				}
			}
		} catch (Exception e) {
			POSMessageDialog.showError(posWindow, com.floreantpos.POSConstants.ERROR_MESSAGE, e);
		} finally {
			glassPane.setVisible(false);
		}
	}

}
