package com.floreantpos.ui.dialog;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.floreantpos.main.Application;
import com.floreantpos.swing.GlassPane;

public class POSDialog extends JDialog {
	protected boolean canceled = true;
	private GlassPane glassPane;

	public POSDialog() throws HeadlessException {
		super(Application.getPosWindow(), true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		glassPane = new GlassPane();
		setGlassPane(glassPane);
		
		initUI();
	}

	public POSDialog(Dialog owner, boolean modal) {
		super(owner, modal);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		glassPane = new GlassPane();
		setGlassPane(glassPane);
		
		initUI();
	}

	public POSDialog(Dialog owner, String title, boolean modal) {
		super(owner, title, modal);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		glassPane = new GlassPane();
		setGlassPane(glassPane);
		
		initUI();
	}

	public POSDialog(Frame owner, boolean modal) throws HeadlessException {
		super(owner, modal);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		glassPane = new GlassPane();
		setGlassPane(glassPane);
		
		initUI();
	}

	public POSDialog(Frame owner, boolean modal, boolean unDecorated) throws HeadlessException {
		super(owner, modal);
		//setUndecorated(unDecorated);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		glassPane = new GlassPane();
		setGlassPane(glassPane);
		initUI();
	}
	
	protected void initUI() {}

	public void open() {
		canceled = false;
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

	public void setGlassPaneVisible(boolean b) {
		glassPane.setVisible(b);
	}
}
