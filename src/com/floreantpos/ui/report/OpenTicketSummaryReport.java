package com.floreantpos.ui.report;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JRViewer;

import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.TicketReportModel;
import com.floreantpos.report.services.ReportService;

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
		map.put("reportTitle", "============================ Open Ticket Summary =============================");
		map.put("reportTime", ReportService.formatFullDate(new Date()));
		//map.put("dateRange", Application.formatDate(date1) + " to " + Application.formatDate(date2));
		map.put("terminalName", com.floreantpos.POSConstants.ALL);
		
		JasperReport masterReport = (JasperReport) JRLoader.loadObject(OpenTicketSummaryReport.class.getResource("/com/floreantpos/ui/report/open_ticket_summary_report.jasper"));
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
