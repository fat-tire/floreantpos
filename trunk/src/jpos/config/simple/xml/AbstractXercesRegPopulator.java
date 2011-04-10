package jpos.config.simple.xml;

///////////////////////////////////////////////////////////////////////////////
//
// This software is provided "AS IS".  The JavaPOS working group (including
// each of the Corporate members, contributors and individuals)  MAKES NO
// REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED 
// WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
// NON-INFRINGEMENT. The JavaPOS working group shall not be liable for
// any damages suffered as a result of using, modifying or distributing this
// software or its derivatives. Permission to use, copy, modify, and distribute
// the software and its documentation for any purpose is hereby granted. 
//
// The JavaPOS Config/Loader (aka JCL) is now under the CPL license, which 
// is an OSS Apache-like license.  The complete license is located at:
//    http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
//
///////////////////////////////////////////////////////////////////////////////

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;

import jpos.config.JposEntry;
import jpos.config.Version;
import jpos.config.simple.AbstractRegPopulator;
import jpos.util.JposEntryUtility;
import jpos.util.tracing.Tracer;
import jpos.util.tracing.TracerFactory;

import org.apache.xerces.dom.DOMImplementationImpl;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

/**
 * This class is an abstract super class for all Xerces based parser/reg 
 * populator with functionality to serialize an enumeration of JposEntry 
 * objects into XML 
 * <p>
 * <b>NOTE</b>: this class must define a public no-argument ctor so that it may be 
 * created via reflection when its defined in the jpos.properties as the 
 * jpos.config.regPopulatorClass
 * </p>
 * @see jpos.util.JposProperties#JPOS_REG_POPULATOR_CLASS_PROP_NAME
 * @since 2.1.0
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public abstract class AbstractXercesRegPopulator extends AbstractRegPopulator
		implements XmlRegPopulator {
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	/**
	 * 1-arg constructor that takes the unique ID
	 * @param s the unique ID string
	 * @since 1.3 (Washington DC 2001)
	 */
	public AbstractXercesRegPopulator(String s) {
		super(s);
	}

	//-------------------------------------------------------------------------
	// Public methods
	//

	/**
	 * Tell the populator to save the current entries 
	 * @param entries an enumeration of JposEntry objects
	 * @since 1.2 (NY 2K meeting)
	 * @throws java.lang.Exception if any error occurs while saving
	 */
	public void save(Enumeration entries) throws Exception {
		if (isPopulatorFileDefined())
			convertJposEntriesToXml(entries, getPopulatorFileOS());
		else
			convertJposEntriesToXml(entries, new FileOutputStream(
					getDefaultXmlFileName()));
	}

	/**
	 * Tell the populator to save the current entries in the file specified 
	 * @param entries an enumeration of JposEntry objects
	 * @param xmlFileName the XML file name to save entries
	 * @since 1.3 (SF 2K meeting)
	 * @throws java.lang.Exception if any error occurs while saving
	 */
	public void save(Enumeration entries, String xmlFileName) throws Exception {
		File xmlFile = new File(xmlFileName);
		FileOutputStream fos = new FileOutputStream(xmlFile);

		convertJposEntriesToXml(entries, fos);

		fos.close();
	}

	/**
	 * @return the URL pointing to the entries file loaded or saved
	 * @since 1.2 (NY 2K meeting)
	 */
	public URL getEntriesURL() {
		URL url = null;

		if (getPopulatorFileURL() != null && !getPopulatorFileURL().equals(""))
			try {
				url = new URL(getPopulatorFileURL());
			} catch (Exception e) {
				tracer.println("getEntriesURL: Exception.message="
						+ e.getMessage());
			}
		else
			url = createURLFromFile(new File(getPopulatorFileName()));

		//<temp>            
		tracer.println("getPopulatorFileURL()=" + getPopulatorFileURL());
		tracer.println("getPopulatorFileName()=" + getPopulatorFileName());
		//</temp>

		return url;
	}

	//--------------------------------------------------------------------------
	// Protected methods
	//

	/** @return the Tracer object */
	protected Tracer getTracer() {
		return tracer;
	}

	/** 
	 * @return the default XML file name that this populator will save 
	 * entries to 
	 */
	protected String getDefaultXmlFileName() {
		return xmlFileName;
	}

	/**
	 * Converts an Enumeration of JposEntry objects to XML
	 * @param entries an Enumeration of JposEntry objects
	 * @param os the OutputStream to stream the entries to
	 * @exception java.lang.Exception if something goes wrong serializing
	 * @since 1.2 (NY 2K meeting)
	 */
	protected void convertJposEntriesToXml(Enumeration entries, OutputStream os)
			throws Exception {
		Document document = getParser().getDocument();
		serializeDocument(document, entries, os);
	}

	/**
	 * @return the DOM parser object
	 * @since 1.2 (NY 2K meeting)
	 */
	protected DOMParser getParser() {
		return domParser;
	}

	/**
	 * Serializes the JposEntry objects to an XML document and save to OutputStream
	 * @param document the XML document object
	 * @param entries an Enumeration of JposEntry objects
	 * @param os the OuputStream object
	 * @exception java.lang.Exception anything goes wrong while saving
	 * @since 1.2 (NY 2K meeting)
	 */
	protected void serializeDocument(Document document, Enumeration entries,
			OutputStream os) throws Exception {
		Document newDoc = createEmptyDocument();

		insertJposEntriesInDoc(newDoc, entries);

		insertDateSavedComment(newDoc);

		OutputFormat outFormat = new OutputFormat("xml", "UTF-8", true);

		outFormat.setStandalone(false);
		outFormat.setIndenting(true);
		outFormat.setIndent(4);
		outFormat.setPreserveSpace(true);
		outFormat.setLineWidth(0);

		insertDTDInfo(newDoc, outFormat);

		PrintWriter outWriter = null;
		try {
			outWriter = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(os, "UTF-8")));
		} catch (UnsupportedEncodingException ex) {
			tracer.println("Error making PrintWriter: "
					+ "UnsupportedEncodingException.message = "
					+ ex.getMessage());
		}

		if (outWriter != null) {
			XMLSerializer xmlSerializer = new XMLSerializer(outWriter,
					outFormat);
			xmlSerializer.serialize(newDoc);
		}
	}

	/**
	 * @return a String with the document type definition value.  For DTD this
	 * would be the DTD relative path/file and for schemas the XSD 
	 * relative path/file
	 * @since 2.1.0
	 */
	protected String getDoctypeValue() {
		return "jpos/res/jcl.dtd";
	}

	/**
	 * Inset DTD information in the XML Document object
	 * @param doc the XML Document object
	 * @param outFormat the OuputFormat object
	 * @exception java.lang.Exception in case something goes wrong
	 * @since 1.2 (NY 2K meeting)
	 */
	protected void insertDTDInfo(Document doc, OutputFormat outFormat)
			throws Exception {
		//String publicId = OutputFormat.whichDoctypePublic(doc);
		//String systemId = OutputFormat.whichDoctypeSystem(doc);

		outFormat.setDoctype("JposEntries", getDoctypeValue());
	}

	/**
	 * @return an empty XML Document object
	 * @since 1.2 (NY 2K meeting)
	 */
	protected Document createEmptyDocument() {
		DOMImplementationImpl domImpImpl = (DOMImplementationImpl) DOMImplementationImpl
				.getDOMImplementation();
		DocumentType docType = domImpImpl.createDocumentType("JposEntries",
				"-//JavaPOS//DTD//EN", getDoctypeValue());

		Document doc = domImpImpl.createDocument(null, "JposEntries", docType);

		return doc;
	}

	/**
	 * Inserts date and info saved in the XML Document object
	 * @param document the XML Document object
	 * @exception java.lang.Exception in case something goes wrong
	 * @since 1.2 (NY 2K meeting)
	 */
	protected void insertDateSavedComment(Document document) throws Exception {
		String dateString = DateFormat.getInstance().format(
				new Date(System.currentTimeMillis()));

		String commentString = "Saved by JavaPOS jpos.config/loader (JCL) version "
				+ Version.getVersionString() + " on " + dateString;

		Comment comment = document.createComment(commentString);

		document.getDocumentElement().insertBefore(comment,
				document.getDocumentElement().getFirstChild());

		document.getDocumentElement().insertBefore(
				document.createTextNode("\n"), comment);

		document.getDocumentElement()
				.appendChild(document.createTextNode("\n"));
	}

	/**
	 * Appends the <creation> element to the document
	 * @param doc the XML Document object
	 * @param jposEntryElement the <JposEntryElement> XML Element object
	 * @param jposEntry the JposEntry object
	 * @since 1.2 (NY 2K meeting)
	 */
	protected void appendCreationElement(Document doc,
			Element jposEntryElement, JposEntry jposEntry) {
		jposEntryElement.appendChild(doc.createTextNode("    " + "    "));

		Element creationElement = doc.createElement("creation");

		Attr factoryClassAttr = doc.createAttribute("factoryClass");
		Attr serviceClassAttr = doc.createAttribute("serviceClass");

		factoryClassAttr.setValue((String) jposEntry
				.getPropertyValue("serviceInstanceFactoryClass"));

		serviceClassAttr.setValue((String) jposEntry
				.getPropertyValue("serviceClass"));

		creationElement.setAttributeNode(factoryClassAttr);
		creationElement.setAttributeNode(serviceClassAttr);

		jposEntryElement.appendChild(creationElement);
		jposEntryElement.appendChild(doc.createTextNode("\n"));
	}

	/**
	 * Appends the <vendor> element to the document
	 * @param doc the XML Document object
	 * @param jposEntryElement the <JposEntryElement> XML Element object
	 * @param jposEntry the JposEntry object
	 * @since 1.2 (NY 2K meeting)
	 */
	protected void appendVendorElement(Document doc, Element jposEntryElement,
			JposEntry jposEntry) {
		jposEntryElement.appendChild(doc.createTextNode("    " + "    "));

		Element vendorElement = doc.createElement("vendor");

		Attr nameAttr = doc.createAttribute("name");
		Attr urlAttr = doc.createAttribute("url");

		nameAttr.setValue((String) jposEntry.getPropertyValue("vendorName"));
		urlAttr.setValue((String) jposEntry.getPropertyValue("vendorURL"));

		vendorElement.setAttributeNode(nameAttr);
		vendorElement.setAttributeNode(urlAttr);

		jposEntryElement.appendChild(vendorElement);
		jposEntryElement.appendChild(doc.createTextNode("\n"));
	}

	/**
	 * Appends the <jpos> element to the document
	 * @param doc the XML Document object
	 * @param jposEntryElement the <JposEntryElement> XML Element object
	 * @param jposEntry the JposEntry object
	 * @since 1.2 (NY 2K meeting)
	 */
	protected void appendJposElement(Document doc, Element jposEntryElement,
			JposEntry jposEntry) {
		jposEntryElement.appendChild(doc.createTextNode("    " + "    "));

		Element jposElement = doc.createElement("jpos");

		Attr versionAttr = doc.createAttribute("version");
		Attr categoryAttr = doc.createAttribute("category");

		versionAttr
				.setValue((String) jposEntry.getPropertyValue("jposVersion"));

		categoryAttr.setValue((String) jposEntry
				.getPropertyValue("deviceCategory"));

		jposElement.setAttributeNode(versionAttr);
		jposElement.setAttributeNode(categoryAttr);

		jposEntryElement.appendChild(jposElement);
		jposEntryElement.appendChild(doc.createTextNode("\n"));
	}

	/**
	 * Appends the <product> element to the document
	 * @param doc the XML Document object
	 * @param jposEntryElement the <JposEntryElement> XML Element object
	 * @param jposEntry the JposEntry object
	 * @since 1.2 (NY 2K meeting)
	 */
	protected void appendProductElement(Document doc, Element jposEntryElement,
			JposEntry jposEntry) {
		jposEntryElement.appendChild(doc.createTextNode("    " + "    "));

		Element productElement = doc.createElement("product");

		Attr nameAttr = doc.createAttribute("name");
		Attr descriptionAttr = doc.createAttribute("description");
		Attr urlAttr = doc.createAttribute("url");

		nameAttr.setValue((String) jposEntry.getPropertyValue("productName"));

		descriptionAttr.setValue((String) jposEntry
				.getPropertyValue("productDescription"));

		urlAttr.setValue((String) jposEntry.getPropertyValue("productURL"));

		productElement.setAttributeNode(nameAttr);
		productElement.setAttributeNode(descriptionAttr);
		productElement.setAttributeNode(urlAttr);

		jposEntryElement.appendChild(productElement);
		jposEntryElement.appendChild(doc.createTextNode("\n"));
	}

	/**
	 * Appends the <prop> element to the document
	 * @param doc the XML Document object
	 * @param jposEntryElement the <JposEntryElement> XML Element object
	 * @param propName the property name
	 * @param propValue the property value
	 * @since 1.2 (NY 2K meeting)
	 */
	protected void appendPropElement(Document doc, Element jposEntryElement,
			String propName, Object propValue) {
		jposEntryElement.appendChild(doc.createTextNode("    " + "    "));

		Element propElement = doc.createElement("prop");

		Attr nameAttr = doc.createAttribute("name");
		Attr valueAttr = doc.createAttribute("value");
		Attr typeAttr = doc.createAttribute("type");

		nameAttr.setValue(propName);
		valueAttr.setValue(propValue.toString());
		typeAttr
				.setValue(JposEntryUtility.shortClassName(propValue.getClass()));

		propElement.setAttributeNode(nameAttr);
		propElement.setAttributeNode(valueAttr);
		propElement.setAttributeNode(typeAttr);

		jposEntryElement.appendChild(propElement);
		jposEntryElement.appendChild(doc.createTextNode("\n"));
	}

	/**
	 * Appends non-required properties name and value
	 * @param doc the XML Document object
	 * @param jposEntryElement the <JposEntryElement> XML Element object
	 * @param jposEntry the JposEntry object
	 * @since 1.2 (NY 2K meeting)
	 */
	protected void appendPropElements(Document doc, Element jposEntryElement,
			JposEntry jposEntry) {
		jposEntryElement
				.appendChild(doc.createTextNode("\n" + "    " + "    "));

		String comment = "Other non JavaPOS required property"
				+ " (mostly vendor properties and bus specific "
				+ "properties i.e. RS232 )";

		jposEntryElement.appendChild(doc.createComment(comment));

		jposEntryElement.appendChild(doc.createTextNode("\n"));

		Enumeration props = jposEntry.getPropertyNames();

		while (props.hasMoreElements()) {
			String propName = (String) props.nextElement();

			if (!JposEntryUtility.isRequiredPropName(propName))
				appendPropElement(doc, jposEntryElement, propName, jposEntry
						.getPropertyValue(propName));
		}
	}

	/**
	 * Insert the <JposEntryElement> in the XML document object
	 * @param doc the XML Document object
	 * @param jposEntryElement the <JposEntryElement> XML Element object
	 * @param jposEntry the JposEntry object
	 * @since 1.2 (NY 2K meeting)
	 */
	protected void insertJposEntryInDoc(Document doc, Element jposEntryElement,
			JposEntry jposEntry) {
		appendCreationElement(doc, jposEntryElement, jposEntry);
		appendVendorElement(doc, jposEntryElement, jposEntry);
		appendJposElement(doc, jposEntryElement, jposEntry);
		appendProductElement(doc, jposEntryElement, jposEntry);
		appendPropElements(doc, jposEntryElement, jposEntry);

		doc.getDocumentElement().appendChild(doc.createTextNode("\n" + "    "));
		doc.getDocumentElement().appendChild(jposEntryElement);
		doc.getDocumentElement().appendChild(doc.createTextNode("\n" + "    "));
	}

	/**
	 * Insert an Enumeration of <JposEntryElement> objects in the XML document
	 * @param doc the XML Document object
	 * @param entries an Enumeration of JposEntry objects
	 * @since 1.2 (NY 2K meeting)
	 */
	protected void insertJposEntriesInDoc(Document doc, Enumeration entries) {
		while (entries.hasMoreElements()) {
			JposEntry jposEntry = (JposEntry) entries.nextElement();

			if (JposEntryUtility.isValidJposEntry(jposEntry)) {
				doc.getDocumentElement().appendChild(
						doc.createTextNode("\n" + "    "));

				Element jposEntryElement = doc.createElement("JposEntry");

				Attr logicalNameAttr = doc.createAttribute("logicalName");
				logicalNameAttr.setValue((String) jposEntry
						.getPropertyValue("logicalName"));

				jposEntryElement.setAttributeNode(logicalNameAttr);

				jposEntryElement.appendChild(doc.createTextNode("\n"));

				insertJposEntryInDoc(doc, jposEntryElement, jposEntry);
			}
		}
	}

	//--------------------------------------------------------------------------
	// Instance variables
	//

	protected String xmlFileName = DEFAULT_XML_FILE_NAME;

	protected DOMParser domParser = new DOMParser();

	private Tracer tracer = TracerFactory.getInstance().createTracer(
			"AbstractXercesRegPopulator");

	//--------------------------------------------------------------------------
	// Public constants
	//

	public static final String DTD_FILE_PATH = "jpos" + File.separator + "res";
	public static final String DTD_FILE_NAME = DTD_FILE_PATH + File.separator
			+ "jcl.dtd";
}