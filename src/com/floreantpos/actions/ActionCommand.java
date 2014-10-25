package com.floreantpos.actions;

public enum ActionCommand {
	AUTHORIZE,
	AUTHORIZE_ALL,
	EDIT_TIPS,
	CLOSE;
	
	public String toString() {
		return name().replaceAll("_", " ");
	};
}
