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
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperPrint;

import com.floreantpos.PosLog;
import com.floreantpos.main.Application;
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

	public final static String VIEW_NAME = "TICKET_DETAIL"; //$NON-NLS-1$

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
			
			JPanel reportPanel = new JPanel(new MigLayout("wrap 1, ax 50%","","")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			PosScrollPane scrollPane = new PosScrollPane(reportPanel);
			scrollPane.getVerticalScrollBar().setUnitIncrement(20);

			for (Iterator iter = tickets.iterator(); iter.hasNext();) {
				Ticket ticket = (Ticket) iter.next();
				
				TicketPrintProperties printProperties = new TicketPrintProperties("*** ORDER " + ticket.getId() + " ***", false, true, true); //$NON-NLS-1$ //$NON-NLS-2$
				HashMap map = ReceiptPrintService.populateTicketProperties(ticket, printProperties, null);
				map.put(JRParameter.IS_IGNORE_PAGINATION, true);
				JasperPrint jasperPrint = ReceiptPrintService.createPrint(ticket, map, null);

				TicketReceiptView receiptView = new TicketReceiptView(jasperPrint);
				reportPanel.add(receiptView.getReportPanel());
			}
			
			topPanel.add(scrollPane, BorderLayout.CENTER);
			
			revalidate();
			repaint();
		} catch (Exception e) {
			PosLog.error(getClass(), e);
			POSMessageDialog.showError(Application.getPosWindow(), e.getMessage(), e);
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
