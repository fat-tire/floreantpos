package com.floreantpos.config.ui;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.RestaurantDAO;

public class DrawerPullConfigurationView extends ConfigurationView {
	private RestaurantDAO dao;
	private Restaurant restaurant;
	
	JComboBox cbHour;
	JComboBox cbMin;
	JCheckBox chkAutoDrawerPull;
	
	public DrawerPullConfigurationView() {
		setLayout(new MigLayout("align 50% 50%"));
		
		Integer[] hours = new Integer[24];
		Integer[] minutes = new Integer[60];
		
		for(int i = 0; i < 24; i++) {
			hours[i] = Integer.valueOf(i);
		}
		for(int i = 0; i < 60; i++) {
			minutes[i] = Integer.valueOf(i);
		}
		
		add(chkAutoDrawerPull = new JCheckBox(com.floreantpos.POSConstants.AUTO_DRAWER_PULL_EVERY_DAY_AT_), "wrap, span");
		add(new JLabel(com.floreantpos.POSConstants.HOUR + ":"), "");
		add(cbHour = new JComboBox(hours), "");
		
		add(new JLabel("Min" + ":"), "");
		add(cbMin = new JComboBox(minutes), "");
	}

	@Override
	public boolean save() throws Exception {
		if(!isInitialized()) {
			return true;
		}

		restaurant.setAutoDrawerPullEnable(chkAutoDrawerPull.isSelected());
		restaurant.setDrawerPullHour((Integer) cbHour.getSelectedItem());
		restaurant.setDrawerPullMin((Integer) cbMin.getSelectedItem());

		dao.saveOrUpdate(restaurant);
		
		JOptionPane.showMessageDialog(this, "You must restart POS for the Auto drawer pull change to take effect");
		return true;
	}
	
	@Override
	public void initialize() throws Exception {
		dao = new RestaurantDAO();
		restaurant = dao.get(Integer.valueOf(1));

		chkAutoDrawerPull.setSelected(restaurant.isAutoDrawerPullEnable());
		cbHour.setSelectedItem(restaurant.getDrawerPullHour());
		cbMin.setSelectedItem(restaurant.getDrawerPullMin());
		
		setInitialized(true);
	}
	
	@Override
	public String getName() {
		return "Auto Drawer Pull Configuration";
	}
}
