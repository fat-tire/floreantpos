package com.floreantpos.report;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.print.JRPrinterAWT;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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

	public static JasperPrint createPrintableReceipt(Ticket ticket, final double paidAmount) throws Exception {
		HashMap map = populateTicketProperties(ticket, paidAmount);
		final String FILE_RECEIPT_REPORT = "/com/floreantpos/report/TicketReceiptReport.jasper";

		return createJasperPrint(FILE_RECEIPT_REPORT, map, new JRTableModelDataSource(new TicketDataSource(ticket)));
	}

	public static void printTicket(Ticket ticket, final double paidAmount) {
		try {

			JasperPrint jasperPrint = createPrintableReceipt(ticket, paidAmount);
			JasperPrintManager.printReport(jasperPrint, false);

		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	public static HashMap populateTicketProperties(Ticket ticket, final double paidAmount) {
		Restaurant restaurant = RestaurantDAO.getWorkingRestaurant();

		double totalAmount = ticket.getTotalAmount();
		double tipAmount = 0;

		HashMap map = new HashMap();
		map.put("currencySymbol", Application.getCurrencySymbol());
//		map.put("headerLine1", restaurant.getName());
//		map.put("headerLine2", restaurant.getAddressLine1());
//		map.put("headerLine3", restaurant.getAddressLine2());
//		map.put("headerLine4", restaurant.getAddressLine3());
		map.put("footerMessage", restaurant.getTicketFooterMessage());
		map.put("showHeaderSeparator", Boolean.TRUE);
		map.put("terminal", "Terminal#: 9999");

		if (StringUtils.isNotEmpty(restaurant.getTelephone())) {
//			map.put("headerLine5", com.floreantpos.POSConstants.TEL + ": " + restaurant.getTelephone());
		}

		map.put("checkNo", com.floreantpos.POSConstants.CHK_NO + ticket.getId());
		map.put("tableNo", com.floreantpos.POSConstants.TABLE_NO + ticket.getTableNumber());
		map.put("guestCount", com.floreantpos.POSConstants.GUESTS_ + ticket.getNumberOfGuests());
		map.put("serverName", com.floreantpos.POSConstants.SERVER + ": " + ticket.getOwner());
		map.put("reportDate", com.floreantpos.POSConstants.DATE + ": " + Application.formatDate(new Date()));
		map.put("grandSubtotal", NumberUtil.formatNumber(ticket.getSubtotalAmount()));

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
		double changedAmount = paidAmount - netAmount;

		if (changedAmount < 0) {
			changedAmount = 0;
		}

		map.put("netAmount", NumberUtil.formatNumber(netAmount));
		map.put("paidAmount", NumberUtil.formatNumber(paidAmount));
		map.put("changedAmount", NumberUtil.formatNumber(changedAmount));
		return map;
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
			ticketReportStream = JReportPrintService.class.getResourceAsStream("/com/floreantpos/report/KitchenReceipt.jasper");
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

//	public static void main(String[] args) {
//		Ticket ticket = new Ticket(100);
//		ticket.setTableNumber(1);
//		ticket.setNumberOfGuests(2);
//		ticket.setSubtotalAmount(100.0);
//		ticket.setTotalAmount(112.0);
//		ticket.setTaxAmount(12.0);
//
//		TicketItem item = new TicketItem(0);
//		item.setBeverage(true);
//		item.setCategoryName("bev");
//		item.setItemCount(2);
//
//		ticket.getTicketItems().add(item);
//
//		User user = new User();
//		user.setFirstName("Test");
//		user.setLastName("user");
//
//		ticket.setOwner(user);
//
//		Restaurant restaurant = new Restaurant();
//		restaurant.setName("test resturant");
//		restaurant.setAddressLine1("a");
//		restaurant.setAddressLine2("b");
//		restaurant.setAddressLine3("c");
//		restaurant.setTelephone("000");
//
//		HashMap map = new HashMap();
//		map.put("headerLine1", restaurant.getName());
//		map.put("headerLine2", restaurant.getAddressLine1());
//		map.put("headerLine3", restaurant.getAddressLine2());
//		map.put("headerLine4", restaurant.getAddressLine3());
//		map.put("headerLine5", com.floreantpos.POSConstants.TEL + ": " + restaurant.getTelephone());
//
//		map.put("checkNo", com.floreantpos.POSConstants.CHK_NO + ticket.getId());
//		map.put("tableNo", com.floreantpos.POSConstants.TABLE_NO + ticket.getTableNumber());
//		map.put("guestCount", com.floreantpos.POSConstants.GUESTS_ + ticket.getNumberOfGuests());
//		map.put("serverName", com.floreantpos.POSConstants.SERVER + ": " + ticket.getOwner());
//		map.put("reportDate", com.floreantpos.POSConstants.DATE + ": " + Application.formatDate(new Date()));
//		map.put("grandSubtotal", NumberUtil.formatNumber(ticket.getSubtotalAmount()));
//		map.put("grandTotal", NumberUtil.formatNumber(ticket.getTotalAmount()));
//		map.put("taxAmount", NumberUtil.formatNumber(ticket.getTaxAmount()));
//		map.put("tipAmount", NumberUtil.formatNumber(10.0));
//		map.put("totalWithTip", "999");
//
//		InputStream ticketReportStream = null;
//
//		try {
//			ticketReportStream = JReportPrintService.class.getResourceAsStream(FILE_RECEIPT_REPORT);
//			JasperReport ticketReport = (JasperReport) JRLoader.loadObject(ticketReportStream);
//
//			JasperPrint jasperPrint = JasperFillManager.fillReport(ticketReport, map, new JRTableModelDataSource(new TicketDataSource(ticket)));
//			//JasperPrintManager.printReport(jasperPrint, false);
//
//			JasperViewer.viewReport(jasperPrint);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
//		} finally {
//			try {
//				ticketReportStream.close();
//			} catch (Exception x) {
//			}
//		}
//	}
}
