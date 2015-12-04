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
package com.floreantpos.model.dao;

import java.util.Date;

import com.floreantpos.model.DataUpdateInfo;

public class DataUpdateInfoDAO extends BaseDataUpdateInfoDAO {
	private static DataUpdateInfo lastUpdateInfo;

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public DataUpdateInfoDAO() {
	}

	public static synchronized DataUpdateInfo getLastUpdateInfo() {
		if (lastUpdateInfo != null) {
			try {
				getInstance().refresh(lastUpdateInfo);
			} catch (Exception x) {
				lastUpdateInfo = null;
			}
			
			return lastUpdateInfo;
		}

		lastUpdateInfo = getInstance().get(1);

		if (lastUpdateInfo == null) {
			lastUpdateInfo = new DataUpdateInfo();
			lastUpdateInfo.setLastUpdateTime(new Date());

			getInstance().save(lastUpdateInfo);
		}

		return lastUpdateInfo;
	}

}