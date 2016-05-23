package com.floreantpos.model;

import com.floreantpos.main.Application;
import com.floreantpos.model.base.BaseDeliveryCharge;
import com.floreantpos.util.NumberUtil;

public class DeliveryCharge extends BaseDeliveryCharge {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public DeliveryCharge() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public DeliveryCharge(java.lang.Integer id) {
		super(id);
	}

	/*[CONSTRUCTOR MARKER END]*/
	@Override
	public String toString() {
		return NumberUtil.roundToTwoDigit(super.getStartRange()) + "    -     " + NumberUtil.roundToTwoDigit(super.getEndRange());
	}

}