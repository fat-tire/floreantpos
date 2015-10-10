package com.floreantpos.ui.views.payment;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
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
		
		setTitle(Messages.getString("GiftCertDialog.0")); //$NON-NLS-1$
		
		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle(Messages.getString("GiftCertDialog.1")); //$NON-NLS-1$
		getContentPane().add(titlePanel, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[][grow]", "[][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		JLabel lblGiftCertificateNumber = new JLabel(Messages.getString("GiftCertDialog.5")); //$NON-NLS-1$
		panel.add(lblGiftCertificateNumber, "cell 0 0,alignx trailing"); //$NON-NLS-1$
		
		tfGiftCertNumber = new FixedLengthTextField(64);
		panel.add(tfGiftCertNumber, "cell 1 0,growx"); //$NON-NLS-1$
		
		JLabel lblFaceValue = new JLabel(Messages.getString("GiftCertDialog.8")); //$NON-NLS-1$
		panel.add(lblFaceValue, "cell 0 1,alignx trailing"); //$NON-NLS-1$
		
		tfFaceValue = new DoubleTextField();
		tfFaceValue.setText("50"); //$NON-NLS-1$
		panel.add(tfFaceValue, "cell 1 1,growx"); //$NON-NLS-1$
		
		qwertyKeyPad = new QwertyKeyPad();
		panel.add(qwertyKeyPad, "newline, gaptop 10px, span"); //$NON-NLS-1$
		
		JPanel buttonPanel = new JPanel(new MigLayout("align 50%")); //$NON-NLS-1$
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		PosButton psbtnOk = new PosButton();
		psbtnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(StringUtils.isEmpty(getGiftCertNumber())) {
					POSMessageDialog.showMessage(Messages.getString("GiftCertDialog.14")); //$NON-NLS-1$
					return;
				}
				
				if(getGiftCertFaceValue() <= 0) {
					POSMessageDialog.showMessage(Messages.getString("GiftCertDialog.15")); //$NON-NLS-1$
					return;
				}
				
				setCanceled(false);
				dispose();
			}
		});
		psbtnOk.setText(Messages.getString("GiftCertDialog.16")); //$NON-NLS-1$
		buttonPanel.add(psbtnOk, "w 100!, h 60!"); //$NON-NLS-1$
		
		PosButton psbtnCancel = new PosButton();
		psbtnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		psbtnCancel.setText(Messages.getString("GiftCertDialog.18")); //$NON-NLS-1$
		buttonPanel.add(psbtnCancel, "w 100!, h 60!"); //$NON-NLS-1$
		
	}
	
	public String getGiftCertNumber() {
		return tfGiftCertNumber.getText();
	}
	
	public double getGiftCertFaceValue() {
		return tfFaceValue.getDouble();
	}
}
