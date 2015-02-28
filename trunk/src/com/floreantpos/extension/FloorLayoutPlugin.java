package com.floreantpos.extension;

import java.util.List;

import net.xeoh.plugins.base.Plugin;

import com.floreantpos.config.ui.ConfigurationDialog;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;

public interface FloorLayoutPlugin extends Plugin {
	void initialize();
	void openTicketsAndTablesDisplay();
	void initConfigurationView(ConfigurationDialog dialog);
	List<ShopTable> captureTableNumbers(Ticket ticket);
}
