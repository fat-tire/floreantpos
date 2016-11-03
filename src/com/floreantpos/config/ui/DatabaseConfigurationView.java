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
package com.floreantpos.config.ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Database;
import com.floreantpos.Messages;
import com.floreantpos.config.AppConfig;
import com.floreantpos.main.Application;
import com.floreantpos.main.Main;
import com.floreantpos.swing.POSPasswordField;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.DatabaseConnectionException;
import com.floreantpos.util.DatabaseUtil;
import com.floreantpos.util.POSUtil;

public class DatabaseConfigurationView extends ConfigurationView implements ActionListener {

	private static final String CONFIGURE_DB = "CD"; //$NON-NLS-1$
	private static final String SAVE = "SAVE"; //$NON-NLS-1$
	private static final String CANCEL = "cancel"; //$NON-NLS-1$
	private static final String TEST = "test"; //$NON-NLS-1$
	private POSTextField tfServerAddress;
	private POSTextField tfServerPort;
	private POSTextField tfDatabaseName;
	private POSTextField tfUserName;
	private POSPasswordField tfPassword;
	private JButton btnTestConnection;
	private JButton btnCreateDb;
	private JButton btnSave;
	private JComboBox databaseCombo;
	private JLabel lblServerAddress;
	private JLabel lblServerPort;
	private JLabel lblDbName;
	private JLabel lblUserName;
	private JLabel lblDbPassword;

	public DatabaseConfigurationView() throws HeadlessException {
		super();
		initUI();
		addUIListeners();
	}

	protected void initUI() {
		setLayout(new BorderLayout());

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new MigLayout("fill", "[][grow,fill]", "[][][][][][][][grow,fill]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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

		contentPanel.add(new JLabel(Messages.getString("DatabaseConfigurationDialog.8"))); //$NON-NLS-1$
		contentPanel.add(databaseCombo, "grow, wrap"); //$NON-NLS-1$
		lblServerAddress = new JLabel(Messages.getString("DatabaseConfigurationDialog.10") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		contentPanel.add(lblServerAddress);
		contentPanel.add(tfServerAddress, "grow, wrap"); //$NON-NLS-1$
		lblServerPort = new JLabel(Messages.getString("DatabaseConfigurationDialog.13") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		contentPanel.add(lblServerPort);
		contentPanel.add(tfServerPort, "grow, wrap"); //$NON-NLS-1$
		lblDbName = new JLabel(Messages.getString("DatabaseConfigurationDialog.16") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		contentPanel.add(lblDbName);
		contentPanel.add(tfDatabaseName, "grow, wrap"); //$NON-NLS-1$
		lblUserName = new JLabel(Messages.getString("DatabaseConfigurationDialog.19") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		contentPanel.add(lblUserName);
		contentPanel.add(tfUserName, "grow, wrap"); //$NON-NLS-1$
		lblDbPassword = new JLabel(Messages.getString("DatabaseConfigurationDialog.22") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		contentPanel.add(lblDbPassword);
		contentPanel.add(tfPassword, "grow, wrap"); //$NON-NLS-1$
		contentPanel.add(new JSeparator(), "span, grow, gaptop 10"); //$NON-NLS-1$

		btnTestConnection = new JButton(Messages.getString("DatabaseConfigurationDialog.26")); //$NON-NLS-1$
		btnTestConnection.setActionCommand(TEST);
		btnSave = new JButton(Messages.getString("DatabaseConfigurationDialog.27")); //$NON-NLS-1$
		btnSave.setActionCommand(SAVE);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnCreateDb = new JButton(Messages.getString("DatabaseConfigurationDialog.29")); //$NON-NLS-1$
		btnCreateDb.setActionCommand(CONFIGURE_DB);
		buttonPanel.add(btnCreateDb);
		buttonPanel.add(btnTestConnection);
		buttonPanel.add(btnSave);

		contentPanel.add(buttonPanel, "span, grow"); //$NON-NLS-1$

		JScrollPane scrollPane = new JScrollPane(contentPanel);
		scrollPane.setBorder(null);
		add(scrollPane);
	}

	private void addUIListeners() {
		btnTestConnection.addActionListener(this);
		btnCreateDb.addActionListener(this);
		btnSave.addActionListener(this);

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

	public void actionPerformed(ActionEvent e) {
		try {
			String command = e.getActionCommand();

			Database selectedDb = (Database) databaseCombo.getSelectedItem();

			String providerName = selectedDb.getProviderName();
			String databaseURL = tfServerAddress.getText();
			String databasePort = tfServerPort.getText();
			String databaseName = tfDatabaseName.getText();
			String user = tfUserName.getText();
			String pass = new String(tfPassword.getPassword());

			String connectionString = selectedDb.getConnectString(databaseURL, databasePort, databaseName);
			String hibernateDialect = selectedDb.getHibernateDialect();
			String driverClass = selectedDb.getHibernateConnectionDriverClass();

			if (TEST.equalsIgnoreCase(command)) {
				Application.getInstance().setSystemInitialized(false);
				saveConfig(selectedDb, providerName, databaseURL, databasePort, databaseName, user, pass, connectionString, hibernateDialect);

				try {
					DatabaseUtil.checkConnection(connectionString, hibernateDialect, driverClass, user, pass);
				} catch (DatabaseConnectionException e1) {
					JOptionPane.showMessageDialog(this, Messages.getString("DatabaseConfigurationDialog.32")); //$NON-NLS-1$
					return;
				}

				JOptionPane.showMessageDialog(POSUtil.getBackOfficeWindow(), Messages.getString("DatabaseConfigurationDialog.31")); //$NON-NLS-1$
			}
			else if (CONFIGURE_DB.equals(command)) {
				Application.getInstance().setSystemInitialized(false);

				int i = JOptionPane.showConfirmDialog(POSUtil.getBackOfficeWindow(),
						Messages.getString("DatabaseConfigurationDialog.33"), Messages.getString("DatabaseConfigurationDialog.34"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
				if (i != JOptionPane.YES_OPTION) {
					return;
				}

				i = JOptionPane.showConfirmDialog(POSUtil.getBackOfficeWindow(),
						Messages.getString("DatabaseConfigurationView.3"), Messages.getString("DatabaseConfigurationView.4"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
				boolean generateSampleData = false;
				if (i == JOptionPane.YES_OPTION)
					generateSampleData = true;

				saveConfig(selectedDb, providerName, databaseURL, databasePort, databaseName, user, pass, connectionString, hibernateDialect);

				String connectionString2 = selectedDb.getCreateDbConnectString(databaseURL, databasePort, databaseName);

				this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				boolean createDatabase = DatabaseUtil.createDatabase(connectionString2, hibernateDialect, driverClass, user, pass, generateSampleData);
				this.setCursor(Cursor.getDefaultCursor());

				if (createDatabase) {
					//JOptionPane.showMessageDialog(DatabaseConfigurationView.this, Messages.getString("DatabaseConfigurationDialog.35")); //$NON-NLS-1$
					JOptionPane.showMessageDialog(POSUtil.getBackOfficeWindow(), "Database created. Default password is 1111.\n\nThe system will now restart."); //$NON-NLS-1$

					Main.restart();
				}
				else {
					JOptionPane.showMessageDialog(POSUtil.getBackOfficeWindow(), Messages.getString("DatabaseConfigurationDialog.36")); //$NON-NLS-1$
				}
			}
			else if (SAVE.equalsIgnoreCase(command)) {
				Application.getInstance().setSystemInitialized(false);
				saveConfig(selectedDb, providerName, databaseURL, databasePort, databaseName, user, pass, connectionString, hibernateDialect);
			}
			else if (CANCEL.equalsIgnoreCase(command)) {
			}
		} catch (Exception e2) {
			POSMessageDialog.showMessage(com.floreantpos.util.POSUtil.getBackOfficeWindow(), e2.getMessage());
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

	@Override
	public boolean save() throws Exception {
		return false;
	}

	@Override
	public void initialize() throws Exception {
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

		setInitialized(true);
	}

	@Override
	public String getName() {
		return Messages.getString("DatabaseConfigurationView.5"); //$NON-NLS-1$
	}

}
