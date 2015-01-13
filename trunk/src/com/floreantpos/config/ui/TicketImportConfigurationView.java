package com.floreantpos.config.ui;

import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.config.AppConfig;
import com.floreantpos.swing.IntegerTextField;
import com.floreantpos.swing.POSTextField;

public class TicketImportConfigurationView extends ConfigurationView {
	public static final String CONFIG_TAB_TAX = "Ticket Import";
	
	private POSTextField tfURL = new POSTextField(30);
	private POSTextField tfSecretKey = new POSTextField(10);
	private IntegerTextField tfPollInterval = new IntegerTextField(6); 
	
	public TicketImportConfigurationView() {
		setLayout(new MigLayout("", "[]", "[]"));

		add(new JLabel("Ticket import api URL"));
		add(tfURL, "wrap");
		
		add(new JLabel("Poll interval (in second)"));
		add(tfPollInterval, "wrap");
	}

	@Override
	public boolean save() throws Exception {
		if (!isInitialized()) {
			return true;
		}

		AppConfig.put("ticket_import_url", tfURL.getText());
		AppConfig.put("ticket_import_secret_key", tfSecretKey.getText());
		AppConfig.putInt("ticket_import_poll_interval", tfPollInterval.getInteger());

		return true;
	}

	@Override
	public void initialize() throws Exception {
		tfURL.setText(AppConfig.getString("ticket_import_url", "http://cloud.floreantpos.org/webstore/"));
		tfSecretKey.setText(AppConfig.getString("ticket_import_secret_key", "12345"));
		tfPollInterval.setText(AppConfig.getString("ticket_import_poll_interval", "60"));
		
		setInitialized(true);
	}

	@Override
	public String getName() {
		return CONFIG_TAB_TAX;
	}

}
