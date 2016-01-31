package com.floreantpos.posserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PosRequestHandler extends Thread {
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	
	public PosRequestHandler(Socket socket) throws Exception {
		this.socket = socket;
		
		writer = new PrintWriter(socket.getOutputStream());
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	@Override
	public void run() {
		try {
			String line = reader.readLine();
			System.out.println(line);
			
			writer.write(line);
			writer.flush();
			writer.close();
			
			reader.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
