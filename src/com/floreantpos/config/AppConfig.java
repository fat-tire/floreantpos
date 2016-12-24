/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.config;

import java.io.File;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.floreantpos.Database;
import com.floreantpos.PosLog;

public class AppConfig {
	
	public static final String DATABASE_URL = "database_url"; //$NON-NLS-1$
	public static final String DATABASE_PORT = "database_port"; //$NON-NLS-1$
	public static final String DATABASE_NAME = "database_name"; //$NON-NLS-1$
	public static final String DATABASE_USER = "database_user"; //$NON-NLS-1$
	public static final String DATABASE_PASSWORD = "database_pass"; //$NON-NLS-1$
	public static final String CONNECTION_STRING = "connection_string"; //$NON-NLS-1$
	public static final String DATABASE_PROVIDER_NAME = "database_provider_name"; //$NON-NLS-1$
	
	private static final String KITCHEN_PRINT_ON_ORDER_SETTLE = "kitchen_print_on_order_settle"; //$NON-NLS-1$
	private static final String KITCHEN_PRINT_ON_ORDER_FINISH = "kitchen_print_on_order_finish"; //$NON-NLS-1$
	private static final String PRINT_RECEIPT_ON_ORDER_SETTLE = "print_receipt_on_order_settle"; //$NON-NLS-1$
	private static final String PRINT_RECEIPT_ON_ORDER_FINISH = "print_receipt_on_order_finish"; //$NON-NLS-1$
	
	private static PropertiesConfiguration config;
	
	static {
		try {
			//File workingDir = Application.getWorkingDir();
			File configFile = new File("floreantpos.config.properties"); //$NON-NLS-1$
			if(!configFile.exists()) {
				configFile.createNewFile();
			}
			
			config = new PropertiesConfiguration(configFile);
			config.setAutoSave(true);

		} catch (Exception e) {
			PosLog.error(AppConfig.class, e.getMessage());
		}
	}
	
	public static PropertiesConfiguration getConfig() {
		return config;
	}
	
	public static boolean getBoolean(String key, boolean defaultValue) {
		return config.getBoolean(key, defaultValue);
	}
	
	public static int getInt(String key, int defaultValue) {
		return config.getInt(key, defaultValue);
	}
	
	public static void putInt(String key, int value) {
		config.setProperty(key, value);
	}
	
	public static String getString(String key) {
		return config.getString(key, null);
	}
	
	public static String getString(String key, String defaultValue) {
		return config.getString(key, defaultValue);
	}
	
	public static void put(String key, boolean value) {
		config.setProperty(key, value);
	}
	
	public static void put(String key, String value) {
		config.setProperty(key, value);
	}
	
	public static String getDatabaseHost() {
		return config.getString(DATABASE_URL, "localhost"); //$NON-NLS-1$
	}
	
	public static void setDatabaseHost(String url) {
		config.setProperty(DATABASE_URL, url);
	}

	public static String getConnectString() {
		return config.getString(CONNECTION_STRING, Database.DERBY_SINGLE.getConnectString("", "", ""));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	public static void setConnectString(String connectionString) {
		config.setProperty(CONNECTION_STRING, connectionString);
	}
	
	public static String getDatabasePort() {
		return config.getString(DATABASE_PORT, null);
	}
	
	public static void setDatabasePort(String port) {
		config.setProperty(DATABASE_PORT, port);
	}
	
	public static String getDatabaseName() {
		return config.getString(DATABASE_NAME, "posdb"); //$NON-NLS-1$
	}
	
	public static void setDatabaseName(String name) {
		config.setProperty(DATABASE_NAME, name);
	}
	
	public static String getDatabaseUser() {
		return config.getString(DATABASE_USER, "app"); //$NON-NLS-1$
	}
	
	public static void setDatabaseUser(String user) {
		config.setProperty(DATABASE_USER, user);
	}
	
	public static String getDatabasePassword() {
		return config.getString(DATABASE_PASSWORD, "sa"); //$NON-NLS-1$
	}
	
	public static void setDatabasePassword(String password) {
		config.setProperty(DATABASE_PASSWORD, password);
	}
	
	public static void setDatabaseProviderName(String databaseProviderName) {
		config.setProperty(DATABASE_PROVIDER_NAME, databaseProviderName);
	}
	
	public static String getDatabaseProviderName() {
		return config.getString(DATABASE_PROVIDER_NAME, Database.DERBY_SINGLE.getProviderName()); //$NON-NLS-1$
	}
	
	public static Database getDefaultDatabase() {
		return Database.getByProviderName(getDatabaseProviderName());
	}
	
	public static boolean isPrintReceiptOnOrderFinish() {
		return getBoolean(PRINT_RECEIPT_ON_ORDER_FINISH, false);
	}
	
	public static void setPrintReceiptOnOrderFinish(boolean print) {
		config.setProperty(PRINT_RECEIPT_ON_ORDER_FINISH, print);
	}
	
	public static boolean isPrintReceiptOnOrderSettle() {
		return getBoolean(PRINT_RECEIPT_ON_ORDER_SETTLE, false);
	}
	
	public static void setPrintReceiptOnOrderSettle(boolean print) {
		config.setProperty(PRINT_RECEIPT_ON_ORDER_SETTLE, print);
	}
	
	public static boolean isPrintToKitchenOnOrderFinish() {
		return getBoolean(KITCHEN_PRINT_ON_ORDER_FINISH, false);
	}
	
	public static void setPrintToKitchenOnOrderFinish(boolean print) {
		config.setProperty(KITCHEN_PRINT_ON_ORDER_FINISH, print);
	}
	
	public static boolean isPrintToKitchenOnOrderSettle() {
		return getBoolean(KITCHEN_PRINT_ON_ORDER_SETTLE, false);
	}
	
	public static void setPrintToKitchenOnOrderSettle(boolean print) {
		config.setProperty(KITCHEN_PRINT_ON_ORDER_SETTLE, print);
	}
}
