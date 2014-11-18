package com.floreantpos.extension;

import java.util.List;

import com.floreantpos.config.ui.ConfigurationDialog;
import com.floreantpos.model.ShopTable;

import net.xeoh.plugins.base.Plugin;

public interface FloorLayoutPlugin extends Plugin {
	void initialize();
	void initConfigurationView(ConfigurationDialog dialog);
	List<ShopTable> captureTableNumbers();
}
