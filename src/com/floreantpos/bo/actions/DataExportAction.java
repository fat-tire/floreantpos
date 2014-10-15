package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.MenuItemShiftDAO;
import com.floreantpos.model.dao.MenuModifierDAO;
import com.floreantpos.model.dao.MenuModifierGroupDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.ShiftDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.model.dao.UserPermissionDAO;
import com.floreantpos.model.dao.UserTypeDAO;
import com.floreantpos.util.datamigrate.Elements;

public class DataExportAction extends AbstractAction {
	public DataExportAction() {
		super("Export Data");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			JFileChooser fileChooser = new JFileChooser();
			int option = fileChooser.showSaveDialog(BackOfficeWindow.getInstance());
			if(option != JFileChooser.APPROVE_OPTION) {
				return;
			}
			
			File file = fileChooser.getSelectedFile();
			
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Elements.class);
			Marshaller m = jaxbContext.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	        m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
	        
	        StringWriter writer = new StringWriter();
	        
	        GenericDAO dao = new GenericDAO();
	        Session session = dao.createNewSession();
	        Transaction transaction = session.beginTransaction();
	        
	        Elements elements = new Elements();
	        
//	   	 * 2. USERS
//	   	 * 3. TAX
//	   	 * 4. MENU_CATEGORY
//	   	 * 5. MENU_GROUP
//	   	 * 6. MENU_MODIFIER
//	   	 * 7. MENU_MODIFIER_GROUP
//	   	 * 8. MENU_ITEM
//	   	 * 9. MENU_ITEM_SHIFT
//	   	 * 10. RESTAURANT
//	   	 * 11. USER_TYPE
//	   	 * 12. USER_PERMISSION
//	   	 * 13. SHIFT
	        
	        elements.setUsers(UserDAO.getInstance().findAll(session));
	        elements.setTaxes(TaxDAO.getInstance().findAll(session));
	        elements.setMenuCategories(MenuCategoryDAO.getInstance().findAll(session));
	        elements.setMenuGroups(MenuGroupDAO.getInstance().findAll(session));
	        elements.setMenuModifiers(MenuModifierDAO.getInstance().findAll(session));
	        elements.setMenuModifierGroups(MenuModifierGroupDAO.getInstance().findAll(session));
	        elements.setMenuItems(MenuItemDAO.getInstance().findAll(session));
	        elements.setMenuItemShifts(MenuItemShiftDAO.getInstance().findAll(session));
	        elements.setRestaurants(RestaurantDAO.getInstance().findAll(session));
	        elements.setUserTypes(UserTypeDAO.getInstance().findAll(session));
	        elements.setUserPermissions(UserPermissionDAO.getInstance().findAll(session));
	        elements.setShifts(ShiftDAO.getInstance().findAll(session));
	        
	        m.marshal(elements, writer);
	        
	        transaction.commit();
	        session.close();
	        
	        
	        FileWriter fileWriter = new FileWriter(file);
	        fileWriter.write(writer.toString());
	        fileWriter.close();
	        
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
