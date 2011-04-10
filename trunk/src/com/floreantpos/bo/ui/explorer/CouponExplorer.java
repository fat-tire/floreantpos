package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.floreantpos.main.Application;
import com.floreantpos.model.CouponAndDiscount;
import com.floreantpos.model.dao.CouponAndDiscountDAO;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.CouponForm;

public class CouponExplorer extends TransparentPanel implements ActionListener {
	private JTable explorerView;
	private CouponExplorerTableModel explorerModel;
	
	public CouponExplorer() {
		
		explorerView = new JTable();
		explorerView.setDefaultRenderer(Object.class, new PosTableRenderer());
		
		setLayout(new BorderLayout(5,5));
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
		CouponAndDiscountDAO dao = new CouponAndDiscountDAO();
		List<CouponAndDiscount> couponList = dao.findAll();
		explorerModel = new CouponExplorerTableModel(couponList);
		explorerView.setModel(explorerModel);
	}
	
	private void addNewCoupon() {
		try {
			CouponForm editor = new CouponForm();
			BeanEditorDialog dialog = new BeanEditorDialog(editor, Application.getInstance().getBackOfficeWindow(), true);
			dialog.open();
			
			if (dialog.isCanceled())
				return;
			CouponAndDiscount coupon = (CouponAndDiscount) editor.getBean();
			explorerModel.addCoupon(coupon);
		} catch (Exception x) {
			MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
		}
	}
	
	private void editCoupon(CouponAndDiscount coupon) {
		try {
			CouponForm editor = new CouponForm(coupon);
			BeanEditorDialog dialog = new BeanEditorDialog(editor, Application.getInstance().getBackOfficeWindow(), true);
			dialog.open();
			if (dialog.isCanceled())
				return;

			explorerView.repaint();
		} catch (Throwable x) {
			MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
		}
	}
	
	private void deleteCoupon(int index, CouponAndDiscount coupon) {
		try {
			if (ConfirmDeleteDialog.showMessage(this, com.floreantpos.POSConstants.CONFIRM_DELETE, com.floreantpos.POSConstants.DELETE) == ConfirmDeleteDialog.YES) {
				CouponAndDiscountDAO dao = new CouponAndDiscountDAO();
				dao.delete(coupon);
				explorerModel.deleteCoupon(coupon, index);
			}
		} catch (Exception x) {
			MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
		}
	}
	
	private class CouponExplorerTableModel extends AbstractTableModel {
		String[] columnNames = {com.floreantpos.POSConstants.NAME, com.floreantpos.POSConstants.COUPON_TYPE, com.floreantpos.POSConstants.COUPON_VALUE, com.floreantpos.POSConstants.EXPIRY_DATE, com.floreantpos.POSConstants.DISABLED, com.floreantpos.POSConstants.NEVER_EXPIRE};
		List<CouponAndDiscount> couponList;
		
		CouponExplorerTableModel(List<CouponAndDiscount> list) {
			this.couponList = list;
		}
		
		public int getRowCount() {
			if(couponList == null) return 0;
			
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
			if(couponList == null) return "";
			
			CouponAndDiscount coupon = couponList.get(row);
			switch(column) {
				case 0:
					return coupon.getName();
					
				case 1:
					return CouponAndDiscount.COUPON_TYPE_NAMES[coupon.getType()];
					
				case 2:
					return Double.valueOf(coupon.getValue());
					
				case 3:
					return coupon.getExpiryDate();
					
				case 4:
					return Boolean.valueOf(coupon.isDisabled());
					
				case 5:
					return Boolean.valueOf(coupon.isNeverExpire());
			}
			
			return null;
		}
		
		public void addCoupon(CouponAndDiscount coupon) {
			int size = couponList.size();
			couponList.add(coupon);
			fireTableRowsInserted(size, size);
		}
		
		public void deleteCoupon(CouponAndDiscount coupon, int index) {
			couponList.remove(coupon);
			fireTableRowsDeleted(index, index);
		}
		
		public CouponAndDiscount getCoupon(int index) {
			return couponList.get(index);
		}
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if(com.floreantpos.POSConstants.ADD.equals(actionCommand)) {
			addNewCoupon();
		}
		else if(com.floreantpos.POSConstants.EDIT.equals(actionCommand)) {
			int index = explorerView.getSelectedRow();
			if(index < 0) {
				MessageDialog.showError(com.floreantpos.POSConstants.SELECT_COUPON_TO_EDIT);
				return;
			}
			CouponAndDiscount coupon = explorerModel.getCoupon(index);
			editCoupon(coupon);
		}
		else if(com.floreantpos.POSConstants.DELETE.equals(actionCommand)) {
			int index = explorerView.getSelectedRow();
			if(index < 0) {
				MessageDialog.showError(com.floreantpos.POSConstants.SELECT_COUPON_TO_DELETE);
				return;
			}
			CouponAndDiscount coupon = explorerModel.getCoupon(index);
			deleteCoupon(index, coupon);
		}
	}
}
