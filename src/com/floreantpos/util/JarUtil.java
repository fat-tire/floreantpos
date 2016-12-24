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
package com.floreantpos.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import com.floreantpos.PosLog;

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
			PosLog.error(JarUtil.class, e.getMessage());
			executableLocation = ".";
		}
		return executableLocation + "/";
	}
}
