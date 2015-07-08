package com.floreantpos.config.ui;

import javax.swing.JLabel;

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
		setLayout(new MigLayout("", "[]", "[]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		add(new JLabel(Messages.getString("TicketImportConfigurationView.4"))); //$NON-NLS-1$
		add(tfURL, "wrap"); //$NON-NLS-1$
		
		add(new JLabel(Messages.getString("TicketImportConfigurationView.6"))); //$NON-NLS-1$
		add(tfPollInterval, "wrap"); //$NON-NLS-1$
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
