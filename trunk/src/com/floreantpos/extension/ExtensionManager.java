package com.floreantpos.extension;

import java.io.File;
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
	private static List<FloreantPlugin> plugins = new ArrayList<FloreantPlugin>();
	
	public static void initialize() {
		PluginManager pluginManager = PluginManagerFactory.createPluginManager();

		String jarLocation = JarUtil.getExecutableJarLocation(Application.class);
		pluginManager.addPluginsFrom(new File(jarLocation).toURI());

		String pluginsPath = System.getProperty("pluginsPath");

		if (StringUtils.isNotEmpty(pluginsPath)) {
			pluginManager.addPluginsFrom(new File(pluginsPath).toURI());
		}
		else {
			pluginManager.addPluginsFrom(new File("plugins/").toURI()); //$NON-NLS-1$
		}

		PluginManagerUtil pmUtil = new PluginManagerUtil(Application.getPluginManager());
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
				ExtensionManager.plugins.add((FloreantPlugin) plugin);
			}
		}
	}
	
	public static List<FloreantPlugin> getPlugins() {
		return plugins;
	}
	
//	public static List<FloreantPlugin> getPlugins(Class pluginClass) {
//		List<FloreantPlugin> list = new ArrayList<FloreantPlugin>();
//		
//		for (FloreantPlugin floreantPlugin : ExtensionManager.plugins) {
//			if(floreantPlugin instanceof pluginClass)
//		}
//	}
}
