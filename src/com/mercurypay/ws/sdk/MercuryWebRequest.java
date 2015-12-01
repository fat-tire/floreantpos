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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

/**
* Mercury Payment Systems WebServices Platform Request
* <br /><br />
* ©2012 Mercury Payment Systems, LLC - all rights reserved.
* <br /><br />
* Disclaimer:
* <br />
* This software and all specifications and documentation contained
* herein or provided to you hereunder (the "Software") are provided
* free of charge strictly on an "AS IS" basis. No representations or
* warranties are expressed or implied, including, but not limited to,
* warranties of suitability, quality, merchantability, or fitness for a
* particular purpose (irrespective of any course of dealing, custom or
* usage of trade), and all such warranties are expressly and
* specifically disclaimed. Mercury Payment Systems shall have no
* liability or responsibility to you nor any other person or entity
* with respect to any liability, loss, or damage, including lost
* profits whether foreseeable or not, or other obligation for any cause
* whatsoever, caused or alleged to be caused directly or indirectly by
* the Software. Use of the Software signifies agreement with this
* disclaimer notice.
*
* @author Mercury Payment Systems
*/
public class MercuryWebRequest {
	private URL mWebServiceURL;
	private String mWebMethodName = "";
	private final String mXMLNamespace = "http://www.mercurypay.com";
	private int mTimeout;
	private HashMap<String, String> mWSParameters;
	private final String mSOAPWrapper = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><%2$s xmlns=\"%1$s\">%3$s</%2$s></soap:Body></soap:Envelope>";
	private final String mMPSExceptionString = "MPSWebRequest Error: %1$s";

	/**
	* MercuryWebRequest Constructor
	* @param webServiceURL
	* URL to MPS WebServices platform
	* @throws Exception
	* When passed an invalid WebService URL
	*/
	public MercuryWebRequest(String webServiceURL) throws Exception {
		// Initialize MercuryWebRequest fields
		this.setWebServiceURL(webServiceURL);
		this.mWebMethodName = "";
		this.mTimeout = 10000;
		this.mWSParameters = new HashMap<String, String>();
	}

	/**
	* Set WebMethod name
	* @param webMethodName
	* WebMethod name
	*/
	public void setWebMethodName(String webMethodName) {
		mWebMethodName = webMethodName.trim();
	}

	/**
	* Set WebService URL
	* @param webServiceURL
	* SSL WebService URL
	* @throws Exception
	* When WebService URL does not use SSL
	*/
	private void setWebServiceURL(String webServiceURL) throws Exception {
		webServiceURL = webServiceURL.trim();
		URL tempURL = new URL(webServiceURL);
		if (tempURL.getProtocol().equals("https"))
			mWebServiceURL = tempURL;
		else
			throw new Exception(String.format(mMPSExceptionString, "WebService URL value must use SSL"));
	}

	/**
	* Set request timeout in seconds, default is 10 seconds
	* @param timeout
	* Web request timeout value in seconds
	* @throws Exception
	* When timeout value is less than 1 second
	*/
	public void setTimeout(int timeout) throws Exception {
		if (timeout > 0)
			mTimeout = timeout * 1000;
		else
			throw new Exception(String.format(mMPSExceptionString, "Timeout value must be greater than 0"));
	}

	/**
	* Build SOAP request string
	* @return SOAP XML wrapped request string
	* @throws Exception
	* When no parameters have been added to the MercuryWebRequest
	*/
	private String buildSOAPRequest() throws Exception {
		if (!mWSParameters.isEmpty()) {
			StringBuilder parameters = new StringBuilder();
			for (Entry<String, String> element : mWSParameters.entrySet())
				parameters.append(String.format("<%1$s>%2$s</%1$s>", element.getKey(), element.getValue()));
			return String.format(mSOAPWrapper, mXMLNamespace, mWebMethodName, parameters.toString());
		}
		else {
			throw new Exception(String.format(mMPSExceptionString, "Cannot build SOAP request with no parameters"));
		}
	}

	/**
	* Add WebMethod parameter, overwrites the value if parameter name already exists
	* @param paramName
	* WebMethod parameter name
	* @param paramValue
	* WebMethod parameter value
	*/
	public void addParameter(String paramName, String paramValue) throws Exception {
		paramName = paramName.trim();
		paramValue = paramValue.trim();
		// If parameter = "tran" then format XML for SOAP parameter
		if (paramName.equals("tran"))
			paramValue = paramValue.replace("<", "&lt;").replace(">", "&gt;").replace("\t", "").replace("\n", "").replace("\r", ""); // Remove less-than, greater-than, newline, carriage return, and tab characters from value
		mWSParameters.put(paramName, paramValue);
	}

	/**
	* Send request to WebService
	* @return String content of WebMethod response
	* @throws Exception
	* When error is encountered communicating with WebService
	*/
	public String sendRequest() throws Exception {
		validateRequiredParameters();
		String responseData = "";
		boolean error = false;
		String soap = buildSOAPRequest();
		// Open connection
		HttpsURLConnection conn = (HttpsURLConnection) mWebServiceURL.openConnection();
		// Set connection properties
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		conn.setReadTimeout(mTimeout);
		conn.setConnectTimeout(mTimeout);
		conn.setUseCaches(false);
		conn.setDefaultUseCaches(false);
		conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		conn.setRequestProperty("Content-Length", String.valueOf(soap.length()));
		conn.setRequestProperty("SOAPAction", "\"" + mXMLNamespace + "/" + mWebMethodName + "\"");
		conn.setRequestProperty("Connection", "Close");
		// Send request
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(soap);
		wr.flush();
		wr.close();
		// Determine response stream according to HTTP response code
		int httpResponseCode = conn.getResponseCode();
		BufferedReader rd;
		if (httpResponseCode != 200)
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		else
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		// Receive response
		String responseBuffer = "";
		while ((responseBuffer = rd.readLine()) != null)
			responseData += responseBuffer;
		// Close response reader
		rd.close();
		// Parse correct value from SOAP response according to HTTP response code
		int start = 0, end = 0;
		if (httpResponseCode != 200) {
			error = true;
			String returnparam = "faultstring";
			if (responseData.contains(returnparam)) {
				start = responseData.indexOf("<" + returnparam + ">") + returnparam.length() + 2;
				end = responseData.indexOf("</" + returnparam + ">");
			}
		}
		else {
			String returnparam = mWebMethodName + "Result";
			start = responseData.indexOf("<" + returnparam + ">") + returnparam.length() + 2;
			end = responseData.indexOf("</" + returnparam + ">");
		}
		// Extract single return parameter
		responseData = responseData.substring(start, end).replace("&lt;", "<").replace("&gt;", ">");
		if (error)
			throw new Exception(String.format(mMPSExceptionString, responseData));
		else
			return responseData;
	}

	/**
	* Validate required WebServices parameters
	* @throws Exception
	* When missing required parameters or containing invalid parameters
	*/
	private void validateRequiredParameters() throws Exception {
		if (mWebMethodName.equals(""))
			throw new Exception(String.format(mMPSExceptionString, "WebMethodName is required"));
		if (!mWSParameters.containsKey("pw"))
			throw new Exception(String.format(mMPSExceptionString, "WebServices password parameter (\"pw\") is required"));
	}
}