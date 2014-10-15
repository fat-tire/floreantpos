package com.floreantpos.util.datamigrate;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Database;

public class DbPanel extends JPanel {
	private JTextField tfServer;
	private JTextField tfPort;
	private JTextField tfDbName;
	private JTextField tfUser;
	private JTextField tfPassword;
	private JComboBox comboBox;
	public DbPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		
		JLabel lblDatabase = new JLabel("Database");
		panel.add(lblDatabase);
		
		comboBox = new JComboBox(Database.values());
		panel.add(comboBox);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new MigLayout("", "[][grow]", "[][][][][]"));
		
		JLabel lblNewLabel_1 = new JLabel("Server address");
		panel_1.add(lblNewLabel_1, "cell 0 1,alignx trailing");
		
		tfServer = new JTextField("localhost");
		panel_1.add(tfServer, "cell 1 1,growx");
		tfServer.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Port");
		panel_1.add(lblNewLabel_2, "cell 0 2,alignx trailing");
		
		tfPort = new JTextField("51527");
		panel_1.add(tfPort, "cell 1 2,growx");
		tfPort.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Database name");
		panel_1.add(lblNewLabel_3, "cell 0 3,alignx trailing");
		
		tfDbName = new JTextField("posdb");
		panel_1.add(tfDbName, "cell 1 3,growx");
		tfDbName.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("User");
		panel_1.add(lblNewLabel_4, "cell 0 4,alignx trailing");
		
		tfUser = new JTextField("app");
		panel_1.add(tfUser, "cell 1 4,growx");
		tfUser.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel("Password");
		panel_1.add(lblNewLabel_5, "cell 0 5,alignx trailing");
		
		tfPassword = new JTextField("sa");
		panel_1.add(tfPassword, "cell 1 5,growx");
		tfPassword.setColumns(10);
		
		comboBox.setSelectedIndex(1);
	}
	
	public Database getDatabase() {
		return (Database) comboBox.getSelectedItem();
	}

	public String getConnectString() {
		Database db = (Database) comboBox.getSelectedItem();
		String connectString = db.getConnectString(tfServer.getText(), tfPort.getText(), tfDbName.getText());
		
		return connectString;
	}
	
	public String getUser() {
		return tfUser.getText();
	}
	
	public String getPass() {
		return tfPassword.getText();
	}
}
