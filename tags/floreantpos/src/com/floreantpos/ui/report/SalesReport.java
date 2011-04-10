package com.floreantpos.ui.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdesktop.swingx.calendar.DateUtils;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JRViewer;

import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.ReportItem;
import com.floreantpos.report.SalesReportModel;
import com.floreantpos.report.SalesReportModelFactory;
import com.floreantpos.report.services.ReportService;

public class SalesReport extends Report {
	private SalesReportModel itemReportModel;
	private SalesReportModel modifierReportModel;

	public SalesReport() {
		super();
	}

	@Override
	public void refresh() throws Exception {
		createModels();

		SalesReportModel itemReportModel = this.itemReportModel;
		SalesReportModel modifierReportModel = this.modifierReportModel;

		JasperReport itemReport = (JasperReport) JRLoader.loadObject(SalesReportModelFactory.class.getResource("/com/floreantpos/ui/report/sales_sub_report.jasper"));
		JasperReport modifierReport = (JasperReport) JRLoader.loadObject(SalesReportModelFactory.class.getResource("/com/floreantpos/ui/report/sales_sub_report.jasper"));

		HashMap map = new HashMap();
		ReportUtil.populateRestaurantProperties(map);
		map.put("reportTitle", "================================= Sales Report ================================");
		map.put("reportTime", ReportService.formatFullDate(new Date()));
		map.put("dateRange", ReportService.formatShortDate(getStartDate()) + " to " + ReportService.formatShortDate(getEndDate()));
		map.put("terminalName", com.floreantpos.POSConstants.ALL);
		map.put("itemDataSource", new JRTableModelDataSource(itemReportModel));
		map.put("modifierDataSource", new JRTableModelDataSource(modifierReportModel));
		map.put("currencySymbol", Application.getCurrencySymbol());
		map.put("itemGrandTotal", itemReportModel.getGrandTotalAsString());
		map.put("modifierGrandTotal", modifierReportModel.getGrandTotalAsString());
		map.put("itemReport", itemReport);
		map.put("modifierReport", modifierReport);

		JasperReport masterReport = (JasperReport) JRLoader.loadObject(SalesReport.class.getResource("/com/floreantpos/ui/report/sales_report.jasper"));

		JasperPrint print = JasperFillManager.fillReport(masterReport, map, new JREmptyDataSource());
		viewer = new JRViewer(print);
	}

	@Override
	public boolean isDateRangeSupported() {
		return true;
	}

	@Override
	public boolean isTypeSupported() {
		return true;
	}

	public void createModels() {
		Date date1 = DateUtils.startOfDay(getStartDate());
		Date date2 = DateUtils.endOfDay(getEndDate());

		List<Ticket> tickets = TicketDAO.getInstance().findTickets(date1, date2, getReportType() == Report.REPORT_TYPE_1 ? true : false);

		HashMap<String, ReportItem> itemMap = new HashMap<String, ReportItem>();
		HashMap<String, ReportItem> modifierMap = new HashMap<String, ReportItem>();

		for (Iterator iter = tickets.iterator(); iter.hasNext();) {
			Ticket ticket = (Ticket) iter.next();
			ticket = TicketDAO.getInstance().initializeTicket(ticket);

			List<TicketItem> ticketItems = ticket.getTicketItems();
			if (ticketItems == null)
				continue;

			String key = null;
			for (TicketItem ticketItem : ticketItems) {
				if (ticketItem.getItemId() == null) {
					key = ticketItem.getName();
				}
				else {
					key = ticketItem.getItemId().toString();
				}
				ReportItem reportItem = itemMap.get(key);

				if (reportItem == null) {
					reportItem = new ReportItem();
					reportItem.setId(key);
					reportItem.setPrice(ticketItem.getUnitPrice());
					reportItem.setName(ticketItem.getName());
					reportItem.setTaxRate(ticketItem.getTaxRate());

					itemMap.put(key, reportItem);
				}
				reportItem.setQuantity(ticketItem.getItemCount() + reportItem.getQuantity());
				reportItem.setTotal(reportItem.getTotal() + ticketItem.getSubtotalAmountWithoutModifiers());

				if (ticketItem.isHasModifiers() && ticketItem.getTicketItemModifierGroups() != null) {
					List<TicketItemModifierGroup> ticketItemModifierGroups = ticketItem.getTicketItemModifierGroups();

					for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
						List<TicketItemModifier> modifiers = ticketItemModifierGroup.getTicketItemModifiers();
						for (TicketItemModifier modifier : modifiers) {
							if (modifier.getItemId() == null) {
								key = modifier.getName();
							}
							else {
								key = modifier.getItemId().toString();
							}
							ReportItem modifierReportItem = modifierMap.get(key);
							if (modifierReportItem == null) {
								modifierReportItem = new ReportItem();
								modifierReportItem.setId(key);
								modifierReportItem.setPrice(modifier.getUnitPrice());
								modifierReportItem.setName(modifier.getName());
								modifierReportItem.setTaxRate(modifier.getTaxRate());

								modifierMap.put(key, modifierReportItem);
							}
							modifierReportItem.setQuantity(modifierReportItem.getQuantity() + 1);
							//modifierReportItem.setTotal(modifierReportItem.getTotal() + modifier.getTotal());
						}
					}
				}
			}
			ticket = null;
			iter.remove();
		}
		itemReportModel = new SalesReportModel();
		itemReportModel.setItems(new ArrayList<ReportItem>(itemMap.values()));
		itemReportModel.calculateGrandTotal();

		modifierReportModel = new SalesReportModel();
		modifierReportModel.setItems(new ArrayList<ReportItem>(modifierMap.values()));
		modifierReportModel.calculateGrandTotal();
	}
}
