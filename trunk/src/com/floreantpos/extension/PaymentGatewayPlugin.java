package com.floreantpos.extension;

import com.floreantpos.config.ui.ConfigurationView;
import com.floreantpos.ui.views.payment.CardProcessor;

public interface PaymentGatewayPlugin extends FloreantPlugin {
	String getId();
	ConfigurationView getConfigurationPane() throws Exception;
	CardProcessor getProcessor();
}
