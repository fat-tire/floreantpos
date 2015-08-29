package com.floreantpos.ui.dialog;

import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JFrame;

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

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
}
