package com.floreantpos.bo.ui.explorer;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.Messages;
import com.floreantpos.model.Currency;
import com.floreantpos.model.dao.CurrencyDAO;
import com.floreantpos.ui.dialog.OkCancelOptionDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;

public class CurrencyDialog extends OkCancelOptionDialog {
	private CurrencyExplorer currencyExplorer;

	public CurrencyDialog() {
		JPanel contentPanel = getContentPanel();
		this.getContentPane();
		setTitle(Messages.getString("CurrencyDialog.0")); //$NON-NLS-1$
		setTitlePaneText(Messages.getString("CurrencyDialog.0")); //$NON-NLS-1$
		currencyExplorer = new CurrencyExplorer();
		contentPanel.add(currencyExplorer);
	}

	@Override
	public void doOk() {
		List<Currency> currencyList = currencyExplorer.getModel().getRows();

		Currency mainCurrency = null;
		boolean isMainSelected = false;
		for (Currency currency : currencyList) {
			if (currency.isMain()) {
				isMainSelected = true;
				mainCurrency = currency;
			}
		}

		if (!isMainSelected) {
			POSMessageDialog.showMessage(POSUtil.getFocusedWindow(), Messages.getString("CurrencyDialog.2")); //$NON-NLS-1$
			return;
		}
		else {
			if (mainCurrency.getExchangeRate() != 1) {
				if (POSMessageDialog.showYesNoQuestionDialog(this, Messages.getString("CurrencyDialog.3"), Messages.getString("CurrencyDialog.4")) == JOptionPane.OK_OPTION) { //$NON-NLS-1$ //$NON-NLS-2$
					mainCurrency.setExchangeRate(1.0);
				}
				else {
					return;
				}
			}
		}

		Session session = null;
		Transaction tx = null;
		try {
			session = CurrencyDAO.getInstance().createNewSession();
			tx = session.beginTransaction();

			for (Currency currency : currencyList) {
				session.saveOrUpdate(currency);
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			return;
		} finally {
			session.close();
		}

		setCanceled(true);
		dispose();
	}
}
