package com.floreantpos.util;

import java.io.Serializable;
import java.sql.Timestamp;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import com.floreantpos.model.dao.RestaurantDAO;

public class IntegerIdGenerator implements IdentifierGenerator {
	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		Timestamp timestamp = RestaurantDAO.getInstance().geTimestamp();
		long time = timestamp.getTime();
		int uniqueId= (int) ((time / 1000L) % Integer.MAX_VALUE);
		return uniqueId;
	}
}
