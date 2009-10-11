package com.mdss.pos.model;

import com.mdss.pos.model.base.BaseCookingInstruction;



public class CookingInstruction extends BaseCookingInstruction {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CookingInstruction () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CookingInstruction (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	@Override
	public String toString() {
		return getDescription();
	}

}