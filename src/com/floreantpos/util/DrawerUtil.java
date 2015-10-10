package com.floreantpos.util;

import jssc.SerialPort;
import jssc.SerialPortException;

import com.floreantpos.Messages;
import com.floreantpos.config.TerminalConfig;

public class DrawerUtil {
	/* fields */

	private boolean escp24pin; //boolean to indicate whether the printer is a 24 pin esc/p2 epson

	private static int MAX_ADVANCE_9PIN = 216; //for 24/48 pin esc/p2 printers this should be 180
	private static int MAX_ADVANCE_24PIN = 180;
	private static int MAX_UNITS = 127; //for vertical positioning range is between 0 - 255 (0 <= n <= 255) according to epson ref. but 255 gives weird errors at 1.5f, 127 as max (0 - 128) seems to be working
	private static final float CM_PER_INCH = 2.54f;

	/* decimal ascii values for epson ESC/P commands */
	private static final char ESC = 27; //escape
	private static final char AT = 64; //@
	private static final char LINE_FEED = 10; //line feed/new line
	private static final char PARENTHESIS_LEFT = 40;
	private static final char BACKSLASH = 92;
	private static final char CR = 13; //carriage return
	private static final char TAB = 9; //horizontal tab
	private static final char FF = 12; //form feed
	private static final char g = 103; //15cpi pitch
	private static final char p = 112; //used for choosing proportional mode or fixed-pitch
	private static final char t = 116; //used for character set assignment/selection
	private static final char l = 108; //used for setting left margin
	private static final char x = 120; //used for setting draft or letter quality (LQ) printing
	private static final char E = 69; //bold font on
	private static final char F = 70; //bold font off
	private static final char J = 74; //used for advancing paper vertically
	private static final char P = 80; //10cpi pitch
	private static final char Q = 81; //used for setting right margin
	private static final char $ = 36; //used for absolute horizontal positioning
	private static final char ARGUMENT_0 = 0;
	private static final char ARGUMENT_1 = 1;
	private static final char ARGUMENT_2 = 2;
	private static final char ARGUMENT_3 = 3;
	private static final char ARGUMENT_4 = 4;
	private static final char ARGUMENT_5 = 5;
	private static final char ARGUMENT_6 = 6;
	private static final char ARGUMENT_7 = 7;
	private static final char ARGUMENT_25 = 25;
	private static final char TEAR = 105;
	private static final char _0 = 0;
	private static final char _1 = 1;

	private static final char _25 = 25;
	private static final char _250 = 250;
	private static final char _999 = 999;
	private static final char _48 = 48;
	private static final char _55 = 55;
	private static final char _121 = 121;

	/* character sets */
	public static final char USA = ARGUMENT_1;
	public static final char BRAZIL = ARGUMENT_25;
	public static SerialPort serialPort;
	
	private static char[] controlCodes;

	public void close() {
		//post: closes the stream, used when printjob ended
		try {
			serialPort.closePort();
		} catch (SerialPortException ex) {
		}
	}

	public static boolean initialize() {

		try {
			serialPort.openPort();//Open serial port
			serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			//Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);
			//serialPort.writeBytes( reconstitutedString.getBytes());//

			//reset default settings
			print(ESC);
			print(AT);

			//select 10-cpi character pitch
			//select10CPI();

			//select draft quality printing
			//selectDraftPrinting();

			//set character set
			setCharacterSet(USA);

		} catch (SerialPortException ex) {
			return false;
		}

		return true;
	}

	public static void select10CPI() { //10 characters per inch (condensed available)
		print(ESC);
		print(P);
	}

	public static void select15CPI() { //15 characters per inch (condensend not available)
		print(ESC);
		print(g);
	}

	public static void selectDraftPrinting() { //set draft quality printing
		print(ESC);
		print(x);
		print((char) 48);
	}

	public static void selectLQPrinting() { //set letter quality printing
		print(ESC);
		print(x);
		print((char) 49);
	}

	public static void setCharacterSet(char charset) {
		//assign character table
		print(ESC);
		print(PARENTHESIS_LEFT);
		print(t);
		print(ARGUMENT_3); //always 3
		print(ARGUMENT_0); //always 0
		print(ARGUMENT_1); //selectable character table 1
		print(charset); //registered character table (arg_25 is brascii)
		print(ARGUMENT_0); //always 0

		//select character table
		print(ESC);
		print(t);
		print(ARGUMENT_1); //selectable character table 1
	}

	public static void lineFeed() {
		//post: performs new line
		print(CR); //according to epson esc/p ref. manual always send carriage return before line feed
		print(LINE_FEED);
	}

	public static void formFeed() {
		//post: ejects single sheet
		print(CR); //according to epson esc/p ref. manual it is recommended to send carriage return before form feed
		print(FF);
	}

	public static void bold(boolean bold) {
		print(ESC);
		if (bold)
			print(E);
		else
			print(F);
	}

	public static void tear() {
		print(ESC);
		print(TEAR);
	}

	public static void kick() {
		print("Start kicking drawer\n"); //$NON-NLS-1$

		//print(ESC);
		//print(AT);

//		print((char) 27);
//		print((char) 112);
//		print((char) 0);
//		print((char) 100);
//		print((char) 250);
		
		print(controlCodes[0]);
		print(controlCodes[1]);
		print(controlCodes[2]);
		print(controlCodes[3]);
		print(controlCodes[4]);

		/*	
		print(ESC);
		print(p);
		print((char)1);
		print(_25);
		print(_250);

		print(ESC);
		print(p);
		print(_0);
		print((char)64);
		print((char)240);

		    print(ESC);
			print(p);
			print(_48);
			print(_55);
			print(_121);
		*/
		print("End kicking drawer\n"); //$NON-NLS-1$
	}

	public static void proportionalMode(boolean proportional) {
		print(ESC);
		print(p);
		if (proportional)
			print((char) 49);
		else
			print((char) 48);
	}

	public static void advanceVertical(float centimeters) {
		//pre: centimeters >= 0 (cm)
		//dummy
	}

	public static void advanceHorizontal(float centimeters) {
		//pre: centimeters >= 0
		//post: advances horizontal print position approx. centimeters
		float inches = centimeters / CM_PER_INCH;
		int units_low = (int) (inches * 120) % 256;
		int units_high = (int) (inches * 120) / 256;

		print(ESC);
		print(BACKSLASH);
		print((char) units_low);
		print((char) units_high);
	}

	public static void setAbsoluteHorizontalPosition(float centimeters) {
		//pre: centimenters >= 0 (cm)
		//post: sets absolute horizontal print position to x centimeters from left margin
		float inches = centimeters / CM_PER_INCH;
		int units_low = (int) (inches * 60) % 256;
		int units_high = (int) (inches * 60) / 256;

		print(ESC);
		print($);
		print((char) units_low);
		print((char) units_high);
	}

	public static void horizontalTab(int tabs) {
		//pre: tabs >= 0
		//post: performs horizontal tabs tabs number of times
		for (int i = 0; i < tabs; i++)
			print(TAB);
	}

	public static void setMargins(int columnsLeft, int columnsRight) {
		//pre: columnsLeft > 0 && <= 255, columnsRight > 0 && <= 255
		//post: sets left margin to columnsLeft columns and right margin to columnsRight columns
		//left
		print(ESC);
		print(l);
		print((char) columnsLeft);

		//right
		print(ESC);
		print(Q);
		print((char) columnsRight);
	}

	public static void print(String text) {
		try {
			serialPort.writeBytes(text.getBytes());

		} catch (SerialPortException ex) {
			System.out.println(ex);
		}

	}

	public static void print(char text) {
		try {
			serialPort.writeByte((byte) text);
		} catch (SerialPortException ex) {
			System.out.println(ex);
		}

	}

	public static boolean isInitialized() {
		//post: returns true iff printer was successfully initialized
		return true;
	}

	public static String getShare() {
		//post: returns printer share name (Windows network)
		return ""; //dummy //$NON-NLS-1$
	}

	public static void kickDrawer() {
		String portName = TerminalConfig.getDrawerPortName();
		char[] codesArray = TerminalConfig.getDrawerControlCodesArray();

		kickDrawer(portName, codesArray);
	}
	
	public static void kickDrawer(String portName, char[] codes) {
		DrawerUtil.controlCodes = codes;
		serialPort = new SerialPort(portName);

		initialize();

		try {
			kick();
			//tear();

			serialPort.closePort();//Close serial port
		} catch (SerialPortException ex) {
			System.out.println(ex);
		}
	}
}