package com.floreantpos.ui.views.payment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.floreantpos.Messages;
import com.floreantpos.config.CardConfig;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.dao.PosTransactionDAO;

public class AuthorizationDialog extends JDialog implements Runnable {
	JTextArea txtStatus = new JTextArea();

	private List<PosTransaction> transactions;

	public AuthorizationDialog(JDialog parent, List<PosTransaction> transactions) {
		super(parent, false);
		this.transactions = transactions;

		setLayout(new BorderLayout(10, 10));
		setTitle(Messages.getString("PaymentProcessWaitDialog.0")); //$NON-NLS-1$

		JLabel label = new JLabel(Messages.getString("PaymentProcessWaitDialog.1")); //$NON-NLS-1$
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(label.getFont().deriveFont(24).deriveFont(Font.BOLD));
		add(label, BorderLayout.NORTH);

		txtStatus.setEditable(false);
		txtStatus.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), new LineBorder(Color.black)));
		txtStatus.setFont(label.getFont().deriveFont(Font.BOLD));
		add(txtStatus, BorderLayout.CENTER);

		setSize(500, 400);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(parent);
	}

	@Override
	public void setVisible(boolean b) {
		if (b) {
			SwingUtilities.invokeLater(this);
		}

		super.setVisible(b);
	}

	public void setStatus(String status, Color color) {
		txtStatus.append(status);
	}

	@Override
	public void run() {
		for (PosTransaction transaction : transactions) {
			try {
				CardProcessor cardProcessor = CardConfig.getPaymentGateway().getProcessor();
				cardProcessor.captureAuthorizedAmount(transaction);
				if (transaction.isCaptured()) {
					PosTransactionDAO.getInstance().saveOrUpdate(transaction);
					setStatus("Authorizing transaction id # " + transaction.getId() + " : Success\n", Color.black);
				} else {

				}
				Thread.sleep(6000);

			} catch (InterruptedException x) {

			} catch (Exception e) {
				setStatus("Authorizing transaction id # " + transaction.getId() + " : Failed. " + e.getMessage(), Color.red);
			}
		}

	}
}