package com.floreantpos.extension;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.util.PluginManagerUtil;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.main.Application;
import com.floreantpos.util.JarUtil;

public class ExtensionManager {
	private List<FloreantPlugin> plugins = new ArrayList<FloreantPlugin>();
	public PluginManager pluginManager;
	
	private static ExtensionManager instance;
	
	public void initialize() {
		pluginManager = PluginManagerFactory.createPluginManager();

		String jarLocation = JarUtil.getExecutableJarLocation(Application.class);
		URI uri = new File(jarLocation).toURI();
		pluginManager.addPluginsFrom(uri);

		String pluginsPath = System.getProperty("pluginsPath");

		if (StringUtils.isNotEmpty(pluginsPath)) {
			pluginManager.addPluginsFrom(new File(pluginsPath).toURI());
		}
		else {
			pluginManager.addPluginsFrom(new File("plugins/").toURI()); //$NON-NLS-1$
		}

		PluginManagerUtil pmUtil = new PluginManagerUtil(pluginManager);
		List<Plugin> plugins = (List<Plugin>) pmUtil.getPlugins();
		
		//sort plugins
		java.util.Collections.sort(plugins, new Comparator<Plugin>() {
			@Override
			public int compare(Plugin o1, Plugin o2) {
				return o1.getClass().getName().compareToIgnoreCase(o2.getClass().getName());
			}
		});
		
		for (Plugin plugin : plugins) {
			if(plugin instanceof FloreantPlugin) {
				this.plugins.add((FloreantPlugin) plugin);
			}
		}
	}
	
	public List<FloreantPlugin> getPlugins() {
		return plugins;
	}
	
//	public static List<FloreantPlugin> getPlugins(Class pluginClass) {
//		List<FloreantPlugin> list = new ArrayList<FloreantPlugin>();
//		
//		for (FloreantPlugin floreantPlugin : ExtensionManager.plugins) {
//			if(floreantPlugin instanceof pluginClass)
//		}
//	}
	
	public static ExtensionManager getInstance() {
		if(instance == null) {
			instance = new ExtensionManager();
			instance.initialize();
		}
		
		return instance;
	}
}
