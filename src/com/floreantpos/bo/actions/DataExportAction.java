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
package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.Messages;
import com.floreantpos.PosLog;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.MenuItemModifierGroupDAO;
import com.floreantpos.model.dao.MenuModifierDAO;
import com.floreantpos.model.dao.ModifierGroupDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.model.dao.UserTypeDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.AESencrp;
import com.floreantpos.util.datamigrate.Elements;

public class DataExportAction extends AbstractAction {
	public DataExportAction() {
		super(Messages.getString("DataExportAction.0")); //$NON-NLS-1$
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Session session = null;
		FileWriter fileWriter = null;
		GenericDAO dao = new GenericDAO();

		try {
			JFileChooser fileChooser = getFileChooser();
			int option = fileChooser.showSaveDialog(com.floreantpos.util.POSUtil.getBackOfficeWindow());
			if (option != JFileChooser.APPROVE_OPTION) {
				return;
			}

			File file = fileChooser.getSelectedFile();
			if (file.exists()) {
				option = JOptionPane.showConfirmDialog(com.floreantpos.util.POSUtil.getFocusedWindow(), Messages.getString("DataExportAction.1") + file.getName() + "?", Messages.getString("DataExportAction.3"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						JOptionPane.YES_NO_OPTION);
				if (option != JOptionPane.YES_OPTION) {
					return;
				}
			}

			// fixMenuItemModifierGroups();

			JAXBContext jaxbContext = JAXBContext.newInstance(Elements.class);
			Marshaller m = jaxbContext.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

			StringWriter writer = new StringWriter();

			session = dao.createNewSession();

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
			elements.setModifierGroups(ModifierGroupDAO.getInstance().findAll(session));
			elements.setMenuItems(MenuItemDAO.getInstance().findAll(session));
			elements.setMenuItemModifierGroups(MenuItemModifierGroupDAO.getInstance().findAll(session));
			List<User> users = UserDAO.getInstance().findAll(session);
//			List<User> userList= new ArrayList<User>();
			for (User user : users) {
				try {
					String password = user.getPassword();
					if (StringUtils.isNotEmpty(password))
						user.setPassword(AESencrp.encrypt(password));
				} catch (Exception ex) {
					continue;
				}
			}
			elements.setUsers(users);
			elements.setUserTypes(UserTypeDAO.getInstance().findAll(session));
			//	        
			//	        elements.setMenuItemShifts(MenuItemShiftDAO.getInstance().findAll(session));
			//	        elements.setRestaurants(RestaurantDAO.getInstance().findAll(session));
			//	        elements.setUserPermissions(UserPermissionDAO.getInstance().findAll(session));
			//	        elements.setShifts(ShiftDAO.getInstance().findAll(session));

			m.marshal(elements, writer);

			fileWriter = new FileWriter(file);
			fileWriter.write(writer.toString());
			fileWriter.close();

			POSMessageDialog.showMessage(com.floreantpos.util.POSUtil.getFocusedWindow(), Messages.getString("DataExportAction.4")); //$NON-NLS-1$

		} catch (Exception e1) {
			PosLog.error(getClass(), e1);
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
		fileChooser.setSelectedFile(new File("floreantpos-menu-items.xml")); //$NON-NLS-1$
		fileChooser.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return "XML File"; //$NON-NLS-1$
			}

			@Override
			public boolean accept(File f) {
				if (f.getName().endsWith(".xml")) { //$NON-NLS-1$
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
		} catch (Exception x) {
			if (transaction != null)
				transaction.rollback();

		} finally {
			menuItemModifierGroupDAO.closeSession(session);
		}
	}
}
