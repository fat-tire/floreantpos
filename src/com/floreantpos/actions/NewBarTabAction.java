package com.floreantpos.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Calendar;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import net.authorize.data.creditcard.CardType;

import com.floreantpos.main.Application;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.Ticket;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.PaymentTypeSelectionDialog;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.ui.views.order.RootView;
import com.floreantpos.ui.views.payment.AuthorizationCodeDialog;
import com.floreantpos.ui.views.payment.AuthorizeDoNetProcessor;
import com.floreantpos.ui.views.payment.CardInputListener;
import com.floreantpos.ui.views.payment.CardInputter;
import com.floreantpos.ui.views.payment.ManualCardEntryDialog;
import com.floreantpos.ui.views.payment.PaymentProcessWaitDialog;
import com.floreantpos.ui.views.payment.SwipeCardDialog;

public class NewBarTabAction extends AbstractAction implements CardInputListener {
	private Component parentComponent;
	private PaymentType selectedPaymentType;

	public NewBarTabAction(Component parentComponent) {
		super("BAR TAB");
		
		this.parentComponent = parentComponent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		PaymentTypeSelectionDialog paymentTypeSelectionDialog = new PaymentTypeSelectionDialog();
		paymentTypeSelectionDialog.setCashButtonVisible(false);
		paymentTypeSelectionDialog.pack();
		paymentTypeSelectionDialog.setLocationRelativeTo(parentComponent);
		paymentTypeSelectionDialog.setVisible(true);
		
		if(paymentTypeSelectionDialog.isCanceled()) {
			return;
		}
		
		selectedPaymentType = paymentTypeSelectionDialog.getSelectedPaymentType();
		
		SwipeCardDialog dialog = new SwipeCardDialog(this);
		dialog.setTitle("Enter credit card information");
		dialog.pack();
		dialog.setLocationRelativeTo(parentComponent);
		dialog.setVisible(true);
	}

	@Override
	public void cardInputted(CardInputter inputter) {
		if (inputter instanceof SwipeCardDialog) {
			useSwipeCard(inputter);
		}
		else if (inputter instanceof ManualCardEntryDialog) {
			useManualCard(inputter);
		}
		else if (inputter instanceof AuthorizationCodeDialog) {
			POSMessageDialog.showError("to be implemented");
		}
	}

	private void useSwipeCard(CardInputter inputter) {
		Application application = Application.getInstance();
		
		String symbol = Application.getCurrencySymbol();
		String message = symbol + "25 will be booked. Proceed?";
		
		int option = JOptionPane.showOptionDialog(parentComponent, message, "Please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if(option != JOptionPane.YES_OPTION) {
			return;
		}
		
		SwipeCardDialog swipeCardDialog = (SwipeCardDialog) inputter;
		String cardString = swipeCardDialog.getCardString();
		
		PaymentProcessWaitDialog waitDialog = new PaymentProcessWaitDialog(Application.getPosWindow());
		waitDialog.setVisible(true);
		
		try {
			CardType cardType = CardType.findByValue(selectedPaymentType.getDisplayString());
			
			String transactionId = AuthorizeDoNetProcessor.authorizeAmount(cardString, 25, cardType);
			
			Ticket ticket = new Ticket();
			
			ticket.setPriceIncludesTax(application.isPriceIncludesTax());
			ticket.setTicketType(Ticket.BAR_TAB);
			ticket.setTableNumber(-1);
			ticket.setTerminal(application.getTerminal());
			ticket.setOwner(Application.getCurrentUser());
			ticket.setShift(application.getCurrentShift());
			
			ticket.addProperty(Ticket.PROPERTY_PAYMENT_METHOD, selectedPaymentType.getDisplayString());
			ticket.addProperty(Ticket.PROPERTY_CARD_TRANSACTION_ID, transactionId);
			ticket.addProperty(Ticket.PROPERTY_CARD_TRACKS, cardString);
			ticket.addProperty(Ticket.PROPERTY_CARD_NAME, selectedPaymentType.getDisplayString());
			ticket.addProperty(Ticket.PROPERTY_CARD_INPUT_METHOD, "swipe");

			Calendar currentTime = Calendar.getInstance();
			ticket.setCreateDate(currentTime.getTime());
			ticket.setCreationHour(currentTime.get(Calendar.HOUR_OF_DAY));
			
			waitDialog.setVisible(false);

			OrderView.getInstance().setCurrentTicket(ticket);
			RootView.getInstance().showView(OrderView.VIEW_NAME);
		} catch (Exception e) {
			POSMessageDialog.showError(parentComponent, "Unable to authorize card.");
		} finally {
			waitDialog.setVisible(false);
		}
	}
	
	private void useManualCard(CardInputter inputter) {
		Application application = Application.getInstance();
		
		String symbol = Application.getCurrencySymbol();
		String message = symbol + "25 will be booked. Proceed?";
		
		int option = JOptionPane.showOptionDialog(parentComponent, message, "Please confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if(option != JOptionPane.YES_OPTION) {
			return;
		}
		
		ManualCardEntryDialog mDialog = (ManualCardEntryDialog) inputter;
		String cardNumber = mDialog.getCardNumber();
		String expMonth = mDialog.getExpMonth();
		String expYear = mDialog.getExpYear();
		
		PaymentProcessWaitDialog waitDialog = new PaymentProcessWaitDialog(Application.getPosWindow());
		waitDialog.setVisible(true);
		
		try {
			CardType cardType = CardType.findByValue(selectedPaymentType.getDisplayString());
			
			String transactionId = AuthorizeDoNetProcessor.authorizeAmount(cardNumber, expMonth, expYear, 25, cardType);
			
			Ticket ticket = new Ticket();
			
			ticket.setPriceIncludesTax(application.isPriceIncludesTax());
			ticket.setTicketType(Ticket.BAR_TAB);
			ticket.setTableNumber(-1);
			ticket.setTerminal(application.getTerminal());
			ticket.setOwner(Application.getCurrentUser());
			ticket.setShift(application.getCurrentShift());
			
			ticket.addProperty(Ticket.PROPERTY_PAYMENT_METHOD, selectedPaymentType.getDisplayString());
			ticket.addProperty(Ticket.PROPERTY_CARD_TRANSACTION_ID, transactionId);
			ticket.addProperty(Ticket.PROPERTY_CARD_NUMBER, cardNumber);
			ticket.addProperty(Ticket.PROPERTY_CARD_EXP_YEAR, expYear);
			ticket.addProperty(Ticket.PROPERTY_CARD_EXP_MONTH, expMonth);
			ticket.addProperty(Ticket.PROPERTY_CARD_NAME, selectedPaymentType.getDisplayString());
			ticket.addProperty(Ticket.PROPERTY_CARD_INPUT_METHOD, "manual");

			Calendar currentTime = Calendar.getInstance();
			ticket.setCreateDate(currentTime.getTime());
			ticket.setCreationHour(currentTime.get(Calendar.HOUR_OF_DAY));
			
			waitDialog.setVisible(false);

			OrderView.getInstance().setCurrentTicket(ticket);
			RootView.getInstance().showView(OrderView.VIEW_NAME);
		} catch (Exception e) {
			POSMessageDialog.showError(parentComponent, "Unable to authorize card.");
		} finally {
			waitDialog.setVisible(false);
		}
	}

}
