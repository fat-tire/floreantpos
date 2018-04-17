package com.floreantpos.dejavoo.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.floreantpos.model.PaymentStatusFilter;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
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
		System.out.println("started server");

	}

	public static void main(String[] args) throws Exception {
		DejavooProxyServer proxyServer = new DejavooProxyServer();
		proxyServer.start();
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		HttpRequestHandler target = new HttpRequestHandler(exchange);
		//target.run();
		Thread thread = new Thread(target);
		thread.start();
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

				byte[] bs = string.getBytes("UTF-8");
				exchange.sendResponseHeaders(200, bs.length);

				OutputStream outputStream = exchange.getResponseBody();
				outputStream.write(bs);
				outputStream.flush();
				outputStream.close();

				int serverId = 1;
				User user = UserDAO.getInstance().get(serverId);
				List<Ticket> ticketList = TicketDAO.getInstance().findTicketsForUser(PaymentStatusFilter.OPEN, null, user);

				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("<request>");
				stringBuilder.append("<RegisterId>9375755</RegisterId>");
				stringBuilder.append("<AuthKey>pbwB89ay72</AuthKey>");
				stringBuilder.append(String.format("<InvoiceList title=\"%s\" count=\"%s\">", "invoices", ticketList.size()));
				for (Ticket ticket : ticketList) {
					//<Invoice id="1" name="John Abrams" amount="10000" type="open" />
					//ticket.getId(), ticket.getCustomer() != null ? ticket.getCustomer().getName() : ""
					stringBuilder.append(String.format("<Invoice id=\"%s\" name=\"%s\" amount=\"%s\" type=\"%s\"/>", ticket.getId(),ticket.getCustomer().getName(),ticket.getTotalAmount(),ticket.isClosed()?"":"open"));
				}
				stringBuilder.append("</InvoiceList>");
				stringBuilder.append("</request>");

				System.out.println(stringBuilder.toString());
				URL url = new URL("http://spinpos.net:80/spin/cgi.html?TerminalTransaction=" + URLEncoder.encode(stringBuilder.toString(), "utf-8"));
				URLConnection urlConnection = url.openConnection();
				urlConnection.connect();
				InputStream stream = urlConnection.getInputStream();
				String string2 = IOUtils.toString(stream);
				while(StringUtils.isNotEmpty(string2)) {
					System.out.println(string2);
					string2 = IOUtils.toString(stream);
				}
				stream.close();
				
				url = new URL("http://spinpos.net:80/spin/cgi.html?TerminalTransaction=%3Crequest%3E%0D%0A%3CRegisterId%3E9375755%3C%2FRegisterId%3E%0D%0A%3CAuthKey%3EpbwB89ay72%3C%2FAuthKey%3E%0D%0A%3CInvoiceData%20id%3D%221%22%20name%3D%22Shadat%22%3E%0D%0A%3CAmountDue%3E9700%3C%2FAmountDue%3E%0D%0A%20%20%3CTotalAmount%3E10000%3C%2FTotalAmount%3E%0D%0A%20%20%3CGoods%20count%3D%225%22%3E%0D%0A%20%20%20%20%3CItem%20name%3D%22miso%20soup%22%20amount%3D%223000%22%20quantity%3D%221%22%20%2F%3E%0D%0A%20%20%20%20%3CItem%20name%3D%22coffee%22%20amount%3D%223600%22%20quantity%3D%222%22%20%2F%3E%0D%0A%20%20%20%20%3CItem%20name%3D%22cheeseburger%22%20amount%3D%221000%22%20quantity%3D%221%22%20%2F%3E%0D%0A%20%20%20%20%3CItem%20name%3D%22hamburger%22%20amount%3D%229000%22%20quantity%3D%221%22%20%2F%3E%0D%0A%20%20%20%20%3CItem%20name%3D%22pancake%22%20amount%3D%2215000%22%20quantity%3D%221%22%20%2F%3E%0D%0A%20%20%3C%2FGoods%3E%0D%0A%3C%2FInvoiceData%3E%0D%0A%3C%2Frequest%3E");
				urlConnection = url.openConnection();
				urlConnection.connect();
				stream = urlConnection.getInputStream();
				string2 = IOUtils.toString(stream);
				while(StringUtils.isNotEmpty(string2)) {
					System.out.println(string2);
					string2 = IOUtils.toString(stream);
				}
				stream.close();
				
				System.out.println("Execution complete!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
