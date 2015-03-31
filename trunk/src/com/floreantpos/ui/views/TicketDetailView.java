/*
 * TicketInfoView.java
 *
 * Created on August 13, 2006, 11:17 PM
 */

package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JasperPrint;

import com.floreantpos.model.Ticket;
import com.floreantpos.report.ReceiptPrintService;
import com.floreantpos.report.TicketPrintProperties;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.ui.dialog.POSMessageDialog;

/**
 *
 * @author  MShahriar
 */
public class TicketDetailView extends JPanel {

	public final static String VIEW_NAME = "TICKET_DETAIL";

	private JPanel topPanel;

	private List<Ticket> tickets;

	/** Creates new form TicketInfoView */
	public TicketDetailView() {

		setLayout(new BorderLayout(5, 5));
		setBorder(new EmptyBorder(15, 15, 15, 15));

		topPanel = new JPanel(new GridLayout());
		add(topPanel, BorderLayout.CENTER);

		setOpaque(false);
	}

	public void clearView() {
		topPanel.removeAll();
	}

	public void updateView() {
		try {
			clearView();
			
			List<Ticket> tickets = getTickets();

			int totalTicket = tickets.size();
			
			if (totalTicket <= 0) {
				return;
			}
			
			JPanel reportPanel = new JPanel(new MigLayout("wrap 1, ax 50%","",""));
			PosScrollPane scrollPane = new PosScrollPane(reportPanel);
			scrollPane.getVerticalScrollBar().setUnitIncrement(20);

			for (Iterator iter = tickets.iterator(); iter.hasNext();) {
				Ticket ticket = (Ticket) iter.next();
				
				TicketPrintProperties printProperties = new TicketPrintProperties("*** ORDER " + ticket.getId() + " ***", false, true, true);
				HashMap map = ReceiptPrintService.populateTicketProperties(ticket, printProperties, null);
				JasperPrint jasperPrint = ReceiptPrintService.createPrint(ticket, map, null);

				TicketReceiptView receiptView = new TicketReceiptView(jasperPrint);
				reportPanel.add(receiptView.getReportPanel());
			}
			
			topPanel.add(scrollPane, BorderLayout.CENTER);
			
			revalidate();
			repaint();
		} catch (Exception e) {
			e.printStackTrace();
			POSMessageDialog.showError(e.getMessage(), e);
		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables

	// End of variables declaration//GEN-END:variables


	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;

		updateView();
	}

	public void setTicket(Ticket ticket) {
		tickets = new ArrayList<Ticket>(1);
		tickets.add(ticket);

		updateView();
	}

	public void cleanup() {
		tickets = null;
	}
}
