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

import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.model.CookingInstruction;
import com.floreantpos.model.dao.CookingInstructionDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.views.NoteView;

public class NewCookongInstructionDialog extends POSDialog implements ActionListener {
	private CookingInstruction cookingInstruction;
	
	private NoteView noteView;
	private PosButton btnOk;
	private PosButton btnCancel;

	public NewCookongInstructionDialog() throws HeadlessException {
		super();
	}

	@Override
	protected void initUI() {
		setLayout(new MigLayout());
		
		noteView = new NoteView();
		btnOk = new PosButton(com.floreantpos.POSConstants.OK);
		btnCancel = new PosButton(com.floreantpos.POSConstants.CANCEL);
		add(noteView, "wrap, span, grow"); //$NON-NLS-1$
		add(new JSeparator(), "wrap, span, grow"); //$NON-NLS-1$
		add(btnOk, "al right,width 120, height 50"); //$NON-NLS-1$
		add(btnCancel, "width 120, height 50"); //$NON-NLS-1$
		
		btnOk.addActionListener(this);
		btnCancel.addActionListener(this);
	}
	
	public String getText() {
		return noteView.getNote();
	}
	
	private void doOk() {
		if(cookingInstruction == null) {
			cookingInstruction = new CookingInstruction();
		}
		cookingInstruction.setDescription(getText());
		
		CookingInstructionDAO dao = new CookingInstructionDAO();
		dao.save(cookingInstruction);
		
		setCanceled(false);
		dispose();
	}

	private void doCancel() {
		setCanceled(true);
		dispose();
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		
		if(com.floreantpos.POSConstants.OK.equalsIgnoreCase(actionCommand)) {
			doOk();
		}
		else if(com.floreantpos.POSConstants.CANCEL.equalsIgnoreCase(actionCommand)) {
			doCancel();
		}
	}

	public CookingInstruction getCookingInstruction() {
		return cookingInstruction;
	}

}
