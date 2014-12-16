package com.mercurypay.ws.sdk;

import java.io.StringReader;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

/**
 * 
 * @author mshahriar
 *
 */

public class MercuryResponse {
	private String cmdStatus;
	private Element responseRoot;
	
	public MercuryResponse(String responseXml) throws Exception {
//		System.out.println(responseXml);
		
		SAXBuilder jdomBuilder = new SAXBuilder();
		Document document = jdomBuilder.build(new StringReader(responseXml));
		
		responseRoot = document.getRootElement();
		cmdStatus = responseRoot.getChild("CmdResponse").getChildText("CmdStatus");
	}
	
	public boolean isApproved() {
		return "Approved".equalsIgnoreCase(cmdStatus);
	}

	public String getCmdStatus() {
		return cmdStatus;
	}
	
	public static void main(String[] args) throws Exception {
		MercuryResponse r = new MercuryResponse("<?xml version=\"1.0\"?><RStream>   <CmdResponse>      <ResponseOrigin>Client</ResponseOrigin>      <DSIXReturnCode>009999</DSIXReturnCode>      <CmdStatus>Error</CmdStatus>      <TextResponse>Invalid Credentials CALL 800-846-4472</TextResponse>   </CmdResponse></RStream>");
		System.out.println(r.cmdStatus);
		
	}

	public String getTransactionId() {
		Element tranResponseElement = responseRoot.getChild("TranResponse");
		if(tranResponseElement == null) {
			return null;
		}
		
		return tranResponseElement.getChildTextTrim("RecordNo");
	}

	public String getAuthCode() {
		Element tranResponseElement = responseRoot.getChild("TranResponse");
		if(tranResponseElement == null) {
			return null;
		}
		
		return tranResponseElement.getChildTextTrim("AuthCode");
	}
	
	public String getAcqRefData() {
		Element tranResponseElement = responseRoot.getChild("TranResponse");
		if(tranResponseElement == null) {
			return null;
		}
		
		return tranResponseElement.getChildTextTrim("AcqRefData");
	}

}
