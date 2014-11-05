package com.floreantpos.config.ui;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.VirtualPrinter;
import com.floreantpos.model.dao.VirtualPrinterDAO;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class AddPrinterDialog extends POSDialog {
	VirtualPrinter printer;
	private FixedLengthTextField tfName;
	private JComboBox cbDevice;

	public AddPrinterDialog() throws HeadlessException {
		super(BackOfficeWindow.getInstance(), true);
		setTitle("Add Printer");
	}

	@Override
	public void initUI() {
		getContentPane().setLayout(new MigLayout("", "[][grow]", "[][][][][][][][][][grow]"));
		
		JLabel lblName = new JLabel("Name");
		getContentPane().add(lblName, "cell 0 0,alignx trailing");
		
		tfName = new FixedLengthTextField(60);
		getContentPane().add(tfName, "cell 1 0,growx");
		
		JLabel lblDevice = new JLabel("Device");
		getContentPane().add(lblDevice, "cell 0 1,alignx trailing");
		
		cbDevice = new JComboBox();
		cbDevice.setModel(new DefaultComboBoxModel(PrintServiceLookup.lookupPrintServices(null, null)));
		cbDevice.setRenderer(new PrintServiceComboRenderer());
		getContentPane().add(cbDevice, "cell 1 1,growx");
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, "cell 0 9 2 1,grow");
		
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
			}
		});
		panel.add(btnCancel);
	}

	protected void doAddPrinter() {
		String name = tfName.getText();
		if (StringUtils.isEmpty(name)) {
			POSMessageDialog.showMessage(this, "Please provide a name");
			return;
		}
		
		if(VirtualPrinterDAO.getInstance().findPrinterByName(name) != null) {
			POSMessageDialog.showMessage(this, "A printer with that name already exists.");
			return;
		}
		
		PrintService printService = (PrintService) cbDevice.getSelectedItem();
		if(printService == null) {
			POSMessageDialog.showMessage(this, "Please select a device.");
			return;
		}
		
		VirtualPrinter vp = new VirtualPrinter();
		vp.setName(name);
		vp.setDeviceName(printService.getName());
	}

}
