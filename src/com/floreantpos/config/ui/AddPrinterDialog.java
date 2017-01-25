/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.config.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.model.Printer;
import com.floreantpos.model.VirtualPrinter;
import com.floreantpos.model.dao.VirtualPrinterDAO;
import com.floreantpos.swing.ComboBoxModel;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;

public class AddPrinterDialog extends POSDialog {
	private static final String DO_NOT_PRINT = "Do not print";
	
	private Printer printer;
	
	private VirtualPrinter virtualPrinter;
	private FixedLengthTextField tfName;
	
	private JComboBox cbVirtualPrinter;
	private JComboBox cbDevice;
	private JCheckBox chckbxDefault;
	
	TitlePanel titlePanel; 

	public AddPrinterDialog() throws HeadlessException {
		super(POSUtil.getBackOfficeWindow(), true);
		
		setTitle(Messages.getString("AddPrinterDialog.0")); //$NON-NLS-1$
		
		setMinimumSize(new Dimension(400, 200));
		//setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
	}

	@Override
	public void initUI() {
		setLayout(new BorderLayout(5,5)); 
		JPanel centerPanel=new JPanel(); 
		centerPanel.setLayout(new MigLayout("", "[][grow][]", "[][][][][grow]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		titlePanel = new TitlePanel();
		titlePanel.setTitle("Printer Type:");//$NON-NLS-1$
		add(titlePanel, BorderLayout.NORTH);
		
		JLabel lblName = new JLabel("Virtual Printer Name : "); //$NON-NLS-1$
		centerPanel.add(lblName, "cell 0 0,alignx trailing"); //$NON-NLS-1$
		
		tfName = new FixedLengthTextField(20);
		
		cbVirtualPrinter = new JComboBox();
		List<VirtualPrinter> virtualPrinters = VirtualPrinterDAO.getInstance().findAll();
		cbVirtualPrinter.setModel(new DefaultComboBoxModel<VirtualPrinter>(virtualPrinters.toArray(new VirtualPrinter[0])));
		//centerPanel.add(cbVirtualPrinter, "cell 1 0,growx"); //$NON-NLS-1$
		
		centerPanel.add(tfName, "cell 1 0,growx"); //$NON-NLS-1$
		
		JButton btnNew = new JButton(Messages.getString("AddPrinterDialog.7")); //$NON-NLS-1$
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAddNewVirtualPrinter();
			}
		});
		//centerPanel.add(btnNew, "cell 2 0"); //$NON-NLS-1$
		
		JLabel lblDevice = new JLabel(Messages.getString("AddPrinterDialog.9")); //$NON-NLS-1$
		centerPanel.add(lblDevice, "cell 0 1,alignx trailing"); //$NON-NLS-1$
		
		cbDevice = new JComboBox();
		List printerServices = new ArrayList<>();
		printerServices.add(DO_NOT_PRINT);
		PrintService[] lookupPrintServices = PrintServiceLookup.lookupPrintServices(null, null);
		//cbDevice.setModel(new DefaultComboBoxModel(PrintServiceLookup.lookupPrintServices(null, null)));
		cbDevice.addItem(null);
		for (int i = 0; i < lookupPrintServices.length; i++) {
			printerServices.add(lookupPrintServices[i]);
		}
		cbDevice.setModel(new ComboBoxModel(printerServices));

		
		cbDevice.setRenderer(new PrintServiceComboRenderer());
		centerPanel.add(cbDevice, "cell 1 1,growx"); //$NON-NLS-1$
		
		chckbxDefault = new JCheckBox(Messages.getString("AddPrinterDialog.12")); //$NON-NLS-1$
		centerPanel.add(chckbxDefault, "cell 1 2"); //$NON-NLS-1$
		
		JSeparator separator = new JSeparator();
		centerPanel.add(separator, "cell 0 3 3 1,growx,gapy 50px"); //$NON-NLS-1$
		
		add(centerPanel, BorderLayout.CENTER); 
		
		JPanel panel = new JPanel();
		
		JButton btnOk = new JButton(Messages.getString("AddPrinterDialog.16")); //$NON-NLS-1$
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAddPrinter();
			}
		});
		panel.add(btnOk);
		
		JButton btnCancel = new JButton(Messages.getString("AddPrinterDialog.17")); //$NON-NLS-1$
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		panel.add(btnCancel);
		add(panel, BorderLayout.SOUTH); //$NON-NLS-1$
	}

	protected void doAddNewVirtualPrinter() {
		VirtualPrinterConfigDialog dialog = new VirtualPrinterConfigDialog();
		dialog.open();
		
		if(dialog.isCanceled()) {
			return;
		}
		
		VirtualPrinter virtualPrinter = dialog.getPrinter();
		DefaultComboBoxModel<VirtualPrinter> model = (DefaultComboBoxModel<VirtualPrinter>) cbVirtualPrinter.getModel();
		model.addElement(virtualPrinter);
		cbVirtualPrinter.setSelectedItem(virtualPrinter);
	}

	protected void doAddPrinter() {
		/*VirtualPrinter vp = (VirtualPrinter) cbVirtualPrinter.getSelectedItem();
		if (vp == null) {
			POSMessageDialog.showError(this, Messages.getString("AddPrinterDialog.18")); //$NON-NLS-1$
			return;
		}*/
		String name = tfName.getText();
		if (StringUtils.isEmpty(name)) {
			POSMessageDialog.showMessage(this, Messages.getString("VirtualPrinterConfigDialog.11")); //$NON-NLS-1$
			return;
		}

		if (virtualPrinter == null) {
			virtualPrinter = new VirtualPrinter();
		}
		
		virtualPrinter.setName(name);

		PrintService printService = null;
		Object selectedObject = cbDevice.getSelectedItem();
		if (selectedObject instanceof PrintService) {
			/*POSMessageDialog.showMessage(this, Messages.getString("AddPrinterDialog.19")); //$NON-NLS-1$
			return;*/
			printService = (PrintService) selectedObject;
		}
		
		boolean defaultPrinter = chckbxDefault.isSelected();
		
		if(printer == null) {
			printer = new Printer();
		}
		printer.setVirtualPrinter(virtualPrinter);
		if (printService != null && printService.getName() != null) {
			printer.setDeviceName(printService.getName());
		}
		else {
			printer.setDeviceName(null);
		}
		printer.setDefaultPrinter(defaultPrinter);
		
		setCanceled(false);
		dispose();
	}

	public Printer getPrinter() {
		return printer;
	}

	public void setPrinter(Printer printer) {
		this.printer = printer;
		this.virtualPrinter=printer.getVirtualPrinter(); 
		
		tfName.setText(printer.getVirtualPrinter().getName()); 
		if (printer != null) {
			cbVirtualPrinter.setSelectedItem(printer.getVirtualPrinter());
			chckbxDefault.setSelected(printer.isDefaultPrinter());
			
			if (printer.getDeviceName() == "No Print") {
				cbDevice.setSelectedItem(DO_NOT_PRINT);
				return;
			}
			ComboBoxModel deviceModel = (ComboBoxModel) cbDevice.getModel();
			for (int i = 0; i < deviceModel.getSize(); i++) {
				Object selectedObject = deviceModel.getElementAt(i);
				if (!(selectedObject instanceof PrintService))
					continue;
				PrintService printService = (PrintService) selectedObject;
				if (printService != null)
					if (printService.getName().equals(printer.getDeviceName())) {
						cbDevice.setSelectedIndex(i);
						break;
					}

			}
		}
	}

}