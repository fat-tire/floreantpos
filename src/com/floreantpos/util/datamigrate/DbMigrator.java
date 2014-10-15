package com.floreantpos.util.datamigrate;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import com.floreantpos.model.MenuItem;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.util.DatabaseUtil;

public class DbMigrator {
	/**
	 * Tables to migrate:
	 * 
	 * 1. TERMINAL
	 * 2. USERS
	 * 3. TAX
	 * 4. MENU_CATEGORY
	 * 5. MENU_GROUP
	 * 6. MENU_MODIFIER
	 * 7. MENU_MODIFIER_GROUP
	 * 8. MENU_ITEM
	 * 9. MENU_ITEM_SHIFT
	 * 10. RESTAURANT
	 * 11. USER_TYPE
	 * 12. USER_PERMISSION
	 * 13. SHIFT
	 * 
	 * 
	 * @param args
	 * @throws Exception
	 */
	
	
	
	public static void main(String[] args) throws Exception {
		DatabaseUtil.initialize();
		
		JAXBContext jaxbContext = JAXBContext.newInstance(MenuItem.class);
		Marshaller m = jaxbContext.createMarshaller();
        //for pretty-print XML in JAXB
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		
		List<MenuItem> list = MenuItemDAO.getInstance().findAll();
		List<MenuItem> list2 = new MenuList();
		
		for (MenuItem menuItem : list) {
			MenuItem menuItem2 = MenuItemDAO.getInstance().initialize(menuItem);
			list2.add(menuItem2);
			m.marshal(menuItem2, System.out);
		}
		//m.marshal(list2, System.out);
		
		Unmarshaller um = jaxbContext.createUnmarshaller();
	}
	
	@XmlRootElement(name="menu-items")
	static class MenuList extends ArrayList {
		
	}
}
