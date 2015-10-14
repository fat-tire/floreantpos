package com.floreantpos.swing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


/**
 * A list from which multiple items can be selected.
 */
public class CheckBoxList<E> extends JTable {

	public CheckBoxList() {
	}

	public CheckBoxList(E[] items) {
		setModel(items);
	}

	public CheckBoxList(List<E> items) {
		setModel(items);
	}

	public void setModel(E[] items) {
		setModel(new CheckBoxListModel(items));
		init();
	}

	public void setModel(List<E> items) {
		setModel(new CheckBoxListModel<E>(items));
		init();
	}

	public List<E> getCheckedValues() {
		List values = new ArrayList();
		CheckBoxListModel model = (CheckBoxListModel) getModel();
		for (int i = 0; i < model.items.size(); i++) {
			CheckBoxList.Entry<E> entry = (Entry<E>) model.items.get(i);
			if (entry.checked) {
				values.add(entry.value);
			}
		}

		return values;
	}

	//{{{ selectAll() method
	@Override
	public void selectAll() {
		CheckBoxListModel model = (CheckBoxListModel) getModel();
		for (int i = 0; i < model.items.size(); i++) {
			Entry entry = (Entry) model.items.get(i);
			entry.checked = true;

		}

		model.fireTableRowsUpdated(0, model.getRowCount());
	}

	public void selectItems(List types) {
		CheckBoxListModel model = (CheckBoxListModel) getModel();

		if (types != null) {
			for (int i = 0; i < model.items.size(); i++) {
				Entry entry = (Entry) model.items.get(i);

				for (int j = 0; j < types.size(); j++) {

					Object type = types.get(j);

					if (type.equals(entry.value)) {
						entry.checked = true;
						break;

					}

				}
			}

			model.fireTableRowsUpdated(0, model.getRowCount());

		}
	}

	public void unCheckAll() {
		CheckBoxListModel model = (CheckBoxListModel) getModel();
		for (int i = 0; i < model.items.size(); i++) {
			Entry entry = (Entry) model.items.get(i);
			entry.checked = false;

		}

		model.fireTableRowsUpdated(0, model.getRowCount());
	}

	public Entry[] getValues() {
		CheckBoxListModel model = (CheckBoxListModel) getModel();
		return (Entry[]) model.items.toArray();
	}

	public Object getSelectedValue() {
		int row = getSelectedRow();
		if (row == -1) {
			return null;
		}
		else {
			return getModel().getValueAt(row, 1);
		}
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		return super.getCellRenderer(row, column);
	}

	private void init() {
		getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setShowGrid(false);
		setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		TableColumn column = getColumnModel().getColumn(0);
		int checkBoxWidth = new JCheckBox().getPreferredSize().width;
		column.setPreferredWidth(checkBoxWidth);
		column.setMinWidth(checkBoxWidth);
		column.setWidth(checkBoxWidth);
		column.setMaxWidth(checkBoxWidth);
		column.setResizable(false);

		setTableHeader(null);
	}

	/**
	 * A check box list entry.
	 */
	public static class Entry<E> {
		boolean checked;
		E value;

		public Entry(boolean checked, E value) {
			this.checked = checked;
			this.value = value;
		}

		public boolean isChecked() {

			return checked;
		}

		public Object getValue() {

			return value;
		}

		public void setChecked(boolean checked) {

			this.checked = checked;
		}

		public void setValue(E value) {

			this.value = value;
		}

	}

	public static class CheckBoxListModel<E> extends AbstractTableModel {
		List<CheckBoxList.Entry<E>> items;

		CheckBoxListModel(List<E> _items) {
			items = new ArrayList<CheckBoxList.Entry<E>>(_items.size());
			for (int i = 0; i < _items.size(); i++) {
				items.add(createEntry(_items.get(i)));
			}
		}

		CheckBoxListModel(E[] _items) {
			items = new ArrayList<CheckBoxList.Entry<E>>(_items.length);
			for (int i = 0; i < _items.length; i++) {
				items.add(createEntry(_items[i]));
			}
		}

		private CheckBoxList.Entry createEntry(E obj) {
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
			return col == 0;
		}

		@Override
		public void setValueAt(Object value, int row, int col) {
			if (col == 0) {
				CheckBoxList.Entry entry = items.get(row);
				entry.checked = (value.equals(Boolean.TRUE));
				
				fireTableRowsUpdated(row, row);
			}
		}
		
		
		

		public List<CheckBoxList.Entry<E>> getItems() {
			
			return items;
		}

		public void setItems(List<CheckBoxList.Entry<E>> items) {
			this.items = items;
		}

	}
}