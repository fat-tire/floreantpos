package com.floreantpos.table;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableColumn;


import com.floreantpos.model.ShopTable;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.swing.BeanTableModel;

public class ShopTableBrowser extends ShopTableModelBrowser<ShopTable> {

	public ShopTableBrowser() {
		super(new ShopTableForm());
		
		BeanTableModel<ShopTable> tableModel = new BeanTableModel<ShopTable>(ShopTable.class);
		
		tableModel.addColumn("Table No", ShopTable.PROP_ID);  //$NON-NLS-1$
		tableModel.addColumn("Capacity", ShopTable.PROP_CAPACITY); //$NON-NLS-1$
		tableModel.addColumn("Description", ShopTable.PROP_DESCRIPTION); //$NON-NLS-1$
		
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
