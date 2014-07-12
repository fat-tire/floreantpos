package com.floreantpos.swing;

import java.util.Calendar;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import net.miginfocom.swing.MigLayout;

public class TimeSelector extends JPanel {
	private JRadioButton rbAM;
	private JRadioButton rbPM;
	private IntegerTextField tfHour;
	private IntegerTextField tfMin;

	public TimeSelector() {
		setLayout(new MigLayout("", "[][grow][][grow][][]", "[]"));

		JLabel lblHour = new JLabel("Hour:");
		add(lblHour, "cell 0 0,alignx trailing");
		
		tfHour = new IntegerTextField();
		tfHour.setColumns(2);
		add(tfHour, "cell 1 0,growy");

		JLabel lblMin = new JLabel("Min:");
		add(lblMin, "cell 2 0,alignx trailing");
		
		tfMin = new IntegerTextField();
		tfMin.setColumns(2);
		add(tfMin, "cell 3 0,growy");

		rbAM = new JRadioButton("AM");
		add(rbAM, "cell 4 0");

		rbPM = new JRadioButton("PM");
		add(rbPM, "cell 5 0");
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rbAM);
		buttonGroup.add(rbPM);
		
		Vector<String> hours = new Vector<String>();
		for(int i = 1; i <= 12; i++) {
			hours.add(String.valueOf(i));
		}
		
		Vector<String> mins = new Vector<String>();
		for(int i = 1; i <= 59; i++) {
			mins.add(String.valueOf(i));
		}
		
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR);
		int min = calendar.get(Calendar.MINUTE);
		int i = calendar.get(Calendar.AM_PM);
		
		setSelectedHour(hour);
		setSelectedMin(min);
		
		if(i == Calendar.AM) {
			rbAM.setSelected(true);
		}
		else {
			rbPM.setSelected(true);
		}
	}

	public int getSelectedHour() {
		if(tfHour.isEmpty()) {
			return -1;
		}
		
		int hour = tfHour.getInteger();
		
		return hour;
	}
	
	public void setSelectedHour(int hour) {
		if(hour == 0) hour = 12;
		
		tfHour.setText(String.valueOf(hour));
	}
	
	public int getSelectedMin() {
		if(tfMin.isEmpty()) {
			return -1;
		}
		
		int hour = tfMin.getInteger();
		
		return hour;
	}
	
	public void setSelectedMin(int min) {
		tfMin.setText(String.valueOf(min));
	}
	
	public int getAmPm() {
		if(rbAM.isSelected()) {
			return Calendar.AM;
		}
		
		return Calendar.PM;
	}
	
	public void setEnable(boolean enable) {
		tfHour.setEnabled(enable);
		tfMin.setEnabled(enable);
		rbAM.setEnabled(enable);
		rbPM.setEnabled(enable);
	}
}
