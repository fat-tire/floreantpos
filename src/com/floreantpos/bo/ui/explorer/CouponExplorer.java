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
package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.Discount;
import com.floreantpos.model.dao.DiscountDAO;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.CouponForm;
import com.floreantpos.util.POSUtil;

public class CouponExplorer extends TransparentPanel implements ActionListener {
	private JTable explorerView;
	private CouponExplorerTableModel explorerModel;

	public CouponExplorer() {

		explorerView = new JTable();
		explorerView.setDefaultRenderer(Object.class, new PosTableRenderer());

		setLayout(new BorderLayout(5, 5));
		add(new JScrollPane(explorerView));

		JButton addButton = new JButton(com.floreantpos.POSConstants.NEW);
		addButton.setActionCommand(com.floreantpos.POSConstants.ADD);
		addButton.addActionListener(this);

		JButton editButton = new JButton(com.floreantpos.POSConstants.EDIT);
		editButton.setActionCommand(com.floreantpos.POSConstants.EDIT);
		editButton.addActionListener(this);

		JButton deleteButton = new JButton(com.floreantpos.POSConstants.DELETE);
		deleteButton.setActionCommand(com.floreantpos.POSConstants.DELETE);
		deleteButton.addActionListener(this);

		TransparentPanel panel = new TransparentPanel();
		panel.add(addButton);
		panel.add(editButton);
		panel.add(deleteButton);
		add(panel, BorderLayout.SOUTH);
	}

	public void initData() throws Exception {
		DiscountDAO dao = new DiscountDAO();
		List<Discount> couponList = dao.findAll();
		explorerModel = new CouponExplorerTableModel(couponList);
		explorerView.setModel(explorerModel);
	}

	private void addNewCoupon() {
		try {
			CouponForm editor = new CouponForm();
			BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
			dialog.open();

			if (dialog.isCanceled())
				return;
			Discount coupon = (Discount) editor.getBean();
			explorerModel.addCoupon(coupon);
		} catch (Exception x) {
			BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
		}
	}

	private void editCoupon(Discount coupon) {
		try {
			CouponForm editor = new CouponForm(coupon);
			BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
			dialog.open();
			if (dialog.isCanceled())
				return;

			explorerView.repaint();
		} catch (Throwable x) {
			BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
		}
	}

	private void deleteCoupon(int index, Discount coupon) {
		try {
			if (ConfirmDeleteDialog.showMessage(this, com.floreantpos.POSConstants.CONFIRM_DELETE, com.floreantpos.POSConstants.DELETE) == ConfirmDeleteDialog.YES) {
				DiscountDAO dao = new DiscountDAO();
				dao.delete(coupon);
				explorerModel.deleteCoupon(coupon, index);
			}
		} catch (Exception x) {
			BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
		}
	}

	private class CouponExplorerTableModel extends AbstractTableModel {
		String[] columnNames = { com.floreantpos.POSConstants.NAME, com.floreantpos.POSConstants.COUPON_TYPE, com.floreantpos.POSConstants.COUPON_VALUE,
				com.floreantpos.POSConstants.EXPIRY_DATE, com.floreantpos.POSConstants.ENABLED, com.floreantpos.POSConstants.NEVER_EXPIRE };
		List<Discount> couponList;

		CouponExplorerTableModel(List<Discount> list) {
			this.couponList = list;
		}

		public int getRowCount() {
			if (couponList == null)
				return 0;

			return couponList.size();
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(int index) {
			return columnNames[index];
		}

		public Object getValueAt(int row, int column) {
			if (couponList == null)
				return ""; //$NON-NLS-1$

			Discount coupon = couponList.get(row);
			switch (column) {
				case 0:
					return coupon.getName();

				case 1:
					return "";//CouponAndDiscount.COUPON_TYPE_NAMES[coupon.getType()]; //$NON-NLS-1$

				case 2:
					return Double.valueOf(coupon.getValue());

				case 3:
					return coupon.getExpiryDate();

				case 4:
					return Boolean.valueOf(coupon.isEnabled());

				case 5:
					return Boolean.valueOf(coupon.isNeverExpire());
			}

			return null;
		}

		public void addCoupon(Discount coupon) {
			int size = couponList.size();
			couponList.add(coupon);
			fireTableRowsInserted(size, size);
		}

		public void deleteCoupon(Discount coupon, int index) {
			couponList.remove(coupon);
			fireTableRowsDeleted(index, index);
		}

		public Discount getCoupon(int index) {
			return couponList.get(index);
		}
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (com.floreantpos.POSConstants.ADD.equals(actionCommand)) {
			addNewCoupon();
		}
		else if (com.floreantpos.POSConstants.EDIT.equals(actionCommand)) {
			int index = explorerView.getSelectedRow();
			if (index < 0) {
				BOMessageDialog.showError(com.floreantpos.POSConstants.SELECT_COUPON_TO_EDIT);
				return;
			}
			Discount coupon = explorerModel.getCoupon(index);
			editCoupon(coupon);
		}
		else if (com.floreantpos.POSConstants.DELETE.equals(actionCommand)) {
			int index = explorerView.getSelectedRow();
			if (index < 0) {
				BOMessageDialog.showError(com.floreantpos.POSConstants.SELECT_COUPON_TO_DELETE);
				return;
			}
			Discount coupon = explorerModel.getCoupon(index);
			deleteCoupon(index, coupon);
		}
	}
}
