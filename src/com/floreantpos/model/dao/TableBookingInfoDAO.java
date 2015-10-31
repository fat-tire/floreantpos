package com.floreantpos.model.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
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
			List<TableBookingInfo> list2 = new ArrayList<TableBookingInfo>();
			for (TableBookingInfo tableBookingInfo : list) {
				if (DateUtil.between(tableBookingInfo.getFromDate(), tableBookingInfo.getToDate(), startDate) || 
						DateUtil.between(tableBookingInfo.getFromDate(), tableBookingInfo.getToDate(), endDate)) {

					list2.add(tableBookingInfo);
				}

			}
			
			Set<ShopTable> bookedTables = new HashSet<ShopTable>();
			for (TableBookingInfo tableBookingInfo : list2) {
				List<ShopTable> tables = tableBookingInfo.getTables();
				if(tables != null) {
					bookedTables.addAll(tables);
				}
			}

			return bookedTables;
		} catch (Exception e) {
			e.printStackTrace();
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
	
	
	public List<ShopTable>  bookedShopTable(){
		
		Set storeShopTable = new LinkedHashSet();
		for (TableBookingInfo tableBookingInfo : TableBookingInfoDAO.getInstance().findAll()) {
			if (!tableBookingInfo.getTables().isEmpty()) {
				List<ShopTable> shopTables = tableBookingInfo.getTables();
				if (!shopTables.isEmpty()) {
					for (ShopTable shopTable : shopTables) {
						if (shopTable != null) {
							storeShopTable.add(shopTable);
						}
					}
				}
			}
		}

		List bookedShopTable = new ArrayList();
		bookedShopTable.addAll(storeShopTable);
		
		
		return bookedShopTable;
	}
}