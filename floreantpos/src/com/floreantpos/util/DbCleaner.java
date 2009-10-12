package com.floreantpos.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.hibernate.Session;

import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.model.dao._RootDAO;

public class DbCleaner {
	//DerbyDatasource datasource;
	
	protected DbCleaner(String arg0) {
		
		//datasource = new DerbyDatasource();
		//Set<String> tableNames = getTableNames();
		//Object[] objects = tableNames.toArray();
		//getColumnNames((String) objects[1]);
	}
	
	private void insertInitData() {
		UserDAO dao = new UserDAO();
		User user = new User();
		user.setFirstName("A");
		user.setLastName("B");
		
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		/*Class.forName("org.apache.derby.jdbc.ClientDriver");
		
		Connection connection = DriverManager.getConnection("jdbc:derby://localhost/posdb");
		DatabaseMetaData databaseMetaData = connection.getMetaData();
		//ResultSet crossReference = databaseMetaData.getCrossReference("*", "app", "ticket", "*", "*", "*");
		
		String[] tables = {"ACTION_HISTORY", "ATTENDENCE_HISTORY" ,"CASH_DRAWER_RESET_HISTORY",
				"DRAWER_PULL_REPORT", "GRATUITY", 
				"TICKET_ITEM_MODIFIER","TICKET_COOKING_INSTRUCTION","TICKET_COUPON_DISCOUNT",
				"TICKET_ITEM", "TICKET_ITEM_MODIFIER", "TRANSACTIONS","TICKET","USERS","VOIDTICKETS"};
		
		
		for (int i = 0; i < tables.length; i++) {
			try {
				ResultSet resultSet = databaseMetaData.getImportedKeys("",
						"APP", tables[i]);

				if (resultSet.next()) {
					//System.out.println("");
					String fk = resultSet.getString(12);
					Statement st = connection.createStatement();
					st.execute("alter table APP." + tables[i]
							+ " drop constraint " + fk);
				} else {
					System.out.println("table [" + tables[i] + "] not found");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < tables.length; i++) {
			try {
				Statement st = connection.createStatement();
				st.execute("drop table APP." + tables[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		connection.close();*/
		
		new DbCleaner(null);
		_RootDAO.initialize();
		
		User u = new User();
		u.setUserId(123);
		u.setSsn("123");
		u.setPassword("123");
		u.setFirstName("Manager");
		u.setLastName("");
		
		UserDAO dao = new UserDAO();
		dao.save(u);
		
		
		/*TicketDAO ticketDAO = new TicketDAO();
		List<Ticket> tickets = ticketDAO.findAll();
		for (Ticket ticket : tickets) {
			//ticket.setGratuity(null);
			ticketDAO.update(ticket);
		}
		
		GenericDAO genericDAO = new GenericDAO();
		Session session = genericDAO.getSession();
		//Transaction transaction = session.beginTransaction();
		
		//String[] s = {"delete Ticket", "delete PosTransaction",
		//		"delete AttendenceHistory","delete CashDrawerResetHistory"};
		
		String[] tables = { "ATTENDENCE_HISTORY", "TICKET_ITEM_MODIFIER", 
				"TICKETITEM_MODIFIERGROUP", "TICKET_ITEM", "TRANSACTIONS", 
				"DISCOUNT","TICKET_COUPON","TICKET_COUPON_DISCOUNT","TICKET_COOKING_INSTRUCTION",
				"GRATUITY", "TICKET", 
				"CASH_DRAWER_RESET_HISTORY","CASH_DRAWER_REPORT","VOIDTICKETS","DRAWER_PULL_REPORT"
				
		};

		Connection con = session.connection();
		Statement st = con.createStatement();
		st.execute("update ticket set gratuity_id=null");
		
		for (int i = 0; i < tables.length; i++) {
			try {
				st.execute("delete from " + tables[i]);
				con.commit();
			}catch(Exception x) {
				x.printStackTrace();
			}
			//Query query = session.createQuery(s[i]);
			//query.executeUpdate();
		}
		
		//transaction.commit();

		
		st.execute("update terminal set current_balance=500");
		st.execute("update users set CLOCKED_IN=0");
		con.commit();*/
	}

//	@Override
//	public Set<String> getTableNames() {
//		try {
//			Connection connection = datasource.getConnection();
//			DatabaseMetaData metaData = connection.getMetaData();
//			ResultSet tables = metaData.getTables(null, "APP", null, null);
//			
//			HashSet<String> set = new HashSet<String>();
//			while(tables.next()) {
//				String string = tables.getString(3);
//				set.add(string);
//			}
//			return set;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	@Override
//	public Set<String> getColumnNames(String tableName) {
//		try {
//			Connection connection = datasource.getConnection();
//			DatabaseMetaData metaData = connection.getMetaData();
//			ResultSet tables = metaData.getColumns(null, "APP", tableName, null);
//			
//			HashSet<String> set = new HashSet<String>();
//			while(tables.next()) {
//				String string = tables.getString(4);
//				set.add(string);
//			}
//			return set;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	@Override
//	public Set<String> getPrimaryKeyColumnNames(String tableName) {
//		try {
//			Connection connection = datasource.getConnection();
//			DatabaseMetaData metaData = connection.getMetaData();
//			ResultSet tables = metaData.getPrimaryKeys(null, "APP", tableName);
//			
//			HashSet<String> set = new HashSet<String>();
//			while(tables.next()) {
//				String string = tables.getString(4);
//				set.add(string);
//			}
//			return set;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	@Override
//	public Set<String> getNotNullColummnNames(String arg0) {
//		return null;
//	}
//
//	@Override
//	public Set<String> getViewNames() {
//		return null;
//	}
}
