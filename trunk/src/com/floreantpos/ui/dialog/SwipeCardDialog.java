package com.floreantpos.ui.dialog;
import com.floreantpos.ui.TitlePanel;
import java.awt.BorderLayout;
import javax.swing.JPasswordField;
import javax.swing.JPanel;
import com.floreantpos.swing.PosButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;

public class SwipeCardDialog extends POSDialog {
	private JPasswordField passwordField;
	private String cardString;
	
	public SwipeCardDialog() {
		
		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle("Swipe card here");
		getContentPane().add(titlePanel, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		PosButton psbtnCancel = new PosButton();
		psbtnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		psbtnCancel.setText("CANCEL");
		panel.add(psbtnCancel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EmptyBorder(20, 10, 20, 10));
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		passwordField = new JPasswordField();
		passwordField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardString = new String(passwordField.getPassword());
				setCanceled(false);
				dispose();
			}
		});
		passwordField.setColumns(30);
		panel_1.add(passwordField);
	}

	public String getCardString() {
		return cardString;
	}
}
