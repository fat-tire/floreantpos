/*
 * TicketInfoView.java
 *
 * Created on August 13, 2006, 11:17 PM
 */

package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JasperPrint;

import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketCouponAndDiscount;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.report.TicketPrintProperties;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.CouponAndDiscountDialog;
import com.floreantpos.ui.dialog.DiscountListDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.NumberUtil;

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

	private SettleTicketView settleTicketView;

	private JPanel topPanel;
	private JPanel balanceDuePanel;
	private JPanel buttonPanel;

	private List<Ticket> tickets;

	/** Creates new form TicketInfoView */
	public TicketDetailView() {

		setLayout(new BorderLayout(5, 5));

		topPanel = new JPanel(new GridLayout());
		add(topPanel, BorderLayout.CENTER);

		balanceDuePanel = new JPanel(new GridLayout(0, 1, 5, 5));
		
		JLabel balanceDueTitle = new JLabel(com.floreantpos.POSConstants.BALANCE_DUE);
		balanceDueTitle.setFont(new Font("Dialog", Font.BOLD, 16));
		balanceDueTitle.setHorizontalAlignment(SwingConstants.CENTER);
		balanceDuePanel.add(balanceDueTitle);
		
		lblBalanceDue = new JLabel("0");
		lblBalanceDue.setFont(new Font("Dialog", Font.BOLD, 16));
		lblBalanceDue.setHorizontalAlignment(SwingConstants.CENTER);
		balanceDuePanel.add(lblBalanceDue);
		
		balanceDuePanel.add(new JSeparator(JSeparator.HORIZONTAL));

		buttonPanel = new JPanel();
		buttonPanel.add(btnApplyCoupon = new PosButton(com.floreantpos.POSConstants.COUPON_DISCOUNT));
		buttonPanel.add(btnViewDiscounts = new PosButton(com.floreantpos.POSConstants.VIEW_DISCOUNTS));
		buttonPanel.add(btnTaxExempt = new POSToggleButton(com.floreantpos.POSConstants.TAX_EXEMPT));

		JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
		bottomPanel.add(balanceDuePanel);
		bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(bottomPanel, BorderLayout.SOUTH);

		setOpaque(false);

		btnApplyCoupon.addActionListener(this);
		btnTaxExempt.addActionListener(this);
		btnViewDiscounts.addActionListener(this);
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
		topPanel.removeAll();
	}

	private void updateView() {
		try {
			clearView();
			
			List<Ticket> tickets = getTickets();

			int totalTicket = tickets.size();
			
			if (totalTicket <= 0) {
				return;
			}
			
			int taxExemptTicketCount = 0;
			double totalDue = 0;
			
			JPanel reportPanel = new JPanel(new MigLayout("wrap 1, ax 50%","",""));
			JScrollPane scrollPane = new JScrollPane(reportPanel);
			scrollPane.getVerticalScrollBar().setUnitIncrement(20);

			for (Iterator iter = tickets.iterator(); iter.hasNext();) {
				Ticket ticket = (Ticket) iter.next();
				totalDue += ticket.getDueAmount();
				
				TicketPrintProperties printProperties = new TicketPrintProperties("*** ORDER " + ticket.getId() + " ***", false, true, true);
				JasperPrint jasperPrint = JReportPrintService.createPrint(ticket, printProperties);

				TicketReceiptView receiptView = new TicketReceiptView(jasperPrint);
				reportPanel.add(receiptView.getReportPanel());
				
				if(ticket.isTaxExempt()) {
					taxExemptTicketCount++;
				}
			}
			
			topPanel.add(scrollPane, BorderLayout.CENTER);
			lblBalanceDue.setText(Application.getCurrencySymbol() + " " + NumberUtil.roundToTwoDigit(totalDue));
			
			if (taxExemptTicketCount == tickets.size()) {
				btnTaxExempt.setSelected(true);
			}
			else {
				btnTaxExempt.setSelected(false);
			}
			
			btnApplyCoupon.setEnabled(totalTicket == 1);
			
		} catch (Exception e) {
			POSMessageDialog.showError(e.getMessage());
		}
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
