package com.floreantpos.ui.views;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import net.sf.jasperreports.engine.JasperPrint;

public class TicketReceiptView extends JPanel {
	net.sf.jasperreports.swing.JRViewer jrViewer;

	public TicketReceiptView(JasperPrint jasperPrint) {
		setLayout(new BorderLayout());
		
		jrViewer = new net.sf.jasperreports.swing.JRViewer(jasperPrint);
		jrViewer.setToolbarVisible(false);
		jrViewer.setStatusbarVisible(false);
		
		add(jrViewer);
	}
	
	public JPanel getReportPanel() {
		return jrViewer.getReportPanel();
	}
}
