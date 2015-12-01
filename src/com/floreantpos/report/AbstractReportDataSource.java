/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.report;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public abstract class AbstractReportDataSource extends AbstractTableModel {
	protected String[] columnNames;
	protected List rows;
	
	public AbstractReportDataSource() {
		super();
	}
	
	public AbstractReportDataSource(String[] columnNames, List rows) {
		super();
		this.columnNames = columnNames;
		this.rows = rows;
	}

	public AbstractReportDataSource(List rows) {
		super();
		this.rows = rows;
	}

	public AbstractReportDataSource(String[] columnNames) {
		super();
		this.columnNames = columnNames;
	}

	public int getRowCount() {
		if(rows == null) {
			return 0;
		}
		return rows.size();
	}

	public int getColumnCount() {
		if(columnNames == null) {
			return 0;
		}
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

}
