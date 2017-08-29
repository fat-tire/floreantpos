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
package com.floreantpos.model;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.floreantpos.config.CardConfig;
import com.floreantpos.model.base.BasePosTransaction;
import com.floreantpos.util.GlobalIdGenerator;
import com.floreantpos.util.POSUtil;

public class PosTransaction extends BasePosTransaction {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public PosTransaction() {
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

	private String cardTrack;
	private String cardNo;
	private String cardExpYear;
	private String cardExpMonth;

	public final static String CASH = "CASH"; //$NON-NLS-1$
	public final static String GIFT_CERT = "GIFT_CERT"; //$NON-NLS-1$
	public final static String CREDIT_CARD = "CREDIT_CARD"; //$NON-NLS-1$
	public final static String DEBIT_CARD = "DEBIT_CARD"; //$NON-NLS-1$
	public final static String CASH_DROP = "CASH_DROP"; //$NON-NLS-1$
	public final static String REFUND = "REFUND"; //$NON-NLS-1$
	public final static String PAY_OUT = "PAY_OUT"; //$NON-NLS-1$
	public final static String VOID_TRANS = "VOID_TRANS"; //$NON-NLS-1$

	@Override
	protected void initialize() {
		setGlobalId(GlobalIdGenerator.generateGlobalId());
	}

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

		double advanceTipsPercentage = CardConfig.getAdvanceTipsPercentage();
		return getTenderAmount() + getTenderAmount() * (advanceTipsPercentage / 100);
	}

	public String getCardTrack() {
		return cardTrack;
	}

	public void setCardTrack(String cardTrack) {
		this.cardTrack = cardTrack;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardExpYear() {
		return cardExpYear;
	}

	public void setCardExpYear(String expYear) {
		this.cardExpYear = expYear;
	}

	public String getCardExpMonth() {
		return cardExpMonth;
	}

	public void setCardExpMonth(String expMonth) {
		this.cardExpMonth = expMonth;
	}

	public String getTicketId() {
		Ticket ticket = getTicket();
		if (ticket == null) {
			return "";
		}
		return String.valueOf(ticket.getId());
	}

	public void setId(Object generateGlobalId) {
		if (generateGlobalId instanceof String) {
			super.setId(NumberUtils.toInt(String.valueOf(generateGlobalId)));
		}
		else {
			super.setId(Integer.valueOf(String.valueOf(generateGlobalId)));
		}
	}
}