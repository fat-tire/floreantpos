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

import java.util.ArrayList;
import java.util.List;

import net.authorize.util.StringUtils;

import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.Messages;
import com.floreantpos.PosException;
import com.floreantpos.PosLog;
import com.floreantpos.model.Discount;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.Terminal;

public class MenuItemDAO extends BaseMenuItemDAO {

	/**
	 * Default constructor. Can be used in place of getInstance()
	 */
	public MenuItemDAO() {
	}

	public MenuItem loadInitialized(Integer key) throws HibernateException {
		Session session = null;
		try {
			session = createNewSession();
			MenuItem menuItem = get(key, session);
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

	public MenuItem initialize(MenuItem menuItem) {
		if (menuItem == null || menuItem.getId() == null)
			return menuItem;

		Session session = null;

		try {
			session = createNewSession();
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
	public List<MenuItem> findByParent(Terminal terminal, MenuGroup group, boolean includeInvisibleItems) throws PosException {
		Session session = null;

		try {
			session = getSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(MenuItem.PROP_PARENT, group));
			//
			criteria.addOrder(Order.asc(MenuItem.PROP_SORT_ORDER));

			if (!includeInvisibleItems) {
				criteria.add(Restrictions.eq(MenuItem.PROP_VISIBLE, Boolean.TRUE));
			}

			return criteria.list();
		} catch (Exception e) {
			PosLog.error(getClass(), e);
			throw new PosException(Messages.getString("MenuItemDAO.0")); //$NON-NLS-1$
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public List<MenuItem> findByParent(Terminal terminal, MenuGroup menuGroup, Object selectedOrderType, boolean includeInvisibleItems) {
		Session session = null;
		Criteria criteria = null;
		try {
			session = getSession();
			criteria = session.createCriteria(MenuItem.class);

			if (menuGroup != null) {
				criteria.add(Restrictions.eq(MenuItem.PROP_PARENT, menuGroup));
			}
			criteria.addOrder(Order.asc(MenuItem.PROP_SORT_ORDER));

			if (!includeInvisibleItems) {
				criteria.add(Restrictions.eq(MenuItem.PROP_VISIBLE, Boolean.TRUE));
			}

			if (selectedOrderType instanceof OrderType) {
				OrderType orderType = (OrderType) selectedOrderType;
				criteria.createAlias("orderTypeList", "type", CriteriaSpecification.LEFT_JOIN);
				criteria.add(Restrictions.or(Restrictions.isEmpty("orderTypeList"), Restrictions.eq("type.id", orderType.getId())));
			}
			return criteria.list();

		} catch (Exception e) {
		}
		return criteria.list();
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
			throw new PosException(Messages.getString("MenuItemDAO.1")); //$NON-NLS-1$
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public List<MenuItem> getMenuItems(String itemName, Object menuGroup, Object selectedType) {
		Session session = null;
		Criteria criteria = null;
		try {
			session = getSession();
			criteria = session.createCriteria(MenuItem.class);

			if (menuGroup != null && menuGroup instanceof MenuGroup) {
				criteria.add(Restrictions.eq(MenuItem.PROP_PARENT, (MenuGroup) menuGroup));
			}
			else if (menuGroup != null && menuGroup instanceof String) {
				criteria.add(Restrictions.isNull(MenuItem.PROP_PARENT));
			}
			if (StringUtils.isNotEmpty(itemName)) {
				criteria.add(Restrictions.ilike(MenuItem.PROP_NAME, itemName.trim(), MatchMode.ANYWHERE));
			}

			//List<MenuItem> similarItems = criteria.list();

			if (selectedType != null && selectedType instanceof OrderType) {
				OrderType orderType = (OrderType) selectedType;

				criteria.createAlias("orderTypeList", "type");
				criteria.add(Restrictions.eq("type.id", orderType.getId()));

				/*List<MenuItem> selectedMenuItems = new ArrayList();
				
				List<MenuItem> items = findAll();
				
				for (MenuItem item : items) {
				
					List<OrderType> types = item.getOrderTypeList();
					OrderType type = (OrderType) selectedType;
				
					if (types.contains(type.getName()) || types.isEmpty()) {
						selectedMenuItems.add(item);
					}
				}
				similarItems.retainAll(selectedMenuItems);*/
			}
			else if (selectedType != null && selectedType instanceof String) {
				criteria.add(Restrictions.isEmpty("orderTypeList"));
			}
			return criteria.list();

		} catch (Exception e) {
		}
		return criteria.list();
	}

	public List<MenuItem> getPizzaItems(String itemName, MenuGroup menuGroup, Object selectedType) {
		Session session = null;
		Criteria criteria = null;
		try {
			session = getSession();
			criteria = session.createCriteria(MenuItem.class);
			criteria.add(Restrictions.eq(MenuItem.PROP_PIZZA_TYPE, true));

			if (menuGroup != null) {
				criteria.add(Restrictions.eq(MenuItem.PROP_PARENT, menuGroup));
			}

			if (StringUtils.isNotEmpty(itemName)) {
				criteria.add(Restrictions.ilike(MenuItem.PROP_NAME, itemName.trim(), MatchMode.ANYWHERE));
			}

			//List<MenuItem> similarItems = criteria.list();

			if (selectedType instanceof OrderType) {
				OrderType orderType = (OrderType) selectedType;

				criteria.createAlias("orderTypeList", "type");
				criteria.add(Restrictions.eq("type.id", orderType.getId()));

				/*List<MenuItem> selectedMenuItems = new ArrayList();
				
				List<MenuItem> items = findAll();
				
				for (MenuItem item : items) {
				
					List<OrderType> types = item.getOrderTypeList();
					OrderType type = (OrderType) selectedType;
					if (types.contains(type.getName()) || types.isEmpty()) {
						selectedMenuItems.add(item);
					}
				}
				similarItems.retainAll(selectedMenuItems);*/
			}

			return criteria.list();

		} catch (Exception e) {
		}
		return criteria.list();
	}

	public void releaseParent(List<MenuItem> menuItemList) {
		if (menuItemList == null) {
			return;
		}

		Session session = null;
		Transaction tx = null;

		try {
			session = createNewSession();
			tx = session.beginTransaction();

			for (MenuItem menuItem : menuItemList) {
				menuItem.setParent(null);
				session.saveOrUpdate(menuItem);
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

	public void releaseParentAndDelete(MenuItem item) {
		if (item == null) {
			return;
		}

		Session session = null;
		Transaction tx = null;

		try {
			session = createNewSession();
			tx = session.beginTransaction();

			List<Discount> itemCoupons = item.getDiscounts();

			for (Discount coupon : itemCoupons) {
				List<MenuItem> mergeItems = new ArrayList();
				for (MenuItem menuItem : coupon.getMenuItems()) {
					if (menuItem != item) {
						mergeItems.add(menuItem);
					}
				}
				coupon.setMenuItems(mergeItems);
				DiscountDAO.getInstance().saveOrUpdate(coupon);
			}
			delete(item);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			LogFactory.getLog(ShopTableDAO.class).error(e);
			throw new RuntimeException(e);
		} finally {
			closeSession(session);
		}
	}

	public MenuItem getMenuItemByBarcode(String barcode) {
		Session session = null;
		Criteria criteria = null;
		try {
			session = createNewSession();
			criteria = session.createCriteria(MenuItem.class);
			criteria.add(Restrictions.like(MenuItem.PROP_BARCODE, barcode));
			List<MenuItem> result = criteria.list();
			if (result == null || result.isEmpty()) {
				return null;
			}
			return (MenuItem) result.get(0);
		} finally {
			closeSession(session);
		}
	}

	public List<MenuItem> getPizzaItems() {
		Session session = null;
		Criteria criteria = null;
		try {
			session = createNewSession();
			criteria = session.createCriteria(MenuItem.class);
			criteria.add(Restrictions.eq(MenuItem.PROP_PIZZA_TYPE, true));

			List<MenuItem> result = criteria.list();

			return result;
		} finally {
			closeSession(session);
		}
	}

	public List<MenuItem> getMenuItems() {
		Session session = null;
		Criteria criteria = null;
		try {
			session = createNewSession();
			criteria = session.createCriteria(MenuItem.class);
			criteria.add(Restrictions.or(Restrictions.eq(MenuItem.PROP_PIZZA_TYPE, false), Restrictions.isNull(MenuItem.PROP_PIZZA_TYPE)));

			List<MenuItem> result = criteria.list();

			return result;
		} finally {
			closeSession(session);
		}
	}

	public void saveAll(List<MenuItem> menuItems) {
		if (menuItems == null) {
			return;
		}
		Session session = null;
		Transaction tx = null;

		try {
			session = createNewSession();
			tx = session.beginTransaction();
			for (MenuItem menuItem : menuItems) {
				session.saveOrUpdate(menuItem);
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			PosLog.error(getClass(), e);
		} finally {
			closeSession(session);
		}
	}

}