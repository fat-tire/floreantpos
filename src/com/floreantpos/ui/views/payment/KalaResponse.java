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
		 mykala_id = object.getString("id");
		 first_name = object.getString("first_name");
		 last_name = object.getString("last_name");
		 company_name = object.getString("company_name");
		 address = object.getString("address");
		 city = object.getString("city");
		 county = object.getString("county");
		 state = object.getString("state");
		 zip = object.getString("zip");
		 phone1 = object.getString("phone1");
		 phone2 = object.getString("phone2");
		 email = object.getString("email");
		 web = object.getString("first_name");
		 offer_id = object.getString("kala_id");
		 points = object.getString("points");
		 success = Boolean.valueOf(object.get("success").toString());
		 message = object.getString("message");
		 coupon = object.getString("coupon");
		 offer = object.getString("offer").replaceAll("%", "");
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
		return first_name + " " + last_name;
	}
}
