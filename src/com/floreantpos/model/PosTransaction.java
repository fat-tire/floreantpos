package com.floreantpos.model;

import java.util.HashMap;

import com.floreantpos.model.base.BasePosTransaction;
import com.floreantpos.util.POSUtil;



public class PosTransaction extends BasePosTransaction {
	private static final long serialVersionUID = 1L;
	
/*[CONSTRUCTOR MARKER BEGIN]*/
	public PosTransaction () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public PosTransaction (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	public boolean isCard() {
		return (this instanceof CreditCardTransaction) || (this instanceof DebitCardTransaction);
	}
	
	public void addProperty(String name, String value) {
		if (getProperties() == null) {
			setProperties(new HashMap<String, String>());
		}

		getProperties().put(name, value);
	}

	public boolean hasProperty(String key) {
		return getProperty(key) != null;
	}

	public String getProperty(String key) {
		if (getProperties() == null) {
			return null;
		}

		return getProperties().get(key);
	}
	
	public boolean isPropertyValueTrue(String propertyName) {
		String property = getProperty(propertyName);
		
		return POSUtil.getBoolean(property);
	}
	
	public Double calculateTotalAmount() {
		return getAmount() + getTipsAmount();
	}
	
	public Double calculateAuthorizeAmount() {
		return getTenderAmount() + getTenderAmount() * 0.2;
	}
}