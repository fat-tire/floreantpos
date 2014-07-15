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
}