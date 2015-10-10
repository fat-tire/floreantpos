package com.floreantpos.util.datamigrate;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Database;
import com.floreantpos.Messages;

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
		
		JLabel lblDatabase = new JLabel(Messages.getString("DbPanel.0")); //$NON-NLS-1$
		panel.add(lblDatabase);
		
		comboBox = new JComboBox(Database.values());
		panel.add(comboBox);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new MigLayout("", "[][grow]", "[][][][][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		JLabel lblNewLabel_1 = new JLabel(Messages.getString("DbPanel.4")); //$NON-NLS-1$
		panel_1.add(lblNewLabel_1, "cell 0 1,alignx trailing"); //$NON-NLS-1$
		
		tfServer = new JTextField("localhost"); //$NON-NLS-1$
		panel_1.add(tfServer, "cell 1 1,growx"); //$NON-NLS-1$
		tfServer.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel(Messages.getString("DbPanel.8")); //$NON-NLS-1$
		panel_1.add(lblNewLabel_2, "cell 0 2,alignx trailing"); //$NON-NLS-1$
		
		tfPort = new JTextField("51527"); //$NON-NLS-1$
		panel_1.add(tfPort, "cell 1 2,growx"); //$NON-NLS-1$
		tfPort.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel(Messages.getString("DbPanel.12")); //$NON-NLS-1$
		panel_1.add(lblNewLabel_3, "cell 0 3,alignx trailing"); //$NON-NLS-1$
		
		tfDbName = new JTextField("posdb"); //$NON-NLS-1$
		panel_1.add(tfDbName, "cell 1 3,growx"); //$NON-NLS-1$
		tfDbName.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel(Messages.getString("DbPanel.16")); //$NON-NLS-1$
		panel_1.add(lblNewLabel_4, "cell 0 4,alignx trailing"); //$NON-NLS-1$
		
		tfUser = new JTextField("app"); //$NON-NLS-1$
		panel_1.add(tfUser, "cell 1 4,growx"); //$NON-NLS-1$
		tfUser.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel(Messages.getString("DbPanel.20")); //$NON-NLS-1$
		panel_1.add(lblNewLabel_5, "cell 0 5,alignx trailing"); //$NON-NLS-1$
		
		tfPassword = new JTextField("sa"); //$NON-NLS-1$
		panel_1.add(tfPassword, "cell 1 5,growx"); //$NON-NLS-1$
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
