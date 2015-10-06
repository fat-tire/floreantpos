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
import com.floreantpos.main.Application;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.views.order.RootView;
import com.floreantpos.ui.views.order.ViewPanel;

public class SwitchboardOtherFunctionsView extends ViewPanel {
	public static final String VIEW_NAME="afv"; //$NON-NLS-1$
	private static SwitchboardOtherFunctionsView instance;
	
	private PosAction[] actions = {
			new DrawerAssignmentAction(),
			new DrawerPullAction(),
			new DrawerBleedAction(),
			new DrawerKickAction(),
			new PayoutAction(),
			new ServerTipsAction(),
			new ShowTransactionsAuthorizationsAction(),
			new ShowKitchenDisplayAction(),
			new ManageTableLayoutAction(),
			new ShowOnlineTicketManagementAction()
	};
	
//	private PosButton btnDrawerPull = new PosButton(new DrawerPullAction());
//	
//	private PosButton btnAuthorize = new PosButton(new ShowTransactionsAuthorizationsAction());
//	private PosButton btnKitchenDisplay = new PosButton(new ShowKitchenDisplayAction());
//	private PosButton btnPayout = new PosButton(new PayoutAction());
//	private PosButton btnTableManage = new PosButton(new ManageTableLayoutAction());
//	private PosButton btnOnlineTickets = new PosButton(POSConstants.ONLINE_TICKET_BUTTON_TEXT);
	
	public SwitchboardOtherFunctionsView() {
		setLayout(new BorderLayout(5, 5));
		PosButton btnBack = new PosButton(Messages.getString("SwitchboardOtherFunctionsView.1")); //$NON-NLS-1$
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RootView.getInstance().showView(SwitchboardView.VIEW_NAME);
			}
		});
		add(btnBack, BorderLayout.SOUTH);
		
		JPanel contentPanel = new JPanel(new MigLayout("align 50% 50%, wrap 5")); //$NON-NLS-1$
		
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
		
//		add(btnAuthorize, "w 150!, h 150!");
//		add(btnKitchenDisplay, "w 150!, h 150!");
//		add(btnPayout, "w 150!, h 150!");
//		add(btnTableManage, "w 150!, h 150!");
//		add(btnOnlineTickets, "w 150!, h 150!");
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

}
