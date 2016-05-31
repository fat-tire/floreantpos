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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import com.floreantpos.swing.ListTableModel;

public class AttendanceReportModel extends ListTableModel {
	SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMMdd  HH:mm"); //$NON-NLS-1$

	DecimalFormat decimalFormat = new DecimalFormat("0.00"); //$NON-NLS-1$

	public AttendanceReportModel() {
		super(new String[] { "employeeId", "employeeName", "clockIn", "clockOut", "workTime" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		AttendanceReportData data = (AttendanceReportData) rows.get(rowIndex);

		switch (columnIndex) {
			case 0:
				return String.valueOf(data.getUser().getUserId());

			case 1:
				return data.getUser().getFirstName() + " " + data.getUser().getLastName(); //$NON-NLS-1$

			case 2:
				return dateFormat2.format(data.getClockIn());

			case 3:
				return dateFormat2.format(data.getClockOut());

			case 4:
				return decimalFormat.format(data.getWorkTime());

		}
		return null;
	}

}
