package com.mdss.pos.ui.views.order.actions;

import com.mdss.pos.model.MenuGroup;
import com.mdss.pos.model.MenuItem;

public interface ItemSelectionListener {
	void itemSelected(MenuItem menuItem);
	void itemSelectionFinished(MenuGroup parent);
}
