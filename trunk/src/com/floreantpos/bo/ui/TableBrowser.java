package com.floreantpos.bo.ui;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import com.floreantpos.model.ShopTable;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.swing.BeanTableModel;
import com.floreantpos.ui.forms.ShopTableForm;

public class TableBrowser extends ModelBrowser<ShopTable> {

	public TableBrowser() {
		super(new ShopTableForm());
		
		BeanTableModel<ShopTable> tableModel = new BeanTableModel<ShopTable>(ShopTable.class);
		tableModel.addColumn("TABLE NUMBER", ShopTable.PROP_ID);
		tableModel.addColumn("CAPACITY", ShopTable.PROP_CAPACITY);
		tableModel.addColumn("NAME", ShopTable.PROP_NAME);
		
		
		init(tableModel);
		
		browserTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		setColumnWidth(0, 120);
		setColumnWidth(1, 120);
	}

	@Override
	public void refreshTable() {
		List<ShopTable> tables = ShopTableDAO.getInstance().findAll();
		BeanTableModel tableModel = (BeanTableModel) browserTable.getModel();
		tableModel.removeAll();
		tableModel.addRows(tables);
	}
	
	private void setColumnWidth(int columnNumber, int width) {
		TableColumn column = browserTable.getColumnModel().getColumn(columnNumber);

		column.setPreferredWidth(width);
		column.setMaxWidth(width);
		column.setMinWidth(width);
	}
}
