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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.floreantpos.main.Application;
import com.floreantpos.model.DrawerPullReport;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.TipsCashoutReport;
import com.floreantpos.model.TipsCashoutReportTableModel;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.report.ReportUtil;
import com.floreantpos.util.NumberUtil;

public class PosPrintService {
	private static Log logger = LogFactory.getLog(PosPrintService.class);
	
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
