package com.floreantpos.swing;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.floreantpos.main.Application;

public class MessageDialog {
	private static Logger logger = Logger.getLogger(Application.class);
	
	public static void showError(String errorMessage) {
		JOptionPane.showMessageDialog(Application.getInstance().getBackOfficeWindow(), errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void showError(String errorMessage, Throwable t) {
		logger.error(errorMessage, t);
		JOptionPane.showMessageDialog(Application.getInstance().getBackOfficeWindow(), errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void showError(Throwable t) {
		logger.error("Error", t);
		JOptionPane.showMessageDialog(Application.getInstance().getBackOfficeWindow(), "An unexpected error has occured, you may need to restart the application", "Error", JOptionPane.ERROR_MESSAGE);
	}
}
