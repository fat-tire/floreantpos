package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.MenuCategory;

public class MenuCategoryDAO extends BaseMenuCategoryDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public MenuCategoryDAO() {
	}

	public List<MenuCategory> findAllEnable() {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuCategory.PROP_VISIBLE, Boolean.TRUE));
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
	
	public List<MenuCategory> findNonBevegares() {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuCategory.PROP_VISIBLE, Boolean.TRUE));
			criteria.add(Restrictions.or(Restrictions.isNull(MenuCategory.PROP_BEVERAGE), Restrictions.eq(MenuCategory.PROP_BEVERAGE, Boolean.FALSE)));
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
	
	public List<MenuCategory> findBevegares() {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuCategory.PROP_VISIBLE, Boolean.TRUE));
			criteria.add(Restrictions.eq(MenuCategory.PROP_BEVERAGE, Boolean.TRUE));
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
}