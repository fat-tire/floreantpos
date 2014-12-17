package com.floreantpos.config.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.config.CardConfig;
import com.floreantpos.model.CardReader;
import com.floreantpos.model.MerchantGateway;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.POSTextField;

public class CardConfigurationView extends ConfigurationView {
	//private List<String>

	private POSTextField tfMerchantAccount;
	private JComboBox cbGateway;
	private JComboBox cbCardReader;
	private JPasswordField tfMerchantPass;
	private JCheckBox cbSandboxMode;
	private JCheckBox chckbxAllowMagneticSwipe;
	private JCheckBox chckbxAllowCardManual;
	private JCheckBox chckbxAllowExternalTerminal;
	private DoubleTextField tfBarTabLimit = new DoubleTextField(10);

	public CardConfigurationView() {
		createUI();
	}

	private void createUI() {
		setLayout(new MigLayout("", "[][grow]", "[][][][][][][][]"));
		
		chckbxAllowMagneticSwipe = new JCheckBox("Allow Magnetic Swipe Card");
		chckbxAllowMagneticSwipe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateCardList();
			}
		});
		add(chckbxAllowMagneticSwipe, "cell 0 0 2 1");
		
		chckbxAllowCardManual = new JCheckBox("Allow Card Manual Entry");
		chckbxAllowCardManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateCardList();
			}
		});
		add(chckbxAllowCardManual, "cell 0 1 2 1");
		
		chckbxAllowExternalTerminal = new JCheckBox("Allow External Terminal");
		chckbxAllowExternalTerminal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateCardList();
			}
		});
		add(chckbxAllowExternalTerminal, "cell 0 2 2 1");

		JLabel lblMagneticCardReader = new JLabel("Default Card Entry Method");
		add(lblMagneticCardReader, "cell 0 3,alignx leading");

		cbCardReader = new JComboBox();
		cbCardReader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateCheckBoxes();
			}
		});
		add(cbCardReader, "cell 1 3,growx");

		JLabel lblMerchantGateway = new JLabel("Merchant Gateway");
		add(lblMerchantGateway, "cell 0 4,alignx leading");

		cbGateway = new JComboBox();
		cbGateway.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MerchantGateway gateway = (MerchantGateway) cbGateway.getSelectedItem();
				switch(gateway) {
					case AUTHORIZE_NET:
						tfMerchantAccount.setText("6tuU4N3H");
						tfMerchantPass.setText("4k6955x3T8bCVPVm");
						break;
						
					case MERCURY_PAY:
						tfMerchantAccount.setText("118725340908147");
						tfMerchantPass.setText("XYZ");
						break;
				}
			}
		});
		add(cbGateway, "cell 1 4,growx");

		JLabel lblMerchantAccount = new JLabel("Merchant Account");
		add(lblMerchantAccount, "cell 0 5,alignx leading");

		tfMerchantAccount = new POSTextField();
		add(tfMerchantAccount, "cell 1 5,growx");

		JLabel lblSecretCode = new JLabel("Secret Code");
		add(lblSecretCode, "cell 0 6,alignx leading");

		cbCardReader.setModel(new DefaultComboBoxModel<CardReader>(CardReader.values()));
		cbGateway.setModel(new DefaultComboBoxModel<MerchantGateway>(MerchantGateway.values()));

		tfMerchantPass = new JPasswordField();
		add(tfMerchantPass, "cell 1 6,growx");
		
		cbSandboxMode = new JCheckBox("Sandbox mode");
		add(cbSandboxMode, "cell 1 7");
		
		JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
		add(separator, "newline, growx, span 10, wrap");
		
		add(new JLabel("Bar tab limit"));
		add(tfBarTabLimit);
	}

	protected void updateCheckBoxes() {
		CardReader selectedItem = (CardReader) cbCardReader.getSelectedItem();
		if(selectedItem == CardReader.SWIPE) {
			chckbxAllowMagneticSwipe.setSelected(true);
		}
		else if(selectedItem == CardReader.MANUAL) {
			chckbxAllowCardManual.setSelected(true);
		}
		else if(selectedItem == CardReader.EXTERNAL_TERMINAL) {
			chckbxAllowExternalTerminal.setSelected(true);
		}
	}

	protected void updateCardList() {
		boolean swipeSupported = chckbxAllowMagneticSwipe.isSelected();
		boolean manualSupported = chckbxAllowCardManual.isSelected();
		boolean extSupported = chckbxAllowExternalTerminal.isSelected();
		
		CardReader currentReader = (CardReader) cbCardReader.getSelectedItem();
		Vector<CardReader> readers = new Vector<CardReader>(3);
		
		if(swipeSupported) {
			readers.add(CardReader.SWIPE);
		}
		
		if(manualSupported) {
			readers.add(CardReader.MANUAL);
		}
		
		if(extSupported) {
			readers.add(CardReader.EXTERNAL_TERMINAL);
		}
		
		cbCardReader.setModel(createComboBoxModel(readers));
		if (readers.contains(currentReader)) {
			cbCardReader.setSelectedItem(currentReader);
		}
		
		if (!swipeSupported && !manualSupported && !extSupported) {
			cbCardReader.setEnabled(false);
			cbGateway.setEnabled(false);
			tfMerchantAccount.setEnabled(false);
			tfMerchantPass.setEnabled(false);
			cbSandboxMode.setEnabled(false);
		}
		else {
			cbCardReader.setEnabled(true);
			cbGateway.setEnabled(true);
			tfMerchantAccount.setEnabled(true);
			tfMerchantPass.setEnabled(true);
			cbSandboxMode.setEnabled(true);
		}
		
		if(swipeSupported || manualSupported) {
			cbGateway.setEnabled(true);
			tfMerchantAccount.setEnabled(true);
			tfMerchantPass.setEnabled(true);
			cbSandboxMode.setEnabled(true);
		}
		else {
			cbGateway.setEnabled(false);
			tfMerchantAccount.setEnabled(false);
			tfMerchantPass.setEnabled(false);
			cbSandboxMode.setEnabled(false);
		}
	}

	private DefaultComboBoxModel<CardReader> createComboBoxModel(Vector items) {
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		for (Object object : items) {
			model.addElement(object);
		}
		return model;
	}

	@Override
	public boolean save() throws Exception {
		CardConfig.setSwipeCardSupported(chckbxAllowMagneticSwipe.isSelected());
		CardConfig.setManualEntrySupported(chckbxAllowCardManual.isSelected());
		CardConfig.setExtTerminalSupported(chckbxAllowExternalTerminal.isSelected());
		
		CardReader cardReader = (CardReader) cbCardReader.getSelectedItem();
		CardConfig.setCardReader(cardReader);
		
		MerchantGateway gateway = (MerchantGateway) cbGateway.getSelectedItem();
		CardConfig.setMerchantGateway(gateway);
		
		CardConfig.setMerchantAccount(tfMerchantAccount.getText());
		CardConfig.setMerchantPass(new String(tfMerchantPass.getPassword()));
		
		CardConfig.setSandboxMode(cbSandboxMode.isSelected());
		
		CardConfig.setBartabLimit(tfBarTabLimit.getDouble());
		
		return true;
	}

	@Override
	public void initialize() throws Exception {
		chckbxAllowMagneticSwipe.setSelected(CardConfig.isSwipeCardSupported());
		chckbxAllowCardManual.setSelected(CardConfig.isManualEntrySupported());
		chckbxAllowExternalTerminal.setSelected(CardConfig.isExtTerminalSupported());
		
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
		tfBarTabLimit.setText(String.valueOf(CardConfig.getBartabLimit()));
		
		updateCardList();

		setInitialized(true);
	}

	@Override
	public String getName() {
		return "Card";
	}

}
