package com.floreantpos.report;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemCookingInstruction;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.util.NumberUtil;

public class JReportPrintService {
	private static Log logger = LogFactory.getLog(JReportPrintService.class);

	public static JasperPrint createJasperPrint(String reportFile, Map<String, String> properties, JRDataSource dataSource) throws Exception {
		InputStream ticketReportStream = null;

		try {

			ticketReportStream = JReportPrintService.class.getResourceAsStream(reportFile);
			JasperReport ticketReport = (JasperReport) JRLoader.loadObject(ticketReportStream);

			JasperPrint jasperPrint = JasperFillManager.fillReport(ticketReport, properties, dataSource);

			return jasperPrint;

		} finally {
			IOUtils.closeQuietly(ticketReportStream);
		}
	}

	public static JasperPrint createPrint(Ticket ticket, TicketPrintProperties printProperties) throws Exception {
		HashMap map = populateTicketProperties(ticket, printProperties);

		final String FILE_RECEIPT_REPORT = "/com/floreantpos/report/TicketReceiptReport.jasper";

		return createJasperPrint(FILE_RECEIPT_REPORT, map, new JRTableModelDataSource(new TicketDataSource(ticket)));
	}

	public static void printTicket(Ticket ticket) {
		try {

			TicketPrintProperties printProperties = new TicketPrintProperties("*** PACKAGER RECEIPT ***", true, true, true);
			JasperPrint jasperPrint = createPrint(ticket, printProperties);
			JasperPrintManager.printReport(jasperPrint, false);

		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	public static HashMap populateTicketProperties(Ticket ticket, TicketPrintProperties printProperties) {
		Restaurant restaurant = RestaurantDAO.getWorkingRestaurant();

		double totalAmount = ticket.getTotalAmount();
		double tipAmount = 0;

		HashMap map = new HashMap();
		String currencySymbol = Application.getCurrencySymbol();
		map.put("currencySymbol", currencySymbol);
		map.put("receiptType", printProperties.getReceiptTypeName());
		map.put("showSubtotal", Boolean.valueOf(printProperties.isShowSubtotal()));
		map.put("showHeaderSeparator", Boolean.valueOf(printProperties.isShowHeader()));
		map.put("showFooter", Boolean.valueOf(printProperties.isShowFooter()));
		
		map.put("terminal", "Terminal#: " + Application.getInstance().getTerminal().getId());
		map.put("checkNo", com.floreantpos.POSConstants.CHK_NO + ": " + ticket.getId());
		map.put("tableNo", com.floreantpos.POSConstants.TABLE_NO + ": " + ticket.getTableNumber());
		map.put("guestCount", com.floreantpos.POSConstants.GUESTS_ + ": " + ticket.getNumberOfGuests());
		map.put("serverName", com.floreantpos.POSConstants.SERVER + ": " + ticket.getOwner());
		map.put("reportDate", com.floreantpos.POSConstants.DATE + ": " + Application.formatDate(new Date()));

		if (printProperties.isShowHeader()) {
			map.put("headerLine1", restaurant.getName());
			map.put("headerLine2", restaurant.getAddressLine1());
			map.put("headerLine3", restaurant.getAddressLine2());
			map.put("headerLine4", restaurant.getAddressLine3());
			map.put("headerLine5", com.floreantpos.POSConstants.TEL + ": " + restaurant.getTelephone());
		}

		if (printProperties.isShowFooter()) {
			if (ticket.getDiscountAmount() > 0.0) {
				map.put("discountAmount", NumberUtil.formatNumber(ticket.getDiscountAmount()));
			}

			if (ticket.getTaxAmount() > 0.0) {
				map.put("taxAmount", NumberUtil.formatNumber(ticket.getTaxAmount()));
			}

			if (ticket.getServiceCharge() > 0.0) {
				map.put("serviceCharge", NumberUtil.formatNumber(ticket.getServiceCharge()));
			}

			if (ticket.getGratuity() != null) {
				tipAmount = ticket.getGratuity().getAmount();
				map.put("tipAmount", NumberUtil.formatNumber(tipAmount));
			}

			double netAmount = totalAmount + tipAmount;
			double changedAmount = ticket.getTenderedAmount() - netAmount;

			if (changedAmount < 0) {
				changedAmount = 0;
			}

			map.put("totalText", "Total " + currencySymbol);
			map.put("discountText", "Discount " + currencySymbol);
			map.put("taxText", "Tax " + currencySymbol);
			map.put("serviceChargeText", "Service Charge " + currencySymbol);
			map.put("tipsText", "Tips " + currencySymbol);
			map.put("netAmountText", "Net Amount " + currencySymbol);
			map.put("paidAmountText", "Paid Amount " + currencySymbol);
			map.put("changeAmountText", "Change Amount " + currencySymbol);

			map.put("netAmount", NumberUtil.formatNumber(netAmount));
			map.put("paidAmount", NumberUtil.formatNumber(ticket.getTenderedAmount()));
			map.put("changedAmount", NumberUtil.formatNumber(changedAmount));
			map.put("grandSubtotal", NumberUtil.formatNumber(ticket.getSubtotalAmount()));
			map.put("footerMessage", restaurant.getTicketFooterMessage());
		}

		return map;
	}

	public static void printTicketToKitchen(Ticket ticket) {
		try {
			
			TicketPrintProperties printProperties = new TicketPrintProperties("*** KITCHEN ***", false, false, false);
			JasperPrint jasperPrint = createPrint(ticket, printProperties);
			JasperPrintManager.printReport(jasperPrint, false);

			//no exception, so print to kitchen successful.
			//now mark items as printed.
			markItemsAsPrinted(ticket);
			
		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
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
						modifierGroup.setPrintedToKitchen(true);
					}
				}

				List<TicketItemCookingInstruction> cookingInstructions = ticketItem.getCookingInstructions();
				if (cookingInstructions != null) {
					for (TicketItemCookingInstruction ticketItemCookingInstruction : cookingInstructions) {
						ticketItemCookingInstruction.setPrintedToKitchen(true);
					}
				}
			}
		}
	}
}
