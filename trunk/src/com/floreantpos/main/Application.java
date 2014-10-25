package com.floreantpos.main;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.AppProperties;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.config.ui.DatabaseConfigurationDialog;
import com.floreantpos.model.PrinterConfiguration;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Shift;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.dao.PrinterConfigurationDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TerminalDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.LoginScreen;
import com.floreantpos.ui.views.order.RootView;
import com.floreantpos.util.DatabaseConnectionException;
import com.floreantpos.util.DatabaseUtil;
import com.floreantpos.util.POSUtil;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.ExperienceBlue;

public class Application {
	private static Log logger = LogFactory.getLog(Application.class);
	
	private boolean developmentMode = false;

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
		pluginManager.addPluginsFrom(new File("plugins/").toURI());
		
		if(developmentMode) {
			pluginManager.addPluginsFrom(new File("/home/mshahriar/project/oro/target/classes").toURI());
		}

		setApplicationLook();

		rootView = RootView.getInstance();

		posWindow.getContentPane().add(rootView);
		posWindow.setupSizeAndLocation();
		
		if(TerminalConfig.isFullscreenMode()) {
			posWindow.enterFullScreenMode();
		}
		
		posWindow.setVisible(true);

		initializeSystem();
	}

	private void setApplicationLook() {
		try {
			PlasticXPLookAndFeel.setPlasticTheme(new ExperienceBlue());
			UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
			//UIManager.setLookAndFeel(new NimbusLookAndFeel());
			UIManager.put("ComboBox.is3DEnabled", Boolean.FALSE); //$NON-NLS-1$
		} catch (Exception ignored) {
		}
	}

	public void initializeSystem() {
		if (isSystemInitialized()) {
			return;
		}

		try {

			posWindow.setGlassPaneVisible(true);
			//posWindow.setGlassPaneMessage(com.floreantpos.POSConstants.LOADING);

			DatabaseUtil.checkConnection(DatabaseUtil.initialize());

			initTerminal();
			initPrintConfig();
			refreshRestaurant();
			//setTicketActiveSetterScheduler();
			setSystemInitialized(true);

		} catch (DatabaseConnectionException e) {
			e.printStackTrace();
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));

			if (writer.toString().contains("Another instance of Derby may have already booted")) {
				POSMessageDialog.showError("Another FloreantPOS instance may be already running.\n" + "Multiple instances cannot be run in Derby single mode");

				return;
			}
			else {
				int option = JOptionPane.showConfirmDialog(getPosWindow(),
						Messages.getString("Application.0"), Messages.getString(POSConstants.POS_MESSAGE_ERROR), JOptionPane.YES_NO_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
				if (option == JOptionPane.YES_OPTION) {
					DatabaseConfigurationDialog.show(Application.getPosWindow());
				}
			}
		} catch (Exception e) {
			POSMessageDialog.showError(e.getMessage(), e);
			e.printStackTrace();
			logger.error(e);
		} finally {
			getPosWindow().setGlassPaneVisible(false);
		}
	}

//	private void setTicketActiveSetterScheduler() {
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
//		calendar.set(Calendar.HOUR_OF_DAY, 0);
//		calendar.set(Calendar.MINUTE, 0);
//		calendar.set(Calendar.SECOND, 0);
//
//	}

	private void initPrintConfig() {
		printConfiguration = PrinterConfigurationDAO.getInstance().get(PrinterConfiguration.ID);
		if (printConfiguration == null) {
			printConfiguration = new PrinterConfiguration();
		}
	}

	private void initTerminal() {
		int terminalId = TerminalConfig.getTerminalId();

		if (terminalId == -1) {
			Random random = new Random();
			terminalId = random.nextInt(10000) + 1;
		}

		Terminal terminal = null;
		try {
			terminal = TerminalDAO.getInstance().get(new Integer(terminalId));
			if (terminal == null) {
				terminal = new Terminal();
				terminal.setId(terminalId);
				terminal.setOpeningBalance(new Double(500));
				terminal.setCurrentBalance(new Double(500));
				terminal.setName(String.valueOf(terminalId)); //$NON-NLS-1$

				TerminalDAO.getInstance().saveOrUpdate(terminal);
			}
		} catch (Exception e) {
			throw new DatabaseConnectionException();
		}

		TerminalConfig.setTerminalId(terminalId);
		RootView.getInstance().getLoginScreen().setTerminalId(terminalId);

		this.terminal = terminal;
	}

	public void refreshRestaurant() {
		try {
			this.restaurant = RestaurantDAO.getRestaurant();

			if (restaurant.getUniqueId() == null || restaurant.getUniqueId() == 0) {
				restaurant.setUniqueId(RandomUtils.nextInt());
				RestaurantDAO.getInstance().saveOrUpdate(restaurant);
			}

			if (restaurant.isAutoDrawerPullEnable() && autoDrawerPullTimer == null) {
				autoDrawerPullTimer = new Timer(60 * 1000, new AutoDrawerPullAction());
				autoDrawerPullTimer.start();
			}
			else {
				if (autoDrawerPullTimer != null) {
					autoDrawerPullTimer.stop();
					autoDrawerPullTimer = null;
				}
			}

			if (restaurant.isItemPriceIncludesTax()) {
				posWindow.setStatus("Tax is included in item price");
			}
			else {
				posWindow.setStatus("Tax is not included in item price");
			}
		} catch (Exception e) {
			throw new DatabaseConnectionException();
		}
	}

	public static String getCurrencyName() {
		Application application = getInstance();
		if (application.restaurant == null) {
			application.refreshRestaurant();
		}
		return application.restaurant.getCurrencyName();
	}

	public static String getCurrencySymbol() {
		Application application = getInstance();
		if (application.restaurant == null) {
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
		User user = getCurrentUser();
		
		if(user != null && !user.hasPermission(UserPermission.SHUT_DOWN)) {
			POSMessageDialog.showError("You do not have permission to execute this action");
			return;
		}
		
		int option = JOptionPane.showOptionDialog(getPosWindow(), com.floreantpos.POSConstants.SURE_SHUTDOWN_, com.floreantpos.POSConstants.CONFIRM_SHUTDOWN,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (option != JOptionPane.YES_OPTION) {
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
		if (enable) {
			if (autoDrawerPullTimer != null) {
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

	public boolean isDevelopmentMode() {
		return developmentMode;
	}

	public void setDevelopmentMode(boolean developmentMode) {
		this.developmentMode = developmentMode;
	}
	
	public boolean isPriceIncludesTax() {
		Restaurant restaurant = getRestaurant();
		if(restaurant == null) {
			return false;
		}
		
		return POSUtil.getBoolean(restaurant.isItemPriceIncludesTax());
	}
}
