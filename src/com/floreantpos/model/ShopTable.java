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

import java.util.Date;
import java.util.List;

import com.floreantpos.model.base.BaseShopTable;
import com.floreantpos.model.dao.ShopTableStatusDAO;

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
	private Date ticketCreateTime;

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

	public Integer getTicketId() {
		ShopTableStatus status = getStatus();
		if (status == null)
			return null;
		List<Integer> ticketNumbers = status.getListOfTicketNumbers();
		if (ticketNumbers != null && ticketNumbers.size() > 0)
			return ticketNumbers.get(0);
		return null;
	}

	public String getTicketIdAsString() {
		ShopTableStatus status = getStatus();
		if (status == null)
			return null;
		return status.getTicketIdAsString();
	}

	public Integer getUserId() {
		ShopTableStatus status = getStatus();
		if (status == null)
			return null;
		return status.getUserId();
	}

	public String getUserName() {
		ShopTableStatus status = getStatus();
		if (status == null)
			return null;
		return status.getUserName();
	}

	public ShopTableStatus getStatus() {
		ShopTableStatus shopTableStatus = super.getShopTableStatus();
		if (shopTableStatus != null)
			return shopTableStatus;
		return saveAndGetNewStatus();
	}

	public ShopTableStatus saveAndGetNewStatus() {
		ShopTableStatus shopTableStatus = new ShopTableStatus();
		Integer tableId = getId();
		shopTableStatus.setId(tableId);
		shopTableStatus.setTableStatus(TableStatus.Available);
		if (tableId != null)
			ShopTableStatusDAO.getInstance().save(shopTableStatus);
		setShopTableStatus(shopTableStatus);
		return shopTableStatus;
	}

	public java.lang.Boolean isFree() {
		return getTableStatus() == TableStatus.Available;
	}

	private TableStatus getTableStatus() {
		ShopTableStatus shopTableStatus = super.getShopTableStatus();
		return shopTableStatus == null ? TableStatus.Available : shopTableStatus.getTableStatus();
	}

	private void setTableStatus(TableStatus tableStatus) {
		ShopTableStatus shopTableStatus = super.getShopTableStatus();
		if (shopTableStatus != null) {
			shopTableStatus.setTableStatus(tableStatus);
		}
	}

	public void setFree(java.lang.Boolean free) {
		setTableStatus(free ? TableStatus.Available : TableStatus.Seat);
	}

	public java.lang.Boolean isServing() {
		return getTableStatus() == TableStatus.Seat;
	}

	public void setServing(java.lang.Boolean serving) {
		setTableStatus(serving ? TableStatus.Seat : TableStatus.Available);
	}

	public java.lang.Boolean isBooked() {
		return getTableStatus() == TableStatus.Booked;
	}

	public void setBooked(java.lang.Boolean booked) {
		setTableStatus(booked ? TableStatus.Booked : TableStatus.Available);
	}

	public java.lang.Boolean isDirty() {
		return getTableStatus() == TableStatus.Dirty;
	}

	public void setDirty(java.lang.Boolean dirty) {
		setTableStatus(dirty ? TableStatus.Dirty : TableStatus.Available);
	}

	public java.lang.Boolean isDisable() {
		return getTableStatus() == TableStatus.Disable;
	}

	public void setDisable(java.lang.Boolean disable) {
		setTableStatus(disable ? TableStatus.Disable : TableStatus.Available);
	}

	public Date getTicketCreateTime() {
		return ticketCreateTime;
	}

	public void setTicketCreateTime(Date ticketCreateTime) {
		this.ticketCreateTime = ticketCreateTime;
	}
}