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

import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;

public class POSDialog extends JDialog {
	protected boolean canceled = true;

	public POSDialog() throws HeadlessException {
		super(Application.getPosWindow(), true);
		initUI();
	}
	
	public POSDialog(Frame owner, boolean modal) {
		super(owner, modal);
		
		initUI();
		setIconImage(Application.getPosWindow().getIconImage());
	}

//	public POSDialog(Window owner) {
//		this(owner, "");
//	}
//
	public POSDialog(Window owner, String title) {
		this(owner, title, true);

		initUI();
	}
	
	public POSDialog(Window owner, String title, boolean modal) {
		super(owner, title, modal ? ModalityType.APPLICATION_MODAL: ModalityType.MODELESS);
		
		initUI();
	}
	
	protected void initUI() {}

	public void open() {
		canceled = true;
		
		if (isUndecorated()) {
			Window owner = getOwner();
			if (owner instanceof JFrame) {
				JFrame frame = (JFrame) owner;
				setLocationRelativeTo(frame.getContentPane());
			}
			else {
				setLocationRelativeTo(owner);
			}

		}
		else {
			setLocationRelativeTo(getOwner());
		}
		setVisible(true);
	}
	
	public void openFullScreen() {
		canceled = true;
		setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
		if (TerminalConfig.isFullscreenMode()) {
			setUndecorated(true);
		}
		setVisible(true);
	}
	
	public void openUndecoratedFullScreen() {
		canceled = true;
		setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
		setUndecorated(true);
		setVisible(true);
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
}
