package com.floreantpos.config.ui;

import javax.swing.JCheckBox;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.util.POSUtil;

public class TaxConfigurationView extends ConfigurationView {
	public static final String CONFIG_TAB_TAX = Messages.getString("TaxConfigurationView.0"); //$NON-NLS-1$
	private Restaurant restaurant;
	private JCheckBox cbItemSalesPriceIncludesTax;
	
	public TaxConfigurationView() {
		setLayout(new MigLayout("", "[]", "[]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		cbItemSalesPriceIncludesTax = new JCheckBox(Messages.getString("TaxConfigurationView.4")); //$NON-NLS-1$
		add(cbItemSalesPriceIncludesTax, "cell 0 0"); //$NON-NLS-1$
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
