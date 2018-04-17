package com.floreantpos.config.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;

import com.floreantpos.Messages;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.extension.ExtensionManager;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.main.Application;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.dao.TerminalDAO;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.DrawerUtil;
import com.floreantpos.util.POSUtil;
import com.floreantpos.util.SerialPortUtil;

public class PeripheralConfigurationView extends ConfigurationView {
	public static final String CONFIG_TAB_PERIPHERAL = "Peripherals"; //$NON-NLS-1$
	private JCheckBox chkHasCashDrawer;
	private JTextField tfDrawerName = new JTextField(10);
	private JTextField tfDrawerCodes = new JTextField(15);
	private DoubleTextField tfDrawerInitialBalance = new DoubleTextField(6);
	private JCheckBox cbCustomerDisplay;

	private JTextField tfCustomerDisplayPort;
	private JTextField tfCustomerDisplayMessage;
	private JCheckBox cbScaleActive;
	private JTextField tfScalePort;
	private FixedLengthTextField tfScaleDisplayMessage;

	private JCheckBox chkCallerIdEnable;
	private JComboBox cbCallerIds;

	public PeripheralConfigurationView() {
		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new MigLayout("", "[grow]", "[][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		JPanel drawerConfigPanel = new JPanel(new MigLayout());
		drawerConfigPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("PeripheralConfigurationView.4"))); //$NON-NLS-1$

		chkHasCashDrawer = new JCheckBox(Messages.getString("TerminalConfigurationView.15")); //$NON-NLS-1$
		drawerConfigPanel.add(chkHasCashDrawer, "span 5, wrap"); //$NON-NLS-1$

		drawerConfigPanel.add(new JLabel(Messages.getString("TerminalConfigurationView.25"))); //$NON-NLS-1$
		drawerConfigPanel.add(tfDrawerName, ""); //$NON-NLS-1$

		drawerConfigPanel.add(new JLabel(Messages.getString("TerminalConfigurationView.27")), "newline"); //$NON-NLS-1$ //$NON-NLS-2$
		drawerConfigPanel.add(tfDrawerCodes, ""); //$NON-NLS-1$

		JButton btnDrawerTest = new JButton(Messages.getString("TerminalConfigurationView.11")); //$NON-NLS-1$
		btnDrawerTest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = tfDrawerCodes.getText();
				if (StringUtils.isEmpty(text)) {
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

		contentPanel.add(drawerConfigPanel, "span 3, grow, wrap"); //$NON-NLS-1$

		chkHasCashDrawer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doEnableDisableDrawerPull();
			}
		});

		JPanel customerDisplayPanel = new JPanel(new MigLayout());
		customerDisplayPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("PeripheralConfigurationView.5"))); //$NON-NLS-1$

		cbCustomerDisplay = new JCheckBox(Messages.getString("PeripheralConfigurationView.6")); //$NON-NLS-1$
		tfCustomerDisplayPort = new JTextField(20);
		tfCustomerDisplayMessage = new FixedLengthTextField(20);

		JButton btnTest = new JButton(Messages.getString("PeripheralConfigurationView.7")); //$NON-NLS-1$
		btnTest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//TerminalConfig.setCustomerDisplayPort(tfCustomerDisplayPort.getText());
				DrawerUtil.setCustomerDisplayMessage(tfCustomerDisplayPort.getText(), String.format("%200s", ""));
				DrawerUtil.setCustomerDisplayMessage(tfCustomerDisplayPort.getText(), tfCustomerDisplayMessage.getText());
			}
		});

		JButton btnRestoreCustomerDefault = new JButton(Messages.getString("TerminalConfigurationView.32")); //$NON-NLS-1$
		btnRestoreCustomerDefault.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tfCustomerDisplayPort.setText("COM2"); //$NON-NLS-1$
				tfCustomerDisplayMessage.setText("1234567891234567891"); //$NON-NLS-1$
			}
		});

		customerDisplayPanel.add(cbCustomerDisplay, "wrap"); //$NON-NLS-1$
		customerDisplayPanel.add(new JLabel(Messages.getString("PeripheralConfigurationView.0"))); //$NON-NLS-1$
		customerDisplayPanel.add(tfCustomerDisplayPort, "wrap"); //$NON-NLS-1$
		customerDisplayPanel.add(new JLabel(Messages.getString("PeripheralConfigurationView.1"))); //$NON-NLS-1$
		customerDisplayPanel.add(tfCustomerDisplayMessage);
		customerDisplayPanel.add(btnTest);
		customerDisplayPanel.add(btnRestoreCustomerDefault);

		contentPanel.add(customerDisplayPanel, "grow,wrap"); //$NON-NLS-1$

		JPanel scaleDisplayPanel = new JPanel(new MigLayout());
		scaleDisplayPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("PeripheralConfigurationView.15"))); //$NON-NLS-1$

		cbScaleActive = new JCheckBox(Messages.getString("PeripheralConfigurationView.16")); //$NON-NLS-1$
		tfScalePort = new JTextField(20);
		tfScaleDisplayMessage = new FixedLengthTextField(20);

		JButton btnTestScale = new JButton(Messages.getString("PeripheralConfigurationView.17")); //$NON-NLS-1$
		btnTestScale.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				testScaleMachine();
			}
		});

		JButton btnRestoreScaleDefault = new JButton(Messages.getString("TerminalConfigurationView.32")); //$NON-NLS-1$
		btnRestoreScaleDefault.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tfScalePort.setText(Messages.getString("PeripheralConfigurationView.18")); //$NON-NLS-1$
			}
		});

		scaleDisplayPanel.add(cbScaleActive, "wrap"); //$NON-NLS-1$
		scaleDisplayPanel.add(new JLabel(Messages.getString("PeripheralConfigurationView.20"))); //$NON-NLS-1$
		scaleDisplayPanel.add(tfScalePort);
		scaleDisplayPanel.add(btnTestScale);
		scaleDisplayPanel.add(btnRestoreScaleDefault);

		if (TerminalConfig.getScaleActivationValue().equals("cas10")) { //$NON-NLS-1$
			contentPanel.add(scaleDisplayPanel, "grow,wrap"); //$NON-NLS-1$
		}

		JPanel callerIdPanel = new JPanel(new MigLayout());
		callerIdPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("PeripheralConfigurationView.23"))); //$NON-NLS-1$

		chkCallerIdEnable = new JCheckBox(Messages.getString("PeripheralConfigurationView.24")); //$NON-NLS-1$

		Vector callerIds = new Vector();
		callerIds.add("NONE"); //$NON-NLS-1$
		callerIds.add("AD101"); //$NON-NLS-1$
		callerIds.add("Whozz calling"); //$NON-NLS-1$
		cbCallerIds = new JComboBox(callerIds);

		callerIdPanel.add(chkCallerIdEnable, "span 2,wrap");//$NON-NLS-1$
		callerIdPanel.add(new JLabel("Caller Id device:")); //$NON-NLS-1$
		callerIdPanel.add(cbCallerIds);

		OrderServiceExtension orderServicePlugin = (OrderServiceExtension) ExtensionManager.getPlugin(OrderServiceExtension.class);
		if (orderServicePlugin != null) {
			contentPanel.add(callerIdPanel, "grow,wrap"); //$NON-NLS-1$
		}

		JScrollPane scrollPane = new JScrollPane(contentPanel);
		scrollPane.setBorder(null);
		add(scrollPane);
	}

	protected void doEnableDisableDrawerPull() {
		boolean selected = chkHasCashDrawer.isSelected();
		tfDrawerName.setEnabled(selected);
		tfDrawerCodes.setEnabled(selected);
		tfDrawerInitialBalance.setEnabled(selected);
	}

	@Override
	public boolean save() throws Exception {

		TerminalConfig.setDrawerPortName(tfDrawerName.getText());
		TerminalConfig.setDrawerControlCodes(tfDrawerCodes.getText());

		TerminalConfig.setCustomerDisplay(cbCustomerDisplay.isSelected());
		TerminalConfig.setCustomerDisplayPort(tfCustomerDisplayPort.getText());
		TerminalConfig.setCustomerDisplayMessage(tfCustomerDisplayMessage.getText());

		TerminalConfig.setScaleDisplay(cbScaleActive.isSelected());
		TerminalConfig.setScalePort(tfScalePort.getText());
		TerminalConfig.setScaleDisplayMessage(tfScaleDisplayMessage.getText());

		TerminalConfig.setCallerIdDevice(cbCallerIds.getSelectedItem().toString());
		TerminalConfig.setEnabledCallerIdDevice(chkCallerIdEnable.isSelected());

		TerminalDAO terminalDAO = TerminalDAO.getInstance();
		Terminal terminal = terminalDAO.get(TerminalConfig.getTerminalId());
		if (terminal == null) {
			terminal = new Terminal();
			terminal.setId(TerminalConfig.getTerminalId());
			terminal.setCurrentBalance(tfDrawerInitialBalance.getDouble());
			terminal.setName(String.valueOf(TerminalConfig.getTerminalId()));
		}

		terminal.setHasCashDrawer(chkHasCashDrawer.isSelected());
		terminal.setOpeningBalance(tfDrawerInitialBalance.getDouble());

		terminalDAO.saveOrUpdate(terminal);
		return true;
	}

	@Override
	public void initialize() throws Exception {

		Terminal terminal = Application.getInstance().refreshAndGetTerminal();
		chkHasCashDrawer.setSelected(terminal.isHasCashDrawer());
		tfDrawerName.setText(TerminalConfig.getDrawerPortName());
		tfDrawerCodes.setText(TerminalConfig.getDrawerControlCodes());
		tfDrawerInitialBalance.setText("" + terminal.getOpeningBalance()); //$NON-NLS-1$

		cbCustomerDisplay.setSelected(TerminalConfig.isActiveCustomerDisplay());
		tfCustomerDisplayPort.setText(TerminalConfig.getCustomerDisplayPort());
		tfCustomerDisplayMessage.setText(TerminalConfig.getCustomerDisplayMessage());

		cbScaleActive.setSelected(TerminalConfig.isActiveScaleDisplay());
		tfScalePort.setText(TerminalConfig.getScalePort());
		tfScaleDisplayMessage.setText(TerminalConfig.getScaleDisplayMessage());

		cbCallerIds.setSelectedItem(TerminalConfig.getCallerIdDevice());
		chkCallerIdEnable.setSelected(TerminalConfig.isEanbledCallerIdDevice());

		doEnableDisableDrawerPull();

		setInitialized(true);
	}

	private void testScaleMachine() {
		try {

			String string = SerialPortUtil.readWeight(tfScalePort.getText());
			POSMessageDialog.showError(POSUtil.getFocusedWindow(), string);

		} catch (Exception ex) {
			POSMessageDialog.showError(POSUtil.getFocusedWindow(), ex.getMessage());
			LogFactory.getLog(PeripheralConfigurationView.class).error(ex);
		}
	}

	@Override
	public String getName() {
		return CONFIG_TAB_PERIPHERAL;
	}
}
