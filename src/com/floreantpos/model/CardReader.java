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
package com.floreantpos.model;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;

public enum CardReader {
	PAX("PAX"), //$NON-NLS-1$
	SWIPE(Messages.getString("CardReader.0")), //$NON-NLS-1$
	MANUAL(Messages.getString("CardReader.1")), //$NON-NLS-1$
	EXTERNAL_TERMINAL(Messages.getString("CardReader.2")), //$NON-NLS-1$
	THIRD_PARTY("ThirdParty") //$NON-NLS-1$
	
	; //$NON-NLS-1$
	
	private String type;
	
	private CardReader(String typeString) {
		this.type = typeString;
	}
	
	public String getType() {
		return type;
	}
	
	public static CardReader fromString(String name) {
		if(StringUtils.isEmpty(name)) {
			return null;
		}
		
		return CardReader.valueOf(name);
	}
	
	@Override
	public String toString() {
		return type;
	}
}
