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
import java.util.Calendar;
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JRViewer;

import org.jdesktop.swingx.JXDatePicker;

import com.floreantpos.POSConstants;
import com.floreantpos.PosLog;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.AttendenceHistoryDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.util.UiUtil;

/**
 * Created by IntelliJ IDEA.
 * User: mshahriar
 * Date: Feb 28, 2007
 * Time: 12:25:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class AttendanceReportView extends TransparentPanel {
	SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd,yyy"); //$NON-NLS-1$
	private JButton btnGo;
	private JXDatePicker fromDatePicker;
	private JXDatePicker toDatePicker;
	private JPanel reportPanel;
	private JComboBox cbUserType;

	public AttendanceReportView() {

		setLayout(new BorderLayout());
		createUI();
	}

	private void createUI() {

		fromDatePicker = UiUtil.getCurrentMonthStart();
		toDatePicker = UiUtil.getDeafultDate();
		toDatePicker.setDate(new Date());
		
		btnGo = new JButton();
		btnGo.setText(com.floreantpos.POSConstants.GO);
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewReport();
			}
		});

		cbUserType = new JComboBox();

		UserDAO dao = new UserDAO();
		List<User> userTypes = dao.findAll();

		Vector list = new Vector();
		list.add(POSConstants.ALL);
		list.addAll(userTypes);

		cbUserType.setModel(new DefaultComboBoxModel(list));

		setLayout(new BorderLayout());

		JPanel topPanel = new JPanel(new MigLayout());

		topPanel.add(new JLabel(com.floreantpos.POSConstants.START_DATE + ":")); //$NON-NLS-1$ 
		topPanel.add(fromDatePicker);
		topPanel.add(new JLabel(com.floreantpos.POSConstants.END_DATE + ":")); //$NON-NLS-1$ 
		topPanel.add(toDatePicker);
		topPanel.add(new JLabel(POSConstants.USER + ":")); //$NON-NLS-1$
		topPanel.add(cbUserType);
		topPanel.add(btnGo, "width 60!"); //$NON-NLS-1$
		add(topPanel, BorderLayout.NORTH);

		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
		centerPanel.add(new JSeparator(), BorderLayout.NORTH);

		reportPanel = new JPanel(new BorderLayout());
		centerPanel.add(reportPanel);

		add(centerPanel);
	}

	private void viewReport() {
		Date fromDate = fromDatePicker.getDate();
		Date toDate = toDatePicker.getDate();

		if (fromDate.after(toDate)) {
			POSMessageDialog.showError(com.floreantpos.util.POSUtil.getFocusedWindow(), com.floreantpos.POSConstants.FROM_DATE_CANNOT_BE_GREATER_THAN_TO_DATE_);
			return;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.clear();

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(fromDate);

		calendar.set(Calendar.YEAR, calendar2.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, calendar2.get(Calendar.MONTH));
		calendar.set(Calendar.DATE, calendar2.get(Calendar.DATE));
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		fromDate = calendar.getTime();

		calendar.clear();
		calendar2.setTime(toDate);
		calendar.set(Calendar.YEAR, calendar2.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, calendar2.get(Calendar.MONTH));
		calendar.set(Calendar.DATE, calendar2.get(Calendar.DATE));
		calendar.set(Calendar.HOUR, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		toDate = calendar.getTime();

		User user = null;
		if (!cbUserType.getSelectedItem().equals(POSConstants.ALL)) {
			user = (User) cbUserType.getSelectedItem();
		}

		AttendenceHistoryDAO dao = new AttendenceHistoryDAO();
		List<AttendanceReportData> attendanceList = dao.findAttendance(fromDate, toDate, user);

		try {
			
			JasperReport report = ReportUtil.getReport("EmployeeAttendanceReport"); //$NON-NLS-1$

			HashMap properties = new HashMap();
			ReportUtil.populateRestaurantProperties(properties);
			properties.put("fromDate", dateFormat.format(fromDate)); //$NON-NLS-1$
			properties.put("toDate", dateFormat.format(toDate)); //$NON-NLS-1$
			properties.put("reportDate", dateFormat.format(new Date())); //$NON-NLS-1$

			AttendanceReportModel reportModel = new AttendanceReportModel();
			reportModel.setRows(attendanceList);

			JasperPrint print = JasperFillManager.fillReport(report, properties, new JRTableModelDataSource(reportModel));

			JRViewer viewer = new JRViewer(print);
			reportPanel.removeAll();
			reportPanel.add(viewer);
			reportPanel.revalidate();
		} catch (JRException e) {
			PosLog.error(getClass(), e);
		}
	}
}
