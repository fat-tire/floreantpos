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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXDatePicker;

import com.floreantpos.POSConstants;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.report.service.ReportService;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.util.UiUtil;
import com.floreantpos.util.NumberUtil;

public class SalesBalanceReportView extends JPanel {
	private SimpleDateFormat fullDateFormatter = new SimpleDateFormat("dd MMM yyyy, hh:mm a"); //$NON-NLS-1$
	private SimpleDateFormat shortDateFormatter = new SimpleDateFormat("dd MMM yyyy"); //$NON-NLS-1$

	private JXDatePicker fromDatePicker = UiUtil.getCurrentMonthStart();
	private JXDatePicker toDatePicker = UiUtil.getCurrentMonthEnd();
	private JComboBox cbUserType;
	private JButton btnToday;
	private JButton btnGo = new JButton(com.floreantpos.POSConstants.GO);
	private JPanel reportContainer;

	public SalesBalanceReportView() {
		super(new BorderLayout());

		JPanel topPanel = new JPanel(new MigLayout());

		btnToday = new JButton(POSConstants.TODAYS_REPORT.toUpperCase());
		btnToday.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Date today = new Date();
				try {
					viewReport(today, today);
				} catch (Exception ex) {
					Logger.getLogger(SalesBalanceReportView.class).debug(ex);
				}
			}
		});
		cbUserType = new JComboBox();

		UserDAO dao = new UserDAO();
		List<User> userTypes = dao.findAll();

		Vector list = new Vector();
		list.add(POSConstants.ALL);
		list.addAll(userTypes);

		cbUserType.setModel(new DefaultComboBoxModel(list));

		topPanel.add(new JLabel(com.floreantpos.POSConstants.FROM + ":")); //$NON-NLS-1$ 
		topPanel.add(fromDatePicker);
		topPanel.add(new JLabel(com.floreantpos.POSConstants.TO + ":")); //$NON-NLS-1$ 
		topPanel.add(toDatePicker);
		topPanel.add(new JLabel(POSConstants.USER + ":")); //$NON-NLS-1$
		topPanel.add(cbUserType);
		topPanel.add(btnGo, "width 60!"); //$NON-NLS-1$
		topPanel.add(btnToday);
		add(topPanel, BorderLayout.NORTH);

		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
		centerPanel.add(new JSeparator(), BorderLayout.NORTH);

		reportContainer = new JPanel(new BorderLayout());
		centerPanel.add(reportContainer);

		add(centerPanel);

		btnGo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					Date fromDate = fromDatePicker.getDate();
					Date toDate = toDatePicker.getDate();
					viewReport(fromDate, toDate);
				} catch (Exception e1) {
					POSMessageDialog.showError(SalesBalanceReportView.this, POSConstants.ERROR_MESSAGE, e1);
				}
			}

		});
	}

	private void viewReport(Date fromDate, Date toDate) throws Exception {

		if (fromDate.after(toDate)) {
			POSMessageDialog.showError(com.floreantpos.util.POSUtil.getFocusedWindow(), com.floreantpos.POSConstants.FROM_DATE_CANNOT_BE_GREATER_THAN_TO_DATE_);
			return;
		}

		fromDate = DateUtil.startOfDay(fromDate);
		toDate = DateUtil.endOfDay(toDate);

		User user = null;
		if (!cbUserType.getSelectedItem().equals(POSConstants.ALL)) {
			user = (User) cbUserType.getSelectedItem();
		}

		ReportService reportService = new ReportService();
		SalesBalanceReport report = reportService.getSalesBalanceReport(fromDate, toDate, user);

		HashMap map = new HashMap();
		ReportUtil.populateRestaurantProperties(map);
		map.put("fromDate", shortDateFormatter.format(fromDate)); //$NON-NLS-1$
		map.put("toDate", shortDateFormatter.format(toDate)); //$NON-NLS-1$
		map.put("reportTime", fullDateFormatter.format(new Date())); //$NON-NLS-1$
		map.put("userName", user == null ? com.floreantpos.POSConstants.ALL : user.getFullName()); //$NON-NLS-1$

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

		map.put("visaCreditCardSum", NumberUtil.formatNumber(report.getVisaCreditCardAmount())); //$NON-NLS-1$
		map.put("mastercardSum", NumberUtil.formatNumber(report.getMasterCardAmount())); //$NON-NLS-1$
		map.put("amexSum", NumberUtil.formatNumber(report.getAmexAmount())); //$NON-NLS-1$
		map.put("discoverySum", NumberUtil.formatNumber(report.getDiscoveryAmount())); //$NON-NLS-1$
		
//		map.put("totalReceipts", NumberUtil.formatNumber(report.getCreditCardReceiptsAmount()));
//		map.put("totalTips", NumberUtil.formatNumber(report.getGrossTipsPaidAmount()));

		JasperReport jasperReport = ReportUtil.getReport("sales_summary_balance_report"); //$NON-NLS-1$
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JREmptyDataSource());
		JRViewer viewer = new JRViewer(jasperPrint);
		reportContainer.removeAll();
		reportContainer.add(viewer);
		reportContainer.revalidate();

	}
}
