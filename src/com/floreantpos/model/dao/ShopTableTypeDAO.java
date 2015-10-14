package com.floreantpos.model.dao;

import org.hibernate.criterion.Order;



import com.floreantpos.model.ShopTableType;





public class ShopTableTypeDAO extends BaseShopTableTypeDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public ShopTableTypeDAO () {}

	@Override
	public Order getDefaultOrder() {
		return Order.asc(ShopTableType.PROP_ID);
	}
	



}