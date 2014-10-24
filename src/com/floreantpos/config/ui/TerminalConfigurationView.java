package com.floreantpos.config.ui;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.swing.IntegerTextField;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class TerminalConfigurationView extends ConfigurationView {
//	private JCheckBox cbxPrintReceiptOnOrderFinish;
//	private JCheckBox cbxPrintReceiptOnSettle;
//	private JCheckBox cbxPrintKitchenOnOrderFinish;
//	private JCheckBox cbxPrintKitchenOnSettle;
	private IntegerTextField tfTerminalNumber;
	//private FixedLengthTextField tfAdminPassword = new FixedLengthTextField(16);
	
	private JCheckBox cbEnableDineIn = new JCheckBox("DINE IN");
	private JCheckBox cbEnableTakeOut = new JCheckBox("TAKE OUT");
	private JCheckBox cbEnablePickUp = new JCheckBox("PICK UP");
	private JCheckBox cbEnableHomeDelivery = new JCheckBox("HOME DELIVERY");
	private JCheckBox cbEnableDriveThru = new JCheckBox("DRIVE THRU");
	private JCheckBox cbEnableBarTab = new JCheckBox("BAR TAB");
	
	private JCheckBox cbFullscreenMode = new JCheckBox("Kiosk Mode");
	
	public TerminalConfigurationView() {
		super();
		
		initComponents();
	}

	private void initComponents() {
		setLayout(new MigLayout("gap 5px 15px", "[110px][245px]", "[19px][23px][23px][23px][23px]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		JLabel lblTerminalNumber = new JLabel(Messages.getString("TerminalConfigurationView.TERMINAL_NUMBER")); //$NON-NLS-1$
		add(lblTerminalNumber, "alignx left,aligny center"); //$NON-NLS-1$
		
		tfTerminalNumber = new IntegerTextField();
		tfTerminalNumber.setColumns(10);
		add(tfTerminalNumber, "growx,aligny top, wrap"); //$NON-NLS-1$
		add(cbFullscreenMode, "wrap"); //$NON-NLS-1$
		
		JPanel ticketTypePanel = new JPanel(new GridLayout(0,1));
		ticketTypePanel.setBorder(BorderFactory.createTitledBorder("Ticket Types"));
		ticketTypePanel.add(cbEnableDineIn);
		ticketTypePanel.add(cbEnableTakeOut);
		ticketTypePanel.add(cbEnablePickUp);
		ticketTypePanel.add(cbEnableHomeDelivery);
		ticketTypePanel.add(cbEnableDriveThru);
		ticketTypePanel.add(cbEnableBarTab);
		
		add(ticketTypePanel, "wrap");
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

	@Override
	public boolean save() {
		int terminalNumber = 0;
		
		try {
			terminalNumber = Integer.parseInt(tfTerminalNumber.getText());
		} catch(Exception x) {
			POSMessageDialog.showError(Messages.getString("TerminalConfigurationView.14")); //$NON-NLS-1$
			return false;
		}
		
		TerminalConfig.setTerminalId(terminalNumber);
		TerminalConfig.setDineInEnable(cbEnableDineIn.isSelected());
		TerminalConfig.setPickupEnable(cbEnablePickUp.isSelected());
		TerminalConfig.setTakeOutEnable(cbEnableTakeOut.isSelected());
		TerminalConfig.setHomeDeliveryEnable(cbEnableHomeDelivery.isSelected());
		TerminalConfig.setDriveThruEnable(cbEnableDriveThru.isSelected());
		TerminalConfig.setBarTabEnable(cbEnableBarTab.isSelected());
		TerminalConfig.setFullscreenMode(cbFullscreenMode.isSelected());
		
		POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), "Please restart system for the configuration to take effect");
		
		return true;
	}

	@Override
	public void initialize() throws Exception {
		tfTerminalNumber.setText(String.valueOf(TerminalConfig.getTerminalId()));
		
		cbEnableDineIn.setSelected(TerminalConfig.isDineInEnable());
		cbEnablePickUp.setSelected(TerminalConfig.isPickupEnable());
		cbEnableTakeOut.setSelected(TerminalConfig.isTakeOutEnable());
		cbEnableHomeDelivery.setSelected(TerminalConfig.isHomeDeliveryEnable());
		cbEnableDriveThru.setSelected(TerminalConfig.isDriveThruEnable());
		cbEnableBarTab.setSelected(TerminalConfig.isBarTabEnable());
		cbFullscreenMode.setSelected(TerminalConfig.isFullscreenMode());
		
		setInitialized(true);
	}

	@Override
	public String getName() {
		return "Terminal";
	}
}
