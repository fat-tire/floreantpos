package com.floreantpos.model;


import java.awt.Color;

public enum TableStatus {
	Seat(1, Color.RED, Color.WHITE), 
	Booked(2, Color.ORANGE, Color.BLACK, false), 
	Dirty(3), 
	Disable(4), 
	Available(5);

	private final int value;
	private Color bgColor;
	private Color textColor;
	private Boolean enabled;

	private TableStatus(int value) {
		this.value = value;
	}

	private TableStatus(int value, Color bgColor, Color textColor) {
		this.value = value;
		this.bgColor = bgColor;
		this.textColor = textColor;
	}

	private TableStatus(int value, Color bgColor, Color textColor, Boolean enabled) {
		this.value = value;
		this.bgColor = bgColor;
		this.textColor = textColor;
		this.enabled = enabled;
	}

	public int getValue() {
		return value;
	}

	public static TableStatus get(int value) {
		switch (value) {
			case 1:
				return Seat;
			case 2:
				return Booked;
			case 3:
				return Dirty;
			case 4:
				return Disable;
			case 5:
				return Available;

			default:
				return Available;
		}
	}

	@Override
	public String toString() {
		return name();
	}

	public Color getBgColor() {
		return bgColor == null ? Color.WHITE : bgColor;
	}

	public Color getTextColor() {
		return textColor == null ? Color.BLACK : textColor;
	}

	public Boolean getEnabled() {
		return enabled;
	}
}
