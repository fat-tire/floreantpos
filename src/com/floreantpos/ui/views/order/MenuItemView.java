/*
 * GroupView.java
 *
 * Created on August 5, 2006, 9:29 PM
 */

package com.floreantpos.ui.views.order;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.SwingConstants;

import com.floreantpos.PosException;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.swing.ImageIcon;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.MiscTicketItemDialog;
import com.floreantpos.ui.views.order.actions.ItemSelectionListener;

/**
 * 
 * @author MShahriar
 */
public class MenuItemView extends SelectionView {
	public final static String VIEW_NAME = "ITEM_VIEW";

	private Vector<ItemSelectionListener> listenerList = new Vector<ItemSelectionListener>();

	private MenuGroup menuGroup;
	private PosButton btnMisc = new PosButton(com.floreantpos.POSConstants.MISC);

	/** Creates new form GroupView */
	public MenuItemView() {
		super(com.floreantpos.POSConstants.ITEMS);
		
		btnMisc.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				MiscTicketItemDialog dialog = new MiscTicketItemDialog();
				dialog.open();
				if (!dialog.isCanceled()) {
					TicketItem ticketItem = dialog.getTicketItem();
					RootView.getInstance().getOrderView().getTicketView().addTicketItem(ticketItem);
				}
			}
		});
		
		setBackEnable(false);
	}

	public MenuGroup getMenuGroup() {
		return menuGroup;
	}

	public void setMenuGroup(MenuGroup menuGroup) {
		this.menuGroup = menuGroup;

		reset();

		if (menuGroup == null) {
			return;
		}

		MenuItemDAO dao = new MenuItemDAO();
		try {
			List<MenuItem> items = dao.findByParent(menuGroup, false);
			setBackEnable(items.size() > 0);
			
			setItems(items);
			
//			for (int i = 0; i < items.size(); i++) {
//				MenuItem menuItem = items.get(i);
//				ItemButton itemButton = new ItemButton(menuItem);
//				addButton(itemButton);
//			}
//			revalidate();
//			repaint();
		} catch (PosException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected AbstractButton createItemButton(Object item) {
		MenuItem menuItem = (MenuItem) item;
		ItemButton itemButton = new ItemButton(menuItem);
		
		return itemButton;
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

	private void fireBackFromItemSelected() {
		for (ItemSelectionListener listener : listenerList) {
			listener.itemSelectionFinished(menuGroup);
		}
	}

	private class ItemButton extends PosButton implements ActionListener {
		private static final int BUTTON_SIZE = 100;
		MenuItem foodItem;

		ItemButton(MenuItem menuItem) {
			this.foodItem = menuItem;
			setVerticalTextPosition(SwingConstants.BOTTOM);
			setHorizontalTextPosition(SwingConstants.CENTER);
			
			if(menuItem.getImage() != null) {
				int w = BUTTON_SIZE - 10;
				int h = BUTTON_SIZE - 10;
				
				if(menuItem.isShowImageOnly()) {
					ImageIcon imageIcon = new ImageIcon(new ImageIcon(menuItem.getImage()).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
					setIcon(imageIcon);
				}
				else {
					w = 80;
					h = 80;
					
					ImageIcon imageIcon = new ImageIcon(new ImageIcon(menuItem.getImage()).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
					setIcon(imageIcon);
					setText("<html><body><center>" + menuItem.getDisplayName() + "</center></body></html>");
				}
				
			}
			else {
				setText("<html><body><center>" + menuItem.getName() + "</center></body></html>");
			}
			
			if(menuItem.getButtonColor() != null) {
				setBackground(new Color(menuItem.getButtonColor()));
			}
			if(menuItem.getTextColor() != null) {
				setForeground(new Color(menuItem.getTextColor()));
			}
			
			setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			fireItemSelected(foodItem);
		}
	}

	@Override
	public void doGoBack() {
		fireBackFromItemSelected();
	}
}
