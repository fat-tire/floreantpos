package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.POSConstants;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.util.POSUtil;

public class PasswordEntryDialog extends POSDialog implements ActionListener {
	private TitlePanel titlePanel;
	private JPasswordField tfPassword = new JPasswordField();

	public PasswordEntryDialog() {
		init();
	}

	private void init() {
		setResizable(false);
		
		JPanel container = (JPanel) getContentPane();
		container.setBorder(new EmptyBorder(5,5,5,5));
		setLayout(new BorderLayout());

		titlePanel = new TitlePanel();
		add(titlePanel, BorderLayout.NORTH);

		JPanel contentPane = new JPanel(new BorderLayout(10, 10));
		contentPane.setBorder(new EmptyBorder(10,0,0,0));
		add(contentPane);

		tfPassword.setFont(tfPassword.getFont().deriveFont(Font.BOLD, 24));
		tfPassword.setFocusable(true);
		tfPassword.requestFocus();
		tfPassword.setBackground(Color.WHITE);
		contentPane.add(tfPassword, BorderLayout.NORTH);

		// PosButton posButton = new PosButton(POSConstants.CLEAR_ALL);
		// posButton.setFocusable(false);
		// posButton.setMinimumSize(new Dimension(25, 23));
		// posButton.addActionListener(this);
		// contentPane.add(posButton, "growy,height 55,wrap");
		
		JPanel buttonPanel = new JPanel(new GridLayout(0, 3, 5, 5));
		contentPane.add(buttonPanel);

		String[][] numbers = { { "7", "8", "9" }, { "4", "5", "6" }, { "1", "2", "3" }, { "", "0", "CLEAR" } };
//		String[][] iconNames = new String[][] { { "7_32.png", "8_32.png", "9_32.png" }, { "4_32.png", "5_32.png", "6_32.png" },
//				{ "1_32.png", "2_32.png", "3_32.png" }, { "", "0_32.png", "clear_32.png" } };

		Dimension size = new Dimension(80, 80);
		
		for (int i = 0; i < numbers.length; i++) {
			for (int j = 0; j < numbers[i].length; j++) {
				PosButton posButton = new PosButton();
				posButton.setPreferredSize(size);
				String buttonText = String.valueOf(numbers[i][j]);

				posButton.setText(buttonText);
				posButton.setActionCommand(buttonText);
				posButton.addActionListener(this);
				buttonPanel.add(posButton);
			}
		}
		
		JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
		bottomPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		
		bottomPanel.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.NORTH);
		
		JPanel bottomButtonPanel = new JPanel(new GridLayout(1, 0, 10, 10));
		bottomPanel.add(bottomButtonPanel);
		
		PosButton btnOk = new PosButton(POSConstants.OK.toUpperCase());
		btnOk.setPreferredSize(size);
		btnOk.addActionListener(this);
		bottomButtonPanel.add(btnOk);
		
		PosButton btnCancel = new PosButton(POSConstants.CANCEL.toUpperCase());
		btnCancel.setPreferredSize(size);
		btnCancel.addActionListener(this);
		bottomButtonPanel.add(btnCancel);
	}

	private void doOk() {
		char[] password = tfPassword.getPassword();

		if (password == null || password.length == 0) {
			POSMessageDialog.showError(this, "Please enter password");
			return;
		}

		boolean validPassword = POSUtil.isValidPassword(password);
		if (!validPassword) {
			POSMessageDialog.showError(this, "The password is not valid. Password can only contain digit.");
			return;
		}

		setCanceled(false);
		dispose();
	}

	private void doCancel() {
		setCanceled(true);
		dispose();
	}

	private void doClearAll() {
		tfPassword.setText("");
	}

	private void doClear() {
		tfPassword.setText("");
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();

		if (POSConstants.CANCEL.equalsIgnoreCase(actionCommand)) {
			doCancel();
		}
		else if (POSConstants.OK.equalsIgnoreCase(actionCommand)) {
			doOk();
		}
		else if (actionCommand.equals(POSConstants.CLEAR_ALL)) {
			doClearAll();
		}
		else if (actionCommand.equals(POSConstants.CLEAR)) {
			doClear();
		}
		else {
			if (StringUtils.isNotEmpty(actionCommand)) {
				tfPassword.setText(getPasswordAsString() + actionCommand);
			}
		}
	}

	public void setTitle(String title) {
		titlePanel.setTitle(title);

		super.setTitle(title);
	}

	public void setDialogTitle(String title) {
		super.setTitle(title);
	}

	private String getPasswordAsString() {
		return new String(tfPassword.getPassword());
	}

	public static void main(String[] args) {
		PasswordEntryDialog dialog2 = new PasswordEntryDialog();
		dialog2.pack();
		dialog2.setVisible(true);
	}

	public static String show(Component parent, String title) {
		PasswordEntryDialog dialog2 = new PasswordEntryDialog();
		dialog2.setTitle(title);
		dialog2.pack();
		dialog2.setLocationRelativeTo(parent);
		dialog2.setVisible(true);

		return dialog2.getPasswordAsString();
	}
}
