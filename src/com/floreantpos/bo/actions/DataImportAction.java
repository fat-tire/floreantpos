package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
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
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.model.Tax;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
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
		if(option != JFileChooser.APPROVE_OPTION) {
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
		Session session = null;
		Transaction transaction = null;
		FileReader reader = null;
		GenericDAO dao = new GenericDAO();
		
		Map<String, Object> objectMap = new HashMap<String, Object>();
		
		try {
			
			if(file == null) return;
			
			reader = new FileReader(file);
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Elements.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			Elements elements = (Elements) unmarshaller.unmarshal(reader);
			
			session = dao.createNewSession();
			transaction = session.beginTransaction();
			
			List<Tax> taxes = elements.getTaxes();
			for (Tax tax : taxes) {
				objectMap.put(tax.toString() + "_" + tax.getId(), tax);
				tax.setId(null);
				
				TaxDAO.getInstance().save(tax, session);
			}
			
			List<MenuCategory> menuCategories = elements.getMenuCategories();
			for (MenuCategory menuCategory : menuCategories) {
				
				objectMap.put(menuCategory.toString() + "_" + menuCategory.getId(), menuCategory);
				menuCategory.setId(null);
				
				MenuCategoryDAO.getInstance().save(menuCategory, session);
			}
			
			List<MenuGroup> menuGroups = elements.getMenuGroups();
			for (MenuGroup menuGroup : menuGroups) {
				
				MenuCategory menuCategory = menuGroup.getParent();
				if(menuCategory != null) {
					menuCategory = (MenuCategory) objectMap.get(menuCategory.toString() + "_" + menuCategory.getId());
					menuGroup.setParent(menuCategory);
				}
				
				objectMap.put(menuGroup.toString() + "_" + menuGroup.getId(), menuGroup);
				menuGroup.setId(null);
				
				MenuGroupDAO.getInstance().saveOrUpdate(menuGroup, session);
			}
			
			List<MenuModifierGroup> menuModifierGroups = elements.getMenuModifierGroups();
			for (MenuModifierGroup menuModifierGroup : menuModifierGroups) {
				objectMap.put(menuModifierGroup.toString() + "_" + menuModifierGroup.getId(), menuModifierGroup);
				menuModifierGroup.setId(null);
				
				MenuModifierGroupDAO.getInstance().saveOrUpdate(menuModifierGroup, session);
			}
			
			List<MenuModifier> menuModifiers = elements.getMenuModifiers();
			for (MenuModifier menuModifier : menuModifiers) {
				
				objectMap.put(menuModifier.toString() + "_" + menuModifier.getId(), menuModifier);
				menuModifier.setId(null);
				
				MenuModifierGroup menuModifierGroup = menuModifier.getModifierGroup();
				if(menuModifierGroup != null) {
					menuModifierGroup = (MenuModifierGroup) objectMap.get(menuModifierGroup.toString() + "_" + menuModifierGroup.getId());
					menuModifier.setModifierGroup(menuModifierGroup);
				}
				
				Tax tax = menuModifier.getTax();
				if(tax != null) {
					tax = (Tax) objectMap.get(tax.toString() + "_" + tax.getId());
					menuModifier.setTax(tax);
				}
				
				MenuModifierDAO.getInstance().saveOrUpdate(menuModifier, session);
			}
			
			List<MenuItem> menuItems = elements.getMenuItems();
			for (MenuItem menuItem : menuItems) {
				
				objectMap.put(menuItem.toString() + "_" + menuItem.getId(), menuItem);
				menuItem.setId(null);
				
				MenuGroup menuGroup = menuItem.getParent();
				if(menuGroup != null) {
					menuGroup = (MenuGroup) objectMap.get(menuGroup.toString() + "_" + menuGroup.getId());
					menuItem.setParent(menuGroup);
				}
				
				Tax tax = menuItem.getTax();
				if(tax != null) {
					tax = (Tax) objectMap.get(tax.toString() + "_" + tax.getId());
					menuItem.setTax(tax);
				}
				
				MenuItemDAO.getInstance().saveOrUpdate(menuItem, session);
			}
			
			transaction.commit();
		} catch (Exception e1) {
			
			if(transaction != null) transaction.rollback();
			throw e1;
			
		} finally {
			dao.closeSession(session);
			IOUtils.closeQuietly(reader);
		}
	}
}
