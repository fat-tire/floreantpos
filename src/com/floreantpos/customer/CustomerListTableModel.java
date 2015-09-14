package com.floreantpos.customer;

import java.util.List;

import com.floreantpos.model.Customer;
import com.floreantpos.swing.ListTableModel;

public class CustomerListTableModel extends ListTableModel<Customer> {
	private final static String[] columns = { "PHONE", "NAME", "DoB", "ADDRESS", "CITY", "STATE" };

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
