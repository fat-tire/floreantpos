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
package com.floreantpos.posserver;

import java.util.ArrayList;
import java.util.List;

import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;

public enum TransactionType {
	GET_TABLES("45"), APPLY_PAYMENT("46"), PRINT_CHECK("47");

	private String displayString;

	TransactionType(String display) {
		this.displayString = display;
	}

	@Override
	public String toString() {
		return displayString;
	}

	public String getDisplayString() {
		return displayString;
	}

	public void setDisplayString(String displayString) {
		this.displayString = displayString;
	}

	public POSResponse createPOSResponse(POSRequest posRequest) {
		POSResponse posResponse = null;
		switch (this) {
			case GET_TABLES:
				if (posRequest.posDefaultInfo.table.equals("0")) {
					posResponse = getAllTables(posRequest);
				}
				else {
					posResponse = getTable(posRequest);
				}

				break;

			case APPLY_PAYMENT:
				posResponse = applyPayment(posRequest);
				break;

			case PRINT_CHECK:
				break;
		}

		return posResponse;
	}

	private POSResponse getAllTables(POSRequest posRequest) {
		POSResponse posResponse = new POSResponse();

		User user = UserDAO.getInstance().findUserBySecretKey(posRequest.posDefaultInfo.server);
		List<Ticket> ticketsForUser = TicketDAO.getInstance().findOpenTicketsForUser(user);

		Ident ident = new Ident();
		ident.setId(posRequest.ident.id);
		ident.setTermserialno(posRequest.ident.termserialno);
		ident.setTtype("45");

		POSDefaultInfo posDefaultInfo = new POSDefaultInfo();
		posDefaultInfo.setServer(posRequest.posDefaultInfo.server);
		posDefaultInfo.setRes("1");
		posDefaultInfo.setrText("Success");

		posResponse.setIdent(ident);
		posResponse.setPosDefaultInfo(posDefaultInfo);

		Checks checks = new Checks();

		checks.setCheckList(new ArrayList<Check>());

		for (Ticket ticket : ticketsForUser) {
			List<Integer> tableNumbers = ticket.getTableNumbers();
			if (tableNumbers != null && tableNumbers.size() > 0) {
				Check chk = new Check();
				chk.setTableNo(String.valueOf(tableNumbers.get(0)));
				chk.setTableName("");
				chk.setChkName(String.valueOf(ticket.getId()));
				chk.setChkNo(String.valueOf(ticket.getId()));
				chk.setAmt(String.valueOf(Math.round((ticket.getDueAmount() - ticket.getTaxAmount()) * 100)));
				chk.setTax(String.valueOf(Math.round(ticket.getTaxAmount() * 100)));
				checks.getCheckList().add(chk);
			}
		}

		posResponse.setChecks(checks);

		return posResponse;
	}

	private POSResponse getTable(POSRequest posRequest) {
		POSResponse posResponse = new POSResponse();

		User user = UserDAO.getInstance().findUserBySecretKey(posRequest.posDefaultInfo.server);
		List<Ticket> ticketsForUser = TicketDAO.getInstance().findOpenTicketsForUser(user);

		Ident ident = new Ident();
		ident.setId(posRequest.ident.id);
		ident.setTermserialno(posRequest.ident.termserialno);
		ident.setTtype("45");

		POSDefaultInfo posDefaultInfo = new POSDefaultInfo();
		posDefaultInfo.setServer(posRequest.posDefaultInfo.server);
		posDefaultInfo.setTable(posRequest.posDefaultInfo.table);
		posDefaultInfo.setRes("1");
		posDefaultInfo.setrText("Success");

		posResponse.setIdent(ident);
		posResponse.setPosDefaultInfo(posDefaultInfo);

		Checks checks = new Checks();

		checks.setCheckList(new ArrayList<Check>());

		for (Ticket ticket : ticketsForUser) {
			List<Integer> tableNumbers = ticket.getTableNumbers();
			if (tableNumbers != null && tableNumbers.size() > 0) {
				if (tableNumbers.contains(Integer.parseInt(posRequest.posDefaultInfo.table))) {
					Check chk = new Check();
					chk.setTableNo(String.valueOf(posDefaultInfo.table));
					chk.setTableName("");
					chk.setChkName("");
					chk.setChkNo(String.valueOf(ticket.getId()));
					chk.setAmt(String.valueOf(Math.round((ticket.getDueAmount() - ticket.getTaxAmount()) * 100)));
					chk.setTax(String.valueOf(Math.round(ticket.getTaxAmount() * 100)));
					checks.getCheckList().add(chk);
					break;
				}
			}
		}

		posResponse.setChecks(checks);

		return posResponse;
	}

	private POSResponse applyPayment(POSRequest posRequest) {
		POSResponse posResponse = new POSResponse();

		Ticket ticket = TicketDAO.getInstance().loadFullTicket(Integer.parseInt(posRequest.posDefaultInfo.check));

		Ident ident = new Ident();
		ident.setId(posRequest.ident.id);
		ident.setTermserialno(posRequest.ident.termserialno);
		ident.setTtype("46");

		POSDefaultInfo posDefaultInfo = new POSDefaultInfo();
		posDefaultInfo.setTable(posRequest.posDefaultInfo.table);
		posDefaultInfo.setCheck(posRequest.posDefaultInfo.check);
		posDefaultInfo.setServer(posRequest.posDefaultInfo.server);
		posDefaultInfo.setRes("1");
		posDefaultInfo.setrText("Success");

		posResponse.setIdent(ident);
		posResponse.setPosDefaultInfo(posDefaultInfo);

		PrintText line1 = new PrintText("---Restaurant Name---");
		PrintText line2 = new PrintText("---Address---");
		PrintText line3 = new PrintText("---Cell---");
		//PrintText line4 = new PrintText("-------" + ticket.getId() + "-------");

		posResponse.getPrintText().add(line1);
		posResponse.getPrintText().add(line2);
		posResponse.getPrintText().add(line3);
		//posResponse.getPrintText().add(line4);

		return posResponse;
	}
}
