package com.floreantpos.bo.ui;

import com.floreantpos.model.ShopTable;
import com.floreantpos.swing.BeanTableModel;
import com.floreantpos.ui.forms.ShopTableForm;

public class TableBrowser extends ModelBrowser<ShopTable> {

	public TableBrowser() {
		super(new ShopTableForm());
		
		BeanTableModel<ShopTable> tableModel = new BeanTableModel<ShopTable>(ShopTable.class);
		tableModel.addColumn("TABLE NUMBER", "tableNumber");
		tableModel.addColumn("NAME", "name");
		
		init(tableModel);
	}

}
