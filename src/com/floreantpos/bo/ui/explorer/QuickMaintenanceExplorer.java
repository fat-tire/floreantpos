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
package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;

import com.floreantpos.main.Application;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.model.MenuCategoryForm;
import com.floreantpos.ui.model.MenuGroupForm;
import com.floreantpos.ui.model.MenuItemForm;
import com.floreantpos.ui.views.order.OrderView;

public class QuickMaintenanceExplorer extends TransparentPanel {

	public QuickMaintenanceExplorer() {
		super(new BorderLayout());
	}

	public static void quickAddOrUpdateBeanObject(Object object) {
		if (object instanceof MenuItem) {
			quickAddOrUpdateMenuItem((MenuItem) object);
		}
		else if (object instanceof MenuGroup) {
			quickAddOrUpdateMenuGroup((MenuGroup) object);
		}
		else if (object instanceof MenuCategory) {
			quickAddOrUpdateMenuCategory((MenuCategory) object);
		}
	}

	private static void quickAddOrUpdateMenuItem(MenuItem menuItem) {
		try {
			MenuItemForm editor = new MenuItemForm(menuItem);
			BeanEditorDialog dialog = new BeanEditorDialog(Application.getPosWindow(), editor);
			dialog.open();
			if (dialog.isCanceled())
				return;
			OrderView.getInstance().getItemView().updateView(menuItem);
			return;
		} catch (Exception e) {
			return;
		}
	}

	private static void quickAddOrUpdateMenuGroup(MenuGroup menuGroup) {
		try {
			MenuGroupForm editor = new MenuGroupForm(menuGroup);
			BeanEditorDialog dialog = new BeanEditorDialog(Application.getPosWindow(), editor);
			dialog.open();
			if (dialog.isCanceled())
				return;
			OrderView.getInstance().getGroupView().updateView(menuGroup);
			return;
		} catch (Exception e) {
			return;
		}
	}

	private static void quickAddOrUpdateMenuCategory(MenuCategory menuCategory) {
		try {
			MenuCategoryForm editor = new MenuCategoryForm(menuCategory);
			BeanEditorDialog dialog = new BeanEditorDialog(Application.getPosWindow(), editor);
			dialog.open();
			if (dialog.isCanceled())
				return;
			OrderView.getInstance().getCategoryView().updateView(menuCategory);
			return;
		} catch (Exception e) {
			return;
		}
	}
}
