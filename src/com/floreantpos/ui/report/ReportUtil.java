package com.floreantpos.ui.report;

import java.util.Map;

import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.RestaurantDAO;

public class ReportUtil {
	public static void populateRestaurantProperties(Map map) {
		RestaurantDAO dao = new RestaurantDAO();
		Restaurant restaurant = dao.get(Integer.valueOf(1));
		map.put("restaurantName", restaurant.getName());
		map.put("addressLine1", restaurant.getAddressLine1());
		map.put("addressLine2", restaurant.getAddressLine2());
		map.put("addressLine3", restaurant.getAddressLine3());
		map.put("phone", restaurant.getTelephone());
	}
}
