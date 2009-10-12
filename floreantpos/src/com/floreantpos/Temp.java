package com.floreantpos;

import java.net.URL;
import java.net.URLConnection;

import com.floreantpos.model.UserPermission;
import com.floreantpos.model.UserType;
import com.floreantpos.model.dao.UserPermissionDAO;
import com.floreantpos.model.dao.UserTypeDAO;
import com.floreantpos.model.dao._RootDAO;

public class Temp {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		/*URL url = new URL("http://www.somewhereinblog.net/blog/rodayaablog");
		for (int i = 0; i < 11; i++) {
			URLConnection connection = url.openConnection();
			connection.connect();
			Object content = connection.getContent();
		}*/
		
		_RootDAO.initialize();
		
		UserType userType = new UserType();
		userType.setName("MANAGER");
		UserPermission userPermission = new UserPermission("Void Ticket2");
		
		userType.addTopermissions(UserPermission.VOID_TICKET);
		
		UserPermissionDAO dao1 = new UserPermissionDAO();
		UserTypeDAO dao = new UserTypeDAO();
		//dao1.saveOrUpdate(userPermission);
		dao.saveOrUpdate(userType);
		
		//UserType type = dao.load(new Integer(1));
		//System.out.println(type.getPermissions().size());
	}

}
