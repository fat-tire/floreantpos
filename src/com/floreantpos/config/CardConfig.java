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
package com.floreantpos.config;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.PosLog;
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
		return AppConfig.getString(MERCHANT_ACCOUNT, null); //$NON-NLS-1$
	}

	public static void setMerchantPass(String pass) {
		try {

			if (StringUtils.isEmpty(pass)) {
				AppConfig.put(MERCHANT_PASS, ""); //$NON-NLS-1$
				return;
			}

			AppConfig.put(MERCHANT_PASS, AESencrp.encrypt(pass));
		} catch (Exception e) {
			PosLog.error(CardConfig.class, e.getMessage());
		}
	}

	public static String getMerchantPass() throws Exception {
		String string = AppConfig.getString(MERCHANT_PASS);

		if (StringUtils.isNotEmpty(string)) {
			return AESencrp.decrypt(string);
		}

		return string; //$NON-NLS-1$
	}

	public static boolean isSandboxMode() {
		return AppConfig.getBoolean("sandboxMode", true); //$NON-NLS-1$
	}

	public static void setSandboxMode(boolean sandbosMode) {
		AppConfig.put("sandboxMode", sandbosMode); //$NON-NLS-1$
	}

	public static double getBartabLimit() {
		try {
			return Double.parseDouble(AppConfig.getString("bartablimit", "25")); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (Exception e) {
			return 25;
		}
	}

	public static void setBartabLimit(double limit) {
		AppConfig.put("bartablimit", String.valueOf(limit)); //$NON-NLS-1$
	}
	
	public static double getAdvanceTipsPercentage() {
		try {
			return Double.parseDouble(AppConfig.getString("advanceTipsPercentage")); //$NON-NLS-1$
		} catch (Exception e) {
			return 20;
		}
	}
	
	public static void setAdvanceTipsPercentage(double advanceTipsPercentage) {
		AppConfig.put("advanceTipsPercentage", String.valueOf(advanceTipsPercentage)); //$NON-NLS-1$
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
