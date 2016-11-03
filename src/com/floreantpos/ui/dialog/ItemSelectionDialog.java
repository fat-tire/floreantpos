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

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableModel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.swing.ItemCheckBoxList;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.util.POSUtil;

public class ItemSelectionDialog extends POSDialog implements ActionListener {
	private TitlePanel titlePanel;
	private PosButton btnCancel;
	private PosButton btnOk;

	private ItemCheckBoxList cbListItems;

	public ItemSelectionDialog() {
		super(POSUtil.getBackOfficeWindow(), true);
		init();
	}

	private void init() {
		setLayout(new BorderLayout(10, 10));
		setIconImage(Application.getPosWindow().getIconImage());

		JPanel contentPane = new JPanel(new BorderLayout(10, 10));

		titlePanel = new TitlePanel();
		titlePanel.setTitle("Select item");
		contentPane.add(titlePanel, BorderLayout.NORTH);

		cbListItems = new ItemCheckBoxList();

		contentPane.add(new JScrollPane(cbListItems), BorderLayout.CENTER);

		btnOk = new PosButton(POSConstants.OK);
		btnOk.setFocusable(false);
		btnOk.addActionListener(this);

		btnCancel = new PosButton(POSConstants.CANCEL);
		btnCancel.setFocusable(false);
		btnCancel.addActionListener(this);

		JPanel footerPanel = new JPanel(new MigLayout("fill, ins 2", "sg, fill", ""));
		footerPanel.add(btnOk);
		footerPanel.add(btnCancel);

		contentPane.add(footerPanel, BorderLayout.SOUTH);

		add(contentPane);
		setSize(500, 400);
	}

	public void setModel(TableModel items) {
		cbListItems.setModel(items);
	}

	public TableModel getModel() {
		return cbListItems.getModel();
	}

	private void doOk() {
		setCanceled(false);
		dispose();
	}

	private void doCancel() {
		setCanceled(true);
		dispose();
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();

		if (POSConstants.CANCEL.equalsIgnoreCase(actionCommand)) {
			doCancel();
		}
		else if (POSConstants.OK.equalsIgnoreCase(actionCommand)) {
			doOk();
		}
	}
}
