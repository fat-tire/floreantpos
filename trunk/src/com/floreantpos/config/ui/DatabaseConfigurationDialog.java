package com.floreantpos.config.ui;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Database;
import com.floreantpos.Messages;
import com.floreantpos.config.AppConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.swing.POSPasswordField;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.DatabaseConnectionException;
import com.floreantpos.util.DatabaseUtil;

public class DatabaseConfigurationDialog extends POSDialog implements ActionListener {

	private static final String CREATE_DATABASE = "CD"; //$NON-NLS-1$
	private static final String UPDATE_DATABASE = "UD"; //$NON-NLS-1$
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
	private PosButton btnUpdateDb;
	private PosButton btnExit;
	private PosButton btnSave;
	private JComboBox databaseCombo;

	private TitlePanel titlePanel;
	private JLabel lblServerAddress;
	private JLabel lblServerPort;
	private JLabel lblDbName;
	private JLabel lblUserName;
	private JLabel lblDbPassword;

	public DatabaseConfigurationDialog() throws HeadlessException {
		super();

		setFieldValues();
		addUIListeners();
	}

	protected void initUI() {
		getContentPane().setLayout(new MigLayout("fill", "[][fill, grow]", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		titlePanel = new TitlePanel();
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

		getContentPane().add(titlePanel, "span, grow, wrap"); //$NON-NLS-1$

		getContentPane().add(new JLabel(Messages.getString("DatabaseConfigurationDialog.8"))); //$NON-NLS-1$
		getContentPane().add(databaseCombo, "grow, wrap"); //$NON-NLS-1$
		lblServerAddress = new JLabel(Messages.getString("DatabaseConfigurationDialog.10") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		getContentPane().add(lblServerAddress);
		getContentPane().add(tfServerAddress, "grow, wrap"); //$NON-NLS-1$
		lblServerPort = new JLabel(Messages.getString("DatabaseConfigurationDialog.13") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		getContentPane().add(lblServerPort);
		getContentPane().add(tfServerPort, "grow, wrap"); //$NON-NLS-1$
		lblDbName = new JLabel(Messages.getString("DatabaseConfigurationDialog.16") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		getContentPane().add(lblDbName);
		getContentPane().add(tfDatabaseName, "grow, wrap"); //$NON-NLS-1$
		lblUserName = new JLabel(Messages.getString("DatabaseConfigurationDialog.19") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		getContentPane().add(lblUserName);
		getContentPane().add(tfUserName, "grow, wrap"); //$NON-NLS-1$
		lblDbPassword = new JLabel(Messages.getString("DatabaseConfigurationDialog.22") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		getContentPane().add(lblDbPassword);
		getContentPane().add(tfPassword, "grow, wrap"); //$NON-NLS-1$
		getContentPane().add(new JSeparator(), "span, grow, gaptop 10"); //$NON-NLS-1$

		btnTestConnection = new PosButton(Messages.getString("DatabaseConfigurationDialog.26").toUpperCase()); //$NON-NLS-1$
		btnTestConnection.setActionCommand(TEST);
		btnSave = new PosButton(Messages.getString("DatabaseConfigurationDialog.27").toUpperCase()); //$NON-NLS-1$
		btnSave.setActionCommand(SAVE);
		btnExit = new PosButton(Messages.getString("DatabaseConfigurationDialog.28").toUpperCase()); //$NON-NLS-1$
		btnExit.setActionCommand(CANCEL);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		btnCreateDb = new PosButton(Messages.getString("DatabaseConfigurationDialog.29").toUpperCase()); //$NON-NLS-1$
		btnCreateDb.setActionCommand(CREATE_DATABASE);
		
		btnUpdateDb = new PosButton(Messages.getString("UPDATE_DATABASE").toUpperCase()); //$NON-NLS-1$
		btnUpdateDb.setActionCommand(UPDATE_DATABASE);
		
		buttonPanel.add(btnUpdateDb);
		buttonPanel.add(btnCreateDb);
		buttonPanel.add(btnTestConnection);
		buttonPanel.add(btnSave);
		buttonPanel.add(btnExit);

		getContentPane().add(buttonPanel, "span, grow"); //$NON-NLS-1$
	}

	private void addUIListeners() {
		btnTestConnection.addActionListener(this);
		btnCreateDb.addActionListener(this);
		btnSave.addActionListener(this);
		btnExit.addActionListener(this);
		btnUpdateDb.addActionListener(this);

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
				dispose();
				return;
			}
			
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			
			Application.getInstance().setSystemInitialized(false);
			saveConfig(selectedDb, providerName, databaseURL, databasePort, databaseName, user, pass, connectionString, hibernateDialect);
			
			if (TEST.equalsIgnoreCase(command)) {
				try {
					DatabaseUtil.checkConnection(connectionString, hibernateDialect, driverClass, user, pass);
				} catch (DatabaseConnectionException e1) {
					JOptionPane.showMessageDialog(this, Messages.getString("DatabaseConfigurationDialog.32")); //$NON-NLS-1$
					return;
				}

				JOptionPane.showMessageDialog(this, Messages.getString("DatabaseConfigurationDialog.31")); //$NON-NLS-1$
			}
			else if(UPDATE_DATABASE.equals(command)) {
				int i = JOptionPane.showConfirmDialog(this, "This will update existing database. Existing data will not be lost. Proceed?", "Confirm", JOptionPane.YES_NO_OPTION);
				if (i != JOptionPane.YES_OPTION) {
					return;
				}
				
				//isAuthorizedToPerformDbChange();
				
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				
				boolean databaseUpdated = DatabaseUtil.updateDatabase(connectionString, hibernateDialect, driverClass, user, pass);
				if(databaseUpdated) {
					JOptionPane.showMessageDialog(DatabaseConfigurationDialog.this, "Database successfully updated.");
				}
				else {
					JOptionPane.showMessageDialog(DatabaseConfigurationDialog.this, "Failed to update database, please see log for detail.");
				}
			}
			else if (CREATE_DATABASE.equals(command)) {
				int i = JOptionPane.showConfirmDialog(this,
						Messages.getString("DatabaseConfigurationDialog.33"), Messages.getString("DatabaseConfigurationDialog.34"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
				if (i != JOptionPane.YES_OPTION) {
					return;
				}

				i = JOptionPane.showConfirmDialog(this, "Do you want to generate sample data?", "Confirm", JOptionPane.YES_NO_OPTION);
				boolean generateSampleData = false;
				if (i == JOptionPane.YES_OPTION)
					generateSampleData = true;

				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				
				String createDbConnectString = selectedDb.getCreateDbConnectString(databaseURL, databasePort, databaseName);
				
				boolean databaseCreated = DatabaseUtil.createDatabase(createDbConnectString, hibernateDialect, driverClass, user, pass, generateSampleData);

				if (databaseCreated) {
					JOptionPane.showMessageDialog(DatabaseConfigurationDialog.this, "Database created. You can now log in using password 1111.\n" +
							"Do not forget to change password once you are logged in.");
				}
				else {
					JOptionPane.showMessageDialog(DatabaseConfigurationDialog.this, Messages.getString("DatabaseConfigurationDialog.36")); //$NON-NLS-1$
				}
			}
			else if (SAVE.equalsIgnoreCase(command)) {
				dispose();
			}
		} catch (Exception e2) {
			e2.printStackTrace();
			POSMessageDialog.showMessage(this, e2.getMessage());
		} finally {
			setCursor(Cursor.getDefaultCursor());
		}
	}

	private void isAuthorizedToPerformDbChange() {
		DatabaseUtil.initialize();
		
		UserDAO.getInstance().findAll();
		
		String password = JOptionPane.showInputDialog("Enter Administrator password.");
		User user2 = UserDAO.getInstance().findUserBySecretKey(password);
		if(user2 == null || !user2.isAdministrator()) {
			POSMessageDialog.showError(this, "Wrong password");
			return;
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

	public void setTitle(String title) {
		super.setTitle(Messages.getString("DatabaseConfigurationDialog.37")); //$NON-NLS-1$

		titlePanel.setTitle(title);
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

	public static DatabaseConfigurationDialog show(Frame parent) {
		DatabaseConfigurationDialog dialog = new DatabaseConfigurationDialog();
		dialog.setTitle(Messages.getString("DatabaseConfigurationDialog.38")); //$NON-NLS-1$
		dialog.pack();
		dialog.open();

		return dialog;
	}
}
