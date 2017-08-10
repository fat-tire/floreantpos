package com.floreantpos.util;

import java.util.Random;

public class NumericGlobalIdGenerator extends GlobalIdGenerator {
	@Override
	public String generate() {
		return generateGlobalId();
	}
	
	public static String generateGlobalId() {
		long currentTimeMillis = System.currentTimeMillis();
		Random random = new Random();
		for (int i = 0; i < 3; i++) {
			currentTimeMillis += random.nextInt();
		}
		
		return String.valueOf(currentTimeMillis);
	}
}
