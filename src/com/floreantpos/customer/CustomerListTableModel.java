package com.floreantpos.customer;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.floreantpos.model.Customer;

public class CustomerListTableModel extends AbstractTableModel {
	private final String[] columns = { "PHONE", "NAME", "DoB", "ADDRESS", "CITY", "STATE" };

	private List<Customer> customers;

	public CustomerListTableModel() {
	}

	public CustomerListTableModel(List<Customer> customers) {
		this.customers = customers;
	}

	@Override
	public int getRowCount() {
		if (customers == null) {
			return 0;
		}

		return customers.size();
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}
	
	@Override
	public String getColumnName(int column) {
		return columns[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (customers == null) {
			return null;
		}

		Customer customer = customers.get(rowIndex);

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

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}
	
	public Customer getCustomer(int index) {
		if(customers == null) {
			return null;
		}
		
		if(index < 0 || index >= customers.size()) {
			return null;
		}
		
		return customers.get(index);
	}

}
