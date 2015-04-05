package com.floreantpos.ui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

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

		setLayout(new MigLayout("align 50% 0%, ins 20", "[]20[]", ""));

		add(createLabel("TOTAL AMOUNT" + ":", JLabel.LEFT), "grow");
		lblTotalAmount = createLabel("0.0", JLabel.RIGHT);
		add(lblTotalAmount, "span, grow");

		add(createLabel("TENDERED AMOUNT" + ":", JLabel.LEFT), "newline,grow");
		lblTenderedAmount = createLabel("0.0", JLabel.RIGHT);
		add(lblTenderedAmount, "span, grow");

		add(new JSeparator(), "newline,span, grow");

		add(createLabel("PAID AMOUNT" + ":", JLabel.LEFT), "newline,grow");
		lblPaidAmount = createLabel("0.0", JLabel.RIGHT);
		add(lblPaidAmount, "span, grow");

		add(createLabel("DUE AMOUNT" + ":", JLabel.LEFT), "newline,grow");
		lblDueAmount = createLabel("0.0", JLabel.RIGHT);
		add(lblDueAmount, "span, grow");

		add(new JSeparator(), "newline,span, grow");

		add(createLabel("GRATUITY AMOUNT" + ":", JLabel.LEFT), "newline,grow");
		lblGratuityAmount = createLabel("0.0", JLabel.RIGHT);
		add(lblGratuityAmount, "span, grow");

		add(new JSeparator(), "newline,span, grow");

		add(createLabel("CHANGE DUE" + ":", JLabel.LEFT), "grow");
		lblChangeDue = createLabel("0.0", JLabel.RIGHT);
		add(lblChangeDue, "span, grow");

		add(new JSeparator(), "sg mygroup,newline,span,grow");
		PosButton btnClose = new PosButton("CLOSE");
		btnClose.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}

		});

		PosButton btnPrintStoreCopy = new PosButton("PRINT STORE COPY");
		btnPrintStoreCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					ReceiptPrintService.printTransaction(completedTransaction, false);

				} catch (Exception ee) {
					POSMessageDialog.showError(Application.getPosWindow(), "There was an error while printing.", ee);
				}
				dispose();
			}
		});

		PosButton btnPrintAllCopy = new PosButton("PRINT STORE & MERCHANT COPY");
		btnPrintAllCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					ReceiptPrintService.printTransaction(completedTransaction, true);

				} catch (Exception ee) {
					POSMessageDialog.showError(Application.getPosWindow(), "There was an error while printing.", ee);
				}
				dispose();
			}
		});

		JPanel p = new JPanel();

		if (completedTransaction.isCard()) {
			p.add(btnPrintAllCopy, "newline,skip, h 50");
			p.add(btnPrintStoreCopy, "skip, h 50");
			p.add(btnClose, "skip, h 50");
		}
		else {
			btnPrintStoreCopy.setText("PRINT");
			p.add(btnPrintStoreCopy, "skip, h 50");
			p.add(btnClose, "skip, h 50");
		}

		add(p, "newline, span 2, grow, gaptop 15px");
		//setResizable(false);
	}

	protected JLabel createLabel(String text, int alignment) {
		JLabel label = new JLabel(text);
		label.setFont(new java.awt.Font("Tahoma", 1, 24));
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
