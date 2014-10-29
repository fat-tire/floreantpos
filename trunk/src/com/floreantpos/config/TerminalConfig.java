package com.floreantpos.config;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.floreantpos.util.PasswordHasher;

public class TerminalConfig {
	private static final String TOUCH_FONT_SIZE = "TOUCH_FONT_SIZE";//$NON-NLS-1$

	private static final String TOUCH_BUTTON_HEIGHT = "TOUCH_BUTTON_HEIGHT";//$NON-NLS-1$

	private static final String BAR_TAB_ENABLE = "BarTab_enable";//$NON-NLS-1$

	private static final String DRIVE_THRU_ENABLE = "DriveThru_enable";//$NON-NLS-1$

	private static final String HOME_DELIVERY_ENABLE = "HomeDelivery_enable";//$NON-NLS-1$

	private static final String TAKE_OUT_ENABLE = "TakeOut_enable";//$NON-NLS-1$

	private static final String PICKUP_ENABLE = "Pickup_enable";//$NON-NLS-1$

	private static final String DINE_IN_ENABLE = "DineIn_enable";//$NON-NLS-1$
	
	private static final String ADMIN_PASSWORD = "admin_pass";//$NON-NLS-1$

	static final String TERMINAL_ID = "terminal_id"; //$NON-NLS-1$
	static final String FULLSCREEN_MODE = "fullscreen_mode"; //$NON-NLS-1$
	
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
		config.setProperty(DINE_IN_ENABLE, enable);
	}
	
	public static boolean isPickupEnable() {
		return config.getBoolean(PICKUP_ENABLE, true);
	}
	
	public static void setPickupEnable(boolean enable) {
		config.setProperty(PICKUP_ENABLE, enable);
	}
	
	public static boolean isTakeOutEnable() {
		return config.getBoolean(TAKE_OUT_ENABLE, true);
	}
	
	public static void setTakeOutEnable(boolean enable) {
		config.setProperty(TAKE_OUT_ENABLE, enable);
	}
	
	public static boolean isHomeDeliveryEnable() {
		return config.getBoolean(HOME_DELIVERY_ENABLE, true);
	}
	
	public static void setHomeDeliveryEnable(boolean enable) {
		config.setProperty(HOME_DELIVERY_ENABLE, enable);
	}
	
	public static boolean isDriveThruEnable() {
		return config.getBoolean(DRIVE_THRU_ENABLE, true);
	}
	
	public static void setDriveThruEnable(boolean enable) {
		config.setProperty(DRIVE_THRU_ENABLE, enable);
	}
	
	public static boolean isBarTabEnable() {
		return config.getBoolean(BAR_TAB_ENABLE, true);
	}
	
	public static void setBarTabEnable(boolean enable) {
		config.setProperty(BAR_TAB_ENABLE, enable);
	}
	
	public static boolean isFullscreenMode() {
		return config.getBoolean(FULLSCREEN_MODE, false);
	}
	
	public static void setFullscreenMode(boolean fullscreen) {
		config.setProperty(FULLSCREEN_MODE, fullscreen);
	}
	
	public static String getAdminPassword() {
		return config.getString(ADMIN_PASSWORD, PasswordHasher.hashPassword("1111"));
	}
	
	public static void setAdminPassword(String password) {
		config.setProperty(ADMIN_PASSWORD, PasswordHasher.hashPassword(password));
	}

	public static boolean matchAdminPassword(String password) {
		return getAdminPassword().equals(PasswordHasher.hashPassword(password));
	}
	
	public static void setTouchScreenButtonHeight(int height) {
		config.setProperty(TOUCH_BUTTON_HEIGHT, height);
	}
	
	public static int getTouchScreenButtonHeight() {
		return config.getInt(TOUCH_BUTTON_HEIGHT, 45);
	}
	
	public static void setTouchScreenFontSize(int size) {
		config.setProperty(TOUCH_FONT_SIZE, size);
	}
	
	public static int getTouchScreenFontSize() {
		return config.getInt(TOUCH_FONT_SIZE, 12);
	}
}
