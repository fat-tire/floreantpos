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
package com.floreantpos.config.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.config.AppConfig;
import com.floreantpos.swing.IntegerTextField;
import com.floreantpos.swing.POSTextField;

public class TicketImportConfigurationView extends ConfigurationView {
	public static final String CONFIG_TAB_TAX = "Ticket Import"; //$NON-NLS-1$
	
	private POSTextField tfURL = new POSTextField(30);
	private POSTextField tfSecretKey = new POSTextField(10);
	private IntegerTextField tfPollInterval = new IntegerTextField(6); 
	
	public TicketImportConfigurationView() {
		JPanel contentPanel=new JPanel(); 
		contentPanel.setLayout(new MigLayout("", "[]", "[]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		contentPanel.add(new JLabel(Messages.getString("TicketImportConfigurationView.4"))); //$NON-NLS-1$
		contentPanel.add(tfURL, "wrap"); //$NON-NLS-1$
		
		contentPanel.add(new JLabel(Messages.getString("TicketImportConfigurationView.6"))); //$NON-NLS-1$
		contentPanel.add(tfPollInterval, "wrap"); //$NON-NLS-1$
		
		add(contentPanel); 
	}

	@Override
	public boolean save() throws Exception {
		if (!isInitialized()) {
			return true;
		}

		AppConfig.put("ticket_import_url", tfURL.getText()); //$NON-NLS-1$
		AppConfig.put("ticket_import_secret_key", tfSecretKey.getText()); //$NON-NLS-1$
		AppConfig.putInt("ticket_import_poll_interval", tfPollInterval.getInteger()); //$NON-NLS-1$

		return true;
	}

	@Override
	public void initialize() throws Exception {
		tfURL.setText(AppConfig.getString("ticket_import_url", "http://cloud.floreantpos.org/webstore/")); //$NON-NLS-1$ //$NON-NLS-2$
		tfSecretKey.setText(AppConfig.getString("ticket_import_secret_key", "12345")); //$NON-NLS-1$ //$NON-NLS-2$
		tfPollInterval.setText(AppConfig.getString("ticket_import_poll_interval", "60")); //$NON-NLS-1$ //$NON-NLS-2$
		
		setInitialized(true);
	}

	@Override
	public String getName() {
		return CONFIG_TAB_TAX;
	}

}
