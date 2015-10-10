package com.floreantpos.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class PasswordHasher {

	public static void main(String[] args) throws Exception {
		System.out.println(hashPassword("123")); //$NON-NLS-1$
	}

	public static String hashPassword(String password) {
		byte[] passwordBytes = null;
		MessageDigest md = null;

		try {
			passwordBytes = password.getBytes("UTF-8"); //$NON-NLS-1$
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {
			md = MessageDigest.getInstance("SHA1"); //$NON-NLS-1$
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		byte[] hashBytes = md.digest(passwordBytes);

		return Hex.encodeHexString(hashBytes);

	}

}