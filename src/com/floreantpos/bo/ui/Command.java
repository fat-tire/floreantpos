package com.floreantpos.bo.ui;

public enum Command {
	NEW,
	EDIT,
	DELETE,
	SAVE,
	CANCEL,
	UNKNOWN
	;
	
	public static Command fromString(String s) {
		Command[] values = values();
		for (Command command : values) {
			if(command.name().equalsIgnoreCase(s)) {
				return command;
			}
		}
		
		return UNKNOWN;
	}
}
