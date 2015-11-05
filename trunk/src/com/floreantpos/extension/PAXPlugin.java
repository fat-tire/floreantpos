package com.floreantpos.extension;

import javax.swing.JMenuBar;

public class PAXPlugin implements PaymentGatewayPlugin {

	@Override
	public String getName() {
		return "PAX";
	}

	@Override
	public void configureBackofficeMenuBar(JMenuBar menuBar) {

	}

}
