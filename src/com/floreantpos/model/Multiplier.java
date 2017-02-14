package com.floreantpos.model;

import com.floreantpos.model.base.BaseMultiplier;

public class Multiplier extends BaseMultiplier {
	private static final long serialVersionUID = 1L;
	public static final String REGULAR = "Regular";

	public Multiplier() {
		super();
	}

	public Multiplier(java.lang.String name) {
		super(name);
	}

	@Override
	public Integer getButtonColor() {
		return buttonColor;
	}

	@Override
	public Integer getTextColor() {
		return textColor;
	}

}