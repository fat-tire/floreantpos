package com.floreantpos.posserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.InputSource;

import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
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
		//Get Table Response Samples
		//general response
		String prepend = "<POSResponse><Ident id='";

		String respTable = "' ttype='45'/>";

		String resp = "";
		String ids = "";
		while (true) {
			//ACCEPT Connections
			System.out.println("Waiting For Connections....");
			Socket s = ss.accept();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
			//Read from the socket
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
			
			int serverId = Integer.parseInt(posRequest.posDefaultInfo.server);
			User user = UserDAO.getInstance().get(serverId);
			List<Ticket> ticketsForUser = TicketDAO.getInstance().findOpenTicketsForUser(user);
			
			for (Iterator iterator = ticketsForUser.iterator(); iterator.hasNext();) {
				Ticket ticket = (Ticket) iterator.next();
			}
			
			POSDefaultInfo posDefaultInfo=new POSDefaultInfo(); 
			
			JAXBContext jaxbContext2 = JAXBContext.newInstance(POSResponse.class);
			Marshaller marshaller = jaxbContext2.createMarshaller();
			//Object marshal = marshaller.marshal();
			//System.out.println(marshal);
			

		}
	}
}
