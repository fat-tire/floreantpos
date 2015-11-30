package com.floreantpos.ui.views.payment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.floreantpos.Messages;

public abstract class AuthorizationDialog extends JDialog {
	private final SwingWorker<Void, Void> worker = new AuthorizingProcess();
	JTextArea txtStatus = new JTextArea();

	public AuthorizationDialog(JDialog parent) {
		super(parent, false);
		setLayout(new BorderLayout(10, 10));
		setTitle(Messages.getString("PaymentProcessWaitDialog.0")); //$NON-NLS-1$

		JLabel label = new JLabel(Messages.getString("PaymentProcessWaitDialog.1")); //$NON-NLS-1$
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(label.getFont().deriveFont(24).deriveFont(Font.BOLD));
		add(label, BorderLayout.NORTH);

		txtStatus.setEditable(false);
		txtStatus.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), new LineBorder(Color.black)));
		txtStatus.setFont(label.getFont().deriveFont(Font.BOLD));
		txtStatus.setBackground(Color.white);
		add(txtStatus, BorderLayout.CENTER);

		setSize(500, 400);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(parent);
	}

	public void startAuthorizing() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				before();
			}
		});

		worker.execute();
	}

	protected void before() {
		setVisible(true);
	}

	public void setStatus(String status, Color color) {
		txtStatus.append(status);
		txtStatus.setForeground(color);
	}

	protected abstract void doInBackground() throws Exception;

	protected abstract void done();

	private class AuthorizingProcess extends SwingWorker<Void, Void> {
		@Override
		protected Void doInBackground() throws Exception {
			AuthorizationDialog.this.doInBackground();

			return null;
		}

		@Override
		protected void done() {
			AuthorizationDialog.this.done();
		}
	}
}