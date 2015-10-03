package com.floreantpos.customer;

import java.util.List;

import com.floreantpos.Messages;
import com.floreantpos.model.Customer;
import com.floreantpos.swing.ListTableModel;

public class CustomerListTableModel extends ListTableModel<Customer> {
	private final static String[] columns = { Messages.getString("CustomerListTableModel.0"), Messages.getString("CustomerListTableModel.1"), Messages.getString("CustomerListTableModel.2"), Messages.getString("CustomerListTableModel.3"), Messages.getString("CustomerListTableModel.4"), Messages.getString("CustomerListTableModel.5") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

	public CustomerListTableModel() {
		super(columns);
	}

	public CustomerListTableModel(List<Customer> customers) {
		super(columns, customers);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		Customer customer = getRowData(rowIndex);

		switch (columnIndex) {
			case 0:
				return customer.getTelephoneNo();
			case 1:
				return customer.getName();

			case 2:
				return customer.getDob();
				
			case 3:
				return customer.getAddress();
				
			case 4:
				return customer.getCity();
				
			case 5:
				return customer.getState();

		}
		return null;
	}
}
