package com.floreantpos.extension;

import com.floreantpos.config.ui.ConfigurationView;
import com.floreantpos.config.ui.DefaultMerchantGatewayConfigurationView;
import com.floreantpos.ui.views.payment.CardProcessor;
import com.floreantpos.ui.views.payment.MercuryPayProcessor;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class MercuryGatewayPlugin extends AuthorizeNetGatewayPlugin {

	@Override
	public String getName() {
		return "Mercury Pay";
	}
	
	@Override
	public ConfigurationView getConfigurationPane() {
		if(view == null) {
			view = new DefaultMerchantGatewayConfigurationView();
			view.setMerchantDefaultValue("118725340908147", "XYZ");
		}
		
		return view;
	}

	@Override
	public void init() {

	}

	@Override
	public void initBackoffice() {

	}

	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public String getId() {
		return String.valueOf("Mercury Pay".hashCode());
	}
	
	@Override
	public CardProcessor getProcessor() {
		return new MercuryPayProcessor();
	}
}
