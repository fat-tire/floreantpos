package com.floreantpos.ui.views.order.actions;

import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuModifier;

public interface ModifierSelectionListener {
	void modifierSelected(MenuItem parent, MenuModifier modifier);
	void modifierSelectionFiniched(MenuItem parent);
}
