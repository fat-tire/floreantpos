package com.floreantpos.ui.views.payment;

import java.math.BigDecimal;

import net.authorize.Environment;
import net.authorize.Merchant;
import net.authorize.TransactionType;
import net.authorize.aim.Transaction;
import net.authorize.aim.cardpresent.Result;
import net.authorize.data.creditcard.CardType;
import net.authorize.data.creditcard.CreditCard;

import org.apache.commons.validator.routines.CreditCardValidator;

import com.floreantpos.config.CardConfig;

public class CreditCardTransactionProcessor {
	public static String processUsingAuthorizeDotNet(String cardTracks, double tenderedAmount, CardType cardType) throws Exception {
		//		private static String apiLoginID = "6tuU4N3H";
		//	    private static String transactionKey = "4k6955x3T8bCVPVm"; 

		String apiLoginID = CardConfig.getMerchantAccount();
		String transactionKey = CardConfig.getMerchantPass();
		//String MD5Value = "paltalk123";

		Environment environment = Environment.PRODUCTION;
		if (CardConfig.isSandboxMode()) {
			environment = Environment.SANDBOX;
		}

		Merchant merchant = Merchant.createMerchant(environment, apiLoginID, transactionKey);
		merchant.setDeviceType(net.authorize.DeviceType.VIRTUAL_TERMINAL);
		merchant.setMarketType(net.authorize.MarketType.RETAIL);
		//merchant.setMD5Value(MD5Value);

		
		
		
		// Create credit card
		CreditCard creditCard = CreditCard.createCreditCard();
		creditCard.setCardType(cardType);

		//%B4111111111111111^SHAH/RIAR^1803101000000000020000831000000?;4111111111111111=1803101000020000831?
		String[] tracks = cardTracks.split(";");

		creditCard.setTrack1(tracks[0]);
		if (tracks.length > 1) {
			creditCard.setTrack2(";" + tracks[1]);
		}

		// Create transaction
		Transaction authCaptureTransaction = merchant.createAIMTransaction(TransactionType.AUTH_CAPTURE, new BigDecimal(tenderedAmount));
		authCaptureTransaction.setCreditCard(creditCard);

		Result<Transaction> result = (Result<Transaction>) merchant.postTransaction(authCaptureTransaction);

		if (result.isApproved()) {
			//POSMessageDialog.showMessage("Approved!</br>" + "Transaction Id: " + result.getTransId());
			return result.getAuthCode();
		}
		else if (result.isDeclined()) {
			throw new Exception("Card declined" + result.getResponseReasonCodes().get(0).getReasonText());
			//POSMessageDialog.showMessage("Declined.</br>");
			//System.out.println(result.getResponseReasonCodes().get(0) + " : " + result.getResponseReasonCodes().get(0).getReasonText());
		}
		else {
			throw new Exception("Card error" + result.getResponseReasonCodes().get(0).getReasonText());
			//POSMessageDialog.showMessage("Error.</br>");
			//System.out.println(result.getResponseReasonCodes().get(0) + " : " + result.getResponseReasonCodes().get(0).getReasonText());
		}
	}
	
	public static void main(String[] args) {
		//%B4111111111111111^SHAH/RIAR^1803101000000000020000831000000?;4111111111111111=1803101000020000831?
		Object valid = CreditCardValidator.VISA_VALIDATOR.validate("4111111111111111^SHAH/RIAR^1803101000000000020000831000000?;4111111111111111=1803101000020000831?");
		
		System.out.println(valid);
	}
}
