package com.mdss.pos.jreports;

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
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mdss.pos.main.Application;
import com.mdss.pos.model.Restaurant;
import com.mdss.pos.model.Ticket;
import com.mdss.pos.model.TicketItem;
import com.mdss.pos.model.TicketItemModifier;
import com.mdss.pos.model.TicketItemModifierGroup;
import com.mdss.pos.model.dao.RestaurantDAO;

public class JReportPrintService {
	private static Log logger = LogFactory.getLog(JReportPrintService.class);

	public static void printTicket(Ticket ticket) {
		Restaurant restaurant = RestaurantDAO.getInstance().get(
				Integer.valueOf(1));

		HashMap map = new HashMap();
		map.put("headerLine1", restaurant.getName());
		map.put("headerLine2", restaurant.getAddressLine1());
		map.put("headerLine3", restaurant.getAddressLine2());
		map.put("headerLine4", restaurant.getAddressLine3());
		map.put("headerLine5", restaurant.getTelephone());

		map.put("checkNo", "Chk#: " + ticket.getId());
		map.put("tableNo", "Table#: " + ticket.getTableNumber());
		map.put("guestCount", "Guests: " + ticket.getNumberOfGuests());
		map.put("serverName", "Server: " + ticket.getOwner());
		map.put("reportDate", "Date: " + Application.formatDate(new Date()));
		map.put("grandSubtotal", Application.formatNumber(ticket
				.getSubtotalAmount()));
		map
				.put("grandTotal", Application.formatNumber(ticket
						.getTotalAmount()));
		map.put("taxAmount", Application.formatNumber(ticket.getTaxAmount()));
		if (ticket.getGratuity() != null) {
			map.put("tipAmount", Application.formatNumber(ticket.getGratuity()
					.getAmount()));
		}

		InputStream ticketReportStream = null;

		try {
			ticketReportStream = JReportPrintService.class
					.getResourceAsStream("/com/mdss/pos/jreports/TicketReceiptReport.jasper");
			JasperReport ticketReport = (JasperReport) JRLoader
					.loadObject(ticketReportStream);

			JasperPrint jasperPrint = JasperFillManager.fillReport(
					ticketReport, map, new JRTableModelDataSource(
							new TicketDataSource(ticket)));
			//JasperViewer.viewReport(jasperPrint, false);
			JasperPrintManager.printReport(jasperPrint, false);

		} catch (JRException e) {
			logger.error("Error while printing to normal printer", e);
		} finally {
			try {
				ticketReportStream.close();
			} catch (Exception x) {
			}
		}
	}

	public static void printTicketToKitchen(Ticket ticket) {
		Restaurant restaurant = RestaurantDAO.getInstance().get(
				Integer.valueOf(1));

		HashMap map = new HashMap();
		map.put("headerLine1", restaurant.getName());

		map.put("checkNo", "Chk#: " + ticket.getId());
		map.put("tableNo", "Table#: " + ticket.getTableNumber());
		map.put("guestCount", "Guests: " + ticket.getNumberOfGuests());
		map.put("serverName", "Server: " + ticket.getOwner());
		map.put("reportDate", "Date: " + Application.formatDate(new Date()));

		InputStream ticketReportStream = null;

		try {
			ticketReportStream = JReportPrintService.class
					.getResourceAsStream("/com/mdss/pos/jreports/KitchenReceipt.jasper");
			JasperReport ticketReport = (JasperReport) JRLoader
					.loadObject(ticketReportStream);

			JasperPrint jasperPrint = JasperFillManager.fillReport(
					ticketReport, map, new JRTableModelDataSource(
							new KitchenTicketDataSource(ticket)));
			//JasperViewer.viewReport(jasperPrint, false);
			JasperPrintManager.printReport(jasperPrint, false);

			//no exception, so print to kitchen successful.
			//now mark items as printed.
			markItemsAsPrinted(ticket);

		} catch (JRException e) {
			logger.error("Error while printing to normal printer", e);
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

				List<TicketItemModifierGroup> modifierGroups = ticketItem
						.getTicketItemModifierGroups();
				if (modifierGroups != null) {
					for (TicketItemModifierGroup modifierGroup : modifierGroups) {
						List<TicketItemModifier> modifiers = modifierGroup
								.getTicketItemModifiers();
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
