package com.floreantpos.ui.views;

import java.awt.LayoutManager;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.floreantpos.main.Application;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Ticket;
import com.floreantpos.print.PosPrintService;
import com.floreantpos.services.PosTransactionService;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.PaymentTypeSelectionDialog;
import com.floreantpos.ui.dialog.TransactionCompletionDialog;
import com.floreantpos.ui.views.order.RootView;

public abstract class PaymentView extends JPanel {
	protected SettleTicketView settleTicketView;
	
	public PaymentView(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	public PaymentView(LayoutManager layout) {
		super(layout);
	}

	public PaymentView(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public PaymentView() {
		super();
	}
	
	public SettleTicketView getSettleTicketView() {
		return settleTicketView;
	}

	public void setSettleTicketView(SettleTicketView settleTicketView) {
		this.settleTicketView = settleTicketView;
	}
	
	protected double getTotalAmount() {
		List<Ticket> ticketsToSettle = settleTicketView.getTicketsToSettle();
		if(ticketsToSettle == null) {
			return 0;
		}
		
		double total = 0;
		for (Ticket ticket : ticketsToSettle) {
			total += ticket.getTotalAmount();
		}
		return total;
	}
	
	protected double getPaidAmount() {
		List<Ticket> ticketsToSettle = settleTicketView.getTicketsToSettle();
		if(ticketsToSettle == null) {
			return 0;
		}
		
		double total = 0;
		for (Ticket ticket : ticketsToSettle) {
			total += ticket.getPaidAmount();
		}
		return total;
	}
	
	protected double getDueAmount() {
		List<Ticket> ticketsToSettle = settleTicketView.getTicketsToSettle();
		if(ticketsToSettle == null) {
			return 0;
		}
		
		double total = 0;
		for (Ticket ticket : ticketsToSettle) {
			total += ticket.getDueAmount();
		}
		return total;
	}
	
	protected double getTotalGratuity() {
		List<Ticket> ticketsToSettle = settleTicketView.getTicketsToSettle();
		if(ticketsToSettle == null) {
			return 0;
		}
		
		double total = 0;
		for (Ticket ticket : ticketsToSettle) {
			if(ticket.getGratuity() != null) {
				total += ticket.getGratuity().getAmount();
			}
		}
		return total;
	}

	public void changePaymentMethod() {
		PaymentTypeSelectionDialog dialog = new PaymentTypeSelectionDialog();
		dialog.setSize(250, 400);
		dialog.open();
		if (!dialog.isCanceled()) {
			SettleTicketView view = SettleTicketView.getInstance();
			view.setPaymentView(dialog.getSelectedPaymentView());
			
			view.setTicketsToSettle(settleTicketView.getTicketsToSettle());
			
			RootView.getInstance().showView(SettleTicketView.VIEW_NAME);
		}
	}
	
	public void settleTickets(double tenderedAmount, double gratuityAmount, PosTransaction posTransaction, String cardType, String cardAuthorizationCode) {
		try {
			double totalAmount = Double.parseDouble(Application.formatNumber(getTotalAmount()));
			double dueAmountBeforePaid = Double.parseDouble(Application.formatNumber(getDueAmount()));
			
			List<Ticket> ticketsToSettle = settleTicketView.getTicketsToSettle();
			
			if (ticketsToSettle.size() > 1 && tenderedAmount < dueAmountBeforePaid) {
				MessageDialog.showError("You cannot partially pay multiple tickets.");
				return;
			}
			
			PosTransactionService service = PosTransactionService.getInstance();
			service.settleTickets(ticketsToSettle, tenderedAmount, gratuityAmount, posTransaction, cardType, cardAuthorizationCode);
			
			try {
				for (Ticket ticket : ticketsToSettle) {
					PosPrintService.printTicket(ticket);
				}
			}catch(Exception ee) {
				POSMessageDialog.showError(Application.getPosWindow(), "There was an error while printing money receipt", ee);
			}
			
			/*if(Application.getPrinterConfiguration().isPrintReceiptWhenTicketPaid()) {
				try {
					for (Ticket ticket : ticketsToSettle) {
						PosPrintService.printTicket(ticket);
					}
				}catch(Exception ee) {
					POSMessageDialog.showError(Application.getPosWindow(), "There was an error while printing to kitchen", ee);
				}
			}
			
			if(Application.getPrinterConfiguration().isPrintKitchenWhenTicketPaid()) {
				try {
					for (Ticket ticket : ticketsToSettle) {
						if(ticket.needsKitchenPrint()) {
							PosPrintService.printToKitcken(ticket);
						}
						ticket.clearDeletedItems();
					}
				}catch(Exception ee) {
					POSMessageDialog.showError(Application.getPosWindow(), "There was an error while printing to kitchen", ee);
				}
			}*/
			
			double paidAmount = Double.parseDouble(Application.formatNumber(getPaidAmount()));
			double dueAmount = Double.parseDouble(Application.formatNumber(getDueAmount()));
			
			TransactionCompletionDialog dialog = TransactionCompletionDialog.getInstance();
			dialog.setTickets(ticketsToSettle);
			dialog.setTenderedAmount(tenderedAmount);
			dialog.setTotalAmount(totalAmount);
			dialog.setPaidAmount(paidAmount);
			dialog.setDueAmount(dueAmount);
			dialog.setDueAmountBeforePaid(dueAmountBeforePaid);
			dialog.setGratuityAmount(gratuityAmount);
			dialog.updateView();
			dialog.pack();
			dialog.open();
			
			if(dueAmount > 0.0) {
				int option = JOptionPane.showConfirmDialog(Application.getPosWindow(), "A partial payment has been made, do you want to make another payment on this check?", "MDS-POS", JOptionPane.YES_NO_OPTION);
				if(option != JOptionPane.YES_OPTION) {
					RootView.getInstance().showView(SwitchboardView.VIEW_NAME);
					return;
				}
				
				PaymentTypeSelectionDialog paymentTypeSelectionDialog = new PaymentTypeSelectionDialog();
				paymentTypeSelectionDialog.setSize(250, 400);
				paymentTypeSelectionDialog.open();

				if (!paymentTypeSelectionDialog.isCanceled()) {
					settleTicketView.setPaymentView(paymentTypeSelectionDialog.getSelectedPaymentView());
					settleTicketView.setTicketsToSettle(ticketsToSettle);
				}
			}
			else {
				RootView.getInstance().showView(SwitchboardView.VIEW_NAME);
			}
		} catch (Exception e) {
			POSMessageDialog.showError(this, POSMessageDialog.ERROR_MESSAGE, e);
		}
	}
	
	public abstract void updateView();
}
