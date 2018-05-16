package com.floreantpos.extension;

import java.awt.Component;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.floreantpos.config.ui.ConfigurationView;
import com.floreantpos.config.ui.InginicoConfigurationView;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Ticket;
import com.floreantpos.ui.views.payment.CardProcessor;

@PluginImplementation
public class InginicoPlugin extends PaymentGatewayPlugin {
	InginicoConfigurationView view;

	@Override
	public String getProductName() {
		return "Ingenico IWL220 TGI"; //$NON-NLS-1$
	}

	@Override
	public void initUI() {
	}

	@Override
	public void initBackoffice() {
	}

	@Override
	public void initConfigurationView(JDialog dialog) {

	}

	@Override
	public String getId() {
		return "Inginico"; // //$NON-NLS-1$
	}

	@Override
	public String getSecurityCode() {
		return "-1105096101";//$NON-NLS-1$
	}

	@Override
	public String toString() {
		return getProductName();
	}

	@Override
	public ConfigurationView getConfigurationPane() throws Exception {
		if (view == null) {
			view = new InginicoConfigurationView();
			view.initialize();
		}

		return view;
	}

	@Override
	public CardProcessor getProcessor() {
		return null;
	}

	@Override
	public boolean shouldShowCardInputProcessor() {
		return true;
	}

	@Override
	public List<AbstractAction> getSpecialFunctionActions() {
		return null;
	}

	@Override
	public void initLicense() {
	}

	@Override
	public boolean hasValidLicense() {
		return true;
	}

	@Override
	public String getProductVersion() {
		return null;
	}

	@Override
	public Component getParent() {
		return null;
	}

	@Override
	public boolean printUsingThisTerminal() {
		return false;
	}

	@Override
	public void printTicket(Ticket ticket) {
	}

	@Override
	public void printTransaction(PosTransaction transaction, boolean storeCopy, boolean customerCopy) {

	}

	@Override
	public void printTicketWithTipsBlock(Ticket ticket) {
	}
}
