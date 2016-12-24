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
package com.floreantpos.util.datamigrate;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

import com.floreantpos.Database;
import com.floreantpos.Messages;
import com.floreantpos.PosLog;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao._RootDAO;
import com.floreantpos.ui.TitlePanel;

public class DataMigrationWindow extends JFrame {

	private Database sourceDatabase;
	private Database destDatabase;

	private DbPanel sourceDbPanel = new DbPanel();
	private DbPanel destDbPanel = new DbPanel();
	private String sourceConnectString;
	private String destConnectString;

	public DataMigrationWindow() {

		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle(Messages.getString("DataMigrationWindow.0")); //$NON-NLS-1$

		getContentPane().add(titlePanel, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);

		JPanel centerPanel = new JPanel();
		getContentPane().add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new GridLayout(1, 0, 10, 10));

		sourceDbPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("DataMigrationWindow.1"))); //$NON-NLS-1$
		destDbPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("DataMigrationWindow.2"))); //$NON-NLS-1$

		centerPanel.add(sourceDbPanel);
		centerPanel.add(destDbPanel);

		JButton btnMigrate = new JButton(Messages.getString("DataMigrationWindow.3")); //$NON-NLS-1$
		btnMigrate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				migrate();
			}
		});
		panel.add(btnMigrate);
	}

	

	protected void migrate() {
		sourceDatabase = sourceDbPanel.getDatabase();
		destDatabase = destDbPanel.getDatabase();
		
		sourceConnectString = sourceDbPanel.getConnectString();
		destConnectString = destDbPanel.getConnectString();
		
		Configuration sourceConfiguration = _RootDAO.getNewConfiguration(null);

		sourceConfiguration = sourceConfiguration.setProperty("hibernate.dialect", sourceDatabase.getHibernateDialect()); //$NON-NLS-1$
		sourceConfiguration = sourceConfiguration.setProperty("hibernate.connection.driver_class", sourceDatabase.getHibernateConnectionDriverClass()); //$NON-NLS-1$

		sourceConfiguration = sourceConfiguration.setProperty("hibernate.connection.url", sourceConnectString); //$NON-NLS-1$
		sourceConfiguration = sourceConfiguration.setProperty("hibernate.connection.username", sourceDbPanel.getUser()); //$NON-NLS-1$
		sourceConfiguration = sourceConfiguration.setProperty("hibernate.connection.password", sourceDbPanel.getPass()); //$NON-NLS-1$
		
		Configuration destConfiguration = _RootDAO.getNewConfiguration(null);

		destConfiguration = destConfiguration.setProperty("hibernate.dialect", destDatabase.getHibernateDialect()); //$NON-NLS-1$
		destConfiguration = destConfiguration.setProperty("hibernate.connection.driver_class", destDatabase.getHibernateConnectionDriverClass()); //$NON-NLS-1$

		destConfiguration = destConfiguration.setProperty("hibernate.connection.url", destConnectString); //$NON-NLS-1$
		destConfiguration = destConfiguration.setProperty("hibernate.connection.username", destDbPanel.getUser()); //$NON-NLS-1$
		destConfiguration = destConfiguration.setProperty("hibernate.connection.password", destDbPanel.getPass()); //$NON-NLS-1$
		
		SessionFactory sourceSessionFactory = sourceConfiguration.buildSessionFactory();
		SessionFactory destSessionFactory = destConfiguration.buildSessionFactory();
		
		Session sourceSession = sourceSessionFactory.openSession();
		Session destSession = destSessionFactory.openSession();
		
		Transaction transaction = destSession.beginTransaction();
		List<MenuCategory> categories = MenuCategoryDAO.getInstance().findAll(sourceSession);
		for (MenuCategory menuCategory : categories) {
			MenuCategory m = new MenuCategory();
			m.setName(menuCategory.getName());
			m.setTranslatedName(menuCategory.getTranslatedName());
			m.setBeverage(menuCategory.isBeverage());
			m.setVisible(menuCategory.isVisible());
			
			PosLog.info(DataMigrationWindow.class, "" + menuCategory);
			
			MenuCategoryDAO.getInstance().save(m, destSession);
		}
		
		transaction.commit();
		destSession.close();
		
		PosLog.info(getClass(),"done"); //$NON-NLS-1$
	}



	public static void main(String[] args) {
		DataMigrationWindow window = new DataMigrationWindow();
		window.setSize(800, 600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

}
