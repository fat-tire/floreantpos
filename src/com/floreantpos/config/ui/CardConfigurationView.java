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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.config.CardConfig;
import com.floreantpos.extension.ExtensionManager;
import com.floreantpos.extension.FloreantPlugin;
import com.floreantpos.extension.PaymentGatewayPlugin;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class CardConfigurationView extends ConfigurationView {

	private JComboBox cbGateway;
	private DoubleTextField tfBarTabLimit = new DoubleTextField(10);
	private DoubleTextField tfAdvanceTipsPercentage = new DoubleTextField(10);

	private JPanel pluginConfigPanel = new JPanel(new BorderLayout());

	public CardConfigurationView() {
		createUI();
	}

	private void createUI() {
		setLayout(new BorderLayout());

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][][][][][][][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		JLabel lblMerchantGateway = new JLabel(Messages.getString("CardConfigurationView.2")); //$NON-NLS-1$
		contentPanel.add(lblMerchantGateway, "cell 0 4,alignx leading"); //$NON-NLS-1$

		cbGateway = new JComboBox();
		cbGateway.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					updatePluginConfigUI();
				} catch (Exception e1) {
					POSMessageDialog.showError(CardConfigurationView.this, e1.getMessage(), e1);
				}
			}
		});
		contentPanel.add(cbGateway, "cell 1 4,growx"); //$NON-NLS-1$
		contentPanel.add(pluginConfigPanel, "newline,span,wrap,growx"); //$NON-NLS-1$

		contentPanel.add(new JLabel(Messages.getString("CardConfigurationView.1")), "cell 0 6"); //$NON-NLS-1$//$NON-NLS-2$
		contentPanel.add(tfBarTabLimit, "cell 1 6"); //$NON-NLS-1$

		contentPanel.add(new JLabel(Messages.getString("CardConfigurationView.4")), "cell 0 7"); //$NON-NLS-1$ //$NON-NLS-2$
		contentPanel.add(tfAdvanceTipsPercentage, "cell 1 7"); //$NON-NLS-1$
		contentPanel.add(new JLabel(Messages.getString("CardConfigurationView.10")), "cell 1 7"); //$NON-NLS-1$ //$NON-NLS-2$

		JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
		contentPanel.add(separator, "newline, growx, span 10, wrap"); //$NON-NLS-1$

		JScrollPane scrollPane = new JScrollPane(contentPanel);
		scrollPane.setBorder(null);
		add(scrollPane);

	}

	private void initialMerchantGateways() {
		DefaultComboBoxModel<PaymentGatewayPlugin> model = new DefaultComboBoxModel<PaymentGatewayPlugin>();
		List<FloreantPlugin> plugins = ExtensionManager.getPlugins(PaymentGatewayPlugin.class);

		for (FloreantPlugin plugin : plugins) {
			model.addElement((PaymentGatewayPlugin) plugin);
		}

		cbGateway.setModel(model);
		cbGateway.setSelectedItem(CardConfig.getPaymentGateway());
	}

	@Override
	public boolean save() throws Exception {
		PaymentGatewayPlugin plugin = (PaymentGatewayPlugin) cbGateway.getSelectedItem();
		plugin.getConfigurationPane().save();

		CardConfig.setPaymentGateway(plugin);

		CardConfig.setBartabLimit(tfBarTabLimit.getDouble());
		CardConfig.setAdvanceTipsPercentage(tfAdvanceTipsPercentage.getDouble());

		return true;
	}

	@Override
	public void initialize() throws Exception {
		initialMerchantGateways();

		tfBarTabLimit.setText(String.valueOf(CardConfig.getBartabLimit()));
		tfAdvanceTipsPercentage.setText(String.valueOf(CardConfig.getAdvanceTipsPercentage()));

		updatePluginConfigUI();

		setInitialized(true);
	}

	private void updatePluginConfigUI() throws Exception {
		PaymentGatewayPlugin plugin = (PaymentGatewayPlugin) cbGateway.getSelectedItem();
		pluginConfigPanel.removeAll();
		if (plugin == null) {
			return;
		}
		ConfigurationView configurationPane = plugin.getConfigurationPane();
		configurationPane.initialize();
		pluginConfigPanel.add(configurationPane);
		revalidate();
		repaint();
	}

	@Override
	public String getName() {
		return Messages.getString("CardConfigurationView.6"); //$NON-NLS-1$
	}

}
