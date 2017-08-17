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
package com.floreantpos.ui.views.payment;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.ui.dialog.OkCancelOptionDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class GiftCertDialog extends OkCancelOptionDialog {
	private FixedLengthTextField tfGiftCertNumber;
	private DoubleTextField tfFaceValue;
	private QwertyKeyPad qwertyKeyPad;

	public GiftCertDialog() {
		super();

		setTitle(Messages.getString("GiftCertDialog.0")); //$NON-NLS-1$
		setTitlePaneText(Messages.getString("GiftCertDialog.1"));

		JPanel panel = getContentPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[][grow]", "[][]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		JLabel lblGiftCertificateNumber = new JLabel(Messages.getString("GiftCertDialog.5")); //$NON-NLS-1$
		panel.add(lblGiftCertificateNumber, "cell 0 0,alignx trailing"); //$NON-NLS-1$

		tfGiftCertNumber = new FixedLengthTextField();
		tfGiftCertNumber.setLength(64);
		panel.add(tfGiftCertNumber, "cell 1 0,growx"); //$NON-NLS-1$

		JLabel lblFaceValue = new JLabel(Messages.getString("GiftCertDialog.8")); //$NON-NLS-1$
		panel.add(lblFaceValue, "cell 0 1,alignx trailing"); //$NON-NLS-1$

		tfFaceValue = new DoubleTextField();
		tfFaceValue.setText("50"); //$NON-NLS-1$
		panel.add(tfFaceValue, "cell 1 1,growx"); //$NON-NLS-1$

		qwertyKeyPad = new QwertyKeyPad();
		panel.add(qwertyKeyPad, "newline, gaptop 10px, span"); //$NON-NLS-1$
	}

	@Override
	public void doOk() {
		if (StringUtils.isEmpty(getGiftCertNumber())) {
			POSMessageDialog.showMessage(Messages.getString("GiftCertDialog.14")); //$NON-NLS-1$
			return;
		}

		if (getGiftCertFaceValue() <= 0) {
			POSMessageDialog.showMessage(Messages.getString("GiftCertDialog.15")); //$NON-NLS-1$
			return;
		}

		setCanceled(false);
		dispose();
	}

	public String getGiftCertNumber() {
		return tfGiftCertNumber.getText();
	}

	public double getGiftCertFaceValue() {
		return tfFaceValue.getDouble();
	}
}
