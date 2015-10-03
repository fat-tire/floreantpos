package com.floreantpos.model.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.MenuModifierGroup;

public class ModifierDAO extends BaseModifierDAO {
	Session session = null;
	Criteria criteria = null;

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public ModifierDAO() {
	}

	public List<MenuModifier> findModifier(String name, MenuModifierGroup menuModifierGroup) {

		try {
			session = getSession();
			criteria = session.createCriteria(MenuModifier.class);
			if (StringUtils.isNotEmpty(name)) {
				criteria.add(Restrictions.ilike(MenuModifier.PROP_NAME, name+"%".trim(),MatchMode.ANYWHERE)); //$NON-NLS-1$
			}

			if (menuModifierGroup != null) {

				criteria.add(Restrictions.eq(MenuModifier.PROP_MODIFIER_GROUP, menuModifierGroup));
			}

			return criteria.list();
		} finally {

			session.close();
		}
	}

/*	public List<MenuModifier> findTest() {

		session = getSession();
		criteria = session.createCriteria(MenuModifier.class);
		criteria.setFirstResult(2);
		criteria.setMaxResults(5);
		return criteria.list();
	}*/

}