package com.floreantpos.bo.ui;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;

public class BOMessageDialog {
	private static Logger logger = Logger.getLogger(Application.class);

	public static void showError(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message, com.floreantpos.POSConstants.MDS_POS, JOptionPane.ERROR_MESSAGE, null);
	}

	public static void showError(Component parent, String message, Throwable x) {
		x.printStackTrace();
		logger.error(message, x);
		JOptionPane.showMessageDialog(parent, message, com.floreantpos.POSConstants.MDS_POS, JOptionPane.ERROR_MESSAGE, null);
	}

	public static void showError(String errorMessage) {
		JOptionPane.showMessageDialog(com.floreantpos.util.POSUtil.getFocusedWindow(), errorMessage, Messages.getString("BOMessageDialog.0"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
	}

	public static void showError(String errorMessage, Throwable t) {
		logger.error(errorMessage, t);
		JOptionPane.showMessageDialog(com.floreantpos.util.POSUtil.getFocusedWindow(), errorMessage, Messages.getString("BOMessageDialog.1"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
	}

	public static void showError(Throwable t) {
		logger.error(Messages.getString("BOMessageDialog.2"), t); //$NON-NLS-1$
		JOptionPane.showMessageDialog(com.floreantpos.util.POSUtil.getFocusedWindow(), Messages.getString("BOMessageDialog.3"), Messages.getString("BOMessageDialog.4"), //$NON-NLS-1$ //$NON-NLS-2$
				JOptionPane.ERROR_MESSAGE);
	}
}
