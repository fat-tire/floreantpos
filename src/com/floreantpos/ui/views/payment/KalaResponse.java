/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.ui.views.payment;


public class KalaResponse {
	String mykala_id;
	String first_name;
	String last_name;
	String company_name;
	String address;
	String city;
	String county;
	String state;
	String zip;
	String phone1;
	String phone2;
	String email;
	String web;
	String offer_id;
	String points;
	boolean success;
	String message;
	String coupon;
	String offer;
	
	public void parse(javax.json.JsonObject object) {
		 mykala_id = object.getString("id"); //$NON-NLS-1$
		 first_name = object.getString("first_name"); //$NON-NLS-1$
		 last_name = object.getString("last_name"); //$NON-NLS-1$
		 company_name = object.getString("company_name"); //$NON-NLS-1$
		 address = object.getString("address"); //$NON-NLS-1$
		 city = object.getString("city"); //$NON-NLS-1$
		 county = object.getString("county"); //$NON-NLS-1$
		 state = object.getString("state"); //$NON-NLS-1$
		 zip = object.getString("zip"); //$NON-NLS-1$
		 phone1 = object.getString("phone1"); //$NON-NLS-1$
		 phone2 = object.getString("phone2"); //$NON-NLS-1$
		 email = object.getString("email"); //$NON-NLS-1$
		 web = object.getString("first_name"); //$NON-NLS-1$
		 offer_id = object.getString("kala_id"); //$NON-NLS-1$
		 points = object.getString("points"); //$NON-NLS-1$
		 success = Boolean.valueOf(object.get("success").toString()); //$NON-NLS-1$
		 message = object.getString("message"); //$NON-NLS-1$
		 coupon = object.getString("coupon"); //$NON-NLS-1$
		 offer = object.getString("offer").replaceAll("%", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	public String getMykala_id() {
		return mykala_id;
	}

	public void setMykala_id(String id) {
		this.mykala_id = id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getOffer_id() {
		return offer_id;
	}

	public void setOffer_id(String offer_id) {
		this.offer_id = offer_id;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	public String getOffer() {
		return offer;
	}

	public void setOffer(String offer) {
		this.offer = offer;
	}
	
	public String getName() {
		return first_name + " " + last_name; //$NON-NLS-1$
	}
}
