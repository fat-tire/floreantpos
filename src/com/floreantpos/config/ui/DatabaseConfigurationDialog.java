package com.floreantpos.config.ui;

import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
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
import com.floreantpos.swing.POSPasswordField;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.util.DatabaseUtil;

public class DatabaseConfigurationDialog extends POSDialog implements ActionListener {
	
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
	private JButton btnExit;
	private JButton btnSave;
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

	public DatabaseConfigurationDialog(Dialog owner, boolean modal) {
		super(owner, modal);
		
		setFieldValues();
		addUIListeners();
	}

	public DatabaseConfigurationDialog(Frame owner, boolean modal) throws HeadlessException {
		super(owner, modal);
		
		setFieldValues();
		addUIListeners();
	}
	
	protected void initUI() {
		getContentPane().setLayout(new MigLayout("fill","[][fill, grow]","")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
		titlePanel = new TitlePanel();
		tfServerAddress = new POSTextField();
		tfServerPort = new POSTextField();
		tfDatabaseName = new POSTextField();
		tfUserName = new POSTextField();
		tfPassword = new POSPasswordField();
		databaseCombo = new JComboBox(Database.values());

		String databaseProviderName = AppConfig.getDatabaseProviderName();
		if(StringUtils.isNotEmpty(databaseProviderName)) {
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
		getContentPane().add(new JSeparator(),"span, grow, gaptop 10"); //$NON-NLS-1$
		
		btnTestConnection = new JButton(Messages.getString("DatabaseConfigurationDialog.26")); //$NON-NLS-1$
		btnTestConnection.setActionCommand(TEST);
		btnSave = new JButton(Messages.getString("DatabaseConfigurationDialog.27")); //$NON-NLS-1$
		btnSave.setActionCommand(SAVE);
		btnExit = new JButton(Messages.getString("DatabaseConfigurationDialog.28")); //$NON-NLS-1$
		btnExit.setActionCommand(CANCEL);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnCreateDb = new JButton(Messages.getString("DatabaseConfigurationDialog.29")); //$NON-NLS-1$
		btnCreateDb.setActionCommand(CONFIGURE_DB);
		buttonPanel.add(btnCreateDb);
		buttonPanel.add(btnTestConnection);
		buttonPanel.add(btnSave);
		buttonPanel.add(btnExit);
		
		getContentPane().add(buttonPanel, Messages.getString("DatabaseConfigurationDialog.30")); //$NON-NLS-1$
	}

	private void addUIListeners() {
		btnTestConnection.addActionListener(this);
		btnCreateDb.addActionListener(this);
		btnSave.addActionListener(this);
		btnExit.addActionListener(this);
		
		databaseCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Database selectedDb = (Database) databaseCombo.getSelectedItem();
				
				if(selectedDb == Database.DEMO_DATABASE) {
					setFieldsVisible(false);
					return;
				}
				
				setFieldsVisible(true);
				
				String databasePort = AppConfig.getDatabasePort();
				if(StringUtils.isEmpty(databasePort)) {
					databasePort = selectedDb.getDefaultPort();
				}
				
				tfServerPort.setText(databasePort);
			}
		});
	}

	private void setFieldValues() {
		Database selectedDb = (Database) databaseCombo.getSelectedItem();
		
		String databaseURL = AppConfig.getDatabaseURL();
		tfServerAddress.setText(databaseURL);
		
		String databasePort = AppConfig.getDatabasePort();
		if(StringUtils.isEmpty(databasePort)) {
			databasePort = selectedDb.getDefaultPort();
		}
		
		tfServerPort.setText(databasePort);
		tfDatabaseName.setText(AppConfig.getDatabaseName());
		tfUserName.setText(AppConfig.getDatabaseUser());
		tfPassword.setText(AppConfig.getDatabasePassword());
		
		if(selectedDb == Database.DEMO_DATABASE) {
			setFieldsVisible(false);
		}
		else {
			setFieldsVisible(true);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		Database selectedDb = (Database) databaseCombo.getSelectedItem();
		
		String providerName = selectedDb.getEngineName();
		String databaseURL = tfServerAddress.getText();
		String databasePort = tfServerPort.getText();
		String databaseName = tfDatabaseName.getText();
		String user = tfUserName.getText();
		String pass = new String(tfPassword.getPassword());
		
		String connectionString = selectedDb.getConnectString(databaseURL, databasePort, databaseName);
		String hibernateDialect = selectedDb.getHibernateDialect();
		String driverClass = selectedDb.getHibernateConnectionDriverClass();
		
		if(TEST.equalsIgnoreCase(command)) {
			Application.getInstance().setSystemInitialized(false);
			saveConfig(selectedDb, providerName, databaseURL, databasePort, databaseName, user, pass, connectionString, hibernateDialect);
			
			if(DatabaseUtil.checkConnection(connectionString, hibernateDialect, driverClass, user, pass)) {
				JOptionPane.showMessageDialog(this, Messages.getString("DatabaseConfigurationDialog.31")); //$NON-NLS-1$
			}
			else {
				JOptionPane.showMessageDialog(this, Messages.getString("DatabaseConfigurationDialog.32")); //$NON-NLS-1$
			}
		}
		else if(CONFIGURE_DB.equals(command)) {
			Application.getInstance().setSystemInitialized(false);
			
			int i = JOptionPane.showConfirmDialog(this, Messages.getString("DatabaseConfigurationDialog.33"), Messages.getString("DatabaseConfigurationDialog.34"), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
			if(i != JOptionPane.YES_OPTION) {
				return;
			}
			
			saveConfig(selectedDb, providerName, databaseURL, databasePort, databaseName, user, pass, connectionString, hibernateDialect);
			
			String connectionString2 = selectedDb.getCreateDbConnectString(databaseURL, databasePort, databaseName);
			
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			boolean createDatabase = DatabaseUtil.createDatabase(connectionString2, hibernateDialect, driverClass, user, pass);
			this.setCursor(Cursor.getDefaultCursor());
			
			if(createDatabase) {
				JOptionPane.showMessageDialog(DatabaseConfigurationDialog.this, Messages.getString("DatabaseConfigurationDialog.35")); //$NON-NLS-1$
			}
			else {
				JOptionPane.showMessageDialog(DatabaseConfigurationDialog.this, Messages.getString("DatabaseConfigurationDialog.36")); //$NON-NLS-1$
			}
		}
		else if(SAVE.equalsIgnoreCase(command)) {
			saveConfig(selectedDb, providerName, databaseURL, databasePort, databaseName, user, pass, connectionString, hibernateDialect);
			dispose();
		}
		else if(CANCEL.equalsIgnoreCase(command)) {
			dispose();
		}
	}

	private void saveConfig(Database selectedDb, String providerName, String databaseURL, String databasePort, String databaseName, String user, String pass,
			String connectionString, String hibernateDialect) {
		AppConfig.setDatabaseProviderName(providerName);
		AppConfig.setHibernateConnectionDriverClass(selectedDb.getHibernateConnectionDriverClass());
		AppConfig.setHibernateDialect(hibernateDialect);
		AppConfig.setConnectString(connectionString);
		AppConfig.setDatabaseURL(databaseURL);
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
		DatabaseConfigurationDialog dialog = new DatabaseConfigurationDialog(Application.getPosWindow(), true);
		dialog.setTitle(Messages.getString("DatabaseConfigurationDialog.38")); //$NON-NLS-1$
		dialog.pack();
		dialog.open();
		
		return dialog;
	}
}
