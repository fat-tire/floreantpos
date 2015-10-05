package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;

public class PasswordEntryDialog extends POSDialog implements ActionListener {
	private TitlePanel titlePanel;
	private JPasswordField tfPassword;
	private JLabel statusLabel;
	
	private User user;

	public PasswordEntryDialog() {
		super(Application.getPosWindow(), true);
		init();
	}
	
	public PasswordEntryDialog(Frame parent) {
		super(parent, true);
		
		init();
	}

	private void init() {
		setResizable(false);

		JPanel container = (JPanel) getContentPane();
		container.setBorder(new EmptyBorder(5, 15, 10, 15));
		setLayout(new BorderLayout());

		titlePanel = new TitlePanel();
		add(titlePanel, BorderLayout.NORTH);

		JPanel contentPane = new JPanel(new BorderLayout(10, 10));
		contentPane.setBorder(new EmptyBorder(10, 0, 0, 0));
		add(contentPane);

		JPanel inputPanel = createInputPanel();
		contentPane.add(inputPanel, BorderLayout.NORTH);

		JPanel keyboardPanel = createKeyboardPanel();
		contentPane.add(keyboardPanel);

		JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
		contentPane.add(bottomPanel, BorderLayout.SOUTH);

		bottomPanel.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.NORTH);

		JPanel bottomButtonPanel = new JPanel(new GridLayout(1, 0, 10, 10));
		bottomPanel.add(bottomButtonPanel);

		PosButton btnOk = new PosButton(POSConstants.OK.toUpperCase());
		btnOk.addActionListener(this);
		bottomButtonPanel.add(btnOk);

		PosButton btnCancel = new PosButton(POSConstants.CANCEL.toUpperCase());
		btnCancel.addActionListener(this);
		bottomButtonPanel.add(btnCancel);
	}

	private JPanel createInputPanel() {
		JPanel inputPanel = new JPanel(new BorderLayout(5, 5));

		tfPassword = new JPasswordField();
		tfPassword.setFont(tfPassword.getFont().deriveFont(Font.BOLD, 24));
		tfPassword.setFocusable(true);
		tfPassword.requestFocus();
		tfPassword.setBackground(Color.WHITE);
		tfPassword.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				String secretKey = getPasswordAsString();
				if (secretKey != null && secretKey.length() == TerminalConfig.getDefaultPassLen()) {
					statusLabel.setText(""); //$NON-NLS-1$
					if(checkLogin(secretKey)) {
						setCanceled(false);
						dispose();
					}
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		inputPanel.add(tfPassword, BorderLayout.NORTH);

		statusLabel = new JLabel();
		statusLabel.setHorizontalAlignment(JLabel.CENTER);
		inputPanel.add(statusLabel);

		return inputPanel;
	}

	private JPanel createKeyboardPanel() {
		JPanel buttonPanel = new JPanel(new GridLayout(0, 3, 5, 5));

		String[][] numbers = { { "7", "8", "9" }, { "4", "5", "6" }, { "1", "2", "3" }, { "0", Messages.getString("PasswordEntryDialog.11"), Messages.getString("PasswordEntryDialog.12") } }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$
		String[][] iconNames = new String[][] { { "7.png", "8.png", "9.png" }, { "4.png", "5.png", "6.png" }, { "1.png", "2.png", "3.png" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
				{ "0.png", "clear.png", "clear.png" } }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		Dimension size = new Dimension(120, 80);

		for (int i = 0; i < numbers.length; i++) {
			for (int j = 0; j < numbers[i].length; j++) {
				String buttonText = String.valueOf(numbers[i][j]);

				PosButton posButton = new PosButton();
				if (buttonText.startsWith(Messages.getString("PasswordEntryDialog.25"))) { //$NON-NLS-1$
					posButton.setText(buttonText);
					posButton.addActionListener(this);
				}
				else {
					posButton.setAction(loginAction);
				}

				ImageIcon icon = com.floreantpos.IconFactory.getIcon("/ui_icons/", iconNames[i][j]); //$NON-NLS-1$
				if (icon != null) {
					posButton.setIcon(icon);
				}
				else {
					posButton.setText(buttonText);
				}

				posButton.setPreferredSize(size);
				posButton.setIconTextGap(0);
				posButton.setActionCommand(buttonText);
				buttonPanel.add(posButton);
			}
		}
		return buttonPanel;
	}

	private void doOk() {
//		char[] password = tfPassword.getPassword();
//
//		if (password == null || password.length == 0) {
//			POSMessageDialog.showError(this, "Please enter password");
//			return;
//		}
//
//		boolean validPassword = POSUtil.isValidPassword(password);
//		if (!validPassword) {
//			//POSMessageDialog.showError(this, "The password is not valid. Password can only contain digit.");
//			
//			return;
//		}
//
//		setCanceled(false);
//		dispose();
		
		if(checkLogin(getPasswordAsString())) {
			setCanceled(false);
			dispose();
		}
	}

	private void doCancel() {
		user = null;
		setCanceled(true);
		dispose();
	}

	private void doClearAll() {
		statusLabel.setText(""); //$NON-NLS-1$
		tfPassword.setText(""); //$NON-NLS-1$
	}

	private void doClear() {
		statusLabel.setText(""); //$NON-NLS-1$
		String passwordAsString = getPasswordAsString();
		if (StringUtils.isNotEmpty(passwordAsString)) {
			passwordAsString = passwordAsString.substring(0, passwordAsString.length() - 1);
		}
		tfPassword.setText(passwordAsString);
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

		if (dialog2.isCanceled()) {
			return null;
		}

		return dialog2.getPasswordAsString();
	}
	
	public static User getUser(Component parent, String title) {
		return getUser(parent, title, title);
	}
	
	public static User getUser(Component parent, String windowTitle, String title) {
		PasswordEntryDialog dialog2 = new PasswordEntryDialog();
		dialog2.setTitle(title);
		dialog2.setDialogTitle(windowTitle);
		dialog2.pack();
		dialog2.setLocationRelativeTo(parent);
		dialog2.setVisible(true);
		
		if (dialog2.isCanceled()) {
			return null;
		}
		
		return dialog2.getUser();
	}

	private synchronized boolean checkLogin(String secretKey) {
		user = UserDAO.getInstance().findUserBySecretKey(secretKey);
		if (user == null) {
			statusLabel.setText(Messages.getString("PasswordEntryDialog.30")); //$NON-NLS-1$
			return false;
		}

		return true;
	}

	Action loginAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			tfPassword.setText(getPasswordAsString() + e.getActionCommand());

			String secretKey = getPasswordAsString();
			if (secretKey != null && secretKey.length() == TerminalConfig.getDefaultPassLen()) {
				statusLabel.setText(""); //$NON-NLS-1$
				if(checkLogin(secretKey)) {
					setCanceled(false);
					dispose();
				}
			}
		}
	};

	public User getUser() {
		return user;
	}
}