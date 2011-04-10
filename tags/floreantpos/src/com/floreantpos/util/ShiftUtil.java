package com.floreantpos.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.floreantpos.model.Shift;
import com.floreantpos.model.dao.ShiftDAO;

public class ShiftUtil {

	private static final Calendar calendar = Calendar.getInstance();
	private static final Calendar calendar2 = Calendar.getInstance();
	private static final NumberFormat format = new DecimalFormat("00");
	
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
		
		String s = "";
		s = format.format(calendar.get(Calendar.HOUR) == 0 ? 12 : calendar.get(Calendar.HOUR));
		s += ":" + format.format(calendar.get(Calendar.MINUTE));
		s += calendar.get(Calendar.AM_PM) == Calendar.AM ? " AM" : " PM";
		return s;
	}
	
	public static String getDateRepresentation(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd hh:mm:ss a");
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

		for (Shift shift : shifts) {
			Date startTime = new Date(shift.getStartTime().getTime());
			Date endTime = new Date(shift.getEndTime().getTime());
			
			if(currentTime.after(startTime) && currentTime.before(endTime)) {
				return shift;
			}
		}
		
		calendar.add(Calendar.DATE, 1);
		currentTime = calendar.getTime();
		for (Shift shift : shifts) {
			Date startTime = new Date(shift.getStartTime().getTime());
			Date endTime = new Date(shift.getEndTime().getTime());
			
			if(currentTime.after(startTime) && currentTime.before(endTime)) {
				return shift;
			}
		}
		
		return null;
	}
}
