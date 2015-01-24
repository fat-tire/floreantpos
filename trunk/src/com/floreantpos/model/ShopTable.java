package com.floreantpos.model;

import com.floreantpos.model.base.BaseShopTable;



public class ShopTable extends BaseShopTable {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ShopTable () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ShopTable (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	public ShopTable(String number, Integer x, Integer y) {
		super();
		
		setTableNumber(number);
		setX(x);
		setY(y);
	}
	
	public ShopTable(ShopFloor floor, String number, Integer x, Integer y) {
		super();
		
		setFloor(floor);
		setTableNumber(number);
		setX(x);
		setY(y);
	}
	
	@Override
	public String toString() {
		return getTableNumber();
	}
}