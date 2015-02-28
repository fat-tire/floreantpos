package com.floreantpos.ui.views.payment;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import com.floreantpos.config.CardConfig;
import com.floreantpos.main.Application;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;

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
		
		if(Application.getInstance().isDevelopmentMode()) {
			//passwordField.setText("%B4003000050006781^TEST/MPS^15120000000000000?;4003000050006781=15120000000000000000?|0600|1F4B7720ACF3F616EC857FEE20A771A300DE8E6F742B56A3DBD7E657E7A088293C49E97C3D2CE20DC85B12EC1A0B6FD7|AAFB0F7D8DDB8F889C6FCECFA7B3227AB162706D81E1416E714A57BC6E7560BB70F23B005383C10E||61401000|B24E7B8C1E7155FBE9258C3EA10A0D74366BED9F4EE2F1A117B9BAB63DC548C8328B45895A973091D5B2F113220D861C3FB802566D77DD49|B283724092214AA|BFEFD0C3A3CC5F37|9012090B283724000004|552C||1000");
			passwordField.setText("%B4111111111111111^SHAH/RIAR^1803101000000000020000831000000?;4111111111111111=1803101000020000831?");
		}
		
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
