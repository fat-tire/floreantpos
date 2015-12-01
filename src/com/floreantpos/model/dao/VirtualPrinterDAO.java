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
package com.floreantpos.model.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.VirtualPrinter;


public class VirtualPrinterDAO extends BaseVirtualPrinterDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public VirtualPrinterDAO () {}

	public VirtualPrinter findPrinterByName(String name) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(getReferenceClass());
		criteria.add(Restrictions.eq(VirtualPrinter.PROP_NAME, name));
		
		return (VirtualPrinter) criteria.uniqueResult();
	}

}