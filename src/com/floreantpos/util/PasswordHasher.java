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
package com.floreantpos.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

import com.floreantpos.PosLog;

public class PasswordHasher {

	public static void main(String[] args) throws Exception {
		PosLog.info(PasswordHasher.class, hashPassword("123")); //$NON-NLS-1$
	}

	public static String hashPassword(String password) {
		byte[] passwordBytes = null;
		MessageDigest md = null;

		try {
			passwordBytes = password.getBytes("UTF-8"); //$NON-NLS-1$
		} catch (UnsupportedEncodingException e) {
			PosLog.error(PasswordHasher.class, e.getMessage());
		}

		try {
			md = MessageDigest.getInstance("SHA1"); //$NON-NLS-1$
		} catch (NoSuchAlgorithmException e) {
			PosLog.error(PasswordHasher.class, e.getMessage());
		}

		byte[] hashBytes = md.digest(passwordBytes);

		return Hex.encodeHexString(hashBytes);

	}

}