package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.floreantpos.Messages;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewerPanel;

public class TicketReceiptView extends JPanel {
	net.sf.jasperreports.swing.JRViewer jrViewer;

	public TicketReceiptView(JasperPrint jasperPrint) {
		setLayout(new BorderLayout());
		
		jrViewer = new net.sf.jasperreports.swing.JRViewer(jasperPrint);
		
		add(jrViewer);
	}
	
	public Component getReportPanel() {
		Component[] components = jrViewer.getComponents();
		for (Component component : components) {
			if(component instanceof JRViewerPanel) {
				Component[] components2 = ((JRViewerPanel) component).getComponents();
				for (Component component2 : components2) {
					if(component2 instanceof JScrollPane) {
						JScrollPane scrollPane = (JScrollPane) component2;
						return scrollPane.getViewport().getView();
					}
				}
			}
		}
		
		throw new RuntimeException(Messages.getString("TicketReceiptView.0")); //$NON-NLS-1$
	}
}
