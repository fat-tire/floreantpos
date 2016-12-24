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

	public static void debug(Class eClass, String msg) {
		factory.getInstance(eClass).debug(msg);
	}

	public static void info(Class eClass, String msg) {
		factory.getInstance(eClass).info(msg);
	}
}
