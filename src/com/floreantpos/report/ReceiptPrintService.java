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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.config.CardConfig;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.extension.PaymentGatewayPlugin;
import com.floreantpos.main.Application;
import com.floreantpos.model.CardReader;
import com.floreantpos.model.Currency;
import com.floreantpos.model.KitchenTicket;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Printer;
import com.floreantpos.model.RefundTransaction;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.TerminalPrinters;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.VirtualPrinter;
import com.floreantpos.model.dao.KitchenTicketDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TerminalPrintersDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.CurrencyUtil;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.POSUtil;
import com.floreantpos.util.PrintServiceUtil;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import us.fatehi.magnetictrack.bankcard.BankCardMagneticTrack;

public class ReceiptPrintService {
	private static final String TOTAL_TEXT = "totalAmountText";
	private static final String TIPS_TEXT = "tipsText";
	private static final String DELIVERY_CHARGE_TEXT = "deliveryChargeText";
	private static final String SERVICE_CHARGE_TEXT = "serviceChargeText";
	private static final String TAX_TEXT = "taxText";
	private static final String DISCOUNT_TEXT = "discountText";
	private static final String DATA = "data"; //$NON-NLS-1$
	private static final String TITLE = "title"; //$NON-NLS-1$
	private static final String ORDER_ = "ORDER-"; //$NON-NLS-1$
	public static final String PROP_PRINTER_NAME = "printerName"; //$NON-NLS-1$
	private static final String TIP_AMOUNT = "tipAmount"; //$NON-NLS-1$
	private static final String SERVICE_CHARGE = "serviceCharge"; //$NON-NLS-1$
	private static final String DELIVERY_CHARGE = "deliveryCharge"; //$NON-NLS-1$
	private static final String ADJUST_AMOUNT = "adjustAmount";
	private static final String TAX_AMOUNT = "taxAmount"; //$NON-NLS-1$
	private static final String DISCOUNT_AMOUNT = "discountAmount"; //$NON-NLS-1$
	private static final String HEADER_LINE5 = "headerLine5"; //$NON-NLS-1$
	private static final String HEADER_LINE4 = "headerLine4"; //$NON-NLS-1$
	private static final String HEADER_LINE3 = "headerLine3"; //$NON-NLS-1$
	private static final String HEADER_LINE2 = "headerLine2"; //$NON-NLS-1$
	private static final String HEADER_LINE1 = "headerLine1"; //$NON-NLS-1$
	private static final String REPORT_DATE = "reportDate"; //$NON-NLS-1$
	private static final String SERVER_NAME = "serverName"; //$NON-NLS-1$
	private static final String GUEST_COUNT = "guestCount"; //$NON-NLS-1$
	private static final String TABLE_NO = "tableNo"; //$NON-NLS-1$
	private static final String CHECK_NO = "checkNo"; //$NON-NLS-1$
	private static final String TERMINAL = "terminal"; //$NON-NLS-1$
	private static final String SHOW_FOOTER = "showFooter"; //$NON-NLS-1$
	private static final String SHOW_HEADER_SEPARATOR = "showHeaderSeparator"; //$NON-NLS-1$
	private static final String SHOW_SUBTOTAL = "showSubtotal"; //$NON-NLS-1$
	private static final String RECEIPT_TYPE = "receiptType"; //$NON-NLS-1$
	private static final String SUB_TOTAL_TEXT = "subTotalText"; //$NON-NLS-1$
	private static final String QUANTITY_TEXT = "quantityText"; //$NON-NLS-1$
	private static final String ITEM_TEXT = "itemText"; //$NON-NLS-1$
	private static final String CURRENCY_SYMBOL = "currencySymbol"; //$NON-NLS-1$
	private static Log logger = LogFactory.getLog(ReceiptPrintService.class);

	private static final SimpleDateFormat reportDateFormat = new SimpleDateFormat("M/d/yy, h:mm a"); //$NON-NLS-1$

	public static final String CUSTOMER_COPY = "Customer Copy";
	public static final String DRIVER_COPY = "Driver Copy";

	public static final String CENTER = "center";
	public static final String LEFT = "left";
	public static final String RIGHT = "right";

	public static void printGenericReport(String title, String data) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>(2);
		map.put(TITLE, title);
		map.put(DATA, data);
		JasperPrint jasperPrint = createJasperPrint(ReportUtil.getReport("generic-receipt"), map, new JREmptyDataSource()); //$NON-NLS-1$
		jasperPrint.setProperty(PROP_PRINTER_NAME, Application.getPrinters().getReceiptPrinter());
		printQuitely(jasperPrint);
	}

	public static void testPrinter(String deviceName, String title, String data) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>(2);
		map.put(TITLE, title);
		map.put(DATA, data);
		JasperPrint jasperPrint = createJasperPrint(ReportUtil.getReport("test-printer"), map, new JREmptyDataSource()); //$NON-NLS-1$
		jasperPrint.setProperty(PROP_PRINTER_NAME, deviceName);
		printQuitely(jasperPrint);
	}

	public static JasperPrint createJasperPrint(JasperReport report, Map<String, String> properties, JRDataSource dataSource) throws Exception {
		JasperPrint jasperPrint = JasperFillManager.fillReport(report, properties, dataSource);
		return jasperPrint;
	}

	public static JasperPrint createPrint(Ticket ticket, Map<String, String> map, PosTransaction transaction) throws Exception {
		TicketDataSource dataSource = new TicketDataSource(ticket);
		return createJasperPrint(ReportUtil.getReport("ticket-receipt"), map, new JRTableModelDataSource(dataSource)); //$NON-NLS-1$
	}

	/*public static void printTicket(Ticket ticket) {
		try {
	
			TicketPrintProperties printProperties = new TicketPrintProperties("*** ORDER " + ticket.getId() + " ***", false, true, true); //$NON-NLS-1$ //$NON-NLS-2$
			printProperties.setPrintCookingInstructions(false);
			HashMap map = populateTicketProperties(ticket, printProperties, null);
	
			JasperPrint jasperPrint = createPrint(ticket, map, null);
			jasperPrint.setName(ORDER_ + ticket.getId());
			jasperPrint.setProperty(PROP_PRINTER_NAME, Application.getPrinters().getReceiptPrinter());
			printQuitely(jasperPrint);
	
			JasperPrint jasperPrint = createPrint(ticket, map, null);
			jasperPrint.setName(ORDER_ + ticket.getId());
			jasperPrint.setProperty(PROP_PRINTER_NAME, Application.getPrinters().getReceiptPrinter());
			printQuitely(jasperPrint);
	
		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}*/

	public static void printTicket(Ticket ticket) {
		printTicket(ticket, false);
	}

	public static void printTicket(Ticket ticket, boolean printTipsBlock) {
		try {
			PaymentGatewayPlugin paymentGateway = CardConfig.getPaymentGateway();
			if (paymentGateway != null && paymentGateway.printUsingThisTerminal()) {
				if (printTipsBlock) {
					paymentGateway.printTicketWithTipsBlock(ticket);
				}
				else {
					paymentGateway.printTicket(ticket);
				}
				return;
			}
			String receiptPrinter = Application.getPrinters().getReceiptPrinter();
			if (StringUtils.isEmpty(receiptPrinter)) {
				return;
			}

			TicketPrintProperties printProperties = new TicketPrintProperties("*** ORDER " + ticket.getId() + " ***", false, true, true); //$NON-NLS-1$ //$NON-NLS-2$
			printProperties.setPrintCookingInstructions(false);
			HashMap map = populateTicketProperties(ticket, printProperties, null);

			List<TerminalPrinters> terminalPrinters = TerminalPrintersDAO.getInstance().findTerminalPrinters();

			List<Printer> activeReceiptPrinters = new ArrayList<Printer>();

			for (TerminalPrinters terminalPrinters2 : terminalPrinters) {

				int printerType = terminalPrinters2.getVirtualPrinter().getType();

				if (printerType == VirtualPrinter.RECEIPT) {

					Printer printer = new Printer(terminalPrinters2.getVirtualPrinter(), terminalPrinters2.getPrinterName());
					activeReceiptPrinters.add(printer);
				}
			}

			if (activeReceiptPrinters == null || activeReceiptPrinters.isEmpty()) {

				JasperPrint jasperPrint = createPrint(ticket, map, null);
				jasperPrint.setName(ORDER_ + ticket.getId());
				jasperPrint.setProperty(PROP_PRINTER_NAME, receiptPrinter);
				printQuitely(jasperPrint);

			}
			else {

				for (Printer activeReceiptPrinter : activeReceiptPrinters) {

					JasperPrint jasperPrint = createPrint(ticket, map, null);
					jasperPrint.setName(ORDER_ + ticket.getId() + activeReceiptPrinter.getDeviceName());
					jasperPrint.setProperty(PROP_PRINTER_NAME, activeReceiptPrinter.getDeviceName());
					printQuitely(jasperPrint);
				}

			}
		} catch (PosException e) {
			POSMessageDialog.showError(POSUtil.getFocusedWindow(), e.getMessage());
		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	public static void printTicket(Ticket ticket, String copyType) {
		PaymentGatewayPlugin paymentGateway = CardConfig.getPaymentGateway();
		if (paymentGateway != null && paymentGateway.printUsingThisTerminal()) {
			paymentGateway.printTicket(ticket);
			return;
		}

		try {
			String receiptPrinter = Application.getPrinters().getReceiptPrinter();
			if (StringUtils.isEmpty(receiptPrinter)) {
				return;
			}
			TicketPrintProperties printProperties = new TicketPrintProperties("*** ORDER " + ticket.getId() + " ***", false, true, true); //$NON-NLS-1$ //$NON-NLS-2$
			printProperties.setPrintCookingInstructions(false);
			HashMap map = populateTicketProperties(ticket, printProperties, null);
			map.put("copyType", copyType); //$NON-NLS-1$
			map.put("cardPayment", true); //$NON-NLS-1$

			List<TerminalPrinters> terminalPrinters = TerminalPrintersDAO.getInstance().findTerminalPrinters();

			List<Printer> activeReceiptPrinters = new ArrayList<Printer>();

			for (TerminalPrinters terminalPrinters2 : terminalPrinters) {

				int printerType = terminalPrinters2.getVirtualPrinter().getType();

				if (printerType == VirtualPrinter.RECEIPT) {

					Printer printer = new Printer(terminalPrinters2.getVirtualPrinter(), terminalPrinters2.getPrinterName());
					activeReceiptPrinters.add(printer);
				}
			}

			if (activeReceiptPrinters == null || activeReceiptPrinters.isEmpty()) {
				JasperPrint jasperPrint = createPrint(ticket, map, null);
				jasperPrint.setName(ORDER_ + ticket.getId());
				jasperPrint.setProperty(PROP_PRINTER_NAME, receiptPrinter);
				printQuitely(jasperPrint);

			}
			else {
				for (Printer activeReceiptPrinter : activeReceiptPrinters) {
					String deviceName = activeReceiptPrinter.getDeviceName();
					if (StringUtils.isEmpty(deviceName)) {
						continue;
					}
					JasperPrint jasperPrint = createPrint(ticket, map, null);
					jasperPrint.setName(ORDER_ + ticket.getId() + deviceName);
					jasperPrint.setProperty(PROP_PRINTER_NAME, deviceName);
					printQuitely(jasperPrint);
				}

			}
		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	public static JasperPrint createRefundPrint(Ticket ticket, HashMap map) throws Exception {
		TicketDataSource dataSource = new TicketDataSource(ticket);
		return createJasperPrint(ReportUtil.getReport("RefundReceipt"), map, new JRTableModelDataSource(dataSource)); //$NON-NLS-1$
	}

	public static void printRefundTicket(Ticket ticket, RefundTransaction posTransaction) {
		try {
			String receiptPrinter = Application.getPrinters().getReceiptPrinter();
			if (StringUtils.isEmpty(receiptPrinter)) {
				return;
			}

			TicketPrintProperties printProperties = new TicketPrintProperties("*** REFUND RECEIPT ***", true, true, true); //$NON-NLS-1$
			printProperties.setPrintCookingInstructions(false);
			HashMap map = populateTicketProperties(ticket, printProperties, posTransaction);
			map.put("refundAmountText", Messages.getString("ReceiptPrintService.1")); //$NON-NLS-1$ //$NON-NLS-2$
			map.put("refundAmount", NumberUtil.formatNumber(posTransaction.getAmount())); //$NON-NLS-1$
			map.put("cashRefundText", Messages.getString("ReceiptPrintService.2")); //$NON-NLS-1$ //$NON-NLS-2$
			map.put("cashRefund", NumberUtil.formatNumber(posTransaction.getAmount())); //$NON-NLS-1$

			JasperPrint jasperPrint = createRefundPrint(ticket, map);
			jasperPrint.setName("REFUND_" + ticket.getId()); //$NON-NLS-1$
			jasperPrint.setProperty(PROP_PRINTER_NAME, receiptPrinter);
			printQuitely(jasperPrint);

		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	public static void printTransaction(PosTransaction transaction) {
		try {
			Ticket ticket = transaction.getTicket();
			PaymentGatewayPlugin paymentGateway = CardConfig.getPaymentGateway();
			boolean paymentTypeCash = transaction.getPaymentType().equals(PaymentType.CASH.toString());
			boolean printUsingPaymentGateway = paymentGateway != null && paymentGateway.printUsingThisTerminal();

			if (printUsingPaymentGateway && paymentTypeCash) {
				return;
			}

			if (printUsingPaymentGateway) {
				paymentGateway.printTransaction(transaction, false, false);
				return;
			}

			String receiptPrinter = Application.getPrinters().getReceiptPrinter();
			if (StringUtils.isEmpty(receiptPrinter)) {
				return;
			}

			TicketPrintProperties printProperties = new TicketPrintProperties(Messages.getString("ReceiptPrintService.3"), true, true, true); //$NON-NLS-1$
			printProperties.setPrintCookingInstructions(false);
			HashMap map = populateTicketProperties(ticket, printProperties, transaction);

			if (transaction != null && transaction.isCard()) {
				CardReader cardReader = CardReader.fromString(transaction.getCardReader());

				if (cardReader == CardReader.EXTERNAL_TERMINAL) {
					return;
				}

				map.put("cardPayment", true); //$NON-NLS-1$
				map.put("copyType", Messages.getString("ReceiptPrintService.4")); //$NON-NLS-1$ //$NON-NLS-2$
				JasperPrint jasperPrint = createPrint(ticket, map, transaction);
				jasperPrint.setName("Ticket-" + ticket.getId() + "-CustomerCopy"); //$NON-NLS-1$ //$NON-NLS-2$
				jasperPrint.setProperty(PROP_PRINTER_NAME, receiptPrinter);
				printQuitely(jasperPrint);

				map.put("copyType", Messages.getString("ReceiptPrintService.5")); //$NON-NLS-1$ //$NON-NLS-2$
				jasperPrint = createPrint(ticket, map, transaction);
				jasperPrint.setName("Ticket-" + ticket.getId() + "-MerchantCopy"); //$NON-NLS-1$ //$NON-NLS-2$
				jasperPrint.setProperty(PROP_PRINTER_NAME, receiptPrinter);
				printQuitely(jasperPrint);
			}
			else {
				JasperPrint jasperPrint = createPrint(ticket, map, transaction);
				jasperPrint.setName("Ticket-" + ticket.getId()); //$NON-NLS-1$
				jasperPrint.setProperty(PROP_PRINTER_NAME, receiptPrinter);
				printQuitely(jasperPrint);
			}

		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	public static void printTransaction(PosTransaction transaction, boolean printStoreCopy, boolean printCustomerCopy) {
		try {
			Ticket ticket = transaction.getTicket();
			PaymentGatewayPlugin paymentGateway = CardConfig.getPaymentGateway();
			boolean paymentTypeCash = transaction.getPaymentType().equals(PaymentType.CASH.toString());
			boolean printUsingPaymentGateway = paymentGateway != null && paymentGateway.printUsingThisTerminal();

			if (printUsingPaymentGateway && paymentTypeCash) {
				return;
			}

			if (printUsingPaymentGateway) {
				paymentGateway.printTransaction(transaction, printStoreCopy, printCustomerCopy);
				return;
			}

			TicketPrintProperties printProperties = new TicketPrintProperties(Messages.getString("ReceiptPrintService.6"), true, true, true); //$NON-NLS-1$
			printProperties.setPrintCookingInstructions(false);
			HashMap map = populateTicketProperties(ticket, printProperties, transaction);

			if (transaction != null && transaction.isCard()) {
				map.put("cardPayment", true); //$NON-NLS-1$
				map.put("copyType", Messages.getString("ReceiptPrintService.7")); //$NON-NLS-1$ //$NON-NLS-2$

				JasperPrint jasperPrint = createPrint(ticket, map, transaction);
				jasperPrint.setName("Ticket-" + ticket.getId() + "-MerchantCopy"); //$NON-NLS-1$ //$NON-NLS-2$
				jasperPrint.setProperty(PROP_PRINTER_NAME, Application.getPrinters().getReceiptPrinter());
				printQuitely(jasperPrint);

				if (printCustomerCopy) {
					map.put("copyType", Messages.getString("ReceiptPrintService.8")); //$NON-NLS-1$ //$NON-NLS-2$

					jasperPrint = createPrint(ticket, map, transaction);
					jasperPrint.setName("Ticket-" + ticket.getId() + "-CustomerCopy"); //$NON-NLS-1$ //$NON-NLS-2$
					jasperPrint.setProperty(PROP_PRINTER_NAME, Application.getPrinters().getReceiptPrinter());
					printQuitely(jasperPrint);
				}
			}
			else {
				JasperPrint jasperPrint = createPrint(ticket, map, transaction);
				jasperPrint.setName("Ticket-" + ticket.getId()); //$NON-NLS-1$
				jasperPrint.setProperty(PROP_PRINTER_NAME, Application.getPrinters().getReceiptPrinter());
				printQuitely(jasperPrint);
			}

		} catch (Exception e) {
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		}
	}

	private static void beginRow(StringBuilder html) {
		html.append("<div>"); //$NON-NLS-1$
	}

	private static void endRow(StringBuilder html) {
		html.append("</div>"); //$NON-NLS-1$
	}

	private static void addColumn(StringBuilder html, String columnText) {
		html.append("<span>" + columnText + "</span>"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static HashMap populateTicketProperties(Ticket ticket, TicketPrintProperties printProperties, PosTransaction transaction) {
		Restaurant restaurant = RestaurantDAO.getRestaurant();

		double totalAmount = ticket.getTotalAmount();
		double tipAmount = 0;

		HashMap map = new HashMap();

		map.put(JRParameter.IS_IGNORE_PAGINATION, false);

		String currencySymbol = CurrencyUtil.getCurrencySymbol();
		map.put(CURRENCY_SYMBOL, currencySymbol);
		map.put(ITEM_TEXT, POSConstants.RECEIPT_REPORT_ITEM_LABEL);
		map.put(QUANTITY_TEXT, POSConstants.RECEIPT_REPORT_QUANTITY_LABEL);
		map.put("subtotalHeaderText", Messages.getString("RECEIPT_REPORT_SUBTOTAL_HEADER")); //$NON-NLS-1$ //$NON-NLS-2$
		map.put(SUB_TOTAL_TEXT, POSConstants.RECEIPT_REPORT_SUBTOTAL_LABEL + " " + currencySymbol); //$NON-NLS-1$
		map.put(TOTAL_TEXT, POSConstants.RECEIPT_REPORT_TOTAL_AMOUNT_LABEL + " " + currencySymbol); //$NON-NLS-1$
		map.put("tenderedAmountText", POSConstants.RECEIPT_REPORT_TENDERED_AMOUNT_LABEL + " " + currencySymbol); //$NON-NLS-1$
		map.put(DISCOUNT_TEXT, POSConstants.RECEIPT_REPORT_DISCOUNT_LABEL + " " + currencySymbol); //$NON-NLS-1$
		map.put(TAX_TEXT, POSConstants.RECEIPT_REPORT_TAX_LABEL + " " + currencySymbol); //$NON-NLS-1$
		map.put(SERVICE_CHARGE_TEXT, POSConstants.RECEIPT_REPORT_SERVICE_CHARGE_LABEL + " " + currencySymbol); //$NON-NLS-1$
		map.put(DELIVERY_CHARGE_TEXT, POSConstants.RECEIPT_REPORT_DELIVERY_CHARGE_LABEL + " " + currencySymbol); //$NON-NLS-1$
		map.put(TIPS_TEXT, POSConstants.RECEIPT_REPORT_TIPS_LABEL + " " + currencySymbol); //$NON-NLS-1$
		map.put("paidAmountText", POSConstants.RECEIPT_REPORT_PAIDAMOUNT_LABEL + " " + currencySymbol); //$NON-NLS-1$
		map.put("dueAmountText", POSConstants.RECEIPT_REPORT_DUEAMOUNT_LABEL + " " + currencySymbol); //$NON-NLS-1$
		map.put("changeAmountText", POSConstants.RECEIPT_REPORT_CHANGEAMOUNT_LABEL + " " + currencySymbol); //$NON-NLS-1$

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

		map.put("ticketHeader", ticketHeaderBuilder.toString()); //$NON-NLS-1$

		if (TerminalConfig.isShowBarcodeOnReceipt()) {
			map.put("barcode", String.valueOf(ticket.getId())); //$NON-NLS-1$
		}

		if (printProperties.isShowHeader()) {
			map.put(HEADER_LINE1, restaurant.getName());
			map.put(HEADER_LINE2, restaurant.getAddressLine1());
			map.put(HEADER_LINE3, restaurant.getAddressLine2());
			map.put(HEADER_LINE4, restaurant.getAddressLine3());
			map.put(HEADER_LINE5, restaurant.getTelephone());
		}

		if (printProperties.isShowFooter()) {
			map.put("subtotalAmount", NumberUtil.formatNumber(ticket.getSubtotalAmount())); //$NON-NLS-1$
			double toleranceAmount = ticket.calculateToleranceAmount();
			if (toleranceAmount > 0.0) {
				map.put(ADJUST_AMOUNT, NumberUtil.formatNumber(toleranceAmount));
			}
			if (ticket.getDiscountAmount() > 0.0) {
				map.put(DISCOUNT_AMOUNT, NumberUtil.formatNumber(ticket.getDiscountAmount()));
			}

			if (ticket.getTaxAmount() > 0.0) {
				map.put(TAX_AMOUNT, NumberUtil.formatNumber(ticket.getTaxAmount()));
			}

			if (ticket.getServiceCharge() > 0.0) {
				map.put(SERVICE_CHARGE, NumberUtil.formatNumber(ticket.getServiceCharge()));
			}

			if (ticket.getDeliveryCharge() > 0.0) {
				map.put(DELIVERY_CHARGE, NumberUtil.formatNumber(ticket.getDeliveryCharge()));
			}

			if (ticket.getGratuity() != null) {
				tipAmount = ticket.getGratuity().getAmount();
				map.put(TIP_AMOUNT, NumberUtil.formatNumber(tipAmount));
			}
			map.put("totalAmount", NumberUtil.formatNumber(totalAmount)); //$NON-NLS-1$
			if (transaction != null) {
				map.put("tenderedAmount", NumberUtil.formatNumber(transaction.getTenderAmount())); //$NON-NLS-1$
			}
			map.put("paidAmount", NumberUtil.formatNumber(ticket.getPaidAmount())); //$NON-NLS-1$
			map.put("dueAmount", NumberUtil.formatNumber(ticket.getDueAmount())); //$NON-NLS-1$
			map.put("footerMessage", restaurant.getTicketFooterMessage()); //$NON-NLS-1$
			map.put("copyType", printProperties.getReceiptCopyType()); //$NON-NLS-1$

			if (ticket.isRefunded()) {
				populateRefundProperties(ticket.getTransactions(), map);
			}

			if (transaction != null) {
				double changedAmount = transaction.getTenderAmount() - transaction.getAmount();
				if (changedAmount > 0) {
					map.put("changedAmount", NumberUtil.formatNumber(changedAmount)); //$NON-NLS-1$
				}
				if (transaction.isCard()) {
					map.put("cardPayment", true); //$NON-NLS-1$
					if (transaction.hasProperty("requireSignature")) {
						Boolean requireSignature = Boolean.valueOf(transaction.getProperty("requireSignature"));
						map.put("showSignatureField", requireSignature);
					}
					String cardInformationForReceipt = CardConfig.getPaymentGateway().getProcessor().getCardInformationForReceipt(transaction);
					if (StringUtils.isEmpty(cardInformationForReceipt)) {
						cardInformationForReceipt = getCardInformation(transaction);
					}
					map.put("cardInformation", cardInformationForReceipt); //$NON-NLS-1$
				}
			}
			if (TerminalConfig.isEnabledMultiCurrency()) {
				if (Boolean.valueOf(ticket.getProperty("MULTICURRENCY_CASH"))) {//$NON-NLS-1$
					StringBuilder multiCurrencyBreakdownCashBack = buildMultiCurrency(ticket, printProperties);
					if (multiCurrencyBreakdownCashBack != null)
						map.put("additionalProperties", multiCurrencyBreakdownCashBack.toString()); //$NON-NLS-1$
				}
				else if (!ticket.isPaid()) {
					StringBuilder multiCurrencyTotalAmount = buildMultiCurrencyTotalAmount(ticket, printProperties);
					if (multiCurrencyTotalAmount != null)
						map.put("additionalProperties", multiCurrencyTotalAmount.toString()); //$NON-NLS-1$
				}
			}
		}

		return map;
	}

	private static void populateRefundProperties(Set<PosTransaction> transactions, HashMap map) {
		if (transactions == null)
			return;
		TicketPrintProperties printProperties = new TicketPrintProperties("*** REFUND RECEIPT ***", true, true, true); //$NON-NLS-1$
		printProperties.setPrintCookingInstructions(false);
		double refundAmount = 0;
		for (PosTransaction transaction : transactions) {
			if (transaction instanceof RefundTransaction)
				refundAmount += transaction.getAmount();
		}
		map.put("additionalProperties",
				"<html><b>" + Messages.getString("ReceiptPrintService.1") + " " + CurrencyUtil.getCurrencySymbol() + "&nbsp;" + refundAmount + "</b></html>");
	}

	private static String getCardInformation(PosTransaction transaction) {
		String string = "<br/>CARD INFO: ------------------------";
		string += "<br/>PROCESS: " + transaction.getCardReader();
		string += "<br/> TYPE: " + transaction.getCardType();
		try {
			String cardNumber = transaction.getCardNumber();
			if (transaction.getCardNumber() != null) {
				string += "<br/> ACCT: **** **** **** " + cardNumber.substring(cardNumber.length() - 4, cardNumber.length());
			}
			if (transaction.getCardHolderName() != null) {
				string += "<br/> CARDHOLDER: " + transaction.getCardHolderName();
			}
			if (transaction.getCardTransactionId() != null) {
				string += "<br/> TRANS ID: " + transaction.getCardTransactionId();
			}
			string += "<br/> APPROVAL: " + transaction.getCardAuthCode();
			if (transaction.getCardAID() != null) {
				string += "<br/> AID: " + transaction.getCardAID();
			}
			if (transaction.getCardARQC() != null) {
				string += "<br/> ARQC: " + transaction.getCardARQC();
			}
		} catch (Exception e) {
			logger.equals(e);
		}
		return string;
	}

	private static StringBuilder buildTicketHeader(Ticket ticket, TicketPrintProperties printProperties) {

		StringBuilder ticketHeaderBuilder = new StringBuilder();
		ticketHeaderBuilder.append("<html>"); //$NON-NLS-1$

		beginRow(ticketHeaderBuilder);
		addColumn(ticketHeaderBuilder, "*" + ticket.getOrderType() + "*"); //$NON-NLS-1$ //$NON-NLS-2$
		endRow(ticketHeaderBuilder);

		beginRow(ticketHeaderBuilder);
		addColumn(ticketHeaderBuilder, POSConstants.RECEIPT_REPORT_TERMINAL_LABEL + Application.getInstance().getTerminal().getId());
		endRow(ticketHeaderBuilder);

		beginRow(ticketHeaderBuilder);
		addColumn(ticketHeaderBuilder, POSConstants.RECEIPT_REPORT_TICKET_NO_LABEL + ticket.getId());
		endRow(ticketHeaderBuilder);

		OrderType orderType = ticket.getOrderType();
		if (orderType.isShowTableSelection() || orderType.isShowGuestSelection()) {//fix
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
		addColumn(ticketHeaderBuilder, POSConstants.RECEIPT_REPORT_DATE_LABEL + reportDateFormat.format(new Date()));
		endRow(ticketHeaderBuilder);

		beginRow(ticketHeaderBuilder);
		addColumn(ticketHeaderBuilder, ""); //$NON-NLS-1$
		endRow(ticketHeaderBuilder);

		User driver = ticket.getAssignedDriver();
		if (driver != null) {
			beginRow(ticketHeaderBuilder);
			addColumn(ticketHeaderBuilder, "*Driver*"); //$NON-NLS-1$ //$NON-NLS-2$
			endRow(ticketHeaderBuilder);

			if (StringUtils.isNotEmpty(driver.getFullName())) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, driver.getFullName());
				endRow(ticketHeaderBuilder);
			}

			beginRow(ticketHeaderBuilder);
			addColumn(ticketHeaderBuilder, ""); //$NON-NLS-1$
			endRow(ticketHeaderBuilder);
		}

		//customer info section
		if (orderType.isRequiredCustomerData()) {

			String customerName = ticket.getProperty(Ticket.CUSTOMER_NAME);
			String customerMobile = ticket.getProperty(Ticket.CUSTOMER_MOBILE);

			if (StringUtils.isNotEmpty(customerName)) {
				beginRow(ticketHeaderBuilder);
				addColumn(ticketHeaderBuilder, Messages.getString("ReceiptPrintService.9")); //$NON-NLS-1$
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

					if (StringUtils.isNotEmpty(ticket.getExtraDeliveryInfo())) {
						beginRow(ticketHeaderBuilder);
						addColumn(ticketHeaderBuilder, ticket.getExtraDeliveryInfo());
						endRow(ticketHeaderBuilder);
					}
				}
				else {
					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, Messages.getString("ReceiptPrintService.111")); //$NON-NLS-1$
					endRow(ticketHeaderBuilder);
				}

				if (StringUtils.isNotEmpty(customerMobile)) {
					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, "Tel: " + customerMobile); //$NON-NLS-1$
					endRow(ticketHeaderBuilder);
				}

				if (ticket.getDeliveryDate() != null) {
					beginRow(ticketHeaderBuilder);
					addColumn(ticketHeaderBuilder, "Delivery: " + reportDateFormat.format(ticket.getDeliveryDate())); //$NON-NLS-1$
					endRow(ticketHeaderBuilder);
				}
			}
		}

		ticketHeaderBuilder.append("</html>"); //$NON-NLS-1$
		return ticketHeaderBuilder;
	}

	private static StringBuilder buildMultiCurrencyTotalAmount(Ticket ticket, TicketPrintProperties printProperties) {
		DecimalFormat decimalFormat = new DecimalFormat("0.00"); //$NON-NLS-1$

		StringBuilder currencyAmountBuilder = new StringBuilder();
		currencyAmountBuilder.append("<html><table>"); //$NON-NLS-1$

		String sep = "------------------------------------";//$NON-NLS-1$

		beginRow(currencyAmountBuilder);
		addColumn(currencyAmountBuilder, "&nbsp;");//$NON-NLS-1$
		addColumn(currencyAmountBuilder, "&nbsp;");//$NON-NLS-1$
		addColumn(currencyAmountBuilder, "&nbsp;");//$NON-NLS-1$
		endRow(currencyAmountBuilder);

		beginRow(currencyAmountBuilder);
		addColumn(currencyAmountBuilder, "<b>Currency breakdown</b>");
		endRow(currencyAmountBuilder);

		beginRow(currencyAmountBuilder);
		addColumn(currencyAmountBuilder, sep);
		endRow(currencyAmountBuilder);

		beginRow(currencyAmountBuilder);
		addColumn(currencyAmountBuilder, getHtmlText("", 10, CENTER));//$NON-NLS-1$
		addColumn(currencyAmountBuilder, getHtmlText("Net Amount", 12, CENTER));//$NON-NLS-1$
		//addColumn(currencyAmountBuilder, getHtmlText("Paid", 10, CENTER));//$NON-NLS-1$
		addColumn(currencyAmountBuilder, getHtmlText("Due", 12, CENTER));//$NON-NLS-1$
		endRow(currencyAmountBuilder);

		beginRow(currencyAmountBuilder);
		addColumn(currencyAmountBuilder, sep);
		endRow(currencyAmountBuilder);

		int rowCount = 0;
		for (Currency currency : CurrencyUtil.getAllCurrency()) {
			if (currency == null) {
				continue;
			}
			String key = currency.getName();
			double rate = currency.getExchangeRate();
			beginRow(currencyAmountBuilder);
			addColumn(currencyAmountBuilder, getHtmlText(key, 10, LEFT));
			addColumn(currencyAmountBuilder, getHtmlText(decimalFormat.format(ticket.getTotalAmount() * rate), 12, RIGHT));
			//addColumn(currencyAmountBuilder, getHtmlText(decimalFormat.format(ticket.getPaidAmount() * rate), 10, RIGHT));
			addColumn(currencyAmountBuilder, getHtmlText(decimalFormat.format(ticket.getDueAmount() * rate), 12, RIGHT));
			endRow(currencyAmountBuilder);
			rowCount++;
		}

		if (rowCount == 0) {
			return null;
		}
		currencyAmountBuilder.append("</table></html>"); //$NON-NLS-1$
		return currencyAmountBuilder;
	}

	private static StringBuilder buildMultiCurrency(Ticket ticket, TicketPrintProperties printProperties) {

		DecimalFormat decimalFormat = new DecimalFormat("0.000"); //$NON-NLS-1$

		StringBuilder currencyAmountBuilder = new StringBuilder();
		currencyAmountBuilder.append("<html><table>"); //$NON-NLS-1$

		String sep = "------------------------------------";//$NON-NLS-1$

		beginRow(currencyAmountBuilder);
		addColumn(currencyAmountBuilder, "&nbsp;");//$NON-NLS-1$
		addColumn(currencyAmountBuilder, "&nbsp;");//$NON-NLS-1$
		addColumn(currencyAmountBuilder, "&nbsp;");//$NON-NLS-1$
		endRow(currencyAmountBuilder);

		String groupSettleTickets = ticket.getProperty("GROUP_SETTLE_TICKETS");
		if (groupSettleTickets == null) {
			groupSettleTickets = "";
		}

		beginRow(currencyAmountBuilder);
		addColumn(currencyAmountBuilder, groupSettleTickets + "<b>Currency breakdown</b>");
		endRow(currencyAmountBuilder);

		beginRow(currencyAmountBuilder);
		addColumn(currencyAmountBuilder, sep);
		endRow(currencyAmountBuilder);

		beginRow(currencyAmountBuilder);
		addColumn(currencyAmountBuilder, getHtmlText("", 10, CENTER));//$NON-NLS-1$
		addColumn(currencyAmountBuilder, getHtmlText("Paid", 12, CENTER));//$NON-NLS-1$
		addColumn(currencyAmountBuilder, getHtmlText("Cashback", 12, CENTER));//$NON-NLS-1$
		endRow(currencyAmountBuilder);

		beginRow(currencyAmountBuilder);
		addColumn(currencyAmountBuilder, sep);
		endRow(currencyAmountBuilder);

		int rowCount = 0;
		List<Currency> allCurrency = CurrencyUtil.getAllCurrency();
		if (allCurrency != null) {
			for (Currency currency : allCurrency) {
				if (currency == null) {
					continue;
				}
				String key = currency.getName();

				String paidAmount = ticket.getProperty(key);
				String cashBackAmount = ticket.getProperty(key + "_CASH_BACK");//$NON-NLS-1$

				if (paidAmount == null) {
					paidAmount = "0";//$NON-NLS-1$
				}
				if (cashBackAmount == null) {
					cashBackAmount = "0";//$NON-NLS-1$
				}
				Double paid = Double.valueOf(paidAmount);
				Double changeDue = Double.valueOf(cashBackAmount);
				if (paid == 0 && changeDue == 0) {
					continue;
				}
				beginRow(currencyAmountBuilder);
				addColumn(currencyAmountBuilder, getHtmlText(key, 10, LEFT));
				addColumn(currencyAmountBuilder, getHtmlText(decimalFormat.format(paid), 12, RIGHT));
				addColumn(currencyAmountBuilder, getHtmlText(decimalFormat.format(changeDue), 12, RIGHT));
				endRow(currencyAmountBuilder);
				rowCount++;
			}
		}

		if (rowCount == 0) {
			return null;
		}
		currencyAmountBuilder.append("</table></html>"); //$NON-NLS-1$
		return currencyAmountBuilder;
	}

	public static String getHtmlText(String txt, int length, String align) {
		if (txt.length() > 34) {
			txt = txt.substring(0, 34);
		}

		if (align.equals(CENTER)) {
			int space = (length - txt.length()) / 2;
			for (int i = 1; i < space; i++) {
				txt = "&nbsp;" + txt + "&nbsp;";//$NON-NLS-1$//$NON-NLS-2$
			}
		}
		else if (align.equals(RIGHT)) {
			int space = (length - txt.length());
			for (int i = 1; i < space; i++) {
				txt = "&nbsp;" + txt;//$NON-NLS-1$
			}
		}
		else if (align.equals(LEFT)) {
			int space = (length - txt.length());
			for (int i = 1; i < space; i++) {
				txt = txt + "&nbsp;";//$NON-NLS-1$
			}
		}
		return txt;
	}

	public static JasperPrint createKitchenPrint(KitchenTicket ticket) throws Exception {
		HashMap map = new HashMap();

		map.put(HEADER_LINE1, Application.getInstance().getRestaurant().getName());
		map.put(HEADER_LINE2, Messages.getString("ReceiptPrintService.115")); //$NON-NLS-1$
		map.put("cardPayment", true); //$NON-NLS-1$
		map.put(SHOW_HEADER_SEPARATOR, Boolean.TRUE);
		map.put(SHOW_HEADER_SEPARATOR, Boolean.TRUE);
		map.put(CHECK_NO, POSConstants.RECEIPT_REPORT_TICKET_NO_LABEL + ticket.getTicketId());
		if (ticket.getTableNumbers() != null && ticket.getTableNumbers().size() > 0) {
			map.put(TABLE_NO, POSConstants.RECEIPT_REPORT_TABLE_NO_LABEL + ticket.getTableNumbers());
		}

		if (StringUtils.isNotEmpty(ticket.getCustomerName())) {
			map.put("customer", Messages.getString("ReceiptPrintService.0") + ticket.getCustomerName()); //$NON-NLS-1$ //$NON-NLS-2$
		}

		map.put(SERVER_NAME, POSConstants.RECEIPT_REPORT_SERVER_LABEL + ticket.getServerName());
		map.put(REPORT_DATE, Messages.getString("ReceiptPrintService.119") + reportDateFormat.format(new Date())); //$NON-NLS-1$

		map.put("ticketHeader", Messages.getString("ReceiptPrintService.10")); //$NON-NLS-1$ //$NON-NLS-2$
		String ticketType = ticket.getTicketType();
		if (StringUtils.isNotEmpty(ticketType)) {
			ticketType = ticketType.replaceAll("_", " "); //$NON-NLS-1$ //$NON-NLS-2$
		}
		map.put("orderType", "* " + ticketType + " *"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		KitchenTicketDataSource dataSource = new KitchenTicketDataSource(ticket);

		return createJasperPrint(ReportUtil.getReport("kitchen-receipt"), map, new JRTableModelDataSource(dataSource)); //$NON-NLS-1$
	}

	public static JasperPrint createKitchenPrint(String virtualPrinterName, KitchenTicket ticket, String deviceName) throws Exception {
		HashMap map = new HashMap();

		map.put(HEADER_LINE1, Application.getInstance().getRestaurant().getName());
		map.put(HEADER_LINE2, Messages.getString("ReceiptPrintService.115")); //$NON-NLS-1$
		map.put("cardPayment", true); //$NON-NLS-1$
		map.put(SHOW_HEADER_SEPARATOR, Boolean.TRUE);
		map.put(SHOW_HEADER_SEPARATOR, Boolean.TRUE);
		map.put(CHECK_NO, POSConstants.RECEIPT_REPORT_TICKET_NO_LABEL + ticket.getTicketId() + "-" + ticket.getSequenceNumber());
		if (ticket.getTableNumbers() != null && ticket.getTableNumbers().size() > 0) {
			map.put(TABLE_NO, POSConstants.RECEIPT_REPORT_TABLE_NO_LABEL + ticket.getTableNumbers());
		}

		if (StringUtils.isNotEmpty(ticket.getCustomerName())) {
			map.put("customer", Messages.getString("ReceiptPrintService.0") + ticket.getCustomerName()); //$NON-NLS-1$ //$NON-NLS-2$
		}

		map.put(SERVER_NAME, POSConstants.RECEIPT_REPORT_SERVER_LABEL + ticket.getServerName());

		map.put(REPORT_DATE, Messages.getString("ReceiptPrintService.119") + DateUtil.getReportDate()); //$NON-NLS-1$

		map.put("ticketHeader", Messages.getString("ReceiptPrintService.10")); //$NON-NLS-1$ //$NON-NLS-2$
		String ticketType = ticket.getTicketType();
		if (StringUtils.isNotEmpty(ticketType)) {
			ticketType = ticketType.replaceAll("_", " "); //$NON-NLS-1$ //$NON-NLS-2$
		}
		map.put("orderType", "* " + ticketType + " *"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		map.put("printerName", "Printer Name : " + virtualPrinterName); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		KitchenTicketDataSource dataSource = new KitchenTicketDataSource(ticket);

		String reportName = "kitchen-receipt";

		if (TerminalConfig.isGroupKitchenReceiptItems()) {
			reportName = "kitchen-receipt-grouped-by-categories";
		}
		return createJasperPrint(ReportUtil.getReport(reportName), map, new JRTableModelDataSource(dataSource)); //$NON-NLS-1$
	}

	public static void printToKitchen(Ticket ticket) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = KitchenTicketDAO.getInstance().createNewSession();
			transaction = session.beginTransaction();

			List<KitchenTicket> kitchenTickets = KitchenTicket.fromTicket(ticket);

			for (KitchenTicket kitchenTicket : kitchenTickets) {
				Printer printer = kitchenTicket.getPrinter();
				if (printer == null) {
					continue;
				}
				String deviceName = printer.getDeviceName();
				JasperPrint jasperPrint = createKitchenPrint(printer.getVirtualPrinter().getName(), kitchenTicket, deviceName);

				jasperPrint.setName("FP_KitchenReceipt_" + ticket.getId() + "_" + kitchenTicket.getSequenceNumber()); //$NON-NLS-1$ //$NON-NLS-2$ 
				jasperPrint.setProperty(PROP_PRINTER_NAME, deviceName);

				printQuitely(jasperPrint);

				session.saveOrUpdate(kitchenTicket);
			}
			transaction.commit();

			TicketDAO.getInstance().saveOrUpdate(ticket);

		} catch (Exception e) {
			transaction.rollback();
			logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
		} finally {
			session.close();
		}
	}

	public static void printQuitely(JasperPrint jasperPrint) throws JRException {
		try {
			JRPrintServiceExporter exporter = new JRPrintServiceExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE,
					PrintServiceUtil.getPrintServiceForPrinter(jasperPrint.getProperty(PROP_PRINTER_NAME)));
			exporter.exportReport();
		} catch (Exception x) {
			String msg = "No print selected\n";
			String message = x.getMessage();
			if (message != null && message.contains("PrinterAbortException")) {//$NON-NLS-1$
				//do nothing
			}
			else {
				logger.error(msg + x);
			}
		}
	}

	private static String getCardNumber(BankCardMagneticTrack track) {
		String no = ""; //$NON-NLS-1$

		try {
			if (track.getTrack1().hasPrimaryAccountNumber()) {
				no = track.getTrack1().getPrimaryAccountNumber().getAccountNumber();
				no = "************" + no.substring(12); //$NON-NLS-1$
			}
			else if (track.getTrack2().hasPrimaryAccountNumber()) {
				no = track.getTrack2().getPrimaryAccountNumber().getAccountNumber();
				no = "************" + no.substring(12); //$NON-NLS-1$
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return no;
	}
}
