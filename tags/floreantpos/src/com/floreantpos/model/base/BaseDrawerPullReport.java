package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;

import com.floreantpos.model.DrawerPullVoidTicketEntry;


/**
 * This is an object that contains data related to the DRAWER_PULL_REPORT table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="DRAWER_PULL_REPORT"
 */

public abstract class BaseDrawerPullReport  implements Comparable, Serializable {

	public static String REF = "DrawerPullReport";
	public static String PROP_TOTAL_DISCOUNT_CHECK_SIZE = "totalDiscountCheckSize";
	public static String PROP_TOTAL_DISCOUNT_SALES = "totalDiscountSales";
	public static String PROP_VARIANCE = "variance";
	public static String PROP_PAY_OUT_AMOUNT = "payOutAmount";
	public static String PROP_DEBIT_CARD_RECEIPT_AMOUNT = "debitCardReceiptAmount";
	public static String PROP_SALES_TAX = "salesTax";
	public static String PROP_TOTAL_VOID = "totalVoid";
	public static String PROP_CREDIT_CARD_RECEIPT_NUMBER = "creditCardReceiptNumber";
	public static String PROP_TIPS_DIFFERENTIAL = "tipsDifferential";
	public static String PROP_DRAWER_ACCOUNTABLE = "drawerAccountable";
	public static String PROP_CASH_RECEIPT_NUMBER = "cashReceiptNumber";
	public static String PROP_CASH_TAX = "cashTax";
	public static String PROP_RECEIPT_DIFFERENTIAL = "receiptDifferential";
	public static String PROP_TOTAL_DISCOUNT_GUEST = "totalDiscountGuest";
	public static String PROP_GIFT_CERT_CHANGE_AMOUNT = "giftCertChangeAmount";
	public static String PROP_PAY_OUT_NUMBER = "payOutNumber";
	public static String PROP_NET_SALES = "netSales";
	public static String PROP_CASH_BACK = "cashBack";
	public static String PROP_DRAWER_BLEED_NUMBER = "drawerBleedNumber";
	public static String PROP_DRAWER_BLEED_AMOUNT = "drawerBleedAmount";
	public static String PROP_CASH_RECEIPT_AMOUNT = "cashReceiptAmount";
	public static String PROP_TOTAL_DISCOUNT_PARTY_SIZE = "totalDiscountPartySize";
	public static String PROP_DEBIT_CARD_RECEIPT_NUMBER = "debitCardReceiptNumber";
	public static String PROP_TERMINAL = "terminal";
	public static String PROP_REG = "reg";
	public static String PROP_TOTAL_VOID_WST = "totalVoidWst";
	public static String PROP_CREDIT_CARD_RECEIPT_AMOUNT = "creditCardReceiptAmount";
	public static String PROP_TOTAL_DISCOUNT_PERCENTAGE = "totalDiscountPercentage";
	public static String PROP_GIFT_CERT_RETURN_AMOUNT = "giftCertReturnAmount";
	public static String PROP_BEGIN_CASH = "beginCash";
	public static String PROP_TOTAL_DISCOUNT_RATIO = "totalDiscountRatio";
	public static String PROP_CASH_TO_DEPOSIT = "cashToDeposit";
	public static String PROP_TOTAL_DISCOUNT_AMOUNT = "totalDiscountAmount";
	public static String PROP_TOTAL_DISCOUNT_COUNT = "totalDiscountCount";
	public static String PROP_TIPS_PAID = "tipsPaid";
	public static String PROP_TOTAL_REVENUE = "totalRevenue";
	public static String PROP_ID = "id";
	public static String PROP_GROSS_RECEIPTS = "grossReceipts";
	public static String PROP_CHARGED_TIPS = "chargedTips";
	public static String PROP_GIFT_CERT_RETURN_COUNT = "giftCertReturnCount";
	public static String PROP_TICKET_COUNT = "ticketCount";
	public static String PROP_REPORT_TIME = "reportTime";
	public static String PROP_CASH_TIPS = "cashTips";


	// constructors
	public BaseDrawerPullReport () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseDrawerPullReport (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.util.Date reportTime;
	private java.lang.String reg;
	private java.lang.Integer ticketCount;
	private java.lang.Double beginCash;
	private java.lang.Double netSales;
	private java.lang.Double salesTax;
	private java.lang.Double cashTax;
	private java.lang.Double totalRevenue;
	private java.lang.Double grossReceipts;
	private java.lang.Integer giftCertReturnCount;
	private java.lang.Double giftCertReturnAmount;
	private java.lang.Double giftCertChangeAmount;
	private java.lang.Integer cashReceiptNumber;
	private java.lang.Double cashReceiptAmount;
	private java.lang.Integer creditCardReceiptNumber;
	private java.lang.Double creditCardReceiptAmount;
	private java.lang.Integer debitCardReceiptNumber;
	private java.lang.Double debitCardReceiptAmount;
	private java.lang.Double receiptDifferential;
	private java.lang.Double cashBack;
	private java.lang.Double cashTips;
	private java.lang.Double chargedTips;
	private java.lang.Double tipsPaid;
	private java.lang.Double tipsDifferential;
	private java.lang.Integer payOutNumber;
	private java.lang.Double payOutAmount;
	private java.lang.Integer drawerBleedNumber;
	private java.lang.Double drawerBleedAmount;
	private java.lang.Double drawerAccountable;
	private java.lang.Double cashToDeposit;
	private java.lang.Double variance;
	private java.lang.Double totalVoidWst;
	private java.lang.Double totalVoid;
	private java.lang.Integer totalDiscountCount;
	private java.lang.Double totalDiscountAmount;
	private java.lang.Double totalDiscountSales;
	private java.lang.Integer totalDiscountGuest;
	private java.lang.Integer totalDiscountPartySize;
	private java.lang.Integer totalDiscountCheckSize;
	private java.lang.Double totalDiscountPercentage;
	private java.lang.Double totalDiscountRatio;

	// many to one
	private com.floreantpos.model.Terminal terminal;

	// collections
	private java.util.Set<DrawerPullVoidTicketEntry> voidTickets;



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
	 * Return the value associated with the column: REPORT_TIME
	 */
	public java.util.Date getReportTime () {
			return reportTime;
	}

	/**
	 * Set the value related to the column: REPORT_TIME
	 * @param reportTime the REPORT_TIME value
	 */
	public void setReportTime (java.util.Date reportTime) {
		this.reportTime = reportTime;
	}



	/**
	 * Return the value associated with the column: REG
	 */
	public java.lang.String getReg () {
			return reg;
	}

	/**
	 * Set the value related to the column: REG
	 * @param reg the REG value
	 */
	public void setReg (java.lang.String reg) {
		this.reg = reg;
	}



	/**
	 * Return the value associated with the column: TICKET_COUNT
	 */
	public java.lang.Integer getTicketCount () {
			return ticketCount == null ? Integer.valueOf(0) : ticketCount;
	}

	/**
	 * Set the value related to the column: TICKET_COUNT
	 * @param ticketCount the TICKET_COUNT value
	 */
	public void setTicketCount (java.lang.Integer ticketCount) {
		this.ticketCount = ticketCount;
	}



	/**
	 * Return the value associated with the column: BEGIN_CASH
	 */
	public java.lang.Double getBeginCash () {
					return beginCash == null ? Double.valueOf(0) : beginCash;
			}

	/**
	 * Set the value related to the column: BEGIN_CASH
	 * @param beginCash the BEGIN_CASH value
	 */
	public void setBeginCash (java.lang.Double beginCash) {
		this.beginCash = beginCash;
	}



	/**
	 * Return the value associated with the column: NET_SALES
	 */
	public java.lang.Double getNetSales () {
					return netSales == null ? Double.valueOf(0) : netSales;
			}

	/**
	 * Set the value related to the column: NET_SALES
	 * @param netSales the NET_SALES value
	 */
	public void setNetSales (java.lang.Double netSales) {
		this.netSales = netSales;
	}



	/**
	 * Return the value associated with the column: SALES_TAX
	 */
	public java.lang.Double getSalesTax () {
					return salesTax == null ? Double.valueOf(0) : salesTax;
			}

	/**
	 * Set the value related to the column: SALES_TAX
	 * @param salesTax the SALES_TAX value
	 */
	public void setSalesTax (java.lang.Double salesTax) {
		this.salesTax = salesTax;
	}



	/**
	 * Return the value associated with the column: CASH_TAX
	 */
	public java.lang.Double getCashTax () {
					return cashTax == null ? Double.valueOf(0) : cashTax;
			}

	/**
	 * Set the value related to the column: CASH_TAX
	 * @param cashTax the CASH_TAX value
	 */
	public void setCashTax (java.lang.Double cashTax) {
		this.cashTax = cashTax;
	}



	/**
	 * Return the value associated with the column: TOTAL_REVENUE
	 */
	public java.lang.Double getTotalRevenue () {
					return totalRevenue == null ? Double.valueOf(0) : totalRevenue;
			}

	/**
	 * Set the value related to the column: TOTAL_REVENUE
	 * @param totalRevenue the TOTAL_REVENUE value
	 */
	public void setTotalRevenue (java.lang.Double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}



	/**
	 * Return the value associated with the column: GROSS_RECEIPTS
	 */
	public java.lang.Double getGrossReceipts () {
					return grossReceipts == null ? Double.valueOf(0) : grossReceipts;
			}

	/**
	 * Set the value related to the column: GROSS_RECEIPTS
	 * @param grossReceipts the GROSS_RECEIPTS value
	 */
	public void setGrossReceipts (java.lang.Double grossReceipts) {
		this.grossReceipts = grossReceipts;
	}



	/**
	 * Return the value associated with the column: GIFTCERTRETURNCOUNT
	 */
	public java.lang.Integer getGiftCertReturnCount () {
			return giftCertReturnCount == null ? Integer.valueOf(0) : giftCertReturnCount;
	}

	/**
	 * Set the value related to the column: GIFTCERTRETURNCOUNT
	 * @param giftCertReturnCount the GIFTCERTRETURNCOUNT value
	 */
	public void setGiftCertReturnCount (java.lang.Integer giftCertReturnCount) {
		this.giftCertReturnCount = giftCertReturnCount;
	}



	/**
	 * Return the value associated with the column: GIFTCERTRETURNAMOUNT
	 */
	public java.lang.Double getGiftCertReturnAmount () {
					return giftCertReturnAmount == null ? Double.valueOf(0) : giftCertReturnAmount;
			}

	/**
	 * Set the value related to the column: GIFTCERTRETURNAMOUNT
	 * @param giftCertReturnAmount the GIFTCERTRETURNAMOUNT value
	 */
	public void setGiftCertReturnAmount (java.lang.Double giftCertReturnAmount) {
		this.giftCertReturnAmount = giftCertReturnAmount;
	}



	/**
	 * Return the value associated with the column: GIFTCERTCHANGEAMOUNT
	 */
	public java.lang.Double getGiftCertChangeAmount () {
					return giftCertChangeAmount == null ? Double.valueOf(0) : giftCertChangeAmount;
			}

	/**
	 * Set the value related to the column: GIFTCERTCHANGEAMOUNT
	 * @param giftCertChangeAmount the GIFTCERTCHANGEAMOUNT value
	 */
	public void setGiftCertChangeAmount (java.lang.Double giftCertChangeAmount) {
		this.giftCertChangeAmount = giftCertChangeAmount;
	}



	/**
	 * Return the value associated with the column: CASH_RECEIPT_NO
	 */
	public java.lang.Integer getCashReceiptNumber () {
			return cashReceiptNumber == null ? Integer.valueOf(0) : cashReceiptNumber;
	}

	/**
	 * Set the value related to the column: CASH_RECEIPT_NO
	 * @param cashReceiptNumber the CASH_RECEIPT_NO value
	 */
	public void setCashReceiptNumber (java.lang.Integer cashReceiptNumber) {
		this.cashReceiptNumber = cashReceiptNumber;
	}



	/**
	 * Return the value associated with the column: CASH_RECEIPT_AMOUNT
	 */
	public java.lang.Double getCashReceiptAmount () {
					return cashReceiptAmount == null ? Double.valueOf(0) : cashReceiptAmount;
			}

	/**
	 * Set the value related to the column: CASH_RECEIPT_AMOUNT
	 * @param cashReceiptAmount the CASH_RECEIPT_AMOUNT value
	 */
	public void setCashReceiptAmount (java.lang.Double cashReceiptAmount) {
		this.cashReceiptAmount = cashReceiptAmount;
	}



	/**
	 * Return the value associated with the column: CREDIT_CARD_RECEIPT_NO
	 */
	public java.lang.Integer getCreditCardReceiptNumber () {
			return creditCardReceiptNumber == null ? Integer.valueOf(0) : creditCardReceiptNumber;
	}

	/**
	 * Set the value related to the column: CREDIT_CARD_RECEIPT_NO
	 * @param creditCardReceiptNumber the CREDIT_CARD_RECEIPT_NO value
	 */
	public void setCreditCardReceiptNumber (java.lang.Integer creditCardReceiptNumber) {
		this.creditCardReceiptNumber = creditCardReceiptNumber;
	}



	/**
	 * Return the value associated with the column: CREDIT_CARD_RECEIPT_AMOUNT
	 */
	public java.lang.Double getCreditCardReceiptAmount () {
					return creditCardReceiptAmount == null ? Double.valueOf(0) : creditCardReceiptAmount;
			}

	/**
	 * Set the value related to the column: CREDIT_CARD_RECEIPT_AMOUNT
	 * @param creditCardReceiptAmount the CREDIT_CARD_RECEIPT_AMOUNT value
	 */
	public void setCreditCardReceiptAmount (java.lang.Double creditCardReceiptAmount) {
		this.creditCardReceiptAmount = creditCardReceiptAmount;
	}



	/**
	 * Return the value associated with the column: DEBIT_CARD_RECEIPT_NO
	 */
	public java.lang.Integer getDebitCardReceiptNumber () {
			return debitCardReceiptNumber == null ? Integer.valueOf(0) : debitCardReceiptNumber;
	}

	/**
	 * Set the value related to the column: DEBIT_CARD_RECEIPT_NO
	 * @param debitCardReceiptNumber the DEBIT_CARD_RECEIPT_NO value
	 */
	public void setDebitCardReceiptNumber (java.lang.Integer debitCardReceiptNumber) {
		this.debitCardReceiptNumber = debitCardReceiptNumber;
	}



	/**
	 * Return the value associated with the column: DEBIT_CARD_RECEIPT_AMOUNT
	 */
	public java.lang.Double getDebitCardReceiptAmount () {
					return debitCardReceiptAmount == null ? Double.valueOf(0) : debitCardReceiptAmount;
			}

	/**
	 * Set the value related to the column: DEBIT_CARD_RECEIPT_AMOUNT
	 * @param debitCardReceiptAmount the DEBIT_CARD_RECEIPT_AMOUNT value
	 */
	public void setDebitCardReceiptAmount (java.lang.Double debitCardReceiptAmount) {
		this.debitCardReceiptAmount = debitCardReceiptAmount;
	}



	/**
	 * Return the value associated with the column: RECEIPT_DIFFERENTIAL
	 */
	public java.lang.Double getReceiptDifferential () {
					return receiptDifferential == null ? Double.valueOf(0) : receiptDifferential;
			}

	/**
	 * Set the value related to the column: RECEIPT_DIFFERENTIAL
	 * @param receiptDifferential the RECEIPT_DIFFERENTIAL value
	 */
	public void setReceiptDifferential (java.lang.Double receiptDifferential) {
		this.receiptDifferential = receiptDifferential;
	}



	/**
	 * Return the value associated with the column: CASH_BACK
	 */
	public java.lang.Double getCashBack () {
					return cashBack == null ? Double.valueOf(0) : cashBack;
			}

	/**
	 * Set the value related to the column: CASH_BACK
	 * @param cashBack the CASH_BACK value
	 */
	public void setCashBack (java.lang.Double cashBack) {
		this.cashBack = cashBack;
	}



	/**
	 * Return the value associated with the column: CASH_TIPS
	 */
	public java.lang.Double getCashTips () {
					return cashTips == null ? Double.valueOf(0) : cashTips;
			}

	/**
	 * Set the value related to the column: CASH_TIPS
	 * @param cashTips the CASH_TIPS value
	 */
	public void setCashTips (java.lang.Double cashTips) {
		this.cashTips = cashTips;
	}



	/**
	 * Return the value associated with the column: CHARGED_TIPS
	 */
	public java.lang.Double getChargedTips () {
					return chargedTips == null ? Double.valueOf(0) : chargedTips;
			}

	/**
	 * Set the value related to the column: CHARGED_TIPS
	 * @param chargedTips the CHARGED_TIPS value
	 */
	public void setChargedTips (java.lang.Double chargedTips) {
		this.chargedTips = chargedTips;
	}



	/**
	 * Return the value associated with the column: TIPS_PAID
	 */
	public java.lang.Double getTipsPaid () {
					return tipsPaid == null ? Double.valueOf(0) : tipsPaid;
			}

	/**
	 * Set the value related to the column: TIPS_PAID
	 * @param tipsPaid the TIPS_PAID value
	 */
	public void setTipsPaid (java.lang.Double tipsPaid) {
		this.tipsPaid = tipsPaid;
	}



	/**
	 * Return the value associated with the column: TIPS_DIFFERENTIAL
	 */
	public java.lang.Double getTipsDifferential () {
					return tipsDifferential == null ? Double.valueOf(0) : tipsDifferential;
			}

	/**
	 * Set the value related to the column: TIPS_DIFFERENTIAL
	 * @param tipsDifferential the TIPS_DIFFERENTIAL value
	 */
	public void setTipsDifferential (java.lang.Double tipsDifferential) {
		this.tipsDifferential = tipsDifferential;
	}



	/**
	 * Return the value associated with the column: PAY_OUT_NO
	 */
	public java.lang.Integer getPayOutNumber () {
			return payOutNumber == null ? Integer.valueOf(0) : payOutNumber;
	}

	/**
	 * Set the value related to the column: PAY_OUT_NO
	 * @param payOutNumber the PAY_OUT_NO value
	 */
	public void setPayOutNumber (java.lang.Integer payOutNumber) {
		this.payOutNumber = payOutNumber;
	}



	/**
	 * Return the value associated with the column: PAY_OUT_AMOUNT
	 */
	public java.lang.Double getPayOutAmount () {
					return payOutAmount == null ? Double.valueOf(0) : payOutAmount;
			}

	/**
	 * Set the value related to the column: PAY_OUT_AMOUNT
	 * @param payOutAmount the PAY_OUT_AMOUNT value
	 */
	public void setPayOutAmount (java.lang.Double payOutAmount) {
		this.payOutAmount = payOutAmount;
	}



	/**
	 * Return the value associated with the column: DRAWER_BLEED_NO
	 */
	public java.lang.Integer getDrawerBleedNumber () {
			return drawerBleedNumber == null ? Integer.valueOf(0) : drawerBleedNumber;
	}

	/**
	 * Set the value related to the column: DRAWER_BLEED_NO
	 * @param drawerBleedNumber the DRAWER_BLEED_NO value
	 */
	public void setDrawerBleedNumber (java.lang.Integer drawerBleedNumber) {
		this.drawerBleedNumber = drawerBleedNumber;
	}



	/**
	 * Return the value associated with the column: DRAWER_BLEED_AMOUNT
	 */
	public java.lang.Double getDrawerBleedAmount () {
					return drawerBleedAmount == null ? Double.valueOf(0) : drawerBleedAmount;
			}

	/**
	 * Set the value related to the column: DRAWER_BLEED_AMOUNT
	 * @param drawerBleedAmount the DRAWER_BLEED_AMOUNT value
	 */
	public void setDrawerBleedAmount (java.lang.Double drawerBleedAmount) {
		this.drawerBleedAmount = drawerBleedAmount;
	}



	/**
	 * Return the value associated with the column: DRAWER_ACCOUNTABLE
	 */
	public java.lang.Double getDrawerAccountable () {
					return drawerAccountable == null ? Double.valueOf(0) : drawerAccountable;
			}

	/**
	 * Set the value related to the column: DRAWER_ACCOUNTABLE
	 * @param drawerAccountable the DRAWER_ACCOUNTABLE value
	 */
	public void setDrawerAccountable (java.lang.Double drawerAccountable) {
		this.drawerAccountable = drawerAccountable;
	}



	/**
	 * Return the value associated with the column: CASH_TO_DEPOSIT
	 */
	public java.lang.Double getCashToDeposit () {
					return cashToDeposit == null ? Double.valueOf(0) : cashToDeposit;
			}

	/**
	 * Set the value related to the column: CASH_TO_DEPOSIT
	 * @param cashToDeposit the CASH_TO_DEPOSIT value
	 */
	public void setCashToDeposit (java.lang.Double cashToDeposit) {
		this.cashToDeposit = cashToDeposit;
	}



	/**
	 * Return the value associated with the column: VARIANCE
	 */
	public java.lang.Double getVariance () {
					return variance == null ? Double.valueOf(0) : variance;
			}

	/**
	 * Set the value related to the column: VARIANCE
	 * @param variance the VARIANCE value
	 */
	public void setVariance (java.lang.Double variance) {
		this.variance = variance;
	}



	/**
	 * Return the value associated with the column: totalVoidWst
	 */
	public java.lang.Double getTotalVoidWst () {
					return totalVoidWst == null ? Double.valueOf(0) : totalVoidWst;
			}

	/**
	 * Set the value related to the column: totalVoidWst
	 * @param totalVoidWst the totalVoidWst value
	 */
	public void setTotalVoidWst (java.lang.Double totalVoidWst) {
		this.totalVoidWst = totalVoidWst;
	}



	/**
	 * Return the value associated with the column: totalVoid
	 */
	public java.lang.Double getTotalVoid () {
					return totalVoid == null ? Double.valueOf(0) : totalVoid;
			}

	/**
	 * Set the value related to the column: totalVoid
	 * @param totalVoid the totalVoid value
	 */
	public void setTotalVoid (java.lang.Double totalVoid) {
		this.totalVoid = totalVoid;
	}



	/**
	 * Return the value associated with the column: totalDiscountCount
	 */
	public java.lang.Integer getTotalDiscountCount () {
			return totalDiscountCount == null ? Integer.valueOf(0) : totalDiscountCount;
	}

	/**
	 * Set the value related to the column: totalDiscountCount
	 * @param totalDiscountCount the totalDiscountCount value
	 */
	public void setTotalDiscountCount (java.lang.Integer totalDiscountCount) {
		this.totalDiscountCount = totalDiscountCount;
	}



	/**
	 * Return the value associated with the column: totalDiscountAmount
	 */
	public java.lang.Double getTotalDiscountAmount () {
					return totalDiscountAmount == null ? Double.valueOf(0) : totalDiscountAmount;
			}

	/**
	 * Set the value related to the column: totalDiscountAmount
	 * @param totalDiscountAmount the totalDiscountAmount value
	 */
	public void setTotalDiscountAmount (java.lang.Double totalDiscountAmount) {
		this.totalDiscountAmount = totalDiscountAmount;
	}



	/**
	 * Return the value associated with the column: totalDiscountSales
	 */
	public java.lang.Double getTotalDiscountSales () {
					return totalDiscountSales == null ? Double.valueOf(0) : totalDiscountSales;
			}

	/**
	 * Set the value related to the column: totalDiscountSales
	 * @param totalDiscountSales the totalDiscountSales value
	 */
	public void setTotalDiscountSales (java.lang.Double totalDiscountSales) {
		this.totalDiscountSales = totalDiscountSales;
	}



	/**
	 * Return the value associated with the column: totalDiscountGuest
	 */
	public java.lang.Integer getTotalDiscountGuest () {
			return totalDiscountGuest == null ? Integer.valueOf(0) : totalDiscountGuest;
	}

	/**
	 * Set the value related to the column: totalDiscountGuest
	 * @param totalDiscountGuest the totalDiscountGuest value
	 */
	public void setTotalDiscountGuest (java.lang.Integer totalDiscountGuest) {
		this.totalDiscountGuest = totalDiscountGuest;
	}



	/**
	 * Return the value associated with the column: totalDiscountPartySize
	 */
	public java.lang.Integer getTotalDiscountPartySize () {
			return totalDiscountPartySize == null ? Integer.valueOf(0) : totalDiscountPartySize;
	}

	/**
	 * Set the value related to the column: totalDiscountPartySize
	 * @param totalDiscountPartySize the totalDiscountPartySize value
	 */
	public void setTotalDiscountPartySize (java.lang.Integer totalDiscountPartySize) {
		this.totalDiscountPartySize = totalDiscountPartySize;
	}



	/**
	 * Return the value associated with the column: totalDiscountCheckSize
	 */
	public java.lang.Integer getTotalDiscountCheckSize () {
			return totalDiscountCheckSize == null ? Integer.valueOf(0) : totalDiscountCheckSize;
	}

	/**
	 * Set the value related to the column: totalDiscountCheckSize
	 * @param totalDiscountCheckSize the totalDiscountCheckSize value
	 */
	public void setTotalDiscountCheckSize (java.lang.Integer totalDiscountCheckSize) {
		this.totalDiscountCheckSize = totalDiscountCheckSize;
	}



	/**
	 * Return the value associated with the column: totalDiscountPercentage
	 */
	public java.lang.Double getTotalDiscountPercentage () {
					return totalDiscountPercentage == null ? Double.valueOf(0) : totalDiscountPercentage;
			}

	/**
	 * Set the value related to the column: totalDiscountPercentage
	 * @param totalDiscountPercentage the totalDiscountPercentage value
	 */
	public void setTotalDiscountPercentage (java.lang.Double totalDiscountPercentage) {
		this.totalDiscountPercentage = totalDiscountPercentage;
	}



	/**
	 * Return the value associated with the column: totalDiscountRatio
	 */
	public java.lang.Double getTotalDiscountRatio () {
					return totalDiscountRatio == null ? Double.valueOf(0) : totalDiscountRatio;
			}

	/**
	 * Set the value related to the column: totalDiscountRatio
	 * @param totalDiscountRatio the totalDiscountRatio value
	 */
	public void setTotalDiscountRatio (java.lang.Double totalDiscountRatio) {
		this.totalDiscountRatio = totalDiscountRatio;
	}



	/**
	 * Return the value associated with the column: TERMINAL_ID
	 */
	public com.floreantpos.model.Terminal getTerminal () {
			return terminal;
	}

	/**
	 * Set the value related to the column: TERMINAL_ID
	 * @param terminal the TERMINAL_ID value
	 */
	public void setTerminal (com.floreantpos.model.Terminal terminal) {
		this.terminal = terminal;
	}



	/**
	 * Return the value associated with the column: voidTickets
	 */
	public java.util.Set<DrawerPullVoidTicketEntry> getVoidTickets () {
			return voidTickets;
	}

	/**
	 * Set the value related to the column: voidTickets
	 * @param voidTickets the voidTickets value
	 */
	public void setVoidTickets (java.util.Set<DrawerPullVoidTicketEntry> voidTickets) {
		this.voidTickets = voidTickets;
	}





	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.floreantpos.model.DrawerPullReport)) return false;
		else {
			com.floreantpos.model.DrawerPullReport drawerPullReport = (com.floreantpos.model.DrawerPullReport) obj;
			if (null == this.getId() || null == drawerPullReport.getId()) return false;
			else return (this.getId().equals(drawerPullReport.getId()));
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