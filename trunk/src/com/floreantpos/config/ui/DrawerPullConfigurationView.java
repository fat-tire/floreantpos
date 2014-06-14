package com.floreantpos.config.ui;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.RestaurantDAO;

public class DrawerPullConfigurationView extends ConfigurationView {
	private RestaurantDAO dao;
	private Restaurant restaurant;
	
	JComboBox cbHour;
	JComboBox cbMin;
	JCheckBox chkAutoDrawerPull;
	
	public DrawerPullConfigurationView() {
		setLayout(new MigLayout("align 50% 50%")); //$NON-NLS-1$
		
		Integer[] hours = new Integer[24];
		Integer[] minutes = new Integer[60];
		
		for(int i = 0; i < 24; i++) {
			hours[i] = Integer.valueOf(i);
		}
		for(int i = 0; i < 60; i++) {
			minutes[i] = Integer.valueOf(i);
		}
		
		add(chkAutoDrawerPull = new JCheckBox(com.floreantpos.POSConstants.AUTO_DRAWER_PULL_EVERY_DAY_AT_), "wrap, span"); //$NON-NLS-1$
		add(new JLabel(com.floreantpos.POSConstants.HOUR + ":"), ""); //$NON-NLS-1$ //$NON-NLS-2$
		add(cbHour = new JComboBox(hours), ""); //$NON-NLS-1$
		
		add(new JLabel(Messages.getString("DrawerPullConfigurationView.MINUTE") + ":"), ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		add(cbMin = new JComboBox(minutes), ""); //$NON-NLS-1$
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
		
		JOptionPane.showMessageDialog(this, Messages.getString("DrawerPullConfigurationView.RESTART_MESSAGE")); //$NON-NLS-1$
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
		return Messages.getString("DrawerPullConfigurationView.DRAWER_PULL_CONFIG"); //$NON-NLS-1$
	}
}
