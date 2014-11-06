package com.floreantpos.config.ui;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
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
import javax.swing.JSeparator;

public class VirtualPrinterConfigDialog extends POSDialog {
	private VirtualPrinter printer;
	private FixedLengthTextField tfName;

	public VirtualPrinterConfigDialog() throws HeadlessException {
		super(BackOfficeWindow.getInstance(), true);
		setTitle("Add/Edit Virtual Printer");
		
		setSize(400, 150);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	@Override
	public void initUI() {
		getContentPane().setLayout(new MigLayout("", "[][grow]", "[][][]"));
		
		JLabel lblName = new JLabel("Name");
		getContentPane().add(lblName, "cell 0 0,alignx trailing");
		
		tfName = new FixedLengthTextField(60);
		getContentPane().add(tfName, "cell 1 0,growx");
		
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
