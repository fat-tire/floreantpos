package com.floreantpos.model.dao;

import org.hibernate.criterion.Order;

import com.floreantpos.model.MenuItemSize;
import com.floreantpos.model.dao.BaseMenuItemSizeDAO;


public class MenuItemSizeDAO extends BaseMenuItemSizeDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public MenuItemSizeDAO () {}

	@Override
	public Order getDefaultOrder() {
		return Order.asc(MenuItemSize.PROP_SORT_ORDER);
	}
}