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
/*
 * OrderView.java
 *
 * Created on August 4, 2006, 6:58 PM
 */

package com.floreantpos.ui.views.order;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.customer.CustomerSelectorDialog;
import com.floreantpos.customer.CustomerSelectorFactory;
import com.floreantpos.main.Application;
import com.floreantpos.model.ITicketItem;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemCookingInstruction;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.MiscTicketItemDialog;
import com.floreantpos.ui.dialog.NumberSelectionDialog2;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.PasswordEntryDialog;
import com.floreantpos.ui.dialog.SeatSelectionDialog;
import com.floreantpos.ui.tableselection.TableSelectorDialog;
import com.floreantpos.ui.tableselection.TableSelectorFactory;
import com.floreantpos.ui.views.CookingInstructionSelectionView;

/**
 *
 * @author  MShahriar
 */
public class OrderView extends ViewPanel {
	private HashMap<String, JComponent> views = new HashMap<String, JComponent>();

	public final static String VIEW_NAME = "ORDER_VIEW"; //$NON-NLS-1$
	private static OrderView instance;

	private Ticket currentTicket;

	private com.floreantpos.ui.views.order.CategoryView categoryView = new com.floreantpos.ui.views.order.CategoryView();
	private com.floreantpos.swing.TransparentPanel midContainer = new com.floreantpos.swing.TransparentPanel(new BorderLayout(5, 5));
	private com.floreantpos.ui.views.order.TicketView ticketView = new com.floreantpos.ui.views.order.TicketView();

	private GroupView groupView = new GroupView();
	private MenuItemView itemView = new MenuItemView();
	private OrderController orderController = new OrderController(this);

	private JPanel actionButtonPanel = new JPanel(new MigLayout("fill, ins 2, hidemode 3", "sg, fill", ""));

	private com.floreantpos.swing.PosButton btnHold = new com.floreantpos.swing.PosButton(com.floreantpos.POSConstants.HOLD_BUTTON_TEXT);
	private com.floreantpos.swing.PosButton btnDone = new com.floreantpos.swing.PosButton(com.floreantpos.POSConstants.SAVE_BUTTON_TEXT);
	private com.floreantpos.swing.PosButton btnSend = new com.floreantpos.swing.PosButton(com.floreantpos.POSConstants.SEND_TO_KITCHEN);
	private com.floreantpos.swing.PosButton btnCancel = new com.floreantpos.swing.PosButton(POSConstants.CANCEL_BUTTON_TEXT);
	private com.floreantpos.swing.PosButton btnGuestNo = new com.floreantpos.swing.PosButton(POSConstants.GUEST_NO_BUTTON_TEXT);
	private com.floreantpos.swing.PosButton btnSeatNo = new com.floreantpos.swing.PosButton("SEAT:");
	private com.floreantpos.swing.PosButton btnMisc = new com.floreantpos.swing.PosButton(POSConstants.MISC_BUTTON_TEXT);
	private com.floreantpos.swing.PosButton btnOrderType = new com.floreantpos.swing.PosButton(POSConstants.ORDER_TYPE_BUTTON_TEXT);
	private com.floreantpos.swing.PosButton btnTableNumber = new com.floreantpos.swing.PosButton(POSConstants.TABLE_NO_BUTTON_TEXT);
	private com.floreantpos.swing.PosButton btnCustomer = new PosButton(POSConstants.CUSTOMER_SELECTION_BUTTON_TEXT);
	private PosButton btnCookingInstruction = new PosButton(IconFactory.getIcon("/ui_icons/", "cooking-instruction.png"));
	//	private PosButton btnAddOn = new PosButton(POSConstants.ADD_ON);
	private PosButton btnDiscount = new PosButton(Messages.getString("TicketView.43")); //$NON-NLS-1$

	/** Creates new form OrderView */
	private OrderView() {
		initComponents();
	}

	public void addView(final String viewName, final JComponent view) {
		JComponent oldView = views.get(viewName);
		if (oldView != null) {
			return;
		}
		midContainer.add(view, viewName);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {
		setOpaque(false);
		setLayout(new java.awt.BorderLayout(2, 1));

		midContainer.setOpaque(false);
		midContainer.setBorder(null);
		midContainer.add(groupView, BorderLayout.NORTH);
		midContainer.add(itemView);

		add(categoryView, java.awt.BorderLayout.EAST);
		add(ticketView, java.awt.BorderLayout.WEST);
		add(midContainer, java.awt.BorderLayout.CENTER);
		add(actionButtonPanel, java.awt.BorderLayout.SOUTH);

		//		addView(GroupView.VIEW_NAME, groupView);
		//		addView(MenuItemView.VIEW_NAME, itemView);
		//		addView("VIEW_EMPTY", new com.floreantpos.swing.TransparentPanel()); //$NON-NLS-1$

		addActionButtonPanel();
		btnOrderType.setVisible(false);
		showView("VIEW_EMPTY"); //$NON-NLS-1$
	}// </editor-fold>//GEN-END:initComponents

	private void handleTicketItemSelection() {

		ITicketItem selectedItem = (ITicketItem) ticketView.getTicketViewerTable().getSelected();
		TicketItem selectedTicketItem = null;
		OrderView orderView = OrderView.getInstance();

		if (selectedItem instanceof TicketItem) {
			selectedTicketItem = (TicketItem) selectedItem;
			MenuItemDAO dao = new MenuItemDAO();
			MenuItem menuItem = dao.get(selectedTicketItem.getItemId());

			if (menuItem != null) {
				MenuGroup menuGroup = menuItem.getParent();
				MenuItemView itemView = OrderView.getInstance().getItemView();
				if (!menuGroup.equals(itemView.getMenuGroup())) {
					itemView.setMenuGroup(menuGroup);
				}

				orderView.showView(MenuItemView.VIEW_NAME);
				itemView.selectItem(menuItem);

				MenuCategory menuCategory = menuGroup.getParent();
				orderView.getCategoryView().setSelectedCategory(menuCategory);
			}
		}

		if (selectedItem == null) {
			btnCookingInstruction.setEnabled(false);
			btnDiscount.setEnabled(false);
			//			btnAddOn.setEnabled(false);
		}
		else {
			btnCookingInstruction.setEnabled(selectedItem.canAddCookingInstruction());
			btnDiscount.setEnabled(selectedItem.canAddDiscount());
			//			btnAddOn.setEnabled(selectedTicketItem != null && selectedTicketItem.isHasModifiers());
		}
		//			btnVoid.setEnabled(item.canAddAdOn());
		//			btnAddOn.setEnabled(item.canVoid());

		//		else if (selectedObject instanceof TicketItemModifier) {
		//			selectedTicketItem = ((TicketItemModifier) selectedObject).getParent().getParent();
		//			if (selectedTicketItem == null)
		//				return;
		//
		//			ModifierView modifierView = orderView.getModifierView();
		//
		//			if (selectedTicketItem.isHasModifiers()) {
		//				MenuItemDAO dao = new MenuItemDAO();
		//				MenuItem menuItem = dao.get(selectedTicketItem.getItemId());
		//				if (!menuItem.equals(modifierView.getMenuItem())) {
		//					menuItem = dao.initialize(menuItem);
		//					modifierView.setMenuItem(menuItem, selectedTicketItem);
		//				}
		//
		//				MenuCategory menuCategory = menuItem.getParent().getParent();
		//				orderView.getCategoryView().setSelectedCategory(menuCategory);
		//
		//				TicketItemModifier ticketItemModifier = (TicketItemModifier) selectedObject;
		//				ticketItemModifier.setSelected(true);
		//				modifierView.select(ticketItemModifier);
		//
		//				orderView.showView(ModifierView.VIEW_NAME);
		//			}
		//		}
	}

	private void addActionButtonPanel() {
		ticketView.getTicketViewerTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					handleTicketItemSelection();
				}
			}
		});

		btnDone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {

					ticketView.doFinishOrder();

				} catch (StaleObjectStateException x) {
					POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("TicketView.22")); //$NON-NLS-1$
					return;
				} catch (PosException x) {
					POSMessageDialog.showError(x.getMessage());
				} catch (Exception x) {
					POSMessageDialog.showError(Application.getPosWindow(), POSConstants.ERROR_MESSAGE, x);
				}
			}
		});

		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				if (ticketView.isCancelable()) {
					ticketView.doCancelOrder();
					return;
				}

				int result = POSMessageDialog.showYesNoQuestionDialog(null, "Items have been sent to kitchen, are you sure to cancel this ticket?", "Confirm");
				if (result != JOptionPane.YES_OPTION) {
					return;
				}

				ticketView.doCancelOrder();
				ticketView.setAllowToLogOut(true);
			}
		});

		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {

					ticketView.sendTicketToKitchen();
					ticketView.updateView();
					POSMessageDialog.showMessage("Items sent to kitchen");
				} catch (StaleObjectStateException x) {
					POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("TicketView.22")); //$NON-NLS-1$
					return;
				} catch (PosException x) {
					POSMessageDialog.showError(x.getMessage());
				} catch (Exception x) {
					POSMessageDialog.showError(Application.getPosWindow(), POSConstants.ERROR_MESSAGE, x);
				}

			}
		});

		btnOrderType.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// doViewOrderInfo();
				//doChangeOrderType(); fix
			}
		});

		btnCustomer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doAddEditCustomer();
			}
		});

		btnMisc.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doInsertMisc(evt);
			}
		});

		btnGuestNo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCustomerNumberActionPerformed();
			}
		});

		btnSeatNo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doAddSeatNumber();
			}
		});

		btnTableNumber.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				updateTableNumber();
			}
		});

		btnCookingInstruction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAddCookingInstruction();
			}
		});

		btnHold.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentTicket.getOrderType().isShowTableSelection() && currentTicket.getOrderType().isRequiredCustomerData()//fix
						&& !Application.getCurrentUser().hasPermission(UserPermission.HOLD_TICKET)) {
					//

					String password = PasswordEntryDialog.show(Application.getPosWindow(), "Please enter privileged password");
					if (StringUtils.isEmpty(password)) {
						return;
					}

					User user2 = UserDAO.getInstance().findUserBySecretKey(password);
					if (user2 == null) {
						POSMessageDialog.showError(Application.getPosWindow(), "No user found with that secret key");
						return;
					}
					else {
						if (!user2.hasPermission(UserPermission.HOLD_TICKET)) {
							POSMessageDialog.showError(Application.getPosWindow(), "No permission");
							return;
						}
					}
				}

				if (ticketView.getTicket().getTicketItems() == null || ticketView.getTicket().getTicketItems().size() == 0) {
					POSMessageDialog.showError(com.floreantpos.POSConstants.TICKET_IS_EMPTY_);
					return;
				}
				ticketView.doHoldOrder();
				ticketView.setAllowToLogOut(true);
			}
		});

		//		btnAddOn.addActionListener(new ActionListener() {
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//				doAddAddOn();
		//			}
		//		});

		btnDiscount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addDiscount();
			}
		});

		actionButtonPanel.add(btnOrderType);
		actionButtonPanel.add(btnCustomer);
		actionButtonPanel.add(btnTableNumber);
		actionButtonPanel.add(btnGuestNo);
		actionButtonPanel.add(btnSeatNo);
		actionButtonPanel.add(btnCookingInstruction);
		//		actionButtonPanel.add(btnAddOn);
		actionButtonPanel.add(btnMisc);
		actionButtonPanel.add(btnHold);
		actionButtonPanel.add(btnSend);
		actionButtonPanel.add(btnCancel);
		actionButtonPanel.add(btnDone);

		btnCookingInstruction.setEnabled(false);
		//		btnAddOn.setEnabled(false);
	}

	public void updateTableNumber() {
		Session session = null;
		org.hibernate.Transaction transaction = null;

		try {

			Ticket thisTicket = currentTicket;

			TableSelectorDialog dialog = TableSelectorFactory.createTableSelectorDialog(thisTicket.getOrderType());
			dialog.setCreateNewTicket(false);
			if (thisTicket != null) {
				dialog.setTicket(thisTicket);
			}
			dialog.openUndecoratedFullScreen();

			if (dialog.isCanceled()) {
				return;
			}
			List<ShopTable> tables = dialog.getSelectedTables();

			if (tables == null) {
				return;
			}

			session = TicketDAO.getInstance().createNewSession();
			transaction = session.beginTransaction();

			clearShopTable(session, thisTicket);
			session.saveOrUpdate(thisTicket);

			for (ShopTable shopTable : tables) {
				shopTable.setServing(true);
				session.merge(shopTable);

				thisTicket.addTable(shopTable.getTableNumber());
			}

			session.merge(thisTicket);
			transaction.commit();

			actionUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private void clearShopTable(Session session, Ticket thisTicket) {
		ShopTableDAO shopTableDao = ShopTableDAO.getInstance();
		List<ShopTable> tables2 = shopTableDao.getTables(thisTicket);

		if (tables2 == null)
			return;

		shopTableDao.releaseAndDeleteTicketTables(thisTicket);

		tables2.clear();
	}

	protected void btnCustomerNumberActionPerformed() {// GEN-FIRST:event_btnCustomerNumberActionPerformed
		Ticket thisTicket = currentTicket;
		int guestNumber = thisTicket.getNumberOfGuests();

		NumberSelectionDialog2 dialog = new NumberSelectionDialog2();
		dialog.setTitle(com.floreantpos.POSConstants.NUMBER_OF_GUESTS);
		dialog.setValue(guestNumber);
		dialog.pack();
		dialog.open();

		if (dialog.isCanceled()) {
			return;
		}

		guestNumber = (int) dialog.getValue();
		if (guestNumber == 0) {
			POSMessageDialog.showError(Application.getPosWindow(), com.floreantpos.POSConstants.GUEST_NUMBER_CANNOT_BE_0);
			return;
		}

		thisTicket.setNumberOfGuests(guestNumber);
		actionUpdate();
	}// GEN-LAST:event_btnCustomerNumberActionPerformed

	protected void doAddSeatNumber() {// GEN-FIRST:event_btnCustomerNumberActionPerformed
		SeatSelectionDialog seatDialog = new SeatSelectionDialog(currentTicket.getTableNumbers(), getSeatNumbers());
		seatDialog.setTitle("Select Seat");
		seatDialog.pack();
		seatDialog.open();

		if (seatDialog.isCanceled()) {
			return;
		}
		int seatNumber = seatDialog.getSeatNumber();
		if (seatNumber == -1) {
			NumberSelectionDialog2 dialog = new NumberSelectionDialog2();
			dialog.setTitle("Enter seat number");
			dialog.pack();
			dialog.open();

			if (dialog.isCanceled()) {
				return;
			}
			seatNumber = (int) dialog.getValue();
		}

		btnSeatNo.setText("SEAT: " + seatNumber);
		btnSeatNo.putClientProperty("SEAT_NO", seatNumber);
		doAddSeatTreatTicketItem(seatNumber);
	}

	private void doAddSeatTreatTicketItem(Integer seatNumber) {
		TicketItem ticketItem = new TicketItem();
		if (seatNumber == 0)
			ticketItem.setName("Seat** Shared");
		else
			ticketItem.setName("Seat** " + seatNumber);

		ticketItem.setShouldPrintToKitchen(true);
		ticketItem.setTreatAsSeat(true);
		ticketItem.setSeatNumber(seatNumber);
		ticketItem.setTicket(currentTicket);
		ticketView.addTicketItem(ticketItem);
	}

	private int getLastSeatNumber() {
		int lastSeatNumber = 0;
		List<TicketItem> ticketItems = currentTicket.getTicketItems();
		if (ticketItems != null && !ticketItems.isEmpty()) {
			TicketItem lastTicketItem = ticketItems.get(ticketItems.size() - 1);
			lastSeatNumber = lastTicketItem.getSeatNumber();
		}
		return lastSeatNumber;
	}

	protected Integer getSelectedSeatNumber() {
		Object seatNumber = btnSeatNo.getClientProperty("SEAT_NO");
		if (seatNumber == null)
			return 0;

		Integer seatNo = (Integer) seatNumber;

		boolean sendToKitchen = false;
		for (TicketItem ticketItem : currentTicket.getTicketItems()) {
			if (!ticketItem.isTreatAsSeat())
				continue;
			int existingSeatNumber = ticketItem.getSeatNumber();
			if (existingSeatNumber == seatNo) {
				sendToKitchen = ticketItem.isPrintedToKitchen();
			}
		}
		if (sendToKitchen) {
			doAddSeatTreatTicketItem(seatNo);
		}

		return seatNo;
	}

	protected List<Integer> getSeatNumbers() {
		List<Integer> seatNumbers = new ArrayList<>();

		for (TicketItem ticketItem : currentTicket.getTicketItems()) {
			if (ticketItem.isTreatAsSeat() && !seatNumbers.contains(ticketItem.getSeatNumber())) {
				seatNumbers.add(ticketItem.getSeatNumber());
			}
		}
		return seatNumbers;
	}

	protected void doInsertMisc(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_doInsertMisc
		MiscTicketItemDialog dialog = new MiscTicketItemDialog();
		//dialog.setSize(900, 580);
		dialog.pack();
		dialog.open();

		if (!dialog.isCanceled()) {
			TicketItem ticketItem = dialog.getTicketItem();
			ticketItem.setTicket(currentTicket);
			ticketItem.calculatePrice();
			ticketView.addTicketItem(ticketItem);
		}
	}// GEN-LAST:event_doInsertMisc

	protected void doAddEditCustomer() {
		CustomerSelectorDialog dialog = CustomerSelectorFactory.createCustomerSelectorDialog(currentTicket.getOrderType());
		dialog.setCreateNewTicket(false);
		if (currentTicket != null) {
			dialog.setTicket(currentTicket);
		}
		dialog.openUndecoratedFullScreen();

		if (!dialog.isCanceled()) {
			currentTicket.setCustomer(dialog.getSelectedCustomer());
		}
	}

	protected void addDiscount() {
		Object selectedObject = (ITicketItem) ticketView.getTicketViewerTable().getSelected();

		if (!(selectedObject instanceof TicketItem)) {
			POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("TicketView.20")); //$NON-NLS-1$
			return;
		}

		/*TicketItem ticketItem = (TicketItem) selectedObject;

		double d = NumberSelectionDialog2.takeDoubleInput(
				Messages.getString("TicketView.39"), Messages.getString("TicketView.40"), ticketItem.getDiscountAmount()); //$NON-NLS-1$ //$NON-NLS-2$
		if (Double.isNaN(d)) {
			return;
		}

		ticketItem.setDiscountAmount(d);
		ticketView.getTicketViewerTable().repaint();
		ticketView.updateView();*/
	}

	protected void doAddCookingInstruction() {

		try {
			Object object = ticketView.getTicketViewerTable().getSelected();
			if (!(object instanceof TicketItem)) {
				POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("TicketView.20")); //$NON-NLS-1$
				return;
			}

			TicketItem ticketItem = (TicketItem) object;

			if (ticketItem.isPrintedToKitchen()) {
				POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("TicketView.21")); //$NON-NLS-1$
				return;
			}

			CookingInstructionSelectionView dialog = new CookingInstructionSelectionView();
			dialog.setSize(800, 600);
			dialog.setLocationRelativeTo(Application.getPosWindow());
			dialog.setVisible(true);

			if (dialog.isCanceled()) {
				return;
			}

			List<TicketItemCookingInstruction> instructions = dialog.getTicketItemCookingInstructions();
			ticketItem.addCookingInstructions(instructions);

			ticketView.getTicketViewerTable().updateView();
		} catch (Exception e) {
			e.printStackTrace();
			POSMessageDialog.showError(e.getMessage());
		}
	}

	//	private void doAddAddOn() {
	//		Object object = ticketView.getTicketViewerTable().getSelected();
	//		if (!(object instanceof TicketItem)) {
	//			POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("TicketView.20")); //$NON-NLS-1$
	//			return;
	//		}
	//
	//		TicketItem ticketItem = (TicketItem) object;
	//
	//		if (!ticketItem.isHasModifiers()) {
	//			return;
	//		}
	//
	//		Integer itemId = ticketItem.getItemId();
	//		MenuItem menuItem = MenuItemDAO.getInstance().get(itemId);
	//		if (menuItem == null) {
	//			return;
	//		}
	//
	//		menuItem = MenuItemDAO.getInstance().initialize(menuItem);
	//		ModifierSelectionDialog dialog = new ModifierSelectionDialog(new ModifierSelectionModel(ticketItem, menuItem), true);
	//		dialog.open();
	//		ticketView.updateView();
	//	}

	public void actionUpdate() {

		if (currentTicket != null) {
			OrderType type = currentTicket.getOrderType();

			if (type.isPrepaid()) {
				btnDone.setVisible(false);
			}
			else {
				btnDone.setVisible(true);
			}

			if (!type.isShouldPrintToKitchen()) {
				btnSend.setEnabled(false);
			}
			else {
				btnSend.setEnabled(true);
			}

			if (!type.isAllowSeatBasedOrder()) {
				btnSeatNo.setVisible(false);
			}
			else {
				btnSeatNo.setVisible(true);
				int lastSeatNumber = getLastSeatNumber();
				btnSeatNo.putClientProperty("SEAT_NO", lastSeatNumber);
				if (lastSeatNumber > 0)
					btnSeatNo.setText("SEAT:" + lastSeatNumber);
				else
					btnSeatNo.setText("SEAT:");
			}

			if (!type.isShowTableSelection()) {
				btnGuestNo.setVisible(false);
				btnTableNumber.setVisible(false);
			}
			else {
				btnGuestNo.setVisible(true);
				btnTableNumber.setVisible(true);

				List<Integer> tableNumbers = currentTicket.getTableNumbers();

				if (tableNumbers != null) {

					String tables = getTableNumbers(currentTicket.getTableNumbers());

					btnTableNumber.setText("<html><center>" + "TABLE" + ": " + tables + "</center><html/>");

				}
				else {
					btnTableNumber.setText("TABLE");
				}

				btnGuestNo.setText("GUEST" + ": " + String.valueOf(currentTicket.getNumberOfGuests()));
			}
		}
	}

	private String getTableNumbers(List<Integer> numbers) {

		String tableNumbers = "";

		if (numbers != null && !numbers.isEmpty()) {
			for (Iterator iterator = numbers.iterator(); iterator.hasNext();) {
				Integer n = (Integer) iterator.next();
				tableNumbers += n;

				if (iterator.hasNext()) {
					tableNumbers += ", ";
				}
			}
			return tableNumbers;
		}
		return tableNumbers;
	}

	public void showView(final String viewName) {

		//scardLayout.show(midContainer, viewName);
		//getTicketView().txtSearchItem.requestFocus();
	}

	public com.floreantpos.ui.views.order.CategoryView getCategoryView() {
		return categoryView;
	}

	public void setCategoryView(com.floreantpos.ui.views.order.CategoryView categoryView) {
		this.categoryView = categoryView;
	}

	public GroupView getGroupView() {
		return groupView;
	}

	public void setGroupView(GroupView groupView) {
		this.groupView = groupView;
	}

	public MenuItemView getItemView() {
		return itemView;
	}

	public void setItemView(MenuItemView itemView) {
		this.itemView = itemView;
	}

	public com.floreantpos.ui.views.order.TicketView getTicketView() {
		return ticketView;
	}

	public void setTicketView(com.floreantpos.ui.views.order.TicketView ticketView) {
		this.ticketView = ticketView;
	}

	public OrderController getOrderController() {
		return orderController;
	}

	public Ticket getCurrentTicket() {
		return currentTicket;
	}

	public void setCurrentTicket(Ticket currentTicket) {
		this.currentTicket = currentTicket;
		ticketView.setTicket(currentTicket);
		actionUpdate();
		resetView();
	}

	public synchronized static OrderView getInstance() {
		if (instance == null) {
			instance = new OrderView();
		}
		return instance;
	}

	public void resetView() {
	}

	@Override
	public void setVisible(boolean aFlag) {
		if (aFlag) {
			try {
				categoryView.initialize();
			} catch (Throwable t) {
				POSMessageDialog.showError(Application.getPosWindow(), com.floreantpos.POSConstants.ERROR_MESSAGE, t);
			}
		}
		else {
			categoryView.cleanup();
		}
		super.setVisible(aFlag);
	}

	@Override
	public String getViewName() {
		return VIEW_NAME;
	}
}