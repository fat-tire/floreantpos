package com.mdss.pos.config.ui;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.mdss.pos.PosException;
import com.mdss.pos.main.Application;
import com.mdss.pos.model.PrinterConfiguration;
import com.mdss.pos.model.dao.PrinterConfigurationDAO;
import com.mdss.pos.swing.FixedLengthDocument;

public class PrintConfigurationView extends ConfigurationView {
	private JTextField tfReceiptPrinterName = new JTextField();
	private JTextField tfKitchenPrinterName = new JTextField();
	private JCheckBox chkPrintReceiptWhenTicketSettled = new JCheckBox("Print receipt when ticket settled");
	private JCheckBox chkPrintReceiptWhenTicketPaid = new JCheckBox("Print receipt when ticket paid");
	private JCheckBox chkPrintKitchenWhenTicketSettled = new JCheckBox("Print to kitchen when ticket settled");
	private JCheckBox chkPrintKitchenWhenTicketPaid = new JCheckBox("Print to kitchen when ticket paid");
	private JCheckBox chkUseNormalPrinterForTicket = new JCheckBox("Use normal printer for ticket");
	private JCheckBox chkUseNormalPrinterForKitchen = new JCheckBox("Use normal printer for kitchen");
	
	private PrinterConfigurationDAO dao = new PrinterConfigurationDAO();
	
	public PrintConfigurationView() {
		setLayout(new MigLayout("","[][grow, fill]",""));
		
		tfReceiptPrinterName.setDocument(new FixedLengthDocument(60));
		tfKitchenPrinterName.setDocument(new FixedLengthDocument(60));
		
		add(new JLabel("Receipt Printer Name:"));
		add(tfReceiptPrinterName, "grow, wrap");
		add(new JLabel("Kitchen Printer Name:"));
		add(tfKitchenPrinterName, "grow, wrap");
		add(chkPrintReceiptWhenTicketSettled, "skip 1, wrap");
		add(chkPrintReceiptWhenTicketPaid, "skip 1, wrap");
		add(chkPrintKitchenWhenTicketSettled, "skip 1, wrap");
		add(chkPrintKitchenWhenTicketPaid, "skip 1, wrap");
		add(chkUseNormalPrinterForTicket, "skip 1, wrap");
		add(chkUseNormalPrinterForKitchen, "skip 1, wrap");
	}

	@Override
	public boolean save() throws Exception {
		PrinterConfiguration configuration = new PrinterConfiguration();
		configuration.setKitchenPrinterName(tfKitchenPrinterName.getText());
		configuration.setReceiptPrinterName(tfReceiptPrinterName.getText());
		configuration.setPrintKitchenWhenTicketPaid(chkPrintKitchenWhenTicketPaid.isSelected());
		configuration.setPrintKitchenWhenTicketSettled(chkPrintKitchenWhenTicketSettled.isSelected());
		configuration.setPrintReceiptWhenTicketPaid(chkPrintReceiptWhenTicketPaid.isSelected());
		configuration.setPrintRecreiptWhenTicketSettled(chkPrintReceiptWhenTicketSettled.isSelected());
		configuration.setUseNormalPrinterForTicket(chkUseNormalPrinterForTicket.isSelected());
		configuration.setUseNormalPrinterForKitchen(chkUseNormalPrinterForKitchen.isSelected());
		
		if(StringUtils.isEmpty(configuration.getReceiptPrinterName())) {
			throw new PosException("Receipt printer name is required.");
		}
		if(StringUtils.isEmpty(configuration.getKitchenPrinterName())) {
			throw new PosException("Kitchen printer name is required.");
		}
		
		configuration.setId(PrinterConfiguration.ID);
		dao.saveOrUpdate(configuration);
		
		Application.getInstance().printConfiguration = configuration;
		return true;
	}
	
	@Override
	public void initialize() throws Exception {
		PrinterConfiguration configuration = dao.get(PrinterConfiguration.ID);
		if(configuration == null) {
			configuration = new PrinterConfiguration();
		}
		tfReceiptPrinterName.setText(configuration.getReceiptPrinterName());
		tfKitchenPrinterName.setText(configuration.getKitchenPrinterName());
		chkPrintReceiptWhenTicketSettled.setSelected(configuration.isPrintKitchenWhenTicketSettled());
		chkPrintReceiptWhenTicketPaid.setSelected(configuration.isPrintReceiptWhenTicketPaid());
		chkPrintKitchenWhenTicketSettled.setSelected(configuration.isPrintKitchenWhenTicketSettled());
		chkPrintKitchenWhenTicketPaid.setSelected(configuration.isPrintKitchenWhenTicketPaid());
		chkUseNormalPrinterForTicket.setSelected(configuration.isUseNormalPrinterForTicket());
		chkUseNormalPrinterForKitchen.setSelected(configuration.isUseNormalPrinterForKitchen());
		setInitialized(true);
	}

	@Override
	public String getName() {
		return "Print Configuration";
	}
}
