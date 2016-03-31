package com.floreantpos.model.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.floreantpos.ui.forms.QuickCustomerForm;

public class ZipCodeUtil {

	private static Log logger = LogFactory.getLog(ZipCodeUtil.class);

	private static HashMap<String, ZipCodeMap> zipCodeCache = new HashMap<String, ZipCodeMap>();

	private static boolean isInitialize;

	public static String getCity(String zipCode) {

		if (!isInitialize) {
			initialize();
		}

		if (!isZipCodeMap(zipCode)) {
			return null;
		}

		String city = zipCodeCache.get(zipCode).getCity();
		return city;
	}

	public static String getState(String zipCode) {

		if (!isInitialize) {
			initialize();
		}

		if (!isZipCodeMap(zipCode)) {
			return null;
		}

		String state = zipCodeCache.get(zipCode).getState();
		return state;
	}

	private static boolean isZipCodeMap(String zipCode) {

		ZipCodeMap zipCodeMap = zipCodeCache.get(zipCode);

		if (zipCodeMap == null) {
			return false;
		}
		return true;
	}

	private static void initialize() {

		InputStream inputStream = null;
		try {

			inputStream = QuickCustomerForm.class.getResourceAsStream("/Zipcodes/US.txt"); //$NON-NLS-1$
			List<String> lines = IOUtils.readLines(inputStream);

			for (String line : lines) {
				String str[] = line.split(","); //$NON-NLS-1$

				String zipCode = str[0];
				String state = str[1];
				String city = str[2];

				ZipCodeMap zMap = new ZipCodeMap();

				zMap.setState(state);
				zMap.setCity(city);

				zipCodeCache.put(zipCode, zMap);
			}
			isInitialize = true;
		} catch (Exception e2) {
			logger.error(e2);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
}
