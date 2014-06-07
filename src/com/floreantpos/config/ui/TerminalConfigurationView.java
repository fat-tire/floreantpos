package com.floreantpos.config.ui;

import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.config.AppConfig;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.swing.TitledView;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class TerminalConfigurationView extends TitledView implements UISaveHandler {
	private JCheckBox cbxPrintReceiptOnOrderFinish;
	private JCheckBox cbxPrintReceiptOnSettle;
	private JCheckBox cbxPrintKitchenOnOrderFinish;
	private JCheckBox cbxPrintKitchenOnSettle;
	private POSTextField tfTerminalNumber;
	
	public TerminalConfigurationView() {
		super();
		
		setTitlePaneVisible(false);
		
		initComponents();
		
		tfTerminalNumber.setText(String.valueOf(AppConfig.getTerminalId()));
		cbxPrintReceiptOnOrderFinish.setSelected(AppConfig.isPrintReceiptOnOrderFinish());
		cbxPrintReceiptOnSettle.setSelected(AppConfig.isPrintReceiptOnOrderSettle());
		cbxPrintKitchenOnOrderFinish.setSelected(AppConfig.isPrintToKitchenOnOrderFinish());
		cbxPrintKitchenOnSettle.setSelected(AppConfig.isPrintToKitchenOnOrderSettle());
		
		
	}

	private void initComponents() {
		JPanel contentPane = getContentPane();
		
		contentPane.setLayout(new MigLayout("gap 5px 15px", "[110px][245px]", "[19px][23px][23px][23px][23px]"));
		
		JLabel lblTerminalNumber = new JLabel("Terminal number:");
		contentPane.add(lblTerminalNumber, "cell 0 0,alignx left,aligny center");
		
		tfTerminalNumber = new POSTextField();
		tfTerminalNumber.setColumns(10);
		contentPane.add(tfTerminalNumber, "cell 1 0,growx,aligny top");
		
		cbxPrintReceiptOnOrderFinish = new JCheckBox("Print receipt on order finish");
		cbxPrintReceiptOnOrderFinish.setMargin(new Insets(2, 0, 2, 2));
		contentPane.add(cbxPrintReceiptOnOrderFinish, "cell 1 1,alignx left,aligny top");
		
		cbxPrintReceiptOnSettle = new JCheckBox("Print receipt on order settle");
		cbxPrintReceiptOnSettle.setMargin(new Insets(2, 0, 2, 2));
		contentPane.add(cbxPrintReceiptOnSettle, "cell 1 2,alignx left,aligny top");
		
		cbxPrintKitchenOnOrderFinish = new JCheckBox("Print to kitchen on order finish");
		cbxPrintKitchenOnOrderFinish.setMargin(new Insets(2, 0, 2, 2));
		contentPane.add(cbxPrintKitchenOnOrderFinish, "cell 1 3,alignx left,aligny top");
		
		cbxPrintKitchenOnSettle = new JCheckBox("Print to kitchen on order settle");
		cbxPrintKitchenOnSettle.setMargin(new Insets(2, 0, 2, 2));
		contentPane.add(cbxPrintKitchenOnSettle, "cell 1 4,alignx left,aligny top");
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new TerminalConfigurationView());
		frame.setSize(500, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public boolean canSave() {
		return true;
	}

	public void save() {
		int terminalNumber = 0;
		
		try {
			terminalNumber = Integer.parseInt(tfTerminalNumber.getText());
		} catch(Exception x) {
			POSMessageDialog.showError("Terminal number is not valid");
			return;
		}
		
		AppConfig.setTerminalId(terminalNumber);
		AppConfig.setPrintReceiptOnOrderFinish(cbxPrintReceiptOnOrderFinish.isSelected());
		AppConfig.setPrintReceiptOnOrderSettle(cbxPrintKitchenOnSettle.isSelected());
		AppConfig.setPrintToKitchenOnOrderFinish(cbxPrintKitchenOnOrderFinish.isSelected());
		AppConfig.setPrintToKitchenOnOrderSettle(cbxPrintKitchenOnSettle.isSelected());
	}
}
