package com.floreantpos.posserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class PosRequestHandler extends Thread {
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;

	private POSRequest posRequest;
	private POSResponse posResponse;

	public PosRequestHandler(Socket socket) throws Exception {
		this.socket = socket;
		posResponse = new POSResponse();

		writer = new PrintWriter(socket.getOutputStream());
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	@Override
	public void run() {
		try {

			try {
				sendResponse();
			} catch (Exception e) {

			}
			String line = reader.readLine();
			//System.out.println(line);

			writer.write(line);
			writer.flush();
			writer.close();

			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void acceptRequest(POSRequest posRequest) {
		this.posRequest = posRequest;
		String tType = posRequest.ident.ttype;

		if (tType.equals("45")) {
			posResponse = TransactionType.GET_TABLES.createPOSResponse(posRequest);
		}
		else if (tType.equals("46")) {
			posResponse = TransactionType.APPLY_PAYMENT.createPOSResponse(posRequest);
		}
		else if (tType.equals("47")) {
			posResponse = TransactionType.PRINT_CHECK.createPOSResponse(posRequest);
		}
	}

	private void sendResponse() throws Exception {
		String resp = "";
		DataOutputStream dos = null;

		dos = new DataOutputStream(socket.getOutputStream());

		JAXBContext messageContext = JAXBContext.newInstance(POSResponse.class);
		Marshaller marshaller = messageContext.createMarshaller();
		StringWriter dataWriter = new StringWriter();
		marshaller.marshal(posResponse, dataWriter);

		resp = dataWriter.toString();
		resp = resp.replaceAll("<\\?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"\\?>", "");

		String len = String.format("%05d", resp.length());
		resp = len + resp;
		byte[] tosend = resp.getBytes();

		System.out.println("Reponse to Terminal===>[" + resp + "]");
		dos.write(tosend, 0, tosend.length);
		dataWriter.close();
		dos.flush();
	}
}
