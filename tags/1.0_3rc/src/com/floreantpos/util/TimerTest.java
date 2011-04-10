package com.floreantpos.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

public class TimerTest extends TimerTask {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		TimerTest test = new TimerTest();
//		
//		Timer timer = new Timer();
//		
//		Calendar c = Calendar.getInstance();
//		c.set(Calendar.MINUTE, 40);
//		Date date = c.getTime();
//		timer.scheduleAtFixedRate(test, date, 1000);
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy MMM dd HH:mm:s a");
		Date time = calendar.getTime();
		System.out.println("expected next launch: " + format.format(time));
		
		java.util.Timer activeDateScheduler = new java.util.Timer();
		activeDateScheduler.scheduleAtFixedRate(new TimerTest(), time, 1000);
	}
	int i = 0;
	@Override
	public void run() {
		System.out.println(++i);
	}

}
