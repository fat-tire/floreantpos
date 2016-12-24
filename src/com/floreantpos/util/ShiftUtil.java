/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.floreantpos.model.Shift;
import com.floreantpos.model.dao.ShiftDAO;

public class ShiftUtil {

	private static final String DEFAULT_SHIFT = "DEFAULT SHIFT"; //$NON-NLS-1$
	private static final Calendar calendar = Calendar.getInstance();
	private static final Calendar calendar2 = Calendar.getInstance();
	private static final NumberFormat format = new DecimalFormat("00"); //$NON-NLS-1$
	
	static {
		calendar.clear();
	}

	/**
	 * For shift, we only care for hour and minute, not date.
	 * 
	 * @param shiftTime
	 * @return
	 */
	public static Date formatShiftTime(Date shiftTime) {
		calendar.clear();
		calendar2.setTime(shiftTime);

		calendar.set(Calendar.HOUR, calendar2.get(Calendar.HOUR));
		calendar.set(Calendar.MINUTE, calendar2.get(Calendar.MINUTE));
		calendar.set(Calendar.AM_PM, calendar2.get(Calendar.AM_PM));

		return calendar.getTime();
	}

	public static Date buildShiftStartTime(int startHour, int startMin, int startAmPm, int endHour, int endMin, int endAmPm) {
		startHour = startHour == 12 ? 0 : startHour;
		
		calendar.clear();

		calendar.set(Calendar.HOUR, startHour);
		calendar.set(Calendar.MINUTE, startMin);

		calendar.set(Calendar.AM_PM, startAmPm);
		
		return calendar.getTime();
	}
	
	public static Date buildShiftEndTime(int startHour, int startMin, int startAmPm, int endHour, int endMin, int endAmPm) {
		endHour = endHour == 12 ? 0 : endHour;
		
		calendar.clear();

		calendar.set(Calendar.HOUR, endHour);
		calendar.set(Calendar.MINUTE, endMin);

		calendar.set(Calendar.AM_PM, endAmPm);
		
		if(startAmPm == Calendar.PM && endAmPm == Calendar.AM) {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return calendar.getTime();
	}

	
	public static String buildShiftTimeRepresentation(Date shiftTime) {
		calendar.setTime(shiftTime);
		
		String s = ""; //$NON-NLS-1$
		s = format.format(calendar.get(Calendar.HOUR) == 0 ? 12 : calendar.get(Calendar.HOUR));
		s += ":" + format.format(calendar.get(Calendar.MINUTE)); //$NON-NLS-1$
		s += calendar.get(Calendar.AM_PM) == Calendar.AM ? " AM" : " PM"; //$NON-NLS-1$ //$NON-NLS-2$
		return s;
	}
	
	public static String getDateRepresentation(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd hh:mm:ss a"); //$NON-NLS-1$
		return formatter.format(date);
	}
	
	public static Shift getCurrentShift() {
		Calendar calendar = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar.clear();
		
		calendar.set(Calendar.HOUR, calendar2.get(Calendar.HOUR));
		calendar.set(Calendar.MINUTE, calendar2.get(Calendar.MINUTE));
		calendar.set(Calendar.AM_PM, calendar2.get(Calendar.AM_PM));
		
		Date currentTime = calendar.getTime();
		
		ShiftDAO shiftDAO = new ShiftDAO();
		List<Shift> shifts = shiftDAO.findAll();
		
		Shift defaultShift = findDefaultShift(shifts);

		Shift currentShift = findCurrentShift(currentTime, shifts);
		if(currentShift != null) {
			return currentShift;
		}
		
		calendar.add(Calendar.DATE, 1);
		currentTime = calendar.getTime();
		
		currentShift = findCurrentShift(currentTime, shifts);
		if(currentShift != null) {
			return currentShift;
		}
		
		if(defaultShift == null) {
			return getDefaultShift();
		}
		
		return defaultShift;
	}

	private static Shift findDefaultShift(List<Shift> shifts) {
		for (Iterator iterator = shifts.iterator(); iterator.hasNext();) {
			Shift shift = (Shift) iterator.next();
			
			if(DEFAULT_SHIFT.equalsIgnoreCase(shift.getName()) && shift.getShiftLength() == 86400000) {
				iterator.remove();
				return shift;
			}
		}
		
		return null;
	}

	private static Shift findCurrentShift(Date currentTime, List<Shift> shifts) {
		for (Shift shift : shifts) {
			Date startTime = new Date(shift.getStartTime().getTime());
			Date endTime = new Date(shift.getEndTime().getTime());
			
			if(currentTime.after(startTime) && currentTime.before(endTime)) {
				return shift;
			}
		}
		
		return null;
	}
	
	private static Shift getDefaultShift() {
		Calendar calendar = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		
		calendar.clear();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.AM_PM, Calendar.AM);

		calendar2.clear();
		calendar2.add(Calendar.DATE, 1);
		calendar2.set(Calendar.HOUR, 0);
		calendar2.set(Calendar.MINUTE, 0);
		calendar2.set(Calendar.AM_PM, Calendar.AM);
		
		Shift defaultShift = new Shift();
		defaultShift.setName(DEFAULT_SHIFT);
		defaultShift.setStartTime(calendar.getTime());
		defaultShift.setEndTime(calendar2.getTime());
		defaultShift.setShiftLength(calendar2.getTimeInMillis() - calendar.getTimeInMillis());
		
		ShiftDAO shiftDAO = ShiftDAO.getInstance();
		shiftDAO.saveOrUpdate(defaultShift);
		
		return defaultShift;
	}
	
//	public static void adjustUserShiftAndClockIn(User user, Shift currentShift) {
//		Application application = Application.getInstance();
//		Calendar currentTime = Calendar.getInstance();
//
//		if (user.isClockedIn() != null && user.isClockedIn().booleanValue()) {
//			Shift userShift = user.getCurrentShift();
//			Date userLastClockInTime = user.getLastClockInTime();
//			long elaspedTimeSinceLastLogin = Math.abs(currentTime.getTimeInMillis() - userLastClockInTime.getTime());
//
//			if (userShift != null) {
//				if (!userShift.equals(currentShift)) {
//					reClockInUser(currentTime, user, currentShift);
//				}
//				else if (userShift.getShiftLength() != null && (elaspedTimeSinceLastLogin >= userShift.getShiftLength())) {
//					reClockInUser(currentTime, user, currentShift);
//				}
//			}
//			else {
//				user.doClockIn(application.getTerminal(), currentShift, currentTime);
//			}
//		}
//		else {
//			user.doClockIn(application.getTerminal(), currentShift, currentTime);
//		}
//	}
	
//	private static void reClockInUser(Calendar currentTime, User user, Shift currentShift) {
//		POSMessageDialog.showMessage("You will be clocked out from previous Shift");
//
//		Application application = Application.getInstance();
//		AttendenceHistoryDAO attendenceHistoryDAO = new AttendenceHistoryDAO();
//
//		AttendenceHistory attendenceHistory = attendenceHistoryDAO.findHistoryByClockedInTime(user);
//		if (attendenceHistory == null) {
//			attendenceHistory = new AttendenceHistory();
//			Date lastClockInTime = user.getLastClockInTime();
//			Calendar c = Calendar.getInstance();
//			c.setTime(lastClockInTime);
//			attendenceHistory.setClockInTime(lastClockInTime);
//			attendenceHistory.setClockInHour(Short.valueOf((short) c.get(Calendar.HOUR)));
//			attendenceHistory.setUser(user);
//			attendenceHistory.setTerminal(application.getTerminal());
//			attendenceHistory.setShift(user.getCurrentShift());
//		}
//		user.doClockOut(attendenceHistory, currentShift, currentTime);
//		user.doClockIn(application.getTerminal(), currentShift, currentTime);
//	}
}
