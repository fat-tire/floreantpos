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
package com.floreantpos.swing;

import java.awt.Dimension;

import com.floreantpos.config.TerminalConfig;

public class PosUIManager {

	private static final int DEFAULT_FONT_SIZE = 12;
	private static final int TITLE_FONT_SIZE = 18;
	private static final int LARGE_FONT_SIZE = 16;
	private static final int NUMBER_FIELD_FONT_SIZE = 24;
	private static final int TABLE_NUMBER_FONT_SIZE = 30;

	private static final double SCREEN_SCALE_FACTOR = TerminalConfig.getScreenScaleFactor();

	public static Dimension getSize(int w, int h) {
		int width = (int) (w * SCREEN_SCALE_FACTOR);
		int height = (int) (h * SCREEN_SCALE_FACTOR);
		return new Dimension(width, height);
	}

	public static int getSize(int size) {
		return (int) (size * SCREEN_SCALE_FACTOR);
	}

	public static int getFontSize(int fontSize) {
		return (int) (fontSize * SCREEN_SCALE_FACTOR);
	}

	public static Dimension getSize_w100_h70() {
		int width = (int) (100 * SCREEN_SCALE_FACTOR);
		int height = (int) (70 * SCREEN_SCALE_FACTOR);
		return new Dimension(width, height);
	}
	
	public static Dimension getSize_w120_h70() {
		int width = (int) (120 * SCREEN_SCALE_FACTOR);
		int height = (int) (70 * SCREEN_SCALE_FACTOR);
		return new Dimension(width, height);
	}

	public static double getScreenScaleFactor() {
		return SCREEN_SCALE_FACTOR;
	}

	public static int getDefaultFontSize() {
		return (int) (DEFAULT_FONT_SIZE * SCREEN_SCALE_FACTOR);
	}

	public static int getTitleFontSize() {
		return (int) (TITLE_FONT_SIZE * SCREEN_SCALE_FACTOR);
	}

	public static int getLargeFontSize() {
		return (int) (LARGE_FONT_SIZE * SCREEN_SCALE_FACTOR);
	}

	public static int getNumberFieldFontSize() {
		return (int) (NUMBER_FIELD_FONT_SIZE * SCREEN_SCALE_FACTOR);
	}

	public static int getTableNumberFontSize() {
		return (int) (TABLE_NUMBER_FONT_SIZE * SCREEN_SCALE_FACTOR);
	}
}
