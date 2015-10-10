package com.floreantpos.ui.views.payment;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;

public class ConfirmPayDialog extends POSDialog {
	private JLabel lblInfo;
	public ConfirmPayDialog() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(600, 400);
		setResizable(false);
		
		createUI();
	}
	private void createUI() {
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JSeparator separator = new JSeparator();
		panel.add(separator, BorderLayout.NORTH);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		
		PosButton psbtnConfirm = new PosButton();
		psbtnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(false);
				dispose();
			}
		});
		psbtnConfirm.setText(Messages.getString("ConfirmPayDialog.0")); //$NON-NLS-1$
		panel_1.add(psbtnConfirm);
		
		PosButton psbtnCancel = new PosButton();
		psbtnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		psbtnCancel.setText(Messages.getString("ConfirmPayDialog.1")); //$NON-NLS-1$
		panel_1.add(psbtnCancel);
		
		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle(Messages.getString("ConfirmPayDialog.2")); //$NON-NLS-1$
		getContentPane().add(titlePanel, BorderLayout.NORTH);
		
		lblInfo = new JLabel(""); //$NON-NLS-1$
		lblInfo.setBorder(new EmptyBorder(10, 10, 10, 10));
		lblInfo.setFont(new Font("Dialog", Font.BOLD, 16)); //$NON-NLS-1$
		getContentPane().add(lblInfo, BorderLayout.CENTER);
	}

	public void setMessage(String message) {
		lblInfo.setText(message);
	}
	
	public void setAmount(double amount) {
		lblInfo.setText("<html>You are going to process <b>" + Application.getCurrencySymbol() + amount
							+ "</b>.<br/><br/>If you are sure press <b>CONFIRM</b>, otherwise press <b>CANCEL</b>.<br/><br/></html>");
	}
}
