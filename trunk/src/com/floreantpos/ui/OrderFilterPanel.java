package com.floreantpos.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXCollapsiblePane;

import com.floreantpos.ITicketList;
import com.floreantpos.Messages;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.OrderTypeFilter;
import com.floreantpos.model.PaymentStatusFilter;
import com.floreantpos.swing.POSToggleButton;


public class OrderFilterPanel extends JXCollapsiblePane {
	private ITicketList ticketList;
	private TicketListView ticketLists;
	
	
	public OrderFilterPanel(ITicketList ticketList) {
		this.ticketList = ticketList;
		this.ticketLists = (TicketListView) ticketList;
		
		setCollapsed(true);
		getContentPane().setLayout(new MigLayout("fill", "fill, grow", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		createPaymentStatusFilterPanel();
		createOrderTypeFilterPanel();
	}


	private void createPaymentStatusFilterPanel() {
		POSToggleButton btnFilterByOpenStatus = new POSToggleButton(PaymentStatusFilter.OPEN.toString());
		POSToggleButton btnFilterByPaidStatus = new POSToggleButton(PaymentStatusFilter.PAID.toString());
		POSToggleButton btnFilterByUnPaidStatus = new POSToggleButton(PaymentStatusFilter.CLOSED.toString());
		
		ButtonGroup paymentGroup = new ButtonGroup();
		paymentGroup.add(btnFilterByOpenStatus);
		paymentGroup.add(btnFilterByPaidStatus);
		paymentGroup.add(btnFilterByUnPaidStatus);
		
		PaymentStatusFilter paymentStatusFilter = TerminalConfig.getPaymentStatusFilter();
		switch(paymentStatusFilter) {
			case OPEN:
				btnFilterByOpenStatus.setSelected(true);
				break;
				
			case PAID:
				btnFilterByPaidStatus.setSelected(true);
				break;
				
			case CLOSED:
				btnFilterByUnPaidStatus.setSelected(true);
				break;
				
		}
		
		ActionListener psFilterHandler = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String actionCommand = e.getActionCommand();
				String filter = actionCommand.replaceAll("\\s", "_"); //$NON-NLS-1$ //$NON-NLS-2$
				TerminalConfig.setPaymentStatusFilter(filter);

				ticketList.updateTicketList();
				ticketLists.updateButtonStatus();
				
			}
		};
		
		btnFilterByOpenStatus.addActionListener(psFilterHandler);
		btnFilterByPaidStatus.addActionListener(psFilterHandler);
		btnFilterByUnPaidStatus.addActionListener(psFilterHandler);
		
		JPanel filterByPaymentStatusPanel = new JPanel(new MigLayout("", "fill, grow", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		filterByPaymentStatusPanel.setBorder(new TitledBorder(Messages.getString("SwitchboardView.3"))); //$NON-NLS-1$
		filterByPaymentStatusPanel.add(btnFilterByOpenStatus);
		filterByPaymentStatusPanel.add(btnFilterByPaidStatus);
		filterByPaymentStatusPanel.add(btnFilterByUnPaidStatus);
		
		getContentPane().add(filterByPaymentStatusPanel);
	}
	
	private void createOrderTypeFilterPanel() {
		POSToggleButton btnFilterByOrderTypeALL = new POSToggleButton(OrderTypeFilter.ALL.toString());
		POSToggleButton btnFilterByDineIn = new POSToggleButton(OrderTypeFilter.DINE_IN.toString());
		POSToggleButton btnFilterByTakeOut = new POSToggleButton(OrderTypeFilter.TAKE_OUT.toString());
		POSToggleButton btnFilterByPickup = new POSToggleButton(OrderTypeFilter.PICKUP.toString());
		POSToggleButton btnFilterByHomeDeli = new POSToggleButton(OrderTypeFilter.HOME_DELIVERY.toString());
		POSToggleButton btnFilterByDriveThru = new POSToggleButton(OrderTypeFilter.DRIVE_THRU.toString());
		POSToggleButton btnFilterByBarTab = new POSToggleButton(OrderTypeFilter.BAR_TAB.toString());
		
		ButtonGroup orderTypeGroup = new ButtonGroup();
		orderTypeGroup.add(btnFilterByOrderTypeALL);
		orderTypeGroup.add(btnFilterByDineIn);
		orderTypeGroup.add(btnFilterByTakeOut);
		orderTypeGroup.add(btnFilterByPickup);
		orderTypeGroup.add(btnFilterByHomeDeli);
		orderTypeGroup.add(btnFilterByDriveThru);
		orderTypeGroup.add(btnFilterByBarTab);

		OrderTypeFilter orderTypeFilter = TerminalConfig.getOrderTypeFilter();
		switch(orderTypeFilter) {
			case ALL:
				btnFilterByOrderTypeALL.setSelected(true);
				break;
				
			case DINE_IN:
				btnFilterByDineIn.setSelected(true);
				break;
				
			case TAKE_OUT:
				btnFilterByTakeOut.setSelected(true);
				break;
				
			case PICKUP:
				btnFilterByPickup.setSelected(true);
				break;
				
			case HOME_DELIVERY:
				btnFilterByHomeDeli.setSelected(true);
				break;
				
			case DRIVE_THRU:
				btnFilterByDriveThru.setSelected(true);
				break;
				
			case BAR_TAB:
				btnFilterByBarTab.setSelected(true);
				break;
		}
		
		JPanel filterByOrderPanel = new JPanel(new MigLayout("", "fill, grow", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		filterByOrderPanel.setBorder(new TitledBorder(Messages.getString("SwitchboardView.4"))); //$NON-NLS-1$
		filterByOrderPanel.add(btnFilterByOrderTypeALL);
		filterByOrderPanel.add(btnFilterByDineIn);
		filterByOrderPanel.add(btnFilterByTakeOut);
		filterByOrderPanel.add(btnFilterByPickup);
		filterByOrderPanel.add(btnFilterByHomeDeli);
		filterByOrderPanel.add(btnFilterByDriveThru);
		filterByOrderPanel.add(btnFilterByBarTab);
		
		getContentPane().add(filterByOrderPanel);
		
		
		ActionListener orderTypeFilterHandler = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String actionCommand = e.getActionCommand();
				String filter = actionCommand.replaceAll("\\s", "_"); //$NON-NLS-1$ //$NON-NLS-2$
				TerminalConfig.setOrderTypeFilter(filter);
		
			
				//ticketLists.setCurrentRowIndexZero();
				ticketList.updateTicketList();
				ticketLists.updateButtonStatus();
			}
		};
		

		btnFilterByOrderTypeALL.addActionListener(orderTypeFilterHandler);
		btnFilterByDineIn.addActionListener(orderTypeFilterHandler);
		btnFilterByTakeOut.addActionListener(orderTypeFilterHandler);
		btnFilterByPickup.addActionListener(orderTypeFilterHandler);
		btnFilterByHomeDeli.addActionListener(orderTypeFilterHandler);
		btnFilterByDriveThru.addActionListener(orderTypeFilterHandler);
		btnFilterByBarTab.addActionListener(orderTypeFilterHandler);
	}
}
