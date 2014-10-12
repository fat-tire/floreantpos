package com.floreantpos.report;

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
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.report.service.ReportService;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.util.UiUtil;
import com.floreantpos.util.NumberUtil;

public class SalesBalanceReportView extends JPanel {
	private SimpleDateFormat fullDateFormatter = new SimpleDateFormat("yyyy MMM dd, hh:mm a");
	private SimpleDateFormat shortDateFormatter = new SimpleDateFormat("yyyy MMM dd");
	
	private JXDatePicker fromDatePicker = UiUtil.getCurrentMonthStart();
	private JXDatePicker toDatePicker = UiUtil.getCurrentMonthEnd();
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
			POSMessageDialog.showError(BackOfficeWindow.getInstance(), com.floreantpos.POSConstants.FROM_DATE_CANNOT_BE_GREATER_THAN_TO_DATE_);
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
		
		map.put("grossTaxableSales", NumberUtil.formatNumber(report.getGrossTaxableSalesAmount()));
		map.put("grossNonTaxableSales", NumberUtil.formatNumber(report.getGrossNonTaxableSalesAmount()));
		map.put("discounts", NumberUtil.formatNumber(report.getDiscountAmount()));
		map.put("netSales", NumberUtil.formatNumber(report.getNetSalesAmount()));
		map.put("salesTaxes", NumberUtil.formatNumber(report.getSalesTaxAmount()));
		map.put("totalRevenues", NumberUtil.formatNumber(report.getTotalRevenueAmount()));
		map.put("giftCertSold", NumberUtil.formatNumber(report.getGiftCertSalesAmount()));
		map.put("payIns", NumberUtil.formatNumber(report.getPayInsAmount()));
		map.put("chargedTips", NumberUtil.formatNumber(report.getChargedTipsAmount()));
		map.put("grossReceipts", NumberUtil.formatNumber(report.getGrossReceiptsAmount()));
		map.put("cashReceipts", NumberUtil.formatNumber(report.getCashReceiptsAmount()));
		map.put("creditCardReceipts", NumberUtil.formatNumber(report.getCreditCardReceiptsAmount()));
		map.put("grossTipsPaid", NumberUtil.formatNumber(report.getGrossTipsPaidAmount()));
		map.put("arReceipts", NumberUtil.formatNumber(report.getArReceiptsAmount()));
		map.put("giftCertReturns", NumberUtil.formatNumber(report.getGiftCertReturnAmount()));
		map.put("giftCertChange", NumberUtil.formatNumber(report.getGiftCertChangeAmount()));
		map.put("cashBack", NumberUtil.formatNumber(report.getCashBackAmount()));
		map.put("receiptDiff", NumberUtil.formatNumber(report.getReceiptDiffAmount()));
		map.put("tipsDiscount", NumberUtil.formatNumber(report.getTipsDiscountAmount()));
		map.put("cashPayout", NumberUtil.formatNumber(report.getCashPayoutAmount()));
		map.put("cashAccountable", NumberUtil.formatNumber(report.getCashAccountableAmount()));
		map.put("drawerPulls", NumberUtil.formatNumber(report.getDrawerPullsAmount()));
		map.put("coCurrent", NumberUtil.formatNumber(report.getCoCurrentAmount()));
		map.put("coPrevious", NumberUtil.formatNumber(report.getCoPreviousAmount()));
		map.put("coOverShort", NumberUtil.formatNumber(report.getOverShortAmount()));
		map.put("days", String.valueOf((int) ((toDate.getTime() - fromDate.getTime()) * (1.15740741 * Math.pow(10, -8))) + 1));
		
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/com/floreantpos/report/template/sales_summary_balance_report.jasper"));
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JREmptyDataSource());
		JRViewer viewer = new JRViewer(jasperPrint);
		reportContainer.removeAll();
		reportContainer.add(viewer);
		reportContainer.revalidate();
		
	}
}
