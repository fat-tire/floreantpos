package com.floreantpos.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.floreantpos.model.base.BaseTax;

@XmlRootElement(name="tax")
public class Tax extends BaseTax {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public Tax () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Tax (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Tax (
		java.lang.Integer id,
		java.lang.String name) {

		super (
			id,
			name);
	}

	/*[CONSTRUCTOR MARKER END]*/
	
	public String getUniqueId() {
		return ("tax_" + getName() + "_" + getId()).replaceAll("\\s+", "_"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Override
	public String toString() {
		return getName() + " (" + getRate() + "%)"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}