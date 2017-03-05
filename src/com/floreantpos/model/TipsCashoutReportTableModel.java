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
package com.floreantpos.model;

import java.util.List;

import com.floreantpos.swing.ListTableModel;
import com.floreantpos.util.NumberUtil;

public class TipsCashoutReportTableModel extends ListTableModel {
	public TipsCashoutReportTableModel(List<TipsCashoutReportData> datas) {
		super(new String[] { "TICKET ID", "", "TICKET TOTAL", "TIPS" }, datas); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	public TipsCashoutReportTableModel(List<TipsCashoutReportData> datas, String[] columnNames) {
		super(columnNames, datas);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		TipsCashoutReportData data = (TipsCashoutReportData) rows.get(rowIndex);

		switch (columnIndex) {
			case 0:
				return data.getTicketId();

			case 1:
				return data.getSaleType();

			case 2:
				return NumberUtil.formatNumber(data.getTicketTotal());

			case 3:
				return NumberUtil.formatNumber(data.getTips());
		}

		return null;
	}
}