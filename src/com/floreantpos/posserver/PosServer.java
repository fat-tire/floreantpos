package com.floreantpos.posserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.floreantpos.PosLog;
import com.floreantpos.model.dao._RootDAO;

public class PosServer implements Runnable {
	public static final int PORT = 5656;

	public PosServer() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		ServerSocket ss = null;

		try {
			PosLog.info(getClass(), "listening on ..." + PORT);
			ss = new ServerSocket(PORT);
			listen(ss);
		} catch (Exception e) {
			PosLog.error(getClass(), e);
		} finally {
			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					// oh, well...
				}
			}
		}
	}

	static void listen(ServerSocket ss) throws Exception {
		//DataInputStream dis = null;
		//DataOutputStream dos = null;
		//String posdft = "", pamt = "", tamt = "", cardtype = "", server = "", table = "", check = "", edc = "";
		//String Card[] = { "DEBIT", "MCRD", "VISA", "DINER", "DSCVR", "AMEX", "JCB", "PL", "CASH", "GIFT" };
		// Get Table Response Samples
		// general response
		//String prepend = "<POSResponse><Ident id='";

		//String respTable = "' ttype='45'/>";

		String resp = "";
		String ids = "";
		while (true) {
			// ACCEPT Connections
			PosLog.info(PosServer.class, "Waiting For Connections....");
			Socket s = ss.accept();

			PosRequestHandler posRequestHandler = new PosRequestHandler(s);
			posRequestHandler.start();

			//			//BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			//			//BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			//			//dis = new DataInputStream(s.getInputStream());
			//			//dos = new DataOutputStream(s.getOutputStream());
			//			// Read from the socket
			//			byte[] b1 = new byte[3000];
			//			s.getInputStream().read(b1);
			//			if (b1.length <= 1)
			//				continue;
			//			// Read from Terminal
			//			String request = new String(b1).trim();
			//			PosLog.debug(getClass(),"Request From Terminal==>[" + request + "]");
			//
			//			int index = request.indexOf("<");
			//			request = request.substring(index);
			//
			//			InputSource is = new InputSource();
			//			is.setCharacterStream(new StringReader(request));
			//
			//			JAXBContext jaxbContext = JAXBContext.newInstance(POSRequest.class);
			//			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			//			POSRequest posRequest = (POSRequest) unmarshaller.unmarshal(is);
			//
			////			PosRequestHandler posRequestHandler = new PosRequestHandler(s);
			////			posRequestHandler.acceptRequest(posRequest);
			//			
			//
			//			/*User user = UserDAO.getInstance().findUserBySecretKey(posRequest.posDefaultInfo.server);
			//			List<Ticket> ticketsForUser = TicketDAO.getInstance().findOpenTicketsForUser(user);
			//
			//			POSResponse posResponse = new POSResponse();
			//
			//			Ident ident = new Ident();
			//			ident.setId(posRequest.ident.id);
			//			ident.setTermserialno(posRequest.ident.termserialno);
			//			ident.setTtype("45");
			//
			//			POSDefaultInfo posDefaultInfo = new POSDefaultInfo();
			//			posDefaultInfo.setServer(posRequest.posDefaultInfo.server);
			//			posDefaultInfo.setRes("1");
			//			posDefaultInfo.setrText("Success");
			//
			//			posResponse.setIdent(ident);
			//			posResponse.setPosDefaultInfo(posDefaultInfo);
			//
			//			Checks checks = new Checks();
			//
			//			checks.setCheckList(new ArrayList<Check>());
			//
			//			for (Ticket ticket : ticketsForUser) {
			//				List<Integer> tableNumbers = ticket.getTableNumbers();
			//				if (tableNumbers != null && tableNumbers.size() > 0) {
			//					Check chk = new Check();
			//					chk.setTableNo(String.valueOf(tableNumbers.get(0)));
			//					chk.setTableName("bar1");
			//					chk.setChkName(String.valueOf(ticket.getId()));
			//					chk.setChkNo(String.valueOf(ticket.getId()));
			//					chk.setAmt(String.valueOf(Math.round((ticket.getDueAmount() - ticket.getTaxAmount()) * 100)));
			//					chk.setTax(String.valueOf(Math.round(ticket.getTaxAmount() * 100)));
			//					checks.getCheckList().add(chk);
			//				}
			//			}
			//
			//			posResponse.setChecks(checks);
			//
			//			JAXBContext messageContext = JAXBContext.newInstance(POSResponse.class);
			//			Marshaller marshaller = messageContext.createMarshaller();
			//			final StringWriter dataWriter = new StringWriter();
			//			marshaller.marshal(posResponse, dataWriter);
			//
			//			resp = dataWriter.toString();
			//			resp = resp.replaceAll("<\\?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"\\?>", "");
			//			//PosLog.debug(getClass(),resp);
			//
			//			// /append the appropriate length of response
			//			String len = String.format("%05d", resp.length());
			//			resp = len + resp;
			//			byte[] tosend = resp.getBytes();
			//
			//			PosLog.debug(getClass(),"Reponse to Terminal===>[" + resp + "]");
			//			dos.write(tosend, 0, tosend.length);*/
			//			//dos.flush();
			//			// close the connection after 5 seconds
			//			Thread.sleep(5000);
			//			s.close();
		}
	}

	public static void main(String[] args) throws Exception {
		_RootDAO.initialize();

		/*User user = UserDAO.getInstance().findUserBySecretKey("1111");
		List<Ticket> ticketsForUser = TicketDAO.getInstance().findOpenTicketsForUser(user);

		POSResponse posResponse = new POSResponse();

		Ident ident = new Ident();
		ident.setId("345");
		ident.setTtype("45");

		POSDefaultInfo posDefaultInfo = new POSDefaultInfo();
		posDefaultInfo.setServer("1111");
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
				chk.setTableName("-");
				chk.setChkName("-");
				chk.setChkNo(String.valueOf(ticket.getId()));
				chk.setAmt(String.valueOf(Math.round((ticket.getDueAmount() - ticket.getTaxAmount()) * 100)));
				chk.setTax(String.valueOf(Math.round(ticket.getTaxAmount() * 100)));
				checks.getCheckList().add(chk);
			}
		}

		posResponse.setChecks(checks);

		JAXBContext messageContext = JAXBContext.newInstance(POSResponse.class);
		Marshaller marshaller = messageContext.createMarshaller();
		final StringWriter dataWriter = new StringWriter();
		marshaller.marshal(posResponse, dataWriter);

		String string = dataWriter.toString();
		string = string.replaceAll("<\\?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"\\?>", "");
		PosLog.debug(getClass(),string);*/
	}
}
