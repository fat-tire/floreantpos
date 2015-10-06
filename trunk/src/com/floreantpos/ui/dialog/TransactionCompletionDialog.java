package com.floreantpos.ui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.report.ReceiptPrintService;
import com.floreantpos.swing.PosButton;
import com.floreantpos.util.NumberUtil;

public class TransactionCompletionDialog extends POSDialog {
	//private List<Ticket> tickets;
	private double tenderedAmount;
	private double totalAmount;
	private double paidAmount;
	private double dueAmount;
	private double gratuityAmount;
	private double changeAmount;

	private JLabel lblTenderedAmount;
	private JLabel lblTotalAmount;
	private JLabel lblPaidAmount;
	private JLabel lblDueAmount;
	private JLabel lblChangeDue;
	private JLabel lblGratuityAmount;

	private PosTransaction completedTransaction;

	public TransactionCompletionDialog(PosTransaction transaction) {
		this.completedTransaction = transaction;

		setTitle(com.floreantpos.POSConstants.TRANSACTION_COMPLETED);

		setLayout(new MigLayout("align 50% 0%, ins 20", "[]20[]", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		add(createLabel(Messages.getString("TransactionCompletionDialog.3") + ":", JLabel.LEFT), "grow"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		lblTotalAmount = createLabel("0.0", JLabel.RIGHT); //$NON-NLS-1$
		add(lblTotalAmount, "span, grow"); //$NON-NLS-1$

		add(createLabel(Messages.getString("TransactionCompletionDialog.8") + ":", JLabel.LEFT), "newline,grow"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		lblTenderedAmount = createLabel("0.0", JLabel.RIGHT); //$NON-NLS-1$
		add(lblTenderedAmount, "span, grow"); //$NON-NLS-1$

		add(new JSeparator(), "newline,span, grow"); //$NON-NLS-1$

		add(createLabel(Messages.getString("TransactionCompletionDialog.14") + ":", JLabel.LEFT), "newline,grow"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		lblPaidAmount = createLabel("0.0", JLabel.RIGHT); //$NON-NLS-1$
		add(lblPaidAmount, "span, grow"); //$NON-NLS-1$

		add(createLabel(Messages.getString("TransactionCompletionDialog.19") + ":", JLabel.LEFT), "newline,grow"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		lblDueAmount = createLabel("0.0", JLabel.RIGHT); //$NON-NLS-1$
		add(lblDueAmount, "span, grow"); //$NON-NLS-1$

		add(new JSeparator(), "newline,span, grow"); //$NON-NLS-1$

		add(createLabel(Messages.getString("TransactionCompletionDialog.25") + ":", JLabel.LEFT), "newline,grow"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		lblGratuityAmount = createLabel("0.0", JLabel.RIGHT); //$NON-NLS-1$
		add(lblGratuityAmount, "span, grow"); //$NON-NLS-1$

		add(new JSeparator(), "newline,span, grow"); //$NON-NLS-1$

		add(createLabel(Messages.getString("TransactionCompletionDialog.31") + ":", JLabel.LEFT), "grow"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		lblChangeDue = createLabel("0.0", JLabel.RIGHT); //$NON-NLS-1$
		add(lblChangeDue, "span, grow"); //$NON-NLS-1$

		add(new JSeparator(), "sg mygroup,newline,span,grow"); //$NON-NLS-1$
		PosButton btnClose = new PosButton(Messages.getString("TransactionCompletionDialog.37")); //$NON-NLS-1$
		btnClose.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}

		});

		PosButton btnPrintStoreCopy = new PosButton(Messages.getString("TransactionCompletionDialog.38")); //$NON-NLS-1$
		btnPrintStoreCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					ReceiptPrintService.printTransaction(completedTransaction, false);

				} catch (Exception ee) {
					POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("TransactionCompletionDialog.39"), ee); //$NON-NLS-1$
				}
				dispose();
			}
		});

		PosButton btnPrintAllCopy = new PosButton(Messages.getString("TransactionCompletionDialog.40")); //$NON-NLS-1$
		btnPrintAllCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					ReceiptPrintService.printTransaction(completedTransaction, true);

				} catch (Exception ee) {
					POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("TransactionCompletionDialog.41"), ee); //$NON-NLS-1$
				}
				dispose();
			}
		});

		JPanel p = new JPanel();

		if (completedTransaction.isCard()) {
			p.add(btnPrintAllCopy, "newline,skip, h 50"); //$NON-NLS-1$
			p.add(btnPrintStoreCopy, "skip, h 50"); //$NON-NLS-1$
			p.add(btnClose, "skip, h 50"); //$NON-NLS-1$
		}
		else {
			btnPrintStoreCopy.setText(Messages.getString("TransactionCompletionDialog.0")); //$NON-NLS-1$
			p.add(btnPrintStoreCopy, "skip, h 50"); //$NON-NLS-1$
			p.add(btnClose, "skip, h 50"); //$NON-NLS-1$
		}

		add(p, "newline, span 2, grow, gaptop 15px"); //$NON-NLS-1$
		//setResizable(false);
	}

	protected JLabel createLabel(String text, int alignment) {
		JLabel label = new JLabel(text);
		label.setFont(new java.awt.Font("Tahoma", 1, 24)); //$NON-NLS-1$
		//label.setForeground(new java.awt.Color(255, 102, 0));
		label.setHorizontalAlignment(alignment);
		label.setText(text);
		return label;
	}

	public double getTenderedAmount() {
		return tenderedAmount;
	}

	public void setTenderedAmount(double amountTendered) {
		this.tenderedAmount = amountTendered;
	}

	public void updateView() {
		lblTotalAmount.setText(NumberUtil.formatNumber(totalAmount));
		lblTenderedAmount.setText(NumberUtil.formatNumber(tenderedAmount));
		lblPaidAmount.setText(NumberUtil.formatNumber(paidAmount));
		lblDueAmount.setText(NumberUtil.formatNumber(dueAmount));
		lblGratuityAmount.setText(NumberUtil.formatNumber(gratuityAmount));
		lblChangeDue.setText(NumberUtil.formatNumber(changeAmount));
	}


	public double getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(double dueAmount) {
		this.dueAmount = dueAmount;
	}

	public double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getGratuityAmount() {
		return gratuityAmount;
	}

	public void setGratuityAmount(double gratuityAmount) {
		this.gratuityAmount = gratuityAmount;
	}

	public double getChangeAmount() {
		return changeAmount;
	}

	public void setChangeAmount(double changeAmount) {
		this.changeAmount = changeAmount;
	}

	public void setCompletedTransaction(PosTransaction completedTransaction) {
		this.completedTransaction = completedTransaction;
	}
}
