package com.floreantpos.actions;

public enum ActionCommand {
	AUTHORIZE,
	EDIT_TIPS,
	CLOSE;
	
	public String toString() {
		return name().replaceAll("_", " ");
	};
}
