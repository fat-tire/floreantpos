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
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom2.Document;
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
				System.out.println("request method: " + exchange.getRequestMethod());
				InputStream inputStream = exchange.getRequestBody();
				String string = IOUtils.toString(inputStream);
				System.out.println("request body: " + string);
				inputStream.close();
				
				if (string.startsWith("InvoiceData")) {
					String xpathValue = getXpathValue("/InvoiceData/@status", string);
					if ("cancel".equalsIgnoreCase(xpathValue)) {
						return;
					}
				}

				byte[] bs = string.getBytes("UTF-8");
				exchange.sendResponseHeaders(200, bs.length);

				OutputStream outputStream = exchange.getResponseBody();
				outputStream.write(bs);
				outputStream.flush();
				outputStream.close();

				int serverId = 1;
				User user = UserDAO.getInstance().get(serverId);
				List<Ticket> ticketList = TicketDAO.getInstance().findTicketsForUser(PaymentStatusFilter.OPEN, POSConstants.ALL, user);

				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("<request>");
				stringBuilder.append("<RegisterId>" + registerId + "</RegisterId>");
				stringBuilder.append("<AuthKey>" + authKey + "</AuthKey>");
				stringBuilder.append(String.format("<InvoiceList title=\"%s\" count=\"%s\">", "invoices", ticketList.size()));
				for (Ticket ticket : ticketList) {
					//<Invoice id="1" name="John Abrams" amount="10000" type="open" />
					//ticket.getId(), ticket.getCustomer() != null ? ticket.getCustomer().getName() : ""
					stringBuilder.append(String.format("<Invoice id=\"%s\" name=\"%s\" amount=\"%s\" type=\"%s\"/>", ticket.getId(),
							ticket.getOwner().getFirstName(), ticket.getTotalAmount(), ticket.isClosed() ? "" : "open"));
				}
				stringBuilder.append("</InvoiceList>");
				stringBuilder.append("</request>");

				System.out.println(stringBuilder.toString());
				URL url = new URL("http://spinpos.net:80/spin/cgi.html?TerminalTransaction=" + URLEncoder.encode(stringBuilder.toString(), "utf-8"));
				URLConnection urlConnection = url.openConnection();
				urlConnection.connect();
				InputStream stream = urlConnection.getInputStream();
				String string2 = IOUtils.toString(stream);
				while (StringUtils.isNotEmpty(string2)) {
					System.out.println(string2);
					XPathFactory xPathFactory = XPathFactory.newInstance();
					XPath newXPath = xPathFactory.newXPath();
					try {
						String id = newXPath.evaluate("/xmp/response/ID", new InputSource(new StringReader(string2)));
						if ("-1".equals(id)) {
							stream.close();
							return;
						}
						
						Ticket ticket = TicketDAO.getInstance().get(Integer.parseInt(id));
						StringBuilder builder = new StringBuilder();
						builder.append("<request>");
						builder.append("<RegisterId>" + registerId + "</RegisterId>");
						builder.append("<AuthKey>" + authKey + "</AuthKey>");
						builder.append(String.format("<InvoiceData id=\"%s\" name=\"%s\">", ticket.getId(), ticket.getOwner().getFirstName()));
						builder.append("<AmountDue>" + ticket.getTotalAmount() + "</AmountDue>");
						builder.append("<TotalAmount>" + ticket.getDueAmount() + "</TotalAmount>");
						builder.append("<Goods count=\"" + ticket.getTicketItems().size() + "\">");
						List<TicketItem> ticketItems = ticket.getTicketItems();
						for (TicketItem ticketItem : ticketItems) {
							builder.append(String.format("<Item name=\"%s\" amount=\"%s\" quantity=\"%s\" />", ticketItem.getName(), ticketItem.getTotalAmount(), ticketItem.getItemQuantity()));
						}
						builder.append("</Goods>");
						builder.append("</InvoiceData>");
						builder.append("</request>");
						System.out.println(builder.toString());
						url = new URL(
								"http://spinpos.net:80/spin/cgi.html?TerminalTransaction=" + URLEncoder.encode(builder.toString(), "utf-8"));
						urlConnection = url.openConnection();
						urlConnection.connect();
						stream = urlConnection.getInputStream();
						string2 = IOUtils.toString(stream);
						while (StringUtils.isNotEmpty(string2)) {
							System.out.println(string2);
							string2 = IOUtils.toString(stream);
						}
						stream.close();

						System.out.println("Execution complete!");
					} catch (XPathExpressionException e) {
						e.printStackTrace();
					}
				}
				stream.close();

				
			} catch (IOException e) {
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

}
