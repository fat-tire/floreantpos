package com.floreantpos.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.floreantpos.model.AttendenceHistory;
import com.floreantpos.model.DrawerPullReport;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Shift;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.AttendenceHistoryDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TerminalDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.report.services.ReportService;
import com.floreantpos.swing.GlassPane;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.ShiftUtil;

public class AutoDrawerPullAction implements ActionListener {

	public void actionPerformed(ActionEvent e) {
		PosWindow posWindow = Application.getPosWindow();
		try {
			RestaurantDAO restaurantDAO = RestaurantDAO.getInstance();
			Restaurant restaurant = restaurantDAO.get(Integer.valueOf(1));
			if(!restaurant.isAutoDrawerPullEnable()) {
				return;
			}
			Calendar currentTime = Calendar.getInstance();
			
			int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
			int currentMin = currentTime.get(Calendar.MINUTE);
			
			if(currentHour >= restaurant.getDrawerPullHour() && currentMin >= restaurant.getDrawerPullMin() 
					&& currentMin < restaurant.getDrawerPullMin() + 1) {
				
			}
			else {
				return;
			}
			((GlassPane) posWindow.getGlassPane()).setMessage(com.floreantpos.POSConstants.PERFORMING_AUTO_DRAWER_PULL);
			posWindow.setGlassPaneVisible(true);
			DrawerPullReport report = ReportService.buildDrawerPullReport();

			TerminalDAO dao = new TerminalDAO();
			Terminal terminal = Application.getInstance().getTerminal();
			dao.resetCashDrawer(report, terminal, null);
			
			Shift currentShift = ShiftUtil.getCurrentShift();
			
			UserDAO userDAO = new UserDAO();
			List<User> loggedInUsers = userDAO.getClockedInUser(terminal);
			for (User user : loggedInUsers) {
				AttendenceHistoryDAO attendenceHistoryDAO = new AttendenceHistoryDAO();

				AttendenceHistory attendenceHistory = attendenceHistoryDAO.findHistoryByClockedInTime(user);
				if (attendenceHistory == null) {
					attendenceHistory = new AttendenceHistory();
					Date lastClockInTime = user.getLastClockInTime();
					Calendar c = Calendar.getInstance();
					c.setTime(lastClockInTime);
					attendenceHistory.setClockInTime(lastClockInTime);
					attendenceHistory.setClockInHour(Short.valueOf((short) c.get(Calendar.HOUR)));
					attendenceHistory.setUser(user);
					attendenceHistory.setTerminal(terminal);
					attendenceHistory.setShift(user.getCurrentShift());
				}

				user.doClockOut(attendenceHistory, currentShift, currentTime);
				user.doClockIn(terminal, currentShift, currentTime);
			}
		} catch (Exception ex) {
			POSMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, ex);
		} finally {
			posWindow.setGlassPaneVisible(false);
		}
	}

}
