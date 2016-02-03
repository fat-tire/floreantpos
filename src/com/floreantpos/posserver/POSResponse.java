package com.floreantpos.posserver;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "POSResponse")
public class POSResponse {

	Ident ident;
	POSDefaultInfo posDefaultInfo;
	Checks checks;
	
	List<PrintText> printTexts; 

	public POSResponse() {
		super();
	}

	@XmlElement(name = "Ident")
	public Ident getIdent() {
		return ident;
	}

	public void setIdent(Ident ident) {
		this.ident = ident;
	}

	@XmlElement(name = "POSDefaultInfo")
	public POSDefaultInfo getPosDefaultInfo() {
		return posDefaultInfo;
	}

	public void setPosDefaultInfo(POSDefaultInfo posDefaultInfo) {
		this.posDefaultInfo = posDefaultInfo;
	}

	@XmlElement(name = "Checks")
	public Checks getChecks() {
		return checks;
	}

	public void setChecks(Checks checks) {
		this.checks = checks;
	}
	
	@XmlElement(name = "PrintText")
	public List<PrintText> getPrintText() {
		return printTexts;
	}

	public void setPrintChecks(List<PrintText> printTexts) {
		this.printTexts = printTexts;
	}
}
