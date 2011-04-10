package com.floreantpos.ui.report;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JRViewer;

import org.jdesktop.swingx.JXDatePicker;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.report.SalesBalanceReport;
import com.floreantpos.report.services.ReportService;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class SalesBalanceReportView extends JPanel {
	private SimpleDateFormat fullDateFormatter = new SimpleDateFormat("yyyy MMM dd, hh:mm a");
	private SimpleDateFormat shortDateFormatter = new SimpleDateFormat("yyyy MMM dd");
	
	private JXDatePicker fromDatePicker = new JXDatePicker();
	private JXDatePicker toDatePicker = new JXDatePicker();
	private JButton btnGo = new JButton(com.floreantpos.POSConstants.GO);
	private JPanel reportContainer;
	
	public SalesBalanceReportView() {
		super(new BorderLayout());
		
		JPanel topPanel = new JPanel(new MigLayout());
		
		topPanel.add(new JLabel(com.floreantpos.POSConstants.FROM + ":"), "grow");
		topPanel.add(fromDatePicker,"wrap");
		topPanel.add(new JLabel(com.floreantpos.POSConstants.TO + ":"), "grow");
		topPanel.add(toDatePicker,"wrap");
		topPanel.add(btnGo, "skip 1, al right");
		add(topPanel, BorderLayout.NORTH);
		
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBorder(new EmptyBorder(0, 10,10,10));
		centerPanel.add(new JSeparator(), BorderLayout.NORTH);
		
		reportContainer = new JPanel(new BorderLayout());
		centerPanel.add(reportContainer);
		
		add(centerPanel);
		
		btnGo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					viewReport();
				} catch (Exception e1) {
					POSMessageDialog.showError(SalesBalanceReportView.this, POSConstants.ERROR_MESSAGE, e1);
				}
			}
			
		});
	}
	
	private void viewReport() throws Exception {
		Date fromDate = fromDatePicker.getDate();
		Date toDate = toDatePicker.getDate();
		
		if(fromDate.after(toDate)) {
			POSMessageDialog.showError(Application.getInstance().getBackOfficeWindow(), com.floreantpos.POSConstants.FROM_DATE_CANNOT_BE_GREATER_THAN_TO_DATE_);
			return;
		}
		
		fromDate = DateUtil.startOfDay(fromDate);
		toDate = DateUtil.endOfDay(toDate);
		
		ReportService reportService = new ReportService();
		SalesBalanceReport report = reportService.getSalesBalanceReport(fromDate, toDate);
		
		HashMap<String, String> map = new HashMap<String, String>();
		ReportUtil.populateRestaurantProperties(map);
		map.put("fromDate", shortDateFormatter.format(fromDate));
		map.put("toDate", shortDateFormatter.format(toDate));
		map.put("reportTime", fullDateFormatter.format(new Date()));
		
		map.put("grossTaxableSales", Application.formatNumber(report.getGrossTaxableSalesAmount()));
		map.put("grossNonTaxableSales", Application.formatNumber(report.getGrossNonTaxableSalesAmount()));
		map.put("discounts", Application.formatNumber(report.getDiscountAmount()));
		map.put("netSales", Application.formatNumber(report.getNetSalesAmount()));
		map.put("salesTaxes", Application.formatNumber(report.getSalesTaxAmount()));
		map.put("totalRevenues", Application.formatNumber(report.getTotalRevenueAmount()));
		map.put("giftCertSold", Application.formatNumber(report.getGiftCertSalesAmount()));
		map.put("payIns", Application.formatNumber(report.getPayInsAmount()));
		map.put("chargedTips", Application.formatNumber(report.getChargedTipsAmount()));
		map.put("grossReceipts", Application.formatNumber(report.getGrossReceiptsAmount()));
		map.put("cashReceipts", Application.formatNumber(report.getCashReceiptsAmount()));
		map.put("creditCardReceipts", Application.formatNumber(report.getCreditCardReceiptsAmount()));
		map.put("grossTipsPaid", Application.formatNumber(report.getGrossTipsPaidAmount()));
		map.put("arReceipts", Application.formatNumber(report.getArReceiptsAmount()));
		map.put("giftCertReturns", Application.formatNumber(report.getGiftCertReturnAmount()));
		map.put("giftCertChange", Application.formatNumber(report.getGiftCertChangeAmount()));
		map.put("cashBack", Application.formatNumber(report.getCashBackAmount()));
		map.put("receiptDiff", Application.formatNumber(report.getReceiptDiffAmount()));
		map.put("tipsDiscount", Application.formatNumber(report.getTipsDiscountAmount()));
		map.put("cashPayout", Application.formatNumber(report.getCashPayoutAmount()));
		map.put("cashAccountable", Application.formatNumber(report.getCashAccountableAmount()));
		map.put("drawerPulls", Application.formatNumber(report.getDrawerPullsAmount()));
		map.put("coCurrent", Application.formatNumber(report.getCoCurrentAmount()));
		map.put("coPrevious", Application.formatNumber(report.getCoPreviousAmount()));
		map.put("coOverShort", Application.formatNumber(report.getOverShortAmount()));
		map.put("days", String.valueOf((int) ((toDate.getTime() - fromDate.getTime()) * (1.15740741 * Math.pow(10, -8))) + 1));
		
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/com/floreantpos/ui/report/sales_summary_balance_report.jasper"));
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JREmptyDataSource());
		JRViewer viewer = new JRViewer(jasperPrint);
		reportContainer.removeAll();
		reportContainer.add(viewer);
		reportContainer.revalidate();
		
	}
}
