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
package com.floreantpos.extension;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
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
	private List<FloreantPlugin> plugins;

	private static ExtensionManager instance;

	private synchronized void initialize() {
		PluginManager pluginManager = PluginManagerFactory.createPluginManager();

		String jarLocation = JarUtil.getJarLocation(Application.class);
		URI uri = new File(jarLocation).toURI();
		pluginManager.addPluginsFrom(uri);

		String pluginsPath = System.getProperty("pluginsPath"); //$NON-NLS-1$

		if (StringUtils.isNotEmpty(pluginsPath)) {
			String[] paths = pluginsPath.split(","); //$NON-NLS-1$
			for (String string : paths) {
				pluginManager.addPluginsFrom(new File(string).toURI());
			}
		}
		else {
			pluginManager.addPluginsFrom(new File("plugins/").toURI()); //$NON-NLS-1$
		}

		PluginManagerUtil pmUtil = new PluginManagerUtil(pluginManager);
		List<Plugin> allPlugins = (List<Plugin>) pmUtil.getPlugins();

		//sort plugins
		java.util.Collections.sort(allPlugins, new Comparator<Plugin>() {
			@Override
			public int compare(Plugin o1, Plugin o2) {
				return o1.getClass().getName().compareToIgnoreCase(o2.getClass().getName());
			}
		});

		List<FloreantPlugin> floreantPlugins = new ArrayList<FloreantPlugin>();

		for (Plugin plugin : allPlugins) {
			if (plugin instanceof FloreantPlugin) {
				FloreantPlugin floreantPlugin = (FloreantPlugin) plugin;
				if (floreantPlugin.requireLicense()) {
					floreantPlugin.initLicense();
					if (floreantPlugin.hasValidLicense()) {
						floreantPlugins.add(floreantPlugin);
					}
				}
				else {
					floreantPlugins.add(floreantPlugin);
				}
			}
		}

		this.plugins = Collections.unmodifiableList(floreantPlugins);
	}

	public static List<FloreantPlugin> getPlugins() {
		return getInstance().plugins;
	}

	public static List<FloreantPlugin> getPlugins(Class pluginClass) {
		List<FloreantPlugin> list = new ArrayList<FloreantPlugin>();

		for (FloreantPlugin floreantPlugin : getInstance().plugins) {
			if (pluginClass.isAssignableFrom(floreantPlugin.getClass())) {
				list.add(floreantPlugin);
			}
		}

		return list;
	}

	public static FloreantPlugin getPlugin(Class pluginClass) {
		for (FloreantPlugin floreantPlugin : getInstance().plugins) {
			if (pluginClass.isAssignableFrom(floreantPlugin.getClass())) {
				return floreantPlugin;
			}
		}

		return null;
	}

	public synchronized static ExtensionManager getInstance() {
		if (instance == null) {
			instance = new ExtensionManager();
			instance.initialize();
		}

		return instance;
	}
}
