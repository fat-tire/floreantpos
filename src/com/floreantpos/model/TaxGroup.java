package com.floreantpos.model;

import java.util.Iterator;
import java.util.List;

import com.floreantpos.model.base.BaseTaxGroup;

public class TaxGroup extends BaseTaxGroup {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public TaxGroup () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public TaxGroup (java.lang.String id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public TaxGroup (
		java.lang.String id,
		java.lang.String name) {

		super (
			id,
			name);
	}

	/*[CONSTRUCTOR MARKER END]*/
	@Override
	public String toString() {
		String name = super.getName();
		List<Tax> taxes = getTaxes();
		if (taxes == null || taxes.isEmpty()) {
			return name;
		}
		name += " (";
		for (Iterator iterator = taxes.iterator(); iterator.hasNext();) {
			Tax tax = (Tax) iterator.next();
			name += tax.getName() + ":" + tax.getRate();
			if (iterator.hasNext()) {
				name += ", ";
			}
		}
		name += ")";
		return name;
	}

}