package com.floreantpos.util;

import com.floreantpos.PosException;

public class POSUtil {
	public static boolean isBlankOrNull(String str) {
		if (str == null) {
			return true;
		}
		if (str.trim().equals("")) {
			return true;
		}
		return false;
	}

	public static String escapePropertyKey(String propertyKey) {
		return propertyKey.replaceAll("\\s+", "_");
	}

	public static boolean getBoolean(Boolean b) {
		if (b == null) {
			return false;
		}

		return b;
	}

	public static double getDouble(Double d) {
		if (d == null) {
			return 0;
		}

		return d;
	}
	
	public static double getInteger(Integer d) {
		if (d == null) {
			return 0;
		}
		
		return d;
	}

	public static int parseInteger(String s, String parseErrorMessage) {
		try {
			return Integer.parseInt(s);
		} catch (Exception x) {
			throw new PosException(parseErrorMessage);
		}
	}
	
	public static double parseDouble(String s, String parseErrorMessage, boolean mandatory) {
		try {
			return Double.parseDouble(s);
		} catch (Exception x) {
			if(mandatory) {
				throw new PosException(parseErrorMessage);
			}
			else {
				return 0;
			}
		}
	}
}
