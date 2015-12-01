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

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public abstract class ListTableModel<E> extends AbstractTableModel {
	protected String[] columnNames;
	protected List<E> rows;
	
	public ListTableModel() {
		super();
	}

	public ListTableModel(String[] names) {
		columnNames = names;
	}

	public ListTableModel(String[] names, List<E> rows) {
		columnNames = names;
		this.rows = rows;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	public List<E> getRows() {
		return rows;
	}

	public void setRows(List<E> rows) {
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
	
	public E getRowData(int row) {
		return rows.get(row);
	}

	public void addItem(E data) {
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
