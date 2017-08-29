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
package com.floreantpos.ui.views.payment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.PosLog;
import com.floreantpos.actions.GroupSettleTicketAction;
import com.floreantpos.actions.SettleTicketAction;
import com.floreantpos.actions.SplitTicketAction;
import com.floreantpos.extension.OrderServiceFactory;
import com.floreantpos.main.Application;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.ReceiptPrintService;
import com.floreantpos.services.TicketService;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.OrderInfoDialog;
import com.floreantpos.ui.views.OrderInfoView;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.ui.views.order.RootView;
import com.floreantpos.util.CurrencyUtil;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.POSUtil;
import com.floreantpos.util.TicketAlreadyExistsException;

public class SplitedTicketSelectionDialog extends POSDialog {
	private List<Ticket> splitTickets;
	private JPanel splitTicketsPanel;
	private PosButton btnPay;
	private PosButton btnPayAll;
	private PosButton btnOrderInfo;
	private String action;
	private PosButton btnPrint;
	private PosButton btnPrintAll;
	private boolean confirmSpliting;
	private List<ShopTable> selectedTables;
	private OrderType orderType;
	private boolean createNewTicket;

	public SplitedTicketSelectionDialog(List<Ticket> tickets) {
		super(Application.getPosWindow(), POSConstants.TICKETS.toUpperCase());
		this.splitTickets = tickets;
		setLayout(new BorderLayout());

		JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
		splitTicketsPanel = new JPanel(new MigLayout("wrap 4", "sg", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		splitTicketsPanel.setBorder(new TitledBorder(POSConstants.TICKETS.toUpperCase().replace(".", ""))); //$NON-NLS-1$
		JScrollPane scrollPane = new JScrollPane(splitTicketsPanel);
		scrollPane.setBorder(null);
		centerPanel.add(scrollPane, BorderLayout.CENTER);

		add(centerPanel, BorderLayout.CENTER);
		createButtonPanel();
		rendererTickets();
	}

	public void setSelectedAction(String action) {
		this.action = action;
		if (!StringUtils.isEmpty(action)) {
			btnPay.setVisible(false);
			btnPayAll.setVisible(false);
			btnPrint.setVisible(false);
			btnPrintAll.setVisible(false);
			btnOrderInfo.setVisible(false);
		}
		setSelectedSection(null);
	}

	public void createButtonPanel() {
		TransparentPanel buttonPanel = new com.floreantpos.swing.TransparentPanel();
		buttonPanel.setLayout(new MigLayout("fill,hidemode 3, ins 0 17 10 17", "fill", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		btnPayAll = new PosButton(Messages.getString("SplitedTicketSelectionDialog.7")); //$NON-NLS-1$
		btnPayAll.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (!POSUtil.checkDrawerAssignment()) {
					return;
				}
				for (Ticket ticket : splitTickets) {
					ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
				}
				GroupSettleTicketAction action = new GroupSettleTicketAction(splitTickets);
				action.execute();
				updateView();
				if (getDueAmount() <= 0) {
					dispose();
				}
				else {
					setCanceled(false);
				}
			}
		});

		btnPay = new PosButton(Messages.getString("SplitedTicketSelectionDialog.8")); //$NON-NLS-1$
		btnPay.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (!POSUtil.checkDrawerAssignment()) {
					return;
				}
				TicketSection selectedSection = getSelectedSection();
				if (selectedSection == null) {
					POSMessageDialog.showMessage(POSUtil.getFocusedWindow(), "Please select ticket.");
					return;
				}
				Ticket splitTicket = selectedSection.getTicket();
				if (splitTicket == null)
					return;
				if (splitTicket.isPaid()) {
					POSMessageDialog.showMessage(Messages.getString("SplitedTicketSelectionDialog.9")); //$NON-NLS-1$
					return;
				}
				SettleTicketAction action = new SettleTicketAction(splitTicket.getId());
				action.execute();
				splitTicket = TicketService.getTicket(splitTicket.getId());
				updateTicketView(splitTicket);
				updateView();
				if (getDueAmount() <= 0) {
					dispose();
				}
				else {
					setCanceled(false);
				}
			}
		});

		btnPrintAll = new PosButton(Messages.getString("SplitedTicketSelectionDialog.17"));
		btnPrintAll.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				for (Ticket splitTicket : splitTickets) {
					ReceiptPrintService.printTicket(splitTicket);
				}
			}
		});

		btnPrint = new PosButton(Messages.getString("SplitedTicketSelectionDialog.20"));
		btnPrint.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				TicketSection selectedSection = getSelectedSection();
				if (selectedSection == null) {
					POSMessageDialog.showMessage(POSUtil.getFocusedWindow(), "Please select ticket.");
					return;
				}
				Ticket splitTicket = selectedSection.getTicket();
				if (splitTicket == null)
					return;
				ReceiptPrintService.printTicket(splitTicket);
			}
		});

		btnOrderInfo = new PosButton(Messages.getString("SplitedTicketSelectionDialog.24")); //$NON-NLS-1$
		btnOrderInfo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				showOrderInfo();
			}
		});

		PosButton btnCancel = new PosButton(POSConstants.CANCEL.toUpperCase());
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setCanceled(true);
				dispose();
			}
		});
		buttonPanel.add(btnOrderInfo);
		buttonPanel.add(btnPrintAll);
		buttonPanel.add(btnPrint);
		buttonPanel.add(btnPayAll);
		buttonPanel.add(btnPay);
		buttonPanel.add(btnCancel);
		add(buttonPanel, java.awt.BorderLayout.SOUTH);
	}

	private void showOrderInfo() {
		List<Ticket> ticketsToShow = new ArrayList<Ticket>();
		TicketSection selectedSection = getSelectedSection();
		if (selectedSection == null) {
			ticketsToShow.addAll(splitTickets);
		}
		else {
			ticketsToShow.add(selectedSection.getTicket());
		}
		OrderInfoView view;
		try {
			view = new OrderInfoView(ticketsToShow);
			OrderInfoDialog dialog = new OrderInfoDialog(view);
			dialog.showOnlyPrintButton();
			dialog.setSize(PosUIManager.getSize(400), PosUIManager.getSize(600));
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setLocationRelativeTo(Application.getPosWindow());
			dialog.open();
		} catch (Exception e) {
		}
	}

	protected void updateTicketView(Ticket splitTicket) {
		for (int row = 0; row < splitTickets.size(); row++) {
			Ticket modelTicket = (Ticket) splitTickets.get(row);
			if (modelTicket.getId().equals(splitTicket.getId())) {
				splitTickets.set(row, splitTicket);
			}
		}
	}

	public boolean isConfirmSpliting() {
		return confirmSpliting;
	}

	protected double getDueAmount() {
		double dueAmount = 0;
		for (Ticket ticket : splitTickets) {
			dueAmount += ticket.getDueAmount();
		}
		return dueAmount;
	}

	public void setRefundMode(boolean refundMode) {
		btnPay.setVisible(!refundMode);
		btnPayAll.setVisible(!refundMode);
	}

	public void updateView() {
		if (splitTickets == null) {
			return;
		}
		for (Component c : splitTicketsPanel.getComponents()) {
			TicketSection section = (TicketSection) c;
			section.updateView();
		}
	}

	public void rendererTickets() {
		if (splitTickets == null) {
			return;
		}
		splitTicketsPanel.removeAll();
		for (Ticket splitTicket : splitTickets) {
			TicketSection button = new TicketSection(splitTicket);
			Dimension size = PosUIManager.getSize(160, 150);
			splitTicketsPanel.add(button, "w " + size.width + "!,h " + size.height + "!"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		TicketSection button = new TicketSection(null);
		Dimension size = PosUIManager.getSize(160, 150);
		splitTicketsPanel.add(button, "w " + size.width + "!,h " + size.height + "!"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		setSelectedSection(getFirstSection());
		splitTicketsPanel.revalidate();
		splitTicketsPanel.repaint();
	}

	private class TicketSection extends JPanel implements MouseListener {

		Ticket splitTicket;
		boolean selected;
		private JLabel lblPaidStatus;
		private JLabel lblTitle;
		private JLabel lblTotalAmount;
		private JSeparator separator;

		public TicketSection(Ticket splitTicket) {
			this.splitTicket = splitTicket;
			setLayout(new MigLayout("hidemode 3, inset 0,fill")); //$NON-NLS-1$

			lblTitle = new JLabel();
			lblTitle.setBackground(new Color(66, 155, 207));
			lblTitle.setForeground(Color.white);
			lblTitle.setHorizontalAlignment(JLabel.CENTER);
			lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 20));
			lblTitle.setOpaque(true);

			add(lblTitle, "grow,wrap"); //$NON-NLS-1$
			setBackground(Color.white);
			setBorder(BorderFactory.createLineBorder(Color.GRAY));

			lblTotalAmount = new JLabel();
			lblTotalAmount.setHorizontalAlignment(JLabel.CENTER);
			lblTotalAmount.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 30));
			lblTotalAmount.setBackground(Color.white);
			lblTotalAmount.setOpaque(true);
			add(lblTotalAmount, "grow,wrap"); //$NON-NLS-1$

			separator = new JSeparator();
			add(separator, "gapleft 20,gapright 20,grow,wrap"); //$NON-NLS-1$

			lblPaidStatus = new JLabel();
			lblPaidStatus.setHorizontalAlignment(JLabel.CENTER);
			lblPaidStatus.setBackground(Color.white);
			lblPaidStatus.setForeground(Color.red);
			lblPaidStatus.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 20));
			lblPaidStatus.setOpaque(true);
			add(lblPaidStatus, "grow,wrap"); //$NON-NLS-1$

			addMouseListener(this);
			updateView();
		}

		public void updateView() {
			String currencySymbol = CurrencyUtil.getCurrencySymbol();
			String title = "";
			if (splitTicket == null) {
				title = "[New Ticket]";
				lblPaidStatus.setText("+");
				lblPaidStatus.setFont(new Font(lblPaidStatus.getFont().getName(), Font.BOLD, 48));
				separator.setVisible(false);
				lblTotalAmount.setVisible(false);
				lblTitle.setText("<html><center>" + String.valueOf(title) + "</center></html>");
			}
			else {
				Integer tokenNo = splitTicket.getId();

				if (tokenNo > 0) {
					title = "Ticket# " + tokenNo;
				}
				else
					title = "[New Check]";

				lblTitle.setText("<html><center><small>" + String.valueOf(title) + "</small><br>" + splitTicket.getOwner().getFirstName() + "</center></html>");
				lblTotalAmount.setText(currencySymbol + NumberUtil.formatNumber(splitTicket.getTotalAmount()));
				String dueStatusText = ""; //$NON-NLS-1$
				if (splitTicket.isRefunded()) {
					dueStatusText = "REFUNDED"; //$NON-NLS-1$
				}
				else if (splitTicket.getDueAmount() <= 0) {
					dueStatusText = "PAID"; //$NON-NLS-1$
				}
				else {
					dueStatusText = currencySymbol + NumberUtil.formatNumber(splitTicket.getDueAmount());
				}
				lblPaidStatus.setText(dueStatusText);
			}
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		public boolean isSelected() {
			return selected;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (action != null) {
				setCanceled(false);
				dispose();
				if (splitTicket == null) {
					doCreateNewTicket();
					createNewTicket = true;
				}
				else if (action.equals(POSConstants.SETTLE)) {
					if (!POSUtil.checkDrawerAssignment()) {
						return;
					}
					SettleTicketAction action = new SettleTicketAction(splitTicket.getId());
					action.execute();
				}
				else if (action.equals(POSConstants.SEND_TO_KITCHEN)) {
					if (splitTicket.getOrderType().isShouldPrintToKitchen()) {
						if (splitTicket.needsKitchenPrint()) {
							ReceiptPrintService.printToKitchen(splitTicket);
							POSMessageDialog.showMessage("Ticket successfully sent to kitchen");
							TicketDAO.getInstance().refresh(splitTicket);
						}
						else {
							POSMessageDialog.showMessage("Ticket has already sent to kitchen");
						}
					}
				}
				else if (action.equals(POSConstants.ORDER_INFO)) {
					List<Ticket> ticketsToShow = new ArrayList<>();
					ticketsToShow.add(splitTicket);
					OrderInfoView view;
					try {
						view = new OrderInfoView(ticketsToShow);
						OrderInfoDialog dialog = new OrderInfoDialog(view);
						dialog.updateView();
						dialog.setSize(PosUIManager.getSize(400), PosUIManager.getSize(600));
						dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						dialog.setLocationRelativeTo(Application.getPosWindow());
						dialog.setVisible(true);
					} catch (Exception e1) {
					}
				}
				else if (action.equals(POSConstants.SPLIT_TICKET)) {
					SplitTicketAction action = new SplitTicketAction(splitTicket);
					action.execute();
				}
				else if (action.equals(POSConstants.EDIT)) {
					Ticket ticketToEdit = TicketDAO.getInstance().loadFullTicket(splitTicket.getId());
					OrderView.getInstance().setCurrentTicket(ticketToEdit);
					RootView.getInstance().showView(OrderView.VIEW_NAME);
					OrderView.getInstance().getTicketView().getTxtSearchItem().requestFocus();
				}
			}
		}

		private void doCreateNewTicket() {
			try {
				List<ShopTable> selectedTables = getSelectedTables();
				if (selectedTables.isEmpty()) {
					return;
				}
				OrderServiceFactory.getOrderService().createNewTicket(getOrderType(), selectedTables, null);
			} catch (TicketAlreadyExistsException e) {
				PosLog.error(getClass(), e);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			setSelectedSection(this);
		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}

		public Ticket getTicket() {
			return splitTicket;
		}
	}

	public void setSelectedSection(TicketSection section) {
		for (Component c : splitTicketsPanel.getComponents()) {
			TicketSection sec = (TicketSection) c;
			if (sec == section) {
				sec.setBorder(BorderFactory.createLineBorder(Color.blue, 4));
				sec.setSelected(true);
			}
			else {
				sec.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
				sec.setSelected(false);
			}
		}
	}

	public TicketSection getSelectedSection() {
		for (Component c : splitTicketsPanel.getComponents()) {
			TicketSection sec = (TicketSection) c;
			if (sec.isSelected()) {
				return sec;
			}
		}
		return null;
	}

	public TicketSection getFirstSection() {
		for (Component c : splitTicketsPanel.getComponents()) {
			TicketSection sec = (TicketSection) c;
			return sec;
		}
		return null;
	}

	public Ticket getTicket() {
		if (getSelectedSection() != null)
			return getSelectedSection().getTicket();

		return null;
	}

	public void hidePayButtons(boolean b) {
		btnPay.setVisible(!b);
		btnPayAll.setVisible(!b);
	}

	public List<ShopTable> getSelectedTables() {
		return selectedTables;
	}

	public void setSelectedTables(List<ShopTable> selectedTables) {
		this.selectedTables = selectedTables;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public boolean createNewTicket() {
		return createNewTicket;
	}
}
