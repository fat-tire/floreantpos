package com.floreantpos.util;


public class POSUtil {
	public static boolean isBlankOrNull(String str) {
		if(str == null) {
			return true;
		}
		if(str.trim().equals("")) {
			return true;
		}
		return false;
	}
	
	public static String escapePropertyKey(String propertyKey) {
		return propertyKey.replaceAll("\\s+", "_");
	}
	
	public static boolean getBoolean(Boolean b) {
		if(b == null) {
			return false;
		}
		
		return b;
	}
}
