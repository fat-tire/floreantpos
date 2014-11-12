package com.floreantpos.util;

import java.awt.Dialog;
import java.awt.Window;

import javax.swing.text.JTextComponent;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.ui.dialog.NumberSelectionDialog2;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class PosGuiUtil {
	public static int captureTableNumber() {
		NumberSelectionDialog2 dialog = new NumberSelectionDialog2();
		dialog.setTitle(POSConstants.ENTER_A_TABLE_NUMBER);
		dialog.pack();
		dialog.open();

		if (dialog.isCanceled()) {
			return -1;
		}

		int tableNumber = (int) dialog.getValue();
		if (tableNumber == 0) {
			POSMessageDialog.showError(Application.getPosWindow(), POSConstants.TABLE_NUMBER_CANNOT_BE_0);
			return -1;
		}

		return tableNumber;
	}

	public static int captureGuestNumber() {
		NumberSelectionDialog2 dialog = new NumberSelectionDialog2();
		dialog.setTitle(POSConstants.ENTER_NUMBER_OF_GUEST);
		dialog.pack();
		dialog.open();

		if (dialog.isCanceled()) {
			return -1;
		}

		int numberOfGuests = (int) dialog.getValue();
		if (numberOfGuests == 0) {
			POSMessageDialog.showError(Application.getPosWindow(), POSConstants.GUEST_NUMBER_CANNOT_BE_0);
			return -1;
		}

		return numberOfGuests;
	}

	public static Double parseDouble(JTextComponent textComponent) {
		String text = textComponent.getText();
		try {
			return Double.parseDouble(text);
		} catch (Exception e) {
			return 0.0;
		}
	}

	public static boolean isModalDialogShowing() {
		Window[] windows = Window.getWindows();
		if (windows != null) { // don't rely on current implementation, which at least returns [0].
			for (Window w : windows) {
				if (w.isShowing() && w instanceof Dialog && ((Dialog) w).isModal())
					return true;
			}
		}
		return false;
	}
}
