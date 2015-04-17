package com.floreantpos.util;

import com.floreantpos.model.TicketItemModifier;

public interface ModifierStateChangeListener {
	void modifierStateChanged();
	void updateView(TicketItemModifier modifier);
	void select(TicketItemModifier modifier);
	void clearSelection();
}
