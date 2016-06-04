package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.floreantpos.model.AttendenceHistory;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.util.UiUtil;

import net.miginfocom.swing.MigLayout;

public class DateChoserDialog extends POSDialog {
	private org.jdesktop.swingx.JXDatePicker tbStartDate;
	private org.jdesktop.swingx.JXDatePicker tbEndDate;
	private JComboBox cStartHour;
	private JComboBox cStartMin;
	private JRadioButton cbStartAm;
	private JRadioButton cbStartPm;
	private JComboBox cEndHour;
	private JComboBox cEndMin;
	private JRadioButton cEndAm;
	private JRadioButton cbEndPm;
	private ButtonGroup btnGroupStartAmPm;
	private ButtonGroup btnGroupEndAmPm;
	private PosButton btnOk;
	private PosButton btnCancel;
	private AttendenceHistory attendenceHistory;
	
	private Calendar clockInCalendar;
	
	private Calendar clockOutCalendar;

	public DateChoserDialog(AttendenceHistory history, String title) {
		super(null, title);
		this.attendenceHistory = history;
		initUi();

	}

	private void initUi() {
		setResizable(false);
		JPanel mainPanel = new JPanel(new BorderLayout());

		mainPanel.setBackground(Color.red);

		JPanel panel = new JPanel(new MigLayout("wrap 2", " [][][][][][][][][]", "[][]"));
		//panel.setPreferredSize(new Dimension(0,100));

		btnGroupStartAmPm = new ButtonGroup();
		cbStartAm = new JRadioButton("AM"); //$NON-NLS-1$
		cbStartPm = new JRadioButton("PM"); //$NON-NLS-1$
		btnGroupStartAmPm.add(cbStartAm);
		btnGroupStartAmPm.add(cbStartPm);
		cbStartPm.setSelected(true);

		btnGroupEndAmPm = new ButtonGroup();
		cEndAm = new JRadioButton("AM"); //$NON-NLS-1$
		cbEndPm = new JRadioButton("PM"); //$NON-NLS-1$
		btnGroupEndAmPm.add(cEndAm);
		btnGroupEndAmPm.add(cbEndPm);
		cbEndPm.setSelected(true);

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

		cStartHour = new JComboBox(new DefaultComboBoxModel(hours));
		cStartMin = new JComboBox(stMinModel);
		cEndHour = new JComboBox(new DefaultComboBoxModel(hours));
		cEndHour.setSelectedIndex(1);
		cEndMin = new JComboBox(etMinModel);

		cStartHour.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Integer selectedItem = (Integer) cStartHour.getSelectedItem();
				if (selectedItem == 12) {
					selectedItem = 1;
				}
				else {
					selectedItem = selectedItem + 1;
				}
				cEndHour.setSelectedItem(selectedItem);
			}
		});

		panel.add(new JLabel("Clock In: "), "cell 0 0"); //$NON-NLS-1$
		panel.add(new JLabel("Date"), "cell 1 0"); //$NON-NLS-1$
		panel.add(tbStartDate, "cell 2 0");
		panel.add(new JLabel("Hour"), "cell 3 0"); //$NON-NLS-1$
		panel.add(cStartHour, "cell 4 0");
		panel.add(new JLabel("Min"), "cell 5 0");
		panel.add(cStartMin, "cell 6 0");
		panel.add(cbStartAm, "cell 7 0");
		panel.add(cbStartPm, "cell 8 0");

		panel.add(new JLabel("Clock Out: "), "cell 0 1"); //$NON-NLS-1$
		panel.add(new JLabel("Date"), "cell 1 1"); //$NON-NLS-1$
		panel.add(tbEndDate, "cell 2 1");
		panel.add(new JLabel("Hour"), "cell 3 1"); //$NON-NLS-1$
		panel.add(cEndHour, "cell 4 1");
		panel.add(new JLabel("Min"), "cell 5 1");
		panel.add(cEndMin, "cell 6 1");
		panel.add(cEndAm, "cell 7 1");
		panel.add(cbEndPm, "cell 8 1");

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
	}

	private void updateView() {

		if (attendenceHistory.getClockInTime() != null) {
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(attendenceHistory.getClockInTime());

			tbStartDate.setDate(startCalendar.getTime());

			Integer hour = (Integer) startCalendar.get(Calendar.HOUR);
			if (hour.equals(0)) {
				hour = 12;

			}
			cStartHour.setSelectedItem(hour);
			cStartMin.setSelectedItem(startCalendar.get(Calendar.MINUTE));

			if (startCalendar.get(Calendar.AM_PM) == 0) {
				cbStartAm.setSelected(true);
			}
			else {
				cbStartPm.setSelected(true);
			}

		}
		if (attendenceHistory.getClockOutTime() != null) {
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(attendenceHistory.getClockOutTime());
			tbEndDate.setDate(endCalendar.getTime());

			Integer hour = (Integer) endCalendar.get(Calendar.HOUR);
			if (hour.equals(0)) {
				hour = 12;

			}
			cEndHour.setSelectedItem(hour);
			cEndMin.setSelectedItem(endCalendar.get(Calendar.MINUTE));

			if (endCalendar.get(Calendar.AM_PM) == 0) {
				cEndAm.setSelected(true);
			}
			else {
				cbEndPm.setSelected(true);
			}
		}

	}

	private boolean updateModel() {

		Calendar clockInTime = getStartDate();
		Calendar clockOutTime = getEndDate();

		System.out.println(clockInTime.getTime().getTime());
		System.out.println(clockOutTime.getTime().getTime());

		if (clockInTime.getTime().getTime() > clockOutTime.getTime().getTime()) {
			POSMessageDialog.showMessage("Clock in can not be greater than clock out");
			return false;
		}

		setClockInCalendar(clockInTime);
		setClockOutCalendar(clockOutTime);
		
		return true;
	}

	private Calendar getStartDate() {
		if (tbStartDate.getDate() == null) {
			return null;
		}

		Calendar clStartDate = Calendar.getInstance();
		clStartDate.setTime(tbStartDate.getDate());

		Integer hour = (Integer) cStartHour.getSelectedItem();
		if (hour == 12) {
			hour = 0;
		}

		clStartDate.set(Calendar.HOUR, hour);
		clStartDate.set(Calendar.MINUTE, (Integer) cStartMin.getSelectedItem());

		if (cbStartAm.isSelected()) {
			clStartDate.set(Calendar.AM_PM, Calendar.AM);
		}
		else if (cbStartPm.isSelected()) {
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

		Integer hour = (Integer) cEndHour.getSelectedItem();
		if (hour == 12) {
			hour = 0;
		}

		clEndDate.set(Calendar.HOUR, hour);
		clEndDate.set(Calendar.MINUTE, (Integer) cEndMin.getSelectedItem());

		if (cEndAm.isSelected()) {
			clEndDate.set(Calendar.AM_PM, Calendar.AM);
		}
		else if (cbEndPm.isSelected()) {
			clEndDate.set(Calendar.AM_PM, Calendar.PM);
		}

		return clEndDate;
	}

	public AttendenceHistory getAttendenceHistory() {
		return attendenceHistory;
	}

	/**
	 * @return the clockInCalendar
	 */
	public Calendar getClockInCalendar() {
		return clockInCalendar;
	}

	/**
	 * @param clockInCalendar the clockInCalendar to set
	 */
	public void setClockInCalendar(Calendar clockInCalendar) {
		this.clockInCalendar = clockInCalendar;
	}

	/**
	 * @return the clockOutCalendar
	 */
	public Calendar getClockOutCalendar() {
		return clockOutCalendar;
	}

	/**
	 * @param clockOutCalendar the clockOutCalendar to set
	 */
	public void setClockOutCalendar(Calendar clockOutCalendar) {
		this.clockOutCalendar = clockOutCalendar;
	}
}
