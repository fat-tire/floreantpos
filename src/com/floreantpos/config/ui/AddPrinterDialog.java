package com.floreantpos.config.ui;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import com.floreantpos.Messages;
import com.floreantpos.model.Printer;
import com.floreantpos.model.VirtualPrinter;
import com.floreantpos.model.dao.VirtualPrinterDAO;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;

public class AddPrinterDialog extends POSDialog {
	private Printer printer;
	private JComboBox cbVirtualPrinter;
	private JComboBox cbDevice;
	private JCheckBox chckbxDefault;

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
		getContentPane().setLayout(new MigLayout("", "[][grow][]", "[][][][][grow]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		JLabel lblName = new JLabel(Messages.getString("AddPrinterDialog.4")); //$NON-NLS-1$
		getContentPane().add(lblName, "cell 0 0,alignx trailing"); //$NON-NLS-1$
		
		cbVirtualPrinter = new JComboBox();
		List<VirtualPrinter> virtualPrinters = VirtualPrinterDAO.getInstance().findAll();
		cbVirtualPrinter.setModel(new DefaultComboBoxModel<VirtualPrinter>(virtualPrinters.toArray(new VirtualPrinter[0])));
		getContentPane().add(cbVirtualPrinter, "cell 1 0,growx"); //$NON-NLS-1$
		
		JButton btnNew = new JButton(Messages.getString("AddPrinterDialog.7")); //$NON-NLS-1$
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAddNewVirtualPrinter();
			}
		});
		getContentPane().add(btnNew, "cell 2 0"); //$NON-NLS-1$
		
		JLabel lblDevice = new JLabel(Messages.getString("AddPrinterDialog.9")); //$NON-NLS-1$
		getContentPane().add(lblDevice, "cell 0 1,alignx trailing"); //$NON-NLS-1$
		
		cbDevice = new JComboBox();
		cbDevice.setModel(new DefaultComboBoxModel(PrintServiceLookup.lookupPrintServices(null, null)));
		cbDevice.setRenderer(new PrintServiceComboRenderer());
		getContentPane().add(cbDevice, "cell 1 1,growx"); //$NON-NLS-1$
		
		chckbxDefault = new JCheckBox(Messages.getString("AddPrinterDialog.12")); //$NON-NLS-1$
		getContentPane().add(chckbxDefault, "cell 1 2"); //$NON-NLS-1$
		
		JSeparator separator = new JSeparator();
		getContentPane().add(separator, "cell 0 3 3 1,growx,gapy 50px"); //$NON-NLS-1$
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, "cell 0 4 3 1,grow"); //$NON-NLS-1$
		
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
		VirtualPrinter vp = (VirtualPrinter) cbVirtualPrinter.getSelectedItem();
		if (vp == null) {
			POSMessageDialog.showError(this, Messages.getString("AddPrinterDialog.18")); //$NON-NLS-1$
			return;
		}
		
		PrintService printService = (PrintService) cbDevice.getSelectedItem();
		if(printService == null) {
			POSMessageDialog.showMessage(this, Messages.getString("AddPrinterDialog.19")); //$NON-NLS-1$
			return;
		}
		
		boolean defaultPrinter = chckbxDefault.isSelected();
		
		if(printer == null) {
			printer = new Printer();
		}
		printer.setVirtualPrinter(vp);
		printer.setDeviceName(printService.getName());
		printer.setDefaultPrinter(defaultPrinter);
		
		setCanceled(false);
		dispose();
	}

	public Printer getPrinter() {
		return printer;
	}

	public void setPrinter(Printer printer) {
		this.printer = printer;
		
		if (printer != null) {
			cbVirtualPrinter.setSelectedItem(printer.getVirtualPrinter());
			chckbxDefault.setSelected(printer.isDefaultPrinter());
			
			DefaultComboBoxModel<PrintService> deviceModel = (DefaultComboBoxModel<PrintService>) cbDevice.getModel();
			for(int i = 0; i < deviceModel.getSize(); i++) {
				PrintService printService = deviceModel.getElementAt(i);
				if(printService.getName().equals(printer.getDeviceName())) {
					cbDevice.setSelectedIndex(i);
					break;
				}
			}
		}
	}

}
