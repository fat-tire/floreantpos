package com.floreantpos.customPayment;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import com.floreantpos.Messages;
import com.floreantpos.bo.ui.ModelBrowser;
import com.floreantpos.model.CustomPayment;
import com.floreantpos.model.dao.CustomPaymentDAO;
import com.floreantpos.swing.BeanTableModel;

public class CustomPaymentBrowser extends ModelBrowser<CustomPayment> {

	public CustomPaymentBrowser() {
		super(new CustomPaymentForm());

		BeanTableModel<CustomPayment> tableModel = new BeanTableModel<CustomPayment>(CustomPayment.class);

		tableModel.addColumn(Messages.getString("CustomPaymentBrowser.0"), CustomPayment.PROP_ID); //$NON-NLS-1$
		tableModel.addColumn(Messages.getString("CustomPaymentBrowser.1"), CustomPayment.PROP_NAME); //$NON-NLS-1$
		tableModel.addColumn(Messages.getString("CustomPaymentBrowser.2"), CustomPayment.PROP_REF_NUMBER_FIELD_NAME); //$NON-NLS-1$
		init(tableModel);

		browserTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		//setColumnWidth(0, 120);
		//setColumnWidth(1, 120);
	}

	@Override
	public void refreshTable() {
		List<CustomPayment> tables = CustomPaymentDAO.getInstance().findAll();
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
