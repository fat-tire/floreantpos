package com.floreantpos.ui.dialog;

import java.awt.Component;

import javax.swing.JOptionPane;

public class ConfirmDeleteDialog {
	public static final int YES = JOptionPane.YES_OPTION;
	public static final int NO = JOptionPane.NO_OPTION;
	public static final int CANCEL = JOptionPane.CANCEL_OPTION;
	static final int OK = JOptionPane.OK_OPTION;
	static final int CLOSED = JOptionPane.CLOSED_OPTION;

	public static int showMessage(Component parent, String message, String title) {
		return JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	}

}
