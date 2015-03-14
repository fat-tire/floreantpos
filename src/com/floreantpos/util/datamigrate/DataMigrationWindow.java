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
		titlePanel.setTitle("Migrate data");

		getContentPane().add(titlePanel, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);

		JPanel centerPanel = new JPanel();
		getContentPane().add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new GridLayout(1, 0, 10, 10));

		sourceDbPanel.setBorder(BorderFactory.createTitledBorder("Source"));
		destDbPanel.setBorder(BorderFactory.createTitledBorder("Destination"));

		centerPanel.add(sourceDbPanel);
		centerPanel.add(destDbPanel);

		JButton btnMigrate = new JButton("Migrate");
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

		sourceConfiguration = sourceConfiguration.setProperty("hibernate.dialect", sourceDatabase.getHibernateDialect());
		sourceConfiguration = sourceConfiguration.setProperty("hibernate.connection.driver_class", sourceDatabase.getHibernateConnectionDriverClass());

		sourceConfiguration = sourceConfiguration.setProperty("hibernate.connection.url", sourceConnectString);
		sourceConfiguration = sourceConfiguration.setProperty("hibernate.connection.username", sourceDbPanel.getUser());
		sourceConfiguration = sourceConfiguration.setProperty("hibernate.connection.password", sourceDbPanel.getPass());
		
		Configuration destConfiguration = _RootDAO.getNewConfiguration(null);

		destConfiguration = destConfiguration.setProperty("hibernate.dialect", destDatabase.getHibernateDialect());
		destConfiguration = destConfiguration.setProperty("hibernate.connection.driver_class", destDatabase.getHibernateConnectionDriverClass());

		destConfiguration = destConfiguration.setProperty("hibernate.connection.url", destConnectString);
		destConfiguration = destConfiguration.setProperty("hibernate.connection.username", destDbPanel.getUser());
		destConfiguration = destConfiguration.setProperty("hibernate.connection.password", destDbPanel.getPass());
		
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
			
			System.out.println(menuCategory);
			
			MenuCategoryDAO.getInstance().save(m, destSession);
		}
		
		transaction.commit();
		destSession.close();
		
		System.out.println("done");
	}



	public static void main(String[] args) {
		DataMigrationWindow window = new DataMigrationWindow();
		window.setSize(800, 600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

}
