package com.floreantpos.config;

import java.util.prefs.Preferences;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.floreantpos.main.Application;

public class ApplicationConfig {
	public static final String DATABASE_URL = "DATABASE_URL";
	public static final String DATABASE_PORT = "DATABASE_PORT";
	public static final String DATABASE_NAME = "DATABASE_NAME";
	public static final String DATABASE_USER = "DATABASE_USER";
	public static final String DATABASE_PASSWORD = "DATABASE_PASS";
	public static final String CONNECTION_STRING = "CONNECTION_STRING";
	public static final String HIBERNATE_DIALECT = "hibernate.dialect";
	public static final String HIBERNATE_CONNECTION_DRIVER_CLASS = "hibernate.connection.driver_class";
	public static final String DATABASE_PROVIDER_NAME = "DATABASE_PROVIDER_NAME";
	
	private final static Preferences pref = Preferences.userNodeForPackage(Application.class);
	
	private static PropertiesConfiguration configuration;
	
	static {
		try {
			configuration = new PropertiesConfiguration(ApplicationConfig.class.getResource("/floreantpos.properties"));
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static int getTerminalId() {
		return pref.getInt("TERMINAL_ID", -1);
	}
	
	public static void setTerminalId(int id) {
		pref.putInt("TERMINAL_ID", id);
	}

	public static Preferences getPreferences() {
		return pref;
	}
	
	public static boolean getBoolean(String key, boolean defaultValue) {
		return pref.getBoolean(key, defaultValue);
	}
	
	public static String getString(String key) {
		return pref.get(key, null);
	}
	
	public static String getString(String key, String defaultValue) {
		return pref.get(key, defaultValue);
	}
	
	public static void put(String key, boolean value) {
		pref.putBoolean(key, value);
	}
	
	public static void put(String key, String value) {
		pref.put(key, value);
	}
	
	public static String getDatabaseURL() {
		return pref.get(DATABASE_URL, "localhost");
	}

	public static String getConnectString() {
		return pref.get(CONNECTION_STRING, ""); 
	}
	
	public static void setConnectString(String connectionString) {
		pref.put(CONNECTION_STRING, connectionString);
	}
	
	public static void setDatabaseURL(String url) {
		pref.put(DATABASE_URL, url);
	}
	
	public static String getDatabasePort() {
		return pref.get(DATABASE_PORT, null);
	}
	
	public static void setDatabasePort(String port) {
		pref.put(DATABASE_PORT, port);
	}
	
	public static String getDatabaseName() {
		return pref.get(DATABASE_NAME, "posdb");
	}
	
	public static void setDatabaseName(String name) {
		pref.put(DATABASE_NAME, name);
	}
	
	public static String getDatabaseUser() {
		return pref.get(DATABASE_USER, "app");
	}
	
	public static void setDatabaseUser(String user) {
		pref.put(DATABASE_USER, user);
	}
	
	public static String getDatabasePassword() {
		return pref.get(DATABASE_PASSWORD, "sa");
	}
	
	public static void setDatabasePassword(String password) {
		pref.put(DATABASE_PASSWORD, password);
	}
	
	public static void setHibernateDialect(String dialect) {
		pref.put(HIBERNATE_DIALECT, dialect);
	}
	
	public static String getHibernateDialect() {
		return pref.get(HIBERNATE_DIALECT, "");
	}
	
	public static void setHibernateConnectionDriverClass(String driverClass) {
		pref.put(HIBERNATE_CONNECTION_DRIVER_CLASS, driverClass);
	}
	
	public static String getHibernateConnectionDriverClass() {
		return pref.get(HIBERNATE_CONNECTION_DRIVER_CLASS, "");
	}
	
	public static void setDatabaseProviderName(String databaseProviderName) {
		pref.put(DATABASE_PROVIDER_NAME, databaseProviderName);
	}
	
	public static String getDatabaseProviderName() {
		return pref.get(DATABASE_PROVIDER_NAME, "");
	}
	
	public static PropertiesConfiguration getConfiguration() {
		return configuration;
	}
}
