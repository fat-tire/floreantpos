package com.floreantpos.ui.views.payment;

import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.floreantpos.Messages;

public class PaymentProcessWaitDialog extends JDialog {

	public PaymentProcessWaitDialog(JDialog parent) {
		super(parent, false);
		setTitle(Messages.getString("PaymentProcessWaitDialog.0")); //$NON-NLS-1$
		
		JLabel label = new JLabel(Messages.getString("PaymentProcessWaitDialog.1")); //$NON-NLS-1$
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(label.getFont().deriveFont(24).deriveFont(Font.BOLD));
		add(label);
		
		setSize(500, 400);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(parent);
	}

	public PaymentProcessWaitDialog(JFrame parent) {
		super(parent, false);
		setTitle(Messages.getString("PaymentProcessWaitDialog.2")); //$NON-NLS-1$
		
		JLabel label = new JLabel(Messages.getString("PaymentProcessWaitDialog.3")); //$NON-NLS-1$
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(label.getFont().deriveFont(24).deriveFont(Font.BOLD));
		add(label);
		
		setSize(500, 400);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(parent);
	}
}
