/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.extension;

import java.util.List;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.floreantpos.actions.PosAction;
import com.floreantpos.config.ui.ConfigurationDialog;
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
	public void initConfigurationView(ConfigurationDialog dialog) {
		
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

	@Override
	public List<PosAction> getPosActions() {
		return null;
	}
}
