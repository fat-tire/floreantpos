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

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.model.CookingInstruction;
import com.floreantpos.model.dao.CookingInstructionDAO;
import com.floreantpos.swing.PosButton;

public class SelectCookongInstructionDialog extends POSDialog implements ActionListener {
	private CookingInstruction cookingInstruction;
	
	private JComboBox cbCookingInstructions;
	private PosButton btnNew;
	private PosButton btnOk;
	private PosButton btnCancel;

	public SelectCookongInstructionDialog() throws HeadlessException {
		super();
	}

	@Override
	protected void initUI() {
		setLayout(new MigLayout());
		
		CookingInstructionDAO dao = new CookingInstructionDAO();
		List<CookingInstruction> cookingInstructions = dao.findAll();
		DefaultComboBoxModel cbModel = new DefaultComboBoxModel(cookingInstructions.toArray());
		
		cbCookingInstructions = new JComboBox(cbModel);
		cbCookingInstructions.setFont(cbCookingInstructions.getFont().deriveFont(16));
		btnNew = new PosButton(com.floreantpos.POSConstants.NEW);
		btnOk = new PosButton(com.floreantpos.POSConstants.OK);
		btnCancel = new PosButton(com.floreantpos.POSConstants.CANCEL);
		add(cbCookingInstructions, "wrap, span, grow, h 30"); //$NON-NLS-1$
		add(new JSeparator(), "wrap, span, grow"); //$NON-NLS-1$
		add(btnNew, "al right,width 120, height 30"); //$NON-NLS-1$
		add(btnOk, "al right,width 120, height 30"); //$NON-NLS-1$
		add(btnCancel, "width 120, height 30"); //$NON-NLS-1$
		
		btnNew.addActionListener(this);
		btnOk.addActionListener(this);
		btnCancel.addActionListener(this);
	}
	
	private void doOk() {
		cookingInstruction = (CookingInstruction) cbCookingInstructions.getSelectedItem();
		setCanceled(false);
		dispose();
	}

	private void doCancel() {
		setCanceled(true);
		dispose();
	}
	
	private void doCreateNew() {
		NewCookongInstructionDialog dialog = new NewCookongInstructionDialog();
		dialog.pack();
		dialog.open();
		
		if(!dialog.isCanceled()) {
			cookingInstruction = dialog.getCookingInstruction();
			DefaultComboBoxModel model = (DefaultComboBoxModel) cbCookingInstructions.getModel();
			model.addElement(cookingInstruction);
			model.setSelectedItem(cookingInstruction);
		}
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		
		if(com.floreantpos.POSConstants.OK.equalsIgnoreCase(actionCommand)) {
			doOk();
		}
		else if(com.floreantpos.POSConstants.CANCEL.equalsIgnoreCase(actionCommand)) {
			doCancel();
		}
		else if(com.floreantpos.POSConstants.NEW.equalsIgnoreCase(actionCommand)) {
			doCreateNew();
		}
	}

	public CookingInstruction getCookingInstruction() {
		return cookingInstruction;
	}

	public CookingInstruction getSelectedCookingInstruction() {
		return cookingInstruction;
	}

}
