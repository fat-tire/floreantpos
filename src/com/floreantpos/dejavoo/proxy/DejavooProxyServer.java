package com.floreantpos.dejavoo.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.InputSource;

import com.floreantpos.POSConstants;
import com.floreantpos.config.AppConfig;
import com.floreantpos.model.PaymentStatusFilter;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.model.dao._RootDAO;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class DejavooProxyServer implements HttpHandler {
	com.sun.net.httpserver.HttpServer server;
	private String authKey;
	private String registerId;
	private final String SERVER_NUM = "/TranRequest/server_num";
	private final String GET_LIST = "/TranRequest/get_list";
	private final String TABLE_NUM = "/TranRequest/table_num";
	private final String INVOICE_NUM = "/TranRequest/invoice_num";

	public DejavooProxyServer() {
		_RootDAO.initialize();

		authKey = AppConfig.getString("Dejavoo.AUTH_KEY");
		registerId = AppConfig.getString("Dejavoo.REGISTER_ID");
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
				InputStream inputStream = exchange.getRequestBody();
				String requestString = IOUtils.toString(inputStream);
				inputStream.close();

				byte[] bs = requestString.getBytes("UTF-8");
				exchange.sendResponseHeaders(200, bs.length);
				OutputStream outputStream = exchange.getResponseBody();
				outputStream.write(bs);
				outputStream.flush();
				outputStream.close();

				processRequest(requestString);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private String getXpathValue(String xpath, String input) {
		try {
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath newXPath = xPathFactory.newXPath();
			return newXPath.evaluate(xpath, new InputSource(new StringReader(input)));
		} catch (Exception e) {
			return "";
		}
	}

	private void sendTicketServer() throws Exception {
		int serverId = 1;
		User user = UserDAO.getInstance().get(serverId);
		List<Ticket> ticketList = TicketDAO.getInstance().findTicketsForUser(PaymentStatusFilter.OPEN, POSConstants.ALL, user);
		doProcess(ticketList);
	}

	private void sendTicketList() throws Exception {
		List<Ticket> ticketList = TicketDAO.getInstance().findOpenTickets();

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<request>");
		stringBuilder.append("<RegisterId>" + registerId + "</RegisterId>");
		stringBuilder.append("<AuthKey>" + authKey + "</AuthKey>");
		stringBuilder.append(String.format("<InvoiceList title=\"%s\" count=\"%s\">", "invoices", ticketList.size()));
		for (Ticket ticket : ticketList) {
			String invoice = "<Invoice id=\"%s\" name=\"%s\" amount=\"%s\" type=\"%s\"/>";
			String invoiceFormat = String.format(invoice, ticket.getId(), ticket.getOwner().getFirstName(), ticket.getTotalAmount() * 100,
					ticket.isClosed() ? "closed" : "open");
			stringBuilder.append(invoiceFormat);
		}
		stringBuilder.append("</InvoiceList>");
		stringBuilder.append("</request>");

		System.out.println("sending: " + stringBuilder.toString());
		sendData(stringBuilder.toString());
	}

	private void sendTicketTable() {

	}

	private void sendTicketDetail(String ticketId) throws Exception {
		Ticket ticket = TicketDAO.getInstance().get(Integer.parseInt(ticketId));
		StringBuilder builder = new StringBuilder();
		builder.append("<request>");
		builder.append("<RegisterId>" + registerId + "</RegisterId>");
		builder.append("<AuthKey>" + authKey + "</AuthKey>");
		builder.append(String.format("<InvoiceData id=\"%s\" name=\"%s\">", ticket.getId(), ticket.getOwner().getFirstName()));
		builder.append("<AmountDue>" + ticket.getTotalAmount() * 100 + "</AmountDue>");
		builder.append("<TotalAmount>" + ticket.getDueAmount() * 100 + "</TotalAmount>");
		builder.append("<Goods count=\"" + ticket.getTicketItems().size() + "\">");
		List<TicketItem> ticketItems = ticket.getTicketItems();
		for (TicketItem ticketItem : ticketItems) {
			builder.append(String.format("<Item name=\"%s\" amount=\"%s\" quantity=\"%s\" />", ticketItem.getName(), ticketItem.getTotalAmount() * 100,
					ticketItem.getItemCount()));
		}
		builder.append("</Goods>");
		builder.append("</InvoiceData>");
		builder.append("</request>");
		System.out.println("sending ticket detail:"+builder.toString());
		
		sendData(builder.toString());
	}

	private void doProcess(List<Ticket> ticketList) throws Exception {
		//		URL url = new URL("http://spinpos.net:80/spin/cgi.html?TerminalTransaction=" + URLEncoder.encode(stringBuilder.toString(), "utf-8"));
		//		URLConnection urlConnection = url.openConnection();
		//		urlConnection.setDoOutput(true);
		//		urlConnection.connect();
		//		InputStream stream = urlConnection.getInputStream();
		//		OutputStream outputStream = urlConnection.getOutputStream();
		//
		//		String string2 = IOUtils.toString(stream);
		//
		//		while (StringUtils.isNotEmpty(string2)) {
		//			System.out.println(string2);
		//			XPathFactory xPathFactory = XPathFactory.newInstance();
		//			XPath newXPath = xPathFactory.newXPath();
		//			try {
		//				String id = newXPath.evaluate("/xmp/response/ID", new InputSource(new StringReader(string2)));
		//				if ("-1".equals(id)) {
		//					stream.close();
		//					return;
		//				}
		//
		//				Ticket ticket = TicketDAO.getInstance().get(Integer.parseInt(id));
		//				StringBuilder builder = new StringBuilder();
		//				builder.append("<request>");
		//				builder.append("<RegisterId>" + registerId + "</RegisterId>");
		//				builder.append("<AuthKey>" + authKey + "</AuthKey>");
		//				builder.append(String.format("<InvoiceData id=\"%s\" name=\"%s\">", ticket.getId(), ticket.getOwner().getFirstName()));
		//				builder.append("<AmountDue>" + ticket.getTotalAmount() * 100 + "</AmountDue>");
		//				builder.append("<TotalAmount>" + ticket.getDueAmount() * 100 + "</TotalAmount>");
		//				builder.append("<Goods count=\"" + ticket.getTicketItems().size() + "\">");
		//				List<TicketItem> ticketItems = ticket.getTicketItems();
		//				for (TicketItem ticketItem : ticketItems) {
		//					builder.append(String.format("<Item name=\"%s\" amount=\"%s\" quantity=\"%s\" />", ticketItem.getName(), ticketItem.getTotalAmount() * 100,
		//							ticketItem.getItemCount()));
		//				}
		//				builder.append("</Goods>");
		//				builder.append("</InvoiceData>");
		//				builder.append("</request>");
		//				System.out.println(builder.toString());
		//
		//				outputStream.write(builder.toString().getBytes());
		//
		//				//url = new URL("http://spinpos.net:80/spin/cgi.html?TerminalTransaction=" + URLEncoder.encode(builder.toString(), "utf-8"));
		//				//urlConnection = url.openConnection();
		//				//urlConnection.connect();
		//				//stream = urlConnection.getInputStream();
		//				//string2 = IOUtils.toString(stream);
		//				//while (StringUtils.isNotEmpty(string2)) {
		//				//	System.out.println(string2);
		//				//	string2 = IOUtils.toString(stream);
		//				//}
		//
		//				//						StringBuilder transactionSB = new StringBuilder();
		//				//						transactionSB.append("<xmp>");
		//				//						transactionSB.append("<response>");
		//				//						transactionSB.append("<RegisterId>"+registerId+"</RegisterId>");
		//				//						transactionSB.append("<Message>Successful</Message>");
		//				//						transactionSB.append(String.format("<InvoiceReport id=\"%s\">",ticket.getId()));
		//				//						transactionSB.append("<xmp>");
		//				//						transactionSB.append("<response>");
		//				//						
		//				//						transactionSB.append("</response>");
		//				//						transactionSB.append("</xmp>");
		//				//						transactionSB.append("</InvoiceReport>");
		//				//						transactionSB.append("</response>");
		//				//						transactionSB.append("</xmp>");
		//
		//				stream.close();
		//
		//				System.out.println("Execution complete!");
		//			} catch (XPathExpressionException e) {
		//				e.printStackTrace();
		//			}
		//		}
		//		stream.close();
	}

	private void sendData(String data) throws Exception {
		URL url = new URL("http://spinpos.net:80/spin/cgi.html?TerminalTransaction=" + URLEncoder.encode(data, "utf-8"));
		URLConnection urlConnection = url.openConnection();
		urlConnection.connect();
		InputStream inputStream = urlConnection.getInputStream();

		String requestString = IOUtils.toString(inputStream);
		inputStream.close();
		System.out.println("data sent complete!");

		processRequest(requestString);

		//}
	}

	private void processRequest(String requestString) throws Exception {
		System.out.println("request received: " + requestString);
		if (requestString.startsWith("InvoiceData")) {
			String xpathValue = getXpathValue("/InvoiceData/@status", requestString);
			if ("cancel".equalsIgnoreCase(xpathValue)) {
				return;
			}
		}
		
		if (requestString.startsWith("<xmp")) {
			processSpinResponse(requestString);
			return;
		}

		String xpathValue = getXpathValue(SERVER_NUM, requestString);
		if (StringUtils.isNotEmpty(xpathValue)) {
			sendTicketServer();
		}
		else if (StringUtils.isEmpty(xpathValue)) {
			xpathValue = getXpathValue(GET_LIST, requestString);
			if ("true".equals(xpathValue)) {
				sendTicketList();
			}
		}
		else if (StringUtils.isEmpty(xpathValue)) {
			xpathValue = getXpathValue(TABLE_NUM, requestString);
			sendTicketTable();
		}
		else {
			//xpathValue = getXpathValue(INVOICE_NUM, requestString);
			//sendTicketInvoice();
		}
	}

	private void processSpinResponse(String requestString) throws Exception {
		String xpathValue = getXpathValue("/xmp/response/Message", requestString);
		if ("Error".equalsIgnoreCase(xpathValue) || "ok".equalsIgnoreCase(xpathValue)) {
			System.out.println("Processing terminated.");
		}
		else  {
			sendTicketDetail("2");
		}
	}
}
