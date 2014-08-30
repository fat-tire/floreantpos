package com.floreantpos.model;

import com.floreantpos.model.base.BaseZipCodeVsDeliveryCharge;



public class ZipCodeVsDeliveryCharge extends BaseZipCodeVsDeliveryCharge {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ZipCodeVsDeliveryCharge () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ZipCodeVsDeliveryCharge (java.lang.Integer autoId) {
		super(autoId);
	}

	/**
	 * Constructor for required fields
	 */
	public ZipCodeVsDeliveryCharge (
		java.lang.Integer autoId,
		double deliveryCharge) {

		super (
			autoId,
			deliveryCharge);
	}

/*[CONSTRUCTOR MARKER END]*/


}