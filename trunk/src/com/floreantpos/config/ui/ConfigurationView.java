package com.floreantpos.config.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public abstract class ConfigurationView extends JPanel {
	private boolean initialized = false;
	
	public ConfigurationView() {
		setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(10, 10, 10, 10)));
	}
	
	protected JLabel createLabel(String text) {
		JLabel label = new JLabel(text);
		label.setHorizontalAlignment(JLabel.RIGHT);
		return label;
	}


	protected void addRow(String title, JTextField textField) {
		add(createLabel(title), "newline, grow"); //$NON-NLS-1$
		add(textField, "w 250,height pref"); //$NON-NLS-1$
	}
	
	protected void addRow(String title, JTextField textField, String constraints) {
		add(createLabel(title), "newline, grow"); //$NON-NLS-1$
		add(textField, constraints);
	}
	
	public abstract boolean save() throws Exception;
	public abstract void initialize() throws Exception;
	public abstract String getName();

	public boolean isInitialized() {
		return initialized;
	}


	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
}
