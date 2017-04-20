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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JRViewer;

import org.jdesktop.swingx.calendar.DateUtils;

import com.floreantpos.Messages;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.service.ReportService;
import com.floreantpos.util.CurrencyUtil;

public class SalesReport extends Report {
	private SalesReportModel itemReportModel;
	private SalesReportModel modifierReportModel;

	public SalesReport() {
		super();
	}

	@Override
	public void refresh() throws Exception {
		createModels();

		JasperReport itemReport = ReportUtil.getReport("sales_sub_report"); //$NON-NLS-1$
		JasperReport modifierReport = ReportUtil.getReport("sales_sub_report"); //$NON-NLS-1$

		HashMap map = new HashMap();
		ReportUtil.populateRestaurantProperties(map);
		map.put("reportTitle", Messages.getString("SalesReport.3")); //$NON-NLS-1$ //$NON-NLS-2$
		map.put("reportTime", ReportService.formatFullDate(new Date())); //$NON-NLS-1$
		map.put("dateRange", ReportService.formatShortDate(getStartDate()) + " to " + ReportService.formatShortDate(getEndDate())); //$NON-NLS-1$ //$NON-NLS-2$
		map.put("terminalName", getTerminal() == null ? com.floreantpos.POSConstants.ALL : getTerminal().getName()); //$NON-NLS-1$
		map.put("itemDataSource", new JRTableModelDataSource(itemReportModel)); //$NON-NLS-1$
		map.put("modifierDataSource", new JRTableModelDataSource(modifierReportModel)); //$NON-NLS-1$
		map.put("currency", Messages.getString("SalesReport.8") + CurrencyUtil.getCurrencyName() + " (" + CurrencyUtil.getCurrencySymbol() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
		map.put("itemTotalQuantity", itemReportModel.getTotalQuantity()); //$NON-NLS-1$
		map.put("itemTotal", itemReportModel.getTotalAsString()); //$NON-NLS-1$
		map.put("itemGrossTotal", itemReportModel.getGrossTotalAsDouble()); //$NON-NLS-1$
		map.put("itemDiscountTotal", itemReportModel.getDiscountTotalAsString()); //$NON-NLS-1$
		map.put("itemTaxTotal", itemReportModel.getTaxTotalAsString()); //$NON-NLS-1$
		map.put("itemGrandTotal", itemReportModel.getGrandTotalAsString()); //$NON-NLS-1$
		map.put("modifierTotalQuantity", modifierReportModel.getTotalQuantity()); //$NON-NLS-1$
		map.put("modifierGrossTotal", modifierReportModel.getGrossTotalAsDouble()); //$NON-NLS-1$
		map.put("modifierTaxTotal", modifierReportModel.getTaxTotalAsString()); //$NON-NLS-1$
		map.put("modifierGrandTotal", modifierReportModel.getGrandTotalAsString()); //$NON-NLS-1$
		map.put("modifierTotal", modifierReportModel.getTotalAsString()); //$NON-NLS-1$
		map.put("itemReport", itemReport); //$NON-NLS-1$
		map.put("modifierReport", modifierReport); //$NON-NLS-1$

		JasperReport masterReport = ReportUtil.getReport("sales_report"); //$NON-NLS-1$

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

		List<Ticket> tickets = TicketDAO.getInstance().findTickets(date1, date2, getReportType() == Report.REPORT_TYPE_1 ? true : false, getTerminal());

		HashMap<String, ReportItem> itemMap = new HashMap<String, ReportItem>();
		HashMap<String, ReportItem> modifierMap = new HashMap<String, ReportItem>();

		for (Iterator iter = tickets.iterator(); iter.hasNext();) {
			Ticket t = (Ticket) iter.next();

			Ticket ticket = TicketDAO.getInstance().loadFullTicket(t.getId());

			List<TicketItem> ticketItems = ticket.getTicketItems();
			if (ticketItems == null)
				continue;

			String key = null;
			for (TicketItem ticketItem : ticketItems) {
				if (ticketItem.getUnitPrice() == 0 && !isIncludedFreeItems()) {
					continue;
				}
				if (ticketItem.getItemId() == null) {
					key = ticketItem.getName();
				}
				else {
					key = ticketItem.getItemId().toString();
				}
				key += "-" + ticketItem.getName() + ticketItem.getUnitPrice() + ticketItem.getTaxRate(); //$NON-NLS-1$

				ReportItem reportItem = itemMap.get(key);

				if (reportItem == null) {
					reportItem = new ReportItem();
					reportItem.setId(key);
					reportItem.setUniqueId(ticketItem.getItemId().toString());
					reportItem.setPrice(ticketItem.getUnitPrice());
					reportItem.setName(ticketItem.getName());
					reportItem.setTaxRate(ticketItem.getTaxRate());

					itemMap.put(key, reportItem);
				}

				//set the quantity
				if (ticketItem.isFractionalUnit()) {
					reportItem.setQuantity(ticketItem.getItemQuantity() + reportItem.getQuantity());
				}
				else {
					reportItem.setQuantity(ticketItem.getItemCount() + reportItem.getQuantity());
				}

				//reportItem.setQuantity(ticketItem.getItemCount() + reportItem.getQuantity());

				reportItem.setGrossTotal(reportItem.getGrossTotal() + ticketItem.getTotalAmountWithoutModifiers());
				reportItem.setDiscount(reportItem.getDiscount() + ticketItem.getDiscountAmount());
				reportItem.setTaxTotal(reportItem.getTaxTotal() + ticketItem.getTaxAmountWithoutModifiers());
				reportItem.setTotal(reportItem.getTotal() + ticketItem.getSubtotalAmountWithoutModifiers());

				List<TicketItemModifier> modifiers = ticketItem.getTicketItemModifiers();
				if (modifiers != null) {
					for (TicketItemModifier modifier : modifiers) {
						if (modifier.getUnitPrice() == 0 && !isIncludedFreeItems()) {
							continue;
						}

						if (modifier.getModifierId() == null) {
							key = modifier.getName();
						}
						else {
							key = modifier.getModifierId().toString();
						}
						key += "-" + modifier.getName() + modifier.getModifierType() + "-" + modifier.getUnitPrice() + modifier.getTaxRate(); //$NON-NLS-1$ //$NON-NLS-2$

						ReportItem modifierReportItem = modifierMap.get(key);
						if (modifierReportItem == null) {
							modifierReportItem = new ReportItem();
							modifierReportItem.setId(key);
							modifierReportItem.setUniqueId(modifier.getModifierId().toString());

							modifierReportItem.setPrice(modifier.getUnitPrice());
							modifierReportItem.setName(modifier.getName());
							modifierReportItem.setTaxRate(modifier.getTaxRate());

							modifierMap.put(key, modifierReportItem);
						}
						modifierReportItem.setQuantity(modifierReportItem.getQuantity() + modifier.getItemCount());
						modifierReportItem.setGrossTotal(modifierReportItem.getGrossTotal() + modifier.getTotalAmount());
						modifierReportItem.setTaxTotal(modifierReportItem.getTaxTotal() + modifier.getTaxAmount());
						modifierReportItem.setTotal(modifierReportItem.getTotal() + modifier.getSubTotalAmount());
					}
				}
			}
			ticket = null;
			iter.remove();
		}
		itemReportModel = new SalesReportModel();

		List<ReportItem> itemList = new ArrayList<ReportItem>(itemMap.values());
		Collections.sort(itemList, new Comparator<ReportItem>() {

			public int compare(ReportItem o1, ReportItem o2) {
				return Integer.parseInt(o1.getUniqueId()) - Integer.parseInt(o2.getUniqueId());
			}
		});

		itemReportModel.setItems(itemList);
		itemReportModel.calculateTotalQuantity();
		itemReportModel.calculateDiscountTotal();
		itemReportModel.calculateGrossTotal();
		itemReportModel.calculateTaxTotal();
		itemReportModel.calculateGrandTotal();
		itemReportModel.calculateTotal();

		modifierReportModel = new SalesReportModel();

		List<ReportItem> modifierList = new ArrayList<ReportItem>(modifierMap.values());
		Collections.sort(modifierList, new Comparator<ReportItem>() {

			public int compare(ReportItem o1, ReportItem o2) {
				return Integer.parseInt(o1.getUniqueId()) - Integer.parseInt(o2.getUniqueId());
			}
		});
		modifierReportModel.setItems(modifierList);
		modifierReportModel.calculateTotalQuantity();
		modifierReportModel.calculateGrossTotal();
		modifierReportModel.calculateTaxTotal();
		modifierReportModel.calculateGrandTotal();
		modifierReportModel.calculateTotal();
	}
}
