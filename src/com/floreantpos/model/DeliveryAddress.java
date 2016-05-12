package com.floreantpos.model;

import com.floreantpos.model.base.BaseDeliveryAddress;



public class DeliveryAddress extends BaseDeliveryAddress {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public DeliveryAddress () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public DeliveryAddress (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/
	@Override
	public String toString(){
		return super.getAddress(); 
	}


}