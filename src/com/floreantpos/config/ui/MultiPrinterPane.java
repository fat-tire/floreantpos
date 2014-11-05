package com.floreantpos.config.ui;

import java.awt.BorderLayout;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

import com.floreantpos.model.PrinterGroup;
import com.floreantpos.model.VirtualPrinter;

public class MultiPrinterPane extends JPanel {
	private PrinterGroup printerGroup;
	private JList<VirtualPrinter> printerList = new JList<VirtualPrinter>(new DefaultListModel<VirtualPrinter>());
	
	public MultiPrinterPane(PrinterGroup printerGroup) {
		this.printerGroup = printerGroup;
		
		setBorder(BorderFactory.createTitledBorder(printerGroup.getName()));
		setLayout(new BorderLayout(10, 10));
		
		Set<VirtualPrinter> printers = printerGroup.getPrinters();
		DefaultListModel<VirtualPrinter> listModel = (DefaultListModel<VirtualPrinter>) printerList.getModel();
		
		if(printers != null) {
			for (VirtualPrinter virtualPrinter : printers) {
				listModel.addElement(virtualPrinter);
			}
		}
	}
}
