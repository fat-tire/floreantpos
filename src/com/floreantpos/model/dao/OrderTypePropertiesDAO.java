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

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.model.OrderType;
import com.floreantpos.model.OrderTypeProperties;


public class OrderTypePropertiesDAO extends BaseOrderTypePropertiesDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public OrderTypePropertiesDAO () {}
	
	public static void populate() {
		List<OrderTypeProperties> list = OrderTypePropertiesDAO.getInstance().findAll();
		for (OrderTypeProperties orderTypeProperties : list) {
			OrderType orderType = OrderType.valueOf(orderTypeProperties.getOrdetType());
			orderType.setProperties(orderTypeProperties);
		}
	}

	public void saveAll() throws Exception {
		Session session = null;
		Transaction tx = null;
		
		try {
			session = createNewSession();
			tx = session.beginTransaction();

			saveOrderType(OrderType.DINE_IN, session);
			saveOrderType(OrderType.TAKE_OUT, session);
			saveOrderType(OrderType.PICKUP, session);
			saveOrderType(OrderType.HOME_DELIVERY, session);
			saveOrderType(OrderType.DRIVE_THRU, session);
			saveOrderType(OrderType.BAR_TAB, session);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw e;
		} finally {
			closeSession(session);
		}
	}

	public void saveOrderType(OrderType orderType, Session session) {
		OrderTypeProperties properties = orderType.getProperties();
		
		if(properties == null) {
			return;
		}
		
		saveOrUpdate(properties, session);
	}
}