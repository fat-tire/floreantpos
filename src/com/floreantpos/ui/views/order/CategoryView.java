/*
 * CategoryView.java
 *
 * Created on August 5, 2006, 2:21 AM
 */

package com.floreantpos.ui.views.order;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.swing.POSToggleButton;
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
	
	public static final String VIEW_NAME = "CATEGORY_VIEW";
	
	//private int panelCount = 0;
	
	/** Creates new form CategoryView */
	public CategoryView() {
		super(com.floreantpos.POSConstants.CATEGORIES);
		
		getButtonsPanel().setLayout(new MigLayout("wrap 1", "fill,grow,shrink", ""));
		getButtonScrollPane().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		setBackVisible(false);
		
		categoryButtonGroup = new ButtonGroup();
		setPreferredSize(new Dimension(160, 100));
	}

	public void initialize() {
		reset();
		
		MenuCategoryDAO categoryDAO = new MenuCategoryDAO();
		List<MenuCategory> categories = categoryDAO.findBevegares();
		categories.addAll(categoryDAO.findNonBevegares());
		
		CategoryButton selectedButton = null;
		MenuCategory selectedCategory = null;
		
		for (int i = 0; i < categories.size(); i++) {
			MenuCategory menuCategory = categories.get(i);
			CategoryButton button = new CategoryButton(this,menuCategory);
			categoryButtonGroup.add(button);
			
			buttonMap.put(String.valueOf(menuCategory.getId()), button);
			
			addButton(button);
			
			if(i == 0) {
				selectedButton = button;
				selectedCategory = menuCategory;
			}
		}
		
		if(selectedButton != null && selectedCategory != null) {
			selectedButton.setSelected(true);
			fireCategorySelected(selectedCategory);
		}
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
		private final static Dimension buttonDimension = new Dimension(120, 55);;
		
		CategoryButton(CategoryView view, MenuCategory foodCategory) {
			this.foodCategory = foodCategory;
			
			setText(foodCategory.getName());
			setPreferredSize(buttonDimension);
			addActionListener(view);
		}
	}
	

	public void actionPerformed(ActionEvent e) {
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
		
		logger.debug("Cleared category buttons");
		
	}
	
	private static Logger logger = Logger.getLogger(MenuItemView.class);

	@Override
	public void doGoBack() {
	}
}
