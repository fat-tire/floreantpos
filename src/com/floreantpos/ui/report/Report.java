package com.floreantpos.ui.report;

import java.util.Date;

import net.sf.jasperreports.view.JRViewer;

public abstract class Report {
	public static final int REPORT_TYPE_1 = 0;
	public static final int REPORT_TYPE_2 = 1;

	private Date startDate;
	private Date endDate;
	private int reportType = REPORT_TYPE_1;
	protected JRViewer viewer;

	public abstract void refresh() throws Exception;

	public abstract boolean isDateRangeSupported();

	public abstract boolean isTypeSupported();
	
	public JRViewer getViewer() {
		return viewer;
	}

	public Date getEndDate() {
		if(endDate == null) {
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

	public Date getStartDate() {
		if(startDate == null) {
			return new Date();
		}
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

}
