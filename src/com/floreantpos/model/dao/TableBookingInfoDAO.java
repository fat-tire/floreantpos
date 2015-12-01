package com.floreantpos.model.dao;

import java.util.ArrayList;
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
			criteria.add(Restrictions.ge(TableBookingInfo.PROP_TO_DATE, startDate));

			List<TableBookingInfo> list = criteria.list();
			List<TableBookingInfo> bookings = new ArrayList<TableBookingInfo>();

			for (TableBookingInfo tableBookingInfo : list) {
				if(DateUtil.between(tableBookingInfo.getFromDate(), tableBookingInfo.getToDate(), startDate)
						|| DateUtil.between(tableBookingInfo.getFromDate(), tableBookingInfo.getToDate(), endDate)) {
					bookings.add(tableBookingInfo);
				}
			}

			Set<ShopTable> bookedTables = new HashSet<ShopTable>();
			for (TableBookingInfo tableBookingInfo : bookings) {
				List<ShopTable> tables = tableBookingInfo.getTables();
				if(tables != null) {
					bookedTables.addAll(tables);
				}
			}

			return bookedTables;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(session != null) {
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

	public List<TableBookingInfo> getAllOpenBookedTable() {
		Session session = null;
		try {
			session = createNewSession();

			Criteria criteria = session.createCriteria(getReferenceClass());
			criteria.add(Restrictions.eq(TableBookingInfo.PROP_STATUS, "open"));
			List list = criteria.list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				session.close();
			} catch (Exception e2) {
			}
		}
		return null;

	}

	public void closeBooked(TableBookingInfo bookingInfo, List<ShopTable> tables) {

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

	}
}