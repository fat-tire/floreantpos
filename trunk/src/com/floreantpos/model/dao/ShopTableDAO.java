package com.floreantpos.model.dao;

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;


public class ShopTableDAO extends BaseShopTableDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public ShopTableDAO () {}

	public ShopTable getByNumber(String tableNumber) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(getReferenceClass());
		criteria.add(Restrictions.eq(ShopTable.PROP_TABLE_NUMBER, tableNumber));
		
		return (ShopTable) criteria.uniqueResult();
	}

	public List<ShopTable> getByNumbers(Collection<String> tableNumbers) {
		if(tableNumbers == null) {
			return null;
		}
		
		Session session = getSession();
		Criteria criteria = session.createCriteria(getReferenceClass());
		Disjunction disjunction = Restrictions.disjunction();
		
		for (String tableNumber : tableNumbers) {
			disjunction.add(Restrictions.eq(ShopTable.PROP_TABLE_NUMBER, tableNumber));
		}
		criteria.add(disjunction);
		
		return criteria.list();
	}
	
	public List<ShopTable> getTables(Ticket ticket) {
		return getByNumbers(ticket.getTableNumbers()); 
	}
	
	public void occupyTables(Ticket ticket) {
		List<ShopTable> tables = getTables(ticket);
		
		if(tables == null) return;
		
		Session session = null;
		Transaction tx = null;
		
		try {
			session = createNewSession();
			tx = session.beginTransaction();

			for (ShopTable shopTable : tables) {
				shopTable.setOccupied(true);
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
		
		if(tables == null) return;
		
		Session session = null;
		Transaction tx = null;
		
		try {
			session = createNewSession();
			tx = session.beginTransaction();

			for (ShopTable shopTable : tables) {
				shopTable.setOccupied(false);
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
}