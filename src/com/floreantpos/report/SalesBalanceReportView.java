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
import net.sf.jasperreports.view.JRViewer;

import org.jdesktop.swingx.JXDatePicker;

import com.floreantpos.POSConstants;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.report.service.ReportService;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.util.UiUtil;
import com.floreantpos.util.NumberUtil;

public class SalesBalanceReportView extends JPanel {
	private SimpleDateFormat fullDateFormatter = new SimpleDateFormat("yyyy MMM dd, hh:mm a"); //$NON-NLS-1$
	private SimpleDateFormat shortDateFormatter = new SimpleDateFormat("yyyy MMM dd"); //$NON-NLS-1$
	
	private JXDatePicker fromDatePicker = UiUtil.getCurrentMonthStart();
	private JXDatePicker toDatePicker = UiUtil.getCurrentMonthEnd();
	private JButton btnGo = new JButton(com.floreantpos.POSConstants.GO);
	private JPanel reportContainer;
	
	public SalesBalanceReportView() {
		super(new BorderLayout());
		
		JPanel topPanel = new JPanel(new MigLayout());
		
		topPanel.add(new JLabel(com.floreantpos.POSConstants.FROM + ":"), "grow"); //$NON-NLS-1$ //$NON-NLS-2$
		topPanel.add(fromDatePicker,"wrap"); //$NON-NLS-1$
		topPanel.add(new JLabel(com.floreantpos.POSConstants.TO + ":"), "grow"); //$NON-NLS-1$ //$NON-NLS-2$
		topPanel.add(toDatePicker,"wrap"); //$NON-NLS-1$
		topPanel.add(btnGo, "skip 1, al right"); //$NON-NLS-1$
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
			POSMessageDialog.showError(com.floreantpos.util.POSUtil.getFocusedWindow(), com.floreantpos.POSConstants.FROM_DATE_CANNOT_BE_GREATER_THAN_TO_DATE_);
			return;
		}
		
		fromDate = DateUtil.startOfDay(fromDate);
		toDate = DateUtil.endOfDay(toDate);
		
		ReportService reportService = new ReportService();
		SalesBalanceReport report = reportService.getSalesBalanceReport(fromDate, toDate);
		
		HashMap<String, String> map = new HashMap<String, String>();
		ReportUtil.populateRestaurantProperties(map);
		map.put("fromDate", shortDateFormatter.format(fromDate)); //$NON-NLS-1$
		map.put("toDate", shortDateFormatter.format(toDate)); //$NON-NLS-1$
		map.put("reportTime", fullDateFormatter.format(new Date())); //$NON-NLS-1$
		
		map.put("grossTaxableSales", NumberUtil.formatNumber(report.getGrossTaxableSalesAmount())); //$NON-NLS-1$
		map.put("grossNonTaxableSales", NumberUtil.formatNumber(report.getGrossNonTaxableSalesAmount())); //$NON-NLS-1$
		map.put("discounts", NumberUtil.formatNumber(report.getDiscountAmount())); //$NON-NLS-1$
		map.put("netSales", NumberUtil.formatNumber(report.getNetSalesAmount())); //$NON-NLS-1$
		map.put("salesTaxes", NumberUtil.formatNumber(report.getSalesTaxAmount())); //$NON-NLS-1$
		map.put("totalRevenues", NumberUtil.formatNumber(report.getTotalRevenueAmount())); //$NON-NLS-1$
		map.put("giftCertSold", NumberUtil.formatNumber(report.getGiftCertSalesAmount())); //$NON-NLS-1$
		map.put("payIns", NumberUtil.formatNumber(report.getPayInsAmount())); //$NON-NLS-1$
		map.put("chargedTips", NumberUtil.formatNumber(report.getChargedTipsAmount())); //$NON-NLS-1$
		map.put("grossReceipts", NumberUtil.formatNumber(report.getGrossReceiptsAmount())); //$NON-NLS-1$
		map.put("cashReceipts", NumberUtil.formatNumber(report.getCashReceiptsAmount())); //$NON-NLS-1$
		map.put("creditCardReceipts", NumberUtil.formatNumber(report.getCreditCardReceiptsAmount())); //$NON-NLS-1$
		map.put("grossTipsPaid", NumberUtil.formatNumber(report.getGrossTipsPaidAmount())); //$NON-NLS-1$
		map.put("arReceipts", NumberUtil.formatNumber(report.getArReceiptsAmount())); //$NON-NLS-1$
		map.put("giftCertReturns", NumberUtil.formatNumber(report.getGiftCertReturnAmount())); //$NON-NLS-1$
		map.put("giftCertChange", NumberUtil.formatNumber(report.getGiftCertChangeAmount())); //$NON-NLS-1$
		map.put("cashBack", NumberUtil.formatNumber(report.getCashBackAmount())); //$NON-NLS-1$
		map.put("receiptDiff", NumberUtil.formatNumber(report.getReceiptDiffAmount())); //$NON-NLS-1$
		map.put("tipsDiscount", NumberUtil.formatNumber(report.getTipsDiscountAmount())); //$NON-NLS-1$
		map.put("cashPayout", NumberUtil.formatNumber(report.getCashPayoutAmount())); //$NON-NLS-1$
		map.put("cashAccountable", NumberUtil.formatNumber(report.getCashAccountableAmount())); //$NON-NLS-1$
		map.put("drawerPulls", NumberUtil.formatNumber(report.getDrawerPullsAmount())); //$NON-NLS-1$
		map.put("coCurrent", NumberUtil.formatNumber(report.getCoCurrentAmount())); //$NON-NLS-1$
		map.put("coPrevious", NumberUtil.formatNumber(report.getCoPreviousAmount())); //$NON-NLS-1$
		map.put("coOverShort", NumberUtil.formatNumber(report.getOverShortAmount())); //$NON-NLS-1$
		map.put("days", String.valueOf((int) ((toDate.getTime() - fromDate.getTime()) * (1.15740741 * Math.pow(10, -8))) + 1)); //$NON-NLS-1$
		
		JasperReport jasperReport = ReportUtil.getReport("sales_summary_balance_report"); //$NON-NLS-1$
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JREmptyDataSource());
		JRViewer viewer = new JRViewer(jasperPrint);
		reportContainer.removeAll();
		reportContainer.add(viewer);
		reportContainer.revalidate();
		
	}
}
