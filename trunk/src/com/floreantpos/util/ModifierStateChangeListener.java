package com.floreantpos.util;

import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.TicketItemModifier;

public interface ModifierStateChangeListener {
	void modifierStateChanged();
	void updateView(TicketItemModifier modifier);
}
