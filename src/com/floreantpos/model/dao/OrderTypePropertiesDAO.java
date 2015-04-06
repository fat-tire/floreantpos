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