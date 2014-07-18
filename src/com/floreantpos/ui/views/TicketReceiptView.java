package com.floreantpos.ui.views;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import net.sf.jasperreports.engine.JasperPrint;

import com.floreantpos.model.Ticket;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.report.TicketPrintProperties;

public class TicketReceiptView extends JPanel {

	private Ticket ticket;
	private double paidAmount;

	public TicketReceiptView() {

	}
	
	public TicketReceiptView(Ticket ticket) {
		this.ticket = ticket;
	}

	public TicketReceiptView(Ticket ticket, double paidAmount) {
		super();
		this.ticket = ticket;
		this.paidAmount = paidAmount;
	}
	
	public void createUI() {
		try {
			setLayout(new BorderLayout());
			TicketPrintProperties printProperties = new TicketPrintProperties("*** KITCHEN ***", false, false, false);
			JasperPrint jasperPrint = JReportPrintService.createPrint(ticket, printProperties);
			net.sf.jasperreports.swing.JRViewer jrViewer = new net.sf.jasperreports.swing.JRViewer(jasperPrint);
			jrViewer.setToolbarVisible(false);
			jrViewer.setStatusbarVisible(false);
			add(jrViewer);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}
}
