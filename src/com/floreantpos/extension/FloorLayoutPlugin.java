package com.floreantpos.extension;

import java.awt.Dimension;
import java.util.List;

import com.floreantpos.config.ui.ConfigurationDialog;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;

public interface FloorLayoutPlugin extends FloreantPlugin {
	
	public final static Dimension defaultFloorImageSize = new Dimension(400, 400);
	
	
	
	void initialize();
	void openTicketsAndTablesDisplay();
	void initConfigurationView(ConfigurationDialog dialog);
	List<ShopTable> captureTableNumbers(Ticket ticket);
}
