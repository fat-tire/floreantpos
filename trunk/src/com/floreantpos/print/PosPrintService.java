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

import com.floreantpos.Messages;
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
			
			parameters.put("headerLine1", restaurant.getName()); //$NON-NLS-1$
			parameters.put("terminal", "Terminal # " + terminal.getId()); //$NON-NLS-1$ //$NON-NLS-2$
			parameters.put("user", Messages.getString("PosPrintService.4") + drawerPullReport.getAssignedUser().getFullName()); //$NON-NLS-1$ //$NON-NLS-2$
			parameters.put("date", new Date()); //$NON-NLS-1$
			parameters.put("totalVoid", drawerPullReport.getTotalVoid()); //$NON-NLS-1$
			
			JasperReport subReport = ReportUtil.getReport("drawer-pull-void-veport"); //$NON-NLS-1$
			
			parameters.put("subreportParameter", subReport); //$NON-NLS-1$
			
			JasperReport mainReport = ReportUtil.getReport("drawer-pull-report"); //$NON-NLS-1$
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Arrays.asList(new DrawerPullReport[] {drawerPullReport}));
			JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, dataSource);
			jasperPrint.setProperty("printerName", Application.getPrinters().getReportPrinter()); //$NON-NLS-1$
			JasperPrintManager.printReport(jasperPrint, false);
			
			//JasperViewer.viewReport(jasperPrint, false);
		
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error print drawer pull report", e); //$NON-NLS-1$
		}
	}

	public static void printServerTipsReport(TipsCashoutReport report) {
		
		try {
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
			JRDataSource dataSource = new JRTableModelDataSource(new TipsCashoutReportTableModel(report.getDatas(), new String[] {"ticketId", "saleType", "ticketTotal", "tips"})); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, dataSource);
			JasperViewer.viewReport(jasperPrint, false);
		
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error print tips report", e); //$NON-NLS-1$
		}

	}
}
