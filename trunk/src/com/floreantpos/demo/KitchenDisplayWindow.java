package com.floreantpos.demo;

import java.awt.Toolkit;

import javax.swing.JFrame;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.KitchenTicket;

public class KitchenDisplayWindow extends JFrame {

	KitchenDisplayView view = new KitchenDisplayView(true);

	public KitchenDisplayWindow() {
		setTitle(Messages.getString("KitchenDisplayWindow.0")); //$NON-NLS-1$
		setIconImage(Application.getApplicationIcon().getImage());

		add(view);
		
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public void addTicket(KitchenTicket ticket) {
		view.addTicket(ticket);
	}

	
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);

		view.setVisible(b);
	}

	@Override
	public void dispose() {
		view.cleanup();

		super.dispose();
	}
}
