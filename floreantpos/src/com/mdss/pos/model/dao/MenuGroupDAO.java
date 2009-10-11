package com.mdss.pos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.mdss.pos.PosException;
import com.mdss.pos.model.MenuCategory;
import com.mdss.pos.model.MenuGroup;


public class MenuGroupDAO extends BaseMenuGroupDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public MenuGroupDAO () {}

	@SuppressWarnings("unchecked")
	public List<MenuGroup> findEnabledByParent(MenuCategory category) throws PosException {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuGroup.PROP_VISIBLE, Boolean.TRUE));
			criteria.add(Restrictions.eq(MenuGroup.PROP_PARENT, category));
			
			return criteria.list();
		} finally {
			closeSession(session);
		}
	}
}