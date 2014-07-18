package com.floreantpos.ui.views;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import net.sf.jasperreports.engine.JasperPrint;

public class TicketReceiptView extends JPanel {


	public TicketReceiptView(JasperPrint jasperPrint) {
		setLayout(new BorderLayout());
		
		net.sf.jasperreports.swing.JRViewer jrViewer = new net.sf.jasperreports.swing.JRViewer(jasperPrint);
		jrViewer.setToolbarVisible(false);
		jrViewer.setStatusbarVisible(false);
		
		add(jrViewer);
	}
	
}
