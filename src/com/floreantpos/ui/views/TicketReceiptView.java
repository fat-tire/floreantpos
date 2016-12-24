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
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewerPanel;

import com.floreantpos.Messages;

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
