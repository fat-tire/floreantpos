/**
 * Copyright OROCUBE LLC
 * 
 */
package com.floreantpos.swing;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;


public class CheckBoxListModel extends AbstractTableModel {
	Vector<CheckBoxList.Entry> items;

	CheckBoxListModel(Vector _items) {
		items = new Vector<CheckBoxList.Entry>(_items.size());
		for (int i = 0; i < _items.size(); i++) {
			items.add(createEntry(_items.elementAt(i)));
		}
	}

	CheckBoxListModel(Object[] _items) {
		items = new Vector<CheckBoxList.Entry>(_items.length);
		for (int i = 0; i < _items.length; i++) {
			items.add(createEntry(_items[i]));
		}
	}

	private CheckBoxList.Entry createEntry(Object obj) {
		if (obj instanceof CheckBoxList.Entry)
			return (CheckBoxList.Entry) obj;
		else
			return new CheckBoxList.Entry(false, obj);
	}

	public int getRowCount() {
		return items.size();
	}

	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(int col) {
		return null;
	}

	public Object getValueAt(int row, int col) {
		CheckBoxList.Entry entry = items.get(row);
		switch (col) {
		case 0:
			return Boolean.valueOf(entry.checked);
		case 1:
			return entry.value;
		default:
			throw new InternalError();
		}
	}

	@Override
	public Class getColumnClass(int col) {
		switch (col) {
		case 0:
			return Boolean.class;
		case 1:
			return String.class;
		default:
			throw new InternalError();
		}
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		CheckBoxList.Entry entry = items.get(row);
		return col == 0 && !entry.caption;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		if (col == 0) {
			CheckBoxList.Entry entry = items.get(row);
			if (!entry.caption) {
				entry.checked = (value.equals(Boolean.TRUE));
				fireTableRowsUpdated(row, row);
			}
		}
	}

	public Vector<CheckBoxList.Entry> getItems() {
		return items;
	}

	public void setItems(Vector<CheckBoxList.Entry> items) {
		this.items = items;
	}
} //}}}
