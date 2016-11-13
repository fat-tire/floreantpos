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
package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.report.CustomPaymentReportView;

public class CustomPaymentReportAction extends AbstractAction {

	public CustomPaymentReportAction() {
		super(com.floreantpos.POSConstants.CUSTOM_PAYMENT_REPORT);
	}

	public CustomPaymentReportAction(String name) {
		super(name);
	}

	public CustomPaymentReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = com.floreantpos.util.POSUtil.getBackOfficeWindow();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		CustomPaymentReportView reportView = null;
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.CUSTOM_PAYMENT_REPORT);
		if (index == -1) {
			reportView = new CustomPaymentReportView();
			tabbedPane.addTab(com.floreantpos.POSConstants.CUSTOM_PAYMENT_REPORT, reportView);
		}
		else {
			reportView = (CustomPaymentReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
