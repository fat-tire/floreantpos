package com.floreantpos.util;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

public class GlobalIdGenerator implements IdentifierGenerator {
	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		Class<? extends Object> clazz = object.getClass();
		Serializable generatedId = null;
		try {
			Method method = clazz.getMethod("getId", (Class<?>[]) null);
			if (method != null) {
				Object id = method.invoke(object, (Object[]) null);
				if (id != null) {
					generatedId = (Serializable) id;
				}
			}
			
		} catch (Exception e) {
		}
		if (generatedId == null) {
			generatedId = generate();
		}
		try {
			//set short id if applicable
			Method methodGetShortId = clazz.getMethod("getShortId", (Class<?>[]) null);
			if (methodGetShortId != null) {
				Object shortId = methodGetShortId.invoke(object, (Object[]) null);
				if (shortId == null) {
					shortId = RandomStringUtils.randomNumeric(7);
					Method methodSetShortId = clazz.getMethod("setShortId", String.class);
					methodSetShortId.invoke(object, shortId);
				}
			}
			//set short id if applicable
			Method methodGetBarcode = clazz.getMethod("getBarcode", (Class<?>[]) null);
			if (methodGetBarcode != null) {
				String barcode = (String) methodGetBarcode.invoke(object, (Object[]) null);
				if (StringUtils.isEmpty(barcode)) {
					barcode = generatedId.toString();
					Method methodSetBarcode = clazz.getMethod("setBarcode", String.class);
					methodSetBarcode.invoke(object, barcode);
				}
			}
		} catch (Exception e) {
		}
		
		return generatedId;
	}
	
	public String generate() {
		return generateGlobalId();
	}
	
	public static String generateGlobalId() {
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
