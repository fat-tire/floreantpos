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
 * NotesDialog.java
 *
 * Created on September 8, 2006, 9:43 PM
 */

package com.floreantpos.ui.dialog;

import com.floreantpos.main.Application;
import com.floreantpos.ui.views.NoteView;

/**
 *
 * @author  MShahriar
 */
public class NotesDialog extends OkCancelOptionDialog {
	private NoteView noteView;

	public NotesDialog() {
		initComponents();
	}

	private void initComponents() {
		noteView = new NoteView();
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		noteView.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10));
		getContentPane().add(noteView, java.awt.BorderLayout.CENTER);

		pack();
	}

	public void doOk() {
		setCanceled(false);
		dispose();
	}

	@Override
	public void setTitle(String title) {
		super.setTitle(Application.getTitle());
		setTitlePaneText(title);
	}

	public String getNote() {
		return noteView.getNote();
	}

}
