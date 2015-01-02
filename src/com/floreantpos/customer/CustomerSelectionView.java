package com.floreantpos.customer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.main.Application;
import com.floreantpos.model.Customer;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.swing.PosSmallButton;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.forms.CustomerForm;

public class CustomerSelectionView extends JPanel {

	private PosSmallButton btnCreateNewCustomer;

	private CustomerTable customerTable;
	private POSTextField tfPhone;
	private POSTextField tfLoyaltyNo;
	private POSTextField tfName;
	private PosSmallButton btnInfo;

	protected Customer selectedCustomer;
	
	public CustomerSelectionView() {
		createUI();
	}

	private void createUI() {
		setPreferredSize(new Dimension(690, 553));
		setLayout(new MigLayout("", "[549px,grow]", "[grow][][shrink 0,fill][grow][grow]"));

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "OroCust 0.7", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panel_4, "cell 0 0,grow");
		panel_4.setLayout(new MigLayout("", "[grow][][][]", "[grow][][][]"));

		JLabel lblNewLabel = new JLabel("");
		panel_4.add(lblNewLabel, "cell 0 0 1 3,grow");

		JLabel lblByPhone = new JLabel("Search by Phone");
		panel_4.add(lblByPhone, "cell 1 0");

		tfPhone = new POSTextField();
		panel_4.add(tfPhone, "cell 2 0");
		tfPhone.setColumns(16);

		PosSmallButton psmlbtnSearch = new PosSmallButton();
		panel_4.add(psmlbtnSearch, "cell 3 0 1 3,growy");
		psmlbtnSearch.setFocusable(false);
		psmlbtnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchCustomer();
			}
		});
		psmlbtnSearch.setText("SEARCH");

		JLabel lblByName = new JLabel("Or Loyalty #");
		panel_4.add(lblByName, "cell 1 1,alignx trailing");

		tfLoyaltyNo = new POSTextField();
		panel_4.add(tfLoyaltyNo, "cell 2 1");
		tfLoyaltyNo.setColumns(16);

		JLabel lblByEmail = new JLabel("Or Name");
		panel_4.add(lblByEmail, "cell 1 2,alignx trailing");

		tfName = new POSTextField();
		panel_4.add(tfName, "cell 2 2");
		tfName.setColumns(16);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EmptyBorder(10, 0, 0, 0));
		panel_4.add(panel_2, "cell 0 3 4 1,growx");
		panel_2.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setFocusable(false);
		panel_2.add(scrollPane, BorderLayout.CENTER);

		customerTable = new CustomerTable();
		customerTable.setModel(new CustomerListTableModel());
		customerTable.setFocusable(false);
		customerTable.setRowHeight(35);
		customerTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		customerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				selectedCustomer = customerTable.getSelectedCustomer();
				if (selectedCustomer != null) {
//					btnInfo.setEnabled(true);
				}
				else {
					btnInfo.setEnabled(false);
				}
			}
		});
		scrollPane.setViewportView(customerTable);

		JPanel panel = new JPanel();
		panel_2.add(panel, BorderLayout.SOUTH);

		btnInfo = new PosSmallButton();
		btnInfo.setFocusable(false);
		panel.add(btnInfo);
		btnInfo.setEnabled(false);
		btnInfo.setText("DETAIL");

		PosSmallButton btnHistory = new PosSmallButton();
		btnHistory.setEnabled(false);
		btnHistory.setText("HISTORY");
		panel.add(btnHistory);

		btnCreateNewCustomer = new PosSmallButton();
		btnCreateNewCustomer.setFocusable(false);
		panel.add(btnCreateNewCustomer);
		btnCreateNewCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createNewCustomer();
			}
		});
		btnCreateNewCustomer.setText("NEW");

		PosSmallButton btnCancel = new PosSmallButton();
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnCancel.setText("CANCEL");
		panel.add(btnCancel);

		PosSmallButton btnSelect = new PosSmallButton();
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnSelect.setText("SELECT");
		panel.add(btnSelect);

		JPanel panel_3 = new JPanel(new BorderLayout());
		add(panel_3, "cell 0 1,grow, gapright 2px");

		com.floreantpos.swing.QwertyKeyPad qwertyKeyPad = new com.floreantpos.swing.QwertyKeyPad();
		panel_3.add(qwertyKeyPad);
		tfName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchCustomer();
			}
		});
		tfLoyaltyNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchCustomer();
			}
		});
		tfPhone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchCustomer();
			}
		});
	}

	protected void searchCustomer() {
		String phone = tfPhone.getText();
		String name = tfName.getText();
		String loyalty = tfLoyaltyNo.getText();

		if (StringUtils.isEmpty(phone) && StringUtils.isEmpty(loyalty) && StringUtils.isEmpty(name)) {
			List<Customer> list = CustomerDAO.getInstance().findAll();
			customerTable.setModel(new CustomerListTableModel(list));
			return;
		}

		List<Customer> list = CustomerDAO.getInstance().findBy(phone, loyalty, name);
		customerTable.setModel(new CustomerListTableModel(list));
	}

	protected void createNewCustomer() {
		CustomerForm form = new CustomerForm();
		BeanEditorDialog dialog = new BeanEditorDialog(form, Application.getPosWindow(), true);
		dialog.open();

		if (!dialog.isCanceled()) {
			selectedCustomer = (Customer) form.getBean();
		}
	}

	public Customer getCustomer() {
		return selectedCustomer;
	}

	@Override
	public String getName() {
		return "C";
	}
}
