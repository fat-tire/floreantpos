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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.actions.DrawerAssignmentAction;
import com.floreantpos.actions.DrawerBleedAction;
import com.floreantpos.actions.DrawerKickAction;
import com.floreantpos.actions.DrawerPullAction;
import com.floreantpos.actions.ManageTableLayoutAction;
import com.floreantpos.actions.PayoutAction;
import com.floreantpos.actions.PosAction;
import com.floreantpos.actions.ServerTipsAction;
import com.floreantpos.actions.ShowKitchenDisplayAction;
import com.floreantpos.actions.ShowOnlineTicketManagementAction;
import com.floreantpos.actions.ShowTransactionsAuthorizationsAction;
import com.floreantpos.actions.SwithboardViewAction;
import com.floreantpos.main.Application;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.views.order.RootView;
import com.floreantpos.ui.views.order.ViewPanel;

public class SwitchboardOtherFunctionsView extends ViewPanel {
	public static final String VIEW_NAME="afv"; //$NON-NLS-1$
	private static SwitchboardOtherFunctionsView instance;
	
	private JPanel contentPanel;
	
	public SwitchboardOtherFunctionsView() {
		setLayout(new BorderLayout(5, 5));
		PosButton btnBack = new PosButton(Messages.getString("SwitchboardOtherFunctionsView.1")); //$NON-NLS-1$
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RootView.getInstance().showDefaultView();
			}
		});
		add(btnBack, BorderLayout.SOUTH);
		
		contentPanel = new JPanel(new MigLayout("align 50% 50%, wrap 6"));
		
		PosAction[] actions = {
				new DrawerAssignmentAction(),
				new DrawerPullAction(),
				new DrawerBleedAction(),
				new DrawerKickAction(),
				new PayoutAction(),
				new ServerTipsAction(),
				new ShowTransactionsAuthorizationsAction(),
				new ShowKitchenDisplayAction(),
				new SwithboardViewAction(),
				new ManageTableLayoutAction(),
				new ShowOnlineTicketManagementAction()
		};
		
		for (PosAction action : actions) {
			if(action instanceof DrawerAssignmentAction) {
				if(!Application.getInstance().getTerminal().isHasCashDrawer()) {
					continue;
				}
			}
			
			PosButton button = new PosButton(action);
			contentPanel.add(button, "w 150!, h 150!"); //$NON-NLS-1$
		}
		
		add(contentPanel);
	}
	
	public static SwitchboardOtherFunctionsView getInstance() {
		if(instance == null) {
			instance = new SwitchboardOtherFunctionsView();
		}
		
		return instance;
	}
	
	@Override
	public String getViewName() {
		return VIEW_NAME;
	}

	public JPanel getContentPanel() {
		return contentPanel;
	}
}
