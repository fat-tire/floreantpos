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
package com.floreantpos.model;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.FileUtils;

import com.floreantpos.PosLog;
import com.floreantpos.model.dao.TerminalPrintersDAO;
import com.floreantpos.model.dao.VirtualPrinterDAO;

@XmlRootElement(name = "printers")
public class PosPrinters {
	private static String reportPrinter;
	private static String receiptPrinter;

	private Printer defaultKitchenPrinter;
	private static List<Printer> kitchenPrinters;

	private Map<VirtualPrinter, Printer> kitchePrinterMap = new HashMap<VirtualPrinter, Printer>();

	public String getReportPrinter() {
		return reportPrinter;
	}

	public void setReportPrinter(String reportPrinter) {
		PosPrinters.reportPrinter = reportPrinter;
	}

	public String getReceiptPrinter() {
		return receiptPrinter;
	}

	public void setReceiptPrinter(String receiptPrinter) {
		PosPrinters.receiptPrinter = receiptPrinter;
	}

	public List<Printer> getKitchenPrinters() {
		if (kitchenPrinters == null) {
			kitchenPrinters = new ArrayList<Printer>(4);
		}

		return kitchenPrinters;
	}

	public void setKitchenPrinters(List<Printer> kitchenPrinters) {
		PosPrinters.kitchenPrinters = kitchenPrinters;
	}

	public void addKitchenPrinter(Printer printer) {
		getKitchenPrinters().add(printer);
	}

	public void setDefaultKitchenPrinter(Printer defaultKitchenPrinter) {
		this.defaultKitchenPrinter = defaultKitchenPrinter;
	}

	public Printer getDefaultKitchenPrinter() {
		if (getKitchenPrinters().size() > 0) {
			defaultKitchenPrinter = kitchenPrinters.get(0);

			for (Printer printer : kitchenPrinters) {
				if (printer.isDefaultPrinter()) {
					defaultKitchenPrinter = printer;
					break;
				}
			}
		}

		return defaultKitchenPrinter;
	}

	public Printer getDefaultPrinter() {
		if (getKitchenPrinters().size() > 0) {
			defaultKitchenPrinter = kitchenPrinters.get(0);

			for (Printer printer : kitchenPrinters) {
				if (printer.isDefaultPrinter()) {
					defaultKitchenPrinter = printer;
					break;
				}
			}
		}

		return defaultKitchenPrinter;
	}

	public Printer getKitchenPrinterFor(VirtualPrinter vp) {
		return kitchePrinterMap.get(vp);
	}

	private void populatePrinterMaps() {
		kitchePrinterMap.clear();

		for (Printer printer : getKitchenPrinters()) {
			kitchePrinterMap.put(printer.getVirtualPrinter(), printer);
		}
	}

	public void save() {
		try {
			getDefaultKitchenPrinter();

			populatePrinterMaps();

			File file = new File("config", "printers.xml"); //$NON-NLS-1$ //$NON-NLS-2$

			JAXBContext jaxbContext = JAXBContext.newInstance(PosPrinters.class);
			Marshaller m = jaxbContext.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			StringWriter writer = new StringWriter();
			m.marshal(this, writer);

			FileUtils.write(file, writer.toString());

		} catch (Exception e) {
			PosLog.error(getClass(), e);
		}
	}

	public static PosPrinters load() {
		try {

			List<TerminalPrinters> terminalPrinters = TerminalPrintersDAO.getInstance().findTerminalPrinters();

			PosPrinters printers = new PosPrinters();

			List<Printer> terminalActivePrinters = new ArrayList<Printer>();
			for (TerminalPrinters terminalPrinter : terminalPrinters) {
				int printerType = terminalPrinter.getVirtualPrinter().getType();
				if (printerType == VirtualPrinter.REPORT) {
					if (terminalPrinter.getPrinterName() != null) {
						reportPrinter = terminalPrinter.getPrinterName();
					}
				}
				else if (printerType == VirtualPrinter.RECEIPT) {
					receiptPrinter = terminalPrinter.getPrinterName();
				}
				else if (printerType == VirtualPrinter.KITCHEN_DISPLAY) {
				}
				else {
					Printer printer = new Printer(terminalPrinter.getVirtualPrinter(), terminalPrinter.getPrinterName());
					terminalActivePrinters.add(printer);
				}
			}
			kitchenPrinters = terminalActivePrinters;

//			if (receiptPrinter == null) {
//				receiptPrinter = getDefaultPrinterName();
//			}
//			if (reportPrinter == null) {
//				reportPrinter = getDefaultPrinterName();
//			}
//			if (kitchenPrinters == null || kitchenPrinters.isEmpty()) {
//				Printer printer = new Printer(new VirtualPrinter(1, "kitchen"), getDefaultPrinterName()); //$NON-NLS-1$
//				kitchenPrinters.add(printer);
//			}
			printers.populatePrinterMaps();

			return printers;

		} catch (Exception e) {
			PosLog.error(PosPrinters.class, e.getMessage());
		}

		return null;
	}

	private static String getDefaultPrinterName() {
		PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();
		if (defaultPrintService != null) {
			return defaultPrintService.getName();
		}

		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		if (services.length > 0) {
			return services[0].getName();
		}
		return null;
	}

	private static void initVirtualPrinter(Printer printer) {
		if (printer == null)
			return;

		VirtualPrinter virtualPrinter = printer.getVirtualPrinter();

		VirtualPrinterDAO dao = VirtualPrinterDAO.getInstance();
		VirtualPrinter printerByName = dao.findPrinterByName(virtualPrinter.getName());
		if (printerByName != null) {
			printer.setVirtualPrinter(printerByName);
		}
		else {
			Integer id = virtualPrinter.getId();
			if (dao.get(id) != null) {
				;
				dao.saveOrUpdate(virtualPrinter);
			}
			else {
				dao.save(virtualPrinter);
			}
		}
	}

}
