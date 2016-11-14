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
package com.floreantpos.actions;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.commons.logging.LogFactory;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.AttendenceHistory;
import com.floreantpos.model.EmployeeInOutHistory;
import com.floreantpos.model.Shift;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.AttendenceHistoryDAO;
import com.floreantpos.model.dao.EmployeeInOutHistoryDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.PasswordEntryDialog;
import com.floreantpos.util.POSUtil;
import com.floreantpos.util.ShiftUtil;

public class ClockInOutAction extends PosAction {

	public ClockInOutAction() {
		super(Messages.getString("ClockInOutAction.0")); //$NON-NLS-1$
	}

	public ClockInOutAction(boolean showText, boolean showIcon) {
		if (showText) {
			putValue(Action.NAME, Messages.getString("ClockInOutAction.1")); //$NON-NLS-1$
		}
		if (showIcon) {
			putValue(Action.SMALL_ICON, IconFactory.getIcon("/ui_icons/", "clock_out.png")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@Override
	public void execute() {
		final User user = PasswordEntryDialog.getUser(Application.getPosWindow(), Messages.getString("ClockInOutAction.3")); //$NON-NLS-1$
		if (user == null) {
			return;
		}

		final POSDialog dialog = new POSDialog(Application.getPosWindow(), true);
		dialog.setTitle(Messages.getString("ClockInOutAction.4")); //$NON-NLS-1$

		PosButton btnClockIn = new PosButton(Messages.getString("ClockInOutAction.5")); //$NON-NLS-1$
		btnClockIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
				performClockIn(user);
			}
		});

		PosButton btnClockOut = new PosButton(Messages.getString("ClockInOutAction.6")); //$NON-NLS-1$
		btnClockOut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
				performClockOut(user);
			}
		});

		PosButton btnDriverIn = new PosButton("DRIVER IN"); //$NON-NLS-1$
		btnDriverIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
				performDriverIn(user);
			}
		});

		PosButton btnDriverOut = new PosButton("DRIVER OUT"); //$NON-NLS-1$
		btnDriverOut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
				performDriverOut(user);
			}
		});

		PosButton btnCancel = new PosButton(Messages.getString("ClockInOutAction.7")); //$NON-NLS-1$
		btnCancel.setPreferredSize(new Dimension(150, 120));
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});

		JPanel contentPane = (JPanel) dialog.getContentPane();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new GridLayout(1, 0, 10, 10));

		if (user.isClockedIn()) {
			contentPane.add(btnClockOut);
		}
		else {
			contentPane.add(btnClockIn);
		}

		if (user.isDriver()) {
			if (user.isClockedIn()) {
				if (user.isAvailableForDelivery()) {
					contentPane.add(btnDriverOut);
				}
				else {
					contentPane.add(btnDriverIn);
				}
			}
		}

		contentPane.add(btnCancel);
		dialog.pack();
		dialog.open();
	}

	private void performClockOut(User user) {
		try {
			if (user == null) {
				return;
			}

			AttendenceHistoryDAO attendenceHistoryDAO = new AttendenceHistoryDAO();
			AttendenceHistory attendenceHistory = attendenceHistoryDAO.findHistoryByClockedInTime(user);
			if (attendenceHistory == null) {
				attendenceHistory = new AttendenceHistory();
				Date lastClockInTime = user.getLastClockInTime();
				Calendar c = Calendar.getInstance();
				c.setTime(lastClockInTime);
				attendenceHistory.setClockInTime(lastClockInTime);
				attendenceHistory.setClockInHour(Short.valueOf((short) c.get(Calendar.HOUR)));
				attendenceHistory.setUser(user);
				attendenceHistory.setTerminal(Application.getInstance().getTerminal());
				attendenceHistory.setShift(user.getCurrentShift());
			}

			Shift shift = user.getCurrentShift();
			Calendar calendar = Calendar.getInstance();

			user.doClockOut(attendenceHistory, shift, calendar);

			POSMessageDialog
					.showMessage(Messages.getString("ClockInOutAction.8") + user.getFirstName() + " " + user.getLastName() + Messages.getString("ClockInOutAction.10")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage(), e);
		}
	}

	private void performClockIn(User user) {
		try {
			if (user == null) {
				return;
			}

			if (user.isClockedIn() != null && user.isClockedIn().booleanValue()) {
				POSMessageDialog
						.showMessage(Messages.getString("ClockInOutAction.11") + user.getFirstName() + " " + user.getLastName() + Messages.getString("ClockInOutAction.13")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				return;
			}

			Shift currentShift = ShiftUtil.getCurrentShift();
			//			if (currentShift == null) {
			//				throw new ShiftException(POSConstants.NO_SHIFT_CONFIGURED);
			//			}

			//			ShiftUtil.adjustUserShiftAndClockIn(user, currentShift);

			Calendar currentTime = Calendar.getInstance();
			user.doClockIn(Application.getInstance().getTerminal(), currentShift, currentTime);

			POSMessageDialog
					.showMessage(Messages.getString("ClockInOutAction.14") + user.getFirstName() + " " + user.getLastName() + Messages.getString("ClockInOutAction.16")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage(), e);
		}
	}

	public void performDriverOut(User user) {
		try {
			if (user == null) {
				return;
			}

			Shift currentShift = ShiftUtil.getCurrentShift();
			Terminal terminal = Application.getInstance().getTerminal();

			Calendar currentTime = Calendar.getInstance();
			user.setAvailableForDelivery(false);
			user.setLastClockOutTime(currentTime.getTime());

			LogFactory.getLog(Application.class).info("terminal id befor saving clockIn=" + terminal.getId()); //$NON-NLS-1$

			EmployeeInOutHistory attendenceHistory = new EmployeeInOutHistory();
			attendenceHistory.setOutTime(currentTime.getTime());
			attendenceHistory.setOutHour(Short.valueOf((short) currentTime.get(Calendar.HOUR_OF_DAY)));
			attendenceHistory.setClockOut(true);
			attendenceHistory.setUser(user);
			attendenceHistory.setTerminal(terminal);
			attendenceHistory.setShift(currentShift);

			UserDAO.getInstance().saveDriverOut(user, attendenceHistory, currentShift, currentTime);

			POSMessageDialog.showMessage("Driver " + user.getFirstName() + " " + user.getLastName() + " is out for delivery."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage(), e);
		}
	}

	public void performDriverIn(User user) {
		try {
			if (user == null) {
				return;
			}
			if (!user.isClockedIn()) {
				POSMessageDialog.showMessage(POSUtil.getFocusedWindow(), Messages.getString("ClockInOutAction.2")); //$NON-NLS-1$
				return;
			}

			EmployeeInOutHistoryDAO attendenceHistoryDAO = new EmployeeInOutHistoryDAO();
			EmployeeInOutHistory attendenceHistory = attendenceHistoryDAO.findDriverHistoryByClockedInTime(user);
			if (attendenceHistory == null) {
				attendenceHistory = new EmployeeInOutHistory();
				Date lastClockOutTime = user.getLastClockOutTime();
				Calendar c = Calendar.getInstance();
				c.setTime(lastClockOutTime);
				attendenceHistory.setOutTime(lastClockOutTime);
				attendenceHistory.setOutHour(Short.valueOf((short) c.get(Calendar.HOUR)));
				attendenceHistory.setUser(user);
				attendenceHistory.setTerminal(Application.getInstance().getTerminal());
				attendenceHistory.setShift(user.getCurrentShift());
			}

			Shift shift = user.getCurrentShift();
			Calendar calendar = Calendar.getInstance();

			user.setAvailableForDelivery(true);

			attendenceHistory.setClockOut(false);
			attendenceHistory.setInTime(calendar.getTime());
			attendenceHistory.setInHour(Short.valueOf((short) calendar.get(Calendar.HOUR_OF_DAY)));

			UserDAO.getInstance().saveDriverIn(user, attendenceHistory, shift, calendar);

			POSMessageDialog.showMessage("Driver " + user.getFirstName() + " " + user.getLastName() + " is in after delivery"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage(), e);
		}
	}

}
