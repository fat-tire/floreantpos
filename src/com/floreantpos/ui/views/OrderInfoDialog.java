package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class OrderInfoDialog extends POSDialog {
	OrderInfoView view;
	
	public OrderInfoDialog(OrderInfoView view) {
		this.view = view;
		setTitle(Messages.getString("OrderInfoDialog.0")); //$NON-NLS-1$
		
		createUI();
	}

	private void createUI() {
		add(view);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		PosButton btnPrint = new PosButton();
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doPrint();
			}
		});
		btnPrint.setText(Messages.getString("OrderInfoDialog.1")); //$NON-NLS-1$
		panel.add(btnPrint);
		
		PosButton btnClose = new PosButton();
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnClose.setText(Messages.getString("OrderInfoDialog.2")); //$NON-NLS-1$
		panel.add(btnClose);
	}

	protected void doPrint() {
		try {
			view.print();
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage());
		}
	}

}
