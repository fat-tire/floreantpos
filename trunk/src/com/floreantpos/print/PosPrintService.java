package com.floreantpos.print;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.floreantpos.main.Application;
import com.floreantpos.model.DrawerPullReport;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TipsCashoutReport;
import com.floreantpos.model.TipsCashoutReportTableModel;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.report.ReportUtil;
import com.floreantpos.util.NumberUtil;

public class PosPrintService {
	private static Log logger = LogFactory.getLog(PosPrintService.class);
	
	static int firstColumnLength = 4;
	static int secondColumnLength = 16;
	static int thirdColumnLength = 8;
	static int fourthColumnLength = 8;
	static int columnGap = 2;
	static int totalLength = 42;

	static int kitchenFirstColumnLength = 4;
	static int kitchenSecondColumnLength = 24;
	static int kitchenThirdColumnLength = 8;

	static void printCentered(PosPrinter printer, String text) {
		int blank = totalLength - text.length();
		int half = blank / 2;

		text = StringUtils.leftPad(text, half + text.length(), ' ');
		text = StringUtils.rightPad(text, totalLength, ' ');

		printer.beginLine(PosPrinter.SIZE_0);
		printer.printText(text);
		printer.endLine();
	}

	static void printSeparator(PosPrinter printer, char separatorChar) {
		String text = String.valueOf(separatorChar);
		text = StringUtils.leftPad(text, totalLength, separatorChar);

		printer.beginLine(PosPrinter.SIZE_0);
		printer.printText(text);
		printer.endLine();

	}

	static void print1stColumn(PosPrinter printer, String text, int columnLength) {
		printer.beginLine(PosPrinter.SIZE_0);
		if (text.length() < columnLength) {
			text = StringUtils.rightPad(text, columnLength, ' ');
		}
		printer.printText(text);
		printer.printText(PosPrinter.JPOS_SIZE0);
	}

	static void printLastColumn(PosPrinter printer, String text, int columnLength) {
		if (text.length() < columnLength) {
			text = StringUtils.rightPad(text, columnLength, ' ');
		}
		printer.printText(text);
		printer.printText(PosPrinter.JPOS_SIZE0);
		printer.endLine();
	}

	static void printColumn(PosPrinter printer, String text, int columnLength) {
		if (text.length() < columnLength) {
			text = StringUtils.rightPad(text, columnLength, ' ');
		}
		printer.printText(text);
		printer.printText(PosPrinter.JPOS_SIZE0);
	}

	static void printRightAlignedColumn(PosPrinter printer, String text, int columnLength) {
		if (text.length() < columnLength) {
			text = StringUtils.leftPad(text, columnLength, ' ');
		}
		printer.printText(text);
		printer.printText(PosPrinter.JPOS_SIZE0);
	}

	static void printColumnSeparator(PosPrinter printer) {
		printer.printText("  ");
	}

	static void printMultilineColumn(PosPrinter printer, String text, int previoisColumnLength, int columnLength, boolean padLeft) {
		if (text.length() < columnLength) {
			if (padLeft) {
				text = StringUtils.leftPad(text, text.length() + previoisColumnLength + columnGap, ' ');
				text = StringUtils.rightPad(text, columnLength + previoisColumnLength + columnGap, ' ');
			}
			else {
				text = StringUtils.rightPad(text, columnLength, ' ');
			}
		}
		else if (text.length() > columnLength) {
			String stringBefore = text.substring(0, columnLength);
			String stringAfter = text.substring(columnLength);

			if (padLeft) {
				stringBefore = StringUtils.leftPad(stringBefore, previoisColumnLength + columnGap, ' ');
			}

			printer.printText(stringBefore);
			printer.endLine();
			printer.beginLine(PosPrinter.SIZE_0);
			printSecondColumn(printer, stringAfter, previoisColumnLength, columnLength, true);

			return;
		}

		printer.printText(text);
	}

	static void printFirstColumn(PosPrinter printer, String text, int columnLength) {
		if (text.length() < columnLength) {
			text = StringUtils.rightPad(text, columnLength, ' ');
		}
		printer.printText(text);
		printer.printText(PosPrinter.JPOS_SIZE0 + "  ");
	}

	static void printSecondColumn(PosPrinter printer, String text, int firstColumnLength, int secondColumnLength, boolean padLeft) {
		if (text.length() < secondColumnLength) {
			if (padLeft) {
				text = StringUtils.leftPad(text, text.length() + firstColumnLength + columnGap, ' ');
				text = StringUtils.rightPad(text, secondColumnLength + firstColumnLength + columnGap, ' ');
			}
			else {
				text = StringUtils.rightPad(text, secondColumnLength, ' ');
			}
		}
		else if (text.length() > secondColumnLength) {
			String stringBefore = text.substring(0, secondColumnLength);
			String stringAfter = text.substring(secondColumnLength);

			if (padLeft) {
				stringBefore = StringUtils.leftPad(stringBefore, firstColumnLength + columnGap, ' ');
			}

			printer.printText(stringBefore);
			printer.endLine();
			printer.beginLine(PosPrinter.SIZE_0);
			printSecondColumn(printer, stringAfter, firstColumnLength, secondColumnLength, true);

			return;
		}

		printer.printText(text);
		printer.printText("  ");
	}

	static void printThirdColumn(PosPrinter printer, String text, int thirdColumnLength) {
		if (text.length() < thirdColumnLength) {
			text = StringUtils.leftPad(text, thirdColumnLength, ' ');
		}
		printer.printText(text);
		printer.printText("  ");
	}

	static void printFourthColumn(PosPrinter printer, String text, int fourthColumnLength) {
		if (text.length() < fourthColumnLength) {
			text = StringUtils.leftPad(text, fourthColumnLength, ' ');
		}
		printer.printText(text);
	}

	public static void printVoidInfo(Ticket ticket) throws Exception {
//		PosPrinter posPrinter = null;
//		try {
//			Restaurant restaurant = RestaurantDAO.getInstance().get(Integer.valueOf(1));
//
//			posPrinter = new PosPrinter(PrintConfig.getJavaPosKitchenPrinterName(), PrintConfig.getCashDrawerName());
//
//			posPrinter.beginLine(PosPrinter.SIZE_0);
//			posPrinter.printText("\u001b|cA\u001b|2C" + restaurant.getName());
//			posPrinter.endLine();
//
//			posPrinter.beginLine(PosPrinter.SIZE_0);
//			posPrinter.printText("\u001b|cA\u001DB\1============VOIDED CHECK============\u001DB\0");
//			posPrinter.endLine();
//
//			posPrinter.beginLine(PosPrinter.SIZE_0);
//			posPrinter.printText("Ticket #" + ticket.getId());
//			posPrinter.endLine();
//			posPrinter.beginLine(PosPrinter.SIZE_0);
//			posPrinter.printText(com.floreantpos.POSConstants.SRV_);
//			posPrinter.printText(String.valueOf(ticket.getOwner().getUserId() + "/" + ticket.getOwner()));
//			posPrinter.endLine();
//
//			posPrinter.beginLine(PosPrinter.SIZE_0);
//			posPrinter.printText(com.floreantpos.POSConstants.DATE + ": ");
//			posPrinter.printText(Application.formatDate(new Date()));
//			posPrinter.endLine();
//
//			posPrinter.printEmptyLine();
//
//			posPrinter.printCutPartial();
//		} finally {
//			if (posPrinter != null) {
//				posPrinter.finalize();
//			}
//		}
	}

	static void printDrawerPullLine(PosPrinter printer, String firstColumn, String secondColumn) {
		printer.beginLine(PosPrinter.SIZE_0);
		printFirstColumn(printer, firstColumn, 30);
		printFourthColumn(printer, secondColumn, 10);
		printer.endLine();
	}

	static void printDiscountLine(PosPrinter printer, String firstColumn, String secondColumn) {
		printer.beginLine(PosPrinter.SIZE_0);
		printer.printText("     ");
		printColumn(printer, firstColumn, 27);
		printRightAlignedColumn(printer, secondColumn, 10);
		printer.endLine();
	}

	public static void printDrawerPullReport(DrawerPullReport drawerPullReport, Terminal terminal) {
		
		try {
			HashMap parameters = new HashMap();
			Restaurant restaurant = RestaurantDAO.getInstance().get(Integer.valueOf(1));
			
			parameters.put("headerLine1", restaurant.getName());
			parameters.put("terminal", "Terminal # " + terminal.getId());
			parameters.put("user", "User: " + drawerPullReport.getAssignedUser().getFullName());
			parameters.put("date", new Date());
			parameters.put("totalVoid", drawerPullReport.getTotalVoid());
			
			JasperReport subReport = ReportUtil.getReport("drawer-pull-void-veport");
			
			parameters.put("subreportParameter", subReport);
			
			JasperReport mainReport = ReportUtil.getReport("drawer-pull-report");
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Arrays.asList(new DrawerPullReport[] {drawerPullReport}));
			JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, dataSource);
			jasperPrint.setProperty("printerName", Application.getPrinters().getReportPrinter());
			JasperPrintManager.printReport(jasperPrint, false);
			
			//JasperViewer.viewReport(jasperPrint, false);
		
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error print drawer pull report", e);
		}
	}

	public static void printServerTipsReport(TipsCashoutReport report) {
		
		try {
			HashMap parameters = new HashMap();
			parameters.put("server", report.getServer());
			parameters.put("fromDate", Application.formatDate(report.getFromDate()));
			parameters.put("toDate", Application.formatDate(report.getToDate()));
			parameters.put("reportDate", Application.formatDate(report.getReportTime()));
			parameters.put("transactionCount", report.getDatas() == null ? "0" : "" + report.getDatas().size());
			parameters.put("cashTips", NumberUtil.formatNumber(report.getCashTipsAmount()));
			parameters.put("chargedTips", NumberUtil.formatNumber(report.getChargedTipsAmount()));
			parameters.put("tipsDue", NumberUtil.formatNumber(report.getTipsDue()));
			
			Restaurant restaurant = RestaurantDAO.getInstance().get(Integer.valueOf(1));
			
			parameters.put("headerLine1", restaurant.getName());
			
			JasperReport mainReport = ReportUtil.getReport("ServerTipsReport");
			JRDataSource dataSource = new JRTableModelDataSource(new TipsCashoutReportTableModel(report.getDatas(), new String[] {"ticketId", "saleType", "ticketTotal", "tips"}));
			JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, dataSource);
			JasperViewer.viewReport(jasperPrint, false);
		
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error print tips report", e);
		}

	}
}
