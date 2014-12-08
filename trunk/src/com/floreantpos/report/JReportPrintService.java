package com.floreantpos.report;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import us.fatehi.magnetictrack.bankcard.BankCardMagneticTrack;

import com.floreantpos.POSConstants;
import com.floreantpos.demo.KitchenDisplay;
import com.floreantpos.main.Application;
import com.floreantpos.model.KitchenTicket;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.RefundTransaction;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.util.NumberUtil;

public class JReportPrintService {
	private static final String TIP_AMOUNT = "tipAmount";
	private static final String SERVICE_CHARGE = "serviceCharge";
	private static final String TAX_AMOUNT = "taxAmount";
	private static final String DISCOUNT_AMOUNT = "discountAmount";
	private static final String HEADER_LINE5 = "headerLine5";
	private static final String HEADER_LINE4 = "headerLine4";
	private static final String HEADER_LINE3 = "headerLine3";
	private static final String HEADER_LINE2 = "headerLine2";
	private static final String HEADER_LINE1 = "headerLine1";
	private static final String REPORT_DATE = "reportDate";
	private static final String SERVER_NAME = "serverName";
	private static final String GUEST_COUNT = "guestCount";
	private static final String TABLE_NO = "tableNo";
	private static final String CHECK_NO = "checkNo";
	private static final String TERMINAL = "terminal";
	private static final String SHOW_FOOTER = "showFooter";
	private static final String SHOW_HEADER_SEPARATOR = "showHeaderSeparator";
	private static final String SHOW_SUBTOTAL = "showSubtotal";
	private static final String RECEIPT_TYPE = "receiptType";
	private static final String SUB_TOTAL_TEXT = "subTotalText";
	private static final String QUANTITY_TEXT = "quantityText";
	private static final String ITEM_TEXT = "itemText";
	private static final String CURRENCY_SYMBOL = "currencySymbol";
	private static Log logger = LogFactory.getLog(JReportPrintService.class);

	public static void printGenericReport(String title, String data) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>(2);
		map.put("title", title);
		map.put("data", data);
		JasperPrint jasperPrint = createJasperPrint("/com/floreantpos/report/template/GenericReport.jasper", map, new JREmptyDataSource());
		jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
		printQuitely(jasperPrint);
	}

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

	public static JasperPrint createPrint(Ticket ticket, Map<String, String> map, PosTransaction transaction) throws Exception {

		final String FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/TicketReceiptReport.jasper";

		TicketDataSource dataSource = new TicketDataSource(ticket);
		return createJasperPrint(FILE_RECEIPT_REPORT, map, new JRTableModelDataSource(dataSource));
	}

	public static void printTicket(Ticket ticket) {
		try {

			TicketPrintProperties printProperties = new TicketPrintProperties("*** ORDER " + ticket.getId() + " ***", false, true, true);
			printProperties.setPrintCookingInstructions(false);
			HashMap map = populateTicketProperties(ticket, printProperties, null);

			JasperPrint jasperPrint = createPrint(ticket, map, null);
			jasperPrint.setName("ORDER_" + ticket.getId());
			jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
			printQuitely(jasperPrint);

		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	public static JasperPrint createRefundPrint(Ticket ticket, HashMap map) throws Exception {
		final String FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/RefundReceipt.jasper";

		TicketDataSource dataSource = new TicketDataSource(ticket);
		return createJasperPrint(FILE_RECEIPT_REPORT, map, new JRTableModelDataSource(dataSource));
	}

	public static void printRefundTicket(Ticket ticket, RefundTransaction posTransaction) {
		try {

			TicketPrintProperties printProperties = new TicketPrintProperties("*** REFUND RECEIPT ***", true, true, true);
			printProperties.setPrintCookingInstructions(false);
			HashMap map = populateTicketProperties(ticket, printProperties, posTransaction);
			map.put("refundAmountText", "Total Refund");
			map.put("refundAmount", String.valueOf(posTransaction.getAmount()));
			map.put("cashRefundText", "Cash Refund");
			map.put("cashRefund", String.valueOf(posTransaction.getAmount()));

			JasperPrint jasperPrint = createRefundPrint(ticket, map);
			jasperPrint.setName("REFUND_" + ticket.getId());
			jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
			printQuitely(jasperPrint);

		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	public static void printTransaction(PosTransaction transaction) {
		try {
			Ticket ticket = transaction.getTicket();

			TicketPrintProperties printProperties = new TicketPrintProperties("*** PAYMENT RECEIPT ***", true, true, true);
			printProperties.setPrintCookingInstructions(false);
			HashMap map = populateTicketProperties(ticket, printProperties, transaction);

			if (transaction != null && transaction.isCard()) {
				map.put("cardPayment", true);
				map.put("copyType", "Customer Copy");
				JasperPrint jasperPrint = createPrint(ticket, map, transaction);
				jasperPrint.setName("Ticket-" + ticket.getId() + "-CustomerCopy");
				jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
				printQuitely(jasperPrint);

				map.put("copyType", "Merchant Copy");
				jasperPrint = createPrint(ticket, map, transaction);
				jasperPrint.setName("Ticket-" + ticket.getId() + "-MerchantCopy");
				jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
				printQuitely(jasperPrint);
			}
			else {
				JasperPrint jasperPrint = createPrint(ticket, map, transaction);
				jasperPrint.setName("Ticket-" + ticket.getId());
				jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
				printQuitely(jasperPrint);
			}

		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	public static void printTransaction(PosTransaction transaction, boolean printCustomerCopy) {
		try {
			Ticket ticket = transaction.getTicket();

			TicketPrintProperties printProperties = new TicketPrintProperties("*** PAYMENT RECEIPT ***", true, true, true);
			printProperties.setPrintCookingInstructions(false);
			HashMap map = populateTicketProperties(ticket, printProperties, transaction);

			if (transaction != null && transaction.isCard()) {
				map.put("cardPayment", true);
				map.put("copyType", "Merchant Copy");

				JasperPrint jasperPrint = createPrint(ticket, map, transaction);
				jasperPrint.setName("Ticket-" + ticket.getId() + "-MerchantCopy");
				jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
				printQuitely(jasperPrint);

				if (printCustomerCopy) {
					map.put("copyType", "Customer Copy");
					
					jasperPrint = createPrint(ticket, map, transaction);
					jasperPrint.setName("Ticket-" + ticket.getId() + "-CustomerCopy");
					jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
					printQuitely(jasperPrint);
				}
			}
			else {
				JasperPrint jasperPrint = createPrint(ticket, map, transaction);
				jasperPrint.setName("Ticket-" + ticket.getId());
				jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
				printQuitely(jasperPrint);
			}

		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	private static void beginRow(StringBuilder html) {
		html.append("<div>");
	}

	private static void endRow(StringBuilder html) {
		html.append("</div>");
	}

	private static void addColumn(StringBuilder html, String columnText) {
		html.append("<span>" + columnText + "</span>");
	}

	public static HashMap populateTicketProperties(Ticket ticket, TicketPrintProperties printProperties, PosTransaction transaction) {
		Restaurant restaurant = RestaurantDAO.getRestaurant();

		double totalAmount = ticket.getTotalAmount();
		double tipAmount = 0;

		HashMap map = new HashMap();
		String currencySymbol = Application.getCurrencySymbol();
		map.put(CURRENCY_SYMBOL, currencySymbol);
		map.put(ITEM_TEXT, POSConstants.RECEIPT_REPORT_ITEM_LABEL);
		map.put(QUANTITY_TEXT, POSConstants.RECEIPT_REPORT_QUANTITY_LABEL);
		map.put(SUB_TOTAL_TEXT, POSConstants.RECEIPT_REPORT_SUBTOTAL_LABEL);
		map.put(RECEIPT_TYPE, printProperties.getReceiptTypeName());
		map.put(SHOW_SUBTOTAL, Boolean.valueOf(printProperties.isShowSubtotal()));
		map.put(SHOW_HEADER_SEPARATOR, Boolean.TRUE);
		map.put(SHOW_FOOTER, Boolean.valueOf(printProperties.isShowFooter()));

		map.put(TERMINAL, POSConstants.RECEIPT_REPORT_TERMINAL_LABEL + Application.getInstance().getTerminal().getId());
		map.put(CHECK_NO, POSConstants.RECEIPT_REPORT_TICKET_NO_LABEL + ticket.getId());
		map.put(TABLE_NO, POSConstants.RECEIPT_REPORT_TABLE_NO_LABEL + ticket.getTableNumbers());
		map.put(GUEST_COUNT, POSConstants.RECEIPT_REPORT_GUEST_NO_LABEL + ticket.getNumberOfGuests());
		map.put(SERVER_NAME, POSConstants.RECEIPT_REPORT_SERVER_LABEL + ticket.getOwner());
		map.put(REPORT_DATE, POSConstants.RECEIPT_REPORT_DATE_LABEL + Application.formatDate(new Date()));

		StringBuilder ticketHeaderBuilder = buildTicketHeader(ticket, printProperties);

		map.put("ticketHeader", ticketHeaderBuilder.toString());
		map.put("barcode", String.valueOf(ticket.getId()));

		if (printProperties.isShowHeader()) {
			map.put(HEADER_LINE1, restaurant.getName());
			map.put(HEADER_LINE2, restaurant.getAddressLine1());
			map.put(HEADER_LINE3, restaurant.getAddressLine2());
			map.put(HEADER_LINE4, restaurant.getAddressLine3());
			map.put(HEADER_LINE5, restaurant.getTelephone());
		}

		if (printProperties.isShowFooter()) {
			if (ticket.getDiscountAmount() > 0.0) {
				map.put(DISCOUNT_AMOUNT, NumberUtil.formatNumber(ticket.getDiscountAmount()));
			}

			if (ticket.getTaxAmount() > 0.0) {
				map.put(TAX_AMOUNT, NumberUtil.formatNumber(ticket.getTaxAmount()));
			}

			if (ticket.getServiceCharge() > 0.0) {
				map.put(SERVICE_CHARGE, NumberUtil.formatNumber(ticket.getServiceCharge()));
			}

			if (ticket.getGratuity() != null) {
				tipAmount = ticket.getGratuity().getAmount();
				map.put(TIP_AMOUNT, NumberUtil.formatNumber(tipAmount));
			}

			map.put("totalText", POSConstants.RECEIPT_REPORT_TOTAL_LABEL + currencySymbol);
			map.put("discountText", POSConstants.RECEIPT_REPORT_DISCOUNT_LABEL + currencySymbol);
			map.put("taxText", POSConstants.RECEIPT_REPORT_TAX_LABEL + currencySymbol);
			map.put("serviceChargeText", POSConstants.RECEIPT_REPORT_SERVICE_CHARGE_LABEL + currencySymbol);
			map.put("tipsText", POSConstants.RECEIPT_REPORT_TIPS_LABEL + currencySymbol);
			map.put("netAmountText", POSConstants.RECEIPT_REPORT_NETAMOUNT_LABEL + currencySymbol);
			map.put("paidAmountText", POSConstants.RECEIPT_REPORT_PAIDAMOUNT_LABEL + currencySymbol);
			map.put("dueAmountText", POSConstants.RECEIPT_REPORT_DUEAMOUNT_LABEL + currencySymbol);
			map.put("changeAmountText", POSConstants.RECEIPT_REPORT_CHANGEAMOUNT_LABEL + currencySymbol);

			map.put("netAmount", NumberUtil.formatNumber(totalAmount));
			map.put("paidAmount", NumberUtil.formatNumber(ticket.getPaidAmount()));
			map.put("dueAmount", NumberUtil.formatNumber(ticket.getDueAmount()));
			map.put("grandSubtotal", NumberUtil.formatNumber(ticket.getSubtotalAmount()));
			map.put("footerMessage", restaurant.getTicketFooterMessage());
			map.put("copyType", printProperties.getReceiptCopyType());

			if (transaction != null) {
				double changedAmount = transaction.getTenderAmount() - transaction.getAmount();
				if (changedAmount < 0) {
					changedAmount = 0;
				}
				map.put("changedAmount", NumberUtil.formatNumber(changedAmount));

				if (transaction.isCard()) {
					map.put("cardPayment", true);

					if (StringUtils.isNotEmpty(transaction.getCardTrack())) {
						BankCardMagneticTrack track = BankCardMagneticTrack.from(transaction.getCardTrack());
						String string = transaction.getCardType();
						string += "<br/>" + "APPROVAL: " + transaction.getCardAuthCode();

						try {
							string += "<br/>" + "ACCT: " + getCardNumber(track);
							string += "<br/>" + "EXP: " + track.getTrack1().getExpirationDate();
							string += "<br/>" + "CARDHOLDER: " + track.getTrack1().getName();
						} catch (Exception e) {
							logger.equals(e);
						}

						map.put("approvalCode", string);
					}
					else {
						String string = "APPROVAL: " + transaction.getCardAuthCode();
						string += "<br/>" + "Card processed in ext. device.";

						map.put("approvalCode", string);
					}
				}
			}

			String messageString = "<html>";
//			String customerName = ticket.getProperty(Ticket.CUSTOMER_NAME);
			
//			if (customerName != null) {
//				if (customer.hasProperty("mykalaid")) {
//					messageString += "<br/>Customer: " + customer.getName();
//				}
//			}
			if (ticket.hasProperty("mykaladiscount")) {
				messageString += "<br/>My Kala point: " + ticket.getProperty("mykalapoing");
				messageString += "<br/>My Kala discount: " + ticket.getDiscountAmount();
			}
			messageString += "</html>";
			map.put("additionalProperties", messageString);
		}

		return map;
	}

	private static StringBuilder buildTicketHeader(Ticket ticket, TicketPrintProperties printProperties) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yy, h:m a");

		StringBuilder ticketHeaderBuilder = new StringBuilder();
		ticketHeaderBuilder.append("<html>");

		beginRow(ticketHeaderBuilder);
		addColumn(ticketHeaderBuilder, "*" + ticket.getType() + "*");
		endRow(ticketHeaderBuilder);

		beginRow(ticketHeaderBuilder);
		addColumn(ticketHeaderBuilder, POSConstants.RECEIPT_REPORT_TERMINAL_LABEL + Application.getInstance().getTerminal().getId());
		endRow(ticketHeaderBuilder);

		beginRow(ticketHeaderBuilder);
		addColumn(ticketHeaderBuilder, POSConstants.RECEIPT_REPORT_TICKET_NO_LABEL + ticket.getId());
		endRow(ticketHeaderBuilder);

		if (ticket.getType() == TicketType.DINE_IN) {
			beginRow(ticketHeaderBuilder);
			addColumn(ticketHeaderBuilder, POSConstants.RECEIPT_REPORT_TABLE_NO_LABEL + ticket.getTableNumbers());
			endRow(ticketHeaderBuilder);

			beginRow(ticketHeaderBuilder);
			addColumn(ticketHeaderBuilder, POSConstants.RECEIPT_REPORT_GUEST_NO_LABEL + ticket.getNumberOfGuests());
			endRow(ticketHeaderBuilder);
		}

		beginRow(ticketHeaderBuilder);
		addColumn(ticketHeaderBuilder, POSConstants.RECEIPT_REPORT_SERVER_LABEL + ticket.getOwner());
		endRow(ticketHeaderBuilder);

		beginRow(ticketHeaderBuilder);
		addColumn(ticketHeaderBuilder, POSConstants.RECEIPT_REPORT_DATE_LABEL + dateFormat.format(new Date()));
		endRow(ticketHeaderBuilder);

		beginRow(ticketHeaderBuilder);
		addColumn(ticketHeaderBuilder, "");
		endRow(ticketHeaderBuilder);

		//customer info section
		if (ticket.getType() != TicketType.DINE_IN) {

			String customerName = ticket.getProperty(Ticket.CUSTOMER_NAME);
			String customerPhone = ticket.getProperty(Ticket.CUSTOMER_PHONE);

			if (StringUtils.isNotEmpty(customerName)) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, "*Delivery to*");
				endRow(ticketHeaderBuilder);

				if (StringUtils.isNotEmpty(customerName)) {
					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, customerName);
					endRow(ticketHeaderBuilder);
				}

				if (StringUtils.isNotEmpty(ticket.getDeliveryAddress())) {
					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, ticket.getDeliveryAddress());
					endRow(ticketHeaderBuilder);
				}
				else {
					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, "Pickup from hotel");
					endRow(ticketHeaderBuilder);
				}

				if (StringUtils.isNotEmpty(customerPhone)) {
					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, "Tel: " + customerPhone);
					endRow(ticketHeaderBuilder);
				}

				if (ticket.getDeliveryDate() != null) {
					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, "Delivery: " + dateFormat.format(ticket.getDeliveryDate()));
					endRow(ticketHeaderBuilder);
				}
			}
		}

		ticketHeaderBuilder.append("</html>");
		return ticketHeaderBuilder;
	}

	public static JasperPrint createKitchenPrint(KitchenTicket ticket) throws Exception {
		HashMap map = new HashMap();

		map.put(HEADER_LINE1, Application.getInstance().getRestaurant().getName());
		map.put(HEADER_LINE2, "*** KITCHEN RECEIPT *** ");
		map.put("cardPayment", true);
		map.put(SHOW_HEADER_SEPARATOR, Boolean.TRUE);
		map.put(SHOW_HEADER_SEPARATOR, Boolean.TRUE);
		map.put(CHECK_NO, POSConstants.RECEIPT_REPORT_TICKET_NO_LABEL + ticket.getTicketId());
		if (ticket.getTableNumbers() != null) {
			map.put(TABLE_NO, POSConstants.RECEIPT_REPORT_TABLE_NO_LABEL + ticket.getTableNumbers());
		}
		//map.put(GUEST_COUNT, POSConstants.RECEIPT_REPORT_GUEST_NO_LABEL + ticket.getNumberOfGuests());
		map.put(SERVER_NAME, POSConstants.RECEIPT_REPORT_SERVER_LABEL + ticket.getServerName());
		map.put(REPORT_DATE, Application.formatDate(new Date()));

		map.put("ticketHeader", "KTICHEN RECEIPT");

		final String FILE_RECEIPT_REPORT = "/com/floreantpos/report/template/KitchenReceipt.jasper";

		KitchenTicketDataSource dataSource = new KitchenTicketDataSource(ticket);

		return createJasperPrint(FILE_RECEIPT_REPORT, map, new JRTableModelDataSource(dataSource));
	}

	public static void printTicketToKitchen(Ticket ticket) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = TicketDAO.getInstance().createNewSession();
			transaction = session.beginTransaction();

			List<KitchenTicket> kitchenTickets = KitchenTicket.fromTicket(ticket);

			for (KitchenTicket kitchenTicket : kitchenTickets) {

				String deviceName = kitchenTicket.getPrinter().getDeviceName();

				JasperPrint jasperPrint = createKitchenPrint(kitchenTicket);
				jasperPrint.setName("KitchenReceipt-" + ticket.getId() + "-" + deviceName);
				jasperPrint.setProperty("printerName", deviceName);
				//JasperViewer.viewReport(jasperPrint, false);

				KitchenDisplay.instance.addTicket(kitchenTicket);

				session.saveOrUpdate(kitchenTicket);

				printQuitely(jasperPrint);

				//markItemsAsPrinted(kitchenTicket);
			}

			session.saveOrUpdate(ticket);
			transaction.commit();

		} catch (Exception e) {
			transaction.rollback();
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		} finally {
			session.close();
		}
	}

	private static void printQuitely(JasperPrint jasperPrint) throws JRException {
		try {
			JasperPrintManager.printReport(jasperPrint, false);
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	//	private static void markItemsAsPrinted(KitchenTicket ticket) {
	//		List<TicketItem> ticketItems = ticket.getTicketItems();
	//		if (ticketItems != null) {
	//			for (TicketItem ticketItem : ticketItems) {
	//				if (!ticketItem.isPrintedToKitchen()) {
	//					ticketItem.setPrintedToKitchen(true);
	//				}
	//
	//				List<TicketItemModifierGroup> modifierGroups = ticketItem.getTicketItemModifierGroups();
	//				if (modifierGroups != null) {
	//					for (TicketItemModifierGroup modifierGroup : modifierGroups) {
	//						modifierGroup.setPrintedToKitchen(true);
	//					}
	//				}
	//
	//				List<TicketItemCookingInstruction> cookingInstructions = ticketItem.getCookingInstructions();
	//				if (cookingInstructions != null) {
	//					for (TicketItemCookingInstruction ticketItemCookingInstruction : cookingInstructions) {
	//						ticketItemCookingInstruction.setPrintedToKitchen(true);
	//					}
	//				}
	//			}
	//		}
	//		
	//		KitchenTicketDAO.getInstance().saveOrUpdate(ticket);
	//	}

	private static String getCardNumber(BankCardMagneticTrack track) {
		String no = "";

		try {
			if (track.getTrack1().hasPrimaryAccountNumber()) {
				no = track.getTrack1().getPrimaryAccountNumber().getAccountNumber();
				no = "************" + no.substring(12);
			}
			else if (track.getTrack2().hasPrimaryAccountNumber()) {
				no = track.getTrack2().getPrimaryAccountNumber().getAccountNumber();
				no = "************" + no.substring(12);
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return no;
	}
}
