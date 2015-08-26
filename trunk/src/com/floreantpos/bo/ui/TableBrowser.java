package com.floreantpos.bo.ui;

import java.util.List;

import com.floreantpos.model.ShopTable;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.swing.BeanTableModel;
import com.floreantpos.ui.forms.ShopTableForm;

public class TableBrowser extends ModelBrowser<ShopTable> {

	public TableBrowser() {
		super(new ShopTableForm());
		
		BeanTableModel<ShopTable> tableModel = new BeanTableModel<ShopTable>(ShopTable.class);
		tableModel.addColumn("TABLE NUMBER", ShopTable.PROP_TABLE_NUMBER);
		tableModel.addColumn("NAME", ShopTable.PROP_NAME);
		tableModel.addColumn("CAPACITY", ShopTable.PROP_CAPACITY);
		
		init(tableModel);
	}

	@Override
	public void refreshTable() {
		List<ShopTable> tables = ShopTableDAO.getInstance().findAll();
		BeanTableModel tableModel = (BeanTableModel) browserTable.getModel();
		tableModel.removeAll();
		tableModel.addRows(tables);
	}
}
