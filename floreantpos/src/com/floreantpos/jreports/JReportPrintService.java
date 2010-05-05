package com.floreantpos.jreports;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.print.JRPrinterAWT;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.dao.RestaurantDAO;

public class JReportPrintService {
	private static Log logger = LogFactory.getLog(JReportPrintService.class);

	public static void printTicket(Ticket ticket) {
		Restaurant restaurant = RestaurantDAO.getInstance().get(Integer.valueOf(1));

		HashMap map = new HashMap();
		map.put("headerLine1", restaurant.getName());
		map.put("headerLine2", restaurant.getAddressLine1());
		map.put("headerLine3", restaurant.getAddressLine2());
		map.put("headerLine4", restaurant.getAddressLine3());
		map.put("headerLine5", com.floreantpos.POSConstants.TEL + ": " + restaurant.getTelephone());

		map.put("checkNo", com.floreantpos.POSConstants.CHK_NO + ticket.getId());
		map.put("tableNo", com.floreantpos.POSConstants.TABLE_NO + ticket.getTableNumber());
		map.put("guestCount", com.floreantpos.POSConstants.GUESTS_ + ticket.getNumberOfGuests());
		map.put("serverName", com.floreantpos.POSConstants.SERVER + ": " + ticket.getOwner());
		map.put("reportDate", com.floreantpos.POSConstants.DATE + ": " + Application.formatDate(new Date()));
		map.put("grandSubtotal", Application.formatNumber(ticket.getSubtotalAmount()));
		map.put("grandTotal", Application.formatNumber(ticket.getTotalAmount()));
		map.put("taxAmount", Application.formatNumber(ticket.getTaxAmount()));
		if (ticket.getGratuity() != null) {
			map.put("tipAmount", Application.formatNumber(ticket.getGratuity().getAmount()));
		}

		InputStream ticketReportStream = null;

		try {
			//ticketReportStream = JReportPrintService.class.getResourceAsStream("/com/floreantpos/jreports/TicketReceiptReport.jrxml");
			ticketReportStream = JReportPrintService.class.getResourceAsStream("/com/floreantpos/jreports/TicketReceiptReport.jasper");

			//JasperReport ticketReport = JasperCompileManager.compileReport(ticketReportStream);
			JasperReport ticketReport = (JasperReport) JRLoader.loadObject(ticketReportStream);

			JasperPrint jasperPrint = JasperFillManager.fillReport(ticketReport, map, new JRTableModelDataSource(new TicketDataSource(ticket)));
			JasperViewer.viewReport(jasperPrint, false);
			JasperPrintManager.printReport(jasperPrint, false);

		} catch (JRException e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		} finally {
			try {
				ticketReportStream.close();
			} catch (Exception x) {
			}
		}
	}

	public static void printTicketToKitchen(Ticket ticket) {
		Restaurant restaurant = RestaurantDAO.getInstance().get(Integer.valueOf(1));

		HashMap map = new HashMap();
		map.put("headerLine1", restaurant.getName());

		map.put("checkNo", com.floreantpos.POSConstants.CHK_NO + ticket.getId());
		map.put("tableNo", com.floreantpos.POSConstants.TABLE_NO + ticket.getTableNumber());
		map.put("guestCount", com.floreantpos.POSConstants.GUESTS_ + ticket.getNumberOfGuests());
		map.put("serverName", com.floreantpos.POSConstants.SERVER + ": " + ticket.getOwner());
		map.put("reportDate", com.floreantpos.POSConstants.DATE + ": " + Application.formatDate(new Date()));

		InputStream ticketReportStream = null;

		try {
			ticketReportStream = JReportPrintService.class.getResourceAsStream("/com/floreantpos/jreports/KitchenReceipt.jasper");
			JasperReport ticketReport = (JasperReport) JRLoader.loadObject(ticketReportStream);

			JasperPrint jasperPrint = JasperFillManager.fillReport(ticketReport, map, new JRTableModelDataSource(new KitchenTicketDataSource(ticket)));
			//JasperViewer.viewReport(jasperPrint, false);

			JRPrinterAWT.printToKitchen = true;
			JasperPrintManager.printReport(jasperPrint, false);

			//no exception, so print to kitchen successful.
			//now mark items as printed.
			markItemsAsPrinted(ticket);

		} catch (JRException e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		} finally {
			try {
				ticketReportStream.close();
			} catch (Exception x) {
			}
		}
	}

	private static void markItemsAsPrinted(Ticket ticket) {
		List<TicketItem> ticketItems = ticket.getTicketItems();
		if (ticketItems != null) {
			for (TicketItem ticketItem : ticketItems) {
				if (!ticketItem.isPrintedToKitchen()) {
					ticketItem.setPrintedToKitchen(true);
				}

				List<TicketItemModifierGroup> modifierGroups = ticketItem.getTicketItemModifierGroups();
				if (modifierGroups != null) {
					for (TicketItemModifierGroup modifierGroup : modifierGroups) {
						List<TicketItemModifier> modifiers = modifierGroup.getTicketItemModifiers();
						if (modifiers != null) {
							for (TicketItemModifier modifier : modifiers) {
								if (!modifier.isPrintedToKitchen()) {
									modifier.setPrintedToKitchen(true);
								}
							}
						}
					}
				}

			}
		}
	}
}
