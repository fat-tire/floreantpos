package com.floreantpos.ui.views.payment;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;

import java.awt.BorderLayout;
import javax.swing.JPasswordField;
import javax.swing.JPanel;

import com.floreantpos.config.CardConfig;
import com.floreantpos.swing.PosButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;
import javax.swing.JSeparator;

public class SwipeCardDialog extends POSDialog implements CardInputter {
	private CardInputListener cardInputListener;
	private JPasswordField passwordField;
	private String cardString;
	private PosButton btnEnterAuthorizationCode;
	private PosButton btnManualEntry;
	
	public SwipeCardDialog(CardInputListener cardInputListener) {
		this.cardInputListener = cardInputListener;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		
		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle("Swipe card here");
		getContentPane().add(titlePanel, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		
		btnManualEntry = new PosButton();
		panel_2.add(btnManualEntry);
		btnManualEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openManualEntry();
			}
		});
		btnManualEntry.setText("MANUAL ENTRY");
		
		btnEnterAuthorizationCode = new PosButton();
		panel_2.add(btnEnterAuthorizationCode);
		btnEnterAuthorizationCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openAuthorizationEntryDialog();
			}
		});
		btnEnterAuthorizationCode.setText("ENTER AUTHORIZATION CODE");
		
		PosButton btnSubmit = new PosButton();
		panel_2.add(btnSubmit);
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				submitCard();
			}
		});
		btnSubmit.setText("SUBMIT");
		
		PosButton psbtnCancel = new PosButton();
		panel_2.add(psbtnCancel);
		psbtnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		psbtnCancel.setText("CANCEL");
		
		JSeparator separator = new JSeparator();
		panel.add(separator, BorderLayout.NORTH);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EmptyBorder(20, 10, 20, 10));
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		passwordField = new JPasswordField();
		passwordField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				submitCard();
			}
		});
		passwordField.setColumns(30);
		panel_1.add(passwordField);
		
		if (!CardConfig.isManualEntrySupported()) {
			btnManualEntry.setEnabled(false);
		}
		if(!CardConfig.isExtTerminalSupported()) {
			btnEnterAuthorizationCode.setEnabled(false);
		}
	}

	protected void openAuthorizationEntryDialog() {
		setCanceled(true);
		dispose();
		
		AuthorizationCodeDialog dialog = new AuthorizationCodeDialog(cardInputListener);
		dialog.setLocationRelativeTo(this);
		dialog.pack();
		dialog.open();
	}

	protected void openManualEntry() {
		setCanceled(true);
		dispose();
		
		ManualCardEntryDialog dialog = new ManualCardEntryDialog(cardInputListener);
		dialog.setLocationRelativeTo(this);
		dialog.pack();
		dialog.open();
	}

	public String getCardString() {
		return cardString;
	}
	
	public void setCardString(String cardString) {
		this.cardString = cardString;
	}

	private void submitCard() {
		cardString = new String(passwordField.getPassword());
		setCanceled(false);
		dispose();
		
		cardInputListener.cardInputted(this);
	}
	
	public void setManualEntryVisible(boolean visible) {
		btnManualEntry.setVisible(visible);
	}
	
	public void setAuthorizationEntryVisible(boolean visible) {
		btnEnterAuthorizationCode.setVisible(visible);
	}
}
