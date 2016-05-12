package com.floreantpos.model;

import com.floreantpos.model.base.BaseDeliveryInstruction;



public class DeliveryInstruction extends BaseDeliveryInstruction {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public DeliveryInstruction () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public DeliveryInstruction (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/
	
	@Override
	public String toString(){
		return super.getNotes(); 
	}


}