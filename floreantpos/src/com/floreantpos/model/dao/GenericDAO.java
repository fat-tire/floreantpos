package com.floreantpos.model.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class GenericDAO extends _RootDAO {

	public GenericDAO() {
		super();
	}

	@Override
	protected Class getReferenceClass() {
		return null;
	}
	
	@Override
	public Serializable save(Object obj) {
		return super.save(obj);
	}
	
	@Override
	public void saveOrUpdate(Object obj) {
		super.saveOrUpdate(obj);
	}
	
	@Override
	public Serializable save(Object obj, Session s) {
		return super.save(obj, s);
	}
	
	@Override
	public void saveOrUpdate(Object obj, Session s) {
		super.saveOrUpdate(obj, s);
	}
	
	@Override
	public Session getSession(String configFile, boolean createNew) {
		return super.getSession(configFile, createNew);
	}
	
	public List findAll(Class clazz, Session session) {
		Criteria crit = session.createCriteria(clazz);
		return crit.list();
	}
	
	public void saveAll(List list, Session session) {
		Transaction tx = session.beginTransaction();
		for (Object object : list) {
			session.saveOrUpdate(object);
		}
		tx.commit();
	}
	
	@Override
	public void closeSession(Session session) {
		try {
			super.closeSession(session);
		}catch(Exception x) {}
	}
}
