package com.floreantpos.config;


public class PrintConfig {
//	public final static String P_RECEIPT_PRINTER_TYPE =  "RECEIPT_PRINTER_TYPE";
	public final static String RECEIPT_PRINTER_NAME = "RECEIPT_PRINTER_NAME";
//	public final static String P_JAVAPOS_PRINTER_FOR_RECEIPT = "RECEIPT_JAVAPOS_PRINTER_NAME";
//	public final static String P_CASH_DRAWER_NAME = "CASH_DRAWER_NAME";
//	public final static String P_KITCHEN_PRINTER_TYPE = "KITCHEN_PRINTER_TYPE";
	public final static String KITCHEN_PRINTER_NAME = "KITCHEN_PRINTER_NAME";
//	public final static String P_JAVAPOS_PRINTER_FOR_KITCHEN = "KITCHEN_JAVAPOS_PRINTER_NAME";
	public final static String P_PRINT_RECEIPT_ON_ORDER_FINISH = "PRINT_RECEIPT_ON_ORDER_FINISH";
	public final static String P_PRINT_RECEIPT_ON_ORDER_SETTLE = "PRINT_RECEIPT_ON_ORDER_SETTLE";
	public final static String P_PRINT_TO_KITCHEN_ON_ORDER_FINISH = "PRINT_TO_KITCHEN_ON_ORDER_FINISH";
	public final static String P_PRINT_TO_KITCHEN_ON_ORDER_SETTLE = "PRINT_TO_KITCHEN_ON_ORDER_SETTLE";

	public static String getReceiptPrinterName() {
		return AppConfig.getString(RECEIPT_PRINTER_NAME, "POSPrinter");
	}

	public static String getKitchenPrinterName() {
		return AppConfig.getString(KITCHEN_PRINTER_NAME, "KitchenPrinter");
	}
	
	public static void setPrintReceiptOnOrderFinish(boolean print) {
		AppConfig.put(P_PRINT_RECEIPT_ON_ORDER_FINISH, print);
	}

	public static boolean isPrintReceiptOnOrderFinish() {
		return AppConfig.getBoolean(P_PRINT_RECEIPT_ON_ORDER_FINISH, true);
	}
	
	public static void setPrintReceiptOnOrderSettle(boolean print) {
		AppConfig.put(P_PRINT_RECEIPT_ON_ORDER_SETTLE, print);
	}

	public static boolean isPrintReceiptOnOrderSettle() {
		return AppConfig.getBoolean(P_PRINT_RECEIPT_ON_ORDER_SETTLE, false);
	}

	public static void setPrintToKitchenOnOrderFinish(boolean print) {
		AppConfig.put(P_PRINT_TO_KITCHEN_ON_ORDER_FINISH, print);
	}
	
	public static boolean isPrintToKitchenOnOrderFinish() {
		return AppConfig.getBoolean(P_PRINT_TO_KITCHEN_ON_ORDER_FINISH, false);
	}
	
	public static void setPrintToKitchenOnOrderSettle(boolean print) {
		AppConfig.put(P_PRINT_TO_KITCHEN_ON_ORDER_SETTLE, print);
	}

	public static boolean isPrintToKitchenOnOrderSettle() {
		return AppConfig.getBoolean(P_PRINT_TO_KITCHEN_ON_ORDER_SETTLE, false);
	}
}
