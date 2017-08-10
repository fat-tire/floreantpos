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

import org.apache.commons.configuration.PropertiesConfiguration;

import com.floreantpos.PosLog;

public class AppProperties {
	
	private static PropertiesConfiguration properties;
	
	static {
		try {
			properties = new PropertiesConfiguration(AppProperties.class.getResource("/app.properties")); //$NON-NLS-1$
		} catch (Exception e) {
			PosLog.error(AppProperties.class, e.getMessage());
		}
	}
	
	public static String getVersion() {
		return properties.getString("floreantpos.version"); //$NON-NLS-1$
		
	}
	
	public static String getAppName() {
		return properties.getString("app.name"); //$NON-NLS-1$
	}
	
	public static String getAppVersion() {
		return properties.getString("app.version"); //$NON-NLS-1$
	}
}
