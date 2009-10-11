package com.mdss.pos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.mdss.pos.main.Application;
import com.mdss.pos.model.CouponAndDiscount;
import com.mdss.pos.model.dao.CouponAndDiscountDAO;
import com.mdss.pos.swing.MessageDialog;
import com.mdss.pos.swing.TransparentPanel;
import com.mdss.pos.ui.PosTableRenderer;
import com.mdss.pos.ui.dialog.BeanEditorDialog;
import com.mdss.pos.ui.dialog.ConfirmDeleteDialog;
import com.mdss.pos.ui.model.CouponForm;

public class CouponExplorer extends TransparentPanel implements ActionListener {
	private JTable explorerView;
	private CouponExplorerTableModel explorerModel;
	
	public CouponExplorer() {
		
		explorerView = new JTable();
		explorerView.setDefaultRenderer(Object.class, new PosTableRenderer());
		
		setLayout(new BorderLayout(5,5));
		add(new JScrollPane(explorerView));
		
		JButton addButton = new JButton("New");
		addButton.setActionCommand("add");
		addButton.addActionListener(this);
		
		JButton editButton = new JButton("Edit");
		editButton.setActionCommand("edit");
		editButton.addActionListener(this);

		JButton deleteButton = new JButton("Delete");
		deleteButton.setActionCommand("delete");
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
			MessageDialog.showError("An error has occured, could add coupon.", x);
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
			MessageDialog.showError("An error has occured, could not update coupon.", x);
		}
	}
	
	private void deleteCoupon(int index, CouponAndDiscount coupon) {
		try {
			if (ConfirmDeleteDialog.showMessage(this, "Sure want to delete?", "Delete") == ConfirmDeleteDialog.YES) {
				CouponAndDiscountDAO dao = new CouponAndDiscountDAO();
				dao.delete(coupon);
				explorerModel.deleteCoupon(coupon, index);
			}
		} catch (Exception x) {
			MessageDialog.showError("An error has occured, could not delete coupon.", x);
		}
	}
	
	private class CouponExplorerTableModel extends AbstractTableModel {
		String[] columnNames = {"NAME", "COUPON_TYPE", "COUPON_VALUE", "EXPIRY_DATE", "DISABLED", "NEVER_EXPIRE"};
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
		if("add".equals(actionCommand)) {
			addNewCoupon();
		}
		else if("edit".equals(actionCommand)) {
			int index = explorerView.getSelectedRow();
			if(index < 0) {
				MessageDialog.showError("Please select the coupon to edit.");
				return;
			}
			CouponAndDiscount coupon = explorerModel.getCoupon(index);
			editCoupon(coupon);
		}
		else if("delete".equals(actionCommand)) {
			int index = explorerView.getSelectedRow();
			if(index < 0) {
				MessageDialog.showError("Please select the coupon to delete.");
				return;
			}
			CouponAndDiscount coupon = explorerModel.getCoupon(index);
			deleteCoupon(index, coupon);
		}
	}
}
