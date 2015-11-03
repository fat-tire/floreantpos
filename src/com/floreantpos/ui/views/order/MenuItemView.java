/*
 * GroupView.java
 *
 * Created on August 5, 2006, 9:29 PM
 */

package com.floreantpos.ui.views.order;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.SwingConstants;

import com.floreantpos.PosException;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.views.order.actions.ItemSelectionListener;

/**
 * 
 * @author MShahriar
 */
public class MenuItemView extends SelectionView {
	public final static String VIEW_NAME = "ITEM_VIEW"; //$NON-NLS-1$

	private Vector<ItemSelectionListener> listenerList = new Vector<ItemSelectionListener>();

	private MenuGroup menuGroup;
	private Map<Integer, ItemButton> menuItemButtonMap = new HashMap<Integer, MenuItemView.ItemButton>();
	
	/** Creates new form GroupView */
	public MenuItemView() {
		super(com.floreantpos.POSConstants.ITEMS);
		setBackEnable(false);
	}

	public MenuGroup getMenuGroup() {
		return menuGroup;
	}

	public void setMenuGroup(MenuGroup menuGroup) {
		this.menuGroup = menuGroup;

		reset();
		menuItemButtonMap.clear();
		
		if (menuGroup == null) {
			return;
		}

		MenuItemDAO dao = new MenuItemDAO();
		try {
			List<MenuItem> items = dao.findByParent(menuGroup, false);
			setBackEnable(items.size() > 0);
			
			setItems(items);
		} catch (PosException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected AbstractButton createItemButton(Object item) {
		MenuItem menuItem = (MenuItem) item;
		ItemButton itemButton = new ItemButton(menuItem);
		menuItemButtonMap.put(menuItem.getId(), itemButton);
		
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
	
	public void selectItem(MenuItem menuItem) {
		ItemButton button = menuItemButtonMap.get(menuItem.getId());
		if(button != null) {
			button.requestFocus();
		}
	}

	private class ItemButton extends PosButton implements ActionListener {
		private static final int BUTTON_SIZE = 100;
		MenuItem foodItem;

		ItemButton(MenuItem menuItem) {
			this.foodItem = menuItem;
			setFocusable(true);
			setFocusPainted(true);
			setVerticalTextPosition(SwingConstants.BOTTOM);
			setHorizontalTextPosition(SwingConstants.CENTER);
			
			if(menuItem.getImage() != null) {
				int w = BUTTON_SIZE - 10;
				int h = BUTTON_SIZE - 10;
				
				if(menuItem.isShowImageOnly()) {
					setIcon(menuItem.getScaledImage(w, h));
				}
				else {
					w = 80;
					h = 40;
					
					setIcon(menuItem.getScaledImage(w, h));
					setText("<html><body><center>" + menuItem.getDisplayName() + "</center></body></html>"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				
			}
			else {
				setText("<html><body><center>" + menuItem.getName() + "</center></body></html>"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			
			Color buttonColor = menuItem.getButtonColor();
			if(buttonColor != null) {
				setBackground(buttonColor);
			}
			Color textColor = menuItem.getTextColor();
			if(textColor != null) {
				setForeground(textColor);
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
