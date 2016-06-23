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
package com.floreantpos.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.model.GlobalConfig;
import com.floreantpos.model.dao.GlobalConfigDAO;

public class GlobalConfigUtil {
	private static List<GlobalConfig> globalConfigList;

	public static void populateGlobalConfig() {
		globalConfigList = new ArrayList<GlobalConfig>();
		List<GlobalConfig> list = GlobalConfigDAO.getInstance().findAll();
		if (list != null) {
			globalConfigList.addAll(list);
		}
	}

	public static GlobalConfig get(String key) {
		if (globalConfigList == null) {
			return null;
		}

		for (GlobalConfig config : globalConfigList) {
			if (key.equals(config.getKey())) {
				return config;
			}
		}

		return null;
	}

	public static String getValue(String key) {
		if (globalConfigList == null) {
			return null;
		}

		for (GlobalConfig config : globalConfigList) {
			if (key.equals(config.getKey())) {
				return config.getValue();
			}
		}

		return null;
	}

	public static String getMapApiKey() {
		String mapApiKey = getValue(GlobalConfig.MAP_API_KEY);
		if (StringUtils.isEmpty(mapApiKey)) {
			return "AIzaSyDc-5LFTSC-bB9kQcZkM74LHUxwndRy_XM";
		}
		return mapApiKey;
	}
}
