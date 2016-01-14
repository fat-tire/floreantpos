package com.floreantpos.swing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.swing.CheckBoxList;

/**
 * A list from which multiple items can be selected.
 */
public class ItemCheckBoxList<E> extends CheckBoxList {

	public ItemCheckBoxList() {
	}
	
	@Override
	public void setModel(List items) {
		setModel(new CheckBoxListModel<E>(items));
		init();
	}
	
	@Override
	public List getCheckedValues() {
		List values = new ArrayList();
		CheckBoxListModel model = (CheckBoxListModel) getModel();
		for (int i = 0; i < model.items.size(); i++) {
			CheckBoxList.Entry<E> entry = (Entry<E>) model.items.get(i);
			if(entry.checked) {
				values.add(entry.value);
			}
		}
		return values;
	}
	
	public List getAllValues() {
		List values = new ArrayList();
		CheckBoxListModel model = (CheckBoxListModel) getModel();
		for (int i = 0; i < model.items.size(); i++) {
			CheckBoxList.Entry<E> entry = (Entry<E>) model.items.get(i);
				values.add(entry.value);
		}
		return values;
	}
	
	@Override
	public void unCheckAll() {
		CheckBoxListModel model = (CheckBoxListModel) getModel();
		for (int i = 0; i < model.items.size(); i++) {
			Entry entry = (Entry) model.items.get(i);
			entry.checked = false;

		}

		model.fireTableRowsUpdated(0, model.getRowCount());
	}
	
	public void setSelected(Object type) {
		CheckBoxListModel model = (CheckBoxListModel) getModel();

		if(type != null) {
			for (int i = 0; i < model.items.size(); i++) {
				Entry entry = (Entry) model.items.get(i);
					if(type.equals(entry.value)) {
						entry.checked = true;
						break;
					}
			}
			model.fireTableRowsUpdated(0, model.getRowCount());
		}
		
	}
	
	@Override
	public void selectItems(List types) {
		CheckBoxListModel model = (CheckBoxListModel) getModel();

		if(types != null) {
			for (int i = 0; i < model.items.size(); i++) {
				Entry entry = (Entry) model.items.get(i);

				for (int j = 0; j < types.size(); j++) {

					Object type = types.get(j);

					if(type.equals(entry.value)) {
						entry.checked = true;
						break;
					}
				}
			}
			model.fireTableRowsUpdated(0, model.getRowCount());
		}
	}
	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {

		if(column == 0) {
			return super.getCellRenderer(row, column);
		}
		DefaultTableCellRenderer center = new DefaultTableCellRenderer();
		center.setHorizontalAlignment(JLabel.LEFT);
		this.getColumnModel().getColumn(column).setCellRenderer(center);
		return super.getCellRenderer(row, column);
	}

	public void init() {
		getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setShowGrid(true);
		setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		TableColumn column = getColumnModel().getColumn(0);
		int checkBoxWidth = new JCheckBox().getPreferredSize().width;
		column.setPreferredWidth(checkBoxWidth);
		column.setMinWidth(checkBoxWidth);
		column.setWidth(checkBoxWidth);
		column.setMaxWidth(checkBoxWidth);
		column.setResizable(false);
	}

	public static class CheckBoxListModel<E> extends CheckBoxList.CheckBoxListModel<E> {
		List<CheckBoxList.Entry<E>> items;

		CheckBoxListModel(List<E> _items) {
			super(_items);
			items = new ArrayList<CheckBoxList.Entry<E>>(_items.size());
			for (int i = 0; i < _items.size(); i++) {
				items.add(createEntry(_items.get(i)));
			}
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public String getColumnName(int col) {

			switch (col) {
				case 0:
				return "ALL";
				
				case 1:
					return "Name";

				default:
					return null;
			}
		}

		@Override
		public Object getValueAt(int row, int col) {
			CheckBoxList.Entry entry = items.get(row);
			switch (col) {
				case 0:
					return Boolean.valueOf(entry.checked);
					
				case 1:
					if(entry.value instanceof MenuItem){
						return ((MenuItem) entry.value).getName();
					}else if(entry.value instanceof MenuGroup){
						return ((MenuGroup) entry.value).getName();
					}else if(entry.value instanceof MenuCategory){
						return ((MenuCategory) entry.value).getName();
					}
					return entry.value; 
				default:
					throw new InternalError();
			}
		}

		@Override
		public void setValueAt(Object value, int row, int col) {
			if(col == 0) {
				CheckBoxList.Entry entry = items.get(row);
				entry.checked = (value.equals(Boolean.TRUE));

				fireTableRowsUpdated(row, row);
			}
		}

	}
}