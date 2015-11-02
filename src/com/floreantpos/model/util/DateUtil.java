package com.floreantpos.model.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static Date startOfDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		return new Date(cal.getTimeInMillis());
	}

	public static Date endOfDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		return new Date(cal.getTimeInMillis());
	}
	
	public static boolean between(Date startDate, Date endDate, Date guniping) {
		if(startDate == null || endDate == null) {
			return false;
		}
		
		return (guniping.equals(startDate) || guniping.after(startDate)) && (guniping.equals(endDate) || guniping.before(endDate));
	}
}
