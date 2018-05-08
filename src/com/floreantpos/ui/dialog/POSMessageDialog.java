/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
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

import com.floreantpos.POSConstants;
import com.floreantpos.config.AppProperties;
import com.floreantpos.main.Application;
import com.floreantpos.ui.RefreshableView;

/**
 * 
 * @author MShahriar
 */
public class POSMessageDialog {

	private static Logger logger = Logger.getLogger(Application.class);
	
	private static void showDialog(Component parent, String message, int messageType, int optionType) {
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
		
		JDialog dialog = optionPane.createDialog(parent, com.floreantpos.POSConstants.MDS_POS);
		dialog.setModal(true);
		dialog.setVisible(true);
	}

	
	public static void showMessage(String message) {
		showDialog(Application.getPosWindow(), message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
	}
	
	public static void showMessage(Component parent, String message) {
		showDialog(parent, message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
	}

	public static void showError(String message) {
		showDialog(Application.getPosWindow(), message, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
	}
	
	public static void showError(Component parent, String message) {
		showDialog(parent, message, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
	}

	public static void showError(Component parent, String message, Throwable x) {
		logger.error(message, x);
		showDialog(parent, message, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
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

	public static int showYesNoQuestionDialog(Component parent, String message, String title, String yesButtonText, String noButtonText) {
		String[] buttonText = { yesButtonText, noButtonText };
		JOptionPane optionPane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, null, buttonText);
		Object[] options = optionPane.getComponents();
		for (Object object : options) {
			if (object instanceof JPanel) {
				JPanel panel = (JPanel) object;
				Component[] components = panel.getComponents();
				for (Component component : components) {
					if (component instanceof JButton) {
						component.setPreferredSize(new Dimension(component.getPreferredSize().width, 60));
					}
				}
			}
		}

		JDialog dialog = optionPane.createDialog(parent, title);
		dialog.setVisible(true);

		String selectedValue = (String) optionPane.getValue();
		if (selectedValue.equals(noButtonText))
			return JOptionPane.CLOSED_OPTION;

		return JOptionPane.YES_OPTION;
	}
	public static void showMessageDialogWithReloadButton(Component parent, RefreshableView refreshView) {
		showMessageDialogWithReloadButton(parent, refreshView, "Data has been changed in other terminal. Please reload this window and try again.");
	}
	public static void showMessageDialogWithReloadButton(Component parent, RefreshableView refreshView, String msg) {
		// @formatter:off
		JOptionPane reloadPane = new JOptionPane(msg, 
				JOptionPane.ERROR_MESSAGE, 
				JOptionPane.YES_NO_OPTION, null, 
				new String[] { "RELOAD", POSConstants.CANCEL.toUpperCase() });
		// @formatter:on

		Object[] options = reloadPane.getComponents();
		for (Object object : options) {
			if (object instanceof JPanel) {
				JPanel panel = (JPanel) object;
				Component[] components = panel.getComponents();
				for (Component component : components) {
					if (component instanceof JButton) {
						component.setPreferredSize(new Dimension(component.getPreferredSize().width, 60));
					}
				}
			}
		}

		JDialog dialog = reloadPane.createDialog(parent == null ? Application.getPosWindow() : parent, AppProperties.getAppName()); 
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setIconImage(Application.getApplicationIcon().getImage());
		dialog.setVisible(true);
		Object selectedValue = (String) reloadPane.getValue();
		if (selectedValue.equals("RELOAD")) {
			if (refreshView != null) {
				refreshView.refresh();
			}
		}
	}
}
