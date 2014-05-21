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
import com.floreantpos.POSConstants;
import com.floreantpos.config.ApplicationConfig;
import com.floreantpos.main.Application;
import com.floreantpos.swing.POSPasswordField;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.util.DatabaseUtil;

public class DatabaseConfigurationDialog extends POSDialog implements ActionListener {
	
	private static final String CLOSE = "close";
	private static final String TEST = "test";
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
		setLayout(new MigLayout("fill","[][fill, grow]",""));
	
		titlePanel = new TitlePanel();
		tfServerAddress = new POSTextField();
		tfServerPort = new POSTextField();
		tfDatabaseName = new POSTextField();
		tfUserName = new POSTextField();
		tfPassword = new POSPasswordField();
		databaseCombo = new JComboBox(Database.values());

		String databaseProviderName = ApplicationConfig.getDatabaseProviderName();
		if(StringUtils.isNotEmpty(databaseProviderName)) {
			databaseCombo.setSelectedItem(Database.getByProviderName(databaseProviderName));
		}

		add(titlePanel, "span, grow, wrap");
		
		add(new JLabel("Database: "));
		add(databaseCombo, "grow, wrap");
		add(new JLabel("Database Server Address" + ":"));
		add(tfServerAddress, "grow, wrap");
		add(new JLabel("Database Server Port" + ":"));
		add(tfServerPort, "grow, wrap");
		add(new JLabel("Database Name" + ":"));
		add(tfDatabaseName, "grow, wrap");
		add(new JLabel("User Name" + ":"));
		add(tfUserName, "grow, wrap");
		add(new JLabel("Database Password" + ":"));
		add(tfPassword, "grow, wrap");
		add(new JSeparator(),"span, grow, gaptop 10");
		
		btnTestConnection = new JButton("Test Connection");
		btnTestConnection.setActionCommand(TEST);
		btnCreateDb = new JButton("Create Database Schema");
		btnCreateDb.setActionCommand("CD");
		btnSave = new JButton("Save");
		btnSave.setActionCommand("SAVE");
		btnExit = new JButton(POSConstants.CLOSE);
		btnExit.setActionCommand(CLOSE);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(btnTestConnection);
		buttonPanel.add(btnCreateDb);
		buttonPanel.add(btnSave);
		buttonPanel.add(btnExit);
		
		add(buttonPanel, "span, grow");
	}

	private void addUIListeners() {
		btnTestConnection.addActionListener(this);
		btnCreateDb.addActionListener(this);
		btnSave.addActionListener(this);
		btnExit.addActionListener(this);
		
		databaseCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Database selectedDb = (Database) databaseCombo.getSelectedItem();
				String databasePort = ApplicationConfig.getDatabasePort();
				if(StringUtils.isEmpty(databasePort)) {
					databasePort = selectedDb.getDefaultPort();
				}
				
				tfServerPort.setText(databasePort);
			}
		});
	}

	private void setFieldValues() {
		Database selectedDb = (Database) databaseCombo.getSelectedItem();
		
		String databaseURL = ApplicationConfig.getDatabaseURL();
		tfServerAddress.setText(databaseURL);
		
		String databasePort = ApplicationConfig.getDatabasePort();
		if(StringUtils.isEmpty(databasePort)) {
			databasePort = selectedDb.getDefaultPort();
		}
		
		tfServerPort.setText(databasePort);
		tfDatabaseName.setText(ApplicationConfig.getDatabaseName());
		tfUserName.setText(ApplicationConfig.getDatabaseUser());
		tfPassword.setText(ApplicationConfig.getDatabasePassword());
	}
	
	public void actionPerformed(ActionEvent e) {
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
		
		if(TEST.equalsIgnoreCase(command)) {
			Application.getInstance().setSystemInitialized(false);
			saveConfig(selectedDb, providerName, databaseURL, databasePort, databaseName, user, pass, connectionString, hibernateDialect);
			
			if(DatabaseUtil.checkConnection(connectionString, hibernateDialect, driverClass, user, pass)) {
				JOptionPane.showMessageDialog(this, "Connection Successfull!");
			}
			else {
				JOptionPane.showMessageDialog(this, "Connection Failed!");
			}
		}
		else if("CD".equals(command)) {
			Application.getInstance().setSystemInitialized(false);
			
			int i = JOptionPane.showConfirmDialog(this, "This will remove existing database schemas, if exists. Proceed?", "Warning", JOptionPane.YES_NO_OPTION);
			if(i != JOptionPane.YES_OPTION) {
				return;
			}
			
			saveConfig(selectedDb, providerName, databaseURL, databasePort, databaseName, user, pass, connectionString, hibernateDialect);
			
			String connectionString2 = selectedDb.getCreateDbConnectString(databaseURL, databasePort, databaseName);
			
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			boolean createDatabase = DatabaseUtil.createDatabase(connectionString2, hibernateDialect, driverClass, user, pass);
			this.setCursor(Cursor.getDefaultCursor());
			
			if(createDatabase) {
				JOptionPane.showMessageDialog(DatabaseConfigurationDialog.this, "Database created.");
			}
			else {
				JOptionPane.showMessageDialog(DatabaseConfigurationDialog.this, "Database creation failed.");
			}
		}
		else if("SAVE".equalsIgnoreCase(command)) {
			saveConfig(selectedDb, providerName, databaseURL, databasePort, databaseName, user, pass, connectionString, hibernateDialect);
		}
		else if(CLOSE.equalsIgnoreCase(command)) {
			dispose();
		}
	}

	private void saveConfig(Database selectedDb, String providerName, String databaseURL, String databasePort, String databaseName, String user, String pass,
			String connectionString, String hibernateDialect) {
		ApplicationConfig.setDatabaseProviderName(providerName);
		ApplicationConfig.setHibernateConnectionDriverClass(selectedDb.getHibernateConnectionDriverClass());
		ApplicationConfig.setHibernateDialect(hibernateDialect);
		ApplicationConfig.setConnectString(connectionString);
		ApplicationConfig.setDatabaseURL(databaseURL);
		ApplicationConfig.setDatabasePort(databasePort);
		ApplicationConfig.setDatabaseName(databaseName);
		ApplicationConfig.setDatabaseUser(user);
		ApplicationConfig.setDatabasePassword(pass);
	}

	public void setTitle(String title) {
		super.setTitle("Configure database");
		
		titlePanel.setTitle(title);
	}
	
	public static DatabaseConfigurationDialog show(Frame parent) {
		DatabaseConfigurationDialog dialog = new DatabaseConfigurationDialog(Application.getPosWindow(), true);
		dialog.setTitle("Configure database");
		dialog.pack();
		dialog.open();
		
		return dialog;
	}
}
