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

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.floreantpos.config.ui.ConfigurationView;
import com.floreantpos.config.ui.DefaultMerchantGatewayConfigurationView;
import com.floreantpos.ui.views.payment.CardProcessor;
import com.floreantpos.ui.views.payment.MercuryPayProcessor;

@PluginImplementation
public class MercuryGatewayPlugin extends AuthorizeNetGatewayPlugin {

	@Override
	public String getProductName() {
		return "Mercury Pay"; //$NON-NLS-1$
	}
	
	@Override
	public ConfigurationView getConfigurationPane() {
		if(view == null) {
			view = new DefaultMerchantGatewayConfigurationView();
			view.setMerchantDefaultValue("118725340908147", "XYZ"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		return view;
	}

	@Override
	public void initUI() {

	}

	@Override
	public void initBackoffice() {

	}

	@Override
	public String toString() {
		return getProductName();
	}
	
	@Override
	public String getId() {
		return String.valueOf("Mercury Pay".hashCode()); //$NON-NLS-1$
	}
	
	@Override
	public CardProcessor getProcessor() {
		return new MercuryPayProcessor();
	}
}
