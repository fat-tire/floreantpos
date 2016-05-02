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
/*
 * PayOutView.java
 *
 * Created on August 25, 2006, 8:15 PM
 */
package com.floreantpos.ui.views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.model.PayoutReason;
import com.floreantpos.model.PayoutRecepient;
import com.floreantpos.model.dao.PayoutReasonDAO;
import com.floreantpos.model.dao.PayoutRecepientDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.NotesDialog;

/**
 *
 * @author  MShahriar
 */
public class PayOutView extends TransparentPanel {

	private JComboBox cbReason;
	private JComboBox cbRecepient;
	private NumberSelectionView numberSelectionView;
	private JTextArea tfNote;

	public PayOutView() {
		init();
	}

	public void initialize() {
		PayoutReasonDAO reasonDAO = new PayoutReasonDAO();
		List<PayoutReason> reasons = reasonDAO.findAll();
		cbReason.setModel(new DefaultComboBoxModel(reasons.toArray()));

		PayoutRecepientDAO recepientDAO = new PayoutRecepientDAO();
		List<PayoutRecepient> recepients = recepientDAO.findAll();
		cbRecepient.setModel(new DefaultComboBoxModel(new Vector(recepients)));
	}

	private void init() {
		setLayout(new MigLayout("inset 0,fill")); //$NON-NLS-1$
		numberSelectionView = new NumberSelectionView();
		numberSelectionView.setTitle(POSConstants.AMOUNT_PAID_OUT);
		numberSelectionView.setBorder(BorderFactory.createCompoundBorder(numberSelectionView.getBorder(), new EmptyBorder(5, 5, 5, 5)));
		numberSelectionView.setDecimalAllowed(true);

		Font font1 = new Font("Tahoma", Font.BOLD, PosUIManager.getFontSize(12)); //$NON-NLS-1$
		Font font2 = new Font("Tahoma", Font.BOLD, PosUIManager.getFontSize(18)); //$NON-NLS-1$

		JLabel lblPayoutReason = new JLabel(POSConstants.PAY_OUT_REASON);
		JLabel lblPayOutReceipient = new JLabel(POSConstants.SELECT_PAY_OUT_RECEPIENT);
		JLabel lblNote = new JLabel(Messages.getString("PayOutView.5")); //$NON-NLS-1$

		lblPayoutReason.setFont(font1);
		lblPayOutReceipient.setFont(font1);
		lblNote.setFont(font1);

		Dimension size = PosUIManager.getSize(300, 40);
		cbReason = new JComboBox();
		cbReason.setPreferredSize(size);
		cbRecepient = new JComboBox();
		cbRecepient.setPreferredSize(size);
		tfNote = new JTextArea();

		cbReason.setFont(font2);
		cbRecepient.setFont(font2);

		JScrollPane jScrollPane1 = new JScrollPane();
		tfNote.setColumns(20);
		tfNote.setEditable(false);
		tfNote.setLineWrap(true);
		tfNote.setRows(4);
		tfNote.setWrapStyleWord(true);
		jScrollPane1.setViewportView(tfNote);

		PosButton btnAddNote = new PosButton("..."); //$NON-NLS-1$
		btnAddNote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnAddNoteActionPerformed(evt);
			}
		});

		PosButton btnNewReason = new PosButton("..."); //$NON-NLS-1$
		btnNewReason.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doNewReason();
			}
		});

		PosButton btnNewRecepient = new PosButton("..."); //$NON-NLS-1$
		btnNewRecepient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doNewRecepient();
			}
		});

		int width = PosUIManager.getSize(70);

		JPanel inputPanel = new JPanel(new MigLayout("fill", "grow, fill", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		inputPanel.add(lblPayoutReason, "grow,wrap"); //$NON-NLS-1$
		inputPanel.add(cbReason, "grow"); //$NON-NLS-1$
		inputPanel.add(btnNewReason, "grow,wrap,w " + width + "!"); //$NON-NLS-1$

		inputPanel.add(lblPayOutReceipient, "grow,gaptop 30,wrap"); //$NON-NLS-1$
		inputPanel.add(cbRecepient, "grow"); //$NON-NLS-1$
		inputPanel.add(btnNewRecepient, "grow,wrap,w " + width + "!"); //$NON-NLS-1$

		inputPanel.add(lblNote, "grow,gaptop 30,wrap"); //$NON-NLS-1$
		inputPanel.add(jScrollPane1, "grow,spany,h " + PosUIManager.getSize(120) + "!"); //$NON-NLS-1$
		inputPanel.add(btnAddNote, "grow, wrap,w " + width + "!"); //$NON-NLS-1$

		add(numberSelectionView, "grow"); //$NON-NLS-1$
		add(inputPanel, "grow"); //$NON-NLS-1$
	}

	protected void doNewRecepient() {
		NotesDialog dialog = new NotesDialog();
		dialog.setTitle(Messages.getString("PayOutView.0")); //$NON-NLS-1$
		dialog.pack();
		dialog.open();

		if (dialog.isCanceled()) {
			return;
		}

		PayoutRecepient recepient = new PayoutRecepient();
		recepient.setName(dialog.getNote());

		PayoutRecepientDAO.getInstance().saveOrUpdate(recepient);
		DefaultComboBoxModel<PayoutRecepient> model = (DefaultComboBoxModel<PayoutRecepient>) cbRecepient.getModel();
		model.addElement(recepient);
	}

	protected void doNewReason() {
		NotesDialog dialog = new NotesDialog();
		dialog.setTitle(Messages.getString("PayOutView.10")); //$NON-NLS-1$
		dialog.pack();
		dialog.open();

		if (dialog.isCanceled()) {
			return;
		}

		PayoutReason reason = new PayoutReason();
		reason.setReason(dialog.getNote());

		PayoutReasonDAO.getInstance().saveOrUpdate(reason);
		DefaultComboBoxModel<PayoutReason> model = (DefaultComboBoxModel<PayoutReason>) cbReason.getModel();
		model.addElement(reason);
	}

	private void btnAddNoteActionPerformed(ActionEvent evt) {
		NotesDialog dialog = new NotesDialog();
		dialog.setTitle(POSConstants.ENTER_PAYOUT_NOTE);
		dialog.pack();
		dialog.open();

		if (!dialog.isCanceled()) {
			tfNote.setText(dialog.getNote());
		}
	}

	public double getPayoutAmount() {
		return numberSelectionView.getValue();
	}

	public String getNote() {
		return tfNote.getText();
	}

	public PayoutReason getReason() {
		return (PayoutReason) cbReason.getSelectedItem();
	}

	public PayoutRecepient getRecepient() {
		return (PayoutRecepient) cbRecepient.getSelectedItem();
	}
}
