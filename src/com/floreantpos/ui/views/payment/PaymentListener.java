package com.floreantpos.ui.views.payment;

public interface PaymentListener {

	void paymentDone();

	void paymentCanceled();

	void paymentDataChanged();
}
