package com.floreantpos;

import org.apache.commons.logging.LogFactory;

public class PosLog {
	private static LogFactory factory;

	static {
		factory = LogFactory.getFactory();
	}

	public static void error(Class eClass, String errMsg) {
		factory.getInstance(eClass).error(errMsg);
	}
	
	public static void error(Class eClass, Exception e) {
		factory.getInstance(eClass).error(e);
	}
	
	public static void error(Class eClass, String message, Exception e) {
		factory.getInstance(eClass).error(message, e);
	}

	public static void debug(Class eClass, String msg) {
		factory.getInstance(eClass).debug(msg);
	}

	public static void info(Class eClass, String msg) {
		factory.getInstance(eClass).info(msg);
	}
}
