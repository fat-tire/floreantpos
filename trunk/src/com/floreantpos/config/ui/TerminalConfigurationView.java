package com.floreantpos.config.ui;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.dao.TerminalDAO;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.IntegerTextField;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.DrawerUtil;

public class TerminalConfigurationView extends ConfigurationView {
	private IntegerTextField tfTerminalNumber;
	private IntegerTextField tfSecretKeyLength;
	
	private JCheckBox cbTranslatedName = new JCheckBox(Messages.getString("TerminalConfigurationView.2")); //$NON-NLS-1$
	private JCheckBox cbFullscreenMode = new JCheckBox(Messages.getString("TerminalConfigurationView.3")); //$NON-NLS-1$
	private JCheckBox cbUseSettlementPrompt = new JCheckBox(Messages.getString("TerminalConfigurationView.4")); //$NON-NLS-1$
	private JCheckBox cbShowDbConfiguration = new JCheckBox(Messages.getString("TerminalConfigurationView.5")); //$NON-NLS-1$
	
	private JComboBox<String> cbFonts = new JComboBox<String>();

	private IntegerTextField tfButtonHeight;

	private IntegerTextField tfFontSize;
	private JCheckBox cbAutoLogoff = new JCheckBox(Messages.getString("TerminalConfigurationView.7")); //$NON-NLS-1$
	private IntegerTextField tfLogoffTime = new IntegerTextField(4);
	
	JCheckBox chkHasCashDrawer;
	private JTextField tfDrawerName = new JTextField(10);
	private JTextField tfDrawerCodes = new JTextField(15);
	DoubleTextField tfDrawerInitialBalance = new DoubleTextField(6);
	
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
		
		add(new JLabel(Messages.getString("TerminalConfigurationView.9"))); //$NON-NLS-1$
		tfSecretKeyLength = new IntegerTextField(3);
		add(tfSecretKeyLength, "wrap"); //$NON-NLS-1$
		
		add(cbShowDbConfiguration, "spanx 3"); //$NON-NLS-1$
		
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
		add(cbAutoLogoff, "newline"); //$NON-NLS-1$
		add(new JLabel(Messages.getString("TerminalConfigurationView.16"))); //$NON-NLS-1$
		add(tfLogoffTime, "wrap"); //$NON-NLS-1$
		
		add(cbTranslatedName, "span 2"); //$NON-NLS-1$
		add(cbFullscreenMode, "newline, span"); //$NON-NLS-1$
		add(cbUseSettlementPrompt, "newline, span"); //$NON-NLS-1$
		
		add(new JLabel(Messages.getString("TerminalConfigurationView.17")), "newline");  //$NON-NLS-1$//$NON-NLS-2$
		add(cbFonts, "span 2, wrap"); //$NON-NLS-1$
		
		JPanel touchConfigPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
		touchConfigPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("TerminalConfigurationView.18"))); //$NON-NLS-1$
		touchConfigPanel.add(new JLabel(Messages.getString("TerminalConfigurationView.19"))); //$NON-NLS-1$
		tfButtonHeight = new IntegerTextField(5);
		touchConfigPanel.add(tfButtonHeight);
		
		touchConfigPanel.add(new JLabel(Messages.getString("TerminalConfigurationView.20"))); //$NON-NLS-1$
		tfFontSize = new IntegerTextField(5);
		touchConfigPanel.add(tfFontSize);
		
		add(touchConfigPanel, "span 3, grow, wrap"); //$NON-NLS-1$
		
		addCashDrawerConfig();
	}

	private void addCashDrawerConfig() {
		Integer[] hours = new Integer[24];
		Integer[] minutes = new Integer[60];
		
		for(int i = 0; i < 24; i++) {
			hours[i] = Integer.valueOf(i);
		}
		for(int i = 0; i < 60; i++) {
			minutes[i] = Integer.valueOf(i);
		}
		
		JPanel drawerConfigPanel = new JPanel(new MigLayout());
		drawerConfigPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("TerminalConfigurationView.22"))); //$NON-NLS-1$
		
		chkHasCashDrawer = new JCheckBox(Messages.getString("TerminalConfigurationView.15")); //$NON-NLS-1$
		drawerConfigPanel.add(chkHasCashDrawer, "span 5, wrap"); //$NON-NLS-1$
		
		drawerConfigPanel.add(new JLabel(Messages.getString("TerminalConfigurationView.25"))); //$NON-NLS-1$
		drawerConfigPanel.add(tfDrawerName, ""); //$NON-NLS-1$
		
		drawerConfigPanel.add(new JLabel(Messages.getString("TerminalConfigurationView.27")), "newline"); //$NON-NLS-1$ //$NON-NLS-2$
		drawerConfigPanel.add(tfDrawerCodes, Messages.getString("TerminalConfigurationView.29")); //$NON-NLS-1$
		
		JButton btnDrawerTest = new JButton(Messages.getString("TerminalConfigurationView.11")); //$NON-NLS-1$
		btnDrawerTest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = tfDrawerCodes.getText();
				if(StringUtils.isEmpty(text)) {
					text = TerminalConfig.getDefaultDrawerControlCodes();
				}
				
				String[] split = text.split(","); //$NON-NLS-1$
				char[] codes = new char[split.length];
				for (int i = 0; i < split.length; i++) {
					try {
						codes[i] = (char) Integer.parseInt(split[i]);
					} catch (Exception x) {
						codes[i] = '0';
					}
				}
				
				DrawerUtil.kickDrawer(tfDrawerName.getText(), codes);
			}
		});
		drawerConfigPanel.add(btnDrawerTest);
		
		JButton btnRestoreDrawerDefault = new JButton(Messages.getString("TerminalConfigurationView.32")); //$NON-NLS-1$
		btnRestoreDrawerDefault.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tfDrawerName.setText("COM1"); //$NON-NLS-1$
				tfDrawerCodes.setText(TerminalConfig.getDefaultDrawerControlCodes());
			}
		});
		drawerConfigPanel.add(btnRestoreDrawerDefault);

		drawerConfigPanel.add(new JLabel(Messages.getString("TerminalConfigurationView.34")), "newline"); //$NON-NLS-1$ //$NON-NLS-2$
		drawerConfigPanel.add(tfDrawerInitialBalance, "span 4, wrap"); //$NON-NLS-1$
		
		add(drawerConfigPanel, "span 3, grow, wrap"); //$NON-NLS-1$
	
		chkHasCashDrawer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doEnableDisableDrawerPull();
			}
		});
	}

	protected void doEnableDisableDrawerPull() {
		boolean selected = chkHasCashDrawer.isSelected();
		tfDrawerName.setEnabled(selected);
		tfDrawerCodes.setEnabled(selected);
		tfDrawerInitialBalance.setEnabled(selected);
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
			POSMessageDialog.showError(com.floreantpos.util.POSUtil.getFocusedWindow(), Messages.getString("TerminalConfigurationView.38")); //$NON-NLS-1$
			return false;
		}
		
		if(fontSize < 8) {
			POSMessageDialog.showError(com.floreantpos.util.POSUtil.getFocusedWindow(), Messages.getString("TerminalConfigurationView.39")); //$NON-NLS-1$
			return false;
		}
		
		try {
			terminalNumber = Integer.parseInt(tfTerminalNumber.getText());
		} catch(Exception x) {
			POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("TerminalConfigurationView.14")); //$NON-NLS-1$
			return false;
		}
		
		int defaultPassLen = tfSecretKeyLength.getInteger();
		if(defaultPassLen == 0) defaultPassLen = 4;
		
		TerminalConfig.setTerminalId(terminalNumber);
		TerminalConfig.setDefaultPassLen(defaultPassLen);
		TerminalConfig.setFullscreenMode(cbFullscreenMode.isSelected());
		TerminalConfig.setShowDbConfigureButton(cbShowDbConfiguration.isSelected());
		TerminalConfig.setUseTranslatedName(cbTranslatedName.isSelected());
		
		TerminalConfig.setTouchScreenButtonHeight(buttonHeight);
		TerminalConfig.setTouchScreenFontSize(fontSize);
		
		TerminalConfig.setAutoLogoffEnable(cbAutoLogoff.isSelected());
		TerminalConfig.setAutoLogoffTime(tfLogoffTime.getInteger() <= 0 ? 10 : tfLogoffTime.getInteger());
		TerminalConfig.setUseSettlementPrompt(cbUseSettlementPrompt.isSelected());
		
		POSMessageDialog.showMessage(com.floreantpos.util.POSUtil.getFocusedWindow(), Messages.getString("TerminalConfigurationView.40")); //$NON-NLS-1$
		
		String selectedFont = (String) cbFonts.getSelectedItem();
		if("<select>".equals(selectedFont)) { //$NON-NLS-1$
			selectedFont = null;
		}
		
		TerminalConfig.setUiDefaultFont(selectedFont);
		TerminalConfig.setDrawerPortName(tfDrawerName.getText());
		TerminalConfig.setDrawerControlCodes(tfDrawerCodes.getText());
		
		TerminalDAO terminalDAO = TerminalDAO.getInstance();
		Terminal terminal = terminalDAO.get(terminalNumber);
		if(terminal == null) {
			terminal = new Terminal();
			terminal.setId(terminalNumber);
			terminal.setCurrentBalance(tfDrawerInitialBalance.getDouble());
			terminal.setName(String.valueOf(terminalNumber));
		}
		
		terminal.setHasCashDrawer(chkHasCashDrawer.isSelected());
		terminal.setOpeningBalance(tfDrawerInitialBalance.getDouble());
		
		terminalDAO.saveOrUpdate(terminal);
		
		return true;
	}

	@Override
	public void initialize() throws Exception {
		tfTerminalNumber.setText(String.valueOf(TerminalConfig.getTerminalId()));
		tfSecretKeyLength.setText(String.valueOf(TerminalConfig.getDefaultPassLen()));
		cbFullscreenMode.setSelected(TerminalConfig.isFullscreenMode());
		cbShowDbConfiguration.setSelected(TerminalConfig.isShowDbConfigureButton());
		cbUseSettlementPrompt.setSelected(TerminalConfig.isUseSettlementPrompt());
		
		tfButtonHeight.setText("" + TerminalConfig.getTouchScreenButtonHeight()); //$NON-NLS-1$
		tfFontSize.setText("" + TerminalConfig.getTouchScreenFontSize()); //$NON-NLS-1$
		
		cbTranslatedName.setSelected(TerminalConfig.isUseTranslatedName());
		cbAutoLogoff.setSelected(TerminalConfig.isAutoLogoffEnable());
		tfLogoffTime.setText("" + TerminalConfig.getAutoLogoffTime()); //$NON-NLS-1$
		tfLogoffTime.setEnabled(cbAutoLogoff.isSelected());
		
		initializeFontConfig();
		
		Terminal terminal = Application.getInstance().refreshAndGetTerminal();
		chkHasCashDrawer.setSelected(terminal.isHasCashDrawer());
		tfDrawerName.setText(TerminalConfig.getDrawerPortName());
		tfDrawerCodes.setText(TerminalConfig.getDrawerControlCodes());
		tfDrawerInitialBalance.setText("" + terminal.getOpeningBalance()); //$NON-NLS-1$
		
		doEnableDisableDrawerPull();
		
		setInitialized(true);
	}

	private void initializeFontConfig() {
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    Font[] fonts = e.getAllFonts(); // Get the fonts
	    DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) cbFonts.getModel();
	    model.addElement("<select>"); //$NON-NLS-1$
	    
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
		return Messages.getString("TerminalConfigurationView.47"); //$NON-NLS-1$
	}
}
