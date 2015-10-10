/*
 * CategoryView.java
 *
 * Created on August 5, 2006, 2:21 AM
 */

package com.floreantpos.ui.views.order;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

import org.apache.log4j.Logger;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.order.actions.CategorySelectionListener;

/**
 *
 * @author  MShahriar
 */
public class CategoryView extends SelectionView implements ActionListener {
	private Vector<CategorySelectionListener> listenerList = new Vector<CategorySelectionListener>();
	//private CardLayout cardLayout = new CardLayout();

	private ButtonGroup categoryButtonGroup;
	private Map<String, CategoryButton> buttonMap = new HashMap<String, CategoryButton>();
	
	public static final String VIEW_NAME = "CATEGORY_VIEW"; //$NON-NLS-1$
	
	//private int panelCount = 0;
	
	/** Creates new form CategoryView */
	public CategoryView() {
		super(com.floreantpos.POSConstants.CATEGORIES, 100, 80);
		
		setBackVisible(false);
		
		categoryButtonGroup = new ButtonGroup();
		setPreferredSize(new Dimension(120, 100));
	}

	public void initialize() {
		reset();
		
		MenuCategoryDAO categoryDAO = new MenuCategoryDAO();
		List<MenuCategory> categories = categoryDAO.findAllEnable();
		if(categories.size() == 0) return;
		
		setItems(categories);
		
		CategoryButton categoryButton = (CategoryButton) buttonsPanel.getComponent(0);
		if(categoryButton != null) {
			categoryButton.setSelected(true);
			fireCategorySelected(categoryButton.foodCategory);
		}
	}
	
	@Override
	protected AbstractButton createItemButton(Object item) {
		MenuCategory menuCategory = (MenuCategory) item;
		
		CategoryButton button = new CategoryButton(this,menuCategory);
		categoryButtonGroup.add(button);
		
		buttonMap.put(String.valueOf(menuCategory.getId()), button);
		
		return button;
	}
	
	public void addCategorySelectionListener(CategorySelectionListener listener) {
		listenerList.add(listener);
	}
	
	public void removeCategorySelectionListener(CategorySelectionListener listener) {
		listenerList.remove(listener);
	}
	
	private void fireCategorySelected(MenuCategory foodCategory) {
		for (CategorySelectionListener listener : listenerList) {
			listener.categorySelected(foodCategory);
		}
	}
	
	public void setSelectedCategory(MenuCategory category) {
		CategoryButton button = buttonMap.get(String.valueOf(category.getId()));
		if(button != null) {
			button.setSelected(true);
		}
	}
	
	private static class CategoryButton extends POSToggleButton {
		MenuCategory foodCategory;
		
		CategoryButton(CategoryView view, MenuCategory menuCategory) {
			this.foodCategory = menuCategory;
			setText("<html><body><center>" + menuCategory.getDisplayName() + "</center></body></html>"); //$NON-NLS-1$ //$NON-NLS-2$
			
			if(menuCategory.getButtonColor() != null) {
				setBackground(new Color(menuCategory.getButtonColor()));
			}
			if(menuCategory.getTextColor() != null) {
				setForeground(new Color(menuCategory.getTextColor()));
			}
			
			addActionListener(view);
		}
	}
	

	public void actionPerformed(ActionEvent e) {
		ModifierView modifierView = OrderView.getInstance().getModifierView();
		
		if(modifierView.isShowing()) {
			List<MenuItemModifierGroup> menuItemModifierGroups = modifierView.getMenuItem().getMenuItemModiferGroups();
			List<TicketItemModifierGroup> ticketItemModifierGroups = modifierView.getParentTicketItem().getTicketItemModifierGroups();

			boolean requiredModifierAdded = modifierView.isRequiredModifiersAdded(menuItemModifierGroups, ticketItemModifierGroups);

			if (!requiredModifierAdded) {
				POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("CategoryView.3")); //$NON-NLS-1$
				return;
			}
		}
		
		CategoryButton button = (CategoryButton) e.getSource();
		if(button.isSelected()) {
			fireCategorySelected(button.foodCategory);
		}
	}
	
	public void cleanup() {
		Collection<CategoryButton> buttons = buttonMap.values();
		
		for (CategoryButton button : buttons) {
			button.removeActionListener(this);
		}
		buttonMap.clear();
		
		logger.debug(Messages.getString("CategoryView.4")); //$NON-NLS-1$
		
	}
	
	private static Logger logger = Logger.getLogger(MenuItemView.class);

	@Override
	public void doGoBack() {
	}
}
