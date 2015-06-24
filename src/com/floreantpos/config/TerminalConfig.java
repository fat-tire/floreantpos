package com.floreantpos.config;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.floreantpos.model.OrderTypeFilter;
import com.floreantpos.model.PaymentStatusFilter;
import com.floreantpos.util.PasswordHasher;

public class TerminalConfig {
	private static final String USE_SETTLEMENT_PROMPT = "UseSettlementPrompt";

	private static final String SHOW_GUEST_SELECTION = "show_guest_selection";

	private static final String ORDER_TYPE_FILTER = "order_type_filter";

	private static final String PS_FILTER = "ps_filter";

	private static final String SHOW_TABLE_SELECTION = "show_table_selection";

	private static final String REGULAR_MODE = "regular_mode";

	private static final String KITCHEN_MODE = "kitchen_mode";

	private static final String CASHIER_MODE = "cashier_mode";

	private static final String SHOW_DB_CONFIGURATION = "show_db_configuration";

	private static final String UI_DEFAULT_FONT = "ui_default_font";

	private static final String AUTO_LOGOFF_TIME = "AUTO_LOGOFF_TIME";

	private static final String AUTO_LOGOFF_ENABLE = "AUTO_LOGOFF_ENABLE";

	private static final String DEFAULT_PASS_LEN = "DEFAULT_PASS_LEN";

	private static final String TOUCH_FONT_SIZE = "TOUCH_FONT_SIZE";//$NON-NLS-1$

	private static final String TOUCH_BUTTON_HEIGHT = "TOUCH_BUTTON_HEIGHT";//$NON-NLS-1$

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
		return config.getInt(TOUCH_BUTTON_HEIGHT, 80);
	}
	
	public static void setTouchScreenFontSize(int size) {
		config.setProperty(TOUCH_FONT_SIZE, size);
	}
	
	public static int getTouchScreenFontSize() {
		return config.getInt(TOUCH_FONT_SIZE, 12);
	}

	public static void setDefaultPassLen(int defaultPassLen) {
		config.setProperty(DEFAULT_PASS_LEN, defaultPassLen);
	}
	
	public static int getDefaultPassLen() {
		return config.getInt(DEFAULT_PASS_LEN, 4);
	}
	
	public static boolean isAutoLogoffEnable() {
		return config.getBoolean(AUTO_LOGOFF_ENABLE, true);
	}
	
	public static void setAutoLogoffEnable(boolean enable) {
		config.setProperty(AUTO_LOGOFF_ENABLE, enable);
	}
	
	public static void setAutoLogoffTime(int time) {
		config.setProperty(AUTO_LOGOFF_TIME, time);
	}
	
	public static int getAutoLogoffTime() {
		return config.getInt(AUTO_LOGOFF_TIME, 60);
	}
	
	public static String getUiDefaultFont() {
		return config.getString(UI_DEFAULT_FONT);
	}
	
	public static void setUiDefaultFont(String fontName) {
		config.setProperty(UI_DEFAULT_FONT, fontName);
	}
	
	public static void setShowDbConfigureButton(boolean show) {
		config.setProperty(SHOW_DB_CONFIGURATION, show);
	}
	
	public static boolean isShowDbConfigureButton() {
		return config.getBoolean(SHOW_DB_CONFIGURATION, true);
	}
	
	public static boolean isCashierMode() {
		return config.getBoolean(CASHIER_MODE, false);
	}
	
	public static void setCashierMode(boolean cashierMode) {
		config.setProperty(CASHIER_MODE, cashierMode);
	}
	
	public static boolean isRegularMode() {
		return config.getBoolean(REGULAR_MODE, false);
	}
	
	public static void setRegularMode(boolean regularMode) {
		config.setProperty(REGULAR_MODE, regularMode);
	}
	
	public static boolean isKitchenMode() {
		return config.getBoolean(KITCHEN_MODE, false);
	}
	
	public static void setKitchenMode(boolean kitchenMode) {
		config.setProperty(KITCHEN_MODE, kitchenMode);
	}
	
	public static boolean isUseTranslatedName() {
		return config.getBoolean("use_translated_name", false);
	}
	
	public static void setUseTranslatedName(boolean useTranslatedName) {
		config.setProperty("use_translated_name", useTranslatedName);
	}
	
	public static OrderTypeFilter getOrderTypeFilter() {
		return OrderTypeFilter.fromString(config.getString(ORDER_TYPE_FILTER));
	}
	
	public static void setOrderTypeFilter(String filter) {
		config.setProperty(ORDER_TYPE_FILTER, filter);
	}
	
	public static PaymentStatusFilter getPaymentStatusFilter() {
		return PaymentStatusFilter.fromString(config.getString(PS_FILTER));
	}
	
	public static void setPaymentStatusFilter(String filter) {
		config.setProperty(PS_FILTER, filter);
	}
	
	public static void setShouldShowTableSelection(boolean showTableSelection) {
		config.setProperty(SHOW_TABLE_SELECTION, Boolean.valueOf(showTableSelection));
	}
	
	public static boolean isShouldShowTableSelection() {
		return config.getBoolean(SHOW_TABLE_SELECTION, Boolean.TRUE);
	}
	
	public static void setShouldShowGuestSelection(boolean showGuestSelection) {
		config.setProperty(SHOW_GUEST_SELECTION, Boolean.valueOf(showGuestSelection));
	}
	
	public static boolean isShouldShowGuestSelection() {
		return config.getBoolean(SHOW_GUEST_SELECTION, Boolean.TRUE);
	}
	
	public static void setUseSettlementPrompt(boolean settlementPrompt) {
		config.setProperty(USE_SETTLEMENT_PROMPT, Boolean.valueOf(settlementPrompt));
	}
	
	public static boolean isUseSettlementPrompt() {
		return config.getBoolean(USE_SETTLEMENT_PROMPT, Boolean.FALSE);
	}
}
