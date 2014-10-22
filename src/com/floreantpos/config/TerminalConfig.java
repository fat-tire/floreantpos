package com.floreantpos.config;

import org.apache.commons.configuration.PropertiesConfiguration;

public class TerminalConfig {
	private static final String BAR_TAB_ENABLE = "BarTab_enable";

	private static final String DRIVE_THRU_ENABLE = "DriveThru_enable";

	private static final String HOME_DELIVERY_ENABLE = "HomeDelivery_enable";

	private static final String TAKE_OUT_ENABLE = "TakeOut_enable";

	private static final String PICKUP_ENABLE = "Pickup_enable";

	private static final String DINE_IN_ENABLE = "DineIn_enable";

	static final String TERMINAL_ID = "terminal_id"; //$NON-NLS-1$
	
	private static PropertiesConfiguration config = AppConfig.getConfig();

	public static int getTerminalId() {
		return config.getInt(TERMINAL_ID, -1);
	}

	public static void setTerminalId(int id) {
		config.setProperty(TERMINAL_ID, id);
	}
	
	public static boolean isDineInEnable() {
		return config.getBoolean(DINE_IN_ENABLE, true);
	}
	
	public static void setDineInEnable(boolean enable) {
		config.addProperty(DINE_IN_ENABLE, enable);
	}
	
	public static boolean isPickupEnable() {
		return config.getBoolean(PICKUP_ENABLE, true);
	}
	
	public static void setPickupEnable(boolean enable) {
		config.addProperty(PICKUP_ENABLE, enable);
	}
	
	public static boolean isTakeOutEnable() {
		return config.getBoolean(TAKE_OUT_ENABLE, true);
	}
	
	public static void setTakeOutEnable(boolean enable) {
		config.addProperty(TAKE_OUT_ENABLE, enable);
	}
	
	public static boolean isHomeDeliveryEnable() {
		return config.getBoolean(HOME_DELIVERY_ENABLE, true);
	}
	
	public static void setHomeDeliveryEnable(boolean enable) {
		config.addProperty(HOME_DELIVERY_ENABLE, enable);
	}
	
	public static boolean isDriveThruEnable() {
		return config.getBoolean(DRIVE_THRU_ENABLE, true);
	}
	
	public static void setDriveThruEnable(boolean enable) {
		config.addProperty(DRIVE_THRU_ENABLE, enable);
	}
	
	public static boolean isBarTabEnable() {
		return config.getBoolean(BAR_TAB_ENABLE, true);
	}
	
	public static void setBarTabEnable(boolean enable) {
		config.addProperty(BAR_TAB_ENABLE, enable);
	}
	
}
