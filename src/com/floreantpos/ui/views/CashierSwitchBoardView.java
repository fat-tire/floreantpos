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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.views.order.ViewPanel;
import com.floreantpos.util.PosGuiUtil;

public class CashierSwitchBoardView extends ViewPanel implements ActionListener {
	public final static String VIEW_NAME = "csbv"; //$NON-NLS-1$
	
	private PosButton btnNewOrder = new PosButton(POSConstants.NEW_ORDER_BUTTON_TEXT);
	private PosButton btnEditOrder = new PosButton(POSConstants.EDIT_TICKET_BUTTON_TEXT);
	private PosButton btnSettleOrder = new PosButton(POSConstants.SETTLE_TICKET_BUTTON_TEXT);
	
	public CashierSwitchBoardView() {
		setLayout(new MigLayout("align 50% 50%")); //$NON-NLS-1$
		
		JPanel orderPanel = new JPanel(new MigLayout());
		orderPanel.setBorder(PosGuiUtil.createTitledBorder(POSConstants.CashierSwitchBoardView_LABEL_ORDER));
		
		orderPanel.add(btnNewOrder, "w 160!, h 160!"); //$NON-NLS-1$
		orderPanel.add(btnEditOrder, "w 160!, h 160!"); //$NON-NLS-1$
		orderPanel.add(btnSettleOrder, "w 160!, h 160!"); //$NON-NLS-1$
		
		btnNewOrder.addActionListener(this);
		btnEditOrder.addActionListener(this);
		btnSettleOrder.addActionListener(this);
		
		add(orderPanel);
	}

	@Override
	public String getViewName() {
		return VIEW_NAME;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if(source == btnNewOrder) {
			//FIXME: ORDER TYPE
			//OrderUtil.createNewTakeOutOrder(new OrderType(1, "TAKE OUT"));//fix
		}
		else if(source == btnEditOrder) {
			
		}
		else if(source == btnSettleOrder) {
			
		}
	}
}
