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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JRViewer;

import org.jdesktop.swingx.JXDatePicker;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.model.CreditCardTransaction;
import com.floreantpos.model.dao.PosTransactionDAO;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.report.service.ReportService;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.util.UiUtil;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.PanelTester;

public class CreditCardReportView extends JPanel {
	private JXDatePicker fromDatePicker = UiUtil.getCurrentMonthStart();
	private JXDatePicker toDatePicker = UiUtil.getCurrentMonthEnd();
	private JButton btnGo = new JButton(com.floreantpos.POSConstants.GO);
	private JPanel reportContainer;
	
	public CreditCardReportView() {
		super(new BorderLayout());
		
		JPanel topPanel = new JPanel(new MigLayout());
		
		topPanel.add(new JLabel(com.floreantpos.POSConstants.FROM + ":"), "grow"); //$NON-NLS-1$ //$NON-NLS-2$
		topPanel.add(fromDatePicker); //$NON-NLS-1$
		topPanel.add(new JLabel(com.floreantpos.POSConstants.TO + ":"), "grow"); //$NON-NLS-1$ //$NON-NLS-2$
		topPanel.add(toDatePicker); //$NON-NLS-1$
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
					POSMessageDialog.showError(CreditCardReportView.this, POSConstants.ERROR_MESSAGE, e1);
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
		
		Date currentTime = new Date();
		
		List<CreditCardTransaction> transactions = (List<CreditCardTransaction>) PosTransactionDAO.getInstance().findTransactions(null, CreditCardTransaction.class, fromDate, toDate);
		
		int saleCount = 0;
		double totalSales = 0;
		double totalTips = 0;
		for (CreditCardTransaction transaction : transactions) {
			++saleCount;
			totalSales += transaction.getAmount();
			totalTips += transaction.getTipsAmount();
		}
		
		HashMap map = new HashMap();
		ReportUtil.populateRestaurantProperties(map);
		map.put("reportTitle", Messages.getString("CreditCardReportView.0")); //$NON-NLS-1$ //$NON-NLS-2$
		map.put("fromDate", ReportService.formatShortDate(fromDate)); //$NON-NLS-1$
		map.put("toDate", ReportService.formatShortDate(toDate)); //$NON-NLS-1$
		map.put("reportTime", ReportService.formatFullDate(currentTime)); //$NON-NLS-1$
		
		map.put("saleCount", String.valueOf(saleCount)); //$NON-NLS-1$
		map.put("totalSales", NumberUtil.formatNumber(totalSales - totalTips)); //$NON-NLS-1$
		map.put("totalTips", NumberUtil.formatNumber(totalTips)); //$NON-NLS-1$
		map.put("total", NumberUtil.formatNumber(totalSales)); //$NON-NLS-1$
		
		JasperReport jasperReport = ReportUtil.getReport("credit-card-report"); //$NON-NLS-1$
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRTableModelDataSource(new CardReportModel(transactions)));
		JRViewer viewer = new JRViewer(jasperPrint);
		reportContainer.removeAll();
		reportContainer.add(viewer);
		reportContainer.revalidate();
		
	}

	public static void main(String[] args) {
		PanelTester.width = 800;
		PanelTester.height = 500;
		PanelTester.test(new CreditCardReportView());
	}
}
