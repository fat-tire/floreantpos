package com.floreantpos.test;

import net.sf.jasperreports.engine.data.JRTableModelDataSource;

public class TestDSFactory {
	public static JRTableModelDataSource createDataSource() {
		return new JRTableModelDataSource(new TestDS());
	}
}
