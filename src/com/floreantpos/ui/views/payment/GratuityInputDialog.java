package com.floreantpos.ui.views.payment;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.swing.NumericKeypad;
import javax.swing.SwingConstants;

public class GratuityInputDialog extends POSDialog {
	private DoubleTextField doubleTextField;
	public GratuityInputDialog() {
		
		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle("Enter gratuity amount");
		getContentPane().add(titlePanel, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[224px,grow,fill]", "[19px][grow,fill][][]"));
		
		doubleTextField = new DoubleTextField();
		doubleTextField.setHorizontalAlignment(SwingConstants.TRAILING);
		doubleTextField.setFocusCycleRoot(true);
		doubleTextField.setColumns(20);
		panel.add(doubleTextField, "cell 0 0,alignx left,height 30px,aligny top");
		
		NumericKeypad numericKeypad = new NumericKeypad();
		panel.add(numericKeypad, "cell 0 1");
		
		JSeparator separator = new JSeparator();
		panel.add(separator, "cell 0 2,growx,gapy 50px");
		
		PosButton psbtnOk = new PosButton();
		psbtnOk.setFocusable(false);
		psbtnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(false);
				dispose();
			}
		});
		psbtnOk.setText("OK");
		panel.add(psbtnOk, "flowx,cell 0 3");
		
		PosButton psbtnCancel = new PosButton();
		psbtnCancel.setFocusable(false);
		psbtnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		psbtnCancel.setText("CANCEL");
		panel.add(psbtnCancel, "cell 0 3");
	}

	public double getGratuityAmount() {
		return doubleTextField.getDouble();
	}
}
