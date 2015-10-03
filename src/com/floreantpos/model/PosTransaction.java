package com.floreantpos.model;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.model.base.BasePosTransaction;
import com.floreantpos.util.POSUtil;

public class PosTransaction extends BasePosTransaction {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public PosTransaction() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public PosTransaction(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public PosTransaction(java.lang.Integer id, java.lang.String transactionType, java.lang.String paymentType) {

		super(id, transactionType, paymentType);
	}

	/*[CONSTRUCTOR MARKER END]*/

	public final static String CASH = "CASH"; //$NON-NLS-1$
	public final static String GIFT_CERT = "GIFT_CERT"; //$NON-NLS-1$
	public final static String CREDIT_CARD = "CREDIT_CARD"; //$NON-NLS-1$
	public final static String DEBIT_CARD = "DEBIT_CARD"; //$NON-NLS-1$
	public final static String CASH_DROP = "CASH_DROP"; //$NON-NLS-1$
	public final static String REFUND = "REFUND"; //$NON-NLS-1$
	public final static String PAY_OUT = "PAY_OUT"; //$NON-NLS-1$
	public final static String VOID_TRANS = "VOID_TRANS"; //$NON-NLS-1$

	@Override
	public String getTransactionType() {
		String type = super.getTransactionType();

		if (StringUtils.isEmpty(type)) {
			return TransactionType.CREDIT.name();
		}

		return type;
	}

	public void updateTerminalBalance() {
		Terminal terminal = getTerminal();
		if (terminal == null) {
			return;
		}

		Double amount = getAmount();
		if (amount == null || amount == 0) {
			return;
		}

		double terminalBalance = terminal.getCurrentBalance();

		TransactionType transactionType = TransactionType.valueOf(getTransactionType());
		switch (transactionType) {
			case CREDIT:
				terminalBalance += amount;
				break;

			case DEBIT:
				terminalBalance -= amount;
		}

		terminal.setCurrentBalance(terminalBalance);
	}

	public boolean isCard() {
		return (this instanceof CreditCardTransaction) || (this instanceof DebitCardTransaction);
	}

	public void addProperty(String name, String value) {
		if (getProperties() == null) {
			setProperties(new HashMap<String, String>());
		}

		getProperties().put(name, value);
	}

	public boolean hasProperty(String key) {
		return getProperty(key) != null;
	}

	public String getProperty(String key) {
		if (getProperties() == null) {
			return null;
		}

		return getProperties().get(key);
	}

	public boolean isPropertyValueTrue(String propertyName) {
		String property = getProperty(propertyName);

		return POSUtil.getBoolean(property);
	}

	public Double calculateTotalAmount() {
		return getAmount() + getTipsAmount();
	}

	public Double calculateAuthorizeAmount() {
		return getTenderAmount() + getTenderAmount() * 0.2;
	}
}