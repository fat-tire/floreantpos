package com.floreantpos.config.ui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.config.CardConfig;
import com.floreantpos.model.CardReader;
import com.floreantpos.model.MerchantGateway;
import com.floreantpos.swing.POSTextField;
import javax.swing.JCheckBox;

public class CardConfigurationView extends ConfigurationView {
	//private List<String>

	private POSTextField tfMerchantAccount;
	private JComboBox cbGateway;
	private JComboBox cbCardReader;
	private JPasswordField tfMerchantPass;
	private JCheckBox cbSandboxMode;

	public CardConfigurationView() {
		createUI();
	}

	private void createUI() {
		setLayout(new MigLayout("", "[][grow]", "[][][][][]"));

		JLabel lblMagneticCardReader = new JLabel("Magnetic Card Reader");
		add(lblMagneticCardReader, "cell 0 0,alignx trailing");

		cbCardReader = new JComboBox();
		add(cbCardReader, "cell 1 0,growx");

		JLabel lblMerchantGateway = new JLabel("Merchant Gateway");
		add(lblMerchantGateway, "cell 0 1,alignx trailing");

		cbGateway = new JComboBox();
		add(cbGateway, "cell 1 1,growx");

		JLabel lblMerchantAccount = new JLabel("Merchant Account");
		add(lblMerchantAccount, "cell 0 2,alignx trailing");

		tfMerchantAccount = new POSTextField();
		add(tfMerchantAccount, "cell 1 2,growx");

		JLabel lblSecretCode = new JLabel("Secret Code");
		add(lblSecretCode, "cell 0 3,alignx trailing");

		cbCardReader.setModel(new DefaultComboBoxModel<CardReader>(CardReader.values()));
		cbGateway.setModel(new DefaultComboBoxModel<MerchantGateway>(MerchantGateway.values()));

		tfMerchantPass = new JPasswordField();
		add(tfMerchantPass, "cell 1 3,growx");
		
		cbSandboxMode = new JCheckBox("Sandbox mode");
		add(cbSandboxMode, "cell 1 4");
	}

	@Override
	public boolean save() throws Exception {
		CardReader cardReader = (CardReader) cbCardReader.getSelectedItem();
		CardConfig.setCardReader(cardReader);
		
		MerchantGateway gateway = (MerchantGateway) cbGateway.getSelectedItem();
		CardConfig.setMerchantGateway(gateway);
		
		CardConfig.setMerchantAccount(tfMerchantAccount.getText());
		CardConfig.setMerchantPass(new String(tfMerchantPass.getPassword()));
		
		CardConfig.setSandboxMode(cbSandboxMode.isSelected());
		
		return true;
	}

	@Override
	public void initialize() throws Exception {
		CardReader card = CardConfig.getCardReader();
		cbCardReader.setSelectedItem(card);

		MerchantGateway merchantGateway = CardConfig.getMerchantGateway();
		cbGateway.setSelectedItem(merchantGateway);

		String merchantAccount = CardConfig.getMerchantAccount();
		if (merchantAccount != null) {
			tfMerchantAccount.setText(merchantAccount);
		}

		String merchantPass = CardConfig.getMerchantPass();
		if (merchantPass != null) {
			tfMerchantPass.setText(merchantPass);
		}
		
		cbSandboxMode.setSelected(CardConfig.isSandboxMode());

		setInitialized(true);
	}

	@Override
	public String getName() {
		return "Card";
	}

}
