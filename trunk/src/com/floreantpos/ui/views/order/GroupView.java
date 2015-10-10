/*
 * GroupView.java
 *
 * Created on August 5, 2006, 9:29 PM
 */

package com.floreantpos.ui.views.order;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;

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
    
    public static final String VIEW_NAME = "GROUP_VIEW"; //$NON-NLS-1$
    
    /** Creates new form GroupView */
    public GroupView() {
        super(com.floreantpos.POSConstants.GROUPS);
        
        setBackEnable(false);
    }

	public MenuCategory getMenuCategory() {
		return menuCategory;
	}

	public void setMenuCategory(MenuCategory menuCategory) {
		this.menuCategory = menuCategory;

		//reset();
		
		if (menuCategory == null) {
			return;
		}
		
		try {
			MenuGroupDAO dao = new MenuGroupDAO();
			List<MenuGroup> groups = dao.findEnabledByParent(menuCategory);
			
			if(groups.size() == 1) {
				MenuGroup menuGroup = groups.get(0);
				fireGroupSelected(menuGroup);
				return;
			}

//			int v = 0;
//			List<MenuGroup> groups2 = new ArrayList<MenuGroup>();
//			for (MenuGroup menuGroup : groups) {
//				String name = menuGroup.getName();
//				for (int i = 0; i < 30; i++) {
//					MenuGroup menuGroup2 = new MenuGroup(menuGroup.getId());
//					menuGroup2.setParent(menuGroup.getParent());
//					menuGroup2.setName(name + (++v));
//					groups2.add(menuGroup2);
//				}
//			}
//			
//			setItems(groups2);
			
			setItems(groups);
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
	
	@Override
	protected JButton createItemButton(Object item) {
		MenuGroup menuGroup = (MenuGroup) item;
		GroupButton button = new GroupButton(menuGroup);
		
		return button;
	}
	
	
	private class GroupButton extends PosButton implements ActionListener {
		MenuGroup menuGroup;
		
		GroupButton(MenuGroup foodGroup) {
			this.menuGroup = foodGroup;
			
			setText("<html><body><center>" + foodGroup.getDisplayName() + "</center></body></html>"); //$NON-NLS-1$ //$NON-NLS-2$
			
			if(menuGroup.getButtonColor() != null) {
				setBackground(new Color(menuGroup.getButtonColor()));
			}
			if(menuGroup.getTextColor() != null) {
				setForeground(new Color(menuGroup.getTextColor()));
			}
			
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			fireGroupSelected(menuGroup);
		}
	}

	@Override
	public void doGoBack() {
	}
}
