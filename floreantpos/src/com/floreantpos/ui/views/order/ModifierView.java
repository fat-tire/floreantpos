/*
 * GroupView.java
 *
 * Created on August 5, 2006, 9:29 PM
 */

package com.floreantpos.ui.views.order;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.floreantpos.PosException;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.order.actions.ModifierSelectionListener;

/**
 *
 * @author  MShahriar
 */
public class ModifierView extends SelectionView {
	private Vector<ModifierSelectionListener> listenerList = new Vector<ModifierSelectionListener>();

	private MenuItem parentMenuItem;
	private TicketItem parentTicketItem;

	private HashMap<String, ModifierButton> buttonMap = new HashMap<String, ModifierButton>();
	
//	private final static ImageIcon normalIcon = IconFactory.getIcon("normalModifier16.png");
//	private final static ImageIcon noIcon = IconFactory.getIcon("noModifier16.png");
//	private final static ImageIcon extraIcon = IconFactory.getIcon("extraModifier16.png");

	public static final String VIEW_NAME = "MODIFIER_VIEW";

	/** Creates new form GroupView */
	public ModifierView() {
		super(com.floreantpos.POSConstants.MODIFIERS);
	}


	public MenuItem getMenuItem() {
		return parentMenuItem;
	}

	public void setMenuItem(MenuItem menuItem, TicketItem ticketItem) {
		this.parentMenuItem = menuItem;
		this.parentTicketItem = ticketItem;
		
		reset();

		//MenuItemDAO dao = new MenuItemDAO();
		try {
			List<MenuItemModifierGroup> menuItemModifierGroups = menuItem.getMenuItemModiferGroups();

			for (Iterator<MenuItemModifierGroup> iter = menuItemModifierGroups.iterator(); iter.hasNext();) {
				MenuItemModifierGroup menuItemModifierGroup = iter.next();
				MenuModifierGroup group = menuItemModifierGroup.getModifierGroup();
				addSeparator(group.getName());

				Set<MenuModifier> modifiers = group.getModifiers();
				for (MenuModifier modifier : modifiers) {
					modifier.setMenuItemModifierGroup(menuItemModifierGroup);
					ModifierButton modifierButton = new ModifierButton(modifier);
					String key = modifier.getId() + "_" + modifier.getModifierGroup().getId();
					buttonMap.put(key, modifierButton);
					addButton(modifierButton);
				}
			}
			revalidate();
			updateVisualRepresentation();
		} catch (PosException e) {
			POSMessageDialog.showError(this, com.floreantpos.POSConstants.ERROR_MESSAGE, e);
		}
	}

	public void addModifierSelectionListener(ModifierSelectionListener listener) {
		listenerList.add(listener);
	}

	public void removeModifierSelectionListener(ModifierSelectionListener listener) {
		listenerList.remove(listener);
	}

	private void fireModifierSelectionFinished() {
		for (ModifierSelectionListener listener : listenerList) {
			listener.modifierSelectionFiniched(parentMenuItem);
		}
	}
	
	

	public void updateVisualRepresentation() {
//		Collection<ModifierButton> modifierButtons = buttonMap.values();
//		for (ModifierButton modifierButton : modifierButtons) {
//			modifierButton.updateView(null);
//		}

		List<TicketItemModifierGroup> ticketItemModifierGroups = parentTicketItem.getTicketItemModifierGroups();
		if (ticketItemModifierGroups != null) {
			for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
				List<TicketItemModifier> ticketItemModifiers = ticketItemModifierGroup.getTicketItemModifiers();
				if (ticketItemModifiers != null) {
					int total = 0;
					int max = ticketItemModifierGroup.getMaxQuantity();
					for (TicketItemModifier ticketItemModifier : ticketItemModifiers) {
						String key = ticketItemModifier.getItemId() + "_" + ticketItemModifier.getGroupId();
						ModifierButton button = buttonMap.get(key);
						if (ticketItemModifier.getModifierType() != TicketItemModifier.NO_MODIFIER) {
							total += ticketItemModifier.getItemCount();
							if (total > max) {
								ticketItemModifier.setModifierType(TicketItemModifier.EXTRA_MODIFIER);
							}
							else {
								ticketItemModifier.setModifierType(TicketItemModifier.NORMAL_MODIFIER);
							}
						}
						button.updateView(ticketItemModifier);
					}
				}
			}
		}
	}

	public TicketItem getParentTicketItem() {
		return parentTicketItem;
	}

	@Override
	public void doGoBack() {
		List<MenuItemModifierGroup> menuItemModifierGroups = parentMenuItem.getMenuItemModiferGroups();
		List<TicketItemModifierGroup> ticketItemModifierGroups = parentTicketItem.getTicketItemModifierGroups();

		boolean requiredModifierAdded = true;
		if (menuItemModifierGroups != null) {
			outer: for (MenuItemModifierGroup menuItemModifierGroup : menuItemModifierGroups) {
				int minQuantity = menuItemModifierGroup.getMinQuantity();
				if (minQuantity == 0)
					continue;

				if (ticketItemModifierGroups == null) {
					requiredModifierAdded = false;
					break outer;
				}

				for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
					if (ticketItemModifierGroup.countItems(false) < minQuantity) {
						requiredModifierAdded = false;
						break outer;
					}
				}
			}
		}

		if (!requiredModifierAdded) {
			int option = JOptionPane.showConfirmDialog(this, com.floreantpos.POSConstants.REQUIRED_MODIFIERS_NOT_ADDED, com.floreantpos.POSConstants.CONFIRM, JOptionPane.YES_NO_OPTION);
			if (option != JOptionPane.YES_OPTION) {
				return;
			}
		}

		fireModifierSelectionFinished();
	}
	
	private class ModifierButton extends PosButton implements ActionListener {
		private MenuModifier menuModifier;

		public ModifierButton(MenuModifier modifier) {
			this.menuModifier = modifier;
			
			setText(modifier.getName());
			addActionListener(this);
		}

		void updateView(TicketItemModifier ticketItemModifier) {
			if (ticketItemModifier == null || ticketItemModifier.getModifierType() == TicketItemModifier.MODIFIER_NOT_INITIALIZED) {
				setBackground(null);
				//setIcon(null);
				return;
			}

			if (ticketItemModifier.getModifierType() == TicketItemModifier.NORMAL_MODIFIER) {
				//setIcon(normalIcon);
				setBackground(Color.GREEN.darker());
			}
			else if (ticketItemModifier.getModifierType() == TicketItemModifier.NO_MODIFIER) {
				//setIcon(noIcon);
				setBackground(Color.RED.darker());
			}
			else if (ticketItemModifier.getModifierType() == TicketItemModifier.EXTRA_MODIFIER) {
				//setIcon(extraIcon);
				setBackground(Color.ORANGE);
			}
		}
		
		public void actionPerformed(ActionEvent e) {
			TicketItemModifierGroup ticketItemModifierGroup = parentTicketItem.findTicketItemModifierGroup(menuModifier, true);
			int modifierCount = ticketItemModifierGroup.countItems(true);
			int maxModifier = ticketItemModifierGroup.getMaxQuantity();

			TicketItemModifier ticketItemModifier = ticketItemModifierGroup.findTicketItemModifier(menuModifier);
			TicketView ticketView = OrderView.getInstance().getTicketView();

			if (ticketItemModifier == null) {
				TicketItemModifier m = ticketItemModifierGroup.addTicketItemModifier(menuModifier, modifierCount >= maxModifier ? TicketItemModifier.EXTRA_MODIFIER : TicketItemModifier.NORMAL_MODIFIER);
				updateView(m);
				ticketView.updateAllView();
				ticketView.selectRow(m.getTableRowNum());
				return;
			}
			
			int modifierType = TicketItemModifier.MODIFIER_NOT_INITIALIZED;
			if(ticketItemModifier.getModifierType() != null) {
				modifierType = ticketItemModifier.getModifierType().intValue();
			}
			switch (modifierType) {
				case TicketItemModifier.MODIFIER_NOT_INITIALIZED:
					ticketItemModifier.setModifierType(TicketItemModifier.NORMAL_MODIFIER);
					updateVisualRepresentation();
					ticketView.updateAllView();
					ticketView.selectRow(ticketItemModifier.getTableRowNum());
					break;

				case TicketItemModifier.NORMAL_MODIFIER:
				case TicketItemModifier.EXTRA_MODIFIER:
					ticketItemModifier.setModifierType(TicketItemModifier.NO_MODIFIER);
					updateVisualRepresentation();
					ticketView.updateAllView();
					ticketView.selectRow(ticketItemModifier.getTableRowNum());
					break;

				case TicketItemModifier.NO_MODIFIER:
					ticketItemModifier.setModifierType(TicketItemModifier.MODIFIER_NOT_INITIALIZED);
					ticketItemModifierGroup.removeTicketItemModifier(ticketItemModifier);
					updateView(ticketItemModifier);
					updateVisualRepresentation();
					ticketView.updateAllView();
					ticketView.selectRow(ticketItemModifier.getTableRowNum() - 1);
					break;
			}
		}
	}
}
