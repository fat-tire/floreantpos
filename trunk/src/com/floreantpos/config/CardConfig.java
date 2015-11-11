package com.floreantpos.config;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.extension.AuthorizeNetGatewayPlugin;
import com.floreantpos.extension.ExtensionManager;
import com.floreantpos.extension.FloreantPlugin;
import com.floreantpos.extension.PaymentGatewayPlugin;
import com.floreantpos.model.CardReader;
import com.floreantpos.util.AESencrp;

public class CardConfig {
	private static final String MERCHANT_PASS = "MerchantPass"; //$NON-NLS-1$
	private static final String MERCHANT_ACCOUNT = "MerchantAccount"; //$NON-NLS-1$
	private static final String CARD_READER = "CARD_READER"; //$NON-NLS-1$

	public static boolean isSwipeCardSupported() {
		return AppConfig.getBoolean("support-swipe-card", true); //$NON-NLS-1$
	}

	public static void setSwipeCardSupported(boolean b) {
		AppConfig.put("support-swipe-card", b); //$NON-NLS-1$
	}

	public static boolean isManualEntrySupported() {
		return AppConfig.getBoolean("support-card-manual-entry", true); //$NON-NLS-1$
	}

	public static void setManualEntrySupported(boolean b) {
		AppConfig.put("support-card-manual-entry", b); //$NON-NLS-1$
	}

	public static boolean isExtTerminalSupported() {
		return AppConfig.getBoolean("support-ext-terminal", true); //$NON-NLS-1$
	}

	public static void setExtTerminalSupported(boolean b) {
		AppConfig.put("support-ext-terminal", b); //$NON-NLS-1$
	}

	public static void setCardReader(CardReader card) {
		if (card == null) {
			AppConfig.put(CARD_READER, ""); //$NON-NLS-1$
			return;
		}
		AppConfig.put(CARD_READER, card.name());
	}

	public static CardReader getCardReader() {
		String string = AppConfig.getString(CARD_READER, "SWIPE"); //$NON-NLS-1$
		return CardReader.fromString(string);
	}

	public static void setMerchantAccount(String account) {
		AppConfig.put(MERCHANT_ACCOUNT, account);
	}

	public static String getMerchantAccount() {
		return AppConfig.getString(MERCHANT_ACCOUNT, Messages.getString("CardConfig.14")); //$NON-NLS-1$
	}

	public static void setMerchantPass(String pass) {
		try {

			if (StringUtils.isEmpty(pass)) {
				AppConfig.put(MERCHANT_PASS, Messages.getString("CardConfig.15")); //$NON-NLS-1$
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

		return Messages.getString("CardConfig.16"); //$NON-NLS-1$
	}

	public static boolean isSandboxMode() {
		return AppConfig.getBoolean(Messages.getString("CardConfig.17"), true); //$NON-NLS-1$
	}

	public static void setSandboxMode(boolean sandbosMode) {
		AppConfig.put(Messages.getString("CardConfig.18"), sandbosMode); //$NON-NLS-1$
	}

	public static double getBartabLimit() {
		try {
			return Double.parseDouble(AppConfig.getString(Messages.getString("CardConfig.19"), Messages.getString("CardConfig.20"))); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (Exception e) {
			return 25;
		}
	}

	public static void setBartabLimit(double limit) {
		AppConfig.put(Messages.getString("CardConfig.21"), String.valueOf(limit)); //$NON-NLS-1$
	}
	
	public static void setPaymentGateway(PaymentGatewayPlugin paymentGateway) {
		AppConfig.put("payment-gateway-id", paymentGateway.getId()); //$NON-NLS-1$
	}

	public static PaymentGatewayPlugin getPaymentGateway() {
		String gatewayId = AppConfig.getString("payment-gateway-id", AuthorizeNetGatewayPlugin.ID); //$NON-NLS-1$
		List<FloreantPlugin> plugins = ExtensionManager.getPlugins(PaymentGatewayPlugin.class);

		for (FloreantPlugin plugin : plugins) {
			if(gatewayId.equals(plugin.getId())) {
				return (PaymentGatewayPlugin) plugin;
			}
		}
		
		//should not reach here
		return new AuthorizeNetGatewayPlugin();
	}
}
