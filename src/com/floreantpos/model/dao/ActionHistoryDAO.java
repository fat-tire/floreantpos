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
package com.floreantpos.model.dao;

import java.util.Date;

import org.apache.commons.logging.LogFactory;

import com.floreantpos.Messages;
import com.floreantpos.model.ActionHistory;
import com.floreantpos.model.User;


public class ActionHistoryDAO extends BaseActionHistoryDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public ActionHistoryDAO () {}

	public void saveHistory(User performer, String actionName, String description) {
		try {
			ActionHistory history = new ActionHistory();
			history.setActionName(actionName);
			history.setDescription(description);
			history.setPerformer(performer);
			history.setActionTime(new Date());
			save(history);
		} catch (Exception e) {
			LogFactory.getLog(ActionHistoryDAO.class).error(Messages.getString("ActionHistoryDAO.0"), e); //$NON-NLS-1$
		}
	}
}