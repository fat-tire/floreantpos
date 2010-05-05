package com.floreantpos.dal;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.PosException;

public class POSDao {
	public static void save(Object o) throws Exception {
		Transaction tx = null;
		
		try {
			Session session = PosSessionFactory.currentSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(o);
			tx.commit();
		} catch (Exception e) {
			if(tx != null) {
				tx.rollback();
			}
			throw e;
		} finally {
			PosSessionFactory.closeSession();
		}
	}
	public static void delete(Object o) throws PosException {
		Transaction tx = null;
		
		Session session;
		try {
			session = PosSessionFactory.currentSession();
			tx = session.beginTransaction();
			session.delete(o);
			tx.commit();
		} catch (Exception e) {
			if(tx != null) {
				tx.rollback();
			}
			throw new PosException(com.floreantpos.POSConstants.UNNABLE_TO_SAVE_ + o.getClass(), e);
		} finally {
			PosSessionFactory.closeSession();
		}
	}
	
	public static List findAll(Class clazz) throws PosException {
		try {
			Session session = PosSessionFactory.currentSession();
			List list = session.createCriteria(clazz).list();
			return list;
		} catch (Exception e) {
			throw new PosException(e);
		} finally {
			PosSessionFactory.closeSession();
		}
	}
	
	public static Object findUnique(Class clazz) throws PosException {
		try {
			Session session = PosSessionFactory.currentSession();
			Object object = session.createCriteria(clazz).uniqueResult();
			return object;
		} catch (Exception e) {
			throw new PosException(e);
		} finally {
			PosSessionFactory.closeSession();
		}
	}

}
