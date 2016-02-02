package com.floreantpos.posserver;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Ident")
public class Ident {
	
	public final static String GET_TABLES = "45";
	public final static String APPLY_PAYMENT = "46";
	public final static String PRINT_CHECK = "47";
	
	String id;
	String ttype;
	String termserialno; 

	public Ident() {
		super();
	}

	@XmlAttribute(name = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute(name = "ttype")
	public String getTtype() {
		return ttype;
	}

	public void setTtype(String ttype) {
		this.ttype = ttype;
	}

	@XmlAttribute(name = "termserialno")
	public String getTermserialno() {
		return termserialno;
	}

	public void setTermserialno(String termserialno) {
		this.termserialno = termserialno;
	}

	
}
