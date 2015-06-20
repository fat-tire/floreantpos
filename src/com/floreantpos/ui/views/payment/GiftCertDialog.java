package com.floreantpos.ui.views.payment;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class GiftCertDialog extends POSDialog {
	private FixedLengthTextField tfGiftCertNumber;
	private DoubleTextField tfFaceValue;
	private QwertyKeyPad qwertyKeyPad;
	
	public GiftCertDialog(JDialog parent) {
		super();
		
		setTitle("Gift Cert");
		
		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle("Enter Gift Certificate Detail");
		getContentPane().add(titlePanel, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[][grow]", "[][]"));
		
		JLabel lblGiftCertificateNumber = new JLabel("Gift certificate number");
		panel.add(lblGiftCertificateNumber, "cell 0 0,alignx trailing");
		
		tfGiftCertNumber = new FixedLengthTextField(64);
		panel.add(tfGiftCertNumber, "cell 1 0,growx");
		
		JLabel lblFaceValue = new JLabel("Face value");
		panel.add(lblFaceValue, "cell 0 1,alignx trailing");
		
		tfFaceValue = new DoubleTextField();
		tfFaceValue.setText("50");
		panel.add(tfFaceValue, "cell 1 1,growx");
		
		qwertyKeyPad = new QwertyKeyPad();
		panel.add(qwertyKeyPad, "newline, gaptop 10px, span");
		
		JPanel buttonPanel = new JPanel(new MigLayout("align 50%"));
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		PosButton psbtnOk = new PosButton();
		psbtnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(StringUtils.isEmpty(getGiftCertNumber())) {
					POSMessageDialog.showMessage("Please enter gift certificate number");
					return;
				}
				
				if(getGiftCertFaceValue() <= 0) {
					POSMessageDialog.showMessage("Please enter valid face value");
					return;
				}
				
				setCanceled(false);
				dispose();
			}
		});
		psbtnOk.setText("OK");
		buttonPanel.add(psbtnOk, "w 100!, h 60!");
		
		PosButton psbtnCancel = new PosButton();
		psbtnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		psbtnCancel.setText("CANCEL");
		buttonPanel.add(psbtnCancel, "w 100!, h 60!");
		
	}
	
	public String getGiftCertNumber() {
		return tfGiftCertNumber.getText();
	}
	
	public double getGiftCertFaceValue() {
		return tfFaceValue.getDouble();
	}
}
