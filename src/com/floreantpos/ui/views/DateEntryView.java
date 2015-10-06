package com.floreantpos.ui.views;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.swing.POSTextField;

public class DateEntryView extends JPanel {
	private POSTextField tfDay;
	private POSTextField tfMonth;
	private POSTextField tfYear;
	
	public DateEntryView() {
		createUI();
	}

	private void createUI() {
		setLayout(new MigLayout("", "[][grow][][grow][][grow]", "[]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		JLabel lblDaye = new JLabel(Messages.getString("DateEntryView.3")); //$NON-NLS-1$
		add(lblDaye, "cell 0 0,alignx trailing"); //$NON-NLS-1$
		
		tfDay = new POSTextField();
		tfDay.setColumns(2);
		add(tfDay, "cell 1 0,growx"); //$NON-NLS-1$
		
		JLabel lblMonth = new JLabel(Messages.getString("DateEntryView.6")); //$NON-NLS-1$
		add(lblMonth, "cell 2 0,alignx trailing"); //$NON-NLS-1$
		
		tfMonth = new POSTextField();
		tfMonth.setColumns(2);
		add(tfMonth, "cell 3 0,growx"); //$NON-NLS-1$
		
		JLabel lblYear = new JLabel(Messages.getString("DateEntryView.9")); //$NON-NLS-1$
		add(lblYear, "cell 4 0,alignx trailing"); //$NON-NLS-1$
		
		tfYear = new POSTextField();
		tfYear.setColumns(4);
		add(tfYear, "cell 5 0,growx"); //$NON-NLS-1$
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
