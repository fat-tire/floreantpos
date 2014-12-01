package com.floreantpos.model;

public enum PackagingDimension {
	Quantity, Weight, Length, Volume;
	
	public String toString() {
		return name();
	};
}
