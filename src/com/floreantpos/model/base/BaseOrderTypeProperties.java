package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the ORDER_TYPE_PROPERTIES table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="ORDER_TYPE_PROPERTIES"
 */

public abstract class BaseOrderTypeProperties  implements Comparable, Serializable {

	public static String REF = "OrderTypeProperties"; //$NON-NLS-1$
	public static String PROP_ORDET_TYPE = "ordetType"; //$NON-NLS-1$
	public static String PROP_BUTTON_COLOR = "buttonColor"; //$NON-NLS-1$
	public static String PROP_VISIBLE = "visible"; //$NON-NLS-1$
	public static String PROP_ALIAS = "alias"; //$NON-NLS-1$
	public static String PROP_POST_PAID = "postPaid"; //$NON-NLS-1$
	public static String PROP_ID = "id"; //$NON-NLS-1$
	public static String PROP_TEXT_COLOR = "textColor"; //$NON-NLS-1$


	// constructors
	public BaseOrderTypeProperties () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseOrderTypeProperties (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
		protected java.lang.String ordetType;
		protected java.lang.String alias;
		protected java.lang.Integer buttonColor;
		protected java.lang.Integer textColor;
		protected java.lang.Boolean visible;
		protected java.lang.Boolean postPaid;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="ID"
     */
	public java.lang.Integer getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: OTYPE
	 */
	public java.lang.String getOrdetType () {
					return ordetType;
			}

	/**
	 * Set the value related to the column: OTYPE
	 * @param ordetType the OTYPE value
	 */
	public void setOrdetType (java.lang.String ordetType) {
		this.ordetType = ordetType;
	}



	/**
	 * Return the value associated with the column: ALIAS
	 */
	public java.lang.String getAlias () {
					return alias;
			}

	/**
	 * Set the value related to the column: ALIAS
	 * @param alias the ALIAS value
	 */
	public void setAlias (java.lang.String alias) {
		this.alias = alias;
	}



	/**
	 * Return the value associated with the column: BTN_COLOR
	 */
	public java.lang.Integer getButtonColor () {
					return buttonColor == null ? Integer.valueOf(0) : buttonColor;
			}

	/**
	 * Set the value related to the column: BTN_COLOR
	 * @param buttonColor the BTN_COLOR value
	 */
	public void setButtonColor (java.lang.Integer buttonColor) {
		this.buttonColor = buttonColor;
	}



	/**
	 * Return the value associated with the column: TEXT_COLOR
	 */
	public java.lang.Integer getTextColor () {
					return textColor == null ? Integer.valueOf(0) : textColor;
			}

	/**
	 * Set the value related to the column: TEXT_COLOR
	 * @param textColor the TEXT_COLOR value
	 */
	public void setTextColor (java.lang.Integer textColor) {
		this.textColor = textColor;
	}



	/**
	 * Return the value associated with the column: VISIBLE
	 */
	public java.lang.Boolean isVisible () {
								return visible == null ? Boolean.FALSE : visible;
					}

	/**
	 * Set the value related to the column: VISIBLE
	 * @param visible the VISIBLE value
	 */
	public void setVisible (java.lang.Boolean visible) {
		this.visible = visible;
	}



	/**
	 * Return the value associated with the column: POST_PAID
	 */
	public java.lang.Boolean isPostPaid () {
								return postPaid == null ? Boolean.FALSE : postPaid;
					}

	/**
	 * Set the value related to the column: POST_PAID
	 * @param postPaid the POST_PAID value
	 */
	public void setPostPaid (java.lang.Boolean postPaid) {
		this.postPaid = postPaid;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.OrderTypeProperties)) return false;
		else {
			com.floreantpos.model.OrderTypeProperties orderTypeProperties = (com.floreantpos.model.OrderTypeProperties) obj;
			if (null == this.getId() || null == orderTypeProperties.getId()) return false;
			else return (this.getId().equals(orderTypeProperties.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode(); //$NON-NLS-1$
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