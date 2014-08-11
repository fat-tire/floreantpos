package com.floreantpos.ui.views.payment;

import com.floreantpos.ui.dialog.POSDialog;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.ui.views.DateEntryView;
import javax.swing.border.TitledBorder;
import com.floreantpos.swing.PosButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.floreantpos.ui.TitlePanel;
import javax.swing.JSeparator;

public class ManualCardEntryDialog extends POSDialog implements CardInputter {
	private CardInputListener cardInputListener;
	
	public ManualCardEntryDialog(CardInputListener cardInputListener) {
		this.cardInputListener = cardInputListener;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		
		createUI();
	}

	private void createUI() {
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[][grow]", "[][][][][][][]"));
		
		JLabel lblCardNumber = new JLabel("Card Number");
		panel.add(lblCardNumber, "cell 0 0,alignx trailing");
		
		POSTextField tfCardNumber = new POSTextField();
		panel.add(tfCardNumber, "cell 1 0,growx");
		
		JLabel lblExpieryMonth = new JLabel("Expiery Month");
		panel.add(lblExpieryMonth, "cell 0 1,alignx trailing");
		
		POSTextField tfExpMonth = new POSTextField();
		tfExpMonth.setColumns(4);
		panel.add(tfExpMonth, "cell 1 1");
		
		JLabel lblExpieryYear = new JLabel("Expiery Year");
		panel.add(lblExpieryYear, "cell 0 2,alignx trailing");
		
		POSTextField tfExpYear = new POSTextField();
		tfExpYear.setColumns(4);
		panel.add(tfExpYear, "cell 1 2");
		
		JLabel lblZipCode = new JLabel("ZIP Code");
		panel.add(lblZipCode, "cell 0 3,alignx trailing");
		
		POSTextField tfZipCode = new POSTextField();
		panel.add(tfZipCode, "cell 1 3,growx");
		
		JLabel lblNewLabel = new JLabel("Card Value Code");
		panel.add(lblNewLabel, "cell 0 4,alignx trailing");
		
		POSTextField tfCardValueCode = new POSTextField();
		panel.add(tfCardValueCode, "cell 1 4,growx");
		
		JLabel lblNameOnCard = new JLabel("Name on Card");
		panel.add(lblNameOnCard, "cell 0 5,alignx trailing");
		
		POSTextField tfCardName = new POSTextField();
		panel.add(tfCardName, "cell 1 5,growx");
		
		JLabel lblAddress = new JLabel("Address");
		panel.add(lblAddress, "cell 0 6,alignx trailing");
		
		POSTextField tfCardAddress = new POSTextField();
		panel.add(tfCardAddress, "cell 1 6,growx");
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);
		
		PosButton btnSwipeCard = new PosButton();
		panel_2.add(btnSwipeCard);
		btnSwipeCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSwipeCardDialog();
			}
		});
		btnSwipeCard.setText("SWIPE CARD");
		
		PosButton btnEnterAuthorizationCode = new PosButton();
		panel_2.add(btnEnterAuthorizationCode);
		btnEnterAuthorizationCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openAuthorizationCodeEntryDialog();
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
		titlePanel.setTitle("Enter Card Manually");
		getContentPane().add(titlePanel, BorderLayout.NORTH);
	}

	protected void openAuthorizationCodeEntryDialog() {
		setCanceled(true);
		dispose();
		
		AuthorizationCodeDialog dialog = new AuthorizationCodeDialog(cardInputListener);
		dialog.pack();
		dialog.open();
	}

	protected void submitCard() {
		setCanceled(false);
		dispose();
		
		cardInputListener.cardInputted(this);
	}

	private void openSwipeCardDialog() {
		setCanceled(true);
		dispose();
		
		SwipeCardDialog swipeCardDialog = new SwipeCardDialog(cardInputListener);
		swipeCardDialog.pack();
		swipeCardDialog.open();
	}

}
