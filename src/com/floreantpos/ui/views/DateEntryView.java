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
