package com.floreantpos.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

import com.floreantpos.POSConstants;

public class CloseDialogAction extends AbstractAction {
	private JDialog dialog;
	
	public CloseDialogAction(JDialog dialog) {
		super(POSConstants.CLOSE.toUpperCase());
		
		this.dialog = dialog;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		dialog.dispose();
	}

}
