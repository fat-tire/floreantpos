package com.floreantpos.ui.views;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import com.floreantpos.swing.POSTextField;

public class DateEntryView extends JPanel {
	private POSTextField tfDay;
	private POSTextField tfMonth;
	private POSTextField tfYear;
	
	public DateEntryView() {
		createUI();
	}

	private void createUI() {
		setLayout(new MigLayout("", "[][grow][][grow][][grow]", "[]"));
		
		JLabel lblDaye = new JLabel("Day");
		add(lblDaye, "cell 0 0,alignx trailing");
		
		tfDay = new POSTextField();
		tfDay.setColumns(2);
		add(tfDay, "cell 1 0,growx");
		
		JLabel lblMonth = new JLabel("Month");
		add(lblMonth, "cell 2 0,alignx trailing");
		
		tfMonth = new POSTextField();
		tfMonth.setColumns(2);
		add(tfMonth, "cell 3 0,growx");
		
		JLabel lblYear = new JLabel("Year");
		add(lblYear, "cell 4 0,alignx trailing");
		
		tfYear = new POSTextField();
		tfYear.setColumns(4);
		add(tfYear, "cell 5 0,growx");
	}
	
	public String getDay() {
		return tfDay.getText();
	}
	
	public String getMonth() {
		return tfMonth.getText();
	}
	
	public String getYear() {
		return tfYear.getText();
	}

}
