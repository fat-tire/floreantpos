package com.floreantpos.actions;

import java.util.Calendar;
import java.util.Date;

import javax.swing.Action;
import javax.swing.JOptionPane;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.AttendenceHistory;
import com.floreantpos.model.Shift;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.AttendenceHistoryDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;

public class ClockoutAction extends PosAction {

	public ClockoutAction() {
		super(Messages.getString("Clockout")); //$NON-NLS-1$
	}

	public ClockoutAction(boolean showText, boolean showIcon) {
		if (showText) {
			putValue(Action.NAME, Messages.getString("Clockout")); //$NON-NLS-1$
		}
		if (showIcon) {
			putValue(Action.SMALL_ICON, IconFactory.getIcon("clock_out.png")); //$NON-NLS-1$
		}
	}

	@Override
	public void execute() {
		int option = POSMessageDialog.showYesNoQuestionDialog(POSUtil.getFocusedWindow(), POSConstants.CONFIRM_CLOCK_OUT, POSConstants.CONFIRM);
		
		if (option != JOptionPane.YES_OPTION) {
			return;
		}

		User user = Application.getCurrentUser();
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
			attendenceHistory.setTerminal(Application.getInstance().getTerminal());
			attendenceHistory.setShift(user.getCurrentShift());
		}

		Shift shift = user.getCurrentShift();
		Calendar calendar = Calendar.getInstance();

		user.doClockOut(attendenceHistory, shift, calendar);

		Application.getInstance().doLogout();
	}

}
