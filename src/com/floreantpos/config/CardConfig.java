package com.floreantpos.config;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.model.CardReader;
import com.floreantpos.model.MerchantGateway;
import com.floreantpos.util.AESencrp;

public class CardConfig {
	private static final String MERCHANT_PASS = "MerchantPass";
	private static final String MERCHANT_ACCOUNT = "MerchantAccount";
	private static final String MERCHANT_GATEWAY = "MerchantGateway";
	private static final String CARD_READER = "CARD_READER";
	
	public static boolean isSwipeCardSupported() {
		return AppConfig.getBoolean("support-swipe-card", true);
	}
	
	public static void setSwipeCardSupported(boolean b) {
		AppConfig.put("support-swipe-card", b);
	}
	
	public static boolean isManualEntrySupported() {
		return AppConfig.getBoolean("support-card-manual-entry", true);
	}
	
	public static void setManualEntrySupported(boolean b) {
		AppConfig.put("support-card-manual-entry", b);
	}
	
	public static boolean isExtTerminalSupported() {
		return AppConfig.getBoolean("support-ext-terminal", true);
	}
	
	public static void setExtTerminalSupported(boolean b) {
		AppConfig.put("support-ext-terminal", b);
	}

	public static void setCardReader(CardReader card) {
		if (card == null) {
			AppConfig.put(CARD_READER, "");
			return;
		}
		AppConfig.put(CARD_READER, card.name());
	}

	public static CardReader getCardReader() {
		String string = AppConfig.getString(CARD_READER, "SWIPE");
		return CardReader.fromString(string);
	}

	public static void setMerchantGateway(MerchantGateway gateway) {
		if (gateway == null) {
			AppConfig.put(MERCHANT_GATEWAY, "");
			return;
		}
		AppConfig.put(MERCHANT_GATEWAY, gateway.name());
	}

	public static MerchantGateway getMerchantGateway() {
		String gateway = AppConfig.getString(MERCHANT_GATEWAY, MerchantGateway.AUTHORIZE_NET.name());
		if (StringUtils.isEmpty(gateway)) {
			return MerchantGateway.AUTHORIZE_NET;
		}

		MerchantGateway merchantGateway = MerchantGateway.valueOf(gateway);
		if(merchantGateway == null) {
			throw new RuntimeException("Merchant gateway is not configured");
		}
		
		return merchantGateway;
	}

	public static void setMerchantAccount(String account) {
		AppConfig.put(MERCHANT_ACCOUNT, account);
	}

	public static String getMerchantAccount() {
		return AppConfig.getString(MERCHANT_ACCOUNT, "6tuU4N3H");
	}

	public static void setMerchantPass(String pass) {
		try {

			if (StringUtils.isEmpty(pass)) {
				AppConfig.put(MERCHANT_PASS, "");
				return;
			}

			AppConfig.put(MERCHANT_PASS, AESencrp.encrypt(pass));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getMerchantPass() throws Exception {
		String string = AppConfig.getString(MERCHANT_PASS);

		if (StringUtils.isNotEmpty(string)) {
			return AESencrp.decrypt(string);
		}

		return "4k6955x3T8bCVPVm";
	}
	
	public static boolean isSandboxMode() {
		return AppConfig.getBoolean("sandboxMode", true);
	}
	
	public static void setSandboxMode(boolean sandbosMode) {
		AppConfig.put("sandboxMode", sandbosMode);
	}
	
	public static double getBartabLimit() {
		try {
			return Double.parseDouble(AppConfig.getString("bartablimit", "25"));
		} catch (Exception e) {
			return 25;
		}
	}
	
	public static void setBartabLimit(double limit) {
		AppConfig.put("bartablimit", String.valueOf(limit));
	}
}
