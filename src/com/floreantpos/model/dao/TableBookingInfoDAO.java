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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.PosLog;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.TableBookingInfo;
import com.floreantpos.model.util.DateUtil;

public class TableBookingInfoDAO extends BaseTableBookingInfoDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public TableBookingInfoDAO() {
	}

	public Collection<ShopTable> getBookedTables(Date startDate, Date endDate) {

		Session session = null;
		try {
			session = createNewSession();
			Criteria criteria = session.createCriteria(getReferenceClass());
			//	criteria.add(Restrictions.ge(TableBookingInfo.PROP_TO_DATE, startDate));

			criteria.add(Restrictions.ge(TableBookingInfo.PROP_TO_DATE, startDate))
					.add(Restrictions.ne(TableBookingInfo.PROP_STATUS, TableBookingInfo.STATUS_CANCEL))
					.add(Restrictions.ne(TableBookingInfo.PROP_STATUS, TableBookingInfo.STATUS_NO_APR))
					.add(Restrictions.ne(TableBookingInfo.PROP_STATUS, TableBookingInfo.STATUS_CLOSE));

			List<TableBookingInfo> list = criteria.list();
			List<TableBookingInfo> bookings = new ArrayList<TableBookingInfo>();

			for (TableBookingInfo tableBookingInfo : list) {
				if (DateUtil.between(tableBookingInfo.getFromDate(), tableBookingInfo.getToDate(), startDate)
						|| DateUtil.between(tableBookingInfo.getFromDate(), tableBookingInfo.getToDate(), endDate)) {
					bookings.add(tableBookingInfo);
				}
			}

			Set<ShopTable> bookedTables = new HashSet<ShopTable>();
			for (TableBookingInfo tableBookingInfo : bookings) {
				List<ShopTable> tables = tableBookingInfo.getTables();
				if (tables != null) {
					bookedTables.addAll(tables);
				}
			}

			return bookedTables;
		} catch (Exception e) {
			PosLog.error(getClass(), e);
		} finally {
			if (session != null) {
				closeSession(session);
			}
		}
		return null;
	}

	public List<ShopTable> getFreeTables(Date startDate, Date endDate) {
		Collection<ShopTable> bookedTables = getBookedTables(startDate, endDate);
		List<ShopTable> allTables = ShopTableDAO.getInstance().findAll();

		allTables.removeAll(bookedTables);

		return allTables;
	}

	public List<TableBookingInfo> getAllOpenBooking() {
		Session session = null;
		try {
			session = createNewSession();

			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.ne(TableBookingInfo.PROP_STATUS, TableBookingInfo.STATUS_CLOSE));
			List list = criteria.list();
			return list;
		} catch (Exception e) {
			PosLog.error(getClass(), e);
		} finally {
			try {
				session.close();
			} catch (Exception e2) {
			}
		}
		return null;

	}

	public void setBookingStatus(TableBookingInfo bookingInfo, String bookingStatus, List<ShopTable> tables) {

		Session session = null;
		Transaction tx = null;

		try {
			session = createNewSession();
			tx = session.beginTransaction();

			bookingInfo.setStatus(bookingStatus);
			saveOrUpdate(bookingInfo);

			if (bookingStatus.equals(TableBookingInfo.STATUS_SEAT) || bookingStatus.equals(TableBookingInfo.STATUS_DELAY)) {
				ShopTableDAO.getInstance().bookedTables(tables);
			}

			if (bookingStatus.equals(TableBookingInfo.STATUS_CANCEL) || bookingStatus.equals(TableBookingInfo.STATUS_NO_APR)
					|| bookingStatus.equals(TableBookingInfo.STATUS_CLOSE)) {
				ShopTableDAO.getInstance().freeTables(tables);
			}

			tx.commit();

		} catch (Exception e) {
			tx.rollback();
			LogFactory.getLog(TableBookingInfo.class).error(e);
			throw new RuntimeException(e);
		} finally {
			closeSession(session);
		}

	}

	/*public void closeBooking(TableBookingInfo bookingInfo, List<ShopTable> tables) {

		Session session = null;
		Transaction tx = null;

		try {
			session = createNewSession();
			tx = session.beginTransaction();

			bookingInfo.setStatus("close");

			saveOrUpdate(bookingInfo);

			ShopTableDAO.getInstance().freeTables(tables);

			tx.commit();

		} catch (Exception e) {
			tx.rollback();
			LogFactory.getLog(TableBookingInfo.class).error(e);
			throw new RuntimeException(e);
		} finally {
			closeSession(session);
		}

	}*/

	public List getTodaysBooking() {

		Session session = null;
		try {

			Calendar startDate = Calendar.getInstance();
			startDate.setLenient(false);
			startDate.setTime(new Date());
			startDate.set(Calendar.HOUR_OF_DAY, 0);
			startDate.set(Calendar.MINUTE, 0);
			startDate.set(Calendar.SECOND, 0);
			startDate.set(Calendar.MILLISECOND, 0);

			Calendar endDate = Calendar.getInstance();
			endDate.setLenient(false);
			endDate.setTime(new Date());
			endDate.set(Calendar.HOUR_OF_DAY, 23);
			endDate.set(Calendar.MINUTE, 59);
			endDate.set(Calendar.SECOND, 59);

			session = createNewSession();

			Criteria criteria = session.createCriteria(TableBookingInfo.class);

			criteria.add(Restrictions.ge(TableBookingInfo.PROP_FROM_DATE, startDate.getTime()))
					.add(Restrictions.le(TableBookingInfo.PROP_FROM_DATE, endDate.getTime()))
					.add(Restrictions.eq(TableBookingInfo.PROP_STATUS, TableBookingInfo.STATUS_OPEN));
			List list = criteria.list();

			return list;
		} catch (Exception e) {
			PosLog.error(getClass(), e);
		} finally {
			session.close();
		}
		return null;
	}

	public List getAllBookingByDate(Date startDate, Date endDate) {

		Session session = null;
		try {
			session = createNewSession();
			Criteria criteria = session.createCriteria(TableBookingInfo.class);

			criteria.add(Restrictions.ge(TableBookingInfo.PROP_FROM_DATE, startDate)).add(Restrictions.le(TableBookingInfo.PROP_FROM_DATE, endDate))
					.add(Restrictions.ne(TableBookingInfo.PROP_STATUS, TableBookingInfo.STATUS_CLOSE));

			List list = criteria.list();

			return list;
		} catch (Exception e) {
			PosLog.error(getClass(), e);
		} finally {
			session.close();
		}
		return null;
	}
}