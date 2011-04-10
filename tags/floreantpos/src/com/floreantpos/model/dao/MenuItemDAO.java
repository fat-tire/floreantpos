package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.PosException;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;


public class MenuItemDAO extends BaseMenuItemDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public MenuItemDAO () {}
	
	public MenuItem initialize(MenuItem menuItem) {
		if(menuItem.getId() == null) return menuItem;
		
		Session session = null;
		
		try {
			session = createNewSession();
			//session.refresh(menuItem);
			menuItem = (MenuItem) session.merge(menuItem);
			Hibernate.initialize(menuItem.getMenuItemModiferGroups());
			List<MenuItemModifierGroup> menuItemModiferGroups = menuItem.getMenuItemModiferGroups();
			if (menuItemModiferGroups != null) {
				for (MenuItemModifierGroup menuItemModifierGroup : menuItemModiferGroups) {
					Hibernate.initialize(menuItemModifierGroup.getModifierGroup().getModifiers());
				}
			}
			Hibernate.initialize(menuItem.getShifts());
			return menuItem;
		} finally {
			closeSession(session);
		}
	}

	@SuppressWarnings("unchecked")
	public List<MenuItem> findByParent(MenuGroup group, boolean includeInvisibleItems) throws PosException {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuItem.PROP_PARENT, group));
			
			if(!includeInvisibleItems) {
				criteria.add(Restrictions.eq(MenuItem.PROP_VISIBLE, Boolean.TRUE));
			}
			
			return criteria.list();
		} catch (Exception e) {
			throw new PosException("Error occured while finding food items");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	public List<MenuItemModifierGroup> findModifierGroups(MenuItem item) throws PosException {
		Session session = null;
		
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuItem.PROP_ID, item.getId()));
			MenuItem newItem = (MenuItem) criteria.uniqueResult();
			Hibernate.initialize(newItem.getMenuItemModiferGroups());

			return newItem.getMenuItemModiferGroups();
		} catch (Exception e) {
			throw new PosException("Error occured while finding food items");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}