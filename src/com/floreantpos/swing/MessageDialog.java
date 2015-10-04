package com.floreantpos.swing;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;

public class MessageDialog {
	private static Logger logger = Logger.getLogger(Application.class);
	
	public static void showError(String errorMessage) {
		JOptionPane.showMessageDialog(Application.getPosWindow(), errorMessage, Messages.getString("MessageDialog.0"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
	}

	public static void showError(String errorMessage, Throwable t) {
		logger.error(errorMessage, t);
		JOptionPane.showMessageDialog(Application.getPosWindow(), errorMessage, Messages.getString("MessageDialog.1"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
	}

	public static void showError(Throwable t) {
		logger.error(Messages.getString("MessageDialog.2"), t); //$NON-NLS-1$
		JOptionPane.showMessageDialog(Application.getPosWindow(), Messages.getString("MessageDialog.3"), Messages.getString("MessageDialog.4"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
