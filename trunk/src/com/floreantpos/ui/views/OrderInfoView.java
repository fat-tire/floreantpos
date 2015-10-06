package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JasperPrint;

import com.floreantpos.model.Ticket;
import com.floreantpos.report.ReceiptPrintService;
import com.floreantpos.report.TicketPrintProperties;
import com.floreantpos.swing.PosScrollPane;

public class OrderInfoView extends JPanel {
	private List<Ticket> tickets;

	public OrderInfoView(List<Ticket> tickets) throws Exception {
		super();
		this.tickets = tickets;
		
		createUI();
	}
	
	private void createUI() throws Exception {
		JPanel reportPanel = new JPanel(new MigLayout("wrap 1, ax 50%", "", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		PosScrollPane scrollPane = new PosScrollPane(reportPanel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);

		for (int i = 0; i < tickets.size(); i++) {
			Ticket ticket = (Ticket) tickets.get(i);
			
			TicketPrintProperties printProperties = new TicketPrintProperties("*** ORDER " + ticket.getId() + " ***", false, true, true); //$NON-NLS-1$ //$NON-NLS-2$
			HashMap map = ReceiptPrintService.populateTicketProperties(ticket, printProperties, null);
			JasperPrint jasperPrint = ReceiptPrintService.createPrint(ticket, map, null);

			TicketReceiptView receiptView = new TicketReceiptView(jasperPrint);
			reportPanel.add(receiptView.getReportPanel());
		}
		
		setLayout(new BorderLayout());
		add(scrollPane);
	}

	public void print() throws Exception {
		for (Iterator iter = tickets.iterator(); iter.hasNext();) {
			Ticket ticket = (Ticket) iter.next();
			
			ReceiptPrintService.printTicket(ticket);
		}
	}
}
