package com.floreantpos.config;

import org.apache.commons.configuration.PropertiesConfiguration;

public class AppProperties {
	
	private static PropertiesConfiguration properties;
	
	static {
		try {
			properties = new PropertiesConfiguration(AppProperties.class.getResource("/app.properties")); //$NON-NLS-1$
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getVersion() {
		return properties.getString("floreantpos.version"); //$NON-NLS-1$
		
	}
}
