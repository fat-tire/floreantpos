package com.floreantpos.posserver;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Checks")
@XmlAccessorType (XmlAccessType.FIELD)
public class Checks {
	
	@XmlElement(name = "Check")
	private List<Check> checks;

	public List<Check> getCheckList() {
		return checks;
	}

	public void setCheckList(List<Check> checks) {
		this.checks = checks;
	}

	
}
