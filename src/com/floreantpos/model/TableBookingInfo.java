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

import java.util.Iterator;
import java.util.List;

import com.floreantpos.model.base.BaseTableBookingInfo;

public class TableBookingInfo extends BaseTableBookingInfo {
	private static final long serialVersionUID = 1L;
	
	public static final String STATUS_CANCEL="cancel"; //$NON-NLS-1$
	public static final String STATUS_CLOSE="close"; //$NON-NLS-1$
	public static final String STATUS_NO_APR="no appear"; //$NON-NLS-1$
	public static final String STATUS_SEAT="seat"; //$NON-NLS-1$
	public static final String STATUS_DELAY="delay"; //$NON-NLS-1$
	public static final String STATUS_OPEN="open"; //$NON-NLS-1$
	

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public TableBookingInfo () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public TableBookingInfo (java.lang.Integer id) {
		super(id);
	}

	/*[CONSTRUCTOR MARKER END]*/
	public String toString() {
		return getId().toString();
	}

	private String customerInfo;
	private String bookedTableNumbers;
	
	/**
	 * @return the customerInfo
	 */
	public String getCustomerInfo() {
		Customer customer = getCustomer();

		if(customer == null) {
			return customerInfo;
		}

		if(!customer.getFirstName().equals("")) { //$NON-NLS-1$
			return customerInfo = customer.getFirstName();
		}

		if(!customer.getMobileNo().equals("")) { //$NON-NLS-1$
			return customerInfo = customer.getMobileNo();
		}
		
		if(!customer.getLoyaltyNo().equals("")) { //$NON-NLS-1$
			return customerInfo = customer.getLoyaltyNo();
		}
		
		return customerInfo;
	}

	/**
	 * @param customerInfo the customerInfo to set
	 */
	public void setCustomerInfo(String customerInfo) {
		this.customerInfo = customerInfo;
	}

	/**
	 * @return table numbers as comma separated string.
	 */
	public String getBookedTableNumbers() {
		if(bookedTableNumbers != null) {
			return bookedTableNumbers;
		}

		List<ShopTable> shopTables = getTables();
		if(shopTables == null || shopTables.isEmpty()) {
			return null;
		}
		String tableNumbers = ""; //$NON-NLS-1$

		for (Iterator iterator = shopTables.iterator(); iterator.hasNext();) {
			ShopTable shopTable = (ShopTable) iterator.next();
			tableNumbers += shopTable.getTableNumber();

			if(iterator.hasNext()) {
				tableNumbers += ", "; //$NON-NLS-1$
			}
		}

		return tableNumbers;
	}

	public void setBookedTableNumbers(String bookTableNumbers) {
		this.bookedTableNumbers = bookTableNumbers;
	}

}