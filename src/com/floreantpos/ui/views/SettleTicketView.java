package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import net.authorize.data.creditcard.CardType;

import com.floreantpos.config.CardConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.CashTransaction;
import com.floreantpos.model.CreditCardTransaction;
import com.floreantpos.model.MerchantGateway;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketCouponAndDiscount;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.ui.dialog.CouponAndDiscountDialog;
import com.floreantpos.ui.dialog.DiscountListDialog;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.PaymentTypeSelectionDialog;
import com.floreantpos.ui.dialog.SwipeCardDialog;
import com.floreantpos.ui.views.payment.CashPaymentView;
import com.floreantpos.ui.views.payment.CreditCardTransactionProcessor;

public class SettleTicketView extends POSDialog {
	public final static String VIEW_NAME = "PAYMENT_VIEW";

	private String previousViewName = SwitchboardView.VIEW_NAME;

	private com.floreantpos.swing.TransparentPanel leftPanel = new com.floreantpos.swing.TransparentPanel(new BorderLayout());
	private com.floreantpos.swing.TransparentPanel rightPanel = new com.floreantpos.swing.TransparentPanel(new BorderLayout());

	private TicketDetailView ticketDetailView;
	private CashPaymentView paymentView;
	protected List<Ticket> ticketsToSettle;

	public SettleTicketView() {
		super(Application.getPosWindow(), true);
		setTitle("Settle ticket");

		getContentPane().setLayout(new BorderLayout(5, 5));

		ticketDetailView = new TicketDetailView();
		ticketDetailView.setSettleTicketView(this);

		paymentView = new CashPaymentView();
		paymentView.setSettleTicketView(this);

		leftPanel.add(ticketDetailView);
		rightPanel.add(paymentView);

		getContentPane().add(leftPanel, BorderLayout.CENTER);
		getContentPane().add(rightPanel, BorderLayout.EAST);
	}

	public void setCurrentTicket(Ticket currentTicket) {
		ticketsToSettle = new ArrayList<Ticket>();
		ticketsToSettle.add(currentTicket);

		ticketDetailView.setTickets(getTicketsToSettle());
		paymentView.updateView();
	}

	private void updateModel() {
		List<Ticket> ticketsToSettle = getTicketsToSettle();

		for (Ticket ticket : ticketsToSettle) {
			ticket.calculatePrice();
		}
	}

	public void doApplyCoupon() {//GEN-FIRST:event_btnApplyCoupondoApplyCoupon
		try {
			List<Ticket> tickets = getTicketsToSettle();

			for (Ticket ticket : tickets) {
				if (ticket.getCouponAndDiscounts() != null && ticket.getCouponAndDiscounts().size() > 0) {
					POSMessageDialog.showError(com.floreantpos.POSConstants.DISCOUNT_COUPON_LIMIT_);
					return;
				}
			}

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
				ticketDetailView.updateView();
				paymentView.updateView();
			}
		} catch (Exception e) {
			POSMessageDialog.showError(this, com.floreantpos.POSConstants.ERROR_MESSAGE, e);
		}
	}//GEN-LAST:event_btnApplyCoupondoApplyCoupon

	public void doTaxExempt(boolean taxExempt) {//GEN-FIRST:event_doTaxExempt
		List<Ticket> ticketsToSettle = getTicketsToSettle();

		boolean setTaxExempt = taxExempt;
		if (setTaxExempt) {
			int option = JOptionPane.showOptionDialog(this, com.floreantpos.POSConstants.CONFIRM_SET_TAX_EXEMPT, com.floreantpos.POSConstants.CONFIRM,
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			if (option != JOptionPane.YES_OPTION) {
				return;
			}

			for (Ticket ticket : ticketsToSettle) {
				ticket.setTaxExempt(true);
				ticket.calculatePrice();
				TicketDAO.getInstance().saveOrUpdate(ticket);
			}
		}
		else {
			for (Ticket ticket : ticketsToSettle) {
				ticket.setTaxExempt(false);
				ticket.calculatePrice();
				TicketDAO.getInstance().saveOrUpdate(ticket);
			}
		}

		ticketDetailView.updateView();
		paymentView.updateView();
	}//GEN-LAST:event_doTaxExempt

	public void doViewDiscounts() {//GEN-FIRST:event_btnViewDiscountsdoViewDiscounts
		try {
			List<Ticket> tickets = getTicketsToSettle();

			DiscountListDialog dialog = new DiscountListDialog(tickets);
			dialog.open();

			if (!dialog.isCanceled() && dialog.isModified()) {
				updateModel();

				for (Ticket ticket : tickets) {
					TicketDAO.getInstance().saveOrUpdate(ticket);
				}

				ticketDetailView.updateView();
				paymentView.updateView();
			}
		} catch (Exception e) {
			POSMessageDialog.showError(this, com.floreantpos.POSConstants.ERROR_MESSAGE, e);
		}
	}//GEN-LAST:event_btnViewDiscountsdoViewDiscounts

	public void doSettle() {
		try {

			PaymentTypeSelectionDialog dialog = new PaymentTypeSelectionDialog();
			dialog.setSize(250, 400);
			dialog.open();
			if (dialog.isCanceled()) {
				return;
			}

			double tenderedAmount = paymentView.getTenderedAmount();
			double gratuityAmount = paymentView.getGratuityAmount();

			switch (dialog.getSelectedPaymentType()) {
				case CASH:
					paymentView.settleTickets(tenderedAmount, gratuityAmount, new CashTransaction(), null, null);
					setCanceled(false);
					dispose();
					break;

				case VISA:
					payUsingCard(PaymentType.VISA, tenderedAmount, gratuityAmount);
					break;
					
				case MASTER_CARD:
					payUsingCard(PaymentType.MASTER_CARD, tenderedAmount, gratuityAmount);
					break;

				default:
					break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void payUsingCard(PaymentType cardType, final double tenderedAmount, final double gratuityAmount) throws Exception {
		SwipeCardDialog swipeCardDialog = new SwipeCardDialog();
		swipeCardDialog.pack();
		swipeCardDialog.open();

		if (swipeCardDialog.isCanceled()) {
			return;
		}
		String cardString = swipeCardDialog.getCardString();

		if (CardConfig.getMerchantGateway() == MerchantGateway.AUTHORIZE_NET) {
			String authorizationCode = CreditCardTransactionProcessor.processUsingAuthorizeDotNet(cardString, tenderedAmount, CardType.VISA);
			paymentView.settleTickets(tenderedAmount, gratuityAmount, new CreditCardTransaction(), cardType.toString(), authorizationCode);
		}
		
		setCanceled(false);
		dispose();
	}

	public void updatePaymentView() {
		paymentView.updateView();
	}

	public String getPreviousViewName() {
		return previousViewName;
	}

	public void setPreviousViewName(String previousViewName) {
		this.previousViewName = previousViewName;
	}

	public List<Ticket> getTicketsToSettle() {
		return ticketsToSettle;
	}

	public void setTicketsToSettle(List<Ticket> ticketsToSettle) {
		this.ticketsToSettle = ticketsToSettle;

		ticketDetailView.setTickets(getTicketsToSettle());
		paymentView.updateView();
	}

	public TicketDetailView getTicketDetailView() {
		return ticketDetailView;
	}
}
