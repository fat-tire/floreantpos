/*
 * TicketInfoView.java
 *
 * Created on August 13, 2006, 11:17 PM
 */

package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketCouponAndDiscount;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.CouponAndDiscountDialog;
import com.floreantpos.ui.dialog.DiscountListDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

/**
 *
 * @author  MShahriar
 */
public class TicketDetailView extends JPanel implements ActionListener {

	public final static String VIEW_NAME = "TICKET_DETAIL";

	private com.floreantpos.swing.PosButton btnApplyCoupon;
	private com.floreantpos.swing.POSToggleButton btnTaxExempt;
	private com.floreantpos.swing.PosButton btnViewDiscounts;

	private javax.swing.JLabel lblBalanceDue;
	private javax.swing.JTextField tfCreateTime;
	private javax.swing.JTextField tfGuests;
	private javax.swing.JTextField tfServerId;
	private javax.swing.JTextField tfServerName;
	private javax.swing.JTextField tfSubtotal;
	private javax.swing.JTextField tfTable;
	private javax.swing.JTextField tfTax;
	private javax.swing.JTextField tfTerminal;
	private javax.swing.JTextField tfTicketId;
	private javax.swing.JTextField tfTotal;
	private javax.swing.JTextField tfDue;
	private javax.swing.JTextField tfTotalDiscount;

	private SettleTicketView settleTicketView;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yy, hh:mm a");

	private JPanel topPanel;
	private JPanel balanceDuePanel;
	private JPanel buttonPanel;

	private List<Ticket> tickets;

	/** Creates new form TicketInfoView */
	public TicketDetailView() {

		setLayout(new BorderLayout(5, 5));

		topPanel = new JPanel(new MigLayout("align 50%"));
		addRow(topPanel, com.floreantpos.POSConstants.TICKET_ID + ":", tfTicketId = new JTextField());
		addRow(topPanel, com.floreantpos.POSConstants.SERVER_ID + ":", tfServerId = new JTextField());
		addRow(topPanel, com.floreantpos.POSConstants.SERVER_NAME + ":", tfServerName = new JTextField());
		addRow(topPanel, com.floreantpos.POSConstants.CREATED + ":", tfCreateTime = new JTextField());
		addRow(topPanel, com.floreantpos.POSConstants.TERMINAL + ":", tfTerminal = new JTextField());
		addRow(topPanel, com.floreantpos.POSConstants.TABLE_NO + ":", tfTable = new JTextField());
		addRow(topPanel, com.floreantpos.POSConstants.GUEST + " #:", tfGuests = new JTextField());
		addRow(topPanel, com.floreantpos.POSConstants.SUBTOTAL + ":", tfSubtotal = new JTextField());
		addRow(topPanel, com.floreantpos.POSConstants.DISCOUNT + ":", tfTotalDiscount = new JTextField());
		addRow(topPanel, com.floreantpos.POSConstants.TAX + ":", tfTax = new JTextField());
		addRow(topPanel, com.floreantpos.POSConstants.TOTAL + ":", tfTotal = new JTextField());
		addRow(topPanel, com.floreantpos.POSConstants.DUE + ":", tfDue = new JTextField());

		add(topPanel, BorderLayout.CENTER);

		balanceDuePanel = new JPanel(new MigLayout("align 50%"));
		balanceDuePanel.add(new JSeparator(), "grow, span,w 320");
		JLabel balanceDueTitle = new JLabel(com.floreantpos.POSConstants.BALANCE_DUE);
		balanceDuePanel.add(balanceDueTitle, "newline,grow,span");
		balanceDuePanel.add(lblBalanceDue = new JLabel("0"), "newline,grow,span");

		buttonPanel = new JPanel(new MigLayout("align 50%"));
		buttonPanel.add(new JSeparator(), "newline, grow,span");
		buttonPanel.add(btnApplyCoupon = new PosButton(com.floreantpos.POSConstants.COUPON_DISCOUNT), "w 160, h 50, grow, span");
		buttonPanel.add(btnViewDiscounts = new PosButton(com.floreantpos.POSConstants.VIEW_DISCOUNTS), "newline,w 160, h 50, ax 100%");
		buttonPanel.add(btnTaxExempt = new POSToggleButton(com.floreantpos.POSConstants.TAX_EXEMPT), "w 160, h 50");

		JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
		bottomPanel.add(balanceDuePanel);
		bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(bottomPanel, BorderLayout.SOUTH);

		setOpaque(false);

		balanceDueTitle.setFont(new java.awt.Font("Tahoma", 1, 36));
		balanceDueTitle.setForeground(new java.awt.Color(255, 102, 0));
		balanceDueTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

		lblBalanceDue.setFont(new java.awt.Font("Tahoma", 1, 36));
		lblBalanceDue.setForeground(new java.awt.Color(255, 102, 0));
		lblBalanceDue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		lblBalanceDue.setText("0.0");

		btnApplyCoupon.addActionListener(this);
		btnTaxExempt.addActionListener(this);
		btnViewDiscounts.addActionListener(this);
	}

	private JLabel createLabel(String text) {
		JLabel label = new JLabel(text);
		label.setHorizontalAlignment(JLabel.RIGHT);
		return label;
	}

	private JTextField createTextField(JTextField textField) {
		textField.setEditable(false);
		textField.setBackground(Color.white);
		return textField;
	}

	private void addRow(JPanel panel, String title, JTextField textField) {
		panel.add(createLabel(title), "newline, height pref");
		panel.add(createTextField(textField), "w 250,height pref");
	}

	protected double getTotalAmount() {
		List<Ticket> ticketsToSettle = getTickets();
		if (ticketsToSettle == null) {
			return 0;
		}

		double total = 0;
		for (Ticket ticket : ticketsToSettle) {
			total += ticket.getTotalAmount();
		}
		return total;
	}

	private void doTaxExempt() {//GEN-FIRST:event_doTaxExempt
		List<Ticket> ticketsToSettle = getTickets();

		boolean setTaxExempt = btnTaxExempt.isSelected();
		if (setTaxExempt) {
			int option = JOptionPane.showOptionDialog(this, com.floreantpos.POSConstants.CONFIRM_SET_TAX_EXEMPT, com.floreantpos.POSConstants.CONFIRM, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			if (option != JOptionPane.YES_OPTION) {
				return;
			}

			for (Ticket ticket : ticketsToSettle) {
				ticket.setTaxExempt(true);
				ticket.calculatePrice();
			}
		}
		else {
			for (Ticket ticket : ticketsToSettle) {
				ticket.setTaxExempt(false);
				ticket.calculatePrice();
			}
		}

		updateView();
		if(settleTicketView != null) {
			settleTicketView.updatePaymentView();
		}
	}//GEN-LAST:event_doTaxExempt

	private void doViewDiscounts() {//GEN-FIRST:event_btnViewDiscountsdoViewDiscounts
		try {
			DiscountListDialog dialog = new DiscountListDialog(tickets);
			dialog.open();
			if (!dialog.isCanceled() && dialog.isModified()) {
				updateModel();
				
				for (Ticket ticket : tickets) {
					TicketDAO.getInstance().saveOrUpdate(ticket);
				}
				updateView();
				
				if(settleTicketView != null) {
					settleTicketView.updatePaymentView();
				}
			}
		} catch (Exception e) {
			POSMessageDialog.showError(this, com.floreantpos.POSConstants.ERROR_MESSAGE, e);
		}
	}//GEN-LAST:event_btnViewDiscountsdoViewDiscounts

	private void doApplyCoupon() {//GEN-FIRST:event_btnApplyCoupondoApplyCoupon
		try {
			for (Ticket ticket : tickets) {
				if(ticket.getCouponAndDiscounts() != null && ticket.getCouponAndDiscounts().size() > 0) {
					POSMessageDialog.showError(com.floreantpos.POSConstants.DISCOUNT_COUPON_LIMIT_);
					return;
				}
			}
			
			List<Ticket> tickets = getTickets();
			Ticket ticket = tickets.get(0);
			CouponAndDiscountDialog dialog = new CouponAndDiscountDialog();
			dialog.setTicket(ticket);
			dialog.initData();
			dialog.open();
			if (!dialog.isCanceled()) {
				TicketCouponAndDiscount coupon = dialog.getSelectedCoupon();
				ticket.addTocouponAndDiscounts(coupon);
				updateModel();
				TicketDAO.getInstance().saveOrUpdate(ticket);
				updateView();
				if(settleTicketView != null) {
					settleTicketView.updatePaymentView();
				}
			}
		} catch (Exception e) {
			POSMessageDialog.showError(this, com.floreantpos.POSConstants.ERROR_MESSAGE, e);
		}
	}//GEN-LAST:event_btnApplyCoupondoApplyCoupon

	private void updateModel() {
		List<Ticket> ticketsToSettle = getTickets();
		for (Ticket ticket : ticketsToSettle) {
			ticket.calculatePrice();
		}
	}

	public void clearView() {
		tfTicketId.setText("");
		tfServerName.setText("");
		tfCreateTime.setText("");
		tfGuests.setText("");
		tfTable.setText("");
		tfTerminal.setText("");
		tfSubtotal.setText("");
		tfTotalDiscount.setText("");
		tfTax.setText("");
		tfTotal.setText("");
		tfDue.setText("");

		lblBalanceDue.setText("0.0");
	}

	private void updateView() {
		List<Ticket> tickets = getTickets();

		if (tickets.size() <= 0) {
			clearView();
			return;
		}

		String currencySymbol = Application.getCurrencySymbol();

		if (tickets.size() == 1) {
			Ticket ticket = tickets.get(0);
			if (ticket.getId() != null) {
				tfTicketId.setText(ticket.getId().toString());
			}
			tfServerId.setText(String.valueOf(ticket.getOwner().getUserId()));
			tfServerName.setText(ticket.getOwner().toString());
			tfCreateTime.setText(dateFormat.format(ticket.getCreateDate()));
			tfGuests.setText(String.valueOf(ticket.getNumberOfGuests()));
			tfTable.setText(String.valueOf(ticket.getTableNumber()));
			tfTerminal.setText(ticket.getTerminal().getName());
			tfSubtotal.setText(currencySymbol + Application.formatNumber(ticket.getSubtotalAmount()));
			tfTotalDiscount.setText(currencySymbol + Application.formatNumber(ticket.getDiscountAmount()));
			tfTax.setText(currencySymbol + Application.formatNumber(ticket.getTaxAmount()));
			tfTotal.setText(currencySymbol + Application.formatNumber(ticket.getTotalAmount()));
			tfDue.setText(currencySymbol + Application.formatNumber(ticket.getDueAmount()));

			lblBalanceDue.setText(Application.formatNumber(ticket.getDueAmount()));
			btnTaxExempt.setSelected(ticket.isTaxExempt());
			btnApplyCoupon.setEnabled(true);
			return;
		}

		Ticket firstTicket = tickets.get(0);
		String idString = "";
		int serverId = firstTicket.getOwner().getUserId();
		String serverName = firstTicket.getOwner().toString();
		String createTime = "<variant>";
		int totalGuests = 0;
		String tableNumber = "<variant>";
		String terminal = firstTicket.getTerminal().getName();
		double subtotal = 0;
		double discount = 0;
		double tax = 0;
		double total = 0;
		int taxExemptTicketCount = 0;

		for (Iterator iter = tickets.iterator(); iter.hasNext();) {
			Ticket ticket = (Ticket) iter.next();

			if (ticket.getId() != null) {
				idString += ticket.getId();
				if (iter.hasNext()) {
					idString += ", ";
				}
			}
			totalGuests += ticket.getNumberOfGuests();
			subtotal += ticket.getSubtotalAmount();
			discount += ticket.getDiscountAmount();
			tax += ticket.getTaxAmount();
			total += ticket.getTotalAmount();

			if (ticket.isTaxExempt()) {
				taxExemptTicketCount++;
			}
		}

		tfTicketId.setText(idString);
		tfServerId.setText(String.valueOf(serverId));
		tfServerName.setText(serverName);
		tfCreateTime.setText(createTime);
		tfGuests.setText(String.valueOf(totalGuests));
		tfTable.setText(tableNumber);
		tfTerminal.setText(terminal);
		tfSubtotal.setText(currencySymbol + Application.formatNumber(subtotal));
		tfTotalDiscount.setText(currencySymbol + Application.formatNumber(discount));
		tfTax.setText(currencySymbol + Application.formatNumber(tax));
		tfTotal.setText(currencySymbol + Application.formatNumber(total));

		lblBalanceDue.setText(Application.formatNumber(total));

		if (taxExemptTicketCount == tickets.size()) {
			btnTaxExempt.setSelected(true);
		}
		else {
			btnTaxExempt.setSelected(false);
		}
		btnApplyCoupon.setEnabled(false);
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables

	// End of variables declaration//GEN-END:variables

	public void setDiscountPanelVisible(boolean b) {
		balanceDuePanel.setVisible(b);
	}

	public boolean isDiscountPanelVisible() {
		return balanceDuePanel.isVisible();
	}

	public void setBalanceDuePanelVisible(boolean b) {
		balanceDuePanel.setVisible(b);
	}

	public boolean isBalanceDuePanelVisible() {
		return balanceDuePanel.isVisible();
	}

	public void setButtonPanelVisible(boolean b) {
		buttonPanel.setVisible(b);
	}

	public SettleTicketView getSettleTicketView() {
		return settleTicketView;
	}

	public void setSettleTicketView(SettleTicketView settleTicketView) {
		this.settleTicketView = settleTicketView;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;

		updateView();
	}

	public void setTicket(Ticket ticket) {
		tickets = new ArrayList<Ticket>(1);
		tickets.add(ticket);

		updateView();
	}

	public void cleanup() {
		tickets = null;
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == btnApplyCoupon) {
			doApplyCoupon();
		}
		if (source == btnTaxExempt) {
			doTaxExempt();
		}
		if (source == btnViewDiscounts) {
			doViewDiscounts();
		}
	}
}
