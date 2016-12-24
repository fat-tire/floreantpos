package com.floreantpos.customPayment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

import org.hibernate.StaleObjectStateException;

import com.floreantpos.Messages;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.CustomPayment;
import com.floreantpos.model.dao.CustomPaymentDAO;
import com.floreantpos.model.util.IllegalModelStateException;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;

public class CustomPaymentForm extends BeanEditor<CustomPayment> {

	private JLabel lblName;
	private JLabel lblRefNumberFieldName;

	private FixedLengthTextField txtName;
	private FixedLengthTextField txtRefNumberFieldName;
	private JCheckBox cbRequiredRefNumber;

	public CustomPaymentForm() {
		initComponent();
	}

	private void initComponent() {
		cbRequiredRefNumber = new JCheckBox(Messages.getString("CustomPaymentForm.0")); //$NON-NLS-1$
		lblRefNumberFieldName = new JLabel(Messages.getString("CustomPaymentForm.1")); //$NON-NLS-1$
		lblName = new JLabel(Messages.getString("CustomPaymentForm.2")); //$NON-NLS-1$
		txtName = new FixedLengthTextField(60);
		txtRefNumberFieldName = new FixedLengthTextField(60);

		setLayout(new MigLayout("hidemode 1", "", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		add(lblName, "split 2"); //$NON-NLS-1$
		add(txtName, "wrap"); //$NON-NLS-1$
		add(cbRequiredRefNumber, "wrap"); //$NON-NLS-1$
		add(lblRefNumberFieldName, "split 2"); //$NON-NLS-1$
		add(txtRefNumberFieldName);

		cbRequiredRefNumber.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (cbRequiredRefNumber.isSelected()) {
					lblRefNumberFieldName.setVisible(true);
					txtRefNumberFieldName.setVisible(true);
				}
				else {
					lblRefNumberFieldName.setVisible(false);
					txtRefNumberFieldName.setVisible(false);
				}
			}
		});

		lblRefNumberFieldName.setVisible(false);
		txtRefNumberFieldName.setVisible(false);
		txtName.setEnabled(false);
		cbRequiredRefNumber.setEnabled(false);
	}

	@Override
	public void setFieldsEnable(boolean enable) {
		txtName.setEnabled(enable);
		cbRequiredRefNumber.setEnabled(enable);
		txtRefNumberFieldName.setEnabled(enable);
	}

	@Override
	public void createNew() {
		setBean(new CustomPayment());
		lblRefNumberFieldName.setVisible(false);
		txtRefNumberFieldName.setVisible(false);
		cbRequiredRefNumber.setSelected(false);
	}

	@Override
	public boolean save() {
		try {
			if (!updateModel())
				return false;

			CustomPayment payment = (CustomPayment) getBean();
			CustomPaymentDAO.getInstance().saveOrUpdate(payment);

			updateView();

			return true;
		} catch (IllegalModelStateException e) {
		} catch (StaleObjectStateException e) {
			BOMessageDialog.showError(this, Messages.getString("CustomPaymentForm.10")); //$NON-NLS-1$
		}
		return false;
	}

	@Override
	protected void updateView() {

		CustomPayment payment = (CustomPayment) getBean();

		txtName.setText(payment.getName());

		if (payment.isRequiredRefNumber()) {
			txtRefNumberFieldName.setVisible(true);
			lblRefNumberFieldName.setVisible(true);
			txtRefNumberFieldName.setText(payment.getRefNumberFieldName());
			cbRequiredRefNumber.setSelected(payment.isRequiredRefNumber());
		}
		else {
			lblRefNumberFieldName.setVisible(false);
			txtRefNumberFieldName.setVisible(false);
			cbRequiredRefNumber.setSelected(payment.isRequiredRefNumber());
		}
	}

	@Override
	protected boolean updateModel() throws IllegalModelStateException {

		CustomPayment payment = (CustomPayment) getBean();

		if (txtName.getText().equals("")) { //$NON-NLS-1$
			POSMessageDialog.showMessage(null, Messages.getString("CustomPaymentForm.12")); //$NON-NLS-1$
			return false;
		}

		payment.setName(txtName.getText());

		if (cbRequiredRefNumber.isSelected()) {
			if (txtRefNumberFieldName.getText().equals("")) { //$NON-NLS-1$
				POSMessageDialog.showMessage(null, Messages.getString("CustomPaymentForm.14")); //$NON-NLS-1$
				return false;
			}
			payment.setRefNumberFieldName(txtRefNumberFieldName.getText());
			payment.setRequiredRefNumber(true);
		}
		else {
			payment.setRefNumberFieldName(""); //$NON-NLS-1$
			payment.setRequiredRefNumber(false);
		}
		return true;
	}

	@Override
	public boolean delete() {
		try {
			CustomPayment payment = (CustomPayment) getBean();
			if (payment == null)
				return false;

			CustomPaymentDAO.getInstance().delete(payment);
			return true;
		} catch (Exception e) {
			POSMessageDialog.showError(POSUtil.getBackOfficeWindow(), e.getMessage(), e);
		}
		return false;
	}

	@Override
	public String getDisplayText() {
		return null;
	}
}
