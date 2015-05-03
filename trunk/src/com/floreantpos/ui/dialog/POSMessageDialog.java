/*
 * MessageDialog.java
 *
 * Created on August 23, 2006, 10:45 PM
 */

package com.floreantpos.ui.dialog;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.floreantpos.main.Application;
import com.floreantpos.util.POSUtil;

/**
 * 
 * @author MShahriar
 */
public class POSMessageDialog extends javax.swing.JDialog {

	private static Logger logger = Logger.getLogger(Application.class);
	
	private static void showDialog(String message, int messageType, int optionType) {
		JOptionPane optionPane = new JOptionPane(message, messageType, optionType);
		Object[] options = optionPane.getComponents();
		for (Object object : options) {
			if(object instanceof JPanel) {
				JPanel panel = (JPanel) object;
				Component[] components = panel.getComponents();
				for (Component component : components) {
					if(component instanceof JButton) {
						component.setPreferredSize(new Dimension(component.getPreferredSize().width, 60));
					}
				}
			}
		}
		
		JDialog dialog = optionPane.createDialog(POSUtil.getFocusedWindow(), com.floreantpos.POSConstants.MDS_POS);
		dialog.setVisible(true);
	}

	public static void showMessage(String message) {
		showDialog(message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
		
		//JOptionPane.showMessageDialog(Application.getPosWindow(), message, com.floreantpos.POSConstants.MDS_POS, JOptionPane.INFORMATION_MESSAGE, null);
	}

	
	
	public static void showMessage(Component parent, String message) {
		showDialog(message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
		//JOptionPane.showMessageDialog(parent, message, com.floreantpos.POSConstants.MDS_POS, JOptionPane.INFORMATION_MESSAGE, null);
	}

	public static void showError(String message) {
		showDialog(message, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
//		JOptionPane.showMessageDialog(Application.getPosWindow(), message, com.floreantpos.POSConstants.MDS_POS, JOptionPane.ERROR_MESSAGE, null);
	}

	public static void showError(Component parent, String message) {
		showDialog(message, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
//		JOptionPane.showMessageDialog(parent, message, com.floreantpos.POSConstants.MDS_POS, JOptionPane.ERROR_MESSAGE, null);
	}

	public static void showError(String message, Throwable x) {
		x.printStackTrace();
		logger.error(message, x);
		showDialog(message, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
//		JOptionPane.showMessageDialog(Application.getPosWindow(), message, com.floreantpos.POSConstants.ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE, null);
	}

	public static void showError(Component parent, String message, Throwable x) {
		x.printStackTrace();
		logger.error(message, x);
		showDialog(message, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
//		JOptionPane.showMessageDialog(parent, message, com.floreantpos.POSConstants.MDS_POS, JOptionPane.ERROR_MESSAGE, null);
	}
	
	public static int showYesNoQuestionDialog(Component parent, String message, String title) {
		JOptionPane optionPane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
		Object[] options = optionPane.getComponents();
		for (Object object : options) {
			if(object instanceof JPanel) {
				JPanel panel = (JPanel) object;
				Component[] components = panel.getComponents();
				for (Component component : components) {
					if(component instanceof JButton) {
						component.setPreferredSize(new Dimension(component.getPreferredSize().width, 60));
					}
				}
			}
		}
		
		JDialog dialog = optionPane.createDialog(parent, title);
		dialog.setVisible(true);
		
		Object selectedValue = optionPane.getValue();
		if(selectedValue == null)
            return JOptionPane.CLOSED_OPTION;
		
		return ((Integer) selectedValue).intValue();
	}
}
