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
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JRViewer;

import com.floreantpos.Messages;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.service.ReportService;
import com.floreantpos.util.CurrencyUtil;

public class OpenTicketSummaryReport extends Report {

	public OpenTicketSummaryReport() {
		super();
	}

	@Override
	public void refresh() throws Exception {
		//Date date1 = DateUtils.startOfDay(getStartDate());
		//Date date2 = DateUtils.endOfDay(getEndDate());
		
		
		List<Ticket> tickets = TicketDAO.getInstance().findOpenTickets(getTerminal(), getUserType());
		TicketReportModel reportModel = new TicketReportModel();
		reportModel.setItems(tickets);
		reportModel.calculateGrandTotal(); 
		
		HashMap map = new HashMap();
		ReportUtil.populateRestaurantProperties(map);
		map.put("reportTitle", Messages.getString("OpenTicketSummaryReport.0")); //$NON-NLS-1$ //$NON-NLS-2$
		map.put("reportTime", ReportService.formatFullDate(new Date())); //$NON-NLS-1$
		//map.put("dateRange", Application.formatDate(date1) + " to " + Application.formatDate(date2));
		map.put("userType", getUserType() == null ? com.floreantpos.POSConstants.ALL : getUserType().getName()); //$NON-NLS-1$
		map.put("terminalName", getTerminal() == null ? com.floreantpos.POSConstants.ALL : getTerminal().getName()); //$NON-NLS-1$
		map.put("currency", Messages.getString("SalesReport.8") + CurrencyUtil.getCurrencyName() + " (" + CurrencyUtil.getCurrencySymbol() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		map.put("grandTotal", reportModel.getGrandTotalAsString()); //$NON-NLS-1$
		
		JasperReport masterReport = ReportUtil.getReport("open_ticket_summary_report"); //$NON-NLS-1$
		JasperPrint print = JasperFillManager.fillReport(masterReport, map, new JRTableModelDataSource(reportModel));
		viewer = new JRViewer(print);
	}

	@Override
	public boolean isDateRangeSupported() {
		return false;
	}

	@Override
	public boolean isTypeSupported() {
		return false;
	}

}
