package com.floreantpos.posserver;

import java.io.DataOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.jfree.util.Log;
import org.xml.sax.InputSource;

import com.floreantpos.PosLog;
import com.floreantpos.main.Application;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.services.PosTransactionService;
import com.floreantpos.ui.views.order.RootView;
import com.floreantpos.ui.views.payment.SettleTicketProcessor;

public class PosRequestHandler extends Thread {
	private Socket socket;

	public PosRequestHandler(Socket socket) throws Exception {
		this.socket = socket;
	}

	@Override
	public void run() {

		try {
			while (true) {
				byte[] b1 = new byte[3000];
				socket.getInputStream().read(b1);

				String request = new String(b1).trim();
				if (request.length() <= 0) {
					break;
				}

				PosLog.info(getClass(), "Request From Terminal==>[" + request + "]");

				int index = request.indexOf("<");
				request = request.substring(index);

				POSRequest posRequest = createRequest(request);
				POSResponse posResponse = createResponse(posRequest);

				String resp = convertResponseToString(posResponse);

				PosLog.info(getClass(),"Reponse to Terminal===>[" + resp + "]");

				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				byte[] tosend = resp.getBytes();
				dos.write(tosend, 0, tosend.length);
				dos.flush();

			}

		} catch (Exception e) {
			Log.debug("Error:" + e);

		} finally {
			try {
				Thread.sleep(5000);
				socket.close();
			} catch (Exception e) {
				Log.debug("Error:" + e);
			}
		}
	}

	private POSRequest createRequest(String requestString) throws Exception {
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(requestString));

		JAXBContext jaxbContext = JAXBContext.newInstance(POSRequest.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		return (POSRequest) unmarshaller.unmarshal(is);
	}

	private POSResponse createResponse(POSRequest posRequest) {
		POSResponse posResponse = new POSResponse();

		//TODO: implement this method.
		
//		String ttype = posRequest.ident.ttype;

//		if (SettleTicketDialog.waitDialog.isVisible()) {
//			Ticket ticket = SettleTicketDialog.getInstance().getTicket();
//			posRequest.posDefaultInfo.check = ticket.getId().toString();
//			posRequest.posDefaultInfo.table = ticket.getTableNumbers().get(0).toString();
//		}
//
//		if (ttype.equals(Ident.GET_TABLES)) {
//			if (posRequest.posDefaultInfo.table.equals("0")) {
//				posResponse = addAllTables(posRequest);
//			}
//			else {
//				posResponse = addTable(posRequest);
//			}
//		}
//		else if (ttype.equals(Ident.APPLY_PAYMENT)) {
//			posResponse = applyPayment(posRequest);
//
//			if (posRequest.payment.edc.equals("1")) {
//				int checkId = Integer.parseInt(posRequest.posDefaultInfo.check);
//				posResponse.setPrintChecks(getPrintText(checkId));
//			}
//		}
//		else if (ttype.equals(Ident.PRINT_CHECK)) {
//			posResponse = printCheck(posRequest);
//		}
//
//		Ident ident = new Ident();
//		ident.setId(posRequest.ident.id);
//		ident.setTermserialno(posRequest.ident.termserialno);
//		ident.setTtype(posRequest.ident.ttype);
//
//		posResponse.setIdent(ident);

		return posResponse;
	}

	private String convertResponseToString(POSResponse posResponse) throws Exception {
		JAXBContext messageContext = JAXBContext.newInstance(POSResponse.class);
		Marshaller marshaller = messageContext.createMarshaller();
		StringWriter dataWriter = new StringWriter();
		marshaller.marshal(posResponse, dataWriter);

		String resp = "";
		resp = dataWriter.toString();
		resp = resp.replaceAll("<\\?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"\\?>", "");

		String len = String.format("%05d", resp.length());
		resp = len + resp;

		return resp;
	}

	private POSResponse addAllTables(POSRequest posRequest) {
		POSResponse posResponse = new POSResponse();

		User user = UserDAO.getInstance().findUserBySecretKey(posRequest.posDefaultInfo.server);
		List<Ticket> ticketsForUser = TicketDAO.getInstance().findOpenTicketsForUser(user);

		Checks checks = new Checks();

		checks.setCheckList(new ArrayList<Check>());

		for (Ticket ticket : ticketsForUser) {
			List<Integer> tableNumbers = ticket.getTableNumbers();
			if (tableNumbers != null && tableNumbers.size() > 0) {
				Check chk = new Check();
				String tableNumber = tableNumbers.get(0).toString();
				if (tableNumbers.get(0) < 10) {
					tableNumber = "0" + tableNumbers.get(0).toString();
				}
				chk.setTableNo(tableNumber);
				chk.setTableName("");
				chk.setChkName(String.valueOf(ticket.getId()));
				chk.setChkNo(String.valueOf(ticket.getId()));
				chk.setAmt(String.valueOf(Math.round((ticket.getDueAmount() - ticket.getTaxAmount()) * 100)));
				chk.setTax(String.valueOf(Math.round(ticket.getTaxAmount() * 100)));
				checks.getCheckList().add(chk);
			}
		}
		posResponse.setChecks(checks);

		POSDefaultInfo posDefaultInfo = new POSDefaultInfo();
		posDefaultInfo.setServer(posRequest.posDefaultInfo.server);
		posDefaultInfo.setTable(posRequest.posDefaultInfo.table);
		posDefaultInfo.setCheck(posRequest.posDefaultInfo.check);
		posDefaultInfo.setRes("1");
		posDefaultInfo.setrText("success");

		posResponse.setPosDefaultInfo(posDefaultInfo);

		return posResponse;
	}

	private POSResponse addTable(POSRequest posRequest) {
		POSResponse posResponse = new POSResponse();

		User user = UserDAO.getInstance().findUserBySecretKey(posRequest.posDefaultInfo.server);
		List<Ticket> ticketsForUser = TicketDAO.getInstance().findOpenTicketsForUser(user);

		Checks checks = new Checks();

		checks.setCheckList(new ArrayList<Check>());

		for (Ticket ticket : ticketsForUser) {
			List<Integer> tableNumbers = ticket.getTableNumbers();
			if (tableNumbers != null && tableNumbers.size() > 0) {
				if (tableNumbers.contains(Integer.parseInt(posRequest.posDefaultInfo.table))) {
					Check chk = new Check();
					String tableNumber = tableNumbers.get(0).toString();
					if (tableNumbers.get(0) < 10) {
						tableNumber = "0" + tableNumbers.get(0).toString();
					}
					chk.setTableNo(String.valueOf(tableNumber));
					chk.setTableName("");
					chk.setChkName("");
					chk.setChkNo(String.valueOf(ticket.getId()));
					chk.setAmt(String.valueOf(Math.round((ticket.getDueAmount() - ticket.getTaxAmount()) * 100)));
					chk.setTax(String.valueOf(Math.round(ticket.getTaxAmount() * 100)));
					checks.getCheckList().add(chk);
					break;
				}
			}
		}
		posResponse.setChecks(checks);

		POSDefaultInfo posDefaultInfo = new POSDefaultInfo();
		posDefaultInfo.setServer(posRequest.posDefaultInfo.server);
		posDefaultInfo.setTable(posRequest.posDefaultInfo.table);
		posDefaultInfo.setCheck(posRequest.posDefaultInfo.check);
		posDefaultInfo.setRes("1");
		posDefaultInfo.setrText("success");

		posResponse.setPosDefaultInfo(posDefaultInfo);

		return posResponse;
	}

	private POSResponse applyPayment(POSRequest posRequest) {
		POSResponse posResponse = new POSResponse();

		Ticket ticket = TicketDAO.getInstance().loadFullTicket(Integer.parseInt(posRequest.posDefaultInfo.check));
		String paymentType = posRequest.payment.cardType;

		PosTransaction transaction = null;
		if (paymentType.equals(CardType.CASH)) {
			transaction = PaymentType.CASH.createTransaction();
			transaction.setCaptured(true);
		}
		else {

			if (paymentType.equals(CardType.CREDIT_MASTER_CARD)) {
				transaction = PaymentType.CREDIT_MASTER_CARD.createTransaction();
			}
			else if (paymentType.equals(CardType.CREDIT_VISA)) {
				transaction = PaymentType.CREDIT_VISA.createTransaction();
			}
			else if (paymentType.equals(CardType.CREDIT_DISCOVERY)) {
				transaction = PaymentType.CREDIT_DISCOVERY.createTransaction();
			}
			else if (paymentType.equals(CardType.CREDIT_AMEX)) {
				transaction = PaymentType.CREDIT_AMEX.createTransaction();
			}
			/*else if (paymentType.equals(CardType.GIFT_CERTIFICATE)) {
				transaction = PaymentType.GIFT_CERTIFICATE.createTransaction();
			}*/

			transaction.setCaptured(false);
			transaction.setCardNumber(posRequest.payment.acct);

			String exp = posRequest.payment.exp;
			if (exp != null) {
				transaction.setCardExpMonth(exp.substring(0, 2));
				transaction.setCardExpYear(exp.substring(2, 4));
			}
		}

		double tenderAmount = Double.parseDouble(posRequest.payment.pamt) / 100;
		transaction.setTenderAmount(tenderAmount);
		transaction.setTicket(ticket);

		if (tenderAmount >= ticket.getDueAmount()) {
			transaction.setAmount(ticket.getDueAmount());
		}
		else {
			transaction.setAmount(tenderAmount);
		}

		PosTransactionService transactionService = PosTransactionService.getInstance();
		try {
			final double dueAmount = ticket.getDueAmount();
			transactionService.settleTicket(ticket, transaction);
			SettleTicketProcessor.printTicket(ticket, transaction);
			SettleTicketProcessor.showTransactionCompleteMsg(dueAmount, tenderAmount, ticket, transaction);

			if (SettleTicketProcessor.waitDialog.isVisible()) {
				SettleTicketProcessor.waitDialog.setCanceled(false);
				SettleTicketProcessor.waitDialog.dispose();
				RootView.getInstance().showDefaultView();
			}

			POSDefaultInfo posDefaultInfo = new POSDefaultInfo();
			posDefaultInfo.setServer(posRequest.posDefaultInfo.server);
			posDefaultInfo.setTable(posRequest.posDefaultInfo.table);
			posDefaultInfo.setCheck(posRequest.posDefaultInfo.check);
			posDefaultInfo.setRes("1");
			posDefaultInfo.setrText("success");

			posResponse.setPosDefaultInfo(posDefaultInfo);

			return posResponse;

		} catch (Exception e) {
			Log.debug("Error:" + e);
		}
		return posResponse;
	}

	private POSResponse printCheck(POSRequest posRequest) {
		POSResponse posResponse = new POSResponse();

		POSDefaultInfo posDefaultInfo = new POSDefaultInfo();
		posDefaultInfo.setServer(posRequest.posDefaultInfo.server);
		posDefaultInfo.setTable(posRequest.posDefaultInfo.table);
		posDefaultInfo.setCheck(posRequest.posDefaultInfo.check);

		List<PrintText> printTexts = getPrintText(Integer.parseInt(posRequest.posDefaultInfo.check));

		posResponse.setPrintChecks(printTexts);
		posResponse.setPosDefaultInfo(posDefaultInfo);

		return posResponse;
	}

	private List<PrintText> getPrintText(Integer checkId) {

		Ticket ticket = TicketDAO.getInstance().loadFullTicket(checkId);

		List<PrintText> printTexts = new ArrayList<PrintText>();

		Restaurant restaurant = Application.getInstance().getRestaurant();

		printTexts.add(new PrintText(restaurant.getName(), PrintText.CENTER));
		printTexts.add(new PrintText(restaurant.getAddressLine1(), PrintText.CENTER));
		printTexts.add(new PrintText(restaurant.getTelephone(), PrintText.CENTER));

		printTexts.add(new PrintText("***Payment Receipt***", PrintText.CENTER));

		String line = "__________________________________";

		printTexts.add(new PrintText(line, PrintText.CENTER));
		printTexts.add(new PrintText("*" + ticket.getTicketType() + "*", PrintText.CENTER));
		printTexts.add(new PrintText("Terminal: " + ticket.getTerminal().getId()));
		printTexts.add(new PrintText("CHK#: " + ticket.getId()));
		printTexts.add(new PrintText("Table: " + ticket.getTableNumbers()));
		printTexts.add(new PrintText("Guests: " + ticket.getNumberOfGuests()));
		printTexts.add(new PrintText("Server: " + ticket.getOwner().getFirstName()));
		printTexts.add(new PrintText("Printed: " + new Date()));

		printTexts.add(new PrintText(line, PrintText.CENTER));
		printTexts.add(new PrintText("ITEM     QTY    SUB", PrintText.RIGHT));
		printTexts.add(new PrintText(line, PrintText.CENTER));

		if (ticket.getTicketItems() != null) {
			List<TicketItem> ticketItems = ticket.getTicketItems();
			for (TicketItem ticketItem : ticketItems) {
				printTexts.add(new PrintText(ticketItem.getName() + "   " + ticketItem.getItemCount() + "    " + ticketItem.getUnitPriceDisplay(),
						PrintText.RIGHT));
			}
		}
		printTexts.add(new PrintText(line, PrintText.CENTER));

		printTexts.add(new PrintText("Total    " + ticket.getSubtotalAmount(), PrintText.RIGHT));
		printTexts.add(new PrintText("Tax    " + ticket.getTaxAmount(), PrintText.RIGHT));

		printTexts.add(new PrintText(line, PrintText.CENTER));

		printTexts.add(new PrintText("Net Amount    " + ticket.getTotalAmount(), PrintText.RIGHT));
		printTexts.add(new PrintText("Paid Amount   " + ticket.getPaidAmount(), PrintText.RIGHT));
		printTexts.add(new PrintText("Due Amount    " + ticket.getDueAmount(), PrintText.RIGHT));

		return printTexts;
	}

}
