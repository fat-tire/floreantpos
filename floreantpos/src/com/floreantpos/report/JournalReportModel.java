package com.floreantpos.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.main.Application;

public class JournalReportModel {
	private Date fromDate;

	private Date toDate;

	private Date reportTime;

	private List<JournalReportData> reportDatas = new ArrayList<JournalReportData>();

	private JournalReportTableModel tableModel;

	public JournalReportTableModel getTableModel() {
		if (tableModel == null) {
			tableModel = new JournalReportTableModel(reportDatas);
		}
		return tableModel;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public void addReportData(JournalReportData data) {
		reportDatas.add(data);
	}

	public static class JournalReportData {
		private Integer refId;
		private Date time;
		private String action;
		private String userInfo;
		private String comments;

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}

		public String getComments() {
			return comments;
		}

		public void setComments(String comments) {
			this.comments = comments;
		}

		public Integer getRefId() {
			return refId;
		}

		public void setRefId(Integer refId) {
			this.refId = refId;
		}

		public Date getTime() {
			return time;
		}

		public void setTime(Date time) {
			this.time = time;
		}

		public String getUserInfo() {
			return userInfo;
		}

		public void setUserInfo(String userInfo) {
			this.userInfo = userInfo;
		}
	}

	public static class JournalReportTableModel extends ListTableModel {

		public JournalReportTableModel(List<JournalReportData> datas) {
			super(new String[] { "refId", "time", "action", "user", "comment" }, datas);
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			JournalReportData data = (JournalReportData) rows.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return String.valueOf(data.getRefId());

			case 1:
				return Application.formatDate(data.getTime());

			case 2:
				return data.getAction();

			case 3:
				return data.getUserInfo();

			case 4:
				return data.getComments();
			}

			return null;
		}

	}
}
