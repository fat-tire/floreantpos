package com.floreantpos.config.ui;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;

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
	private IntegerTextField tfSecretKeyLength;
	//private FixedLengthTextField tfAdminPassword = new FixedLengthTextField(16);
	
	private JCheckBox cbEnableDineIn = new JCheckBox("DINE IN");
	private JCheckBox cbEnableTakeOut = new JCheckBox("TAKE OUT");
	private JCheckBox cbEnablePickUp = new JCheckBox("PICK UP");
	private JCheckBox cbEnableHomeDelivery = new JCheckBox("HOME DELIVERY");
	private JCheckBox cbEnableDriveThru = new JCheckBox("DRIVE THRU");
	private JCheckBox cbEnableBarTab = new JCheckBox("BAR TAB");
	
	private JCheckBox cbFullscreenMode = new JCheckBox("Kiosk Mode");
	
	private JComboBox<String> cbFonts = new JComboBox<String>();

	private IntegerTextField tfButtonHeight;

	private IntegerTextField tfFontSize;
	private JCheckBox cbAutoLogoff = new JCheckBox("Enable auto logoff");
	private IntegerTextField tfLogoffTime = new IntegerTextField(4);
	
	public TerminalConfigurationView() {
		super();
		
		initComponents();
	}

	private void initComponents() {
		setLayout(new MigLayout("gap 5px 10px", "[][][grow]", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		JLabel lblTerminalNumber = new JLabel(Messages.getString("TerminalConfigurationView.TERMINAL_NUMBER")); //$NON-NLS-1$
		add(lblTerminalNumber, "alignx left,aligny center"); //$NON-NLS-1$
		
		tfTerminalNumber = new IntegerTextField();
		tfTerminalNumber.setColumns(10);
		add(tfTerminalNumber, "aligny top, wrap"); //$NON-NLS-1$
		
		add(new JLabel("Default password length"));
		tfSecretKeyLength = new IntegerTextField(3);
		add(tfSecretKeyLength, "wrap");
		
		cbAutoLogoff.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbAutoLogoff.isSelected()) {
					tfLogoffTime.setEnabled(true);
				} 
				else {
					tfLogoffTime.setEnabled(false);
				}
			}
		});
		add(cbAutoLogoff);
		add(new JLabel("Auto logoff time")); //$NON-NLS-1$
		add(tfLogoffTime, "wrap");
		
		add(cbFullscreenMode, "wrap"); //$NON-NLS-1$
		
		add(new JLabel("Default font")); //$NON-NLS-1$
		add(cbFonts, "wrap"); //$NON-NLS-1$
		
		JPanel ticketTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
		ticketTypePanel.setBorder(BorderFactory.createTitledBorder("Ticket Types"));
		ticketTypePanel.add(cbEnableDineIn);
		ticketTypePanel.add(cbEnableTakeOut);
		ticketTypePanel.add(cbEnablePickUp);
		ticketTypePanel.add(cbEnableHomeDelivery);
		ticketTypePanel.add(cbEnableDriveThru);
		ticketTypePanel.add(cbEnableBarTab);
		
		add(ticketTypePanel, "span 3, wrap");
		
		JPanel touchConfigPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
		touchConfigPanel.setBorder(BorderFactory.createTitledBorder("TOUCH SCREEN SETTINGS"));
		touchConfigPanel.add(new JLabel("Button height"));
		tfButtonHeight = new IntegerTextField(5);
		touchConfigPanel.add(tfButtonHeight);
		
		touchConfigPanel.add(new JLabel("Button font size"));
		tfFontSize = new IntegerTextField(5);
		touchConfigPanel.add(tfFontSize);
		
		add(touchConfigPanel, "span 3, grow, wrap");
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
		int buttonHeight = tfButtonHeight.getInteger();
		int fontSize = tfFontSize.getInteger();
		
		if(buttonHeight < 20) {
			POSMessageDialog.showError(BackOfficeWindow.getInstance(), "Please make sure button size is at least 20");
			return false;
		}
		
		if(fontSize < 8) {
			POSMessageDialog.showError(BackOfficeWindow.getInstance(), "Please make sure button font size is at least 8");
			return false;
		}
		
		try {
			terminalNumber = Integer.parseInt(tfTerminalNumber.getText());
		} catch(Exception x) {
			POSMessageDialog.showError(Messages.getString("TerminalConfigurationView.14")); //$NON-NLS-1$
			return false;
		}
		
		int defaultPassLen = tfSecretKeyLength.getInteger();
		if(defaultPassLen == 0) defaultPassLen = 4;
		
		TerminalConfig.setTerminalId(terminalNumber);
		TerminalConfig.setDefaultPassLen(defaultPassLen);
		TerminalConfig.setDineInEnable(cbEnableDineIn.isSelected());
		TerminalConfig.setPickupEnable(cbEnablePickUp.isSelected());
		TerminalConfig.setTakeOutEnable(cbEnableTakeOut.isSelected());
		TerminalConfig.setHomeDeliveryEnable(cbEnableHomeDelivery.isSelected());
		TerminalConfig.setDriveThruEnable(cbEnableDriveThru.isSelected());
		TerminalConfig.setBarTabEnable(cbEnableBarTab.isSelected());
		TerminalConfig.setFullscreenMode(cbFullscreenMode.isSelected());
		
		TerminalConfig.setTouchScreenButtonHeight(buttonHeight);
		TerminalConfig.setTouchScreenFontSize(fontSize);
		
		TerminalConfig.setAutoLogoffEnable(cbAutoLogoff.isSelected());
		TerminalConfig.setAutoLogoffTime(tfLogoffTime.getInteger() <= 0 ? 10 : tfLogoffTime.getInteger());
		
		POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), "Please restart system for the configuration to take effect");
		
		String selectedFont = (String) cbFonts.getSelectedItem();
		if("<select>".equals(selectedFont)) {
			selectedFont = null;
		}
		
		TerminalConfig.setUiDefaultFont(selectedFont);
		
		return true;
	}

	@Override
	public void initialize() throws Exception {
		tfTerminalNumber.setText(String.valueOf(TerminalConfig.getTerminalId()));
		tfSecretKeyLength.setText(String.valueOf(TerminalConfig.getDefaultPassLen()));
		cbEnableDineIn.setSelected(TerminalConfig.isDineInEnable());
		cbEnablePickUp.setSelected(TerminalConfig.isPickupEnable());
		cbEnableTakeOut.setSelected(TerminalConfig.isTakeOutEnable());
		cbEnableHomeDelivery.setSelected(TerminalConfig.isHomeDeliveryEnable());
		cbEnableDriveThru.setSelected(TerminalConfig.isDriveThruEnable());
		cbEnableBarTab.setSelected(TerminalConfig.isBarTabEnable());
		cbFullscreenMode.setSelected(TerminalConfig.isFullscreenMode());
		
		tfButtonHeight.setText("" + TerminalConfig.getTouchScreenButtonHeight());
		tfFontSize.setText("" + TerminalConfig.getTouchScreenFontSize());
		
		cbAutoLogoff.setSelected(TerminalConfig.isAutoLogoffEnable());
		tfLogoffTime.setText("" + TerminalConfig.getAutoLogoffTime());
		tfLogoffTime.setEnabled(cbAutoLogoff.isSelected());
		
		initializeFontConfig();
		
		setInitialized(true);
	}

	private void initializeFontConfig() {
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    Font[] fonts = e.getAllFonts(); // Get the fonts
	    DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) cbFonts.getModel();
	    model.addElement("<select>");
	    
	    for (Font f : fonts) {
	    	model.addElement(f.getFontName());
	    }
	    
	    String uiDefaultFont = TerminalConfig.getUiDefaultFont();
	    if(StringUtils.isNotEmpty(uiDefaultFont)) {
	    	cbFonts.setSelectedItem(uiDefaultFont);
	    }
	}

	@Override
	public String getName() {
		return "Terminal";
	}
}
