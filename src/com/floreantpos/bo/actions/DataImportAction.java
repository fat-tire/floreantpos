package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemModifierGroup;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.model.Tax;
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

public class DataImportAction extends AbstractAction {

	public DataImportAction() {
		super("Import Menu Items");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = DataExportAction.getFileChooser();
		int option = fileChooser.showOpenDialog(BackOfficeWindow.getInstance());
		if (option != JFileChooser.APPROVE_OPTION) {
			return;
		}

		File file = fileChooser.getSelectedFile();
		try {

			importMenuItemsFromFile(file);
			POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), "Success!");

		} catch (Exception e1) {
			e1.printStackTrace();

			POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), e1.getMessage());
		}

	}
	
	public static void importMenuItemsFromFile(File file) throws Exception {
		if (file == null)
			return;

		FileInputStream	inputStream = new FileInputStream(file);
		importMenuItems(inputStream);
	}

	public static void importMenuItems(InputStream inputStream) throws Exception {
		Session session = null;
		Transaction transaction = null;
		GenericDAO dao = new GenericDAO();

		Map<String, Object> objectMap = new HashMap<String, Object>();

		try {


			JAXBContext jaxbContext = JAXBContext.newInstance(Elements.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			Elements elements = (Elements) unmarshaller.unmarshal(inputStream);

			session = dao.createNewSession();
			transaction = session.beginTransaction();

			List<Tax> taxes = elements.getTaxes();
			if (taxes != null) {
				for (Tax tax : taxes) {
					objectMap.put(tax.getUniqueId(), tax);
					tax.setId(null);

					TaxDAO.getInstance().save(tax, session);
				}
			}

			List<MenuCategory> menuCategories = elements.getMenuCategories();
			if (menuCategories != null) {
				for (MenuCategory menuCategory : menuCategories) {

					objectMap.put(menuCategory.getUniqueId(), menuCategory);
					menuCategory.setId(null);

					MenuCategoryDAO.getInstance().save(menuCategory, session);
				}
			}

			List<MenuGroup> menuGroups = elements.getMenuGroups();
			if (menuGroups != null) {
				for (MenuGroup menuGroup : menuGroups) {

					MenuCategory menuCategory = menuGroup.getParent();
					if (menuCategory != null) {
						menuCategory = (MenuCategory) objectMap.get(menuCategory.getUniqueId());
						menuGroup.setParent(menuCategory);
					}

					objectMap.put(menuGroup.getUniqueId(), menuGroup);
					menuGroup.setId(null);

					MenuGroupDAO.getInstance().saveOrUpdate(menuGroup, session);
				}
			}

			List<MenuModifierGroup> menuModifierGroups = elements.getMenuModifierGroups();
			if (menuModifierGroups != null) {
				for (MenuModifierGroup menuModifierGroup : menuModifierGroups) {
					objectMap.put(menuModifierGroup.getUniqueId(), menuModifierGroup);
					menuModifierGroup.setId(null);

					MenuModifierGroupDAO.getInstance().saveOrUpdate(menuModifierGroup, session);
				}
			}

			List<MenuModifier> menuModifiers = elements.getMenuModifiers();
			if (menuModifiers != null) {
				for (MenuModifier menuModifier : menuModifiers) {

					objectMap.put(menuModifier.getUniqueId(), menuModifier);
					menuModifier.setId(null);

					MenuModifierGroup menuModifierGroup = menuModifier.getModifierGroup();
					if (menuModifierGroup != null) {
						menuModifierGroup = (MenuModifierGroup) objectMap.get(menuModifierGroup.getUniqueId());
						menuModifier.setModifierGroup(menuModifierGroup);
					}

					Tax tax = menuModifier.getTax();
					if (tax != null) {
						tax = (Tax) objectMap.get(tax.getUniqueId());
						menuModifier.setTax(tax);
					}

					MenuModifierDAO.getInstance().saveOrUpdate(menuModifier, session);
				}
			}

			List<MenuItemModifierGroup> menuItemModifierGroups = elements.getMenuItemModifierGroups();
			if (menuItemModifierGroups != null) {
				for (MenuItemModifierGroup mimg : menuItemModifierGroups) {
					objectMap.put(mimg.getUniqueId(), mimg);
					mimg.setId(null);

					MenuModifierGroup menuModifierGroup = mimg.getModifierGroup();
					if (menuModifierGroup != null) {
						menuModifierGroup = (MenuModifierGroup) objectMap.get(menuModifierGroup.getUniqueId());
						mimg.setModifierGroup(menuModifierGroup);
					}

					MenuItemModifierGroupDAO.getInstance().save(mimg, session);
				}
			}

			List<MenuItem> menuItems = elements.getMenuItems();
			if (menuItems != null) {
				for (MenuItem menuItem : menuItems) {

					objectMap.put(menuItem.getUniqueId(), menuItem);
					menuItem.setId(null);

					MenuGroup menuGroup = menuItem.getParent();
					if (menuGroup != null) {
						menuGroup = (MenuGroup) objectMap.get(menuGroup.getUniqueId());
						menuItem.setParent(menuGroup);
					}

					Tax tax = menuItem.getTax();
					if (tax != null) {
						tax = (Tax) objectMap.get(tax.getUniqueId());
						menuItem.setTax(tax);
					}

					List<MenuItemModifierGroup> menuItemModiferGroups = menuItem.getMenuItemModiferGroups();
					if (menuItemModiferGroups != null) {
						for (MenuItemModifierGroup menuItemModifierGroup : menuItemModiferGroups) {
							MenuItemModifierGroup menuItemModifierGroup2 = (MenuItemModifierGroup) objectMap.get(menuItemModifierGroup.getUniqueId());
							menuItemModifierGroup.setId(menuItemModifierGroup2.getId());
							menuItemModifierGroup.setModifierGroup(menuItemModifierGroup2.getModifierGroup());
						}
					}

					MenuItemDAO.getInstance().saveOrUpdate(menuItem, session);
				}
			}

			transaction.commit();
		} catch (Exception e1) {

			if (transaction != null)
				transaction.rollback();
			throw e1;

		} finally {
			dao.closeSession(session);
			IOUtils.closeQuietly(inputStream);
		}
	}
}
