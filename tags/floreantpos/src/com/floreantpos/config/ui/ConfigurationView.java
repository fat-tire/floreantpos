package com.floreantpos.config.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class ConfigurationView extends JPanel {
	private boolean initialized = false;
	
	protected JLabel createLabel(String text) {
		JLabel label = new JLabel(text);
		label.setHorizontalAlignment(JLabel.RIGHT);
		return label;
	}


	protected void addRow(String title, JTextField textField) {
		add(createLabel(title), "newline, grow");
		add(textField, "w 250,height pref");
	}
	
	protected void addRow(String title, JTextField textField, String constraints) {
		add(createLabel(title), "newline, grow");
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
