package com.floreantpos.extension;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.floreantpos.config.ui.ConfigurationView;
import com.floreantpos.config.ui.InginicoConfigurationView;
import com.floreantpos.ui.views.payment.CardProcessor;

@PluginImplementation
public class InginicoPlugin implements PaymentGatewayPlugin {
	InginicoConfigurationView view;
	
	@Override
	public String getName() {
		return "Inginico";
	}

	@Override
	public void init() {
	}

	@Override
	public void initBackoffice() {
	}

	@Override
	public String getId() {
		return String.valueOf("Inginico".hashCode()); //
	}
	
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public ConfigurationView getConfigurationPane() throws Exception {
		if(view == null) {
			 view = new InginicoConfigurationView();
			 view.initialize();
		}
		
		return view;
	}

	@Override
	public CardProcessor getProcessor() {
		return null;
	}

	@Override
	public boolean shouldShowCardInputProcessor() {
		return true;
	}

}
