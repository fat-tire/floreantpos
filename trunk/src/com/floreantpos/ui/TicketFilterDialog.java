package com.floreantpos.ui;

import java.awt.HeadlessException;

import com.floreantpos.main.Application;
import com.floreantpos.ui.dialog.POSDialog;

public class TicketFilterDialog extends POSDialog {

	public TicketFilterDialog() throws HeadlessException {
		super(Application.getPosWindow(), "Select ticket filters", true);
	}

}
