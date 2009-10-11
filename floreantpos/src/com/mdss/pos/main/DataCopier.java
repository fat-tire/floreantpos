package com.mdss.pos.main;

import java.util.Calendar;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import com.mdss.pos.model.Restaurant;
import com.mdss.pos.model.Shift;
import com.mdss.pos.model.User;
import com.mdss.pos.model.dao.GenericDAO;
import com.mdss.pos.model.dao._RootDAO;
import com.mdss.pos.util.ShiftUtil;

public class DataCopier {
	static String configFile1 = "/hibernate.cfg.xml";
	static String configFile2 = "/db-backup-hibernate.cfg.xml";
	
	static void createNewDb(Configuration configuration) {
		SchemaExport schemaExport = new SchemaExport(configuration);
		schemaExport.create(true, true);
		
		GenericDAO dao = new GenericDAO();

		Restaurant restaurant = new Restaurant();
		restaurant.setId(1);
		restaurant.setName("Floreant Restaurant");
		restaurant.setAddressLine1("addressLine1");
		restaurant.setAddressLine2("addressLine2");
		dao.saveOrUpdate(restaurant);

		User user = new User();
		user.setUserId(123);
		user.setPassword("123");
		user.setFirstName("Manager");
		user.setLastName("1");
		dao.saveOrUpdate(user);
		
		Shift shift = new Shift();
		Date start = ShiftUtil.buildShiftStartTime(6,0,Calendar.AM,2,0,Calendar.PM);
		Date end = ShiftUtil.buildShiftEndTime(2,0,Calendar.PM,10,0,Calendar.PM);
		shift.setName("MORNING");
		shift.setStartTime(start);
		shift.setEndTime(end);
		shift.setShiftLength(Math.abs(start.getTime() - end.getTime()));
		
		dao.saveOrUpdate(shift);
		
		shift = new Shift();
		start = ShiftUtil.buildShiftStartTime(2,0,Calendar.PM,10,0,Calendar.PM);
		end = ShiftUtil.buildShiftEndTime(10,0,Calendar.PM,6,0,Calendar.AM);
		shift.setName("DAY");
		shift.setStartTime(start);
		shift.setEndTime(end);
		shift.setShiftLength(Math.abs(start.getTime() - end.getTime()));
		
		dao.saveOrUpdate(shift);
		
		shift = new Shift();
		start = ShiftUtil.buildShiftStartTime(10,0,Calendar.PM,6,0,Calendar.AM);
		end = ShiftUtil.buildShiftEndTime(6,0,Calendar.AM,2,0,Calendar.PM);
		shift.setName("NIGHT");
		shift.setStartTime(start);
		shift.setEndTime(end);
		shift.setShiftLength(Math.abs(start.getTime() - end.getTime()));
		
		dao.saveOrUpdate(shift);
	}
	static void copy(Configuration configuration, Configuration configuration2) {
		GenericDAO dao = new GenericDAO();
		Session srcSession = dao.getSession(configFile1, true);
		Session destSession = dao.getSession(configFile2, true);
		
		srcSession.close();
		destSession.close();
		
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Configuration configuration = _RootDAO.getNewConfiguration(configFile1).configure();
		//Configuration configuration2 = _RootDAO.getNewConfiguration(configFile2).configure();
		
		_RootDAO.initialize(configFile1);
		_RootDAO.initialize(configFile2);
		//createNewDb(configuration);
		copy(null, null);
	}

}
