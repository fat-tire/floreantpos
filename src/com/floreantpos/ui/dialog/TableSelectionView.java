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
package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.main.Application;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.ScrollableFlowPanel;
import com.floreantpos.swing.ShopTableButton;
import com.floreantpos.ui.views.order.CustomOrderServiceExtension;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.ui.views.order.RootView;
import com.floreantpos.util.TicketAlreadyExistsException;
import com.jidesoft.swing.JideScrollPane;

public class TableSelectionView extends JPanel implements ActionListener {

	private DefaultListModel<ShopTableButton> addedTableListModel = new DefaultListModel<ShopTableButton>();
	private Map<ShopTable, ShopTableButton> tableButtonMap = new HashMap<ShopTable, ShopTableButton>();

	private ScrollableFlowPanel buttonsPanel;

	private POSToggleButton btnGroup;
	private POSToggleButton btnUnGroup;
	private POSToggleButton btnTransfer;

	private PosButton btnDone;
	private PosButton btnCancel;
	private PosButton btnRefresh;

	private ButtonGroup btnGroups;

	public TableSelectionView() {
		init();
	}

	private void init() {
		setLayout(new BorderLayout());

		buttonsPanel = new ScrollableFlowPanel(FlowLayout.CENTER);

		setLayout(new java.awt.BorderLayout(10, 10));

		TitledBorder titledBorder1 = BorderFactory.createTitledBorder(null, POSConstants.DINE_IN_BUTTON_TEXT, TitledBorder.CENTER,
				TitledBorder.DEFAULT_POSITION);

		JPanel leftPanel = new JPanel(new java.awt.BorderLayout(5, 5));
		leftPanel.setBorder(new CompoundBorder(titledBorder1, new EmptyBorder(2, 2, 2, 2)));

		redererTable();

		JideScrollPane scrollPane = new JideScrollPane(buttonsPanel, JideScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JideScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(60, 0));

		leftPanel.add(scrollPane, java.awt.BorderLayout.CENTER);

		add(leftPanel, java.awt.BorderLayout.CENTER);

		createButtonActionPanel();
	}

	private void createButtonActionPanel() {
		TitledBorder titledBorder2 = BorderFactory.createTitledBorder(null, "-", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION); //$NON-NLS-1$

		JPanel rightPanel = new JPanel(new BorderLayout(20, 20));
		rightPanel.setPreferredSize(new Dimension(120, 0));
		rightPanel.setBorder(new CompoundBorder(titledBorder2, new EmptyBorder(2, 2, 6, 2)));

		JPanel actionBtnPanel = new JPanel(new MigLayout("ins 2 2 0 2, hidemode 3, flowy", "grow", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		btnGroups = new ButtonGroup();

		btnGroup = new POSToggleButton(Messages.getString("TableMapView.1")); //$NON-NLS-1$
		btnUnGroup = new POSToggleButton(Messages.getString("TableMapView.2")); //$NON-NLS-1$
		btnTransfer = new POSToggleButton(Messages.getString("TableMapView.3")); //$NON-NLS-1$
		btnDone = new PosButton(Messages.getString("TableMapView.4")); //$NON-NLS-1$
		btnCancel = new PosButton(POSConstants.CANCEL); //$NON-NLS-1$

		btnGroup.addActionListener(this);
		btnUnGroup.addActionListener(this);
		btnDone.addActionListener(this);
		btnCancel.addActionListener(this);

		btnDone.setVisible(false);
		btnCancel.setVisible(false);

		btnGroup.setIcon(new ImageIcon(getClass().getResource("/images/plus.png"))); //$NON-NLS-1$
		btnUnGroup.setIcon(new ImageIcon(getClass().getResource("/images/minus2.png"))); //$NON-NLS-1$

		btnGroups.add(btnGroup);
		btnGroups.add(btnUnGroup);
		btnGroups.add(btnTransfer);

		actionBtnPanel.add(btnGroup, "grow"); //$NON-NLS-1$
		actionBtnPanel.add(btnUnGroup, "grow"); //$NON-NLS-1$
		actionBtnPanel.add(btnDone, "grow"); //$NON-NLS-1$
		actionBtnPanel.add(btnCancel, "grow"); //$NON-NLS-1$

		rightPanel.add(actionBtnPanel);

		btnRefresh = new PosButton(POSConstants.REFRESH);
		btnRefresh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				redererTable();
				btnGroups.clearSelection();
			}
		});
		rightPanel.add(btnRefresh, BorderLayout.SOUTH);

		add(rightPanel, java.awt.BorderLayout.EAST);

	}

	public synchronized void redererTable() {
		addedTableListModel.clear();
		buttonsPanel.getContentPane().removeAll();
		List<ShopTable> tables = ShopTableDAO.getInstance().findAll();

		for (ShopTable shopTable : tables) {
			ShopTableButton tableButton = new ShopTableButton(shopTable);
			tableButton.setPreferredSize(new Dimension(157, 138));
			tableButton.setFont(new Font(tableButton.getFont().getName(), tableButton.getFont().getStyle(), 30));

			tableButton.setText(tableButton.getText());
			tableButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					addTable(e);
				}
			});

			tableButton.update();
			buttonsPanel.add(tableButton);
			tableButtonMap.put(shopTable, tableButton);
		}

		rendererTablesTicket();
	}

	private void rendererTablesTicket() {
		List<Ticket> openTickets = TicketDAO.getInstance().findOpenTickets();
		for (Ticket ticket : openTickets) {
			for (ShopTableButton shopTableButton : tableButtonMap.values()) {
				if (ticket.getTableNumbers().contains(shopTableButton.getId())) {
					shopTableButton.setText("<html><center>" + shopTableButton.getText() + "<br><h4>" + ticket.getOwner().getFirstName() + "<br>Chk#" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							+ ticket.getId() + "</h4></center></html>"); //$NON-NLS-1$
					shopTableButton.setUser(ticket.getOwner());
				}
			}
		}
	}

	private boolean addTable(ActionEvent e) {

		ShopTableButton button = (ShopTableButton) e.getSource();
		int tableNumber = button.getId();

		ShopTable shopTable = ShopTableDAO.getInstance().getByNumber(tableNumber);

		if (shopTable == null) {
			POSMessageDialog.showError(this, Messages.getString("TableSelectionDialog.2") + e + Messages.getString("TableSelectionDialog.3")); //$NON-NLS-1$ //$NON-NLS-2$
			return false;
		}

		if (btnGroup.isSelected() && !shopTable.isServing()) {
			if (addedTableListModel.contains(button)) {
				return true;
			}
			button.getShopTable().setServing(true);
			button.setBackground(Color.green);
			button.setForeground(Color.black);
			this.addedTableListModel.addElement(button);
			return false;
		}

		if (btnUnGroup.isSelected()) {
			if (addedTableListModel.contains(button)) {
				return true;
			}
			Ticket ticket = button.getTicket();
			if (ticket == null) {
				return false;
			}

			int ticketId = ticket.getId();

			Enumeration<ShopTableButton> elements = addedTableListModel.elements();
			while (elements.hasMoreElements()) {
				ShopTableButton shopTableButton = (ShopTableButton) elements.nextElement();
				if (shopTableButton.getTicket().getId() != ticketId) {
					return false;
				}
			}

			if (addedTableListModel.size() >= ticket.getTableNumbers().size() - 1) {
				return false;
			}

			button.getShopTable().setServing(true);
			button.setBackground(Color.green);
			button.setForeground(Color.black);
			this.addedTableListModel.addElement(button);
			return false;
		}

		if (shopTable.isServing()) {
			int userId = Application.getCurrentUser().getAutoId();
			int userId2 = button.getUser().getAutoId();
			if (userId != userId2) {
				if (!hasUserAccess(button.getUser())) {
					return false;
				}
			}
			editTicket(tableNumber);
			return false;
		}

		if (!btnGroup.isSelected() && !btnGroup.isSelected()) {
			if (!addedTableListModel.contains(button)) {
				button.getShopTable().setServing(true);
				this.addedTableListModel.addElement(button);
			}
			doCreateNewTicket();
		}
		return true;
	}

	private boolean hasUserAccess(User ticketUser) {

		if (ticketUser == null) {
			return false;
		}

		String password = PasswordEntryDialog.show(Application.getPosWindow(), Messages.getString("PosAction.0")); //$NON-NLS-1$
		if (StringUtils.isEmpty(password)) {
			return false;
		}

		int inputUserId = UserDAO.getInstance().findUserBySecretKey(password).getAutoId();
		if (inputUserId != ticketUser.getAutoId()) {
			POSMessageDialog.showError(Application.getPosWindow(), "Incorrect password"); //$NON-NLS-1$
			return false;
		}
		return true;
	}

	public List<ShopTable> getTables() {
		Enumeration<ShopTableButton> elements = this.addedTableListModel.elements();
		List<ShopTable> tables = new ArrayList<ShopTable>();

		while (elements.hasMoreElements()) {
			ShopTableButton shopTableButton = (ShopTableButton) elements.nextElement();
			tables.add(shopTableButton.getShopTable());
		}

		return tables;
	}

	private void clearSelection() {
		addedTableListModel.clear();
		redererTable();
		btnGroups.clearSelection();
		btnGroup.setVisible(true);
		btnUnGroup.setVisible(true);
		btnDone.setVisible(false);
		btnCancel.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object object = e.getSource();
		if (object == btnGroup) {
			addedTableListModel.clear();
			btnUnGroup.setVisible(false);
			btnDone.setVisible(true);
			btnCancel.setVisible(true);
		}
		else if (object == btnUnGroup) {
			addedTableListModel.clear();
			btnGroup.setVisible(false);
			btnDone.setVisible(true);
			btnCancel.setVisible(true);
		}
		else if (object == btnDone) {
			if (btnGroup.isSelected()) {
				doCreateNewTicket();
				clearSelection();
			}
			else if (btnUnGroup.isSelected()) {
				doUnGroupAction();
				clearSelection();
			}
		}
		else if (object == btnCancel) {
			clearSelection();
		}

	}

	private void doCreateNewTicket() {
		try {
			List<ShopTable> selectedTables = getTables();

			OrderServiceExtension orderService = new CustomOrderServiceExtension(selectedTables);
			orderService.createNewTicket(OrderType.DINE_IN);

		} catch (TicketAlreadyExistsException e) {

		}
	}

	private boolean editTicket(Integer shopTableNumber) {
		List<Ticket> openTickets = TicketDAO.getInstance().findOpenTickets();
		Integer ticketId = null;
		for (Ticket ticket : openTickets) {
			if (ticket.getTableNumbers().contains(shopTableNumber)) {
				ticketId = ticket.getId();
				if (ticketId == null) {
					return false;
				}
			}
		}

		Ticket ticketToEdit = null;
		if (ticketId != null) {
			ticketToEdit = TicketDAO.getInstance().loadFullTicket(ticketId);
		}
		else {
			return false;
		}
		OrderView.getInstance().setCurrentTicket(ticketToEdit);
		RootView.getInstance().showView(OrderView.VIEW_NAME);
		OrderView.getInstance().getTicketView().getTxtSearchItem().requestFocus();
		return true;
	}

	private void doUnGroupAction() {
		if (addedTableListModel != null) {
			Enumeration<ShopTableButton> elements = this.addedTableListModel.elements();

			while (elements.hasMoreElements()) {
				ShopTableButton button = (ShopTableButton) elements.nextElement();
				ShopTable shopTable = button.getShopTable();
				Ticket ticket = button.getTicket();
				if (ticket != null) {
					for (Iterator iterator = ticket.getTableNumbers().iterator(); iterator.hasNext();) {
						Integer id = (Integer) iterator.next();
						if (button.getId() == id) {
							iterator.remove();
						}

					}

					shopTable.setServing(false);
					ShopTableDAO.getInstance().saveOrUpdate(shopTable);
					TicketDAO.getInstance().saveOrUpdate(ticket);
				}
			}
		}
	}
}