package com.floreantpos.dejavoo.proxy;

public class DejavooTerminal {
	private String tpn;
	private String regId;
	private String authKey;
	
	public String getTpn() {
		return tpn;
	}
	public void setTpn(String tpn) {
		this.tpn = tpn;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public String getAuthKey() {
		return authKey;
	}
	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}
}
