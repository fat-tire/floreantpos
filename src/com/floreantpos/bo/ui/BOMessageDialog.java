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
package com.floreantpos.bo.ui;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.floreantpos.Messages;
import com.floreantpos.PosLog;
import com.floreantpos.main.Application;

public class BOMessageDialog {
	private static Logger logger = Logger.getLogger(Application.class);

	public static void showError(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message, com.floreantpos.POSConstants.MDS_POS, JOptionPane.ERROR_MESSAGE, null);
	}

	public static void showError(Component parent, String message, Throwable x) {
		PosLog.error(parent.getClass(), x.getMessage());
		//logger.error(message, x);
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
