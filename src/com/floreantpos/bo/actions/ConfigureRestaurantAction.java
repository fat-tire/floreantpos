package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.floreantpos.config.ui.ConfigurationDialog;

public class ConfigureRestaurantAction extends AbstractAction {

	public ConfigureRestaurantAction() {
		super(com.floreantpos.POSConstants.CONFIGURATION);
	}

	public ConfigureRestaurantAction(String name) {
		super(name);
	}

	public ConfigureRestaurantAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		ConfigurationDialog dialog = new ConfigurationDialog();
		dialog.pack();
		dialog.open();
	}

}
