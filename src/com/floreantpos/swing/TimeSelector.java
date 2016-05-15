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
package com.floreantpos.swing;

import java.util.Calendar;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;

public class TimeSelector extends JPanel {
	private JToggleButton tbAM;
	private JToggleButton tbPM;
	private POSComboBox cbHour;
	private POSComboBox cbMin;

	public TimeSelector() {
		setLayout(new MigLayout("", "[][grow][][grow][][]", "[]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		JLabel lblHour = new JLabel(Messages.getString("TimeSelector.0")); //$NON-NLS-1$
		add(lblHour, "cell 0 0,alignx trailing"); //$NON-NLS-1$

		Vector<Integer> hours = new Vector<Integer>();
		for (int i = 1; i <= 12; i++) {
			hours.add(i);
		}
		cbHour = new POSComboBox(hours);
		add(cbHour, "cell 1 0,growy"); //$NON-NLS-1$

		JLabel lblMin = new JLabel(Messages.getString("TimeSelector.1")); //$NON-NLS-1$
		add(lblMin, "cell 2 0,alignx trailing"); //$NON-NLS-1$

		Vector<Integer> minutes = new Vector<Integer>();
		for (int i = 0; i <= 45; i = i + 15) {
			minutes.add(i);
		}

		cbMin = new POSComboBox(minutes);
		add(cbMin, "cell 3 0,growy"); //$NON-NLS-1$

		ButtonGroup group = new ButtonGroup();
		group.add(tbAM);
		group.add(tbPM);

		tbAM = new JToggleButton(Messages.getString("TimeSelector.9")); //$NON-NLS-1$
		add(tbAM, "cell 4 0,w 70!,grow"); //$NON-NLS-1$

		tbPM = new JToggleButton(Messages.getString("TimeSelector.11")); //$NON-NLS-1$
		add(tbPM, "cell 5 0,w 70!,grow"); //$NON-NLS-1$

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(tbAM);
		buttonGroup.add(tbPM);

		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR);
		int min = calendar.get(Calendar.MINUTE);
		int i = calendar.get(Calendar.AM_PM);

		setSelectedHour(hour);
		setSelectedMin(min);

		if (i == Calendar.AM) {
			tbAM.setSelected(true);
		}
		else {
			tbPM.setSelected(true);
		}
	}

	public int getSelectedHour() {
		int hour = (Integer) cbHour.getSelectedItem();
		return hour;
	}

	public void setSelectedHour(int hour) {
		if (hour == 0)
			hour = 12;

		cbHour.setSelectedItem(hour);
	}

	public int getSelectedMin() {
		int minute = (Integer) cbMin.getSelectedItem();
		return minute;
	}

	public void setSelectedMin(int min) {
		cbMin.setSelectedItem(min);
	}

	public int getAmPm() {
		if (tbAM.isSelected()) {
			return Calendar.AM;
		}

		return Calendar.PM;
	}

	public void setEnable(boolean enable) {
		cbHour.setEnabled(enable);
		cbMin.setEnabled(enable);
		tbAM.setEnabled(enable);
		tbPM.setEnabled(enable);
	}
}
