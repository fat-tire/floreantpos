package com.floreantpos.util;

import java.util.UUID;
import java.util.prefs.Preferences;

import org.apache.commons.lang.StringUtils;

import com.sun.accessibility.internal.resources.accessibility;

public class TerminalUtil {
	private static final String FLUID = "a$@d55#";
	private static String uid;

	static {
		Preferences preferences = Preferences.userNodeForPackage(accessibility.class);
		uid = preferences.get(FLUID, null);

		if (StringUtils.isEmpty(uid)) {
			uid = UUID.randomUUID().toString();
			preferences.put(FLUID, uid);
		}
	}

	public static String getSystemUID() {
		return uid;
	}
}
