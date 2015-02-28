package com.floreantpos.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the PRINTER_CONFIGURATION table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="PRINTER_CONFIGURATION"
 */

public abstract class BasePrinterConfiguration  implements Comparable, Serializable {

	public static String REF = "PrinterConfiguration";
	public static String PROP_USE_NORMAL_PRINTER_FOR_TICKET = "useNormalPrinterForTicket";
	public static String PROP_USE_NORMAL_PRINTER_FOR_KITCHEN = "useNormalPrinterForKitchen";
	public static String PROP_PRINT_KITCHEN_WHEN_TICKET_SETTLED = "printKitchenWhenTicketSettled";
	public static String PROP_RECEIPT_PRINTER_NAME = "receiptPrinterName";
	public static String PROP_PRINT_RECEIPT_WHEN_TICKET_PAID = "printReceiptWhenTicketPaid";
	public static String PROP_PRINT_RECREIPT_WHEN_TICKET_SETTLED = "printRecreiptWhenTicketSettled";
	public static String PROP_PRINT_KITCHEN_WHEN_TICKET_PAID = "printKitchenWhenTicketPaid";
	public static String PROP_ID = "id";
	public static String PROP_KITCHEN_PRINTER_NAME = "kitchenPrinterName";


	// constructors
	public BasePrinterConfiguration () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BasePrinterConfiguration (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String receiptPrinterName;
	private java.lang.String kitchenPrinterName;
	private java.lang.Boolean printRecreiptWhenTicketSettled;
	private java.lang.Boolean printKitchenWhenTicketSettled;
	private java.lang.Boolean printReceiptWhenTicketPaid;
	private java.lang.Boolean printKitchenWhenTicketPaid;
	private java.lang.Boolean useNormalPrinterForTicket;
	private java.lang.Boolean useNormalPrinterForKitchen;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="assigned"
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
	 * Return the value associated with the column: RECEIPT_PRINTER
	 */
	public java.lang.String getReceiptPrinterName () {
			return receiptPrinterName;
	}

	/**
	 * Set the value related to the column: RECEIPT_PRINTER
	 * @param receiptPrinterName the RECEIPT_PRINTER value
	 */
	public void setReceiptPrinterName (java.lang.String receiptPrinterName) {
		this.receiptPrinterName = receiptPrinterName;
	}



	/**
	 * Return the value associated with the column: KITCHEN_PRINTER
	 */
	public java.lang.String getKitchenPrinterName () {
			return kitchenPrinterName;
	}

	/**
	 * Set the value related to the column: KITCHEN_PRINTER
	 * @param kitchenPrinterName the KITCHEN_PRINTER value
	 */
	public void setKitchenPrinterName (java.lang.String kitchenPrinterName) {
		this.kitchenPrinterName = kitchenPrinterName;
	}



	/**
	 * Return the value associated with the column: PRWTS
	 */
	public java.lang.Boolean isPrintRecreiptWhenTicketSettled () {
					return printRecreiptWhenTicketSettled == null ? Boolean.valueOf(true) : printRecreiptWhenTicketSettled;
			}

	/**
	 * Set the value related to the column: PRWTS
	 * @param printRecreiptWhenTicketSettled the PRWTS value
	 */
	public void setPrintRecreiptWhenTicketSettled (java.lang.Boolean printRecreiptWhenTicketSettled) {
		this.printRecreiptWhenTicketSettled = printRecreiptWhenTicketSettled;
	}


	/**
	 * Custom property
	 */
	public static String getPrintRecreiptWhenTicketSettledDefaultValue () {
		return "true";
	}


	/**
	 * Return the value associated with the column: PRWTP
	 */
	public java.lang.Boolean isPrintKitchenWhenTicketSettled () {
					return printKitchenWhenTicketSettled == null ? Boolean.valueOf(true) : printKitchenWhenTicketSettled;
			}

	/**
	 * Set the value related to the column: PRWTP
	 * @param printKitchenWhenTicketSettled the PRWTP value
	 */
	public void setPrintKitchenWhenTicketSettled (java.lang.Boolean printKitchenWhenTicketSettled) {
		this.printKitchenWhenTicketSettled = printKitchenWhenTicketSettled;
	}


	/**
	 * Custom property
	 */
	public static String getPrintKitchenWhenTicketSettledDefaultValue () {
		return "true";
	}


	/**
	 * Return the value associated with the column: PKWTS
	 */
	public java.lang.Boolean isPrintReceiptWhenTicketPaid () {
					return printReceiptWhenTicketPaid == null ? Boolean.valueOf(true) : printReceiptWhenTicketPaid;
			}

	/**
	 * Set the value related to the column: PKWTS
	 * @param printReceiptWhenTicketPaid the PKWTS value
	 */
	public void setPrintReceiptWhenTicketPaid (java.lang.Boolean printReceiptWhenTicketPaid) {
		this.printReceiptWhenTicketPaid = printReceiptWhenTicketPaid;
	}


	/**
	 * Custom property
	 */
	public static String getPrintReceiptWhenTicketPaidDefaultValue () {
		return "true";
	}


	/**
	 * Return the value associated with the column: PKWTP
	 */
	public java.lang.Boolean isPrintKitchenWhenTicketPaid () {
					return printKitchenWhenTicketPaid == null ? Boolean.valueOf(true) : printKitchenWhenTicketPaid;
			}

	/**
	 * Set the value related to the column: PKWTP
	 * @param printKitchenWhenTicketPaid the PKWTP value
	 */
	public void setPrintKitchenWhenTicketPaid (java.lang.Boolean printKitchenWhenTicketPaid) {
		this.printKitchenWhenTicketPaid = printKitchenWhenTicketPaid;
	}


	/**
	 * Custom property
	 */
	public static String getPrintKitchenWhenTicketPaidDefaultValue () {
		return "true";
	}


	/**
	 * Return the value associated with the column: UNPFT
	 */
	public java.lang.Boolean isUseNormalPrinterForTicket () {
					return useNormalPrinterForTicket == null ? Boolean.valueOf(false) : useNormalPrinterForTicket;
			}

	/**
	 * Set the value related to the column: UNPFT
	 * @param useNormalPrinterForTicket the UNPFT value
	 */
	public void setUseNormalPrinterForTicket (java.lang.Boolean useNormalPrinterForTicket) {
		this.useNormalPrinterForTicket = useNormalPrinterForTicket;
	}


	/**
	 * Custom property
	 */
	public static String getUseNormalPrinterForTicketDefaultValue () {
		return "false";
	}


	/**
	 * Return the value associated with the column: UNPFK
	 */
	public java.lang.Boolean isUseNormalPrinterForKitchen () {
					return useNormalPrinterForKitchen == null ? Boolean.valueOf(false) : useNormalPrinterForKitchen;
			}

	/**
	 * Set the value related to the column: UNPFK
	 * @param useNormalPrinterForKitchen the UNPFK value
	 */
	public void setUseNormalPrinterForKitchen (java.lang.Boolean useNormalPrinterForKitchen) {
		this.useNormalPrinterForKitchen = useNormalPrinterForKitchen;
	}


	/**
	 * Custom property
	 */
	public static String getUseNormalPrinterForKitchenDefaultValue () {
		return "false";
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.PrinterConfiguration)) return false;
		else {
			com.floreantpos.model.PrinterConfiguration printerConfiguration = (com.floreantpos.model.PrinterConfiguration) obj;
			if (null == this.getId() || null == printerConfiguration.getId()) return false;
			else return (this.getId().equals(printerConfiguration.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
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