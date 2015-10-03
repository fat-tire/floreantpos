package com.floreantpos.bo.ui;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import com.floreantpos.Messages;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.swing.BeanTableModel;
import com.floreantpos.ui.forms.ShopTableForm;

public class TableBrowser extends ModelBrowser<ShopTable> {

	public TableBrowser() {
		super(new ShopTableForm());
		
		BeanTableModel<ShopTable> tableModel = new BeanTableModel<ShopTable>(ShopTable.class);
		tableModel.addColumn(Messages.getString("TableBrowser.0"), ShopTable.PROP_ID); //$NON-NLS-1$
		tableModel.addColumn(Messages.getString("TableBrowser.1"), ShopTable.PROP_CAPACITY); //$NON-NLS-1$
		tableModel.addColumn(Messages.getString("TableBrowser.2"), ShopTable.PROP_NAME); //$NON-NLS-1$
		
		
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
