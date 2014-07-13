package com.floreantpos.main;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.AppConfig;
import com.floreantpos.config.AppProperties;
import com.floreantpos.config.ui.DatabaseConfigurationDialog;
import com.floreantpos.model.PrinterConfiguration;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Shift;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.PrinterConfigurationDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TerminalDAO;
import com.floreantpos.ui.views.LoginScreen;
import com.floreantpos.ui.views.order.RootView;
import com.floreantpos.util.DatabaseConnectionException;
import com.floreantpos.util.DatabaseUtil;
import com.floreantpos.util.TicketActiveDateSetterTask;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.ExperienceBlue;

public class Application {
	private static Log logger = LogFactory.getLog(Application.class);

	private Timer autoDrawerPullTimer;
	
	private PluginManager pluginManager;

	private Terminal terminal;
	private PosWindow posWindow;
	private User currentUser;
	private RootView rootView;
	private BackOfficeWindow backOfficeWindow;
	private Shift currentShift;
	public PrinterConfiguration printConfiguration;
	private Restaurant restaurant;

	private static Application instance;

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy"); //$NON-NLS-1$
	private static ImageIcon applicationIcon;
	
	private boolean systemInitialized;
	private TicketActiveDateSetterTask ticketActiveDateSetterTask;

	public final static String VERSION = AppProperties.getVersion();

	private Application() {
		//Locale.setDefault(Locale.forLanguageTag("ar-EG"));

		applicationIcon = new ImageIcon(getClass().getResource("/icons/icon.png")); //$NON-NLS-1$
		posWindow = new PosWindow();
		posWindow.setTitle(getTitle());
		posWindow.setIconImage(applicationIcon.getImage());
	}

	public void start() {
		pluginManager = PluginManagerFactory.createPluginManager();
		//pluginManager.addPluginsFrom(ClassURI.CLASSPATH);
		pluginManager.addPluginsFrom(new File("plugins/").toURI());
		//pluginManager.addPluginsFrom(new File("/home/mshahriar/project/oro/target/classes").toURI());
		
		setApplicationLook();

		rootView = RootView.getInstance();

		posWindow.setContentPane(rootView);
		posWindow.setupSizeAndLocation();
		posWindow.setVisible(true);
		
		initializeSystem();
	}

	private void setApplicationLook() {
		try {
			PlasticXPLookAndFeel.setPlasticTheme(new ExperienceBlue());
			UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
			UIManager.put("ComboBox.is3DEnabled", Boolean.FALSE); //$NON-NLS-1$
		} catch (Exception ignored) {
		}
	}
	
	public void initializeSystem() {
		if(isSystemInitialized()) {
			return;
		}
		
		try {
			
			posWindow.setGlassPaneVisible(true);
			posWindow.setGlassPaneMessage(com.floreantpos.POSConstants.LOADING);
			
			if(!DatabaseUtil.checkConnection(DatabaseUtil.initialize())) {
				int option = JOptionPane.showConfirmDialog(getPosWindow(), Messages.getString("Application.0"), Messages.getString(POSConstants.POS_MESSAGE_ERROR), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
				if(option == JOptionPane.YES_OPTION) {
					DatabaseConfigurationDialog.show(Application.getPosWindow());
				}
				
				return;
			}

			initTerminal();
			initPrintConfig();
			refreshRestaurant();
			setTicketActiveSetterScheduler();
			setSystemInitialized(true);
			
		} 
		catch (DatabaseConnectionException e) {
			int option = JOptionPane.showConfirmDialog(getPosWindow(), Messages.getString("Application.0"), Messages.getString(POSConstants.POS_MESSAGE_ERROR), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
			if(option == JOptionPane.YES_OPTION) {
				DatabaseConfigurationDialog.show(Application.getPosWindow());
			}
		}
		catch (Exception e) {
			logger.error(e);
		} finally {
			getPosWindow().setGlassPaneVisible(false);
		}
	}

	private void setTicketActiveSetterScheduler() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		Date time = calendar.getTime();

		cancelTicketActiveTaskScheduler();
		
		ticketActiveDateSetterTask = new TicketActiveDateSetterTask();
		ticketActiveDateSetterTask.run();

		java.util.Timer activeDateScheduler = new java.util.Timer();
		activeDateScheduler.scheduleAtFixedRate(ticketActiveDateSetterTask, time, 86400*1000);
	}

	public void cancelTicketActiveTaskScheduler() {
		if(this.ticketActiveDateSetterTask != null) {
			this.ticketActiveDateSetterTask.cancel();
		}
	}

	private void initPrintConfig() {
		printConfiguration = PrinterConfigurationDAO.getInstance().get(PrinterConfiguration.ID);
		if(printConfiguration == null) {
			printConfiguration = new PrinterConfiguration();
		}
	}

	private void initTerminal() {
		int terminalId = AppConfig.getTerminalId();
		
		if (terminalId == -1) {
//			NumberSelectionDialog2 dialog = new NumberSelectionDialog2();
//			dialog.setTitle(com.floreantpos.POSConstants.ENTER_ID_FOR_TERMINAL);
//			dialog.pack();
//			dialog.setLocationRelativeTo(getPosWindow());
//			dialog.setVisible(true);
//			
//			terminalId = (int) dialog.getValue();
			
			Random random = new Random();
			terminalId = random.nextInt(10000) + 1;
		}

		Terminal terminal = TerminalDAO.getInstance().get(new Integer(terminalId));
		if (terminal == null) {
			
			terminal = new Terminal();
			terminal.setId(terminalId);
			terminal.setOpeningBalance(new Double(500));
			terminal.setCurrentBalance(new Double(500));
			terminal.setName(com.floreantpos.POSConstants.TERMINAL + " - " + terminalId); //$NON-NLS-1$
			
			TerminalDAO.getInstance().saveOrUpdate(terminal);
		}
		
		AppConfig.setTerminalId(terminalId);
		RootView.getInstance().getLoginScreen().setTerminalId(terminalId);
		
		this.terminal = terminal;
	}

	public void refreshRestaurant() {
		RestaurantDAO restaurantDAO = RestaurantDAO.getInstance();
		this.restaurant = restaurantDAO.get(Integer.valueOf(1));
		if(restaurant.isAutoDrawerPullEnable() && autoDrawerPullTimer == null) {
			autoDrawerPullTimer = new Timer(60 * 1000, new AutoDrawerPullAction());
			autoDrawerPullTimer.start();
		}
		else {
			if(autoDrawerPullTimer != null) {
				autoDrawerPullTimer.stop();
				autoDrawerPullTimer = null;
			}
		}
	}

	public static String getCurrencyName() {
		Application application = getInstance();
		if(application.restaurant == null) {
			application.refreshRestaurant();
		}
		return application.restaurant.getCurrencyName();
	}

	public static String getCurrencySymbol() {
		Application application = getInstance();
		if(application.restaurant == null) {
			application.refreshRestaurant();
		}
		return application.restaurant.getCurrencySymbol();
	}

	public synchronized static Application getInstance() {
		if (instance == null) {
			instance = new Application();

		}
		return instance;
	}

	public void shutdownPOS() {
		int option = JOptionPane.showOptionDialog(getPosWindow(), com.floreantpos.POSConstants.SURE_SHUTDOWN_, com.floreantpos.POSConstants.CONFIRM_SHUTDOWN, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if(option != JOptionPane.YES_OPTION) {
			return;
		}

		posWindow.saveSizeAndLocation();
		
		System.exit(0);
	}

	public void logout() {
		if (backOfficeWindow != null) {
			backOfficeWindow.setVisible(false);
			backOfficeWindow = null;
			currentShift = null;
		}

		setCurrentUser(null);
		RootView.getInstance().showView(LoginScreen.VIEW_NAME);
	}

	public static User getCurrentUser() {
		return getInstance().currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public RootView getRootView() {
		return rootView;
	}

	public void setRootView(RootView rootView) {
		this.rootView = rootView;
	}

	public static PosWindow getPosWindow() {
		return getInstance().posWindow;
	}

//	public BackOfficeWindow getBackOfficeWindow() {
//		return backOfficeWindow;
//	}

	public void setBackOfficeWindow(BackOfficeWindow backOfficeWindow) {
		this.backOfficeWindow = backOfficeWindow;
	}

	public Terminal getTerminal() {

		TerminalDAO.getInstance().refresh(terminal);

		return terminal;
	}

//	public static PrinterConfiguration getPrinterConfiguration() {
//		return getInstance().printConfiguration;
//	}

	public static String getTitle() {
		return "Floreant POS - Version " + VERSION; //$NON-NLS-1$
	}

	public static ImageIcon getApplicationIcon() {
		return applicationIcon;
	}

	public static void setApplicationIcon(ImageIcon applicationIcon) {
		Application.applicationIcon = applicationIcon;
	}

	public static String formatDate(Date date) {
		return dateFormat.format(date);
	}

	public Shift getCurrentShift() {
		return currentShift;
	}

	public void setCurrentShift(Shift currentShift) {
		this.currentShift = currentShift;
	}

	public void setAutoDrawerPullEnable(boolean enable) {
		if(enable) {
			if(autoDrawerPullTimer != null) {
				return;
			}
			else {
				autoDrawerPullTimer = new Timer(60 * 1000, new AutoDrawerPullAction());
				autoDrawerPullTimer.start();
			}
		}
		else {
			autoDrawerPullTimer.stop();
			autoDrawerPullTimer = null;
		}
	}

	public boolean isSystemInitialized() {
		return systemInitialized;
	}

	public void setSystemInitialized(boolean systemInitialized) {
		this.systemInitialized = systemInitialized;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}
	
	public static PluginManager getPluginManager() {
		return getInstance().pluginManager;
	}
	
	public static File getWorkingDir() {
		File file = new File(Application.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		
		return file.getParentFile();
	}
}
