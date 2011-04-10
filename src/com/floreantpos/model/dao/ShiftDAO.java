package com.floreantpos.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.PosException;
import com.floreantpos.model.Shift;


public class ShiftDAO extends BaseShiftDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public ShiftDAO () {}

	public boolean exists(String shiftName) throws PosException {
		Session session = null;
		
		try {
			session = createNewSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(Shift.PROP_NAME, shiftName));
			List list = criteria.list();
			if (list != null && list.size() > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			throw new PosException("An error occured while trying to check Shift duplicacy", e);
		} finally {
			if(session != null) {
				try {
					session.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	public void refresh(Shift shift) throws PosException {
		Session session = null;
		
		try {
			session = createNewSession();
			session.refresh(shift);
		} catch (Exception e) {
			throw new PosException("An error occured while refreshing Shift state.", e);
		} finally {
			if(session != null) {
				try {
					session.close();
				} catch (Exception e) {
				}
			}
		}
	}
}