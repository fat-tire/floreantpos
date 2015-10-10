package com.floreantpos.ui.views.payment;

import java.io.IOException;

import net.authorize.data.creditcard.CardType;

import com.floreantpos.Messages;
import com.floreantpos.PosException;
import com.floreantpos.config.CardConfig;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Ticket;
import com.floreantpos.ui.util.StreamUtils;
import com.floreantpos.util.NumberUtil;
import com.mercurypay.ws.sdk.MercuryResponse;
import com.mercurypay.ws.sdk.MercuryWebRequest;

public class MercuryPayProcessor implements CardProcessor {

	public final static String $merchantId = "$merchantId"; //$NON-NLS-1$
	public final static String $laneId = "$laneId"; //$NON-NLS-1$
	public final static String $invoiceNo = "$invoiceNo"; //$NON-NLS-1$
	public final static String $encryptedBlock = "$encryptedBlock"; //$NON-NLS-1$
	public final static String $encryptedKey = "$encryptedKey"; //$NON-NLS-1$
	public final static String $amount = "$amount"; //$NON-NLS-1$
	public final static String $authorizeAmount = "$authorizeAmount"; //$NON-NLS-1$
	public final static String $terminalName = "$terminalName"; //$NON-NLS-1$
	public final static String $shiftName = "$shiftName"; //$NON-NLS-1$
	public final static String $operatorId = "$operatorId"; //$NON-NLS-1$
	public final static String $tranCode = "$tranCode"; //$NON-NLS-1$
	public final static String $refNo = "$refNo"; //$NON-NLS-1$

	@Override
	public void authorizeAmount(PosTransaction transaction) throws Exception {
		Ticket ticket = transaction.getTicket();
		
		if(ticket.getType() == OrderType.BAR_TAB && ticket.hasProperty("AcqRefData")) { //$NON-NLS-1$
			captureAuthorizedAmount(transaction);
			return;
		}
		
		String mpsResponse = doPreAuth(ticket, transaction.getCardTrack(), transaction.getAmount());
		
		MercuryResponse result = new MercuryResponse(mpsResponse);
		if(!result.isApproved()) {
			throw new PosException(Messages.getString("MercuryPayProcessor.13")); //$NON-NLS-1$
		}
		
		System.out.println(mpsResponse);
		
		transaction.setCardTransactionId(result.getTransactionId());
		transaction.setCardAuthCode(result.getAuthCode());
		transaction.addProperty("AcqRefData", result.getAcqRefData()); //$NON-NLS-1$
	}

	private String doPreAuth(Ticket ticket, String cardTrack, double amount) throws IOException, Exception {
		String xml = StreamUtils.toString(MercuryPayProcessor.class.getResourceAsStream("/com/mercurypay/ws/sdk/mercuryAuth.xml")); //$NON-NLS-1$

		String[] strings = cardTrack.split("\\|"); //$NON-NLS-1$
		
		//String merchantId = "118725340908147";
		String laneId = "01"; //$NON-NLS-1$
		String tranCode = "PreAuth"; //$NON-NLS-1$
		String invoiceNo = String.valueOf(ticket.getId());
		String amountStrng = NumberUtil.formatNumber(amount);
		String encryptedBlock = strings[3];
		String encryptedKey = strings[9];

		xml = xml.replace($merchantId, CardConfig.getMerchantAccount());
		xml = xml.replace($laneId, laneId);
		xml = xml.replace($tranCode, tranCode);
		xml = xml.replace($invoiceNo, invoiceNo);
		xml = xml.replace($refNo, invoiceNo);
		xml = xml.replace($encryptedBlock, encryptedBlock);
		xml = xml.replace($encryptedKey, encryptedKey);
		xml = xml.replace($amount, amountStrng);
		xml = xml.replace($authorizeAmount, amountStrng);
		
		System.out.println(xml);

		MercuryWebRequest mpswr = new MercuryWebRequest("https://w1.mercurydev.net/ws/ws.asmx"); //$NON-NLS-1$
		mpswr.addParameter("tran", xml); //Set WebServices 'tran' parameter to the XML transaction request //$NON-NLS-1$
		mpswr.addParameter("pw", CardConfig.getMerchantPass()); //Set merchant's WebServices password //$NON-NLS-1$
		mpswr.setWebMethodName("CreditTransaction"); //Set WebServices webmethod to selected type //$NON-NLS-1$
		mpswr.setTimeout(10); //Set request timeout to 10 seconds

		String mpsResponse = mpswr.sendRequest();
		return mpsResponse;
	}

	@Override
	public String authorizeAmount(Ticket ticket, String cardTrack, double amount, String cardType) throws Exception {
		String mpsResponse = doPreAuth(ticket, cardTrack, amount);
		
		System.out.println(mpsResponse);
		
		MercuryResponse result = new MercuryResponse(mpsResponse);
		if(!result.isApproved()) {
			throw new PosException(Messages.getString("MercuryPayProcessor.23")); //$NON-NLS-1$
		}
		
		ticket.addProperty("AuthCode", result.getAuthCode()); //$NON-NLS-1$
		ticket.addProperty("AcqRefData", result.getAcqRefData()); //$NON-NLS-1$
		
		return result.getTransactionId();
	}

	@Override
	public String authorizeAmount(String cardNumber, String expMonth, String expYear, double amount, CardType cardType) throws Exception {
		throw new PosException(Messages.getString("MercuryPayProcessor.26")); //$NON-NLS-1$
	}

	@Override
	public void captureAuthorizedAmount(PosTransaction transaction) throws Exception {
		String xml = StreamUtils.toString(MercuryPayProcessor.class.getResourceAsStream("/com/mercurypay/ws/sdk/mercuryPreAuthCapture.xml")); //$NON-NLS-1$
		Ticket ticket = transaction.getTicket();

		//String merchantId = "118725340908147";
		String laneId = "01"; //$NON-NLS-1$
		String invoiceNo = String.valueOf(ticket.getId());
		String amount = String.valueOf(transaction.getAmount());

		xml = xml.replace($merchantId, CardConfig.getMerchantAccount());
		xml = xml.replace($laneId, laneId);
		xml = xml.replace($invoiceNo, invoiceNo);
		xml = xml.replace($refNo, invoiceNo);
		xml = xml.replace($amount, amount);
		xml = xml.replace($authorizeAmount, amount);
		xml = xml.replace("$gratuity", String.valueOf(transaction.getTipsAmount())); //$NON-NLS-1$
		xml = xml.replace("$recordNo", transaction.getCardTransactionId()); //$NON-NLS-1$
		xml = xml.replace("$authCode", transaction.getCardAuthCode()); //$NON-NLS-1$
		xml = xml.replace("$AcqRefData", transaction.getProperty("AcqRefData")); //$NON-NLS-1$ //$NON-NLS-2$
		
//		System.out.println(xml);
		
		MercuryWebRequest mpswr = new MercuryWebRequest("https://w1.mercurydev.net/ws/ws.asmx"); //$NON-NLS-1$
		mpswr.addParameter("tran", xml); //Set WebServices 'tran' parameter to the XML transaction request //$NON-NLS-1$
		mpswr.addParameter("pw", CardConfig.getMerchantPass()); //Set merchant's WebServices password //$NON-NLS-1$
		mpswr.setWebMethodName("CreditTransaction"); //Set WebServices webmethod to selected type //$NON-NLS-1$
		mpswr.setTimeout(10); //Set request timeout to 10 seconds

		String mpsResponse = mpswr.sendRequest();
		
		MercuryResponse result = new MercuryResponse(mpsResponse);
		if(!result.isApproved()) {
			throw new PosException("Error authorizing transaction."); //$NON-NLS-1$
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
