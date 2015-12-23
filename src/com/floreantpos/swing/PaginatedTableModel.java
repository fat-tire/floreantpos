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
package com.floreantpos.swing;

import java.util.List;


public abstract class PaginatedTableModel extends ListTableModel {

	private int numRows;
	private int currentRowIndex;
	private int pageSize = 100;

	public PaginatedTableModel() {
		super();
	}

	public PaginatedTableModel(String[] columnNames, List rows) {
		super(columnNames, rows);
	}

	public PaginatedTableModel(String[] columnNames) {
		super(columnNames);
	}

	public int getNumRows() {
		return numRows;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public int getCurrentRowIndex() {
		return currentRowIndex;
	}

	public void setCurrentRowIndex(int currentRowIndex) {
		this.currentRowIndex = currentRowIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public boolean hasNext() {
		return (currentRowIndex + pageSize) < numRows;
	}

	public boolean hasPrevious() {
		return currentRowIndex > 0;
	}

	public int getNextRowIndex() {
		if(numRows == 0) {
			return 0;
		}
		
		return getCurrentRowIndex() + getPageSize();
	}

	public int getPreviousRowIndex() {
		int i = getCurrentRowIndex() - getPageSize();
		if(i < 0) {
			i = 0;
		}
		
		return i;
	}

}
