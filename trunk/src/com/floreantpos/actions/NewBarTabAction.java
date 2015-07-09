package com.floreantpos.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Calendar;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import net.authorize.data.creditcard.CardType;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.ITicketList;
import com.floreantpos.Messages;
import com.floreantpos.PosException;
import com.floreantpos.config.CardConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.CardReader;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.PaymentTypeSelectionDialog;
import com.floreantpos.ui.views.payment.AuthorizationCodeDialog;
import com.floreantpos.ui.views.payment.CardInputListener;
import com.floreantpos.ui.views.payment.CardInputter;
import com.floreantpos.ui.views.payment.ManualCardEntryDialog;
import com.floreantpos.ui.views.payment.PaymentProcessWaitDialog;
import com.floreantpos.ui.views.payment.SwipeCardDialog;

public class NewBarTabAction extends AbstractAction implements CardInputListener {
	private Component parentComponent;
	private PaymentType selectedPaymentType;

	public NewBarTabAction(Component parentComponent) {
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
		dialog.setTitle(Messages.getString("NewBarTabAction.0")); //$NON-NLS-1$
		dialog.pack();
		dialog.setLocationRelativeTo(parentComponent);
		dialog.setVisible(true);
	}
	
	private Ticket createTicket(Application application) {
		Ticket ticket = new Ticket();
		
		ticket.setPriceIncludesTax(application.isPriceIncludesTax());
		ticket.setType(OrderType.BAR_TAB);
		ticket.setTerminal(application.getTerminal());
		ticket.setOwner(Application.getCurrentUser());
		ticket.setShift(application.getCurrentShift());
		
		Calendar currentTime = Calendar.getInstance();
		ticket.setCreateDate(currentTime.getTime());
		ticket.setCreationHour(currentTime.get(Calendar.HOUR_OF_DAY));
		
		return ticket;
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
			useAuthCode(inputter);
		}
	}

	private void useAuthCode(CardInputter inputter) {
		try {
			
			AuthorizationCodeDialog authDialog = (AuthorizationCodeDialog) inputter;
			String authorizationCode = authDialog.getAuthorizationCode();
			if (StringUtils.isEmpty(authorizationCode)) {
				throw new PosException(Messages.getString("NewBarTabAction.1")); //$NON-NLS-1$
			}

			Ticket ticket = createTicket(Application.getInstance());

			ticket.addProperty(Ticket.PROPERTY_CARD_AUTH_CODE, authorizationCode);
			ticket.addProperty(Ticket.PROPERTY_PAYMENT_METHOD, selectedPaymentType.name());
			ticket.addProperty(Ticket.PROPERTY_CARD_NAME, selectedPaymentType.getDisplayString());
			ticket.addProperty(Ticket.PROPERTY_CARD_READER, CardReader.EXTERNAL_TERMINAL.name());
			
			//ticket.addToticketItems(createTabOpenItem(ticket));
			
			TicketDAO.getInstance().save(ticket);
			
			POSMessageDialog.showMessage(Messages.getString("NewBarTabAction.2") + ticket.getId()); //$NON-NLS-1$
			if(parentComponent instanceof ITicketList) {
				((ITicketList) parentComponent).updateTicketList();
			}
			
			//OrderView.getInstance().setCurrentTicket(ticket);
			//RootView.getInstance().showView(OrderView.VIEW_NAME);
			
		} catch (Exception e) {
			e.printStackTrace();
			POSMessageDialog.showError(parentComponent, e.getMessage());
		}
	}

	private void useSwipeCard(CardInputter inputter) {
		Application application = Application.getInstance();
		
		String symbol = Application.getCurrencySymbol();
		String message = symbol + CardConfig.getBartabLimit() + Messages.getString("NewBarTabAction.3"); //$NON-NLS-1$
		
		int option = POSMessageDialog.showYesNoQuestionDialog(parentComponent, message, Messages.getString("NewBarTabAction.4")); //$NON-NLS-1$
		if(option != JOptionPane.YES_OPTION) {
			return;
		}
		
		SwipeCardDialog swipeCardDialog = (SwipeCardDialog) inputter;
		String cardString = swipeCardDialog.getCardString();
		
		PaymentProcessWaitDialog waitDialog = new PaymentProcessWaitDialog(Application.getPosWindow());
		waitDialog.setVisible(true);
		
		try {
			Ticket ticket = createTicket(application);
			
			String transactionId = CardConfig.getMerchantGateway().getProcessor().authorizeAmount(ticket, cardString, CardConfig.getBartabLimit(), selectedPaymentType.getDisplayString());
			
			ticket.addProperty(Ticket.PROPERTY_PAYMENT_METHOD, selectedPaymentType.name());
			ticket.addProperty(Ticket.PROPERTY_CARD_NAME, selectedPaymentType.name());
			ticket.addProperty(Ticket.PROPERTY_CARD_TRANSACTION_ID, transactionId);
			ticket.addProperty(Ticket.PROPERTY_CARD_TRACKS, cardString);
			ticket.addProperty(Ticket.PROPERTY_CARD_READER, CardReader.SWIPE.name());
			ticket.addProperty(Ticket.PROPERTY_ADVANCE_PAYMENT, String.valueOf(CardConfig.getBartabLimit()));
			
			//ticket.addToticketItems(createTabOpenItem(ticket));
			
			TicketDAO.getInstance().save(ticket);

			waitDialog.setVisible(false);

			POSMessageDialog.showMessage(Messages.getString("NewBarTabAction.5") + ticket.getId()); //$NON-NLS-1$
			if(parentComponent instanceof ITicketList) {
				((ITicketList) parentComponent).updateTicketList();
			}
			
			//OrderView.getInstance().setCurrentTicket(ticket);
			//RootView.getInstance().showView(OrderView.VIEW_NAME);
		} catch (Exception e) {
			e.printStackTrace();
			POSMessageDialog.showError(parentComponent, e.getMessage());
		} finally {
			waitDialog.setVisible(false);
		}
	}
	
	private void useManualCard(CardInputter inputter) {
		Application application = Application.getInstance();
		
		String symbol = Application.getCurrencySymbol();
		String message = symbol + CardConfig.getBartabLimit() + Messages.getString("NewBarTabAction.6"); //$NON-NLS-1$
		
		int option = POSMessageDialog.showYesNoQuestionDialog(parentComponent, message, Messages.getString("NewBarTabAction.7")); //$NON-NLS-1$
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
			
			String transactionId = CardConfig.getMerchantGateway().getProcessor().authorizeAmount(cardNumber, expMonth, expYear, CardConfig.getBartabLimit(), cardType);
			
			Ticket ticket = createTicket(application);
			
			ticket.addProperty(Ticket.PROPERTY_PAYMENT_METHOD, selectedPaymentType.name());
			ticket.addProperty(Ticket.PROPERTY_CARD_NAME, selectedPaymentType.name());
			ticket.addProperty(Ticket.PROPERTY_CARD_TRANSACTION_ID, transactionId);
			ticket.addProperty(Ticket.PROPERTY_CARD_NUMBER, cardNumber);
			ticket.addProperty(Ticket.PROPERTY_CARD_EXP_YEAR, expYear);
			ticket.addProperty(Ticket.PROPERTY_CARD_EXP_MONTH, expMonth);
			ticket.addProperty(Ticket.PROPERTY_CARD_READER, CardReader.MANUAL.name());
			ticket.addProperty(Ticket.PROPERTY_ADVANCE_PAYMENT, String.valueOf(CardConfig.getBartabLimit()));
			
			//ticket.addToticketItems(createTabOpenItem(ticket));
			
			TicketDAO.getInstance().save(ticket);
			
			waitDialog.setVisible(false);
			
			POSMessageDialog.showMessage(Messages.getString("NewBarTabAction.8") + ticket.getId()); //$NON-NLS-1$
			if(parentComponent instanceof ITicketList) {
				((ITicketList) parentComponent).updateTicketList();
			}

			//OrderView.getInstance().setCurrentTicket(ticket);
			//RootView.getInstance().showView(OrderView.VIEW_NAME);
		} catch (Exception e) {
			e.printStackTrace();
			POSMessageDialog.showError(parentComponent, Messages.getString("NewBarTabAction.9")); //$NON-NLS-1$
		} finally {
			waitDialog.setVisible(false);
		}
	}
}
