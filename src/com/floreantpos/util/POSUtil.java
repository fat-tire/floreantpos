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
package com.floreantpos.util;

import java.awt.Window;
import java.net.URLEncoder;

import javax.swing.JOptionPane;

import com.floreantpos.Messages;
import com.floreantpos.PosException;
import com.floreantpos.actions.DrawerAssignmentAction;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.main.Application;
import com.floreantpos.model.Terminal;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class POSUtil {
	public static Window getFocusedWindow() {
		return javax.swing.FocusManager.getCurrentManager().getFocusedWindow();
	}

	public static BackOfficeWindow getBackOfficeWindow() {
		Window[] windows = Window.getWindows();
		for (Window window : windows) {
			if (window instanceof BackOfficeWindow) {
				return (BackOfficeWindow) window;
			}
		}

		return null;
	}

	public static boolean isBlankOrNull(String str) {
		if (str == null) {
			return true;
		}
		if (str.trim().equals("")) { //$NON-NLS-1$
			return true;
		}
		return false;
	}

	public static String escapePropertyKey(String propertyKey) {
		return propertyKey.replaceAll("\\s+", "_"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static boolean getBoolean(String b) {
		if (b == null) {
			return false;
		}

		return Boolean.valueOf(b);
	}

	public static boolean getBoolean(Boolean b) {
		if (b == null) {
			return false;
		}

		return b;
	}

	public static boolean getBoolean(Boolean b, boolean defaultValue) {
		if (b == null) {
			return defaultValue;
		}

		return b;
	}

	public static double getDouble(Double d) {
		if (d == null) {
			return 0;
		}

		return d;
	}

	public static int getInteger(Integer d) {
		if (d == null) {
			return 0;
		}

		return d;
	}

	public static int parseInteger(String s) {
		try {
			return Integer.parseInt(s);
		} catch (Exception x) {
			return 0;
		}
	}

	public static int parseInteger(String s, String parseErrorMessage) {
		try {
			return Integer.parseInt(s);
		} catch (Exception x) {
			throw new PosException(parseErrorMessage);
		}
	}

	public static double parseDouble(String s) {
		try {
			return Double.parseDouble(s);
		} catch (Exception x) {
			return 0;
		}
	}

	public static double parseDouble(String s, String parseErrorMessage, boolean mandatory) {
		try {
			return Double.parseDouble(s);
		} catch (Exception x) {
			if (mandatory) {
				throw new PosException(parseErrorMessage);
			}
			else {
				return 0;
			}
		}
	}

	public static String encodeURLString(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8"); //$NON-NLS-1$
		} catch (Exception x) {
			return s;
		}
	}

	public static boolean isValidPassword(char[] password) {
		for (char c : password) {
			if (!Character.isDigit(c)) {
				return false;
			}
		}

		return true;
	}

	public static boolean checkDrawerAssignment() {
		Terminal terminal = Application.getInstance().getTerminal();
		if (!terminal.isCashDrawerAssigned()) {
			int option = POSMessageDialog.showYesNoQuestionDialog(Application.getPosWindow(), Messages.getString("SwitchboardView.15") + //$NON-NLS-1$
					Messages.getString("SwitchboardView.16"), Messages.getString("SwitchboardView.17")); //$NON-NLS-1$ //$NON-NLS-2$

			if (option == JOptionPane.YES_OPTION) {
				try {
					DrawerAssignmentAction action = new DrawerAssignmentAction();
					action.execute();
					if (!terminal.isCashDrawerAssigned()) {
						showUnableToAcceptPayment();
						return false;
					}
					return true;
				} catch (Exception e) {
					return false;
				}
			}
			else {
				showUnableToAcceptPayment();
				return false;
			}
		}

		return true;
	}

	private static void showUnableToAcceptPayment() {
		POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("SwitchboardView.18")); //$NON-NLS-1$
	}
}
