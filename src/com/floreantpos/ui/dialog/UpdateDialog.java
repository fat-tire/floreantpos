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
package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.swing.PosButton;
import com.floreantpos.util.POSUtil;

public class UpdateDialog extends POSDialog {
	String[] versions;
	boolean up_to_date = false;
	boolean showTerminalKey = false;
	private JComboBox cbCheckUpdateStatus;

	public UpdateDialog(String[] versions, boolean up_to_date, boolean showTerminalKey) {
		super(POSUtil.getBackOfficeWindow(), "Update"); //$NON-NLS-1$
		setIconImage(Application.getApplicationIcon().getImage());
		setResizable(false);
		this.versions = versions;
		this.up_to_date = up_to_date;
		this.showTerminalKey = showTerminalKey;
		initComponent();
		cbCheckUpdateStatus.setSelectedItem(TerminalConfig.getCheckUpdateStatus());
	}

	protected void initComponent() {
		JPanel panel = new JPanel(new MigLayout("fillx"));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
		JLabel logoLabel = new JLabel(IconFactory.getIcon("/icons/", "fp_logo128x128.png")); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(logoLabel, "cell 0 0 0 2");

		JLabel l = new JLabel("<html><h1>Floreant POS</h1><h4>Current Version " + Application.VERSION + "</h4></html>"); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(l, "cell 0 2");

		String version = "";
		if (up_to_date) {
			version = "<h2>The software is up to date.</h2> ";//$NON-NLS-1$ 
		}
		else if (versions == null || versions.length == 0) {
			version = "<h4><a href='#'>Check for updates</a></h4>";//$NON-NLS-1$
		}
		else if (versions.length > 0) {
			version = "<h2>Update Available</h2> ";//$NON-NLS-1$ 
			for (String i : versions) {
				version += "<h4>" + i + "<a href='#'>  Download</a></h4>";//$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		JLabel lblVersion = new JLabel("<html>" + version + "</html>"); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(lblVersion, "cell 1 0,right");
		lblVersion.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				String link = TerminalConfig.getPosDownloadUrl(); //$NON-NLS-1$
				try {
					openBrowser(link);
				} catch (Exception e1) {
				}
			}
		});

		cbCheckUpdateStatus = new JComboBox();
		cbCheckUpdateStatus.addItem("Daily");
		cbCheckUpdateStatus.addItem("Weekly");
		cbCheckUpdateStatus.addItem("Monthly");
		cbCheckUpdateStatus.addItem("Never");
		panel.add(new JLabel("Check for Update"), "split 2,cell 1 2,aligny top,right");
		panel.add(cbCheckUpdateStatus, "growx,aligny top,right");

		JPanel buttonPanel = new JPanel(new MigLayout("fill"));
		PosButton btnOk = new PosButton(Messages.getString("AboutDialog.5")); //$NON-NLS-1$
		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String status = (String) cbCheckUpdateStatus.getSelectedItem();
				TerminalConfig.setCheckUpdateStatus(status);
				dispose();
			}
		});

		buttonPanel.add(new JSeparator(), "newline, grow");
		buttonPanel.add(btnOk, "newline,center");
		add(buttonPanel, BorderLayout.SOUTH);
		add(panel);
	}

	private void openBrowser(String link) throws Exception {
		URI uri = new URI(link);
		if (Desktop.isDesktopSupported()) {
			Desktop.getDesktop().browse(uri);
		}
	}
}
