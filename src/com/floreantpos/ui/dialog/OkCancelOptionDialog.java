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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.util.POSUtil;

public abstract class OkCancelOptionDialog extends POSDialog {
	private PosButton btnOk;
	private PosButton btnCancel;

	private TitlePanel titlePanel;
	private TransparentPanel contentPanel;

	public OkCancelOptionDialog() {
		super(POSUtil.getBackOfficeWindow(), "");
		init();
		titlePanel.setTitle("");
	}

	public OkCancelOptionDialog(String title) {
		super(Application.getPosWindow(), title);
		init();
		titlePanel.setTitle(title);
	}

	public OkCancelOptionDialog(Window owner) {
		super(owner, "");
		init();
	}

	public OkCancelOptionDialog(Frame owner, boolean model) {
		super(owner, model);
		init();
	}

	public OkCancelOptionDialog(Window owner, String title) {
		super(owner, title);
		init();
		titlePanel.setTitle(title);
	}

	private void init() {
		setLayout(new BorderLayout(10, 10));
		setIconImage(Application.getApplicationIcon().getImage());
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

		titlePanel = new TitlePanel();
		add(titlePanel, BorderLayout.NORTH);

		contentPanel = new TransparentPanel();
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(contentPanel, BorderLayout.CENTER);

		JSeparator jSeparator1 = new JSeparator();
		TransparentPanel buttonPanel = new TransparentPanel();
		btnOk = new PosButton();
		btnCancel = new PosButton();

		TransparentPanel southPanel = new TransparentPanel();
		southPanel.setLayout(new BorderLayout());

		buttonPanel.setLayout(new FlowLayout());

		Dimension btnSize = PosUIManager.getSize(100, 70);

		btnOk.setPreferredSize(btnSize);
		btnOk.setText(com.floreantpos.POSConstants.OK.toUpperCase());
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				doOk();
			}
		});

		buttonPanel.add(btnOk);

		btnCancel.setPreferredSize(btnSize);
		btnCancel.setText(POSConstants.CANCEL.toUpperCase());
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				doCancel();
			}
		});

		buttonPanel.add(btnCancel);

		southPanel.add(jSeparator1, BorderLayout.NORTH);
		southPanel.add(buttonPanel, BorderLayout.CENTER);

		contentPanel.setLayout(new BorderLayout());

		add(southPanel, BorderLayout.SOUTH);
	}

	public void setTitlePaneText(String title) {
		titlePanel.setTitle(title);
	}

	public void setOkButtonText(String text) {
		btnOk.setText(text);
	}

	public JPanel getContentPanel() {
		return contentPanel;
	}

	public abstract void doOk();

	public void doCancel() {
		setCanceled(true);
		dispose();
	}
}
