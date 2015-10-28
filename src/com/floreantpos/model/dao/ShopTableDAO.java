package com.floreantpos.model.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.ShopTable;
import com.floreantpos.model.ShopTableType;
import com.floreantpos.model.Ticket;

public class ShopTableDAO extends BaseShopTableDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public ShopTableDAO() {
	}

	@Override
	public Order getDefaultOrder() {
		return Order.asc(ShopTable.PROP_ID);
	}

	public int getNextTableNumber() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(getReferenceClass());
		criteria.setProjection(Projections.rowCount());

		Integer result = (Integer) criteria.uniqueResult();

		return result;
	}

	public ShopTable getByNumber(int tableNumber) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(getReferenceClass());
		criteria.add(Restrictions.eq(ShopTable.PROP_ID, tableNumber));

		return (ShopTable) criteria.uniqueResult();
	}

	public List<ShopTable> getAllUnassigned() {
		Session session = getSession();
		Criteria criteria = session.createCriteria(getReferenceClass());
		criteria.add(Restrictions.isNull(ShopTable.PROP_FLOOR));

		return criteria.list();
	}

	public List<ShopTable> getByNumbers(Collection<Integer> tableNumbers) {
		if (tableNumbers == null) {
			return null;
		}

		Session session = getSession();
		Criteria criteria = session.createCriteria(getReferenceClass());
		Disjunction disjunction = Restrictions.disjunction();

		for (Integer tableNumber : tableNumbers) {
			disjunction.add(Restrictions.eq(ShopTable.PROP_ID, tableNumber));
		}
		criteria.add(disjunction);

		return criteria.list();
	}

	public List<ShopTable> getTables(Ticket ticket) {
		return getByNumbers(ticket.getTableNumbers());
	}

	public void occupyTables(Ticket ticket) {
		List<ShopTable> tables = getTables(ticket);

		if (tables == null)
			return;

		Session session = null;
		Transaction tx = null;

		try {
			session = createNewSession();
			tx = session.beginTransaction();

			for (ShopTable shopTable : tables) {
				shopTable.setServing(true);
				saveOrUpdate(shopTable);
			}

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			LogFactory.getLog(ShopTableDAO.class).error(e);
			throw new RuntimeException(e);
		} finally {
			closeSession(session);
		}
	}

	public void releaseTables(Ticket ticket) {
		List<ShopTable> tables = getTables(ticket);

		if (tables == null)
			return;

		Session session = null;
		Transaction tx = null;

		try {
			session = createNewSession();
			tx = session.beginTransaction();

			for (ShopTable shopTable : tables) {
				shopTable.setServing(false);
				shopTable.setBooked(false);
				saveOrUpdate(shopTable);
			}

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			LogFactory.getLog(ShopTableDAO.class).error(e);
			throw new RuntimeException(e);
		} finally {
			closeSession(session);
		}
	}

	public void deleteTables(Collection<ShopTable> tables) {
		Session session = null;
		Transaction tx = null;

		try {
			session = createNewSession();
			tx = session.beginTransaction();

			for (ShopTable shopTable : tables) {
				super.delete(shopTable, session);
			}

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			LogFactory.getLog(ShopTableDAO.class).error(e);
			throw new RuntimeException(e);
		} finally {
			closeSession(session);
		}
	}

	public List<ShopTableType> getTableByTypes(List<ShopTableType> types) {
		List<Integer> typeIds = new ArrayList<Integer>();
		for (ShopTableType shopTableType : types) {
			typeIds.add(shopTableType.getId());
		}

		Session session = getSession();
		Criteria criteria = session.createCriteria(ShopTable.class);
		criteria.createAlias("types", "t"); //$NON-NLS-1$ //$NON-NLS-2$
		criteria.add(Restrictions.in("t.id", typeIds)); //$NON-NLS-1$
		//criteria.add(Restrictions("t.id", typeIds)); //$NON-NLS-1$
		criteria.addOrder(Order.asc(ShopTable.PROP_ID));
		return criteria.list();
	}

}