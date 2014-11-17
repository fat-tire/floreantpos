package com.floreantpos.extension;

import com.floreantpos.config.ui.ConfigurationDialog;

import net.xeoh.plugins.base.Plugin;

public interface FloorLayoutPlugin extends Plugin {
	void initConfigurationView(ConfigurationDialog dialog);
}
