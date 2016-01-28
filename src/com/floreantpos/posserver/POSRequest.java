package com.floreantpos.posserver;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "POSRequest")
public class POSRequest {

	
	Ident ident;
	POSDefaultInfo posDefaultInfo; 

	public POSRequest() {
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
}
