package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the CUSTOMER table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="CUSTOMER"
 */

public abstract class BaseCustomer  implements Comparable, Serializable {

	public static String REF = "Customer";
	public static String PROP_PICTURE = "picture";
	public static String PROP_SOCIAL_SECURITY_NUMBER = "socialSecurityNumber";
	public static String PROP_WORK_PHONE_NO = "workPhoneNo";
	public static String PROP_VIP = "vip";
	public static String PROP_LOYALTY_POINT = "loyaltyPoint";
	public static String PROP_SALUTATION = "salutation";
	public static String PROP_NOTE = "note";
	public static String PROP_HOME_PHONE_NO = "homePhoneNo";
	public static String PROP_COUNTRY = "country";
	public static String PROP_LAST_NAME = "lastName";
	public static String PROP_ZIP_CODE = "zipCode";
	public static String PROP_DOB = "dob";
	public static String PROP_CITY = "city";
	public static String PROP_SSN = "ssn";
	public static String PROP_MOBILE_NO = "mobileNo";
	public static String PROP_NAME = "name";
	public static String PROP_STATE = "state";
	public static String PROP_EMAIL = "email";
	public static String PROP_CREDIT_SPENT = "creditSpent";
	public static String PROP_ADDRESS = "address";
	public static String PROP_AUTO_ID = "autoId";
	public static String PROP_FIRST_NAME = "firstName";
	public static String PROP_CREDIT_CARD_NO = "creditCardNo";
	public static String PROP_CREDIT_LIMIT = "creditLimit";
	public static String PROP_LOYALTY_NO = "loyaltyNo";


	// constructors
	public BaseCustomer () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseCustomer (java.lang.Integer autoId) {
		this.setAutoId(autoId);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer autoId;

	// fields
		protected java.lang.String loyaltyNo;
		protected java.lang.Integer loyaltyPoint;
		protected java.lang.String socialSecurityNumber;
		protected byte[] picture;
		protected java.lang.String homePhoneNo;
		protected java.lang.String mobileNo;
		protected java.lang.String workPhoneNo;
		protected java.lang.String email;
		protected java.lang.String salutation;
		protected java.lang.String firstName;
		protected java.lang.String lastName;
		protected java.lang.String name;
		protected java.lang.String dob;
		protected java.lang.String ssn;
		protected java.lang.String address;
		protected java.lang.String city;
		protected java.lang.String state;
		protected java.lang.String zipCode;
		protected java.lang.String country;
		protected java.lang.Boolean vip;
		protected java.lang.Double creditLimit;
		protected java.lang.Double creditSpent;
		protected java.lang.String creditCardNo;
		protected java.lang.String note;

	// collections
	private java.util.List<com.floreantpos.model.DeliveryAddress> deliveryAddresses;
	private java.util.List<com.floreantpos.model.DeliveryInstruction> deliveryInstructions;
	private java.util.Map<String,String> properties;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="AUTO_ID"
     */
	public java.lang.Integer getAutoId () {
		return autoId;
	}

	/**
	 * Set the unique identifier of this class
	 * @param autoId the new ID
	 */
	public void setAutoId (java.lang.Integer autoId) {
		this.autoId = autoId;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: LOYALTY_NO
	 */
	public java.lang.String getLoyaltyNo () {
					return loyaltyNo;
			}

	/**
	 * Set the value related to the column: LOYALTY_NO
	 * @param loyaltyNo the LOYALTY_NO value
	 */
	public void setLoyaltyNo (java.lang.String loyaltyNo) {
		this.loyaltyNo = loyaltyNo;
	}



	/**
	 * Return the value associated with the column: LOYALTY_POINT
	 */
	public java.lang.Integer getLoyaltyPoint () {
									return loyaltyPoint == null ? Integer.valueOf(0) : loyaltyPoint;
					}

	/**
	 * Set the value related to the column: LOYALTY_POINT
	 * @param loyaltyPoint the LOYALTY_POINT value
	 */
	public void setLoyaltyPoint (java.lang.Integer loyaltyPoint) {
		this.loyaltyPoint = loyaltyPoint;
	}



	/**
	 * Return the value associated with the column: SOCIAL_SECURITY_NUMBER
	 */
	public java.lang.String getSocialSecurityNumber () {
					return socialSecurityNumber;
			}

	/**
	 * Set the value related to the column: SOCIAL_SECURITY_NUMBER
	 * @param socialSecurityNumber the SOCIAL_SECURITY_NUMBER value
	 */
	public void setSocialSecurityNumber (java.lang.String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}



	/**
	 * Return the value associated with the column: PICTURE
	 */
	public byte[] getPicture () {
					return picture;
			}

	/**
	 * Set the value related to the column: PICTURE
	 * @param picture the PICTURE value
	 */
	public void setPicture (byte[] picture) {
		this.picture = picture;
	}



	/**
	 * Return the value associated with the column: HOMEPHONE_NO
	 */
	public java.lang.String getHomePhoneNo () {
					return homePhoneNo;
			}

	/**
	 * Set the value related to the column: HOMEPHONE_NO
	 * @param homePhoneNo the HOMEPHONE_NO value
	 */
	public void setHomePhoneNo (java.lang.String homePhoneNo) {
		this.homePhoneNo = homePhoneNo;
	}



	/**
	 * Return the value associated with the column: MOBILE_NO
	 */
	public java.lang.String getMobileNo () {
					return mobileNo;
			}

	/**
	 * Set the value related to the column: MOBILE_NO
	 * @param mobileNo the MOBILE_NO value
	 */
	public void setMobileNo (java.lang.String mobileNo) {
		this.mobileNo = mobileNo;
	}



	/**
	 * Return the value associated with the column: WORKPHONE_NO
	 */
	public java.lang.String getWorkPhoneNo () {
					return workPhoneNo;
			}

	/**
	 * Set the value related to the column: WORKPHONE_NO
	 * @param workPhoneNo the WORKPHONE_NO value
	 */
	public void setWorkPhoneNo (java.lang.String workPhoneNo) {
		this.workPhoneNo = workPhoneNo;
	}



	/**
	 * Return the value associated with the column: EMAIL
	 */
	public java.lang.String getEmail () {
					return email;
			}

	/**
	 * Set the value related to the column: EMAIL
	 * @param email the EMAIL value
	 */
	public void setEmail (java.lang.String email) {
		this.email = email;
	}



	/**
	 * Return the value associated with the column: SALUTATION
	 */
	public java.lang.String getSalutation () {
					return salutation;
			}

	/**
	 * Set the value related to the column: SALUTATION
	 * @param salutation the SALUTATION value
	 */
	public void setSalutation (java.lang.String salutation) {
		this.salutation = salutation;
	}



	/**
	 * Return the value associated with the column: FIRST_NAME
	 */
	public java.lang.String getFirstName () {
					return firstName;
			}

	/**
	 * Set the value related to the column: FIRST_NAME
	 * @param firstName the FIRST_NAME value
	 */
	public void setFirstName (java.lang.String firstName) {
		this.firstName = firstName;
	}



	/**
	 * Return the value associated with the column: LAST_NAME
	 */
	public java.lang.String getLastName () {
					return lastName;
			}

	/**
	 * Set the value related to the column: LAST_NAME
	 * @param lastName the LAST_NAME value
	 */
	public void setLastName (java.lang.String lastName) {
		this.lastName = lastName;
	}



	/**
	 * Return the value associated with the column: name
	 */
	public java.lang.String getName () {
					return name;
			}

	/**
	 * Set the value related to the column: name
	 * @param name the name value
	 */
	public void setName (java.lang.String name) {
		this.name = name;
	}



	/**
	 * Return the value associated with the column: DOB
	 */
	public java.lang.String getDob () {
					return dob;
			}

	/**
	 * Set the value related to the column: DOB
	 * @param dob the DOB value
	 */
	public void setDob (java.lang.String dob) {
		this.dob = dob;
	}



	/**
	 * Return the value associated with the column: SSN
	 */
	public java.lang.String getSsn () {
					return ssn;
			}

	/**
	 * Set the value related to the column: SSN
	 * @param ssn the SSN value
	 */
	public void setSsn (java.lang.String ssn) {
		this.ssn = ssn;
	}



	/**
	 * Return the value associated with the column: ADDRESS
	 */
	public java.lang.String getAddress () {
					return address;
			}

	/**
	 * Set the value related to the column: ADDRESS
	 * @param address the ADDRESS value
	 */
	public void setAddress (java.lang.String address) {
		this.address = address;
	}



	/**
	 * Return the value associated with the column: CITY
	 */
	public java.lang.String getCity () {
					return city;
			}

	/**
	 * Set the value related to the column: CITY
	 * @param city the CITY value
	 */
	public void setCity (java.lang.String city) {
		this.city = city;
	}



	/**
	 * Return the value associated with the column: STATE
	 */
	public java.lang.String getState () {
					return state;
			}

	/**
	 * Set the value related to the column: STATE
	 * @param state the STATE value
	 */
	public void setState (java.lang.String state) {
		this.state = state;
	}



	/**
	 * Return the value associated with the column: ZIP_CODE
	 */
	public java.lang.String getZipCode () {
					return zipCode;
			}

	/**
	 * Set the value related to the column: ZIP_CODE
	 * @param zipCode the ZIP_CODE value
	 */
	public void setZipCode (java.lang.String zipCode) {
		this.zipCode = zipCode;
	}



	/**
	 * Return the value associated with the column: COUNTRY
	 */
	public java.lang.String getCountry () {
					return country;
			}

	/**
	 * Set the value related to the column: COUNTRY
	 * @param country the COUNTRY value
	 */
	public void setCountry (java.lang.String country) {
		this.country = country;
	}



	/**
	 * Return the value associated with the column: VIP
	 */
	public java.lang.Boolean isVip () {
								return vip == null ? Boolean.FALSE : vip;
					}

	/**
	 * Set the value related to the column: VIP
	 * @param vip the VIP value
	 */
	public void setVip (java.lang.Boolean vip) {
		this.vip = vip;
	}



	/**
	 * Return the value associated with the column: CREDIT_LIMIT
	 */
	public java.lang.Double getCreditLimit () {
									return creditLimit == null ? Double.valueOf(0) : creditLimit;
					}

	/**
	 * Set the value related to the column: CREDIT_LIMIT
	 * @param creditLimit the CREDIT_LIMIT value
	 */
	public void setCreditLimit (java.lang.Double creditLimit) {
		this.creditLimit = creditLimit;
	}



	/**
	 * Return the value associated with the column: CREDIT_SPENT
	 */
	public java.lang.Double getCreditSpent () {
									return creditSpent == null ? Double.valueOf(0) : creditSpent;
					}

	/**
	 * Set the value related to the column: CREDIT_SPENT
	 * @param creditSpent the CREDIT_SPENT value
	 */
	public void setCreditSpent (java.lang.Double creditSpent) {
		this.creditSpent = creditSpent;
	}



	/**
	 * Return the value associated with the column: CREDIT_CARD_NO
	 */
	public java.lang.String getCreditCardNo () {
					return creditCardNo;
			}

	/**
	 * Set the value related to the column: CREDIT_CARD_NO
	 * @param creditCardNo the CREDIT_CARD_NO value
	 */
	public void setCreditCardNo (java.lang.String creditCardNo) {
		this.creditCardNo = creditCardNo;
	}



	/**
	 * Return the value associated with the column: NOTE
	 */
	public java.lang.String getNote () {
					return note;
			}

	/**
	 * Set the value related to the column: NOTE
	 * @param note the NOTE value
	 */
	public void setNote (java.lang.String note) {
		this.note = note;
	}



	/**
	 * Return the value associated with the column: deliveryAddresses
	 */
	public java.util.List<com.floreantpos.model.DeliveryAddress> getDeliveryAddresses () {
					return deliveryAddresses;
			}

	/**
	 * Set the value related to the column: deliveryAddresses
	 * @param deliveryAddresses the deliveryAddresses value
	 */
	public void setDeliveryAddresses (java.util.List<com.floreantpos.model.DeliveryAddress> deliveryAddresses) {
		this.deliveryAddresses = deliveryAddresses;
	}

	public void addTodeliveryAddresses (com.floreantpos.model.DeliveryAddress deliveryAddress) {
		if (null == getDeliveryAddresses()) setDeliveryAddresses(new java.util.ArrayList<com.floreantpos.model.DeliveryAddress>());
		getDeliveryAddresses().add(deliveryAddress);
	}



	/**
	 * Return the value associated with the column: deliveryInstructions
	 */
	public java.util.List<com.floreantpos.model.DeliveryInstruction> getDeliveryInstructions () {
					return deliveryInstructions;
			}

	/**
	 * Set the value related to the column: deliveryInstructions
	 * @param deliveryInstructions the deliveryInstructions value
	 */
	public void setDeliveryInstructions (java.util.List<com.floreantpos.model.DeliveryInstruction> deliveryInstructions) {
		this.deliveryInstructions = deliveryInstructions;
	}

	public void addTodeliveryInstructions (com.floreantpos.model.DeliveryInstruction deliveryInstruction) {
		if (null == getDeliveryInstructions()) setDeliveryInstructions(new java.util.ArrayList<com.floreantpos.model.DeliveryInstruction>());
		getDeliveryInstructions().add(deliveryInstruction);
	}



	/**
	 * Return the value associated with the column: properties
	 */
	public java.util.Map<String,String> getProperties () {
					return properties;
			}

	/**
	 * Set the value related to the column: properties
	 * @param properties the properties value
	 */
	public void setProperties (java.util.Map<String,String> properties) {
		this.properties = properties;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.Customer)) return false;
		else {
			com.floreantpos.model.Customer customer = (com.floreantpos.model.Customer) obj;
			if (null == this.getAutoId() || null == customer.getAutoId()) return false;
			else return (this.getAutoId().equals(customer.getAutoId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getAutoId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getAutoId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public int compareTo (Object obj) {
		if (obj.hashCode() > hashCode()) return 1;
		else if (obj.hashCode() < hashCode()) return -1;
		else return 0;
	}

	public String toString () {
		return super.toString();
	}


}