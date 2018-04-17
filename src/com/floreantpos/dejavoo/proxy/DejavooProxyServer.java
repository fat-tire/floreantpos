package com.floreantpos.dejavoo.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;

import com.floreantpos.model.dao._RootDAO;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class DejavooProxyServer implements HttpHandler {
	com.sun.net.httpserver.HttpServer server;
	
	public DejavooProxyServer() {
		_RootDAO.initialize();
	}
	
	public void start() throws Exception {
		InetSocketAddress inetAddress = new InetSocketAddress("0.0.0.0", 8000);
		server = HttpServer.create(inetAddress, 10);
		server.createContext("/", this);
		server.start();
	}

	public static void main(String[] args) throws Exception {
		DejavooProxyServer proxyServer = new DejavooProxyServer();
		proxyServer.start();
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		HttpRequestHandler target = new HttpRequestHandler(exchange);
		target.run();
		//Thread thread = new Thread(target);
		//thread.start();
	}
	
	class HttpRequestHandler implements Runnable {
		HttpExchange exchange;
		
		public HttpRequestHandler(HttpExchange exchange) {
			this.exchange = exchange;
		}

		@Override
		public void run() {
			try {
				System.out.println("request method: " + exchange.getRequestMethod());
				InputStream inputStream = exchange.getRequestBody();
				String string = IOUtils.toString(inputStream);
				System.out.println("request body: " + string);
				inputStream.close();
				
				OutputStream outputStream = exchange.getResponseBody();
				outputStream.write("Send".getBytes());
				outputStream.flush();
				outputStream.close();
				
				URL url = new URL("http://spinpos.net:80/spin/cgi.html?TerminalTransaction=%3Crequest%3E%3CRegisterId%3E9375755%3C%2FRegisterId%3E%3CAuthKey%3EpbwB89ay72%3C%2FAuthKey%3E%3CInvoiceList%20title%3D%22Test%20Invoices%22%20count%3D%227%22%3E%3CInvoice%20id%3D%221%22%20name%3D%22John%20Abrams%22%20amount%3D%2210000%22%20type%3D%22open%22%20%2F%3E%0A%3C%2FInvoiceList%3E%3C%2Frequest%3E");
				URLConnection urlConnection = url.openConnection();
				urlConnection.connect();
				InputStream stream = urlConnection.getInputStream();
				stream.close();
				
				System.out.println("done");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}

}
