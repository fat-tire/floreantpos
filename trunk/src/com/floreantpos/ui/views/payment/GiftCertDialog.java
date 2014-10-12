package com.floreantpos.ui.views.payment;

import java.awt.BorderLayout;

import javax.swing.JDialog;

import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.swing.PosButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GiftCertDialog extends POSDialog {
	private FixedLengthTextField tfGiftCertNumber;
	private DoubleTextField tfFaceValue;
	
	public GiftCertDialog(JDialog parent) {
		super(parent, true);
		
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
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		
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
		panel_1.add(psbtnOk);
		
		PosButton psbtnCancel = new PosButton();
		psbtnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		psbtnCancel.setText("CANCEL");
		panel_1.add(psbtnCancel);
		
	}
	
	public String getGiftCertNumber() {
		return tfGiftCertNumber.getText();
	}
	
	public double getGiftCertFaceValue() {
		return tfFaceValue.getDouble();
	}
}
