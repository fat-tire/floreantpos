package com.floreantpos.ui.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.model.OrderType;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.views.order.ViewPanel;
import com.floreantpos.util.OrderUtil;
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
			OrderUtil.createNewTakeOutOrder(OrderType.TAKE_OUT);
		}
		else if(source == btnEditOrder) {
			
		}
		else if(source == btnSettleOrder) {
			
		}
	}
}
