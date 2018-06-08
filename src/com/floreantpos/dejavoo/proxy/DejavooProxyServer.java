package com.floreantpos.dejavoo.proxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilterReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.xml.utils.XMLChar;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.floreantpos.POSConstants;
import com.floreantpos.config.AppConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.PaymentStatusFilter;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.services.PosTransactionService;
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
	private Map<String, DejavooTerminal> map = new HashMap<>();

	private DecimalFormat numberFormat = new DecimalFormat("0.00");

	public DejavooProxyServer() throws Exception {
		Application application = Application.getInstance();
		application.initializeSystemHeadless();
		readTpnFromXmlFile();
	}

	public void start() throws Exception {
		InetSocketAddress inetAddress = new InetSocketAddress("0.0.0.0", 8000);
		server = HttpServer.create(inetAddress, 10);
		server.createContext("/", this);
		server.start();
		System.out.println("Started Server");
	}

	public static void main(String[] args) throws Exception {
		DejavooProxyServer proxyServer = new DejavooProxyServer();
		proxyServer.start();
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		HttpRequestHandler target = new HttpRequestHandler(exchange);
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
				Reader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
				InvalidXmlCharacterFilter filter = new InvalidXmlCharacterFilter(reader);
				String requestString = IOUtils.toString(filter);
				inputStream.close();
				reader.close();

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

	private void sendTicketsByServer(String serverId) throws Exception {
		User user = UserDAO.getInstance().get(Integer.parseInt(serverId));
		List<Ticket> ticketList = TicketDAO.getInstance().findTicketsForUser(PaymentStatusFilter.OPEN, POSConstants.ALL, user);
		StringBuilder stringBuilder = createInvoiceList(ticketList);

		System.out.println("sending: " + stringBuilder.toString());
		sendData(stringBuilder.toString());
	}

	private void sendTicketList() throws Exception {
		List<Ticket> ticketList = TicketDAO.getInstance().findOpenTickets();
		StringBuilder stringBuilder = createInvoiceList(ticketList);

		System.out.println("sending: " + stringBuilder.toString());
		sendData(stringBuilder.toString());
	}

	private StringBuilder createInvoiceList(List<Ticket> ticketList) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<request>");
		stringBuilder.append("<RegisterId>" + registerId + "</RegisterId>");
		stringBuilder.append("<AuthKey>" + authKey + "</AuthKey>");
		stringBuilder.append(String.format("<InvoiceList title=\"%s\" count=\"%s\">", "invoices", ticketList.size()));
		for (Ticket ticket : ticketList) {
			String invoice = "<Invoice id=\"%s\" name=\"%s\" amount=\"%s\" type=\"%s\"/>";
			String name = ticket.getCustomer() == null ? ticket.getOwner().getFirstName() : ticket.getCustomer().getName();
			String invoiceFormat = String.format(invoice, ticket.getId(), name, numberFormat.format(ticket.getDueAmount() * 100),
					ticket.isClosed() ? "closed" : "open");
			stringBuilder.append(invoiceFormat);
		}
		stringBuilder.append("</InvoiceList>");
		stringBuilder.append("</request>");
		return stringBuilder;
	}

	private void sendTicketsByTable(String tableNum) throws Exception {
		List<Ticket> ticketList = TicketDAO.getInstance().findTicketsByTableNum(Integer.parseInt(tableNum));

		StringBuilder stringBuilder = createInvoiceList(ticketList);

		System.out.println("sending: " + stringBuilder.toString());
		sendData(stringBuilder.toString());

	}

	private void sendTicketDetail(String ticketId) throws Exception {
		Ticket ticket = TicketDAO.getInstance().get(Integer.parseInt(ticketId));
		StringBuilder builder = new StringBuilder();
		builder.append("<request>");
		builder.append("<RegisterId>" + registerId + "</RegisterId>");
		builder.append("<AuthKey>" + authKey + "</AuthKey>");
		builder.append(String.format("<InvoiceData id=\"%s\" name=\"%s\">", ticket.getId(), ticket.getOwner().getFirstName()));
		builder.append("<AmountDue>" + numberFormat.format(ticket.getDueAmount() * 100) + "</AmountDue>");
		builder.append("<TotalAmount>" + numberFormat.format(ticket.getTotalAmount() * 100) + "</TotalAmount>");
		builder.append("<TaxAmount>" + numberFormat.format(ticket.getTaxAmount() * 100) + "</TaxAmount>");
		builder.append("<Goods count=\"" + ticket.getTicketItems().size() + "\">");
		List<TicketItem> ticketItems = ticket.getTicketItems();
		for (TicketItem ticketItem : ticketItems) {
			builder.append(String.format("<Item name=\"%s\" amount=\"%s\" quantity=\"%s\" />", ticketItem.getName(),
					numberFormat.format(ticketItem.getTotalAmount() * 100), ticketItem.getItemCount()));
		}
		builder.append("</Goods>");
		//@formatter:off
		Set<PosTransaction> transactions = ticket.getTransactions();
		int transactionSize = ticket.getTransactions().size();
		if (transactions != null) {
			builder.append(String.format("<Payments count=\"%s\">", transactionSize));
			for (PosTransaction posTransaction : transactions) {
				String paymentType = posTransaction.getPaymentType();
				if (paymentType != null && paymentType.equalsIgnoreCase("credit card")) {
					paymentType = "Credit";
				}
				builder.append(String.format("<Payment " + "refId=\"%s\" " + "name=\"%s\" " + "amount=\"%s\" " + "tip=\"%s\" " + "type=\"%s\" />",
						//+ "acctLast4=\"%s\"/>", 
						posTransaction.getId(), paymentType, numberFormat.format(posTransaction.getAmount() * 100), numberFormat.format(posTransaction.getTipsAmount() * 100),
						posTransaction.getTipsAmount() > 0 ? "closed" : "open"));
			}
			builder.append("</Payments>");
		}
		//@formatter:off
		builder.append("</InvoiceData>");
		builder.append("</request>");
		System.out.println("sending ticket detail:" + builder.toString());

		sendData(builder.toString());
	}

	class InvalidXmlCharacterFilter extends FilterReader {
		protected InvalidXmlCharacterFilter(Reader in) {
			super(in);
		}

		@Override
		public int read(char[] cbuf, int off, int len) throws IOException {
			int read = super.read(cbuf, off, len);
			if (read == -1)
				return read;

			for (int i = off; i < off + read; i++) {
				if (!XMLChar.isValid(cbuf[i]))
					cbuf[i] = '?';
			}
			return read;
		}
	}

	private void sendData(String data) throws Exception {
		URL url = new URL("http://spinpos.net:80/spin/cgi.html?TerminalTransaction=" + URLEncoder.encode(data, "utf-8"));
		URLConnection urlConnection = url.openConnection();
		urlConnection.connect();
		InputStream inputStream = urlConnection.getInputStream();

		Reader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
		InvalidXmlCharacterFilter filter = new InvalidXmlCharacterFilter(reader);

		String requestString = IOUtils.toString(filter);
		reader.close();
		System.out.println("data sent complete!");

		processRequest(requestString);
	}

	private void processRequest(String requestString) throws Exception {
		System.out.println("request received: " + requestString);
		
		adjustRegisterIdAndAuthKey(requestString);
		
		System.out.println();
		if (requestString.startsWith("<InvoiceData")) {
			String status = getXpathValue("/InvoiceData/@status", requestString);
			if ("cancel".equalsIgnoreCase(status)) {
				return;
			}
			if ("ok".equalsIgnoreCase(status)) {
				String trans = getXpathValue("/InvoiceData/trans/@transType", requestString);
				if (StringUtils.isNotEmpty(trans)) {
					processPayment(requestString);
					return;
				}
			}
		}

		if (requestString.startsWith("<xmp")) {
			processSpinResponse(requestString);
			return;
		}
		String xpathValue = getXpathValue(GET_LIST, requestString);
		if ("true".equals(xpathValue)) {
			sendTicketList();
			return;
		}
		xpathValue = getXpathValue(INVOICE_NUM, requestString);
		if (StringUtils.isNotEmpty(xpathValue)) {
			sendTicketDetail(xpathValue);
			return;
		}
		xpathValue = getXpathValue(TABLE_NUM, requestString);
		if (StringUtils.isNotEmpty(xpathValue)) {
			sendTicketsByTable(xpathValue);
			return;
		}
		xpathValue = getXpathValue(SERVER_NUM, requestString);
		if (StringUtils.isNotEmpty(xpathValue)) {
			sendTicketsByServer(xpathValue);
			return;
		}
	}

	private void adjustRegisterIdAndAuthKey(String requestString) {
		String tpn = getXpathValue("/TranRequest/tpn", requestString);
		if(StringUtils.isNotEmpty(tpn)) {
			DejavooTerminal terminal = map.get(tpn);
			if(terminal != null) {
			  registerId = terminal.getRegId();
			  authKey = terminal.getAuthKey();
			}
		}
	}
	
	private void readTpnFromXmlFile() throws Exception {
		try {
			URL resource = getClass().getResource("/tpnlist.xml");
			if (resource == null) {
				String message = "tpnlist.xml file not found! Using authKey and regId from this system.";
				System.out.println(message);
				authKey = AppConfig.getString("Dejavoo.AUTH_KEY");
				registerId = AppConfig.getString("Dejavoo.REGISTER_ID");
				return;
			}
			File xmlFile = new File(resource.getFile());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.parse(xmlFile);
			document.getDocumentElement().normalize();
		
			NodeList nodeList = document.getElementsByTagName("tpn");
			for (int temp = 0; temp < nodeList.getLength(); temp++) {
				Node node = nodeList.item(temp);
				if (node.getNodeType() == Node.ELEMENT_NODE) {

					Element element = (Element) node;
					String tpnId = element.getAttribute("id");
					String regId = element.getElementsByTagName("registerId").item(0).getTextContent();
					String authKey = element.getElementsByTagName("authkey").item(0).getTextContent();
					
					if (tpnId != null) {
						DejavooTerminal terminal = new DejavooTerminal();
						terminal.setTpn(tpnId);
						terminal.setRegId(regId);
						terminal.setAuthKey(authKey);
						map.put(tpnId, terminal);
					}
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}catch(NullPointerException e) {
			e.printStackTrace();
		}
	}

	private void processPayment(String requestString) throws Exception {
		String payType = getXpathValue("/InvoiceData/trans/@paymType", requestString);
		String batchN = getXpathValue("/InvoiceData/trans/@batchNum", requestString);
		String invN = getXpathValue("/InvoiceData/trans/@invNum", requestString);
		String trans_ref_id = getXpathValue("/InvoiceData/trans/@refId", requestString);
		String amountString = getXpathValue("/InvoiceData/trans/@amount", requestString);
		String tipsString = getXpathValue("/InvoiceData/trans/@tip", requestString);

		double amount = Double.parseDouble(amountString) / 100.0;
		double tips = 0;
		Ticket ticket = TicketDAO.getInstance().get(Integer.parseInt(invN));
		if (StringUtils.isNotEmpty(tipsString)) {
			tips = Double.parseDouble(tipsString) / 100.0;
			amount = amount + tips;
			ticket.setGratuityAmount(tips);
			ticket.calculatePrice();
		}
		PosTransaction posTransaction = PaymentType.CREDIT_CARD.createTransaction();

		if ("Debit".equals(payType)) {
			posTransaction = PaymentType.DEBIT_CARD.createTransaction();
		}
		posTransaction.setTicket(ticket);
		posTransaction.setAmount(amount);
		posTransaction.setTipsAmount(tips);
		posTransaction.setCardTransactionId(trans_ref_id);
		posTransaction.addProperty("batchNo", batchN);
		PosTransactionService.getInstance().settleTicket(ticket, posTransaction);
	}

	private void processSpinResponse(String requestString) throws Exception {
		String xpathValue = getXpathValue("/xmp/response/Message", requestString);
		if ("Error".equalsIgnoreCase(xpathValue) || "ok".equalsIgnoreCase(xpathValue)) {
			System.out.println("Processing terminated.");
		}
		else {
			String ticketId = getXpathValue("/xmp/response/ID", requestString);
			if (StringUtils.isEmpty(ticketId) || "-1".equals(ticketId)) {
				return;
			}
			sendTicketDetail(ticketId);
		}
	}
}
