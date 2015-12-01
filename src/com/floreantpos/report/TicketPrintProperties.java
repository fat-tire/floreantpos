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
package com.floreantpos.report;


public class TicketPrintProperties {
	private String receiptTypeName;
	private boolean showHeader;
	private boolean showFooter;
	private boolean showSubtotal;
	
	private String receiptCopyType;
	
	boolean printModifers = true;
	boolean printCookingInstructions = true;
	
	public TicketPrintProperties() {
		super();
	}

	public TicketPrintProperties(String receiptTypeName, boolean showHeader, boolean showFooter, boolean showSubtotal) {
		super();
		this.receiptTypeName = receiptTypeName;
		this.showHeader = showHeader;
		this.showFooter = showFooter;
		this.showSubtotal = showSubtotal;
	}

	public String getReceiptTypeName() {
		return receiptTypeName;
	}

	public void setReceiptTypeName(String receiptTypeName) {
		this.receiptTypeName = receiptTypeName;
	}

	public boolean isShowHeader() {
		return showHeader;
	}

	public void setShowHeader(boolean showHeader) {
		this.showHeader = showHeader;
	}

	public boolean isShowFooter() {
		return showFooter;
	}

	public void setShowFooter(boolean showFooter) {
		this.showFooter = showFooter;
	}

	public boolean isShowSubtotal() {
		return showSubtotal;
	}

	public void setShowSubtotal(boolean showSubtotal) {
		this.showSubtotal = showSubtotal;
	}

	public boolean isPrintModifers() {
		return printModifers;
	}

	public void setPrintModifers(boolean printModifers) {
		this.printModifers = printModifers;
	}

	public boolean isPrintCookingInstructions() {
		return printCookingInstructions;
	}

	public void setPrintCookingInstructions(boolean printCookingInstructions) {
		this.printCookingInstructions = printCookingInstructions;
	}

	public String getReceiptCopyType() {
		return receiptCopyType;
	}

	public void setReceiptCopyType(String receiptCopyName) {
		this.receiptCopyType = receiptCopyName;
	}
}
