/*
 * MessageDialog.java
 *
 * Created on August 23, 2006, 10:45 PM
 */

package com.floreantpos.ui.dialog;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.floreantpos.main.Application;

/**
 *
 * @author  MShahriar
 */
public class POSMessageDialog extends javax.swing.JDialog {
	
	private static Logger logger = Logger.getLogger(Application.class);
    
    public static void showMessage(String message) {
    	JOptionPane.showMessageDialog(Application.getPosWindow(), message, com.floreantpos.POSConstants.MDS_POS, JOptionPane.INFORMATION_MESSAGE, null);
    }
    
    public static void showError(String message) {
    	JOptionPane.showMessageDialog(Application.getPosWindow(), message, com.floreantpos.POSConstants.MDS_POS, JOptionPane.ERROR_MESSAGE, null);
    }

    public static void showError(Component parent, String message) {
    	JOptionPane.showMessageDialog(parent, message, com.floreantpos.POSConstants.MDS_POS, JOptionPane.ERROR_MESSAGE, null);
    }

    public static void showError(String message, Throwable x) {
    	logger.error(message, x);
    	JOptionPane.showMessageDialog(Application.getPosWindow(), message, com.floreantpos.POSConstants.ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE, null);
    }
    public static void showError(Component parent, String message, Throwable x) {
    	logger.error(message, x);
    	JOptionPane.showMessageDialog(parent, message, com.floreantpos.POSConstants.MDS_POS, JOptionPane.ERROR_MESSAGE, null);
    }
}
