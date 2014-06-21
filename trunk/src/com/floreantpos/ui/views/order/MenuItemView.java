/*
 * GroupView.java
 *
 * Created on August 5, 2006, 9:29 PM
 */

package com.floreantpos.ui.views.order;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.SwingConstants;

import com.floreantpos.PosException;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.swing.ImageIcon;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.views.order.actions.ItemSelectionListener;

/**
 * 
 * @author MShahriar
 */
public class MenuItemView extends SelectionView {
	public final static String VIEW_NAME = "ITEM_VIEW";

	private Vector<ItemSelectionListener> listenerList = new Vector<ItemSelectionListener>();

	private MenuGroup menuGroup;

	/** Creates new form GroupView */
	public MenuItemView() {
		super(com.floreantpos.POSConstants.ITEMS);
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

			for (int i = 0; i < items.size(); i++) {
				MenuItem menuItem = items.get(i);
				ItemButton itemButton = new ItemButton(menuItem);
				addButton(itemButton);
			}
			revalidate();
			repaint();
		} catch (PosException e) {
			e.printStackTrace();
		}
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
		private static final int BUTTON_SIZE = 120;
		MenuItem foodItem;

		ItemButton(MenuItem foodItem) {
			this.foodItem = foodItem;
			setVerticalTextPosition(SwingConstants.BOTTOM);
			setHorizontalTextPosition(SwingConstants.CENTER);
			
			if(foodItem.getImage() != null) {
				int w = BUTTON_SIZE - 10;
				int h = BUTTON_SIZE - 10;
				
				if(foodItem.isShowImageOnly()) {
					ImageIcon imageIcon = new ImageIcon(new ImageIcon(foodItem.getImage()).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
					setIcon(imageIcon);
				}
				else {
					w = 80;
					h = 80;
					
					ImageIcon imageIcon = new ImageIcon(new ImageIcon(foodItem.getImage()).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
					setIcon(imageIcon);
					setText(foodItem.getName());
				}
				
			}
			else {
				setText(foodItem.getName());
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
