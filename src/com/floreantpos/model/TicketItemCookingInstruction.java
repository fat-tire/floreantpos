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
package com.floreantpos.model;

import com.floreantpos.model.base.BaseTicketItemCookingInstruction;



public class TicketItemCookingInstruction extends BaseTicketItemCookingInstruction implements ITicketItem {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public TicketItemCookingInstruction () {
		super();
	}

/*[CONSTRUCTOR MARKER END]*/

	private int tableRowNum;

	public int getTableRowNum() {
		return tableRowNum;
	}

	public void setTableRowNum(int tableRowNum) {
		this.tableRowNum = tableRowNum;
	}

	public boolean canAddCookingInstruction() {
		return false;
	}
	
	@Override
	public String toString() {
		return getDescription();
	}
	
	@Override
	public String getNameDisplay() {
		return "   * " + getDescription(); //$NON-NLS-1$
	}

	@Override
	public Double getUnitPriceDisplay() {
		return null;
	}

	@Override
	public String getItemQuantityDisplay() {
		return null;
	}

	@Override
	public Double getSubTotalAmountDisplay() {
		return null;
	}

	@Override
	public Double getTaxAmountWithoutModifiersDisplay() {
		return null;
	}

	@Override
	public Double getTotalAmountWithoutModifiersDisplay() {
		return null;
	}
	
	@Override
	public Double getSubTotalAmountWithoutModifiersDisplay() {
		return null;
	}
	
	@Override
	public String getItemCode() {
		return ""; //$NON-NLS-1$
	}

	@Override
	public boolean canAddDiscount() {
		return false;
	}

	@Override
	public boolean canVoid() {
		return false;
	}

	@Override
	public boolean canAddAdOn() {
		return false;
	}

	@Override
	public Boolean isPrintedToKitchen() {
		return super.isPrintedToKitchen();
	}

	@Override
	public void setDiscountAmount(Double amount) {
	}

	@Override
	public Double getDiscountAmount() {
		return null;
	}

	@Override
	public String getKitchenStatus() {
		return ""; //$NON-NLS-1$
	}
}