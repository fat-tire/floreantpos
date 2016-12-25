package com.floreantpos.services;

import javax.ws.rs.core.MultivaluedMap;

import com.floreantpos.PosLog;
import com.floreantpos.config.TerminalConfig;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class PosWebService {

	private static final String SERVICE_URL = TerminalConfig.getWebServiceUrl();

	public PosWebService() {
	}

	public String getAvailableNewVersions(String terminalKey, String currentPosVersion) {
		try {
			Client client = Client.create();
			client.getProperties();
			MultivaluedMap map = new MultivaluedMapImpl();
			map.add("terminal_key", terminalKey);
			map.add("pos_version", currentPosVersion);
			WebResource webResource = client.resource(SERVICE_URL + "/public/posuser/update");
			ClientResponse response = webResource.accept("application/json").post(ClientResponse.class, map);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			String versionInfo = response.getEntity(String.class);
			PosLog.info(getClass(),"\n============update info============");
			PosLog.info(getClass(),versionInfo);
			return versionInfo;

		} catch (Exception e) {
			//PosLog.error(getClass(), e);
		}
		return null;
	}
}
