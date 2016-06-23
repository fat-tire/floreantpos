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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.extension.ExtensionManager;
import com.floreantpos.extension.FloreantPlugin;
import com.floreantpos.extension.TicketImportPlugin;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;

public class ConfigurationDialog extends POSDialog implements ChangeListener, ActionListener {
	private static final String OK = com.floreantpos.POSConstants.OK;
	private static final String CANCEL = com.floreantpos.POSConstants.CANCEL;
	private JTabbedPane tabbedPane = new JTabbedPane();
	private List<ConfigurationView> views = new ArrayList<ConfigurationView>();

	public ConfigurationDialog() {
		super(POSUtil.getBackOfficeWindow(), true);

		setTitle(Messages.getString("CONFIGURATION_WINDOW_TITLE")); //$NON-NLS-1$
		setLayout(new BorderLayout());

		JPanel contentPanel = new JPanel(new MigLayout("fill", "", "[fill,grow][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		tabbedPane.addChangeListener(this);
		contentPanel.add(tabbedPane, "span, grow"); //$NON-NLS-1$

		addView(new RestaurantConfigurationView());
		addView(new TerminalConfigurationView());
		addView(new PrintConfigurationView());
		addView(new CardConfigurationView());
		addView(new DatabaseConfigurationView());
		addView(new TaxConfigurationView());
		addView(new PeripheralConfigurationView());

		TicketImportPlugin ticketImportPlugin = (TicketImportPlugin) ExtensionManager.getPlugin(TicketImportPlugin.class);
		if (ticketImportPlugin != null) {
			addView(new TicketImportConfigurationView());
		}

		JPanel bottomPanel = new JPanel(new MigLayout("fill")); //$NON-NLS-1$

		JButton btnOk = new JButton(CANCEL);
		btnOk.addActionListener(this);
		bottomPanel.add(btnOk, "dock east, gaptop 5"); //$NON-NLS-1$
		JButton btnCancel = new JButton(OK);
		btnCancel.addActionListener(this);
		bottomPanel.add(btnCancel, "dock east, gapright 5, gaptop 5"); //$NON-NLS-1$

		add(bottomPanel, BorderLayout.SOUTH); //$NON-NLS-1$
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		for (FloreantPlugin plugin : ExtensionManager.getPlugins()) {
			plugin.initConfigurationView(this);
		}
		//addView(new OtherConfigurationView());

		add(contentPanel, BorderLayout.CENTER);
	}

	public void addView(ConfigurationView view) {
		tabbedPane.addTab(view.getName(), view);
		views.add(view);
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);

		if (b) {
			stateChanged(null);
		}
	}

	public void stateChanged(ChangeEvent e) {
		ConfigurationView view = (ConfigurationView) tabbedPane.getSelectedComponent();
		if (!view.isInitialized()) {
			try {
				view.initialize();
			} catch (Exception e1) {
				POSMessageDialog.showError(this, POSConstants.ERROR_MESSAGE, e1);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (OK.equalsIgnoreCase(e.getActionCommand())) {
			try {
				for (ConfigurationView view : views) {
					if (view.isInitialized())
						view.save();
				}
				setCanceled(false);
				dispose();
			} catch (PosException x) {
				POSMessageDialog.showError(this, x.getMessage());
			} catch (Exception x) {
				POSMessageDialog.showError(this, POSConstants.ERROR_MESSAGE, x);
			}
		}
		else if (CANCEL.equalsIgnoreCase(e.getActionCommand())) {
			setCanceled(true);
			dispose();
		}
	}

}
