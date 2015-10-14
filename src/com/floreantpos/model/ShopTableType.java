package com.floreantpos.model;

import com.floreantpos.model.base.BaseShopTableType;



public class ShopTableType extends BaseShopTableType {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ShopTableType () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ShopTableType (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/
	
	public String toString () {
		return getName();
	}
	



}