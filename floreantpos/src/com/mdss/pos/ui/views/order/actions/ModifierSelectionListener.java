package com.mdss.pos.ui.views.order.actions;

import com.mdss.pos.model.MenuItem;
import com.mdss.pos.model.MenuModifier;

public interface ModifierSelectionListener {
	void modifierSelected(MenuItem parent, MenuModifier modifier);
	void modifierSelectionFiniched(MenuItem parent);
}
