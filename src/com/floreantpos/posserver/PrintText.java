package com.floreantpos.posserver;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PrintText")
public class PrintText {
	String text="Test";

	public PrintText() {
		super();
	}
	public PrintText(String txt) {
		super();
		this.text=txt; 
	}

	@XmlAttribute(name = "text")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
