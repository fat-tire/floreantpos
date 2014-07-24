package com.floreantpos.main;

import org.apache.commons.cli.*;


public class Main {

	private static final String DEVELOPMENT_MODE = "developmentMode";

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Options options = new Options();
		options.addOption(DEVELOPMENT_MODE, true, "State if this is developmentMode");
		CommandLineParser parser = new BasicParser();
		CommandLine commandLine = parser.parse(options, args);
		String optionValue = commandLine.getOptionValue(DEVELOPMENT_MODE);
		
		Application application = Application.getInstance();
		
		if(optionValue != null) {
			application.setDevelopmentMode(Boolean.valueOf(optionValue));
		}
		
		application.start();
	}

}
