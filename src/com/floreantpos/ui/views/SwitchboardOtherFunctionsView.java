package com.floreantpos.ui.views;


import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.actions.ManageTableLayoutAction;
import com.floreantpos.actions.PayoutAction;
import com.floreantpos.actions.ShowKitchenDisplayAction;
import com.floreantpos.actions.ShowTransactionsAuthorizationsAction;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.views.order.ViewPanel;

public class SwitchboardOtherFunctionsView extends ViewPanel {
	public static final String VIEW_NAME="afv";
	private static SwitchboardOtherFunctionsView instance;
	
	private PosButton btnAuthorize = new PosButton(new ShowTransactionsAuthorizationsAction());
	private PosButton btnKitchenDisplay = new PosButton(new ShowKitchenDisplayAction());
	private PosButton btnPayout = new PosButton(new PayoutAction());
	private PosButton btnTableManage = new PosButton(new ManageTableLayoutAction());
	private PosButton btnOnlineTickets = new PosButton(POSConstants.ONLINE_TICKET_BUTTON_TEXT);
	
	public SwitchboardOtherFunctionsView() {
		setLayout(new MigLayout("align 50% 50%, wrap 4"));
		
		add(btnAuthorize, "w 150!, h 150!");
		add(btnKitchenDisplay, "w 150!, h 150!");
		add(btnPayout, "w 150!, h 150!");
		add(btnTableManage, "w 150!, h 150!");
		add(btnOnlineTickets, "w 150!, h 150!");
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
