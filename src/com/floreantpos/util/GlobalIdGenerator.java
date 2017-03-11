package com.floreantpos.util;

import java.util.Random;

public final class GlobalIdGenerator {
	public static String generate() {
		long currentTimeMillis = System.currentTimeMillis();
		Random random = new Random();
		for (int i = 0; i < 3; i++) {
			currentTimeMillis += random.nextInt();
		}
		String idString = String.valueOf(currentTimeMillis);
		int length = idString.length();
		if (length == 16) {
			return idString;
		}
		else if (length > 16) {
			return idString.substring(0, 16);
		}
		for (int i = 0; i < (16 - length); i++) {
			char c = (char)(random.nextInt(26) + 'a');
			idString = c + idString;
		}
		
		return idString;
	}
}
