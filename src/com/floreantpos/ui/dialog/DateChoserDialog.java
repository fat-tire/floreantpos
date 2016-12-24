package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.PosLog;
import com.floreantpos.main.Application;
import com.floreantpos.model.AttendenceHistory;
import com.floreantpos.model.Shift;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.swing.ComboBoxModel;
import com.floreantpos.swing.IntegerTextField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.util.UiUtil;
import com.floreantpos.util.POSUtil;
import com.floreantpos.util.ShiftUtil;

public class DateChoserDialog extends POSDialog {
	private org.jdesktop.swingx.JXDatePicker tbStartDate;
	private org.jdesktop.swingx.JXDatePicker tbEndDate;
	private IntegerTextField tfStartHour;
	private IntegerTextField tfStartMin;
	private JRadioButton rbStartAm;
	private JRadioButton rbStartPm;
	private IntegerTextField tfEndHour;
	private IntegerTextField tfEndMin;
	private JRadioButton rbEndAm;
	private JRadioButton rbEndPm;
	private ButtonGroup btnGroupStartAmPm;
	private ButtonGroup btnGroupEndAmPm;
	private PosButton btnOk;
	private PosButton btnCancel;
	private AttendenceHistory attendenceHistory;
	private JComboBox cbEmployees;

	private JCheckBox chkClockOut;

	public DateChoserDialog(String title) {
		super(POSUtil.getBackOfficeWindow(), title);
		attendenceHistory = new AttendenceHistory();
		initUi();
	}

	public DateChoserDialog(AttendenceHistory history, String title) {
		super(POSUtil.getBackOfficeWindow(), title);
		this.attendenceHistory = history;
		initUi();
	}

	private void initUi() {
		setIconImage(Application.getApplicationIcon().getImage());
		setResizable(false);
		JPanel mainPanel = new JPanel(new BorderLayout());

		mainPanel.setBackground(Color.red);

		List<User> employees = UserDAO.getInstance().findAll();
		cbEmployees = new JComboBox<User>(new ComboBoxModel(employees));

		JPanel topPanel = new JPanel(new MigLayout());
		topPanel.add(new JLabel("Select employee:"));
		topPanel.add(cbEmployees);
		mainPanel.add(topPanel, BorderLayout.NORTH);

		JPanel panel = new JPanel(new MigLayout("wrap 2", " [][][][][][][][][]", "[][]"));
		panel.setBorder(new TitledBorder("-"));

		btnGroupStartAmPm = new ButtonGroup();
		rbStartAm = new JRadioButton("AM"); //$NON-NLS-1$
		rbStartPm = new JRadioButton("PM"); //$NON-NLS-1$
		btnGroupStartAmPm.add(rbStartAm);
		btnGroupStartAmPm.add(rbStartPm);
		rbStartPm.setSelected(true);

		btnGroupEndAmPm = new ButtonGroup();
		rbEndAm = new JRadioButton("AM"); //$NON-NLS-1$
		rbEndPm = new JRadioButton("PM"); //$NON-NLS-1$
		btnGroupEndAmPm.add(rbEndAm);
		btnGroupEndAmPm.add(rbEndPm);
		rbEndPm.setSelected(true);

		tbStartDate = UiUtil.getCurrentMonthStart();

		tbEndDate = UiUtil.getCurrentMonthEnd();

		Vector<Integer> hours;

		hours = new Vector<Integer>();
		for (int i = 1; i <= 12; i++) {
			hours.add(Integer.valueOf(i));
		}

		DefaultComboBoxModel stMinModel = new DefaultComboBoxModel();
		stMinModel.addElement(0);
		stMinModel.addElement(15);
		stMinModel.addElement(30);
		stMinModel.addElement(45);

		DefaultComboBoxModel etMinModel = new DefaultComboBoxModel();
		etMinModel.addElement(0);
		etMinModel.addElement(15);
		etMinModel.addElement(30);
		etMinModel.addElement(45);

		tfStartHour = new IntegerTextField();
		tfStartMin = new IntegerTextField();
		tfEndHour = new IntegerTextField();
		tfEndMin = new IntegerTextField();

		tfStartHour.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Integer selectedItem = (Integer) tfStartHour.getInteger();
				if (selectedItem == 12) {
					selectedItem = 1;
				}
				else {
					selectedItem = selectedItem + 1;
				}
				tfEndHour.setText(String.valueOf(selectedItem));
			}
		});

		chkClockOut = new JCheckBox("Clock Out: ");
		chkClockOut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				enabledItemsForClockOut();
			}
		});

		JLabel lblClockIn = new JLabel("Clock In: ");
		panel.add(lblClockIn, "cell 0 0,right"); //$NON-NLS-1$
		panel.add(new JLabel("Date"), "cell 1 0"); //$NON-NLS-1$
		panel.add(tbStartDate, "cell 2 0");
		panel.add(new JLabel("Hour"), "cell 3 0"); //$NON-NLS-1$
		panel.add(tfStartHour, "w 40!,cell 4 0");
		panel.add(new JLabel("Min"), "cell 5 0");
		panel.add(tfStartMin, "w 40!,cell 6 0");
		panel.add(rbStartAm, "cell 7 0");
		panel.add(rbStartPm, "cell 8 0");

		panel.add(chkClockOut, "cell 0 1"); //$NON-NLS-1$
		panel.add(new JLabel("Date"), "cell 1 1"); //$NON-NLS-1$
		panel.add(tbEndDate, "cell 2 1");
		panel.add(new JLabel("Hour"), "cell 3 1"); //$NON-NLS-1$
		panel.add(tfEndHour, "w 40!,cell 4 1");
		panel.add(new JLabel("Min"), "cell 5 1");
		panel.add(tfEndMin, "w 40!,cell 6 1");
		panel.add(rbEndAm, "cell 7 1");
		panel.add(rbEndPm, "cell 8 1");

		JPanel footerPanel = new JPanel(new MigLayout("al center center", "sg", ""));
		btnOk = new PosButton("OK");
		btnCancel = new PosButton("CANCEL");
		btnCancel.setPreferredSize(new Dimension(100, 0));

		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();

			}
		});

		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (updateModel()) {
					setCanceled(false);
					dispose();
				}
			}
		});

		footerPanel.add(btnOk, "grow");
		footerPanel.add(btnCancel, "grow");

		mainPanel.add(panel, BorderLayout.CENTER);
		mainPanel.add(footerPanel, BorderLayout.SOUTH);
		getContentPane().add(mainPanel, BorderLayout.CENTER);

		updateView();
		enabledItemsForClockOut();
	}

	private void enabledItemsForClockOut() {
		boolean selected = chkClockOut.isSelected();
		tbEndDate.setEnabled(selected);
		tfEndHour.setEnabled(selected);
		tfEndMin.setEnabled(selected);
	}

	private void updateView() {

		Calendar startCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();

		if (attendenceHistory.getId() == null) {
			startCalendar.setTime(new Date());
			endCalendar.setTime(new Date());
		}
		else {
			if (attendenceHistory.getClockInTime() != null) {
				startCalendar.setTime(attendenceHistory.getClockInTime());

				if (attendenceHistory.getClockOutTime() != null) {
					endCalendar.setTime(attendenceHistory.getClockOutTime());
				}
			}
			cbEmployees.setSelectedItem(attendenceHistory.getUser());
			chkClockOut.setSelected(attendenceHistory.isClockedOut());
		}

		tbStartDate.setDate(startCalendar.getTime());

		Integer hour = (Integer) startCalendar.get(Calendar.HOUR);
		if (hour.equals(0)) {
			hour = 12;

		}
		tfStartHour.setText(String.valueOf(hour));
		tfStartMin.setText(String.valueOf(startCalendar.get(Calendar.MINUTE)));

		if (startCalendar.get(Calendar.AM_PM) == 0) {
			rbStartAm.setSelected(true);
		}
		else {
			rbStartPm.setSelected(true);
		}

		tbEndDate.setDate(endCalendar.getTime());

		Integer endHour = (Integer) endCalendar.get(Calendar.HOUR);
		if (endHour.equals(0)) {
			endHour = 12;

		}
		tfEndHour.setText(String.valueOf(endHour));
		tfEndMin.setText(String.valueOf(endCalendar.get(Calendar.MINUTE)));

		if (endCalendar.get(Calendar.AM_PM) == 0) {
			rbEndAm.setSelected(true);
		}
		else {
			rbEndPm.setSelected(true);
		}

	}

	private boolean updateModel() {

		Calendar clockInTime = getStartDate();
		Calendar clockOutTime = getEndDate();

		PosLog.info(DateChoserDialog.class, "" + clockInTime.getTime().getTime());
		PosLog.info(DateChoserDialog.class, "" + clockOutTime.getTime().getTime());

		if (clockInTime.getTime().getTime() > clockOutTime.getTime().getTime()) {
			POSMessageDialog.showMessage(POSUtil.getBackOfficeWindow(), "Clock in can not be greater than clock out");
			return false;
		}

		attendenceHistory.setClockInTime(clockInTime.getTime());
		attendenceHistory.setClockInHour(Short.valueOf((short) clockInTime.get(Calendar.HOUR_OF_DAY)));

		if (!chkClockOut.isSelected()) {
			attendenceHistory.setClockOutTime(null);
			attendenceHistory.setClockOutHour(null);
		}
		else {
			attendenceHistory.setClockOutTime(clockOutTime.getTime());
			attendenceHistory.setClockOutHour(Short.valueOf((short) clockOutTime.get(Calendar.HOUR_OF_DAY)));
		}
		User employee = (User) cbEmployees.getSelectedItem();
		Shift currentShift = ShiftUtil.getCurrentShift();

		attendenceHistory.setClockedOut(chkClockOut.isSelected());
		attendenceHistory.setUser(employee);
		attendenceHistory.setTerminal(Application.getInstance().getTerminal());
		attendenceHistory.setShift(currentShift);

		return true;
	}

	private Calendar getStartDate() {
		if (tbStartDate.getDate() == null) {
			return null;
		}

		Calendar clStartDate = Calendar.getInstance();
		clStartDate.setTime(tbStartDate.getDate());

		Integer hour = (Integer) tfStartHour.getInteger();
		if (hour == 12) {
			hour = 0;
		}

		clStartDate.set(Calendar.HOUR, hour);
		clStartDate.set(Calendar.MINUTE, (Integer) tfStartMin.getInteger());

		if (rbStartAm.isSelected()) {
			clStartDate.set(Calendar.AM_PM, Calendar.AM);
		}
		else if (rbStartPm.isSelected()) {
			clStartDate.set(Calendar.AM_PM, Calendar.PM);
		}

		return clStartDate;
	}

	private Calendar getEndDate() {
		if (tbEndDate.getDate() == null) {
			return null;
		}

		Calendar clEndDate = Calendar.getInstance();
		clEndDate.setTime(tbEndDate.getDate());

		Integer hour = (Integer) tfEndHour.getInteger();
		if (hour == 12) {
			hour = 0;
		}

		clEndDate.set(Calendar.HOUR, hour);
		clEndDate.set(Calendar.MINUTE, (Integer) tfEndMin.getInteger());

		if (rbEndAm.isSelected()) {
			clEndDate.set(Calendar.AM_PM, Calendar.AM);
		}
		else if (rbEndPm.isSelected()) {
			clEndDate.set(Calendar.AM_PM, Calendar.PM);
		}

		return clEndDate;
	}

	public AttendenceHistory getAttendenceHistory() {
		return attendenceHistory;
	}
}
