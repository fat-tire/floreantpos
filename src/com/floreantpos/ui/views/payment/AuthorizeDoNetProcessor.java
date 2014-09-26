package com.floreantpos.ui.views.payment;

import java.math.BigDecimal;

import net.authorize.Environment;
import net.authorize.Merchant;
import net.authorize.TransactionType;
import net.authorize.aim.Transaction;
import net.authorize.aim.cardpresent.Result;
import net.authorize.data.creditcard.CardType;
import net.authorize.data.creditcard.CreditCard;

import com.floreantpos.config.CardConfig;

public class AuthorizeDoNetProcessor {
	public static String process(String cardNumber, String expMonth, String expYear, double tenderedAmount, CardType cardType) throws Exception {
		//		private static String apiLoginID = "6tuU4N3H";
		//	    private static String transactionKey = "4k6955x3T8bCVPVm"; 

		Environment environment = createEnvironment();
		Merchant merchant = createMerchant(environment);

		// Create credit card
		CreditCard creditCard = CreditCard.createCreditCard();
		creditCard.setCardType(cardType);

		//%B4111111111111111^SHAH/RIAR^1803101000000000020000831000000?;4111111111111111=1803101000020000831?

		creditCard.setCreditCardNumber(cardNumber);
		creditCard.setExpirationMonth(expMonth);
		creditCard.setExpirationYear(expYear);

		// Create transaction
		Transaction authCaptureTransaction = merchant.createAIMTransaction(TransactionType.AUTH_CAPTURE, new BigDecimal(tenderedAmount));
		authCaptureTransaction.setCreditCard(creditCard);

		Result<Transaction> result = (Result<Transaction>) merchant.postTransaction(authCaptureTransaction);

		if (result.isApproved()) {
			//POSMessageDialog.showMessage("Approved!</br>" + "Transaction Id: " + result.getTransId());
			return result.getAuthCode();
		}
		else if (result.isDeclined()) {
			throw new Exception("Card declined\n" + result.getResponseReasonCodes().get(0).getReasonText());
			//POSMessageDialog.showMessage("Declined.</br>");
			//System.out.println(result.getResponseReasonCodes().get(0) + " : " + result.getResponseReasonCodes().get(0).getReasonText());
		}
		else {
			throw new Exception("Error\n" + result.getResponseReasonCodes().get(0).getReasonText());
			//POSMessageDialog.showMessage("Error.</br>");
			//System.out.println(result.getResponseReasonCodes().get(0) + " : " + result.getResponseReasonCodes().get(0).getReasonText());
		}
	}
	
	public static String process(String cardTracks, double tenderedAmount, CardType cardType) throws Exception {
		Environment environment = createEnvironment();
		Merchant merchant = createMerchant(environment);

		CreditCard creditCard = createCard(cardTracks, cardType);

		// Create transaction
		Transaction authCaptureTransaction = merchant.createAIMTransaction(TransactionType.AUTH_CAPTURE, new BigDecimal(tenderedAmount));
		authCaptureTransaction.setCreditCard(creditCard);

		Result<Transaction> result = (Result<Transaction>) merchant.postTransaction(authCaptureTransaction);

		if (result.isApproved()) {
			//POSMessageDialog.showMessage("Approved!</br>" + "Transaction Id: " + result.getTransId());
			return result.getAuthCode();
		}
		else if (result.isDeclined()) {
			throw new Exception("Card declined\n" + result.getResponseReasonCodes().get(0).getReasonText());
			//POSMessageDialog.showMessage("Declined.</br>");
			//System.out.println(result.getResponseReasonCodes().get(0) + " : " + result.getResponseReasonCodes().get(0).getReasonText());
		}
		else {
			throw new Exception("Error\n" + result.getResponseReasonCodes().get(0).getReasonText());
			//POSMessageDialog.showMessage("Error.</br>");
			//System.out.println(result.getResponseReasonCodes().get(0) + " : " + result.getResponseReasonCodes().get(0).getReasonText());
		}
	}
	
	public static String authorizeAmount(String cardTracks, double amount, CardType cardType) throws Exception {
		Environment environment = createEnvironment();
		Merchant merchant = createMerchant(environment);

		CreditCard creditCard = createCard(cardTracks, cardType);

		// Create transaction
		Transaction authCaptureTransaction = merchant.createAIMTransaction(TransactionType.AUTH_ONLY, new BigDecimal(amount));
		authCaptureTransaction.setCreditCard(creditCard);

		Result<Transaction> result = (Result<Transaction>) merchant.postTransaction(authCaptureTransaction);

		if (result.isApproved()) {
			return result.getTransId();
		}
		else if (result.isDeclined()) {
			throw new Exception("Card declined\n" + result.getResponseReasonCodes().get(0).getReasonText());
		}
		else {
			throw new Exception("Error\n" + result.getResponseReasonCodes().get(0).getReasonText());
		}
	}
	
	public static String captureAmount(String transactionId, double amount) throws Exception {
		Environment environment = createEnvironment();
		Merchant merchant = createMerchant(environment);

		// Create transaction
		Transaction authCaptureTransaction = merchant.createAIMTransaction(TransactionType.PRIOR_AUTH_CAPTURE, new BigDecimal(amount));
		authCaptureTransaction.setTransactionId(transactionId);

		Result<Transaction> result = (Result<Transaction>) merchant.postTransaction(authCaptureTransaction);

		if (result.isApproved()) {
			return result.getAuthCode();
		}
		else if (result.isDeclined()) {
			throw new Exception("Transaction declined\n" + result.getResponseReasonCodes().get(0).getReasonText());
		}
		else {
			throw new Exception("Error\n" + result.getResponseReasonCodes().get(0).getReasonText());
		}
	}
	
	public static String voidAmount(String transactionId, double amount) throws Exception {
		Environment environment = createEnvironment();
		Merchant merchant = createMerchant(environment);
		
		// Create transaction
		Transaction authCaptureTransaction = merchant.createAIMTransaction(TransactionType.VOID, new BigDecimal(amount));
		authCaptureTransaction.setTransactionId(transactionId);
		
		Result<Transaction> result = (Result<Transaction>) merchant.postTransaction(authCaptureTransaction);
		
		if (result.isApproved()) {
			return result.getTransId();
		}
		else if (result.isDeclined()) {
			throw new Exception("Transaction declined\n" + result.getResponseReasonCodes().get(0).getReasonText());
		}
		else {
			throw new Exception("Error\n" + result.getResponseReasonCodes().get(0).getReasonText());
		}
	}

	private static CreditCard createCard(String cardTracks, CardType cardType) {
		// Create credit card
		CreditCard creditCard = CreditCard.createCreditCard();
		creditCard.setCardType(cardType);

		//%B4111111111111111^SHAH/RIAR^1803101000000000020000831000000?;4111111111111111=1803101000020000831?
		String[] tracks = cardTracks.split(";");

		creditCard.setTrack1(tracks[0]);
		if (tracks.length > 1) {
			creditCard.setTrack2(";" + tracks[1]);
		}
		return creditCard;
	}

	private static Merchant createMerchant(Environment environment) throws Exception {
		String apiLoginID = CardConfig.getMerchantAccount();
		String transactionKey = CardConfig.getMerchantPass();
		//String MD5Value = "paltalk123";

		Merchant merchant = Merchant.createMerchant(environment, apiLoginID, transactionKey);
		merchant.setDeviceType(net.authorize.DeviceType.VIRTUAL_TERMINAL);
		merchant.setMarketType(net.authorize.MarketType.RETAIL);
		//merchant.setMD5Value(MD5Value);
		return merchant;
	}

	private static Environment createEnvironment() {
		Environment environment = Environment.PRODUCTION;
		if (CardConfig.isSandboxMode()) {
			environment = Environment.SANDBOX;
		}
		return environment;
	}

	

	public static void main(String[] args) {
		String apiLoginID = "6tuU4N3H";
		String transactionKey = "4k6955x3T8bCVPVm";
		//String MD5Value = "paltalk123";

		Environment environment = Environment.SANDBOX;

		Merchant merchant = Merchant.createMerchant(environment, apiLoginID, transactionKey);
		merchant.setDeviceType(net.authorize.DeviceType.VIRTUAL_TERMINAL);
		merchant.setMarketType(net.authorize.MarketType.RETAIL);
		//merchant.setMD5Value(MD5Value);

		// Create credit card
		CreditCard creditCard = CreditCard.createCreditCard();
		creditCard.setCardType(CardType.VISA);

		//%B4111111111111111^SHAH/RIAR^1803101000000000020000831000000?;4111111111111111=1803101000020000831?

		//%B4111111111111111^SHAH/RIAR^1803101000000000020000831000000?;4111111111111111=1803101000020000831?
//		String cardTracks = "%B4111111111111111^SHAH/RIAR^1803101000000000020000831000000?;4111111111111111=1803101000020000831?";
//		String[] tracks = cardTracks.split(";");
//
//		creditCard.setTrack1(tracks[0]);
//		if (tracks.length > 1) {
//			creditCard.setTrack2(";" + tracks[1]);
//		}

		// Create transaction
		Transaction authCaptureTransaction = merchant.createAIMTransaction(TransactionType.PRIOR_AUTH_CAPTURE, new BigDecimal(100));
		authCaptureTransaction.setTransactionId("2221368345");
		authCaptureTransaction.setCreditCard(creditCard);

		Result<Transaction> result = (Result<Transaction>) merchant.postTransaction(authCaptureTransaction);
		if(result.isApproved()) {
			System.out.println("approved");
		}
		if(result.isDeclined()) {
			System.out.println("declined");
		}
		if(result.isError()) {
			System.out.println("error");
		}
		System.out.println("transaction id: " + result.getTransId());
	}
}
