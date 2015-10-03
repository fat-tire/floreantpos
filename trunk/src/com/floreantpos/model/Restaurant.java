package com.floreantpos.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.model.base.BaseRestaurant;


@XmlRootElement(name="restaurant")
public class Restaurant extends BaseRestaurant {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Restaurant () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Restaurant (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	@Override
	public String getCurrencyName() {
		String currencyName = super.getCurrencyName();
		if(StringUtils.isEmpty(currencyName)) {
			return Messages.getString("Restaurant.0"); //$NON-NLS-1$
		}
		return currencyName;
	}
	
	@Override
	public String getCurrencySymbol() {
		String currencySymbol = super.getCurrencySymbol();
		if(StringUtils.isEmpty(currencySymbol)) {
			currencySymbol = "$"; //$NON-NLS-1$
		}
		return currencySymbol;
	}
}