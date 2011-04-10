package com.floreantpos.report;

import java.util.Date;

import com.floreantpos.model.User;

public class PayrollReportData {
	User user;

	Date date;
	Date from, to;
	double totalHour;
	double rate;
	double payment;


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public double getTotalHour() {
		return totalHour;
	}

	public void setTotalHour(double totalHour) {
		this.totalHour = totalHour;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getPayment() {
		return payment;
	}

	public void setPayment(double payment) {
		this.payment = payment;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public void calculate() {
		long fromTime = from.getTime();
		long toTime = to.getTime();
		
		long milliseconds = toTime - fromTime;
		if(milliseconds < 0) {
			totalHour = 0;
			return;
		}
		
		double seconds = milliseconds / 1000.0;
		double minutes = seconds / 60.0;
        double hours = minutes / 60.0;
		
		double diff = toTime - fromTime;
		diff = diff / (1000 * 60 * 60 * 24);
		//System.out.println(from.toString() + ":" + diff);
		totalHour = hours;
		rate = user.getCostPerHour();
		payment = totalHour * rate;
		
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd hh:hh a");
		//System.out.println(df.format(to) + "-" + df.format(from) + "=" + totalHour);
	}
}
