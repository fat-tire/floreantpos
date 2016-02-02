package com.floreantpos.posserver;

import java.io.DataOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.InputSource;

import com.floreantpos.model.PaymentType;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.services.PosTransactionService;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.payment.SettleTicketDialog;

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
				if (b1.length <= 1)
					continue;

				String request = new String(b1).trim();
				System.out.println("Request From Terminal==>[" + request + "]");

				int index = request.indexOf("<");
				request = request.substring(index);

				POSRequest posRequest = createRequest(request);
				POSResponse posResponse = null;
				String ttype = posRequest.ident.ttype;

				if (ttype.equals(Ident.GET_TABLES)) {
					if (posRequest.posDefaultInfo.table.equals("0")) {
						posResponse = addAllTables(posRequest);
					}
					else {
						posResponse = addTable(posRequest);
					}
				}
				else if (ttype.equals(Ident.APPLY_PAYMENT)) {
					posResponse = applyPayment(posRequest);
				}
				else if (ttype.equals(Ident.PRINT_CHECK)) {
					posResponse = printCheck(posRequest);
				}

				String resp = createResponse(posResponse);
				System.out.println("Reponse to Terminal===>[" + resp + "]");

				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				byte[] tosend = resp.getBytes();
				dos.write(tosend, 0, tosend.length);
				dos.flush();
				dos.close();

				Thread.sleep(5000);
			}

		} catch (Exception e) {
			System.out.println("Error:" + e);

		} finally {
			try {
				socket.close();
			} catch (Exception e) {
				System.out.println("Error:" + e);
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

	private String createResponse(POSResponse posResponse) throws Exception {
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

		Ident ident = new Ident();
		ident.setId(posRequest.ident.id);
		ident.setTtype(posRequest.ident.ttype);

		POSDefaultInfo posDefaultInfo = new POSDefaultInfo();
		posDefaultInfo.setServer(posRequest.posDefaultInfo.server);
		posDefaultInfo.setTable(posRequest.posDefaultInfo.table);
		posDefaultInfo.setCheck(posRequest.posDefaultInfo.check);
		posDefaultInfo.setRes("1");
		posDefaultInfo.setrText("success");

		posResponse.setIdent(ident);
		posResponse.setPosDefaultInfo(posDefaultInfo);

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

		return posResponse;
	}

	private POSResponse addTable(POSRequest posRequest) {

		POSResponse posResponse = new POSResponse();

		User user = UserDAO.getInstance().findUserBySecretKey(posRequest.posDefaultInfo.server);
		List<Ticket> ticketsForUser = TicketDAO.getInstance().findOpenTicketsForUser(user);

		Ident ident = new Ident();
		ident.setId(posRequest.ident.id);
		ident.setTtype(posRequest.ident.ttype);

		POSDefaultInfo posDefaultInfo = new POSDefaultInfo();
		posDefaultInfo.setServer(posRequest.posDefaultInfo.server);
		posDefaultInfo.setTable(posRequest.posDefaultInfo.table);
		posDefaultInfo.setRes("1");
		posDefaultInfo.setrText("success");

		posResponse.setIdent(ident);
		posResponse.setPosDefaultInfo(posDefaultInfo);

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

		return posResponse;
	}

	private POSResponse applyPayment(POSRequest posRequest) {

		Ticket ticket = TicketDAO.getInstance().loadFullTicket(Integer.parseInt(posRequest.posDefaultInfo.check));

		POSResponse posResponse = new POSResponse();
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
				transaction.setCardExpiryMonth(exp.substring(0, 2));
				transaction.setCardExpiryYear(exp.substring(2, 4));
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
			transactionService.settleTicket(ticket, transaction);
			SettleTicketDialog.printTicket(ticket, transaction);

			Ident ident = new Ident();
			ident.setId(posRequest.ident.id);
			ident.setTtype(posRequest.ident.ttype);

			POSDefaultInfo posDefaultInfo = new POSDefaultInfo();
			posDefaultInfo.setTable(posRequest.posDefaultInfo.table);
			posDefaultInfo.setCheck(posRequest.posDefaultInfo.check);
			posDefaultInfo.setServer(posRequest.posDefaultInfo.server);
			posDefaultInfo.setRes("1");
			posDefaultInfo.setrText("success");

			posResponse.setIdent(ident);
			posResponse.setPosDefaultInfo(posDefaultInfo);

		} catch (Exception e) {
			POSMessageDialog.showError("Error" + e);
		}

		return posResponse;
	}

	private POSResponse printCheck(POSRequest posRequest) {

		POSResponse posResponse = new POSResponse();

		User user = UserDAO.getInstance().findUserBySecretKey(posRequest.posDefaultInfo.server);
		List<Ticket> ticketsForUser = TicketDAO.getInstance().findOpenTicketsForUser(user);

		Ident ident = new Ident();
		ident.setId(posRequest.ident.id);
		ident.setTermserialno(posRequest.ident.termserialno);
		ident.setTtype(posRequest.ident.ttype);

		POSDefaultInfo posDefaultInfo = new POSDefaultInfo();
		posDefaultInfo.setTable(posRequest.posDefaultInfo.table);
		posDefaultInfo.setCheck(posRequest.posDefaultInfo.check);
		posDefaultInfo.setServer(posRequest.posDefaultInfo.server);
		posDefaultInfo.setRes("1");
		posDefaultInfo.setrText("success");

		for (Ticket ticket : ticketsForUser) {
			List<Integer> tableNumbers = ticket.getTableNumbers();
			if (tableNumbers != null && tableNumbers.size() > 0) {
				if (tableNumbers.contains(Integer.parseInt(posRequest.posDefaultInfo.table))) {
					posDefaultInfo.setCheck(ticket.getId().toString());
					break;
				}
			}
		}

		PrintText line1 = new PrintText("---Restaurant Name---");
		PrintText line2 = new PrintText("---Address---");
		PrintText line3 = new PrintText("---Cell---");
		//PrintText line4 = new PrintText("-------" + ticket.getId() + "-------");

		posResponse.getPrintText().add(line1);
		posResponse.getPrintText().add(line2);
		posResponse.getPrintText().add(line3);
		//posResponse.getPrintText().add(line4);

		posResponse.setIdent(ident);
		posResponse.setPosDefaultInfo(posDefaultInfo);

		return posResponse;

	}
}
