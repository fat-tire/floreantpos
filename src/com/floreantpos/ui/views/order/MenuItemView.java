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
 * GroupView.java
 *
 * Created on August 5, 2006, 9:29 PM
 */

package com.floreantpos.ui.views.order;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.SwingConstants;

import com.floreantpos.IconFactory;
import com.floreantpos.PosException;
import com.floreantpos.PosLog;
import com.floreantpos.bo.ui.explorer.QuickMaintenanceExplorer;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.ui.views.order.actions.ItemSelectionListener;
import com.floreantpos.util.CurrencyUtil;

/**
 * 
 * @author MShahriar
 */
public class MenuItemView extends SelectionView {
	public final static String VIEW_NAME = "ITEM_VIEW"; //$NON-NLS-1$

	private Vector<ItemSelectionListener> listenerList = new Vector<ItemSelectionListener>();

	private MenuGroup menuGroup;
	private Map<Integer, ItemButton> menuItemButtonMap = new HashMap<Integer, MenuItemView.ItemButton>();
	private boolean showPrice;
	private boolean showStockCount;

	/** Creates new form GroupView */
	public MenuItemView() {
		super(com.floreantpos.POSConstants.ITEMS, PosUIManager.getSize(120), PosUIManager.getSize(80));
		remove(actionButtonPanel);

		btnPrev.setText("<");
		btnNext.setText(">");

		add(btnPrev, BorderLayout.WEST);
		add(btnNext, BorderLayout.EAST);
	}

	public MenuGroup getMenuGroup() {
		return menuGroup;
	}

	public void setMenuGroup(MenuGroup menuGroup) {
		this.menuGroup = menuGroup;

		menuItemButtonMap.clear();

		if (menuGroup == null) {
			setItems(null);
			return;
		}
		OrderType orderType = OrderView.getInstance().getCurrentTicket().getOrderType();
		showPrice = orderType.isShowPriceOnButton();
		showStockCount = orderType.isShowStockCountOnButton();
		MenuItemDAO dao = new MenuItemDAO();
		try {
			List<MenuItem> items = new ArrayList<>();
			if (menuGroup.getId() != null) {
				items = dao.findByParent(Application.getInstance().getTerminal(), menuGroup, orderType, false);
			}
			if (RootView.getInstance().isMaintenanceMode()) {
				MenuItem newMenuItem = new MenuItem(null, "", 0.0, 0.0);
				newMenuItem.setParent(menuGroup);
				items.add(newMenuItem);
			}
			// filterItemsByOrderType(items);
			setItems(items);
		} catch (PosException e) {
			PosLog.error(getClass(), e);
		}
	}

	@Override
	protected AbstractButton createItemButton(Object item) {
		MenuItem menuItem = (MenuItem) item;

		ItemButton itemButton = new ItemButton(menuItem);
		menuItemButtonMap.put(menuItem.getId(), itemButton);

		filterByStockAmount(menuItem, itemButton);
		setInitialized(true);
		return itemButton;
	}

	public void updateView(MenuItem menuItem) {
		setMenuGroup(menuItem.getParent());
	}

	public void addItemSelectionListener(ItemSelectionListener listener) {
		listenerList.add(listener);
	}

	public void removeItemSelectionListener(ItemSelectionListener listener) {
		listenerList.remove(listener);
	}

	private void fireItemSelected(MenuItem foodItem) {
		for (ItemSelectionListener listener : listenerList) {
			listener.itemSelected(foodItem);
		}
	}

	public void selectItem(MenuItem menuItem) {
		// ItemButton button = menuItemButtonMap.get(menuItem.getId());
		/*
		 * if(button != null) { button.requestFocus(); }
		 */
	}

	private void filterItemsByOrderType(List<MenuItem> items) {
		String orderType = OrderView.getInstance().getTicketView().getTicket().getOrderType().toString();
		for (Iterator iterator = items.iterator(); iterator.hasNext();) {
			MenuItem menuItem = (MenuItem) iterator.next();
			List<OrderType> orderTypeList = menuItem.getOrderTypeList();

			if (orderTypeList == null || orderTypeList.size() == 0) {
				continue;
			}

			if (!orderTypeList.contains(orderType)) {
				iterator.remove();
			}
		}
	}

	private void filterByStockAmount(MenuItem menuItem, ItemButton itemButton) {
		if (menuItem.isDisableWhenStockAmountIsZero() && menuItem.getStockAmount() <= 0) {
			itemButton.setEnabled(false);
		}
	}

	public class ItemButton extends PosButton implements ActionListener, MouseListener {
		private int BUTTON_SIZE = 100;
		MenuItem foodItem;

		ItemButton(MenuItem menuItem) {
			setFocusable(false);
			setVerticalTextPosition(SwingConstants.BOTTOM);
			setHorizontalTextPosition(SwingConstants.CENTER);
			BUTTON_SIZE = PosUIManager.getSize(100);

			updateView(menuItem);

			setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
			addActionListener(this);
			addMouseListener(this);
		}

		private void updateView(MenuItem menuItem) {
			this.foodItem = menuItem;
			if (menuItem.getImage() != null) {
				int w = BUTTON_SIZE - PosUIManager.getSize(10);
				int h = BUTTON_SIZE - PosUIManager.getSize(10);

				if (menuItem.isShowImageOnly()) {
					setIcon(menuItem.getScaledImage(w, h));
				}
				else {
					w = PosUIManager.getSize(80);
					h = PosUIManager.getSize(40);

					setIcon(menuItem.getScaledImage(w, h));
					setText("<html><body><center><span style='font-size:7px;margin:0px;'>" + menuItem.getDisplayName() + "</span></center></body></html>"); //$NON-NLS-1$ //$NON-NLS-2$
				}

			}
			else {
				if (menuItem.getId() == null) {
					setIcon(IconFactory.getIcon("/ui_icons/", "add+user.png"));
				}
				else
					setText("<html><body><center>"
							+ menuItem.getName()
							+ (!showPrice ? "" : "<br><h4>" + "<span style='color:white;background-color:green;margin:1;'>" + "&nbsp;" + "&nbsp;" + "&nbsp;"
									+ CurrencyUtil.getCurrencySymbol() + menuItem.getPrice() + "&nbsp;" + "&nbsp;" + "&nbsp;" + "</span>")
							+ "&nbsp;"
							+ (!showStockCount ? "" : "<span style='color:white;background-color:red;margin:1;'>" + "&nbsp;" + "&nbsp;" + "&nbsp;"
									+ menuItem.getStockAmount() + "&nbsp;" + "&nbsp;" + "&nbsp;" + "</span>") + "</h4>" + "</center></body></html>"); //$NON-NLS-1$ //$NON-NLS-2$
			}

			Color buttonColor = menuItem.getButtonColor();
			if (buttonColor != null) {
				setBackground(buttonColor);
			}
			Color textColor = menuItem.getTextColor();
			if (textColor != null) {
				setForeground(textColor);
			}
		}

		public void actionPerformed(ActionEvent e) {
			if (OrderView.getInstance().isVisible() && RootView.getInstance().isMaintenanceMode()) {
				foodItem = MenuItemDAO.getInstance().initialize(foodItem);
				QuickMaintenanceExplorer.quickMaintain(foodItem);
			}
			fireItemSelected(foodItem);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
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
	}

	public void disableItemButton(MenuItem item) {
		ItemButton itemButton = menuItemButtonMap.get(item.getId());
		itemButton.setEnabled(false);

	}
}
