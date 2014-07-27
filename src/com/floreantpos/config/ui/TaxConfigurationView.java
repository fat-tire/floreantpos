package com.floreantpos.config.ui;

import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.util.POSUtil;

import net.miginfocom.swing.MigLayout;
import javax.swing.JCheckBox;

public class TaxConfigurationView extends ConfigurationView {
	public static final String CONFIG_TAB_TAX = "Tax";
	private Restaurant restaurant;
	private JCheckBox cbItemSalesPriceIncludesTax;
	
	public TaxConfigurationView() {
		setLayout(new MigLayout("", "[]", "[]"));

		cbItemSalesPriceIncludesTax = new JCheckBox("Item sales price includes tax");
		add(cbItemSalesPriceIncludesTax, "cell 0 0");
	}

	@Override
	public boolean save() throws Exception {
		if (!isInitialized()) {
			return true;
		}

		restaurant.setItemPriceIncludesTax(cbItemSalesPriceIncludesTax.isSelected());

		RestaurantDAO.getInstance().saveOrUpdate(restaurant);

		Application.getInstance().refreshRestaurant();

		return true;
	}

	@Override
	public void initialize() throws Exception {
		restaurant = RestaurantDAO.getInstance().get(Integer.valueOf(1));
		cbItemSalesPriceIncludesTax.setSelected(POSUtil.getBoolean(restaurant.isItemPriceIncludesTax()));

		setInitialized(true);
	}

	@Override
	public String getName() {
		return CONFIG_TAB_TAX;
	}

}
