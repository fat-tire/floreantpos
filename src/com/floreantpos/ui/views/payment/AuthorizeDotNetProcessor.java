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

import java.math.BigDecimal;

import net.authorize.Environment;
import net.authorize.Merchant;
import net.authorize.TransactionType;
import net.authorize.aim.Transaction;
import net.authorize.aim.cardpresent.Result;
import net.authorize.data.creditcard.CardType;
import net.authorize.data.creditcard.CreditCard;

import org.apache.commons.lang.StringUtils;

import us.fatehi.magnetictrack.bankcard.BankCardMagneticTrack;

import com.floreantpos.Messages;
import com.floreantpos.PosLog;
import com.floreantpos.config.CardConfig;
import com.floreantpos.model.PosTransaction;

public class AuthorizeDotNetProcessor implements CardProcessor {
	public void preAuth(PosTransaction transaction) throws Exception {
		Environment environment = createEnvironment();
		Merchant merchant = createMerchant(environment);

		CreditCard creditCard = createCard(transaction);

		// Create transaction
		Transaction authCaptureTransaction = merchant.createAIMTransaction(TransactionType.AUTH_ONLY, new BigDecimal(transaction.calculateAuthorizeAmount()));
		authCaptureTransaction.setCreditCard(creditCard);

		Result<Transaction> result = (Result<Transaction>) merchant.postTransaction(authCaptureTransaction);

		if (result.isApproved()) {
			transaction.setCaptured(false);
			transaction.setAuthorizable(true);
			populateResult(result, transaction);
		}
		else if (result.isDeclined()) {
			throw new Exception(Messages.getString("AuthorizeDotNetProcessor.2") + result.getResponseReasonCodes().get(0).getReasonText()); //$NON-NLS-1$
		}
		else {
			throw new Exception(Messages.getString("AuthorizeDotNetProcessor.3") + result.getResponseReasonCodes().get(0).getReasonText()); //$NON-NLS-1$
		}
	}

	@Override
	public void chargeAmount(PosTransaction transaction) throws Exception {
		Environment environment = createEnvironment();
		Merchant merchant = createMerchant(environment);

		CreditCard creditCard = createCard(transaction);

		// Create transaction
		Transaction authCaptureTransaction = merchant.createAIMTransaction(TransactionType.AUTH_CAPTURE, new BigDecimal(transaction.getAmount()));
		authCaptureTransaction.setCreditCard(creditCard);

		Result<Transaction> result = (Result<Transaction>) merchant.postTransaction(authCaptureTransaction);

		if (result.isApproved()) {
			transaction.setCaptured(true);
			transaction.setAuthorizable(false);
			populateResult(result, transaction);
		}
		else if (result.isDeclined()) {
			throw new Exception(Messages.getString("AuthorizeDotNetProcessor.2") + result.getResponseReasonCodes().get(0).getReasonText()); //$NON-NLS-1$
		}
		else {
			throw new Exception(Messages.getString("AuthorizeDotNetProcessor.3") + result.getResponseReasonCodes().get(0).getReasonText()); //$NON-NLS-1$
		}
	}

	private void populateResult(Result<Transaction> result, PosTransaction transaction) {
		transaction.setCardTransactionId(result.getTransId());
		transaction.setCardAuthCode(result.getAuthCode());
		CreditCard card = result.getTarget().getCreditCard();
		transaction.setCardType(card.getCardType().name());
		transaction.setCardNumber(card.getCreditCardNumber());
		String rawTrackData = "%" + card.getTrack1() + "?;" + card.getTrack2() + "?";
		BankCardMagneticTrack track = BankCardMagneticTrack.from(rawTrackData);
		transaction.setCardHolderName(track.getTrack1().getName().toString());
		transaction.setCardExpYear(card.getExpirationYear());
		transaction.setCardExpMonth(card.getExpirationMonth());
	}

	public void captureAuthAmount(PosTransaction transaction) throws Exception {
		Environment environment = createEnvironment();
		Merchant merchant = createMerchant(environment);

		// Create transaction
		Transaction authCaptureTransaction = merchant.createAIMTransaction(TransactionType.PRIOR_AUTH_CAPTURE, new BigDecimal(transaction.getAmount()));
		authCaptureTransaction.setTransactionId(transaction.getCardTransactionId());

		Result<Transaction> result = (Result<Transaction>) merchant.postTransaction(authCaptureTransaction);

		if (result.isApproved()) {
			transaction.setCaptured(true);
			transaction.setAuthorizable(false);
		}
		else if (result.isDeclined()) {
			throw new Exception(Messages.getString("AuthorizeDotNetProcessor.6") + result.getResponseReasonCodes().get(0).getReasonText()); //$NON-NLS-1$
		}
		else {
			throw new Exception(Messages.getString("AuthorizeDotNetProcessor.7") + result.getResponseReasonCodes().get(0).getReasonText()); //$NON-NLS-1$
		}
	}

	public void captureNewAmount(PosTransaction transaction) throws Exception {
		Environment environment = createEnvironment();
		Merchant merchant = createMerchant(environment);

		CreditCard creditCard = createCard(transaction);

		// Create transaction
		Transaction authCaptureTransaction = merchant.createAIMTransaction(TransactionType.AUTH_CAPTURE, new BigDecimal(transaction.getAmount()));
		authCaptureTransaction.setCreditCard(creditCard);

		Result<Transaction> result = (Result<Transaction>) merchant.postTransaction(authCaptureTransaction);

		if (result.isApproved()) {
			transaction.setCaptured(true);
			transaction.setAuthorizable(false);
			populateResult(result, transaction);
		}
		else if (result.isDeclined()) {
			throw new Exception(Messages.getString("AuthorizeDotNetProcessor.8") + result.getResponseReasonCodes().get(0).getReasonText()); //$NON-NLS-1$
		}
		else {
			throw new Exception(Messages.getString("AuthorizeDotNetProcessor.9") + result.getResponseReasonCodes().get(0).getReasonText()); //$NON-NLS-1$
		}
	}

	public void voidAmount(PosTransaction transaction) throws Exception {
		Environment environment = createEnvironment();
		Merchant merchant = createMerchant(environment);

		// Create transaction
		Transaction authCaptureTransaction = merchant.createAIMTransaction(TransactionType.VOID, new BigDecimal(transaction.getAmount()));
		authCaptureTransaction.setTransactionId(transaction.getCardTransactionId());

		Result<Transaction> result = (Result<Transaction>) merchant.postTransaction(authCaptureTransaction);

		if (result.isApproved()) {
			transaction.setCaptured(true);
			transaction.setAuthorizable(false);
			populateResult(result, transaction);
		}
		else if (result.isDeclined()) {
			throw new Exception(Messages.getString("AuthorizeDotNetProcessor.10") + result.getResponseReasonCodes().get(0).getReasonText()); //$NON-NLS-1$
		}
		else {
			throw new Exception(Messages.getString("AuthorizeDotNetProcessor.11") + result.getResponseReasonCodes().get(0).getReasonText()); //$NON-NLS-1$
		}
	}

//	public void voidAmount(String transId, double amount) throws Exception {
//		Environment environment = createEnvironment();
//		Merchant merchant = createMerchant(environment);
//
//		// Create transaction
//		Transaction authCaptureTransaction = merchant.createAIMTransaction(TransactionType.VOID, new BigDecimal(amount));
//		authCaptureTransaction.setTransactionId(transId);
//
//		Result<Transaction> result = (Result<Transaction>) merchant.postTransaction(authCaptureTransaction);
//
//		if (result.isApproved()) {
//			transaction.setCaptured(true);
//			transaction.setAuthorizable(false);
//			populateResult(result, transaction);		transaction.setAuthorizationCode(result.getAuthCode());
//		}
//		else if (result.isDeclined()) {
//			throw new Exception(Messages.getString("AuthorizeDotNetProcessor.12") + result.getResponseReasonCodes().get(0).getReasonText()); //$NON-NLS-1$
//		}
//		else {
//			throw new Exception(Messages.getString("AuthorizeDotNetProcessor.13") + result.getResponseReasonCodes().get(0).getReasonText()); //$NON-NLS-1$
//		}
//	}

	private CreditCard createCard(PosTransaction transaction) {
		CreditCard creditCard = CreditCard.createCreditCard();
		creditCard.setCardType(CardType.findByValue(transaction.getCardType()));

		if (StringUtils.isNotEmpty(transaction.getCardTrack())) {
			return createCard(transaction.getCardTrack(), transaction.getCardType());

		}
		else {
			return createCard(transaction.getCardNumber(), transaction.getCardExpMonth(), transaction.getCardExpYear(), transaction.getCardType());
		}
	}

	private CreditCard createCard(String cardTrack, String cardType) {
		CreditCard creditCard = CreditCard.createCreditCard();
		creditCard.setCardType(CardType.findByValue(cardType));

		//%B4111111111111111^SHAH/RIAR^1803101000000000020000831000000?;4111111111111111=1803101000020000831?
		String[] tracks = cardTrack.split(";"); //$NON-NLS-1$

		creditCard.setTrack1(tracks[0]);
		if (tracks.length > 1) {
			creditCard.setTrack2(";" + tracks[1]); //$NON-NLS-1$
		}

		return creditCard;
	}

	private CreditCard createCard(String cardNumber, String expMonth, String expYear, String cardType) {
		CreditCard creditCard = CreditCard.createCreditCard();
		creditCard.setCardType(CardType.findByValue(cardType));

		creditCard.setExpirationYear(expYear);
		creditCard.setExpirationMonth(expMonth);
		creditCard.setCreditCardNumber(cardNumber);

		return creditCard;
	}

	private Merchant createMerchant(Environment environment) throws Exception {
		String apiLoginID = CardConfig.getMerchantAccount();
		String transactionKey = CardConfig.getMerchantPass();
		//String MD5Value = "paltalk123";

		Merchant merchant = Merchant.createMerchant(environment, apiLoginID, transactionKey);
		merchant.setDeviceType(net.authorize.DeviceType.VIRTUAL_TERMINAL);
		merchant.setMarketType(net.authorize.MarketType.RETAIL);
		//merchant.setMD5Value(MD5Value);
		return merchant;
	}

	private Environment createEnvironment() {
		Environment environment = Environment.PRODUCTION;
		if (CardConfig.isSandboxMode()) {
			environment = Environment.SANDBOX;
		}
		return environment;
	}

	public static void main(String[] args) throws Exception {
		//		String apiLoginID = "6tuU4N3H";
		//		String transactionKey = "4k6955x3T8bCVPVm";
		//		//String MD5Value = "paltalk123";
		//
		//		Environment environment = Environment.SANDBOX;
		//
		//		Merchant merchant = Merchant.createMerchant(environment, apiLoginID, transactionKey);
		//		merchant.setDeviceType(net.authorize.DeviceType.VIRTUAL_TERMINAL);
		//		merchant.setMarketType(net.authorize.MarketType.RETAIL);
		//		//merchant.setMD5Value(MD5Value);
		//
		//		// Create credit card
		//		CreditCard creditCard = createCard("%B4111111111111111^SHAH/RIAR^1803101000000000020000831000000?;4111111111111111=1803101000020000831?", CardType.VISA.name());
		//		
		//		Transaction authTransaction = merchant.createAIMTransaction(TransactionType.AUTH_ONLY, new BigDecimal(100));
		//		authTransaction.setCreditCard(creditCard);
		//		
		//		Result<Transaction> result = (Result<Transaction>) merchant.postTransaction(authTransaction);
		//		
		//		if (result.isApproved()) {
		//			
		//			PosLog.debug(getClass(),"authorization successful");
		//			
		//			Thread.sleep(1000);
		//			
		//			Transaction authCaptureTransaction = merchant.createAIMTransaction(TransactionType.PRIOR_AUTH_CAPTURE, new BigDecimal(100));
		//			authCaptureTransaction.setTransactionId(result.getTransId());
		//			authCaptureTransaction.setCreditCard(creditCard);
		//
		//			Result<Transaction> result2 = (Result<Transaction>) merchant.postTransaction(authCaptureTransaction);
		//			
		//			if (result2.isApproved()) {
		//				PosLog.debug(getClass(),"capture successful");
		//				
		//				Thread.sleep(1000);
		//				
		//				Transaction voidTransaction = merchant.createAIMTransaction(TransactionType.VOID, new BigDecimal(100));
		//				voidTransaction.setTransactionId(result.getTransId());
		//				voidTransaction.setCreditCard(creditCard);
		//				
		//				Result<Transaction> result3 = (Result<Transaction>) merchant.postTransaction(authCaptureTransaction);
		//				
		//				if(result3.isApproved()) {
		//					PosLog.debug(getClass(),"void successful");
		//				}
		//				else {
		//					PosLog.debug(getClass(),"void declined");
		//				}
		//			}
		//		}
		//		else {
		//			PosLog.debug(getClass(),result.getXmlResponseDocument());
		//		}

		final BankCardMagneticTrack track = BankCardMagneticTrack
				.from("%B4111111111111111^SHAH/RIAR^1803101000000000020000831000000?;4111111111111111=1803101000020000831?"); //$NON-NLS-1$
		PosLog.info(AuthorizeDotNetProcessor.class, "" + track.getTrack1());

	}

	@Override
	public void voidTransaction(PosTransaction transaction) throws Exception {
		
	}

	@Override
	public String getCardInformationForReceipt(PosTransaction transaction) {
		return null;
	}

	@Override
	public void cancelTransaction() {
	}

	@Override
	public boolean supportTipsAdjustMent() {
		return false;
	}


	@Override
	public void adjustTips(PosTransaction transaction) throws Exception {
		
	}
}
