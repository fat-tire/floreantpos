package com.floreantpos.model.dao;

public class HibernateProjectionsUtil {

	public static int getInt(Object[] array, int index) {
		if (array.length < (index + 1))
			return 0;
	
		Number number = (Number) array[index];
		if (number == null)
			return 0;
	
		return number.intValue();
	}

	public static double getDouble(Object[] array, int index) {
		if (array.length < (index + 1))
			return 0;
	
		Number number = (Number) array[index];
		if (number == null)
			return 0;
	
		return number.doubleValue();
	}

}
