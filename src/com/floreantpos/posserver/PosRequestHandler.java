package com.floreantpos.posserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.InputSource;

import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;

public class PosRequestHandler extends Thread {
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;

	private POSRequest posRequest;
	private POSResponse posResponse;

	public PosRequestHandler(Socket socket) throws Exception {
		this.socket = socket;
		posRequest = new POSRequest();
		posResponse = new POSResponse();

		writer = new PrintWriter(socket.getOutputStream());
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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

				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(request));

				JAXBContext jaxbContext = JAXBContext.newInstance(POSRequest.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				posRequest = (POSRequest) unmarshaller.unmarshal(is);

				String ttype = posRequest.ident.ttype;

				if (ttype.equals("45")) {
					if (posRequest.posDefaultInfo.table.equals("0")) {
						addAllTables();
					}
					else {
						addTable();
					}
				}
				else if (ttype.equals("46")) {
					applyPayment();
				}
				else if (ttype.equals("47")) {
					printCheck();
				}

				JAXBContext messageContext = JAXBContext.newInstance(POSResponse.class);
				Marshaller marshaller = messageContext.createMarshaller();
				StringWriter dataWriter = new StringWriter();
				marshaller.marshal(posResponse, dataWriter);

				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

				String resp = "";
				resp = dataWriter.toString();
				resp = resp.replaceAll("<\\?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"\\?>", "");

				String len = String.format("%05d", resp.length());
				resp = len + resp;
				byte[] tosend = resp.getBytes();

				System.out.println("Reponse to Terminal===>[" + resp + "]");
				dos.write(tosend, 0, tosend.length);
				dos.flush();

				writer.close();
				reader.close();
			}

		} catch (Exception e) {

		}
	}

	private void addAllTables() {
		User user = UserDAO.getInstance().findUserBySecretKey(posRequest.posDefaultInfo.server);
		List<Ticket> ticketsForUser = TicketDAO.getInstance().findOpenTicketsForUser(user);

		Ident ident = new Ident();
		ident.setId(posRequest.ident.id);
		ident.setTermserialno(posRequest.ident.termserialno);
		ident.setTtype("45");

		POSDefaultInfo posDefaultInfo = new POSDefaultInfo();
		posDefaultInfo.setServer(posRequest.posDefaultInfo.server);
		posDefaultInfo.setRes("1");
		posDefaultInfo.setrText("Success");

		posResponse.setIdent(ident);
		posResponse.setPosDefaultInfo(posDefaultInfo);

		Checks checks = new Checks();

		checks.setCheckList(new ArrayList<Check>());

		for (Ticket ticket : ticketsForUser) {
			List<Integer> tableNumbers = ticket.getTableNumbers();
			if (tableNumbers != null && tableNumbers.size() > 0) {
				Check chk = new Check();
				chk.setTableNo(String.valueOf(tableNumbers.get(0)));
				chk.setTableName("");
				chk.setChkName(String.valueOf(ticket.getId()));
				chk.setChkNo(String.valueOf(ticket.getId()));
				chk.setAmt(String.valueOf(Math.round((ticket.getDueAmount() - ticket.getTaxAmount()) * 100)));
				chk.setTax(String.valueOf(Math.round(ticket.getTaxAmount() * 100)));
				checks.getCheckList().add(chk);
			}
		}

		posResponse.setChecks(checks);

	}

	private void addTable() {
		User user = UserDAO.getInstance().findUserBySecretKey(posRequest.posDefaultInfo.server);
		List<Ticket> ticketsForUser = TicketDAO.getInstance().findOpenTicketsForUser(user);

		Ident ident = new Ident();
		ident.setId(posRequest.ident.id);
		ident.setTermserialno(posRequest.ident.termserialno);
		ident.setTtype("45");

		POSDefaultInfo posDefaultInfo = new POSDefaultInfo();
		posDefaultInfo.setServer(posRequest.posDefaultInfo.server);
		posDefaultInfo.setTable(posRequest.posDefaultInfo.table);
		posDefaultInfo.setRes("1");
		posDefaultInfo.setrText("Success");

		posResponse.setIdent(ident);
		posResponse.setPosDefaultInfo(posDefaultInfo);

		Checks checks = new Checks();

		checks.setCheckList(new ArrayList<Check>());

		for (Ticket ticket : ticketsForUser) {
			List<Integer> tableNumbers = ticket.getTableNumbers();
			if (tableNumbers != null && tableNumbers.size() > 0) {
				if (tableNumbers.contains(Integer.parseInt(posRequest.posDefaultInfo.table))) {
					Check chk = new Check();
					chk.setTableNo(String.valueOf(posDefaultInfo.table));
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

	}

	private void applyPayment() {

		Ticket ticket = TicketDAO.getInstance().loadFullTicket(Integer.parseInt(posRequest.posDefaultInfo.check));

		Ident ident = new Ident();
		ident.setId(posRequest.ident.id);
		ident.setTermserialno(posRequest.ident.termserialno);
		ident.setTtype("46");

		POSDefaultInfo posDefaultInfo = new POSDefaultInfo();
		posDefaultInfo.setTable(posRequest.posDefaultInfo.table);
		posDefaultInfo.setCheck(posRequest.posDefaultInfo.check);
		posDefaultInfo.setServer(posRequest.posDefaultInfo.server);
		posDefaultInfo.setRes("1");
		posDefaultInfo.setrText("Success");

		posResponse.setIdent(ident);
		posResponse.setPosDefaultInfo(posDefaultInfo);

		PrintText line1 = new PrintText("---Restaurant Name---");
		PrintText line2 = new PrintText("---Address---");
		PrintText line3 = new PrintText("---Cell---");
		//PrintText line4 = new PrintText("-------" + ticket.getId() + "-------");

		posResponse.getPrintText().add(line1);
		posResponse.getPrintText().add(line2);
		posResponse.getPrintText().add(line3);
		//posResponse.getPrintText().add(line4);

	}

	private void printCheck() {
		User user = UserDAO.getInstance().findUserBySecretKey(posRequest.posDefaultInfo.server);
		List<Ticket> ticketsForUser = TicketDAO.getInstance().findOpenTicketsForUser(user);

		Ident ident = new Ident();
		ident.setId(posRequest.ident.id);
		ident.setTermserialno(posRequest.ident.termserialno);
		ident.setTtype("47");

		POSDefaultInfo posDefaultInfo = new POSDefaultInfo();
		posDefaultInfo.setTable(posRequest.posDefaultInfo.table);
		posDefaultInfo.setCheck(posRequest.posDefaultInfo.check);
		posDefaultInfo.setServer(posRequest.posDefaultInfo.server);
		posDefaultInfo.setRes("1");
		posDefaultInfo.setrText("Success");

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

	}
}
