package com.floreantpos.print;

import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import jpos.CashDrawer;
import jpos.JposException;
import jpos.POSPrinter;
import jpos.POSPrinterConst;

public class PosPrinter {
	public static final int SIZE_0 = 0;
	public static final int SIZE_1 = 1;
	public static final int SIZE_2 = 2;
	public static final int SIZE_3 = 3;

	public static final int STYLE_PLAIN = 0;
	public static final int STYLE_BOLD = 1;
	public static final int STYLE_UNDERLINE = 2;

	public static final int ALIGN_LEFT = 0;
	public static final int ALIGN_RIGHT = 1;
	public static final int ALIGN_CENTER = 2;

	public static final String BARCODE_EAN13 = "EAN13";
	public static final String BARCODE_CODE128 = "CODE128";

	public static final String JPOS_SIZE0 = "\u001b|1C";
	public static final String JPOS_SIZE1 = "\u001b|2C";
	public static final String JPOS_SIZE2 = "\u001b|3C";
	public static final String JPOS_SIZE3 = "\u001b|4C";
	public static final String JPOS_LF = "\n";
	public static final String JPOS_BOLD = "\u001b|bC";
	public static final String JPOS_UNDERLINE = "\u001b|uC";
	public static final String JPOS_CUT = "\u001b|100fP";
	public static final String JPOS_ALIGN_CENTER = "\u001b|cA";
	public static final String JPOS_REVERSE = "\u001DB\1";

	private String m_sName;

	private POSPrinter m_printer;
	private CashDrawer m_drawer;

	private StringBuffer m_sline;
	private boolean m_bTransaction = false;

	/**
	 * Creates a new instance of DevicePrinterJavaPOS
	 * @param sDevicePrinterName
	 * @param sDeviceDrawerName
	 */
	public PosPrinter(String sDevicePrinterName, String sDeviceDrawerName) {
		m_sName = sDevicePrinterName;

		m_printer = new POSPrinter();
		//
		try {
			m_printer.open(sDevicePrinterName);
			m_printer.claim(10000);
			m_printer.setDeviceEnabled(true);
			m_printer.setMapMode(POSPrinterConst.PTR_MM_METRIC); // unit = 1/100 mm - i.e. 1 cm = 10 mm = 10 * 100 units

			
		} catch (JposException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		try {
			m_drawer = new CashDrawer();
			m_drawer.open(sDevicePrinterName);
			m_drawer.claim(10000);
			m_drawer.setDeviceEnabled(true);
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public String getPrinterName() {
		return m_sName;
	}

	public String getPrinterDescription() {
		return null;
	}

	public JComponent getPrinterComponent() {
		return null;
	}

	public void reset() {
	}

	public void printImage(BufferedImage image) {
		//            try {
		//                if (m_printer.getCapRecBitmap()) { // si podemos imprimir bitmaps.
		//                    startTransaction();
		//
		//                    File f = File.createTempFile("jposimg", ".png");
		//                    OutputStream out = new FileOutputStream(f);
		//                    out.write(ImageUtils.writeImage(image));
		//                    out.close();
		//
		//                    m_printer.printBitmap(POSPrinterConst.PTR_S_RECEIPT, f.getAbsolutePath(), POSPrinterConst.PTR_BM_ASIS, POSPrinterConst.PTR_BM_CENTER);
		//                }
		//            } catch (IOException eIO) {
		//            } catch (JposException e) {
		//            }
	}

	public void printBarCode(String sType, String sCode) {
		try {
			if (m_printer.getCapRecBarCode()) { // si podemos imprimir c�digos de barras
				startTransaction();
				//   print a Code 3 of 9 barcode with the data "123456789012" encoded
				//   the 10 * 100, 60 * 100 parameters below specify the barcode's height and width
				//   in the metric map mode (1cm tall, 6cm wide)
				m_printer.printBarCode(POSPrinterConst.PTR_S_RECEIPT, sCode, POSPrinterConst.PTR_BCS_EAN13, 10 * 100, 60 * 100, POSPrinterConst.PTR_BC_CENTER, POSPrinterConst.PTR_BC_TEXT_BELOW);
			}
		} catch (JposException e) {
		}
	}

	public void beginLine(int iTextSize) {
		m_sline = new StringBuffer();
		if (iTextSize == SIZE_0) {
			m_sline.append(JPOS_SIZE0);
		}
		else if (iTextSize == SIZE_1) {
			m_sline.append(JPOS_SIZE1);
		}
		else if (iTextSize == SIZE_2) {
			m_sline.append(JPOS_SIZE2);
		}
		else if (iTextSize == SIZE_3) {
			m_sline.append(JPOS_SIZE3);
		}
		else {
			m_sline.append(JPOS_SIZE0);
		}
	}

	public void printText(int iStyle, String sText) {

		//		if ((iStyle & STYLE_BOLD) != 0) {
		//			m_sline.append(JPOS_BOLD);
		//		}
		//		if ((iStyle & STYLE_UNDERLINE) != 0) {
		//			m_sline.append(JPOS_UNDERLINE);
		//		}

		//m_sline.append(iStyle);
		if(m_sline == null) {
			m_sline = new StringBuffer();
			m_sline.append(JPOS_SIZE0);
		}
		m_sline.append(sText);
	}

	public void printText(String sText) {
		if(m_sline == null) {
			m_sline = new StringBuffer();
			m_sline.append(JPOS_SIZE0);
		}
		m_sline.append(sText);
	}

	public void endLine() {
		if (m_sline != null) {
			m_sline.append(JPOS_LF);
			try {
				startTransaction();
				m_printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, m_sline.toString());
			} catch (JposException e) {
			}
			m_sline = null;
		}
	}

	public void printEmptyLine() {
		beginLine(PosPrinter.SIZE_1);
		endLine();
	}

	public void printCutPartial() {
		try {
			startTransaction();
			m_printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, JPOS_CUT);

			m_printer.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_NORMAL);
			m_bTransaction = false;
		} catch (JposException e) {
		}
	}

	private void startTransaction() throws JposException {
		if (!m_bTransaction) {
			m_printer.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_TRANSACTION);
			m_bTransaction = true;
		}
	}

	public void openDrawer() {
		try {
			m_drawer.openDrawer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void finalize() {
		try {
			m_printer.setDeviceEnabled(false);
			m_printer.release();
			m_printer.close();
			
			
		} catch (JposException e) {
		}
		
		try {
			m_drawer.setDeviceEnabled(false);
			m_drawer.release();
			m_drawer.close();
		} catch(Exception x) {
			x.printStackTrace();
		}
	}
}
