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
	public ZipCodeVsDeliveryCharge (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public ZipCodeVsDeliveryCharge (
		java.lang.Integer id,
		java.lang.String zipCode,
		double deliveryCharge) {

		super (
			id,
			zipCode,
			deliveryCharge);
	}

/*[CONSTRUCTOR MARKER END]*/


}