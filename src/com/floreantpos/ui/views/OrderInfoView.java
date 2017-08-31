/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperPrint;

import com.floreantpos.main.Application;
import com.floreantpos.model.GuestCheckPrint;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.GuestCheckPrintDAO;
import com.floreantpos.report.ReceiptPrintService;
import com.floreantpos.report.TicketPrintProperties;
import com.floreantpos.swing.PosScrollPane;

public class OrderInfoView extends JPanel {

	private List<Ticket> tickets;
	private JPanel reportPanel;

	public OrderInfoView(List<Ticket> tickets) throws Exception {

		this.tickets = tickets;

		createUI();
	}

	public void createUI() throws Exception {

		reportPanel = new JPanel(new MigLayout("wrap 1, ax 50%", "", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		PosScrollPane scrollPane = new PosScrollPane(reportPanel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);

		createReport();

		setLayout(new BorderLayout());
		add(scrollPane);
	}

	public void createReport() throws Exception {

		for (int i = 0; i < tickets.size(); i++) {
			Ticket ticket = (Ticket) tickets.get(i);

			TicketPrintProperties printProperties = new TicketPrintProperties("*** ORDER " + ticket.getId() + " ***", false, true, true); //$NON-NLS-1$ //$NON-NLS-2$
			HashMap map = ReceiptPrintService.populateTicketProperties(ticket, printProperties, null);
			map.put(JRParameter.IS_IGNORE_PAGINATION, true);
			JasperPrint jasperPrint = ReceiptPrintService.createPrint(ticket, map, null);
			TicketReceiptView receiptView = new TicketReceiptView(jasperPrint);
			reportPanel.add(receiptView.getReportPanel());
		}
	}

	public void print() throws Exception {
		for (Iterator iter = tickets.iterator(); iter.hasNext();) {
			Ticket ticket = (Ticket) iter.next();
			ReceiptPrintService.printTicket(ticket);
		}
	}

	private void doCreateGuestCheck(Ticket ticket) {
		GuestCheckPrint guestCheckPrint = new GuestCheckPrint();
		guestCheckPrint.setTicketId(ticket.getId());
		guestCheckPrint.setPrintTime(new Date());
		guestCheckPrint.setTicketTotal(ticket.getTotalAmount());
		guestCheckPrint.setUser(Application.getCurrentUser());

		String strTableNumbers = "";
		List<Integer> tableNumbers = ticket.getTableNumbers();
		if (tableNumbers != null) {
			for (Integer integer : tableNumbers) {
				strTableNumbers += integer + " ";
			}
		}
		guestCheckPrint.setTableNo(strTableNumbers);

		GuestCheckPrintDAO.getInstance().saveOrUpdate(guestCheckPrint);
	}

	public void printCopy(String copyType) throws Exception {
		for (Iterator iter = tickets.iterator(); iter.hasNext();) {
			Ticket ticket = (Ticket) iter.next();
			doCreateGuestCheck(ticket);
			ReceiptPrintService.printTicket(ticket, copyType);
		}
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public JPanel getReportPanel() {
		return reportPanel;
	}
}
