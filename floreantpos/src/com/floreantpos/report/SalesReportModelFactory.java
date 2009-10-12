package com.floreantpos.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.jdesktop.swingx.calendar.DateUtils;

import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.dao.TicketDAO;

public class SalesReportModelFactory {
	private Date startDate;
	private Date endDate;
	private boolean settled = true;
	
	private SalesReportModel itemReportModel;
	private SalesReportModel modifierReportModel;
	
	public SalesReportModelFactory() {
		super();
	}
	
	public void createModels() {
		Date currentDate = new Date();
		if(startDate == null) {
			startDate = DateUtils.startOfDay(currentDate);
		}
		if(endDate == null) {
			endDate = DateUtils.endOfDay(currentDate);
		}
		
		List<Ticket> tickets = TicketDAO.getInstance().findTickets(startDate, endDate, settled);
		
		HashMap<String, ReportItem> itemMap = new HashMap<String, ReportItem>();
		HashMap<String, ReportItem> modifierMap = new HashMap<String, ReportItem>();
		
		for (Iterator iter = tickets.iterator(); iter.hasNext();) {
			Ticket ticket = (Ticket) iter.next();
			ticket = TicketDAO.getInstance().initializeTicket(ticket);
			
			List<TicketItem> ticketItems = ticket.getTicketItems();
			if(ticketItems == null) continue;
			
			String key = null;
			for (TicketItem ticketItem : ticketItems) {
				if(ticketItem.getItemId() == null) {
					key = ticketItem.getName();
				}
				else {
					key = ticketItem.getItemId().toString();
				}
				ReportItem reportItem = itemMap.get(key);
				
				if(reportItem == null) {
					reportItem = new ReportItem();
					reportItem.setId(key);
					reportItem.setPrice(ticketItem.getUnitPrice());
					reportItem.setName(ticketItem.getName());
					reportItem.setTaxRate(ticketItem.getTaxRate());

					itemMap.put(key, reportItem);
				}
				reportItem.setQuantity(ticketItem.getItemCount() + reportItem.getQuantity());
				reportItem.setTotal(reportItem.getTotal() + ticketItem.getSubtotalAmountWithoutModifiers());
				
//				if(ticketItem.isHasModifiers() && ticketItem.getModifiers() != null && ticketItem.getModifiers().size() > 0) {
//					List<TicketItemModifier> modifiers = ticketItem.getModifiers();
//					for (TicketItemModifier modifier : modifiers) {
//						if(modifier.getItemId() == null) {
//							key = modifier.getName();
//						}
//						else {
//							key = modifier.getItemId().toString();
//						}
//						ReportItem modifierReportItem = modifierMap.get(key);
//						if(modifierReportItem == null) {
//							modifierReportItem = new ReportItem();
//							modifierReportItem.setId(key);
//							modifierReportItem.setPrice(modifier.getPrice());
//							modifierReportItem.setName(modifier.getName());
//							modifierReportItem.setTaxRate(modifier.getTaxRate());
//
//							modifierMap.put(key, modifierReportItem);
//						}
//						modifierReportItem.setQuantity(modifierReportItem.getQuantity() + 1);
//						modifierReportItem.setTotal(modifierReportItem.getTotal() + modifier.getTotal());
//					}
//				}
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
		
	
	
	public static void main(String[] args) throws Exception {
		SalesReportModelFactory factory = new SalesReportModelFactory();
		factory.createModels();
		
		SalesReportModel itemReportModel = factory.getItemReportModel();
		SalesReportModel modifierReportModel = factory.getModifierReportModel();
		
		JasperReport itemReport = (JasperReport) JRLoader.loadObject(SalesReportModelFactory.class.getResource("/com/floreantpos/ui/report/SalesSubReport.jasper"));
		JasperReport modifierReport = (JasperReport) JRLoader.loadObject(SalesReportModelFactory.class.getResource("/com/floreantpos/ui/report/SalesSubReport.jasper"));
		
		HashMap map = new HashMap();
		map.put("itemDataSource", new  JRTableModelDataSource(itemReportModel));
		map.put("modifierDataSource", new  JRTableModelDataSource(modifierReportModel));
		map.put("currencySymbol", Application.getCurrencySymbol());
		map.put("itemGrandTotal", itemReportModel.getGrandTotalAsString());
		map.put("modifierGrandTotal", modifierReportModel.getGrandTotalAsString());
		map.put("itemReport", itemReport);
		map.put("modifierReport", modifierReport);
		
		JasperReport masterReport = (JasperReport) JRLoader.loadObject(SalesReportModelFactory.class.getResource("/com/floreantpos/ui/report/SalesReport.jasper"));
		
		JasperPrint print = JasperFillManager.fillReport(masterReport, map, new JREmptyDataSource());
		JasperViewer.viewReport(print, false);
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isSettled() {
		return settled;
	}

	public void setSettled(boolean settled) {
		this.settled = settled;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public SalesReportModel getItemReportModel() {
		return itemReportModel;
	}

	public SalesReportModel getModifierReportModel() {
		return modifierReportModel;
	}
}
