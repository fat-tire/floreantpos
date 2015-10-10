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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
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
import com.floreantpos.util.ModifierStateChangeListener;

/**
 *
 * @author  MShahriar
 */
public class ModifierView extends SelectionView implements ModifierStateChangeListener {
	private Vector<ModifierSelectionListener> listenerList = new Vector<ModifierSelectionListener>();

	private MenuItem parentMenuItem;
	private TicketItem parentTicketItem;

	private HashMap<String, ModifierButton> buttonMap = new HashMap<String, ModifierButton>();

	//	private final static ImageIcon normalIcon = IconFactory.getIcon("normalModifier16.png");
	//	private final static ImageIcon noIcon = IconFactory.getIcon("noModifier16.png");
	//	private final static ImageIcon extraIcon = IconFactory.getIcon("extraModifier16.png");

	private int separatorCount;

	public static final String VIEW_NAME = "MODIFIER_VIEW"; //$NON-NLS-1$

	private ModifierButton currentSelectedButton;

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
		buttonMap.clear();
		separatorCount = 0;

		try {
			List<MenuItemModifierGroup> menuItemModifierGroups = menuItem.getMenuItemModiferGroups();

			List itemList = new ArrayList();

			for (Iterator<MenuItemModifierGroup> iter = menuItemModifierGroups.iterator(); iter.hasNext();) {
				MenuItemModifierGroup menuItemModifierGroup = iter.next();
				MenuModifierGroup group = menuItemModifierGroup.getModifierGroup();

				itemList.add(group.getName());

				Set<MenuModifier> modifiers = group.getModifiers();
				for (MenuModifier modifier : modifiers) {
					modifier.setMenuItemModifierGroup(menuItemModifierGroup);
				}

				itemList.addAll(modifiers);
			}

			setItems(itemList);

		} catch (PosException e) {
			POSMessageDialog.showError(this, com.floreantpos.POSConstants.ERROR_MESSAGE, e);
		}
	}

	protected void renderItems() {
		reset();

		if (this.items == null || items.size() == 0) {
			return;
		}

		Dimension size = buttonsPanel.getSize();
		Dimension itemButtonSize = getButtonSize();

		int horizontalButtonCount = getButtonCount(size.width, getButtonSize().width);
		int verticalButtonCount = getButtonCount(size.height, getButtonSize().height);

		buttonsPanel.setLayout(new MigLayout("alignx 50%, wrap " + horizontalButtonCount)); //$NON-NLS-1$

		//TODO: REVISE CODE
		int totalItem = horizontalButtonCount * verticalButtonCount;

		previousBlockIndex = currentBlockIndex - totalItem + separatorCount;
		nextBlockIndex = currentBlockIndex + totalItem;

		int spCount = getSeparatorCount();

		if (spCount > 0) {
			verticalButtonCount = getButtonCount(size.height - (spCount * 40), getButtonSize().height);

			totalItem = horizontalButtonCount * verticalButtonCount;
			previousBlockIndex = (currentBlockIndex - totalItem) + spCount;
			nextBlockIndex = currentBlockIndex + totalItem + spCount;
		}

		try {
			for (int i = currentBlockIndex; i < nextBlockIndex; i++) {

				Object item = items.get(i);

				if (item instanceof String) {
					addSeparator(item.toString());
					continue;
				}

				AbstractButton itemButton = createItemButton(item);
				buttonsPanel.add(itemButton, "width " + itemButtonSize.width + "!, height " + itemButtonSize.height + "!"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

				if (i == items.size() - 1) {
					break;
				}
			}
		} catch (Exception e) {
			// TODO: fix it.
		}

		if (previousBlockIndex >= 0 && currentBlockIndex != 0) {
			btnPrev.setEnabled(true);
		}

		if (nextBlockIndex < items.size()) {
			btnNext.setEnabled(true);
		}
		separatorCount = spCount;
		//		revalidate();
		//		repaint();
		updateVisualRepresentation();
	}

	protected int getSeparatorCount() {
		if (!(this instanceof ModifierView)) {
			return 0;
		}

		int count = 0;
		for (int i = currentBlockIndex; i < nextBlockIndex; i++) {
			if (i == items.size() - 1) {
				break;
			}

			if (items.get(i) instanceof String) {
				++count;
			}
		}
		return count;
	}

	@Override
	protected AbstractButton createItemButton(Object item) {
		MenuModifier modifier = (MenuModifier) item;
		ModifierButton modifierButton = new ModifierButton(modifier);
		String key = modifier.getId() + "_" + modifier.getModifierGroup().getId(); //$NON-NLS-1$
		buttonMap.put(key, modifierButton);

		return modifierButton;
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
		List<TicketItemModifierGroup> ticketItemModifierGroups = parentTicketItem.getTicketItemModifierGroups();
		if (ticketItemModifierGroups != null) {
			for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
				List<TicketItemModifier> ticketItemModifiers = ticketItemModifierGroup.getTicketItemModifiers();
				if (ticketItemModifiers != null) {
					int total = 0;
					int max = ticketItemModifierGroup.getMaxQuantity();
					for (TicketItemModifier ticketItemModifier : ticketItemModifiers) {
						String key = ticketItemModifier.getItemId() + "_" + ticketItemModifier.getGroupId(); //$NON-NLS-1$
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

		boolean requiredModifierAdded = isRequiredModifiersAdded(menuItemModifierGroups, ticketItemModifierGroups);

		if (!requiredModifierAdded) {
			int option = JOptionPane.showConfirmDialog(this, com.floreantpos.POSConstants.REQUIRED_MODIFIERS_NOT_ADDED, com.floreantpos.POSConstants.CONFIRM,
					JOptionPane.YES_NO_OPTION);
			if (option != JOptionPane.YES_OPTION) {
				return;
			}
		}

		fireModifierSelectionFinished();
	}

	public boolean isRequiredModifiersAdded(List<MenuItemModifierGroup> menuItemModifierGroups, List<TicketItemModifierGroup> ticketItemModifierGroups) {
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
		return requiredModifierAdded;
	}

	private class ModifierButton extends PosButton implements ActionListener {
		private MenuModifier menuModifier;

		public ModifierButton(MenuModifier modifier) {
			this.menuModifier = modifier;

			setText("<html><center>" + modifier.getDisplayName() + "</center></html>"); //$NON-NLS-1$ //$NON-NLS-2$

			if (modifier.getButtonColor() != null) {
				setBackground(new Color(modifier.getButtonColor()));
			}

			if (modifier.getTextColor() != null) {
				setForeground(new Color(modifier.getTextColor()));
			}

			setFocusable(true);
			setFocusPainted(true);
			addActionListener(this);
		}

		void updateView(TicketItemModifier ticketItemModifier) {
			Integer itemCount = ticketItemModifier.getItemCount();

			String text = menuModifier.getDisplayName();
			String style = ""; //$NON-NLS-1$

			if (ticketItemModifier == null || ticketItemModifier.getModifierType() == TicketItemModifier.MODIFIER_NOT_INITIALIZED) {
			}
			else if (ticketItemModifier.getModifierType() == TicketItemModifier.NORMAL_MODIFIER) {
				style = "color: green;"; //$NON-NLS-1$
			}
			//			else if (ticketItemModifier.getModifierType() == TicketItemModifier.NO_MODIFIER) {
			//				//setIcon(noIcon);
			//				setBackground(Color.RED.darker());
			//			}
			else if (ticketItemModifier.getModifierType() == TicketItemModifier.EXTRA_MODIFIER) {
				style = "color: red;"; //$NON-NLS-1$
			}

			StringBuilder sb = new StringBuilder();
			sb.append("<html>"); //$NON-NLS-1$
			sb.append("<center>"); //$NON-NLS-1$
			sb.append(text);

			if (itemCount != 0) {
				sb.append("<h2 style='" + style + "'>"); //$NON-NLS-1$ //$NON-NLS-2$
				sb.append("(" + itemCount + ")"); //$NON-NLS-1$ //$NON-NLS-2$
				sb.append("</h2>"); //$NON-NLS-1$
			}
			sb.append("</center>"); //$NON-NLS-1$
			sb.append("</html>"); //$NON-NLS-1$

			setText(sb.toString());
		}

		public void actionPerformed(ActionEvent e) {
			TicketItemModifierGroup ticketItemModifierGroup = parentTicketItem.findTicketItemModifierGroup(menuModifier, true);
			int modifierCount = ticketItemModifierGroup.countItems(true);
			int maxModifier = ticketItemModifierGroup.getMaxQuantity();

			TicketItemModifier ticketItemModifier = ticketItemModifierGroup.findTicketItemModifier(menuModifier);
			TicketView ticketView = OrderView.getInstance().getTicketView();

			if (ticketItemModifier == null) {
				TicketItemModifier m = ticketItemModifierGroup.addTicketItemModifier(menuModifier,
						modifierCount >= maxModifier ? TicketItemModifier.EXTRA_MODIFIER : TicketItemModifier.NORMAL_MODIFIER);
				updateView(m);
				ticketView.updateAllView();
				ticketView.selectRow(m.getTableRowNum());
				return;
			}

			int modifierType = TicketItemModifier.MODIFIER_NOT_INITIALIZED;
			if (ticketItemModifier.getModifierType() != null) {
				modifierType = ticketItemModifier.getModifierType().intValue();
			}
			switch (modifierType) {
				case TicketItemModifier.MODIFIER_NOT_INITIALIZED:
					ticketItemModifier.setItemCount(ticketItemModifier.getItemCount() + 1);
					ticketItemModifier.setModifierType(TicketItemModifier.NORMAL_MODIFIER);
					updateVisualRepresentation();
					ticketView.updateAllView();
					ticketView.selectRow(ticketItemModifier.getTableRowNum());
					break;

				case TicketItemModifier.NORMAL_MODIFIER:
				case TicketItemModifier.EXTRA_MODIFIER:
					ticketItemModifier.setItemCount(ticketItemModifier.getItemCount() + 1);
					updateVisualRepresentation();
					ticketView.updateAllView();
					ticketView.selectRow(ticketItemModifier.getTableRowNum());
					break;

			//				case TicketItemModifier.NO_MODIFIER:
			//					ticketItemModifier.setModifierType(TicketItemModifier.MODIFIER_NOT_INITIALIZED);
			//					ticketItemModifierGroup.removeTicketItemModifier(ticketItemModifier);
			//					//updateView(ticketItemModifier);
			//					updateVisualRepresentation();
			//					ticketView.updateAllView();
			//					ticketView.selectRow(ticketItemModifier.getTableRowNum() - 1);
			//					break;
			}
		}
	}

	@Override
	public void modifierStateChanged() {
		updateVisualRepresentation();
	}

	@Override
	public void updateView(TicketItemModifier modifier) {
		String key = modifier.getItemId() + "_" + modifier.getGroupId(); //$NON-NLS-1$
		ModifierButton modifierButton = buttonMap.get(key);
		if (modifierButton == null) {
			return;
		}

		modifierButton.updateView(modifier);
	}
	
	@Override
	public void select(TicketItemModifier modifier) {
		String key = modifier.getItemId() + "_" + modifier.getGroupId(); //$NON-NLS-1$
		ModifierButton modifierButton = buttonMap.get(key);
		if (modifierButton == null) {
			return;
		}
		
		if(currentSelectedButton != null) {
			currentSelectedButton.setBorder(UIManager.getBorder("Button.border")); //$NON-NLS-1$
		}
		currentSelectedButton = modifierButton;
		//modifierButton.setBorder(BorderFactory.createLineBorder(Color.blue.brighter(), 2));
		modifierButton.requestFocus(true);
	}
	
	@Override
	public void clearSelection() {
		if(currentSelectedButton != null) {
			currentSelectedButton.setBorder(UIManager.getBorder("Button.border")); //$NON-NLS-1$
		}
		currentSelectedButton = null;
	}
}
