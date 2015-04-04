package com.floreantpos.model;

import java.util.ArrayList;
import java.util.List;

import com.floreantpos.model.base.BaseRecepie;

public class Recepie extends BaseRecepie {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Recepie () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Recepie (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	public void addRecepieItem(RecepieItem recepieItem) {
		List<RecepieItem> recepieItems = getRecepieItems();
		if(recepieItems == null) {
			recepieItems = new ArrayList<RecepieItem>(3);
			setRecepieItems(recepieItems);
		}
		
		recepieItem.setRecepie(this);
		recepieItems.add(recepieItem);
	}

}