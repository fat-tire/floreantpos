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
package com.floreantpos.main;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;

import com.floreantpos.config.TerminalConfig;

public class Main {

	private static final String DEVELOPMENT_MODE = "developmentMode"; //$NON-NLS-1$

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Options options = new Options();
		options.addOption(DEVELOPMENT_MODE, true, "State if this is developmentMode"); //$NON-NLS-1$
		CommandLineParser parser = new BasicParser();
		CommandLine commandLine = parser.parse(options, args);
		String optionValue = commandLine.getOptionValue(DEVELOPMENT_MODE);
		Locale defaultLocale = TerminalConfig.getDefaultLocale();
		if (defaultLocale != null) {
			Locale.setDefault(defaultLocale);
		}

		Application application = Application.getInstance();

		if (optionValue != null) {
			application.setDevelopmentMode(Boolean.valueOf(optionValue));
		}

		application.start();
	}

	public static void restart() throws IOException, InterruptedException, URISyntaxException {
		final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		//		Properties properties = System.getProperties();
		//		Set<Object> keySet = properties.keySet();
		//		for (Object object : keySet) {
		//			PosLog.debug(getClass(),object + ":"+properties.getProperty((String) object));
		//		}

		String classPath = System.getProperty("java.class.path"); //$NON-NLS-1$
		String mainClass = System.getProperty("sun.java.command"); //$NON-NLS-1$

		/* Build command: java -jar application.jar */
		final ArrayList<String> command = new ArrayList<String>();
		command.add(javaBin);
		command.add("-cp"); //$NON-NLS-1$
		command.add(classPath);
		command.add(mainClass);

		final ProcessBuilder builder = new ProcessBuilder(command);
		builder.start();
		System.exit(0);
	}
}
