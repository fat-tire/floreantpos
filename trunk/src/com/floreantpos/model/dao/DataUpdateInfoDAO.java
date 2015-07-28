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