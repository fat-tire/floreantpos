package com.floreantpos.swing;

import java.util.List;


public abstract class PaginatedTableModel extends ListTableModel {

	private int numRows;
	private int currentRowIndex;
	private int pageSize = 10;

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
