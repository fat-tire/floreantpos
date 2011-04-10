package com.floreantpos.model.dao;

import java.util.Date;

import org.apache.commons.logging.LogFactory;

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
			LogFactory.getLog(ActionHistoryDAO.class).error("Error occured while trying to save action history", e);
		}
	}
}