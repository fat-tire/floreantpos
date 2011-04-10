package com.floreantpos.bo.ui.explorer;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public abstract class ListTableModel extends AbstractTableModel {
	protected String[] columnNames;
	protected List rows;
	
	public ListTableModel() {
		super();
	}

	public ListTableModel(String[] names) {
		columnNames = names;
	}

	public ListTableModel(String[] names, List rows) {
		super();
		columnNames = names;
		this.rows = rows;
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
		fireTableDataChanged();
	}

	public int getRowCount() {
		if(rows == null) return 0;
		
		return rows.size();
	}

	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
	
	public Object getRowData(int row) {
		return rows.get(row);
	}

	public void addItem(Object data) {
		if(rows == null) {
			rows = new ArrayList();
		}
		
		int size = rows.size();
		rows.add(data);
		
		fireTableRowsInserted(size, size);
	}
	
	public void deleteItem(int index) {
		rows.remove(index);
		fireTableRowsDeleted(index, index);
	}
	
	public boolean deleteItem(Object item) {
		return rows.remove(item);
	}
	
	public void updateItem(int index) {
		fireTableRowsUpdated(index, index);
	}
}
