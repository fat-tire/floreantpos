package com.floreantpos.ui.views.payment;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.main.Application;
import com.floreantpos.swing.FocusedTextField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;

public class AuthorizationCodeDialog extends POSDialog implements CardInputter {
	private CardInputListener cardInputListener;
	private FocusedTextField tfAuthorizationCode;
	
	public AuthorizationCodeDialog(CardInputListener cardInputListener) {
		super(Application.getPosWindow(), true);
		
		this.cardInputListener = cardInputListener;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		
		createUI();
	}

	private void createUI() {
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[][grow]", "[50][grow]"));
		
		JLabel lblAuthorizationCode = new JLabel("Authorization Code");
		panel.add(lblAuthorizationCode, "cell 0 0,alignx trailing");
		
		tfAuthorizationCode = new FocusedTextField();
		tfAuthorizationCode.setColumns(12);
		panel.add(tfAuthorizationCode, "cell 1 0,growx");
		
		QwertyKeyPad qwertyKeyPad = new QwertyKeyPad();
		panel.add(qwertyKeyPad, "cell 0 1 2 1,grow");
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);
		
		PosButton btnSubmit = new PosButton();
		panel_2.add(btnSubmit);
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(false);
				dispose();
				
				cardInputListener.cardInputted(AuthorizationCodeDialog.this);
			}
		});
		btnSubmit.setText("SUBMIT");
		
		PosButton btnCancel = new PosButton();
		panel_2.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		btnCancel.setText("CANCEL");
		
		JSeparator separator = new JSeparator();
		panel_1.add(separator, BorderLayout.NORTH);
		
		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle("Enter Authorization Code");
		getContentPane().add(titlePanel, BorderLayout.NORTH);
	}
	
	public String getAuthorizationCode() {
		return tfAuthorizationCode.getText();
	}
}
