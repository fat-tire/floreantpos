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

public class OpenTicketSummaryReport extends Report {

	public OpenTicketSummaryReport() {
		super();
	}

	@Override
	public void refresh() throws Exception {
		//Date date1 = DateUtils.startOfDay(getStartDate());
		//Date date2 = DateUtils.endOfDay(getEndDate());
		
		List<Ticket> tickets = TicketDAO.getInstance().findOpenTickets();
		TicketReportModel reportModel = new TicketReportModel();
		reportModel.setItems(tickets);
		
		HashMap map = new HashMap();
		ReportUtil.populateRestaurantProperties(map);
		map.put("reportTitle", Messages.getString("OpenTicketSummaryReport.0")); //$NON-NLS-1$ //$NON-NLS-2$
		map.put("reportTime", ReportService.formatFullDate(new Date())); //$NON-NLS-1$
		//map.put("dateRange", Application.formatDate(date1) + " to " + Application.formatDate(date2));
		map.put("terminalName", com.floreantpos.POSConstants.ALL); //$NON-NLS-1$
		
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
