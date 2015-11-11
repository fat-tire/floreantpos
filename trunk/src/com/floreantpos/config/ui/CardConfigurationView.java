package com.floreantpos.config.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
	
	private JPanel pluginConfigPanel = new JPanel(new BorderLayout());

	public CardConfigurationView() {
		createUI();
	}

	private void createUI() {
		setLayout(new MigLayout("", "[][grow]", "[][][][][][][][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		JLabel lblMerchantGateway = new JLabel(Messages.getString("CardConfigurationView.12")); //$NON-NLS-1$
		add(lblMerchantGateway, "cell 0 4,alignx leading"); //$NON-NLS-1$

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
		add(cbGateway, "cell 1 4,growx"); //$NON-NLS-1$
		add(pluginConfigPanel, "newline,span,wrap,growx"); //$NON-NLS-1$

		JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
		add(separator, "newline, growx, span 10, wrap"); //$NON-NLS-1$
		
		add(new JLabel(Messages.getString("CardConfigurationView.28"))); //$NON-NLS-1$
		add(tfBarTabLimit);
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
		
		return true;
	}

	@Override
	public void initialize() throws Exception {
		initialMerchantGateways();
		
		tfBarTabLimit.setText(String.valueOf(CardConfig.getBartabLimit()));
		
		updatePluginConfigUI();

		setInitialized(true);
	}

	private void updatePluginConfigUI() throws Exception {
		PaymentGatewayPlugin plugin = (PaymentGatewayPlugin) cbGateway.getSelectedItem();
		pluginConfigPanel.removeAll();
		pluginConfigPanel.add(plugin.getConfigurationPane());
		revalidate();
		repaint();
	}

	@Override
	public String getName() {
		return Messages.getString("CardConfigurationView.29"); //$NON-NLS-1$
	}

}
