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
package com.floreantpos.main;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Database;
import com.floreantpos.Messages;
import com.floreantpos.PosLog;
import com.floreantpos.bo.actions.DataImportAction;
import com.floreantpos.config.AppConfig;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.TerminalDAO;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.FixedLengthDocument;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.IntegerTextField;
import com.floreantpos.swing.POSPasswordField;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.DatabaseConnectionException;
import com.floreantpos.util.DatabaseUtil;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.ExperienceBlue;
import com.jidesoft.swing.JideScrollPane;

public class SetUpWindow extends JFrame implements ActionListener {

	private static final String CREATE_DATABASE = "CD"; //$NON-NLS-1$
	private static final String CREATE_SAMPLE_DATA = "UD"; //$NON-NLS-1$
	private static final String SAVE = "SAVE"; //$NON-NLS-1$
	private static final String CANCEL = "cancel"; //$NON-NLS-1$
	private static final String TEST = "test"; //$NON-NLS-1$
	private POSTextField tfServerAddress;
	private POSTextField tfServerPort;
	private POSTextField tfDatabaseName;
	private POSTextField tfUserName;
	private POSPasswordField tfPassword;
	private PosButton btnTestConnection;
	private PosButton btnCreateDb;
	private PosButton btnCreateSampleData;
	private PosButton btnExit;
	private PosButton btnSave;
	private JComboBox databaseCombo;

	private JLabel lblServerAddress;
	private JLabel lblServerPort;
	private JLabel lblDbName;
	private JLabel lblUserName;
	private JLabel lblDbPassword;

	private JLabel lblId;
	private JLabel lblConfirmSecretKey;
	private JLabel lblFirstName;
	private JLabel lblLastName;
	private JLabel lblSecretKey;
	private FixedLengthTextField tfFirstName;
	private FixedLengthTextField tfUserId;
	private FixedLengthTextField tfLastName;
	private JPasswordField tfPassword1;
	private JPasswordField tfPassword2;

	private IntegerTextField tfTerminalNumber;
	private IntegerTextField tfSecretKeyLength;
	private DoubleTextField tfScaleFactor;
	private JCheckBox chkAutoLogoff; //$NON-NLS-1$
	private IntegerTextField tfLogoffTime = new IntegerTextField(4);

	private boolean connectionSuccess;

	public SetUpWindow() throws HeadlessException {
		setLookAndFeel();
		ImageIcon applicationIcon = new ImageIcon(getClass().getResource("/icons/icon.png")); //$NON-NLS-1$
		setIconImage(applicationIcon.getImage());
		initUI();
		setFieldValues();
		addUIListeners();
		updateView();
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (b) {
			setupSizeAndLocation();
		}
	}

	private void setLookAndFeel() {
		try {
			PlasticXPLookAndFeel.setPlasticTheme(new ExperienceBlue());
			UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
			initializeFont();
		} catch (Exception ignored) {
		}
	}

	public void setupSizeAndLocation() {
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(PosUIManager.getSize(700, 420));
	}

	protected void initUI() {
		getContentPane().setLayout(new BorderLayout()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		JPanel databaseConfigPanel = new JPanel(new MigLayout("fill,hidemode 3", "[150px][fill, grow]", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		databaseConfigPanel.setBorder(new TitledBorder(Messages.getString("SetUpWindow.3"))); //$NON-NLS-1$
		tfServerAddress = new POSTextField();
		tfServerPort = new POSTextField();
		tfDatabaseName = new POSTextField();
		tfUserName = new POSTextField();
		tfPassword = new POSPasswordField();
		databaseCombo = new JComboBox(Database.values());

		String databaseProviderName = AppConfig.getDatabaseProviderName();
		if (StringUtils.isNotEmpty(databaseProviderName)) {
			databaseCombo.setSelectedItem(Database.getByProviderName(databaseProviderName));
		}
		btnTestConnection = new PosButton("Test"); //$NON-NLS-1$
		btnTestConnection.setActionCommand(TEST);

		btnCreateDb = new PosButton("Create New"); //$NON-NLS-1$
		btnCreateDb.setActionCommand(CREATE_DATABASE);

		btnCreateSampleData = new PosButton("Create sample data"); //$NON-NLS-1$
		btnCreateSampleData.setActionCommand(CREATE_SAMPLE_DATA);

		databaseConfigPanel.add(new JLabel(Messages.getString("DatabaseConfigurationDialog.8"))); //$NON-NLS-1$
		databaseConfigPanel.add(databaseCombo, "w 200!,grow, split 4"); //$NON-NLS-1$

		databaseConfigPanel.add(btnTestConnection, "w 50!,h 30!"); //$NON-NLS-1$
		databaseConfigPanel.add(btnCreateDb, "w 100!,h 30!"); //$NON-NLS-1$
		databaseConfigPanel.add(btnCreateSampleData, "h 30!,wrap"); //$NON-NLS-1$

		lblServerAddress = new JLabel(Messages.getString("DatabaseConfigurationDialog.10") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		databaseConfigPanel.add(lblServerAddress);
		databaseConfigPanel.add(tfServerAddress, "grow, split 3"); //$NON-NLS-1$
		lblServerPort = new JLabel("Port" + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		databaseConfigPanel.add(lblServerPort);
		tfServerPort.setHorizontalAlignment(JTextField.RIGHT);
		databaseConfigPanel.add(tfServerPort, "w 50!,wrap"); //$NON-NLS-1$
		lblDbName = new JLabel(Messages.getString("DatabaseConfigurationDialog.16") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		databaseConfigPanel.add(lblDbName);
		databaseConfigPanel.add(tfDatabaseName, "grow, wrap"); //$NON-NLS-1$
		lblUserName = new JLabel(Messages.getString("DatabaseConfigurationDialog.19") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		databaseConfigPanel.add(lblUserName);
		databaseConfigPanel.add(tfUserName, "grow, split 3"); //$NON-NLS-1$
		lblDbPassword = new JLabel("Password" + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		databaseConfigPanel.add(lblDbPassword);
		databaseConfigPanel.add(tfPassword, "grow, wrap"); //$NON-NLS-1$

		btnSave = new PosButton(Messages.getString("DatabaseConfigurationDialog.27").toUpperCase()); //$NON-NLS-1$
		btnSave.setActionCommand(SAVE);
		btnExit = new PosButton(Messages.getString("DatabaseConfigurationDialog.28").toUpperCase()); //$NON-NLS-1$
		btnExit.setActionCommand(CANCEL);

		JPanel buttonPanel = new JPanel(new MigLayout("fillx,right")); //$NON-NLS-1$

		buttonPanel.add(btnSave, "h 40!,split 2,right"); //$NON-NLS-1$
		buttonPanel.add(btnExit, "h 40!"); //$NON-NLS-1$

		JPanel contentPanel = new JPanel(new MigLayout("fillx")); //$NON-NLS-1$
		contentPanel.add(databaseConfigPanel, "grow,wrap"); //$NON-NLS-1$
		//contentPanel.add(createUserPanel(), "grow,wrap");
		contentPanel.add(createTerminalConfigPanel(), "grow,wrap"); //$NON-NLS-1$
		getContentPane().add(new JideScrollPane(contentPanel), BorderLayout.CENTER); //$NON-NLS-1$
		getContentPane().add(buttonPanel, BorderLayout.SOUTH); //$NON-NLS-1$

		getContentPane().setBackground(databaseConfigPanel.getBackground());
	}

	private JPanel createUserPanel() {
		JPanel userPanel = new JPanel(new MigLayout("fill,hidemode 3", "[150px][fill, grow]", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		userPanel.setVisible(false);
		userPanel.setBorder(new TitledBorder(Messages.getString("SetUpWindow.16"))); //$NON-NLS-1$

		lblId = new JLabel();
		lblFirstName = new JLabel();
		lblLastName = new JLabel();
		lblSecretKey = new JLabel();
		lblConfirmSecretKey = new JLabel();
		tfPassword1 = new JPasswordField(new FixedLengthDocument(16), "", 5); //$NON-NLS-1$
		tfPassword2 = new JPasswordField(new FixedLengthDocument(16), "", 5); //$NON-NLS-1$
		tfUserId = new FixedLengthTextField();
		tfFirstName = new FixedLengthTextField();
		tfFirstName.setColumns(20);
		tfFirstName.setLength(30);
		tfLastName = new FixedLengthTextField();
		tfLastName.setLength(30);
		tfLastName.setColumns(20);

		lblId.setText("ID"); //$NON-NLS-1$
		userPanel.add(lblId, "aligny center"); //$NON-NLS-1$
		userPanel.add(tfUserId, "growx,aligny center,wrap"); //$NON-NLS-1$

		lblFirstName.setText("First Name"); //$NON-NLS-1$
		userPanel.add(lblFirstName, "aligny center"); //$NON-NLS-1$
		userPanel.add(tfFirstName, "growx,aligny center,split 3"); //$NON-NLS-1$

		lblLastName.setText("Last Name"); //$NON-NLS-1$
		userPanel.add(lblLastName, "aligny center"); //$NON-NLS-1$
		userPanel.add(tfLastName, "growx,aligny ,w 200!,center,wrap"); //$NON-NLS-1$

		lblSecretKey.setText("Secret Key"); //$NON-NLS-1$
		userPanel.add(lblSecretKey, "aligny center"); //$NON-NLS-1$
		userPanel.add(tfPassword1, "growx,aligny center,split 3"); //$NON-NLS-1$

		lblConfirmSecretKey.setText("Confirm Secret Key"); //$NON-NLS-1$
		userPanel.add(lblConfirmSecretKey, "aligny center"); //$NON-NLS-1$
		userPanel.add(tfPassword2, "growx,w 200!,aligny center"); //$NON-NLS-1$

		return userPanel;
	}

	private JPanel createTerminalConfigPanel() {
		JPanel contentPanel = new JPanel(new MigLayout("fill,hidemode 3", "[150px][fill, grow]", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		contentPanel.setBorder(new TitledBorder(Messages.getString("SetUpWindow.20"))); //$NON-NLS-1$

		tfTerminalNumber = new IntegerTextField();
		tfTerminalNumber.setColumns(10);
		contentPanel.add(new JLabel(Messages.getString("SetUpWindow.21"))); //$NON-NLS-1$
		contentPanel.add(tfTerminalNumber, "aligny top,wrap"); //$NON-NLS-1$

		tfSecretKeyLength = new IntegerTextField(3);
		contentPanel.add(new JLabel("Default password length")); //$NON-NLS-1$
		contentPanel.add(tfSecretKeyLength, "wrap"); //$NON-NLS-1$

		chkAutoLogoff = new JCheckBox(Messages.getString("SetUpWindow.22")); //$NON-NLS-1$
		tfLogoffTime.setEnabled(false);
		chkAutoLogoff.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chkAutoLogoff.isSelected()) {
					tfLogoffTime.setEnabled(true);
				}
				else {
					tfLogoffTime.setEnabled(false);
				}
			}
		});
		contentPanel.add(chkAutoLogoff, "newline"); //$NON-NLS-1$
		contentPanel.add(new JLabel(Messages.getString("TerminalConfigurationView.16")), "split 2"); //$NON-NLS-1$ //$NON-NLS-2$
		contentPanel.add(tfLogoffTime, "alignx left,grow,wrap"); //$NON-NLS-1$

		contentPanel.add(new JLabel("Screen scaling")); //$NON-NLS-1$
		tfScaleFactor = new DoubleTextField(5);
		contentPanel.add(tfScaleFactor);

		return contentPanel;
	}

	private void addUIListeners() {
		btnTestConnection.addActionListener(this);
		btnCreateDb.addActionListener(this);
		btnSave.addActionListener(this);
		btnExit.addActionListener(this);
		btnCreateSampleData.addActionListener(this);

		databaseCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Database selectedDb = (Database) databaseCombo.getSelectedItem();

				if (selectedDb == Database.DERBY_SINGLE) {
					setFieldsVisible(false);
					return;
				}

				setFieldsVisible(true);

				String databasePort = AppConfig.getDatabasePort();
				if (StringUtils.isEmpty(databasePort)) {
					databasePort = selectedDb.getDefaultPort();
				}

				tfServerPort.setText(databasePort);
			}
		});
	}

	private void setFieldValues() {
		Database selectedDb = (Database) databaseCombo.getSelectedItem();

		String databaseURL = AppConfig.getDatabaseHost();
		tfServerAddress.setText(databaseURL);

		String databasePort = AppConfig.getDatabasePort();
		if (StringUtils.isEmpty(databasePort)) {
			databasePort = selectedDb.getDefaultPort();
		}

		tfServerPort.setText(databasePort);
		tfDatabaseName.setText(AppConfig.getDatabaseName());
		tfUserName.setText(AppConfig.getDatabaseUser());
		tfPassword.setText(AppConfig.getDatabasePassword());

		if (selectedDb == Database.DERBY_SINGLE) {
			setFieldsVisible(false);
		}
		else {
			setFieldsVisible(true);
		}
	}

	public void actionPerformed(ActionEvent e) {
		try {
			String command = e.getActionCommand();

			Database selectedDb = (Database) databaseCombo.getSelectedItem();

			final String providerName = selectedDb.getProviderName();
			final String databaseURL = tfServerAddress.getText();
			final String databasePort = tfServerPort.getText();
			final String databaseName = tfDatabaseName.getText();
			final String user = tfUserName.getText();
			final String pass = new String(tfPassword.getPassword());

			final String connectionString = selectedDb.getConnectString(databaseURL, databasePort, databaseName);
			final String hibernateDialect = selectedDb.getHibernateDialect();
			final String driverClass = selectedDb.getHibernateConnectionDriverClass();

			if (CANCEL.equalsIgnoreCase(command)) {
				System.exit(1);
				return;
			}

			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			if (TEST.equalsIgnoreCase(command)) {
				try {
					DatabaseUtil.checkConnection(connectionString, hibernateDialect, driverClass, user, pass);
				} catch (DatabaseConnectionException e1) {
					JOptionPane.showMessageDialog(this, Messages.getString("DatabaseConfigurationDialog.32")); //$NON-NLS-1$
					return;
				}
				connectionSuccess = true;
				JOptionPane.showMessageDialog(this, Messages.getString("DatabaseConfigurationDialog.31")); //$NON-NLS-1$
				return;
			}
			saveConfig(selectedDb, providerName, databaseURL, databasePort, databaseName, user, pass, connectionString, hibernateDialect);
			if (CREATE_SAMPLE_DATA.equals(command)) {
				DataImportAction.importMenuItems(DatabaseUtil.class.getResourceAsStream("/floreantpos-menu-items.xml")); //$NON-NLS-1$
			}
			else if (CREATE_DATABASE.equals(command)) {
				int i = JOptionPane.showConfirmDialog(this,
						Messages.getString("DatabaseConfigurationDialog.33"), Messages.getString("DatabaseConfigurationDialog.34"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
				if (i != JOptionPane.YES_OPTION) {
					return;
				}

				i = JOptionPane.showConfirmDialog(this,
						Messages.getString("DatabaseConfigurationDialog.4"), Messages.getString("DatabaseConfigurationDialog.5"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
				boolean generateSampleData = false;
				if (i == JOptionPane.YES_OPTION)
					generateSampleData = true;

				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				String createDbConnectString = selectedDb.getCreateDbConnectString(databaseURL, databasePort, databaseName);
				boolean databaseCreated = DatabaseUtil.createDatabase(createDbConnectString, hibernateDialect, driverClass, user, pass, generateSampleData);

				if (databaseCreated) {
					JOptionPane.showMessageDialog(SetUpWindow.this, Messages.getString("DatabaseConfigurationDialog.6") + //$NON-NLS-1$
							Messages.getString("DatabaseConfigurationDialog.7")); //$NON-NLS-1$
					connectionSuccess = true;
				}
				else {
					JOptionPane.showMessageDialog(SetUpWindow.this, Messages.getString("DatabaseConfigurationDialog.36")); //$NON-NLS-1$
				}
			}
			else if (SAVE.equalsIgnoreCase(command)) {
				Integer terminalId = tfTerminalNumber.getInteger();
				Integer defaultPassLen = tfSecretKeyLength.getInteger();
				Integer autoLogOffTime = tfLogoffTime.getInteger();
				Boolean isLogOff = chkAutoLogoff.isSelected();
				Double scaleFactor = tfScaleFactor.getDouble();

				TerminalConfig.setTerminalId(terminalId);
				TerminalConfig.setDefaultPassLen(defaultPassLen);
				TerminalConfig.setScreenScaleFactor(scaleFactor);
				TerminalConfig.setAutoLogoffEnable(isLogOff);
				TerminalConfig.setAutoLogoffTime(autoLogOffTime <= 0 ? 10 : autoLogOffTime);
				try {
					DatabaseUtil.initialize();
					saveConfigData();
				} catch (Exception ex) {
					int i = JOptionPane.showConfirmDialog(this, "Connection Failed. Do you want to save?", "Connection status!", JOptionPane.YES_NO_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
					if (i == JOptionPane.YES_OPTION) {
						System.exit(1);
					}
				}
			}
		} catch (Exception e2) {
			PosLog.error(getClass(), e2);
			POSMessageDialog.showMessage(this, e2.getMessage());
		} finally {
			setCursor(Cursor.getDefaultCursor());
		}
	}

	private void saveConfig(Database selectedDb, String providerName, String databaseURL, String databasePort, String databaseName, String user, String pass,
			String connectionString, String hibernateDialect) {
		AppConfig.setDatabaseProviderName(providerName);
		AppConfig.setConnectString(connectionString);
		AppConfig.setDatabaseHost(databaseURL);
		AppConfig.setDatabasePort(databasePort);
		AppConfig.setDatabaseName(databaseName);
		AppConfig.setDatabaseUser(user);
		AppConfig.setDatabasePassword(pass);
	}

	private void saveConfigData() {
		User user = null;
		/*try {
			user = UserDAO.getInstance().findUser(Integer.valueOf(tfUserId.getText()));
		} catch (UserNotFoundException ex) {
			user = new User();
		}*/
		Terminal terminal = new Terminal();
		if (!updateModel(user, terminal))
			return;
		/*UserType administrator = new UserType();
		administrator.setName(com.floreantpos.POSConstants.ADMINISTRATOR);
		administrator.setPermissions(new HashSet<UserPermission>(Arrays.asList(UserPermission.permissions)));
		UserTypeDAO.getInstance().saveOrUpdate(administrator);
		user.setType(administrator);
		UserDAO.getInstance().saveOrUpdate(user);*/
		TerminalDAO.getInstance().saveOrUpdate(terminal);
		POSMessageDialog.showMessage(Messages.getString("SetUpWindow.0")); //$NON-NLS-1$

		int i = JOptionPane.showConfirmDialog(this, "Do you want to start application?", "Message", JOptionPane.YES_NO_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
		if (i != JOptionPane.YES_OPTION) {
			System.exit(1);
		}
		else {
			try {
				Main.restart();
			} catch (IOException e) {
			} catch (InterruptedException e) {
			} catch (URISyntaxException e) {
			}
		}
	}

	private void updateView() {
		int terminalId = TerminalConfig.getTerminalId();
		if (terminalId == -1) {
			Random random = new Random();
			terminalId = random.nextInt(10000) + 1;
		}
		tfTerminalNumber.setText(String.valueOf(terminalId));
		loadDefaultData();
	}

	private void loadDefaultData() {
		/*tfUserId.setText("122");
		tfFirstName.setText("Admin");
		tfLastName.setText("System");*/
		tfScaleFactor.setText("1"); //$NON-NLS-1$
		chkAutoLogoff.setSelected(false);
		tfSecretKeyLength.setText("4"); //$NON-NLS-1$
		tfLogoffTime.setText("10"); //$NON-NLS-1$
	}

	private boolean updateModel(User user, Terminal terminal) {
		/*	String userId = tfUserId.getText();
			String firstName = tfFirstName.getText();
			String lastName = tfLastName.getText();
			String secretKey = tfPassword1.getText();
			String confirmSecretKey = tfPassword1.getText();

			if (StringUtils.isEmpty(userId)) {
				POSMessageDialog.showMessage("User Id cannot be empty.");
				return false;
			}
			if (StringUtils.isEmpty(secretKey)) {
				POSMessageDialog.showMessage("Password cannot be empty.");
				return false;
			}
			if (!secretKey.equals(confirmSecretKey)) {
				POSMessageDialog.showMessage("Password not match.");
				return false;
			}

			user.setUserId(Integer.valueOf(userId));
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setPassword(secretKey);*/

		Integer terminalId = tfTerminalNumber.getInteger();
		terminal.setId(terminalId);
		terminal.setName(String.valueOf(terminalId));
		return true;
	}

	public void setTitle(String title) {
		super.setTitle("Application Setup"); //$NON-NLS-1$
	}

	private void setFieldsVisible(boolean visible) {
		lblServerAddress.setVisible(visible);
		tfServerAddress.setVisible(visible);

		lblServerPort.setVisible(visible);
		tfServerPort.setVisible(visible);

		lblDbName.setVisible(visible);
		tfDatabaseName.setVisible(visible);

		lblUserName.setVisible(visible);
		tfUserName.setVisible(visible);

		lblDbPassword.setVisible(visible);
		tfPassword.setVisible(visible);
	}

	public static SetUpWindow open() {
		SetUpWindow window = new SetUpWindow();
		window.setTitle(Messages.getString("DatabaseConfigurationDialog.38")); //$NON-NLS-1$
		window.pack();
		window.setVisible(true);

		return window;
	}

	public static void main(String[] args) throws Exception {
		SetUpWindow.open();
	}

	private void initializeFont() {
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);

			if (value != null && value instanceof FontUIResource) {
				FontUIResource f = (FontUIResource) value;
				String fontName = f.getFontName();

				Font font = new Font(fontName, f.getStyle(), PosUIManager.getDefaultFontSize());
				UIManager.put(key, new FontUIResource(font));
			}
		}
	}
}
