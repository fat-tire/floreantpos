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
package com.mercurypay.ws.sdk;

import java.io.StringReader;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.floreantpos.PosLog;

/**
 * 
 * @author mshahriar
 *
 */

public class MercuryResponse {
	private String cmdStatus;
	private Element responseRoot;
	
	public MercuryResponse(String responseXml) throws Exception {
//		PosLog.debug(getClass(),responseXml);
		
		SAXBuilder jdomBuilder = new SAXBuilder();
		Document document = jdomBuilder.build(new StringReader(responseXml));
		
		responseRoot = document.getRootElement();
		cmdStatus = responseRoot.getChild("CmdResponse").getChildText("CmdStatus"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public boolean isApproved() {
		return "Approved".equalsIgnoreCase(cmdStatus); //$NON-NLS-1$
	}

	public String getCmdStatus() {
		return cmdStatus;
	}
	
	public static void main(String[] args) throws Exception {
		MercuryResponse r = new MercuryResponse("<?xml version=\"1.0\"?><RStream>   <CmdResponse>      <ResponseOrigin>Client</ResponseOrigin>      <DSIXReturnCode>009999</DSIXReturnCode>      <CmdStatus>Error</CmdStatus>      <TextResponse>Invalid Credentials CALL 800-846-4472</TextResponse>   </CmdResponse></RStream>"); //$NON-NLS-1$
		PosLog.info(MercuryResponse.class, r.cmdStatus);
		
	}

	public String getTransactionId() {
		Element tranResponseElement = responseRoot.getChild("TranResponse"); //$NON-NLS-1$
		if(tranResponseElement == null) {
			return null;
		}
		
		return tranResponseElement.getChildTextTrim("RecordNo"); //$NON-NLS-1$
	}

	public String getAuthCode() {
		Element tranResponseElement = responseRoot.getChild("TranResponse"); //$NON-NLS-1$
		if(tranResponseElement == null) {
			return null;
		}
		
		return tranResponseElement.getChildTextTrim("AuthCode"); //$NON-NLS-1$
	}
	
	public String getAcqRefData() {
		Element tranResponseElement = responseRoot.getChild("TranResponse"); //$NON-NLS-1$
		if(tranResponseElement == null) {
			return null;
		}
		
		return tranResponseElement.getChildTextTrim("AcqRefData"); //$NON-NLS-1$
	}

}
