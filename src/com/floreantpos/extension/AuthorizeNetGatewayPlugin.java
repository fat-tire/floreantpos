package com.floreantpos.extension;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class AuthorizeNetGatewayPlugin implements PaymentGatewayPlugin {

	@Override
	public String getName() {
		return "Authorize.Net";
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
}
