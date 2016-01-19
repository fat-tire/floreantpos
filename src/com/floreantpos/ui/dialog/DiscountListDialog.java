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
package com.floreantpos.ui.dialog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.model.Discount;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketDiscount;
import com.floreantpos.swing.PosButton;
import com.floreantpos.util.NumberUtil;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class DiscountListDialog extends POSDialog implements ActionListener {
	private JPanel contentPane;
	private PosButton buttonOK;
	private PosButton buttonCancel;
	private PosButton btnScrollUp;
	private PosButton btnScrollDown;
	private PosButton btnDeleteSelected;
	private JTable tableDiscounts;

	private List<Ticket> tickets;
	private DiscountViewTableModel discountViewTableModel;
	private DefaultListSelectionModel selectionModel;

	private boolean modified = false;

	public DiscountListDialog(List<Ticket> tickets) {
		super();

		this.tickets = tickets;

		setSize(700, 500);
		discountViewTableModel = new DiscountViewTableModel();
		tableDiscounts.setModel(discountViewTableModel);

		selectionModel = new DefaultListSelectionModel();
		selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableDiscounts.setSelectionModel(selectionModel);

		btnScrollUp.setActionCommand("scrollUP"); //$NON-NLS-1$
		btnScrollDown.setActionCommand("scrollDown"); //$NON-NLS-1$
		btnScrollUp.addActionListener(this);
		btnScrollDown.addActionListener(this);

		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);

		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOK();
			}
		});

		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		});

		// call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

		// call onCancel() on ESCAPE
		contentPane.registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		btnDeleteSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doDeleteSelection();
			}
		});
	}

	private void doDeleteSelection() {
		try {
			int selectedRow = selectionModel.getLeadSelectionIndex();
			if (selectedRow < 0) {
				POSMessageDialog.showError(this, com.floreantpos.POSConstants.SELECT_ITEM_TO_DELETE);
				return;
			}
			if (ConfirmDeleteDialog.showMessage(this, com.floreantpos.POSConstants.CONFIRM_DELETE, com.floreantpos.POSConstants.DELETE) != ConfirmDeleteDialog.YES) {
				return;
			}

			Object object = discountViewTableModel.get(selectedRow);
			modified = discountViewTableModel.delete((TicketCoupon) object);
		} catch (Exception e) {
			POSMessageDialog.showError(this, Messages.getString("DiscountListDialog.2"), e); //$NON-NLS-1$
		}
	}

	private void onOK() {
		tickets = null;
		setCanceled(false);
		dispose();
	}

	private void onCancel() {
		tickets = null;
		setCanceled(true);
		dispose();
	}

	{
		// GUI initializer generated by IntelliJ IDEA GUI Designer
		// >>> IMPORTANT!! <<<
		// DO NOT EDIT OR ADD ANY CODE HERE!
		$$$setupUI$$$();
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		contentPane = new JPanel();
		contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL,
				GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
				GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK
				| GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		btnDeleteSelected = new PosButton();
		btnDeleteSelected.setIcon(IconFactory.getIcon("/ui_icons/", "delete.png")); //$NON-NLS-1$ //$NON-NLS-2$
		btnDeleteSelected.setPreferredSize(new Dimension(140, 50));
		btnDeleteSelected.setText(Messages.getString("DiscountListDialog.5")); //$NON-NLS-1$
		panel2.add(btnDeleteSelected);
		buttonOK = new PosButton();
		buttonOK.setIcon(IconFactory.getIcon("/ui_icons/", "finish.png")); //$NON-NLS-1$ //$NON-NLS-2$
		buttonOK.setPreferredSize(new Dimension(120, 50));
		buttonOK.setText(com.floreantpos.POSConstants.OK);
		panel2.add(buttonOK);
		buttonCancel = new PosButton();
		buttonCancel.setIcon(IconFactory.getIcon("/ui_icons/", "cancel.png")); //$NON-NLS-1$ //$NON-NLS-2$
		buttonCancel.setPreferredSize(new Dimension(120, 50));
		buttonCancel.setText(com.floreantpos.POSConstants.CANCEL);
		panel2.add(buttonCancel);
		final JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
		contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK
				| GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(458,
				310), null, 0, false));
		final JScrollPane scrollPane1 = new JScrollPane();
		panel3.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK
				| GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0,
				false));
		tableDiscounts = new JTable();
		scrollPane1.setViewportView(tableDiscounts);
		final JPanel panel4 = new JPanel();
		panel4.setLayout(new FormLayout("fill:p:grow", "center:d:grow,top:4dlu:noGrow,center:d:grow")); //$NON-NLS-1$ //$NON-NLS-2$
		panel3.add(panel4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK
				| GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0,
				false));
		btnScrollUp = new PosButton();
		btnScrollUp.setIcon(IconFactory.getIcon("/ui_icons/", "up.png")); //$NON-NLS-1$ //$NON-NLS-2$
		btnScrollUp.setPreferredSize(new Dimension(50, 50));
		btnScrollUp.setText(""); //$NON-NLS-1$
		CellConstraints cc = new CellConstraints();
		panel4.add(btnScrollUp, cc.xy(1, 1, CellConstraints.CENTER, CellConstraints.BOTTOM));
		btnScrollDown = new PosButton();
		btnScrollDown.setIcon(IconFactory.getIcon("/ui_icons/", "down.png")); //$NON-NLS-1$ //$NON-NLS-2$
		btnScrollDown.setPreferredSize(new Dimension(50, 50));
		btnScrollDown.setText(""); //$NON-NLS-1$
		panel4.add(btnScrollDown, cc.xy(1, 3, CellConstraints.CENTER, CellConstraints.TOP));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return contentPane;
	}

	class DiscountViewTableModel extends AbstractTableModel {
		String[] columnNames = {
				Messages.getString("DiscountListDialog.18"), Messages.getString("DiscountListDialog.19"), Messages.getString("DiscountListDialog.20") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		ArrayList rows = new ArrayList();

		DiscountViewTableModel() {
			for (Iterator iter = tickets.iterator(); iter.hasNext();) {
				Ticket ticket = (Ticket) iter.next();
				List<TicketDiscount> coupons = ticket.getDiscounts();

				if (coupons != null) {
					for (TicketDiscount coupon : coupons) {
						TicketCoupon ticketDiscount = new TicketCoupon(ticket, coupon);
						rows.add(ticketDiscount);
					}
				}

			}

		}

		public int getRowCount() {
			return rows.size();
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			TicketCoupon ticketDiscount = (TicketCoupon) rows.get(rowIndex);
			Object discountObject = ticketDiscount.getDiscountObject();

			switch (columnIndex) {
				case 0:
					if (discountObject instanceof TicketDiscount) {
						return ((TicketDiscount) discountObject).getName();
					}
					return null;

				case 1:
					if (discountObject instanceof TicketDiscount) {
						return Discount.COUPON_TYPE_NAMES[((TicketDiscount) discountObject).getType()];
					}
					return null;

				case 2:
					if (discountObject instanceof TicketDiscount) {
						return NumberUtil.formatNumber(((TicketDiscount) discountObject).getValue());
					}
					return null;
			}

			return null;
		}

		public boolean delete(TicketCoupon ticketDiscount) {
			Ticket ticket = ticketDiscount.getTicket();
			Object object = ticketDiscount.getDiscountObject();

			if (object instanceof TicketCoupon) {
				boolean b = ticket.getDiscounts().remove(object);
				rows.remove(ticketDiscount);
				fireTableDataChanged();
				return b;
			}
			return false;
		}

		public Object get(int index) {
			return rows.get(index);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if ("scrollUP".equals(e.getActionCommand())) { //$NON-NLS-1$

			int selectedRow = selectionModel.getLeadSelectionIndex();

			if (selectedRow <= 0) {
				selectedRow = 0;
			}
			else {
				--selectedRow;
			}

			selectionModel.setLeadSelectionIndex(selectedRow);
			Rectangle cellRect = tableDiscounts.getCellRect(selectedRow, 0, false);
			tableDiscounts.scrollRectToVisible(cellRect);
		}
		else if ("scrollDown".equals(e.getActionCommand())) { //$NON-NLS-1$
			int selectedRow = selectionModel.getLeadSelectionIndex();

			if (selectedRow < 0) {
				selectedRow = 0;
			}
			else if (selectedRow >= discountViewTableModel.getRowCount() - 1) {
				//return;
			}
			else {
				++selectedRow;
			}

			selectionModel.setLeadSelectionIndex(selectedRow);
			Rectangle cellRect = tableDiscounts.getCellRect(selectedRow, 0, false);
			tableDiscounts.scrollRectToVisible(cellRect);
		}
	}

	public boolean isModified() {
		return modified;
	}

	class TicketCoupon {
		private Ticket ticket;
		private Object discountObject;

		public TicketCoupon() {

		}

		public TicketCoupon(Ticket ticket, Object discount) {
			this.ticket = ticket;
			this.discountObject = discount;
		}

		public Object getDiscountObject() {
			return discountObject;
		}

		public void setDiscountObject(Object discount) {
			this.discountObject = discount;
		}

		public Ticket getTicket() {
			return ticket;
		}

		public void setTicket(Ticket ticket) {
			this.ticket = ticket;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof TicketCoupon)) {
				return false;
			}

			TicketCoupon other = (TicketCoupon) obj;
			return this.discountObject.equals(other.discountObject);
		}
	}
}
