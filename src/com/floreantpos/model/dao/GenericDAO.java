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
