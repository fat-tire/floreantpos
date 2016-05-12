/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.model;

import java.util.HashMap;

import com.floreantpos.model.base.BaseCustomer;



public class Customer extends BaseCustomer {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Customer () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Customer (java.lang.Integer autoId) {
		super(autoId);
	}

/*[CONSTRUCTOR MARKER END]*/
	
	public void addProperty(String name, String value) {
		if(getProperties() == null) {
			setProperties(new HashMap<String, String>());
		}
		
		getProperties().put(name, value);
	}
	
	public boolean hasProperty(String key) {
		return getProperty(key) != null;
	}
	
	public String getProperty(String key) {
		if(getProperties() == null) {
			return null;
		}
		
		return getProperties().get(key);
	}
	
	@Override
	public String toString() {
		String fName = getFirstName();
		return fName;
	}
	
	public String getName(){
		String name=super.getFirstName()+" "+ super.getLastName();  //$NON-NLS-1$ 
		return name; 
	}
}