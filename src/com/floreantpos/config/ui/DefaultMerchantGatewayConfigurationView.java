/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.config.ui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.config.CardConfig;
import com.floreantpos.model.CardReader;
import com.floreantpos.swing.POSTextField;

public class DefaultMerchantGatewayConfigurationView extends ConfigurationView {
	private POSTextField tfMerchantAccount;
	private JComboBox cbCardReader;
	private JPasswordField tfMerchantPass;
	private JCheckBox cbSandboxMode;
	private JCheckBox chckbxAllowMagneticSwipe;
	private JCheckBox chckbxAllowCardManual;
	private JCheckBox chckbxAllowExternalTerminal;
	private JButton btnCreateNewMerchantAccount;
	private String link = "http://reseller.authorize.net/application/?resellerId=27144"; //$NON-NLS-1$

	public DefaultMerchantGatewayConfigurationView() {
		setLayout(new MigLayout("", "[][grow]", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		JLabel lblMagneticCardReader = new JLabel(Messages.getString("CardConfigurationView.9")); //$NON-NLS-1$
		add(lblMagneticCardReader, "cell 0 3,alignx leading"); //$NON-NLS-1$

		cbCardReader = new JComboBox();
		cbCardReader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateCheckBoxes();
			}
		});
		add(cbCardReader, "cell 1 3,growx"); //$NON-NLS-1$

		JLabel lblMerchantAccount = new JLabel(Messages.getString("CardConfigurationView.19")); //$NON-NLS-1$
		add(lblMerchantAccount, "cell 0 5,alignx leading"); //$NON-NLS-1$

		tfMerchantAccount = new POSTextField();
		add(tfMerchantAccount, "cell 1 5,growx"); //$NON-NLS-1$

		JLabel lblSecretCode = new JLabel(Messages.getString("CardConfigurationView.22")); //$NON-NLS-1$
		add(lblSecretCode, "cell 0 6,alignx leading"); //$NON-NLS-1$

		cbCardReader.setModel(new DefaultComboBoxModel<CardReader>(CardReader.values()));

		tfMerchantPass = new JPasswordField();
		add(tfMerchantPass, "cell 1 6,growx"); //$NON-NLS-1$

		chckbxAllowMagneticSwipe = new JCheckBox(Messages.getString("CardConfigurationView.3")); //$NON-NLS-1$
		chckbxAllowMagneticSwipe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateCardList();
			}
		});
		add(chckbxAllowMagneticSwipe, "skip 1, newline"); //$NON-NLS-1$

		chckbxAllowCardManual = new JCheckBox(Messages.getString("CardConfigurationView.5")); //$NON-NLS-1$
		chckbxAllowCardManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateCardList();
			}
		});
		add(chckbxAllowCardManual, "skip 1, newline"); //$NON-NLS-1$

		chckbxAllowExternalTerminal = new JCheckBox(Messages.getString("CardConfigurationView.7")); //$NON-NLS-1$
		chckbxAllowExternalTerminal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateCardList();
			}
		});
		add(chckbxAllowExternalTerminal, "skip 1, newline"); //$NON-NLS-1$

		cbSandboxMode = new JCheckBox(Messages.getString("CardConfigurationView.25")); //$NON-NLS-1$
		add(cbSandboxMode, "skip 1, newline"); //$NON-NLS-1$

		btnCreateNewMerchantAccount = new JButton(Messages.getString(Messages.getString("DefaultMerchantGatewayConfigurationView.1"))); //$NON-NLS-1$
		btnCreateNewMerchantAccount.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					openBrowser(link);
				} catch (Exception e1) {
				}
			}
		});
		btnCreateNewMerchantAccount.setForeground(Color.RED);
		btnCreateNewMerchantAccount.setFont(new Font(getFont().getName(), Font.BOLD, 11));

		add(btnCreateNewMerchantAccount, "skip 1, newline"); //$NON-NLS-1$
	}

	private void openBrowser(String link) throws Exception {
		URI uri = new URI(link);
		if (Desktop.isDesktopSupported()) {
			Desktop.getDesktop().browse(uri);
		}
	}

	@Override
	public void initialize() throws Exception {
		chckbxAllowMagneticSwipe.setSelected(CardConfig.isSwipeCardSupported());
		chckbxAllowCardManual.setSelected(CardConfig.isManualEntrySupported());
		chckbxAllowExternalTerminal.setSelected(CardConfig.isExtTerminalSupported());

		CardReader card = CardConfig.getCardReader();
		cbCardReader.setSelectedItem(card);

		String merchantAccount = CardConfig.getMerchantAccount();
		if (merchantAccount != null) {
			tfMerchantAccount.setText(merchantAccount);
		}

		String merchantPass = CardConfig.getMerchantPass();
		if (merchantPass != null) {
			tfMerchantPass.setText(merchantPass);
		}

		cbSandboxMode.setSelected(CardConfig.isSandboxMode());

		updateCardList();
	}

	public void setMerchantDefaultValue(String accountNo, String pass) {
		tfMerchantAccount.setText(accountNo); //$NON-NLS-1$
		tfMerchantPass.setText(pass); //$NON-NLS-1$
	}

	public void setVisibleLinkButton(String btnText, String link, boolean visible) {
		this.link = link;
		btnCreateNewMerchantAccount.setText(btnText);
		btnCreateNewMerchantAccount.setVisible(visible);
	}

	protected void updateCheckBoxes() {
		CardReader selectedItem = (CardReader) cbCardReader.getSelectedItem();
		if (selectedItem == CardReader.SWIPE) {
			chckbxAllowMagneticSwipe.setSelected(true);
		}
		else if (selectedItem == CardReader.MANUAL) {
			chckbxAllowCardManual.setSelected(true);
		}
		else if (selectedItem == CardReader.EXTERNAL_TERMINAL) {
			chckbxAllowExternalTerminal.setSelected(true);
		}
	}

	private DefaultComboBoxModel<CardReader> createComboBoxModel(Vector items) {
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		for (Object object : items) {
			model.addElement(object);
		}
		return model;
	}

	protected void updateCardList() {
		boolean swipeSupported = chckbxAllowMagneticSwipe.isSelected();
		boolean manualSupported = chckbxAllowCardManual.isSelected();
		boolean extSupported = chckbxAllowExternalTerminal.isSelected();

		CardReader currentReader = (CardReader) cbCardReader.getSelectedItem();
		Vector<CardReader> readers = new Vector<CardReader>(3);

		if (swipeSupported) {
			readers.add(CardReader.SWIPE);
		}

		if (manualSupported) {
			readers.add(CardReader.MANUAL);
		}

		if (extSupported) {
			readers.add(CardReader.EXTERNAL_TERMINAL);
		}

		cbCardReader.setModel(createComboBoxModel(readers));
		if (readers.contains(currentReader)) {
			cbCardReader.setSelectedItem(currentReader);
		}

		if (!swipeSupported && !manualSupported && !extSupported) {
			cbCardReader.setEnabled(false);
			//cbGateway.setEnabled(false);
			tfMerchantAccount.setEnabled(false);
			tfMerchantPass.setEnabled(false);
			cbSandboxMode.setEnabled(false);
		}
		else {
			cbCardReader.setEnabled(true);
			//cbGateway.setEnabled(true);
			tfMerchantAccount.setEnabled(true);
			tfMerchantPass.setEnabled(true);
			cbSandboxMode.setEnabled(true);
		}

		if (swipeSupported || manualSupported) {
			//cbGateway.setEnabled(true);
			tfMerchantAccount.setEnabled(true);
			tfMerchantPass.setEnabled(true);
			cbSandboxMode.setEnabled(true);
		}
		else {
			//cbGateway.setEnabled(false);
			tfMerchantAccount.setEnabled(false);
			tfMerchantPass.setEnabled(false);
			cbSandboxMode.setEnabled(false);
		}
	}

	@Override
	public boolean save() throws Exception {
		CardConfig.setSwipeCardSupported(chckbxAllowMagneticSwipe.isSelected());
		CardConfig.setManualEntrySupported(chckbxAllowCardManual.isSelected());
		CardConfig.setExtTerminalSupported(chckbxAllowExternalTerminal.isSelected());

		CardReader cardReader = (CardReader) cbCardReader.getSelectedItem();
		CardConfig.setCardReader(cardReader);

		CardConfig.setMerchantAccount(tfMerchantAccount.getText());
		CardConfig.setMerchantPass(new String(tfMerchantPass.getPassword()));

		CardConfig.setSandboxMode(cbSandboxMode.isSelected());

		return true;
	}

	@Override
	public String getName() {
		return ""; //$NON-NLS-1$
	}
}
