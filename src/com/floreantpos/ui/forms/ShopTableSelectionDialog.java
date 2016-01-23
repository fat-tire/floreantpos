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
package com.floreantpos.ui.forms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.SoftReference;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.combobox.ListComboBoxModel;

import com.floreantpos.Messages;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;

public class ShopTableSelectionDialog extends POSDialog {
	private JComboBox<ShopTable> cbTables;

	private SoftReference<ShopTableSelectionDialog> instance;
	private javax.swing.JSeparator jSeparator1;
	private JPanel buttonPanel;

	//	private JRadioButton rbFree;
	//	private JRadioButton rbServing;
	//	private JRadioButton rbBooked;
	//	private JRadioButton rbDirty;
	//	private JRadioButton rbDisable;

	public ShopTableSelectionDialog() {
		super(POSUtil.getBackOfficeWindow(), Messages.getString("ShopTableSelectionDialog.0"), true); //$NON-NLS-1$

		initModel();
	}

	@Override
	protected void initUI() {
		getContentPane().setLayout(new BorderLayout(0, 0));
		GridLayout experimentLayout = new GridLayout(0, 3, 5, 0);
		buttonPanel = new JPanel(experimentLayout);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		JButton btnOk = new JButton(Messages.getString("ShopTableSelectionDialog.1")); //$NON-NLS-1$
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(false);
				dispose();
			}
		});
	//	btnOk.setPreferredSize(new Dimension(0, 50));
		buttonPanel.add(btnOk);

		JButton btnCancel = new JButton(Messages.getString("ShopTableSelectionDialog.2")); //$NON-NLS-1$
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		//btnCancel.setPreferredSize(new Dimension(0, 50));
		buttonPanel.add(btnCancel);

		JPanel contentPanel = new JPanel();
		contentPanel.setPreferredSize(new Dimension(132, 65));
		getContentPane().add(contentPanel, BorderLayout.NORTH);

		JLabel lblSelectTable = new JLabel(Messages.getString("ShopTableSelectionDialog.3")); //$NON-NLS-1$
		contentPanel.add(lblSelectTable);

		cbTables = new JComboBox<ShopTable>();
		cbTables.setPreferredSize(new Dimension(132, 24));
		contentPanel.add(cbTables);

		jSeparator1 = new javax.swing.JSeparator();
		getContentPane().add(jSeparator1, BorderLayout.CENTER);

		//		JPanel panel = new JPanel();
		//		panel.setBorder(new TitledBorder(null, "Status", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		//		contentPanel.add(panel);
		//		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		//		
		//		rbFree = new JRadioButton("Free");
		//		panel.add(rbFree);
		//		
		//		rbServing = new JRadioButton("Serving");
		//		panel.add(rbServing);
		//		
		//		rbBooked = new JRadioButton("Booked");
		//		panel.add(rbBooked);
		//		
		//		rbDirty = new JRadioButton("Dirty");
		//		panel.add(rbDirty);
		//		
		//		rbDisable = new JRadioButton("Disable");
		//		panel.add(rbDisable);

		//setResizable(false);
	}

	private void initModel() {
		try {
			cbTables.setModel(new ListComboBoxModel<ShopTable>(ShopTableDAO.getInstance().getAllUnassigned()));
		} catch (Exception e) {
			POSMessageDialog.showError(this, Messages.getString("ShopTableSelectionDialog.4"), e); //$NON-NLS-1$
		}
	}

	public ShopTable getSelectedTable() {
		if (isCanceled()) {
			return null;
		}

		return (ShopTable) cbTables.getSelectedItem();
	}

	public ShopTableSelectionDialog getInstance() {
		if (instance == null || instance.get() == null) {
			instance = new SoftReference<ShopTableSelectionDialog>(new ShopTableSelectionDialog());
		}

		return instance.get();
	}

	public JPanel getButtonPanel() {
		return this.buttonPanel;
	}

	public void refresh() {
		try {
			cbTables.setModel(new ListComboBoxModel<ShopTable>(ShopTableDAO.getInstance().getAllUnassigned()));
		} catch (Exception e) {
			POSMessageDialog.showError(this, Messages.getString("ShopTableSelectionDialog.4"), e); //$NON-NLS-1$
		}
	}
}
