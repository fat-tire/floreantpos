package com.floreantpos.posserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.InputSource;

import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;

public class PosServer implements Runnable {
	public static final int PORT = 5656;

	public PosServer() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		ServerSocket ss = null;

		try {
			System.out.println("listening on ..." + PORT);
			ss = new ServerSocket(PORT);
			listen(ss);
		} catch (Exception e) {
			e.printStackTrace();
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
		DataInputStream dis = null;
		DataOutputStream dos = null;
		String posdft = "", pamt = "", tamt = "", cardtype = "", server = "", table = "", check = "", edc = "";
		String Card[] = { "DEBIT", "MCRD", "VISA", "DINER", "DSCVR", "AMEX", "JCB", "PL", "CASH", "GIFT" };
		// Get Table Response Samples
		// general response
		String prepend = "<POSResponse><Ident id='";

		String respTable = "' ttype='45'/>";

		String resp = "";
		String ids = "";
		while (true) {
			// ACCEPT Connections
			System.out.println("Waiting For Connections....");
			Socket s = ss.accept();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
			// Read from the socket
			byte[] b1 = new byte[3000];
			s.getInputStream().read(b1);
			if (b1.length <= 1)
				continue;
			// Read from Terminal
			String request = new String(b1).trim();
			System.out.println("Request From Terminal==>[" + request + "]");

			int index = request.indexOf("<");
			request = request.substring(index);

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(request));

			JAXBContext jaxbContext = JAXBContext.newInstance(POSRequest.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			POSRequest posRequest = (POSRequest) unmarshaller.unmarshal(is);

			User user = UserDAO.getInstance().findUserBySecretKey(posRequest.posDefaultInfo.server);
			List<Ticket> ticketsForUser = TicketDAO.getInstance().findOpenTicketsForUser(user);

			POSResponse posResponse = new POSResponse();

			Ident ident = new Ident();
			ident.setId("345");
			ident.setTtype("45");

			POSDefaultInfo posDefaultInfo = new POSDefaultInfo();
			posDefaultInfo.setServer(user.getAutoId().toString());
			posDefaultInfo.setRes("1");
			posDefaultInfo.setrText("Success");

			posResponse.setIdent(ident);
			posResponse.setPosDefaultInfo(posDefaultInfo);

			Checks checks = new Checks();

			checks.setCheckList(new ArrayList<Check>());

			List<ShopTable> tables = ShopTableDAO.getInstance().findAll();

			if (tables == null) {
				return;
			}
			for (Ticket ticket : ticketsForUser) {
				for (ShopTable shopTable : tables) {
					if (ticket.getTableNumbers().contains(shopTable.getId())) {
						Check chk = new Check();
						chk.setTableNo(String.valueOf(shopTable.getId()));
						chk.setTableName("-");
						chk.setChkName("-");
						chk.setChkNo(String.valueOf(ticket.getId()));
						chk.setAmt(String.valueOf(Math.round(ticket.getDueAmount())));
						chk.setTax(String.valueOf(Math.round(ticket.getTaxAmount())));
						checks.getCheckList().add(chk);
					}
				}
			}

			posResponse.setChecks(checks);

			JAXBContext messageContext = JAXBContext.newInstance(POSResponse.class);
			Marshaller marshaller = messageContext.createMarshaller();
			final StringWriter dataWriter = new StringWriter();
			marshaller.marshal(posResponse, dataWriter);
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			resp = dataWriter.toString();

			// /append the appropriate length of response
			String len = String.format("%05d", resp.length());
			resp = len + resp;
			byte[] tosend = resp.getBytes();

			System.out.println("Reponse to Terminal===>[" + resp + "]");
			dos.write(tosend, 0, tosend.length);
			dos.flush();
			// close the connection after 5 seconds
			Thread.sleep(60000);
			s.close();
		}
	}
}
