package com.floreantpos.ui.views.payment;

import java.io.IOException;

import net.authorize.data.creditcard.CardType;

import org.apache.commons.logging.LogFactory;

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
			mercuryXml = StreamUtils.toString(MercuryPayProcessor.class.getResourceAsStream("/mercuryAuth.xml"));
		} catch (IOException e) {
			LogFactory.getLog(MercuryPayProcessor.class).error(e);
		}
	}

	@Override
	public void authorizeAmount(PosTransaction transaction) throws Exception {
		Ticket ticket = transaction.getTicket();

		String cardTrack = transaction.getCardTrack();
		String[] strings = cardTrack.split("\\|");

		String merchantId = "118725340908147";
		String laneId = "01";
		String tranCode = "PreAuth";
		String invoiceNo = String.valueOf(ticket.getId());
		String amount = String.valueOf(transaction.getAmount());
		String encryptedBlock = strings[3];
		String encryptedKey = strings[9];

		String xml = new String(mercuryXml);
		xml = xml.replace($merchantId, merchantId);
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
		mpswr.addParameter("pw", "XYZ"); //Set merchant's WebServices password
		mpswr.setWebMethodName("CreditTransaction"); //Set WebServices webmethod to selected type
		mpswr.setTimeout(10); //Set request timeout to 10 seconds

		String mpsResponse = mpswr.sendRequest();
		
		MercuryResponse response = new MercuryResponse(mpsResponse);
		System.out.println(response.getCmdStatus());
	}

	@Override
	public String authorizeAmount(String cardTracks, double amount, String cardType) throws Exception {
		System.out.println(cardTracks);
		return null;
	}

	@Override
	public String authorizeAmount(String cardNumber, String expMonth, String expYear, double amount, CardType cardType) throws Exception {
		return null;
	}

	@Override
	public void captureAuthorizedAmount(PosTransaction transaction) throws Exception {
		System.out.println();
	}

	@Override
	public void captureNewAmount(PosTransaction transaction) throws Exception {
		System.out.println();
	}

	@Override
	public void voidAmount(PosTransaction transaction) throws Exception {
	}

	@Override
	public void voidAmount(String transId, double amount) throws Exception {
	}

}
