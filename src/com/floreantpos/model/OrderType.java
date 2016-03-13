package com.floreantpos.model;

import com.floreantpos.model.base.BaseOrderType;

public class OrderType extends BaseOrderType {
	private static final long serialVersionUID = 1L;

	public static final String BAR_TAB = "BAR_TAB";
	public static final String FOR_HERE="FOR HERE"; 
	public static final String TO_GO="TO GO"; 

	public OrderType() {
		super();
	}

	public OrderType(java.lang.Integer id) {
		super(id);
	}

	public OrderType(java.lang.Integer id, java.lang.String name) {

		super(id, name);
	}

	public String name() {
		return super.getName();
	}

	public OrderType valueOf() {
		return this;
	}

	@Override
	public String toString() {
		return getName().replaceAll("_", " ");
	}

}