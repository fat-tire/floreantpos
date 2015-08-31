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
	
	private boolean isTemporary;

	public ShopTable(Integer x, Integer y) {
		super();
		
		setX(x);
		setY(y);
	}
	
	public ShopTable(ShopFloor floor, Integer x, Integer y) {
		super();
		
		setFloor(floor);
		setX(x);
		setY(y);
	}
	
	public Integer getTableNumber() {
		return getId();
	}
	
	@Override
	public String toString() {
		return String.valueOf(getTableNumber());
	}

	public boolean isTemporary() {
		return isTemporary;
	}

	public void setTemporary(boolean isTemporary) {
		this.isTemporary = isTemporary;
	}
}