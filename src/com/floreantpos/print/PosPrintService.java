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
package com.floreantpos.print;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.floreantpos.Messages;
import com.floreantpos.PosException;
import com.floreantpos.PosLog;
import com.floreantpos.main.Application;
import com.floreantpos.model.DrawerPullReport;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.TipsCashoutReport;
import com.floreantpos.model.TipsCashoutReportTableModel;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.report.ReceiptPrintService;
import com.floreantpos.report.ReportUtil;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.PrintServiceUtil;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;

public class PosPrintService {
	private static Log logger = LogFactory.getLog(PosPrintService.class);

	public static void printDrawerPullReport(DrawerPullReport drawerPullReport, Terminal terminal) {

		try {
			String reportPrinter = Application.getPrinters().getReportPrinter();
			if (reportPrinter == null || reportPrinter.isEmpty()) {
				throw new PosException("No report printer is configured!");
			}
			HashMap parameters = new HashMap();
			Restaurant restaurant = RestaurantDAO.getInstance().get(Integer.valueOf(1));

			parameters.put("headerLine1", restaurant.getName()); //$NON-NLS-1$
			parameters.put("terminal", "Terminal # " + terminal.getId()); //$NON-NLS-1$ //$NON-NLS-2$
			if (drawerPullReport.getAssignedUser() != null)
				parameters.put("user", Messages.getString("PosPrintService.4") + drawerPullReport.getAssignedUser().getFullName()); //$NON-NLS-1$ //$NON-NLS-2$
			parameters.put("date", new Date()); //$NON-NLS-1$
			parameters.put("totalVoid", drawerPullReport.getTotalVoid()); //$NON-NLS-1$

			JasperReport subReportCurrencyBalance = ReportUtil.getReport("drawer-currency-balance"); //$NON-NLS-1$
			JasperReport subReport = ReportUtil.getReport("drawer-pull-void-veport"); //$NON-NLS-1$

			parameters.put("currencyBalanceReport", subReportCurrencyBalance); //$NON-NLS-1$
			parameters.put("subreportParameter", subReport); //$NON-NLS-1$

			JasperReport mainReport = ReportUtil.getReport("drawer-pull-report"); //$NON-NLS-1$
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Arrays.asList(new DrawerPullReport[] { drawerPullReport }));
			JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, dataSource);
			//TODO: handle exception
			jasperPrint.setProperty("printerName", Application.getPrinters().getReportPrinter()); //$NON-NLS-1$
			jasperPrint.setName("DrawerPullReport" + drawerPullReport.getId());

			JRPrintServiceExporter exporter = new JRPrintServiceExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE,
					PrintServiceUtil.getPrintServiceForPrinter(jasperPrint.getProperty("printerName")));
			exporter.exportReport();

			//JasperPrintManager.printReport(jasperPrint, false);
			//JasperViewer.viewReport(jasperPrint, false);

		} catch (PosException e) {
			throw e;
		} catch (Exception e) {
			PosLog.error(PosPrintService.class, e.getMessage());
			logger.error("error print drawer pull report", e); //$NON-NLS-1$
		}
	}

	public static void printServerTipsReport(TipsCashoutReport report) {

		try {
			String reportPrinter = Application.getPrinters().getReportPrinter();
			if (reportPrinter == null || reportPrinter.isEmpty()) {
				throw new PosException("No report printer is configured!");
			}
			HashMap parameters = new HashMap();
			parameters.put("server", report.getServer()); //$NON-NLS-1$
			parameters.put("fromDate", Application.formatDate(report.getFromDate())); //$NON-NLS-1$
			parameters.put("toDate", Application.formatDate(report.getToDate())); //$NON-NLS-1$
			parameters.put("reportDate", Application.formatDate(report.getReportTime())); //$NON-NLS-1$
			parameters.put("transactionCount", report.getDatas() == null ? "0" : "" + report.getDatas().size()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			parameters.put("cashTips", NumberUtil.formatNumber(report.getCashTipsAmount())); //$NON-NLS-1$
			parameters.put("chargedTips", NumberUtil.formatNumber(report.getChargedTipsAmount())); //$NON-NLS-1$
			parameters.put("tipsDue", NumberUtil.formatNumber(report.getTipsDue())); //$NON-NLS-1$

			Restaurant restaurant = RestaurantDAO.getInstance().get(Integer.valueOf(1));

			parameters.put("headerLine1", restaurant.getName()); //$NON-NLS-1$

			JasperReport mainReport = ReportUtil.getReport("ServerTipsReport"); //$NON-NLS-1$
			JRDataSource dataSource = new JRTableModelDataSource(
					new TipsCashoutReportTableModel(report.getDatas(), new String[] { "ticketId", "saleType", "ticketTotal", "tips" })); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, dataSource);
			jasperPrint.setProperty(ReceiptPrintService.PROP_PRINTER_NAME, Application.getPrinters().getReportPrinter());
			ReceiptPrintService.printQuitely(jasperPrint);
			//JasperViewer.viewReport(jasperPrint, false);

		} catch (PosException e) {
			throw e;
		} catch (Exception e) {
			PosLog.error(PosPrintService.class, e.getMessage());
			logger.error("error print tips report", e); //$NON-NLS-1$
		}
	}
}
