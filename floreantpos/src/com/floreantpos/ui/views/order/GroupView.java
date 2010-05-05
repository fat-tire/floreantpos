/*
 * GroupView.java
 *
 * Created on August 5, 2006, 9:29 PM
 */

package com.floreantpos.ui.views.order;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.views.order.actions.GroupSelectionListener;

/**
 *
 * @author  MShahriar
 */
public class GroupView extends SelectionView {
	private Vector<GroupSelectionListener> listenerList = new Vector<GroupSelectionListener>();
    
	private MenuCategory menuCategory;
    
    public static final String VIEW_NAME = "GROUP_VIEW";
    
    /** Creates new form GroupView */
    public GroupView() {
        super(com.floreantpos.POSConstants.GROUPS);
        
        setBackEnable(false);
    }

	public MenuCategory getMenuCategory() {
		return menuCategory;
	}

	public void setMenuCategory(MenuCategory foodCategory) {
		this.menuCategory = foodCategory;

		reset();
		
		if (foodCategory == null) {
			return;
		}
		
		try {
			MenuGroupDAO dao = new MenuGroupDAO();
			List<MenuGroup> groups = dao.findEnabledByParent(foodCategory);
	        
	        for (int i = 0; i < groups.size(); i++) {
				MenuGroup foodGroup = groups.get(i);
				GroupButton button = new GroupButton(foodGroup);
				addButton(button);
			}
	        revalidate();
	        repaint();
		} catch (Exception e) {
			MessageDialog.showError(e);
		}
	}
	
	public void addGroupSelectionListener(GroupSelectionListener listener) {
		listenerList.add(listener);
	}
	
	public void removeGroupSelectionListener(GroupSelectionListener listener) {
		listenerList.remove(listener);
	}
	
	private void fireGroupSelected(MenuGroup foodGroup) {
		for (GroupSelectionListener listener : listenerList) {
			listener.groupSelected(foodGroup);
		}
	}
	
	
	private class GroupButton extends PosButton implements ActionListener {
		MenuGroup foodGroup;
		
		GroupButton(MenuGroup foodGroup) {
			this.foodGroup = foodGroup;
			
			setText(foodGroup.getName());
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			fireGroupSelected(foodGroup);
		}
	}

	@Override
	public void doGoBack() {
	}
}
