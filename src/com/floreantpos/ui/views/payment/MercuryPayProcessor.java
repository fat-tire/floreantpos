package com.floreantpos.ui.views.payment;

import java.io.IOException;

import net.authorize.data.creditcard.CardType;

import org.apache.commons.logging.LogFactory;

import com.floreantpos.PosException;
import com.floreantpos.config.CardConfig;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Ticket;
import com.floreantpos.ui.util.StreamUtils;
import com.mercurypay.ws.sdk.MercuryResponse;
import com.mercurypay.ws.sdk.MercuryWebRequest;

public class MercuryPayProcessor implements CardProcessor {

	public final static String $merchantId = "$merchantId";
	public final static String $laneId = "$laneId";
	public final static String $invoiceNo = "$invoiceNo";
	public final static String $encryptedBlock = "$encryptedBlock";
	public final static String $encryptedKey = "$encryptedKey";
	public final static String $amount = "$amount";
	public final static String $authorizeAmount = "$authorizeAmount";
	public final static String $terminalName = "$terminalName";
	public final static String $shiftName = "$shiftName";
	public final static String $operatorId = "$operatorId";
	public final static String $tranCode = "$tranCode";
	public final static String $refNo = "$refNo";

	private static String mercuryXml;

	static {
		try {
			mercuryXml = StreamUtils.toString(MercuryPayProcessor.class.getResourceAsStream("/com/mercurypay/ws/sdk/mercuryAuth.xml"));
		} catch (IOException e) {
			LogFactory.getLog(MercuryPayProcessor.class).error(e);
		}
	}

	@Override
	public void authorizeAmount(PosTransaction transaction) throws Exception {
		Ticket ticket = transaction.getTicket();

		String cardTrack = transaction.getCardTrack();
		String[] strings = cardTrack.split("\\|");

		//String merchantId = "118725340908147";
		String laneId = "01";
		String tranCode = "PreAuth";
		String invoiceNo = String.valueOf(ticket.getId());
		String amount = String.valueOf(transaction.getAmount());
		String encryptedBlock = strings[3];
		String encryptedKey = strings[9];

		String xml = new String(mercuryXml);
		xml = xml.replace($merchantId, CardConfig.getMerchantAccount());
		xml = xml.replace($laneId, laneId);
		xml = xml.replace($tranCode, tranCode);
		xml = xml.replace($invoiceNo, invoiceNo);
		xml = xml.replace($refNo, invoiceNo);
		xml = xml.replace($encryptedBlock, encryptedBlock);
		xml = xml.replace($encryptedKey, encryptedKey);
		xml = xml.replace($amount, amount);
		xml = xml.replace($authorizeAmount, amount);

		MercuryWebRequest mpswr = new MercuryWebRequest("https://w1.mercurydev.net/ws/ws.asmx");
		mpswr.addParameter("tran", xml); //Set WebServices 'tran' parameter to the XML transaction request
		mpswr.addParameter("pw", CardConfig.getMerchantPass()); //Set merchant's WebServices password
		mpswr.setWebMethodName("CreditTransaction"); //Set WebServices webmethod to selected type
		mpswr.setTimeout(10); //Set request timeout to 10 seconds

		String mpsResponse = mpswr.sendRequest();
		
		MercuryResponse result = new MercuryResponse(mpsResponse);
		if(!result.isApproved()) {
			throw new PosException("Error authorizing transaction.");
		}
		
		transaction.setCardTransactionId(result.getTransactionId());
		transaction.setCardAuthCode(result.getAuthCode());
		transaction.addProperty("AcqRefData", result.getAcqRefData());
	}

	@Override
	public String authorizeAmount(String cardTracks, double amount, String cardType) throws Exception {
		throw new PosException("Manual entry is not supported by selected payment gateway.");
	}

	@Override
	public String authorizeAmount(String cardNumber, String expMonth, String expYear, double amount, CardType cardType) throws Exception {
		throw new PosException("Manual entry is not supported by selected payment gateway.");
	}

	@Override
	public void captureAuthorizedAmount(PosTransaction transaction) throws Exception {
		String xml = StreamUtils.toString(MercuryPayProcessor.class.getResourceAsStream("/com/mercurypay/ws/sdk/mercuryPreAuthCapture.xml"));
		Ticket ticket = transaction.getTicket();

		//String merchantId = "118725340908147";
		String laneId = "01";
		String invoiceNo = String.valueOf(ticket.getId());
		String amount = String.valueOf(transaction.getAmount());

		xml = xml.replace($merchantId, CardConfig.getMerchantAccount());
		xml = xml.replace($laneId, laneId);
		xml = xml.replace($invoiceNo, invoiceNo);
		xml = xml.replace($refNo, invoiceNo);
		xml = xml.replace($amount, amount);
		xml = xml.replace($authorizeAmount, amount);
		xml = xml.replace("$gratuity", String.valueOf(transaction.getTipsAmount()));
		xml = xml.replace("$recordNo", transaction.getCardTransactionId());
		xml = xml.replace("$authCode", transaction.getCardAuthCode());
		xml = xml.replace("$AcqRefData", transaction.getProperty("AcqRefData"));
		
//		System.out.println(xml);
		
		MercuryWebRequest mpswr = new MercuryWebRequest("https://w1.mercurydev.net/ws/ws.asmx");
		mpswr.addParameter("tran", xml); //Set WebServices 'tran' parameter to the XML transaction request
		mpswr.addParameter("pw", CardConfig.getMerchantPass()); //Set merchant's WebServices password
		mpswr.setWebMethodName("CreditTransaction"); //Set WebServices webmethod to selected type
		mpswr.setTimeout(10); //Set request timeout to 10 seconds

		String mpsResponse = mpswr.sendRequest();
		
		MercuryResponse result = new MercuryResponse(mpsResponse);
		if(!result.isApproved()) {
			throw new PosException("Error authorizing transaction.");
		}
		
		transaction.setCardTransactionId(result.getTransactionId());
		transaction.setCardAuthCode(result.getAuthCode());
	}

	@Override
	public void captureNewAmount(PosTransaction transaction) throws Exception {
		captureAuthorizedAmount(transaction);
	}

	@Override
	public void voidAmount(PosTransaction transaction) throws Exception {
	}

	@Override
	public void voidAmount(String transId, double amount) throws Exception {
	}

}
