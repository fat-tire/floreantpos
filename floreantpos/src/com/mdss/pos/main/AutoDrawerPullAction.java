package com.mdss.pos.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mdss.pos.model.AttendenceHistory;
import com.mdss.pos.model.DrawerPullReport;
import com.mdss.pos.model.Restaurant;
import com.mdss.pos.model.Shift;
import com.mdss.pos.model.Terminal;
import com.mdss.pos.model.User;
import com.mdss.pos.model.dao.AttendenceHistoryDAO;
import com.mdss.pos.model.dao.RestaurantDAO;
import com.mdss.pos.model.dao.TerminalDAO;
import com.mdss.pos.model.dao.UserDAO;
import com.mdss.pos.report.services.ReportService;
import com.mdss.pos.swing.GlassPane;
import com.mdss.pos.ui.dialog.POSMessageDialog;
import com.mdss.pos.util.ShiftUtil;

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
			((GlassPane) posWindow.getGlassPane()).setMessage("PERFORMING AUTO DRAWER PULL");
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
			POSMessageDialog.showError("An error occured while performing auto drawer pull", ex);
		} finally {
			posWindow.setGlassPaneVisible(false);
		}
	}

}
