package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.util.datamigrate.Elements;

public class DataImportAction extends AbstractAction {

	public DataImportAction() {
		super("Import Data");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Session session = null;
		Transaction transaction = null;
		
		try {
			JFileChooser fileChooser = new JFileChooser();
			int option = fileChooser.showOpenDialog(BackOfficeWindow.getInstance());
			if(option != JFileChooser.APPROVE_OPTION) {
				return;
			}
			
			File file = fileChooser.getSelectedFile();
			FileReader reader = new FileReader(file);
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Elements.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			Elements elements = (Elements) unmarshaller.unmarshal(reader);
			
			GenericDAO dao = new GenericDAO();
			session = dao.createNewSession();
			transaction = session.beginTransaction();
			
			List<MenuItem> menuItems = elements.getMenuItems();
			for (MenuItem menuItem : menuItems) {
				MenuItemDAO.getInstance().saveOrUpdate(menuItem, session);
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if(transaction != null) transaction.commit();
			if(session != null) session.close();
		}
	}
}
