package com.floreantpos.extension;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.floreantpos.config.ui.ConfigurationView;
import com.floreantpos.config.ui.DefaultMerchantGatewayConfigurationView;
import com.floreantpos.ui.views.payment.AuthorizeDotNetProcessor;
import com.floreantpos.ui.views.payment.CardProcessor;

@PluginImplementation
public class AuthorizeNetGatewayPlugin implements PaymentGatewayPlugin {
	public static final String ID = String.valueOf("Authorize.Net".hashCode());
	
	protected DefaultMerchantGatewayConfigurationView view;
	
	@Override
	public String getName() {
		return "Authorize.Net";
	}
	
	@Override
	public ConfigurationView getConfigurationPane() throws Exception {
		if(view == null) {
			view = new DefaultMerchantGatewayConfigurationView();
			view.setMerchantDefaultValue("6tuU4N3H", "4k6955x3T8bCVPVm");
			view.initialize();
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
		return ID;
	}

	@Override
	public CardProcessor getProcessor() {
		return new AuthorizeDotNetProcessor();
	}

	@Override
	public boolean shouldShowCardInputProcessor() {
		return true;
	}
}
