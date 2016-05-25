package com.floreantpos.model;

import com.floreantpos.model.base.BaseDeliveryConfiguration;

public class DeliveryConfiguration extends BaseDeliveryConfiguration {
	private static final long serialVersionUID = 1L;

	public static final String UNIT_KM = "KM";
	public static final String UNIT_MILE = "MILE";

	public DeliveryConfiguration() {
		super();
	}

	public DeliveryConfiguration(java.lang.Integer id) {
		super(id);
	}
}