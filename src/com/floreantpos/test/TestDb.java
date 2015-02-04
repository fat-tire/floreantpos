package com.floreantpos.test;

import java.util.UUID;

public class TestDb {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		UUID uid = UUID.randomUUID();
		System.out.println(uid.toString());
		
//		InetAddress localHost = InetAddress.getLocalHost();
//		NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
//		byte[] mac = networkInterface.getHardwareAddress();
//		if (mac != null) {
//			System.out.print("Current MAC address : ");
//
//			StringBuilder sb = new StringBuilder();
//			for (int i = 0; i < mac.length; i++) {
//				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
//			}
//			System.out.println(sb.toString());
//		}
		
		
//		Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
//		while (enumeration.hasMoreElements()) {
//			NetworkInterface networkInterface = enumeration.nextElement();
//			byte[] mac = networkInterface.getHardwareAddress();
//
//			if (mac != null) {
//				System.out.print("Current MAC address : ");
//
//				StringBuilder sb = new StringBuilder();
//				for (int i = 0; i < mac.length; i++) {
//					sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
//				}
//				System.out.println(sb.toString());
//			}
//		}

	}

}
