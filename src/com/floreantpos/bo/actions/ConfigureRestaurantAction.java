package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.floreantpos.config.ui.ConfigurationDialog;
import com.floreantpos.main.Application;

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
		ConfigurationDialog dialog = new ConfigurationDialog(Application.getInstance().getBackOfficeWindow());
		dialog.pack();
		dialog.open();
	}

}
