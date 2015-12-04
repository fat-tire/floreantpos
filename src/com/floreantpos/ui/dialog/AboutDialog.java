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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.util.POSUtil;

public class AboutDialog extends POSDialog {
	
	public AboutDialog() {
		super(POSUtil.getFocusedWindow(), Messages.getString("AboutDialog.0")); //$NON-NLS-1$
	}
	
	@Override
	protected void initUI() {
		JPanel contentPanel = new JPanel(new BorderLayout(20,20));
		contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		JLabel logoLabel = new JLabel(IconFactory.getIcon("/icons/", "fp_logo128x128.png")); //$NON-NLS-1$ //$NON-NLS-2$
		contentPanel.add(logoLabel, BorderLayout.WEST);
		
		JLabel l = new JLabel("<html><center><h1>Floreant POS</h1><br/><h2>Version " + Application.VERSION + "</h2></center></html>"); //$NON-NLS-1$ //$NON-NLS-2$
		contentPanel.add(l);
		
		JPanel buttonPanel = new JPanel();
		JButton btnOk = new JButton(Messages.getString("AboutDialog.5")); //$NON-NLS-1$
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		buttonPanel.add(btnOk);
		contentPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		add(contentPanel);
	}
}
