package com.floreantpos.model;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.FileUtils;

@XmlRootElement(name = "printers")
public class PosPrinters {
	private Printer reportPrinter;
	
	private Printer defaultReceiptPrinter;
	private Printer defaultKitchenPrinter;
	
	private List<Printer> receiptPrinters;
	private List<Printer> kitchenPrinters;
	
	private Map<VirtualPrinter, Printer> receiptPrinterMap = new HashMap<VirtualPrinter, Printer>();
	private Map<VirtualPrinter, Printer> kitchePrinterMap = new HashMap<VirtualPrinter, Printer>();

	public Printer getReportPrinter() {
		return reportPrinter;
	}

	public void setReportPrinter(Printer reportPrinter) {
		this.reportPrinter = reportPrinter;
	}

	public List<Printer> getReceiptPrinters() {
		if (receiptPrinters == null) {
			receiptPrinters = new ArrayList<Printer>(2);
		}

		return receiptPrinters;
	}

	public void setReceiptPrinters(List<Printer> receiptPrinters) {
		this.receiptPrinters = receiptPrinters;
	}

	public List<Printer> getKitchenPrinters() {
		if (kitchenPrinters == null) {
			kitchenPrinters = new ArrayList<Printer>(2);
		}

		return kitchenPrinters;
	}

	public void setKitchenPrinters(List<Printer> kitchenPrinters) {
		this.kitchenPrinters = kitchenPrinters;
	}

	public void addReceiptPrinter(Printer printer) {
		getReceiptPrinters().add(printer);
	}

	public void addKitchenPrinter(Printer printer) {
		getKitchenPrinters().add(printer);
	}
	
	public void setDefaultReceiptPrinter(Printer defaultReceiptPrinter) {
		this.defaultReceiptPrinter = defaultReceiptPrinter;
	}
	
	public Printer getDefaultReceiptPrinter() {
		if(defaultReceiptPrinter == null && getReceiptPrinters().size() > 0) {
			defaultReceiptPrinter = receiptPrinters.get(0);
			
			for (Printer printer : receiptPrinters) {
				if(printer.isDefaultPrinter()) {
					defaultReceiptPrinter = printer;
					break;
				}
			}
		}
		
		return defaultReceiptPrinter;
	}
	
	public void setDefaultKitchenPrinter(Printer defaultKitchenPrinter) {
		this.defaultKitchenPrinter = defaultKitchenPrinter;
	}
	
	public Printer getDefaultKitchenPrinter() {
		if(defaultKitchenPrinter == null && getKitchenPrinters().size() > 0) {
			defaultKitchenPrinter = kitchenPrinters.get(0);
			
			for (Printer printer : kitchenPrinters) {
				if(printer.isDefaultPrinter()) {
					defaultKitchenPrinter = printer;
					break;
				}
			}
		}
		
		return defaultKitchenPrinter;
	}
	
	public Printer getReceiptPrinterFor(VirtualPrinter vp) {
		return receiptPrinterMap.get(vp);
	}
	
	public Printer getKitchenPrinterFor(VirtualPrinter vp) {
		return kitchePrinterMap.get(vp);
	}
	
	private void populatePrinterMaps() {
		receiptPrinterMap.clear();
		kitchePrinterMap.clear();
		
		for (Printer printer : getReceiptPrinters()) {
			receiptPrinterMap.put(printer.getVirtualPrinter(), printer);
		}
		
		for (Printer printer : getKitchenPrinters()) {
			kitchePrinterMap.put(printer.getVirtualPrinter(), printer);
		}
	}

	public void save() {
		try {
			getDefaultReceiptPrinter();
			getDefaultKitchenPrinter();
			
			populatePrinterMaps();
			
			File file = new File("config", "printers.xml");

			JAXBContext jaxbContext = JAXBContext.newInstance(PosPrinters.class);
			Marshaller m = jaxbContext.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			//m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

			StringWriter writer = new StringWriter();
			m.marshal(this, writer);

			FileUtils.write(file, writer.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static PosPrinters load() {
		try {

			File file = new File("config", "printers.xml");
			if (!file.exists()) {
				return null;
			}

			FileReader reader = new FileReader(file);

			JAXBContext jaxbContext = JAXBContext.newInstance(PosPrinters.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			PosPrinters printers = (PosPrinters) unmarshaller.unmarshal(reader);
			
			printers.populatePrinterMaps();

			return printers;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
