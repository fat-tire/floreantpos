/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PrintConfiguration.java
 *
 * Created on Apr 5, 2010, 4:31:47 PM
 */

package com.floreantpos.config.ui;

import java.awt.Component;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.config.ApplicationConfig;
import com.floreantpos.config.PrintConfig;
import com.floreantpos.print.PrinterType;

/**
 *
 * @author mshahriar
 */
public class PrintConfigurationView extends ConfigurationView {

	/** Creates new form PrintConfiguration */
	public PrintConfigurationView() {
		initComponents();
	}

	@Override
	public String getName() {
		return com.floreantpos.POSConstants.PRINT_CONFIGURATION;
	}

	@Override
	public void initialize() throws Exception {
		PrinterType[] values = PrinterType.values();
//		cbReceiptPrinterType.setModel(new DefaultComboBoxModel(values));
//		cbKitchenPrinterType.setModel(new DefaultComboBoxModel(values));

		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
		cbReceiptPrinterName.setModel(new DefaultComboBoxModel(printServices));
		cbKitchenPrinterName.setModel(new DefaultComboBoxModel(printServices));
		
		PrintServiceComboRenderer comboRenderer = new PrintServiceComboRenderer();
		cbReceiptPrinterName.setRenderer(comboRenderer);
		cbKitchenPrinterName.setRenderer(comboRenderer);
		
//		cbReceiptPrinterType.setSelectedItem(PrinterType.fromString(ApplicationConfig.getString(PrintConfig.P_RECEIPT_PRINTER_TYPE, PrinterType.OS_PRINTER.getName())));
//		cbKitchenPrinterType.setSelectedItem(PrinterType.fromString(ApplicationConfig.getString(PrintConfig.P_KITCHEN_PRINTER_TYPE, PrinterType.OS_PRINTER.getName())));
		
//		tfReceiptPrinterName.setText(ApplicationConfig.getString(PrintConfig.P_JAVAPOS_PRINTER_FOR_RECEIPT, "POSPrinter"));
//		tfReceiptCashDrawerName.setText(ApplicationConfig.getString(PrintConfig.P_CASH_DRAWER_NAME, "CashDrawer"));
//		tfKitchenPrinterName.setText(ApplicationConfig.getString(PrintConfig.P_JAVAPOS_PRINTER_FOR_KITCHEN, "KitchenPrinter"));
		
		setSelectedPrinter(cbReceiptPrinterName, PrintConfig.P_OS_PRINTER_FOR_RECEIPT);
		setSelectedPrinter(cbKitchenPrinterName, PrintConfig.P_OS_PRINTER_FOR_KITCHEN);
		
		chkPrintReceiptWhenTicketSettled.setSelected(ApplicationConfig.getBoolean(PrintConfig.P_PRINT_RECEIPT_WHEN_SETTELED, true));
		chkPrintReceiptWhenTicketPaid.setSelected(ApplicationConfig.getBoolean(PrintConfig.P_PRINT_RECEIPT_WHEN_PAID, false));
		chkPrintKitchenWhenTicketSettled.setSelected(ApplicationConfig.getBoolean(PrintConfig.P_PRINT_KITCHEN_WHEN_SETTELED, false));
		chkPrintKitchenWhenTicketPaid.setSelected(ApplicationConfig.getBoolean(PrintConfig.P_PRINT_KITCHEN_WHEN_PAID, false));
		
		setInitialized(true);
	}

	private void setSelectedPrinter(JComboBox whichPrinter, String propertyName) {
		PrintService osDefaultPrinter = PrintServiceLookup.lookupDefaultPrintService();
		String receiptPrinterName = ApplicationConfig.getString(propertyName, osDefaultPrinter.getName());
		
		int printerCount = whichPrinter.getItemCount();
		for(int i = 0; i < printerCount; i++) {
			PrintService printService = (PrintService) whichPrinter.getItemAt(i);
			if(printService.getName().equals(receiptPrinterName)) {
				whichPrinter.setSelectedIndex(i);
				return;
			}
		}
	}

	@Override
	public boolean save() throws Exception {
//		ApplicationConfig.put(PrintConfig.P_RECEIPT_PRINTER_TYPE, cbReceiptPrinterType.getSelectedItem().toString());
//		ApplicationConfig.put(PrintConfig.P_KITCHEN_PRINTER_TYPE, cbKitchenPrinterType.getSelectedItem().toString());
		
		PrintService printService = (PrintService) cbReceiptPrinterName.getSelectedItem();
		ApplicationConfig.put(PrintConfig.P_OS_PRINTER_FOR_RECEIPT, printService == null ? null : printService.getName());
		printService = (PrintService) cbKitchenPrinterName.getSelectedItem();
		ApplicationConfig.put(PrintConfig.P_OS_PRINTER_FOR_KITCHEN, printService == null ? null : printService.getName());
//		ApplicationConfig.put(PrintConfig.P_JAVAPOS_PRINTER_FOR_RECEIPT, tfReceiptPrinterName.getText());
//		ApplicationConfig.put(PrintConfig.P_CASH_DRAWER_NAME, tfReceiptCashDrawerName.getText());
//		ApplicationConfig.put(PrintConfig.P_JAVAPOS_PRINTER_FOR_KITCHEN, tfKitchenPrinterName.getText());
		ApplicationConfig.put(PrintConfig.P_PRINT_KITCHEN_WHEN_PAID, chkPrintKitchenWhenTicketPaid.isSelected());
		ApplicationConfig.put(PrintConfig.P_PRINT_KITCHEN_WHEN_SETTELED, chkPrintKitchenWhenTicketSettled.isSelected());
		ApplicationConfig.put(PrintConfig.P_PRINT_RECEIPT_WHEN_PAID, chkPrintReceiptWhenTicketPaid.isSelected());
		ApplicationConfig.put(PrintConfig.P_PRINT_RECEIPT_WHEN_SETTELED, chkPrintReceiptWhenTicketSettled.isSelected());
		
		return true;
	}

//	private void setReceiptPrinterType(PrinterType printerType) {
//		switch (printerType) {
//			case OS_PRINTER:
//				lblReceiptPrinterName.setEnabled(true);
//				lblSelectReceiptPrinter.setEnabled(true);
//				cbReceiptPrinterName.setEnabled(true);
//				lblReceiptPrinterName.setEnabled(false);
//				tfReceiptPrinterName.setEnabled(false);
//				lblReceiptCashDrawerName.setEnabled(false);
//				tfReceiptCashDrawerName.setEnabled(false);
//				break;
//				
//			case JAVAPOS:
//				lblReceiptPrinterName.setEnabled(false);
//				lblSelectReceiptPrinter.setEnabled(false);
//				cbReceiptPrinterName.setEnabled(false);
//				lblReceiptPrinterName.setEnabled(true);
//				tfReceiptPrinterName.setEnabled(true);
//				lblReceiptCashDrawerName.setEnabled(true);
//				tfReceiptCashDrawerName.setEnabled(true);
//				break;
//		}
//	}
	
//	private void setKitchenPrinterType(PrinterType printerType) {
//		switch (printerType) {
//			case OS_PRINTER:
//				lblKitchenPrinterName.setEnabled(true);
//				lblSelectKitchenPrinter.setEnabled(true);
//				cbKitchenPrinterName.setEnabled(true);
//				lblKitchenPrinterName.setEnabled(false);
//				tfKitchenPrinterName.setEnabled(false);
//				break;
//				
//			case JAVAPOS:
//				lblKitchenPrinterName.setEnabled(false);
//				lblSelectKitchenPrinter.setEnabled(false);
//				cbKitchenPrinterName.setEnabled(false);
//				lblKitchenPrinterName.setEnabled(true);
//				tfKitchenPrinterName.setEnabled(true);
//				break;
//		}
//	}
	
	

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setLayout(new MigLayout("", "[][]", "[][][18px][18px][18px][18px]"));
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        add(jLabel1, "cell 0 0,alignx right");
        
                jLabel1.setText("Receipt Printer:");
        cbReceiptPrinterName = new javax.swing.JComboBox();
        add(cbReceiptPrinterName, "cell 1 0,growx");
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        add(jLabel2, "cell 0 1,alignx right");
        
                jLabel2.setText("Kitchen Printer:");
        cbKitchenPrinterName = new javax.swing.JComboBox();
        add(cbKitchenPrinterName, "cell 1 1,growx");
        
                chkPrintReceiptWhenTicketSettled = new javax.swing.JCheckBox();
                
                        chkPrintReceiptWhenTicketSettled.setText(com.floreantpos.POSConstants.PRINT_RECEIPT_WHEN_TICKET_SETTLED);
                        add(chkPrintReceiptWhenTicketSettled, "cell 1 2,alignx left,aligny top");
        chkPrintReceiptWhenTicketPaid = new javax.swing.JCheckBox();
        
                chkPrintReceiptWhenTicketPaid.setText(com.floreantpos.POSConstants.PRINT_RECEIPT_WHEN_TICKET_PAID);
                add(chkPrintReceiptWhenTicketPaid, "cell 1 3,alignx left,aligny top");
        chkPrintKitchenWhenTicketSettled = new javax.swing.JCheckBox();
        
                chkPrintKitchenWhenTicketSettled.setText(com.floreantpos.POSConstants.PRINT_TO_KITCHEN_WHEN_TICKET_SETTLED);
                add(chkPrintKitchenWhenTicketSettled, "cell 1 4,alignx left,aligny top");
        chkPrintKitchenWhenTicketPaid = new javax.swing.JCheckBox();
        
                chkPrintKitchenWhenTicketPaid.setText(com.floreantpos.POSConstants.PRINT_TO_KITCHEN_WHEN_TICKET_PAID);
                add(chkPrintKitchenWhenTicketPaid, "cell 1 5,alignx left,aligny top");
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cbKitchenPrinterName;
    private javax.swing.JComboBox cbReceiptPrinterName;
    private javax.swing.JCheckBox chkPrintKitchenWhenTicketPaid;
    private javax.swing.JCheckBox chkPrintKitchenWhenTicketSettled;
    private javax.swing.JCheckBox chkPrintReceiptWhenTicketPaid;
    private javax.swing.JCheckBox chkPrintReceiptWhenTicketSettled;
    // End of variables declaration//GEN-END:variables
    
    private class PrintServiceComboRenderer extends DefaultListCellRenderer {
    	@Override
    	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    		JLabel listCellRendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    		listCellRendererComponent.setText(((PrintService) value).getName());
    		
    		return listCellRendererComponent;
    	}
    }

}
