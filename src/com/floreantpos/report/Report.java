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

import net.sf.jasperreports.view.JRViewer;

import com.floreantpos.model.Terminal;
import com.floreantpos.model.UserType;

public abstract class Report {
	public static final int REPORT_TYPE_1 = 0;
	public static final int REPORT_TYPE_2 = 1;

	private Date startDate;
	private Date endDate;
	private Terminal terminal;
	private UserType userType; 
	private int reportType = REPORT_TYPE_1;
	private boolean includeFreeItem = false;
	protected JRViewer viewer;

	public abstract void refresh() throws Exception;

	public abstract boolean isDateRangeSupported();

	public abstract boolean isTypeSupported();

	public JRViewer getViewer() {
		return viewer;
	}

	public Date getEndDate() {
		if (endDate == null) {
			return new Date();
		}
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getReportType() {
		return reportType;
	}

	public void setReportType(int reportType) {
		this.reportType = reportType;
	}

	public boolean isIncludedFreeItems() {
		return includeFreeItem;
	}

	public void setIncludeFreeItems(boolean includeFreeItem) {
		this.includeFreeItem = includeFreeItem;
	}

	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}
	
	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public Date getStartDate() {
		if (startDate == null) {
			return new Date();
		}
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

}
