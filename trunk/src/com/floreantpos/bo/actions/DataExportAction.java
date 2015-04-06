package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.MenuItemModifierGroupDAO;
import com.floreantpos.model.dao.MenuModifierDAO;
import com.floreantpos.model.dao.MenuModifierGroupDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.datamigrate.Elements;

public class DataExportAction extends AbstractAction {
	public DataExportAction() {
		super("Export Menu Items");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Session session = null;
		Transaction transaction = null;
		FileWriter fileWriter = null;
		GenericDAO dao = new GenericDAO();

		try {
			JFileChooser fileChooser = getFileChooser();
			int option = fileChooser.showSaveDialog(com.floreantpos.util.POSUtil.getFocusedWindow());
			if (option != JFileChooser.APPROVE_OPTION) {
				return;
			}

			File file = fileChooser.getSelectedFile();
			if (file.exists()) {
				option = JOptionPane.showConfirmDialog(com.floreantpos.util.POSUtil.getFocusedWindow(), "Overwrite file " + file.getName() + "?", "Confirm",
						JOptionPane.YES_NO_OPTION);
				if (option != JOptionPane.YES_OPTION) {
					return;
				}
			}

			fixMenuItemModifierGroups();

			JAXBContext jaxbContext = JAXBContext.newInstance(Elements.class);
			Marshaller m = jaxbContext.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

			StringWriter writer = new StringWriter();

			session = dao.createNewSession();
			transaction = session.beginTransaction();

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

			elements.setTaxes(TaxDAO.getInstance().findAll(session));
			elements.setMenuCategories(MenuCategoryDAO.getInstance().findAll(session));
			elements.setMenuGroups(MenuGroupDAO.getInstance().findAll(session));
			elements.setMenuModifiers(MenuModifierDAO.getInstance().findAll(session));
			elements.setMenuModifierGroups(MenuModifierGroupDAO.getInstance().findAll(session));
			elements.setMenuItems(MenuItemDAO.getInstance().findAll(session));
			elements.setMenuItemModifierGroups(MenuItemModifierGroupDAO.getInstance().findAll(session));

			//	        elements.setUsers(UserDAO.getInstance().findAll(session));
			//	        
			//	        elements.setMenuItemShifts(MenuItemShiftDAO.getInstance().findAll(session));
			//	        elements.setRestaurants(RestaurantDAO.getInstance().findAll(session));
			//	        elements.setUserTypes(UserTypeDAO.getInstance().findAll(session));
			//	        elements.setUserPermissions(UserPermissionDAO.getInstance().findAll(session));
			//	        elements.setShifts(ShiftDAO.getInstance().findAll(session));

			m.marshal(elements, writer);

			transaction.commit();

			fileWriter = new FileWriter(file);
			fileWriter.write(writer.toString());
			fileWriter.close();

			POSMessageDialog.showMessage(com.floreantpos.util.POSUtil.getFocusedWindow(), "Saved!");

		} catch (Exception e1) {
			transaction.rollback();
			e1.printStackTrace();
			POSMessageDialog.showMessage(com.floreantpos.util.POSUtil.getFocusedWindow(), e1.getMessage());
		} finally {
			IOUtils.closeQuietly(fileWriter);
			dao.closeSession(session);
		}
	}

	public static JFileChooser getFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setSelectedFile(new File("floreantpos-menu-items.xml"));
		fileChooser.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return "XML File";
			}

			@Override
			public boolean accept(File f) {
				if (f.getName().endsWith(".xml")) {
					return true;
				}

				return false;
			}
		});
		return fileChooser;
	}

	private void fixMenuItemModifierGroups() {
		MenuItemModifierGroupDAO menuItemModifierGroupDAO = MenuItemModifierGroupDAO.getInstance();
		Session session = menuItemModifierGroupDAO.createNewSession();
		Transaction transaction = session.beginTransaction();

		try {

			List<MenuItem> menuItems = MenuItemDAO.getInstance().findAll(session);

			for (MenuItem menuItem : menuItems) {
				List<MenuItemModifierGroup> modiferGroups = menuItem.getMenuItemModiferGroups();
				for (MenuItemModifierGroup menuItemModifierGroup : modiferGroups) {
					//menuItemModifierGroup.setParentMenuItem(menuItem);
					menuItemModifierGroupDAO.saveOrUpdate(menuItemModifierGroup, session);
				}
			}

			transaction.commit();
		} catch(Exception x) {
			if(transaction != null) transaction.rollback();
			
		} finally {
			menuItemModifierGroupDAO.closeSession(session);
		}
	}
}
