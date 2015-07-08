package com.floreantpos.actions;

public enum ActionCommand {
	AUTHORIZE,
	AUTHORIZE_ALL,
	EDIT_TIPS,
	CLOSE,
	OK;
	
	public String toString() {
		return name().replaceAll("_", " "); //$NON-NLS-1$ //$NON-NLS-2$
	};
}
