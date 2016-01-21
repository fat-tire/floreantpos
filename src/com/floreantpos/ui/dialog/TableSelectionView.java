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

import javax.swing.DefaultListModel;
import javax.swing.JPanel;

import com.floreantpos.Messages;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.ScrollableFlowPanel;
import com.floreantpos.swing.ShopTableButton;
import com.floreantpos.ui.views.TableMapView;
import com.jidesoft.swing.JideScrollPane;

public class TableSelectionView extends JPanel {

	private DefaultListModel<ShopTableButton> addedTableListModel = new DefaultListModel<ShopTableButton>();
	private Map<ShopTable, ShopTableButton> tableButtonMap = new HashMap<ShopTable, ShopTableButton>();

	private static TableSelectionView instance;
	private ScrollableFlowPanel buttonsPanel;

	private Ticket ticket;

	public TableSelectionView() {
		init();
	}

	private void init() {
		setLayout(new BorderLayout());

		buttonsPanel = new ScrollableFlowPanel(FlowLayout.LEADING);

		redererTable();

		JideScrollPane scrollPane = new JideScrollPane(buttonsPanel, JideScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JideScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(60, 0));

		add(scrollPane, BorderLayout.CENTER);
	}

	public synchronized void redererTable() {
		addedTableListModel.clear();
		buttonsPanel.getContentPane().removeAll();
		List<ShopTable> tables = ShopTableDAO.getInstance().findAll();

		for (ShopTable shopTable : tables) {
			ShopTableButton tableButton = new ShopTableButton(shopTable);
			tableButton.setPreferredSize(new Dimension(159, 138));
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
					shopTableButton.setText("<html><center>" + shopTableButton.getText() + "<br><h4>" + ticket.getOwner().getFirstName() + "<br>" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							+ ticket.getTotalAmount() + "</h4></center></html>"); //$NON-NLS-1$
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

		if (ticket != null) {
			if (button.getShopTable().isServing()) {
				POSMessageDialog.showError(this, Messages.getString("TableSelectionDialog.2") + "is not empty"); //$NON-NLS-1$ //$NON-NLS-2$
				return false;
			}
		}

		if (TableMapView.btnAttach.isSelected()) {
			if (addedTableListModel.contains(button)) {
				return true;
			}
			button.getShopTable().setServing(true);
			button.setBackground(Color.green);
			button.setForeground(Color.black);
			this.addedTableListModel.addElement(button);
		}

		if (TableMapView.btnDetach.isSelected()) {
			Ticket ticket = button.getTicket();
			if (ticket != null) {
				for (Iterator iterator = ticket.getTableNumbers().iterator(); iterator.hasNext();) {
					Integer id = (Integer) iterator.next();
					if (button.getId() == id) {
						iterator.remove();
					}

				}
				button.getShopTable().setServing(false);
				button.getShopTable().setBooked(false);
				button.update();
				shopTable.setServing(false);
				ShopTableDAO.getInstance().saveOrUpdate(shopTable);
				TicketDAO.getInstance().saveOrUpdate(ticket);
				redererTable();
				return false;
			}

			if (addedTableListModel.contains(button)) {
				addedTableListModel.removeElement(button);
				button.getShopTable().setServing(false);
				button.update();
				return true;
			}
		}

		if (shopTable.isServing() && ticket == null) {
			TableMapView.getInstance().editTicket(tableNumber);
			return false;
		}

		if (!TableMapView.btnAttach.isSelected() && !TableMapView.btnDetach.isSelected()) {
			if (!addedTableListModel.contains(button)) {
				button.getShopTable().setServing(true);
				this.addedTableListModel.addElement(button);
			}
			TableMapView.getInstance().doCreateNewTicket();
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

	public static TableSelectionView getInstance() {
		if (instance == null) {
			instance = new TableSelectionView();
		}
		return instance;
	}
}