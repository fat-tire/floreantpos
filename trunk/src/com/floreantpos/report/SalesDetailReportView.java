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
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JRViewer;

import org.jdesktop.swingx.JXDatePicker;

import com.floreantpos.POSConstants;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.report.service.ReportService;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.util.UiUtil;

public class SalesDetailReportView extends JPanel {
	private SimpleDateFormat fullDateFormatter = new SimpleDateFormat("yyyy MMM dd, hh:mm a"); //$NON-NLS-1$
	private SimpleDateFormat shortDateFormatter = new SimpleDateFormat("yyyy MMM dd"); //$NON-NLS-1$
	
	private JXDatePicker fromDatePicker = UiUtil.getCurrentMonthStart();
	private JXDatePicker toDatePicker = UiUtil.getCurrentMonthEnd();
	private JButton btnGo = new JButton(com.floreantpos.POSConstants.GO);
	private JPanel reportContainer;
	
	public SalesDetailReportView() {
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
					POSMessageDialog.showError(SalesDetailReportView.this, POSConstants.ERROR_MESSAGE, e1);
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
		SalesDetailedReport report = reportService.getSalesDetailedReport(fromDate, toDate);
		
		JasperReport drawerPullReport = ReportUtil.getReport("sales_summary_balance_detailed__1"); //$NON-NLS-1$
		JasperReport creditCardReport = ReportUtil.getReport("sales_summary_balance_detailed_2"); //$NON-NLS-1$
		
		HashMap map = new HashMap();
		ReportUtil.populateRestaurantProperties(map);
		map.put("fromDate", shortDateFormatter.format(fromDate)); //$NON-NLS-1$
		map.put("toDate", shortDateFormatter.format(toDate)); //$NON-NLS-1$
		map.put("reportTime", fullDateFormatter.format(new Date())); //$NON-NLS-1$
		map.put("giftCertReturnCount", report.getGiftCertReturnCount()); //$NON-NLS-1$
		map.put("giftCertReturnAmount", report.getGiftCertReturnAmount()); //$NON-NLS-1$
		map.put("giftCertChangeCount", report.getGiftCertChangeCount()); //$NON-NLS-1$
		map.put("giftCertChangeAmount", report.getGiftCertChangeAmount()); //$NON-NLS-1$
		map.put("tipsCount", report.getTipsCount()); //$NON-NLS-1$
		map.put("tipsAmount", report.getChargedTips()); //$NON-NLS-1$
		map.put("tipsPaidAmount", report.getTipsPaid()); //$NON-NLS-1$
		map.put("drawerPullReport", drawerPullReport); //$NON-NLS-1$
		map.put("drawerPullDatasource", new JRTableModelDataSource(report.getDrawerPullDataTableModel())); //$NON-NLS-1$
		map.put("creditCardReport", creditCardReport); //$NON-NLS-1$
		map.put("creditCardReportDatasource", new JRTableModelDataSource(report.getCreditCardDataTableModel())); //$NON-NLS-1$
		
		JasperReport jasperReport = ReportUtil.getReport("sales_summary_balace_detail"); //$NON-NLS-1$
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JREmptyDataSource());
		JRViewer viewer = new JRViewer(jasperPrint);
		reportContainer.removeAll();
		reportContainer.add(viewer);
		reportContainer.revalidate();
		
	}
}
