package com.floreantpos.model.dao;

import java.util.Set;

import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;

import com.floreantpos.model.ShopFloor;
import com.floreantpos.model.ShopTable;


public class ShopFloorDAO extends BaseShopFloorDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public ShopFloorDAO () {}

	public boolean hasFloor() {
		Number result = (Number) getSession().createCriteria(getReferenceClass()).setProjection(Projections.rowCount()).uniqueResult();
		return result.intValue() != 0;
	}
	
	@Override
	public void delete(ShopFloor shopFloor) throws HibernateException {
		Session session = null;
		Transaction tx = null;
		
		try {
			session = createNewSession();
			tx = session.beginTransaction();

			Set<ShopTable> tables = shopFloor.getTables();

			super.delete(shopFloor, session);

			for (ShopTable shopTable : tables) {
				delete(shopTable, session);
			}

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			LogFactory.getLog(ShopFloorDAO.class).error(e);
			
			throw new HibernateException(e);
		} finally {
			closeSession(session);
		}
	}
}