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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.ModifierGroup;

public class ModifierDAO extends BaseModifierDAO {

	/**
	 * Default constructor. Can be used in place of getInstance()
	 */
	public ModifierDAO() {
	}

	public List<MenuModifier> findModifier(String name, ModifierGroup menuModifierGroup) {
		Session session = null;
		Criteria criteria = null;

		try {
			session = getSession();
			criteria = session.createCriteria(MenuModifier.class);
			if (StringUtils.isNotEmpty(name)) {
				criteria.add(Restrictions.ilike(MenuModifier.PROP_NAME, name + "%".trim(), MatchMode.ANYWHERE)); //$NON-NLS-1$
			}

			if (menuModifierGroup != null) {

				criteria.add(Restrictions.eq(MenuModifier.PROP_MODIFIER_GROUP, menuModifierGroup));
			}

			return criteria.list();
		} finally {

			session.close();
		}
	}

	public List<MenuModifier> findPizzaModifier(String name, ModifierGroup menuModifierGroup) {
		Session session = null;
		Criteria criteria = null;

		try {
			session = getSession();
			criteria = session.createCriteria(MenuModifier.class);
			criteria.add(Restrictions.eq(MenuModifier.PROP_PIZZA_MODIFIER, true)); //$NON-NLS-1$
			if (StringUtils.isNotEmpty(name)) {
				criteria.add(Restrictions.ilike(MenuModifier.PROP_NAME, name + "%".trim(), MatchMode.ANYWHERE)); //$NON-NLS-1$
			}

			if (menuModifierGroup != null) {

				criteria.add(Restrictions.eq(MenuModifier.PROP_MODIFIER_GROUP, menuModifierGroup));
			}

			return criteria.list();
		} finally {

			session.close();
		}
	}

	public List<MenuModifier> getPizzaModifiers() {
		Session session = null;
		Criteria criteria = null;

		try {
			session = createNewSession();
			criteria = session.createCriteria(MenuModifier.class);
			criteria.add(Restrictions.eq(MenuModifier.PROP_PIZZA_MODIFIER, true)); //$NON-NLS-1$

			return criteria.list();
		} finally {

			session.close();
		}
	}

	/*
	 * public List<MenuModifier> findTest() {
	 * 
	 * session = getSession(); criteria =
	 * session.createCriteria(MenuModifier.class); criteria.setFirstResult(2);
	 * criteria.setMaxResults(5); return criteria.list(); }
	 */

}