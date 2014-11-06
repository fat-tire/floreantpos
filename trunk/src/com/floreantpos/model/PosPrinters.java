package com.floreantpos.model;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.FileUtils;

@XmlRootElement(name = "printers")
public class PosPrinters {
	private Printer reportPrinter;
	private List<Printer> receiptPrinters;
	private List<Printer> kitchenPrinters;

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

	public void save() {
		try {

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
			PosPrinters elements = (PosPrinters) unmarshaller.unmarshal(reader);

			return elements;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
