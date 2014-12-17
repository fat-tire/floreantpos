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

import com.floreantpos.config.CardConfig;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Ticket;

public class AuthorizeDotNetProcessor implements CardProcessor {

	public String authorizeAmount(Ticket ticket, String cardTracks, double amount, String cardType) throws Exception {
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

	public void authorizeAmount(PosTransaction transaction) throws Exception {
		Environment environment = createEnvironment();
		Merchant merchant = createMerchant(environment);

		CreditCard creditCard = createCard(transaction);

		// Create transaction
		Transaction authCaptureTransaction = merchant.createAIMTransaction(TransactionType.AUTH_ONLY, new BigDecimal(transaction.calculateAuthorizeAmount()));
		authCaptureTransaction.setCreditCard(creditCard);

		Result<Transaction> result = (Result<Transaction>) merchant.postTransaction(authCaptureTransaction);

		if (result.isApproved()) {
			transaction.setCardTransactionId(result.getTransId());
			transaction.setCardAuthCode(result.getAuthCode());
		}
		else if (result.isDeclined()) {
			throw new Exception("Card declined\n" + result.getResponseReasonCodes().get(0).getReasonText());
		}
		else {
			throw new Exception("Error\n" + result.getResponseReasonCodes().get(0).getReasonText());
		}
	}

	public String authorizeAmount(String cardNumber, String expMonth, String expYear, double amount, CardType cardType) throws Exception {
		Environment environment = createEnvironment();
		Merchant merchant = createMerchant(environment);

		CreditCard creditCard = CreditCard.createCreditCard();
		creditCard.setCardType(cardType);
		creditCard.setExpirationYear(expYear);
		creditCard.setExpirationMonth(expMonth);
		creditCard.setCreditCardNumber(cardNumber);

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

	public void captureAuthorizedAmount(PosTransaction transaction) throws Exception {
		Environment environment = createEnvironment();
		Merchant merchant = createMerchant(environment);

		// Create transaction
		Transaction authCaptureTransaction = merchant.createAIMTransaction(TransactionType.PRIOR_AUTH_CAPTURE, new BigDecimal(transaction.getAmount()));
		authCaptureTransaction.setTransactionId(transaction.getCardTransactionId());

		Result<Transaction> result = (Result<Transaction>) merchant.postTransaction(authCaptureTransaction);

		if (result.isApproved()) {
			transaction.setCardTransactionId(result.getTransId());
			transaction.setCardAuthCode(result.getAuthCode());
		}
		else if (result.isDeclined()) {
			throw new Exception("Transaction declined\n" + result.getResponseReasonCodes().get(0).getReasonText());
		}
		else {
			throw new Exception("Error\n" + result.getResponseReasonCodes().get(0).getReasonText());
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
			transaction.setCardTransactionId(result.getTransId());
			transaction.setCardAuthCode(result.getAuthCode());
		}
		else if (result.isDeclined()) {
			throw new Exception("Card declined\n" + result.getResponseReasonCodes().get(0).getReasonText());
		}
		else {
			throw new Exception("Error\n" + result.getResponseReasonCodes().get(0).getReasonText());
		}
	}

	public void voidAmount(PosTransaction transaction) throws Exception {
		Environment environment = createEnvironment();
		Merchant merchant = createMerchant(environment);

		// Create transaction
		Transaction authCaptureTransaction = merchant.createAIMTransaction(TransactionType.VOID, new BigDecimal(transaction.calculateAuthorizeAmount()));
		authCaptureTransaction.setTransactionId(transaction.getCardTransactionId());

		Result<Transaction> result = (Result<Transaction>) merchant.postTransaction(authCaptureTransaction);

		if (result.isApproved()) {
			transaction.setCardTransactionId(result.getTransId());
			transaction.setCardAuthCode(result.getAuthCode());
		}
		else if (result.isDeclined()) {
			throw new Exception("Transaction declined\n" + result.getResponseReasonCodes().get(0).getReasonText());
		}
		else {
			throw new Exception("Error\n" + result.getResponseReasonCodes().get(0).getReasonText());
		}
	}

	public void voidAmount(String transId, double amount) throws Exception {
		Environment environment = createEnvironment();
		Merchant merchant = createMerchant(environment);

		// Create transaction
		Transaction authCaptureTransaction = merchant.createAIMTransaction(TransactionType.VOID, new BigDecimal(amount));
		authCaptureTransaction.setTransactionId(transId);

		Result<Transaction> result = (Result<Transaction>) merchant.postTransaction(authCaptureTransaction);

		if (result.isApproved()) {
			//			transaction.setCardTransactionId(result.getTransId());
			//			transaction.setAuthorizationCode(result.getAuthCode());
		}
		else if (result.isDeclined()) {
			throw new Exception("Transaction declined\n" + result.getResponseReasonCodes().get(0).getReasonText());
		}
		else {
			throw new Exception("Error\n" + result.getResponseReasonCodes().get(0).getReasonText());
		}
	}

	private CreditCard createCard(PosTransaction transaction) {
		CreditCard creditCard = CreditCard.createCreditCard();
		creditCard.setCardType(CardType.findByValue(transaction.getCardType()));

		if (StringUtils.isNotEmpty(transaction.getCardTrack())) {
			return createCard(transaction.getCardTrack(), transaction.getCardType());

		}
		else {
			return createCard(transaction.getCardNumber(), transaction.getCardExpiryMonth(), transaction.getCardExpiryYear(), transaction.getCardType());
		}
	}

	private CreditCard createCard(String cardTrack, String cardType) {
		CreditCard creditCard = CreditCard.createCreditCard();
		creditCard.setCardType(CardType.findByValue(cardType));

		//%B4111111111111111^SHAH/RIAR^1803101000000000020000831000000?;4111111111111111=1803101000020000831?
		String[] tracks = cardTrack.split(";");

		creditCard.setTrack1(tracks[0]);
		if (tracks.length > 1) {
			creditCard.setTrack2(";" + tracks[1]);
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
		//			System.out.println("authorization successful");
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
		//				System.out.println("capture successful");
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
		//					System.out.println("void successful");
		//				}
		//				else {
		//					System.out.println("void declined");
		//				}
		//			}
		//		}
		//		else {
		//			System.out.println(result.getXmlResponseDocument());
		//		}

		final BankCardMagneticTrack track = BankCardMagneticTrack
				.from("%B4111111111111111^SHAH/RIAR^1803101000000000020000831000000?;4111111111111111=1803101000020000831?");
		System.out.println(track.getTrack1());

	}
}
