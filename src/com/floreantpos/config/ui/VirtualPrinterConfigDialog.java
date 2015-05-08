package com.floreantpos.config.ui;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.model.VirtualPrinter;
import com.floreantpos.model.dao.VirtualPrinterDAO;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class VirtualPrinterConfigDialog extends POSDialog {
	private VirtualPrinter printer;
	private FixedLengthTextField tfName;

//	private JCheckBox cbDineIn = new JCheckBox(OrderType.DINE_IN.toString());
//	private JCheckBox cbTakeOut = new JCheckBox(OrderType.TAKE_OUT.toString());
//	private JCheckBox cbPickup = new JCheckBox(OrderType.PICKUP.toString());
//	private JCheckBox cbHomeDeli = new JCheckBox(OrderType.HOME_DELIVERY.toString());
//	private JCheckBox cbDriveThru = new JCheckBox(OrderType.DRIVE_THRU.toString());
//	private JCheckBox cbBarTab = new JCheckBox(OrderType.BAR_TAB.toString());

	public VirtualPrinterConfigDialog() throws HeadlessException {
		super();
		setTitle("Add/Edit Virtual Printer");
		
		init();

		pack();
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public void init() {
		getContentPane().setLayout(new MigLayout("", "[][grow]", "[][][]"));

		JLabel lblName = new JLabel("Virtual Printer Name");
		getContentPane().add(lblName, "cell 0 0,alignx trailing");

		tfName = new FixedLengthTextField(60);
		getContentPane().add(tfName, "cell 1 0,growx");

//		JPanel orderTypePanel = new JPanel();
//		orderTypePanel.setBorder(new TitledBorder("ORDER TYPE"));
//		orderTypePanel.add(cbDineIn);
//		orderTypePanel.add(cbTakeOut);
//		orderTypePanel.add(cbPickup);
//		orderTypePanel.add(cbHomeDeli);
//		orderTypePanel.add(cbDriveThru);
//		orderTypePanel.add(cbBarTab);
//		add(orderTypePanel, "newline, grow, span 2");

		JSeparator separator = new JSeparator();
		getContentPane().add(separator, "cell 0 1 2 1,growx, gap top 50px");

		JPanel panel = new JPanel();
		getContentPane().add(panel, "cell 0 4 2 1,grow");

		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAddPrinter();
			}
		});
		panel.add(btnOk);

		JButton btnCancel = new JButton("CANCEL");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		panel.add(btnCancel);
	}

	protected void doAddPrinter() {
		try {

			String name = tfName.getText();
			if (StringUtils.isEmpty(name)) {
				POSMessageDialog.showMessage(this, "Please provide a name");
				return;
			}

			VirtualPrinterDAO printerDAO = VirtualPrinterDAO.getInstance();

			if (printerDAO.findPrinterByName(name) != null) {
				POSMessageDialog.showMessage(this, "A printer with that name already exists.");
				return;
			}

			if (printer == null) {
				printer = new VirtualPrinter();
			}

			printer.setName(name);
			
//			List<String> orderTypeNames = new ArrayList<String>();
//			
//			if(cbDineIn.isSelected()) {
//				orderTypeNames.add(OrderType.DINE_IN.name());
//			}
//			if(cbTakeOut.isSelected()) {
//				orderTypeNames.add(OrderType.TAKE_OUT.name());
//			}
//			if(cbPickup.isSelected()) {
//				orderTypeNames.add(OrderType.PICKUP.name());
//			}
//			if(cbHomeDeli.isSelected()) {
//				orderTypeNames.add(OrderType.HOME_DELIVERY.name());
//			}
//			if(cbDriveThru.isSelected()) {
//				orderTypeNames.add(OrderType.DRIVE_THRU.name());
//			}
//			if(cbBarTab.isSelected()) {
//				orderTypeNames.add(OrderType.BAR_TAB.name());
//			}
			
//			printer.setOrderTypeNames(orderTypeNames);

			printerDAO.saveOrUpdate(printer);

			setCanceled(false);
			dispose();

		} catch (Exception e) {
			POSMessageDialog.showError(this, e.getMessage(), e);
		}
	}

	public VirtualPrinter getPrinter() {
		return printer;
	}

	public void setPrinter(VirtualPrinter printer) {
		this.printer = printer;

		if (printer != null) {
			tfName.setText(printer.getName());
		}
	}
}
