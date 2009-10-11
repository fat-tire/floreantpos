package com.mdss.pos.model;

import com.mdss.pos.model.base.BaseTicketCookingInstruction;



public class TicketCookingInstruction extends BaseTicketCookingInstruction {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public TicketCookingInstruction () {
		super();
	}

/*[CONSTRUCTOR MARKER END]*/

	@Override
	public String toString() {
		return getDescription();
	}
}