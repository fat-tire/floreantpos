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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.config.AppConfig;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.PosPrinters;
import com.floreantpos.ui.dialog.POSMessageDialog;

/**
 *
 * @author mshahriar
 */
public class PrintConfigurationView extends ConfigurationView {

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private JComboBox cbReceiptPrinterName;
	private JComboBox cbReportPrinterName;
	private JCheckBox chkKitchenBtn = new JCheckBox("Show KDS button on login screen");
	private JTextField txtYellowTime;
	private JTextField txtRedTime;

	// End of variables declaration//GEN-END:variables

	PosPrinters printers = PosPrinters.load();

	/** Creates new form PrintConfiguration */
	public PrintConfigurationView() {
		initComponents();
	}

	@Override
	public String getName() {
		return com.floreantpos.POSConstants.CONFIG_TAB_PRINT;
	}

	@Override
	public void initialize() throws Exception {
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

		cbReportPrinterName.setModel(new DefaultComboBoxModel(printServices));
		cbReceiptPrinterName.setModel(new DefaultComboBoxModel(printServices));

		PrintServiceComboRenderer comboRenderer = new PrintServiceComboRenderer();
		cbReportPrinterName.setRenderer(comboRenderer);
		cbReceiptPrinterName.setRenderer(comboRenderer);
		//cbFullscreenMode.setSelected(TerminalConfig.isFullscreenMode());
		chkKitchenBtn.setSelected(TerminalConfig.isShowKitchenBtnOnLoginScreen());
		setSelectedPrinter(cbReportPrinterName, printers.getReportPrinter());
		setSelectedPrinter(cbReceiptPrinterName, printers.getReceiptPrinter());

		String yellowTimeOut = AppConfig.getString("YellowTimeOut"); //$NON-NLS-1$
		String redTimeOut = AppConfig.getString("RedTimeOut"); //$NON-NLS-1$

		if (yellowTimeOut != null) {
			txtYellowTime.setText(yellowTimeOut);
		}
		if (redTimeOut != null) {
			txtRedTime.setText(redTimeOut);
		}

		setInitialized(true);

		if (printServices == null || printServices.length == 0) {
			POSMessageDialog.showMessage(com.floreantpos.util.POSUtil.getFocusedWindow(), Messages.getString("PrintConfigurationView.0")); //$NON-NLS-1$
		}
	}

	private void setSelectedPrinter(JComboBox whichPrinter, String printerName) {
		//		PrintService osDefaultPrinter = PrintServiceLookup.lookupDefaultPrintService();
		//
		//		if (osDefaultPrinter == null) {
		//			return;
		//		}

		//String printerName = AppConfig.getString(propertyName, osDefaultPrinter.getName());

		int printerCount = whichPrinter.getItemCount();
		for (int i = 0; i < printerCount; i++) {
			PrintService printService = (PrintService) whichPrinter.getItemAt(i);
			if (printService.getName().equals(printerName)) {
				whichPrinter.setSelectedIndex(i);
				return;
			}
		}
	}

	@Override
	public boolean save() throws Exception {
		PrintService printService = (PrintService) cbReportPrinterName.getSelectedItem();
		printers.setReportPrinter(printService == null ? null : printService.getName());
		//AppConfig.put(PrintConfig.REPORT_PRINTER_NAME, printService == null ? null : printService.getName());

		printService = (PrintService) cbReceiptPrinterName.getSelectedItem();
		printers.setReceiptPrinter(printService == null ? null : printService.getName());
		//AppConfig.put(PrintConfig.RECEIPT_PRINTER_NAME, printService == null ? null : printService.getName());

		//printService = (PrintService) cbKitchenPrinterName.getSelectedItem();
		//AppConfig.put(PrintConfig.KITCHEN_PRINTER_NAME, printService == null ? null : printService.getName());

		AppConfig.put("YellowTimeOut", txtYellowTime.getText()); //$NON-NLS-1$
		AppConfig.put("RedTimeOut", txtRedTime.getText()); //$NON-NLS-1$

		TerminalConfig.setShowKitchenBtnOnLoginScreen(chkKitchenBtn.isSelected());

		//Application.getPrinters().save();

		return true;
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {
		setLayout(new BorderLayout());
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new MigLayout("", "[][grow,fill]", "[][][][18px,grow][][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		JLabel lblReportPrinter = new JLabel(Messages.getString("PrintConfigurationView.4")); //$NON-NLS-1$
		//add(lblReportPrinter, "cell 0 0,alignx trailing"); //$NON-NLS-1$

		cbReportPrinterName = new JComboBox();
		//add(cbReportPrinterName, "cell 1 0,growx"); //$NON-NLS-1$
		javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
		//add(jLabel1, "cell 0 1,alignx right"); //$NON-NLS-1$

		jLabel1.setText(Messages.getString("PrintConfigurationView.8")); //$NON-NLS-1$
		cbReceiptPrinterName = new javax.swing.JComboBox();
		//add(cbReceiptPrinterName, "cell 1 1,growx"); //$NON-NLS-1$
		javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
		//add(jLabel2, "cell 0 2,alignx right"); //$NON-NLS-1$

		MultiPrinterPane multiPrinterPane = new MultiPrinterPane("Printers", printers.getKitchenPrinters()); //$NON-NLS-1$
		contentPanel.add(multiPrinterPane, "cell 0 1 2 1,growx,h 200!"); //$NON-NLS-1$

		PrinterGroupView printerGroupView = new PrinterGroupView(Messages.getString("PrintConfigurationView.13")); //$NON-NLS-1$
		printerGroupView.setPreferredSize(new Dimension(0, 400));
		contentPanel.add(printerGroupView, "cell 0 2 2 2,growx,,h 200!,wrap"); //$NON-NLS-1$

		JPanel footerPanel = new JPanel(new MigLayout());

		txtYellowTime = new JTextField(5);
		txtRedTime = new JTextField(5);

		txtYellowTime.setText("90"); //$NON-NLS-1$
		txtRedTime.setText("120"); //$NON-NLS-1$

		footerPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("PrintConfigurationView.6"))); //$NON-NLS-1$

		JLabel lblYellowTime = new JLabel(Messages.getString("PrintConfigurationView.7")); //$NON-NLS-1$
		JLabel lblRedTime = new JLabel(Messages.getString("PrintConfigurationView.9")); //$NON-NLS-1$

		footerPanel.add(lblYellowTime, "grow"); //$NON-NLS-1$
		footerPanel.add(txtYellowTime, "grow"); //$NON-NLS-1$
		footerPanel.add(new JLabel(Messages.getString("PrintConfigurationView.1")), "grow, wrap"); //$NON-NLS-1$ //$NON-NLS-2$
		footerPanel.add(lblRedTime, "grow"); //$NON-NLS-1$
		footerPanel.add(txtRedTime, "grow"); //$NON-NLS-1$
		footerPanel.add(new JLabel("sec"), "grow,wrap"); //$NON-NLS-1$ //$NON-NLS-2$
		footerPanel.add(chkKitchenBtn);
		contentPanel.add(footerPanel, "newline, grow, span 2,wrap"); //$NON-NLS-1$

		JScrollPane scrollPane = new JScrollPane(contentPanel);
		scrollPane.setBorder(null);
		add(scrollPane);

	}// </editor-fold>//GEN-END:initComponents

	private class PrintServiceComboRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			JLabel listCellRendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			PrintService printService = (PrintService) value;

			if (printService != null) {
				listCellRendererComponent.setText(printService.getName());
			}

			return listCellRendererComponent;
		}
	}

}
