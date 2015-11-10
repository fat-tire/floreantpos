package com.floreantpos.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class JarUtil {
	/**
	 * This will return the directory location of the executable jar. For this to work correctly,
	 * main class of the executable jar should be provided. Any class that is in the executable jar
	 * should also work, but that is not recommended. 
	 * 
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getJarLocation(Class clazz) {
		String executableLocation;
		
		try {
			
			URI uri = clazz.getProtectionDomain().getCodeSource().getLocation().toURI();
			if (uri.toString().endsWith(".jar")) {
				return new File(uri.getPath()).getParentFile().getPath() + "/";
			}
			return uri.getPath();
		
		} catch (URISyntaxException e) {
			e.printStackTrace();
			executableLocation = ".";
		}
		return executableLocation + "/";
	}
}
