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
package com.floreantpos.report;

import java.util.Date;

import com.floreantpos.model.User;

public class AttendanceReportData {
	User user;

	String name;
	Date clockIn;
	Date clockOut;
	double workTime;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getClockIn() {
		return clockIn;
	}

	public void setClockIn(Date clockIn) {
		this.clockIn = clockIn;
	}

	public Date getClockOut() {
		return clockOut;
	}

	public void setClockOut(Date clockOut) {
		this.clockOut = clockOut;
	}

	public double getWorkTime() {
		return workTime;
	}

	public void setWorkTime(double workTime) {
		this.workTime = workTime;
	}

	public void calculate() {
		long cin = clockIn.getTime();
		long cout = clockOut.getTime();

		long milliseconds = cout - cin;
		if (milliseconds < 0) {
			workTime = 0;
			return;
		}

		double seconds = milliseconds / 1000.0;
		double minutes = seconds / 60.0;
		double hours = minutes / 60.0;

		workTime = hours;
	}

}
