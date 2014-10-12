package com.floreantpos.report.datasource;

import javax.swing.table.TableModel;

import net.sf.jasperreports.engine.data.JRTableModelDataSource;

public class SalesReportDatasource extends JRTableModelDataSource {

	public SalesReportDatasource(TableModel arg0) {
		super(arg0);
	}

}
