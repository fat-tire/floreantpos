package com.floreantpos.posserver;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Payment")
public class Payment {
	String pamt;
	String tamt;
	String cback;
	String schrg;
	String cardType;
	String acct;
	String exp;
	String track2;
	String edc;

	public Payment() {
		super();
	}

	@XmlAttribute(name = "pamt")
	public String getPamt() {
		return pamt;
	}

	public void setPamt(String pamt) {
		this.pamt = pamt;
	}

	@XmlAttribute(name = "tamt")
	public String getTamt() {
		return tamt;
	}

	public void setTamt(String tamt) {
		this.tamt = tamt;
	}

	@XmlAttribute(name = "cback")
	public String getCback() {
		return cback;
	}

	public void setCback(String cback) {
		this.cback = cback;
	}

	@XmlAttribute(name = "schrg")
	public String getSchrg() {
		return schrg;
	}

	public void setSchrg(String schrg) {
		this.schrg = schrg;
	}

	@XmlAttribute(name = "cardType")
	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	@XmlAttribute(name = "acct")
	public String getAcct() {
		return acct;
	}

	public void setAcct(String acct) {
		this.acct = acct;
	}

	@XmlAttribute(name = "exp")
	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	@XmlAttribute(name = "track2")
	public String getTrack2() {
		return track2;
	}

	public void setTrack2(String track2) {
		this.track2 = track2;
	}

	@XmlAttribute(name = "edc")
	public String getEdc() {
		return edc;
	}

	public void setEdc(String edc) {
		this.edc = edc;
	}
}
