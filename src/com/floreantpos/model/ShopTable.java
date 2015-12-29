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

import com.floreantpos.model.base.BaseShopTable;

public class ShopTable extends BaseShopTable {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public ShopTable() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ShopTable(java.lang.Integer id) {
		super(id);
	}

	/*[CONSTRUCTOR MARKER END]*/

	private boolean isTemporary;

	public ShopTable(Integer x, Integer y) {
		super();

		setX(x);
		setY(y);
	}

	public ShopTable(ShopFloor floor, Integer x, Integer y, Integer id) {
		super();
		setCapacity(4);
		setId(id);
		setFloor(floor);
		setX(x);
		setY(y);
	}

	public Integer getTableNumber() {
		return getId();
	}

	@Override
	public String toString() {
		return String.valueOf(getTableNumber());
	}

	public boolean isTemporary() {
		return isTemporary;
	}

	public void setTemporary(boolean isTemporary) {
		this.isTemporary = isTemporary;
	}
}