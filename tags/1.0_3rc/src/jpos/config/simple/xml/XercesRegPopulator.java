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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Vector;

import jpos.config.JposConfigException;
import jpos.config.JposEntry;
import jpos.config.simple.SimpleEntry;
import jpos.util.JposEntryUtility;
import jpos.util.tracing.Tracer;
import jpos.util.tracing.TracerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Simple implementation of the JposRegPopulator that loads and saves 
 * the entries in XML using the "jpos/res/jcl.dtd" DTD and the XML4J 
 * (Xerces) API 
 * NOTE: this class must define a public no-argument ctor so that it may be 
 * created via reflection when its defined in the jpos.properties as 
 * the jpos.config.regPopulatorClass
 * @see jpos.util.JposProperties#JPOS_REG_POPULATOR_CLASS_PROP_NAME
 * @since 1.2 (NY 2K meeting)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public class XercesRegPopulator extends AbstractXercesRegPopulator {
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	/**
	 * Default ctor
	 * @since 1.2 (NY 2K meeting)
	 */
	public XercesRegPopulator() {
		super(XercesRegPopulator.class.getName());
	}

	/**
	 * 1-arg constructor that takes the unique ID
	 * @param s the unique ID string
	 * @since 1.3 (Washington DC 2001)
	 */
	public XercesRegPopulator(String s) {
		super(s);
	}

	//-------------------------------------------------------------------------
	// Public methods
	//

	/**
	 * @return the fully qualified class name implementing the 
	 * JposRegPopulator interface
	 * @since 1.3 (Washington DC 2001 meeting)
	 */
	public String getClassName() {
		return XercesRegPopulator.class.getName();
	}

	/**
	 * Tell the populator to load the entries 
	 * @since 1.2 (NY 2K meeting)
	 */
	public void load() {
		tracer.println("load(): isPopulatorFileDefined=" + isPopulatorFileDefined());

		if (isPopulatorFileDefined() == false) {
			getJposEntries().clear();
			xmlFileName = DEFAULT_XML_FILE_NAME;
			load(xmlFileName);

			return;
		}

		try {
			getJposEntries().clear();

			domParser.setEntityResolver(new XercesRegPopulator.JPOSDTDEntityResolver());

			domParser.parse(new InputSource(getPopulatorFileIS()));

			Document document = domParser.getDocument();

			Enumeration entries = extractJposEntries(document);

			while (entries.hasMoreElements()) {
				JposEntry jposEntry = (JposEntry) entries.nextElement();

				if (jposEntry.hasPropertyWithName(JposEntry.LOGICAL_NAME_PROP_NAME))
					getJposEntries().put(jposEntry.getLogicalName(), jposEntry);
			}

			lastLoadException = null;
		} catch (Exception e) {
			lastLoadException = e;
			tracer.println("Error loading XML file.  Exception.msg = " + e.getMessage());
		} finally {
		}
	}

	/**
	 * Loads the entries specified in the xmlFileName
	 * @param xmlFileName the XML file name
	 * @since 1.3 (SF 2K meeting)
	 */
	public void load(String xmlFileName) {
		tracer.println("load: xmlFileName=" + xmlFileName);

		InputStream is = null;
		File xmlFile = new File(xmlFileName);

		try {
			if (xmlFile.exists())
				is = new FileInputStream(xmlFile);
			else
				is = findFileInClasspath(xmlFileName);

			if (is == null) {
				is = getClass().getClassLoader().getResourceAsStream(xmlFileName);
			}

			if (is == null) {
				getJposEntries().clear();

				tracer.println("Could not find file: " + xmlFileName + " in path or CLASSPATH");

				lastLoadException = new FileNotFoundException(xmlFileName);

				return;
			}

			lastLoadException = null;
		} catch (Exception e) {
			lastLoadException = e;
			tracer.println("Error loading XML file.  Exception.message = " + e.getMessage());
		}

		try {
			getJposEntries().clear();

			domParser.setEntityResolver(new XercesRegPopulator.JPOSDTDEntityResolver());

			domParser.parse(new InputSource(is));

			Document document = domParser.getDocument();

			Enumeration entries = extractJposEntries(document);

			while (entries.hasMoreElements()) {
				JposEntry jposEntry = (JposEntry) entries.nextElement();

				if (jposEntry.hasPropertyWithName(JposEntry.LOGICAL_NAME_PROP_NAME))
					getJposEntries().put(jposEntry.getLogicalName(), jposEntry);
			}

			lastLoadException = null;

		} catch (Exception e) {
			lastLoadException = e;
			tracer.println("Error loading XML file.  Exception.message = " + e.getMessage());
		} finally {
		}
	}

	/**
	 * @return the name of this populator.  This should be a short descriptive name
	 * @since 1.3 (Washington DC 2001 meeting)
	 */
	public String getName() {
		return XERCES_REG_POPULATOR_NAME_STRING;
	}

	//--------------------------------------------------------------------------
	// Protected methods
	//

	/**
	 * @return an enumeration of JposEntry objects read from the XML document object
	 * @param document the XML document object
	 * @since 1.2 (NY 2K meeting)
	 */
	protected Enumeration extractJposEntries(Document document) {
		Vector entries = new Vector();

		NodeList nodeList = document.getElementsByTagName("JposEntry");

		String currentEntryLogicalName = "";

		try {
			for (int i = 0; i < nodeList.getLength(); ++i) {
				Node node = nodeList.item(i);

				if (node.getNodeType() != Node.ELEMENT_NODE)
					continue;

				JposEntry jposEntry = new SimpleEntry();

				Element jposEntryElement = (Element) node;

				currentEntryLogicalName = jposEntryElement.getAttribute("logicalName");
				jposEntry.addProperty("logicalName", currentEntryLogicalName);

				NodeList childList = nodeList.item(i).getChildNodes();

				for (int j = 0; j < childList.getLength(); ++j) {
					Node child = childList.item(j);

					if (child.getNodeType() != Node.ELEMENT_NODE)
						continue;

					Element element = (Element) child;

					String elementName = element.getNodeName();

					if (elementName.equals("creation"))
						extractCreationAttr(jposEntry, element);
					else if (elementName.equals("vendor"))
						extractVendorAttr(jposEntry, element);
					else if (elementName.equals("jpos"))
						extractJposAttr(jposEntry, element);
					else if (elementName.equals("product"))
						extractProductAttr(jposEntry, element);
					else
						extractPropAttr(jposEntry, element);
				}

				if (JposEntryUtility.isValidJposEntry(jposEntry))
					entries.addElement(jposEntry);
				else {
					String msg = "JposEntry with logicalName: " + currentEntryLogicalName + " is not valid (missing required properties)";
					throw new JposConfigException(msg);
				}
			}
		} catch (JposConfigException jce) {
			tracer.println("Skipping invalid entry with logicalName: " + currentEntryLogicalName);
			tracer.println("--->JposConfigException.message = " + jce.getMessage());

			tracer.print(jce);

			if (jce.getOrigException() != null)
				tracer.print(jce.getOrigException());
		}

		return entries.elements();
	}

	/**
	 * Get the <creation> element attributes and adds corresponding 
	 * properties to JposEntry
	 * @param jposEntry the entry to add properties to
	 * @param element the <creation> XML element
	 * @since 1.2 (NY 2K meeting)
	 */
	protected void extractCreationAttr(JposEntry jposEntry, Element element) {
		jposEntry.addProperty("serviceInstanceFactoryClass", element.getAttribute("factoryClass"));

		jposEntry.addProperty("serviceClass", element.getAttribute("serviceClass"));
	}

	/**
	 * Get the <vendor> element attributes and adds corresponding 
	 * properties to JposEntry
	 * @param jposEntry the entry to add properties to
	 * @param element the <vendor> XML element
	 * @since 1.2 (NY 2K meeting)
	 */
	protected void extractVendorAttr(JposEntry jposEntry, Element element) {
		jposEntry.addProperty("vendorName", element.getAttribute("name"));
		jposEntry.addProperty("vendorURL", element.getAttribute("url"));
	}

	/**
	 * Get the <jpos> element attributes and adds corresponding properties 
	 * to JposEntry
	 * @param jposEntry the entry to add properties to
	 * @param element the <jpos> XML element
	 * @since 1.2 (NY 2K meeting)
	 */
	protected void extractJposAttr(JposEntry jposEntry, Element element) {
		jposEntry.addProperty("jposVersion", element.getAttribute("version"));

		jposEntry.addProperty("deviceCategory", element.getAttribute("category"));
	}

	/**
	 * Get the <product> element attributes and adds corresponding 
	 * properties to JposEntry
	 * @param jposEntry the entry to add properties to
	 * @param element the <product> XML element
	 * @since 1.2 (NY 2K meeting)
	 */
	protected void extractProductAttr(JposEntry jposEntry, Element element) {
		jposEntry.addProperty("productName", element.getAttribute("name"));

		jposEntry.addProperty("productDescription", element.getAttribute("description"));

		jposEntry.addProperty("productURL", element.getAttribute("url"));
	}

	/**
	 * Get the <prop> element attributes and adds corresponding properties 
	 * to JposEntry
	 * @param jposEntry the entry to add properties to
	 * @param element the <prop> XML element
	 * @since 1.2 (NY 2K meeting)
	 * @throws jpos.config.JposConfigException if the property value does 
	 * not match the type or is not a valid value (like for instance 
	 * an invalid number)
	 */
	protected void extractPropAttr(JposEntry jposEntry, Element element) throws JposConfigException {
		String propName = element.getAttribute("name");
		String propValueString = element.getAttribute("value");
		String propTypeString = element.getAttribute("type");

		if (propTypeString.equals(""))
			propTypeString = "String";

		Object propValue = null;
		Class propType = null;

		try {
			propType = Class.forName((propTypeString.startsWith("java.lang.") ? propTypeString : "java.lang." + propTypeString));
		} catch (ClassNotFoundException cnfe) {
			throw new JposConfigException("Invalid property type: " + propTypeString + " for property named: " + propName, cnfe);
		}

		if (JposEntryUtility.isValidPropType(propType) == false)
			throw new JposConfigException("Invalid property type: " + propTypeString + " for property named: " + propName);

		propValue = JposEntryUtility.parsePropValue(propValueString, propType);

		if (JposEntryUtility.validatePropValue(propValue, propType) == false)
			throw new JposConfigException("Invalid property type: " + propTypeString + " for property named: " + propName);

		jposEntry.add(jposEntry.createProp(propName, propValue, propType));
	}

	/**
	 * JposDTDEntityResolver to resolve DTD
	 */
	public class JPOSDTDEntityResolver implements org.xml.sax.EntityResolver {
		/**
		 * @return the DTD as an InputSource if it is found in a JAR
		 * file in the CLASSPATH otherwise return null
		 */
		public org.xml.sax.InputSource resolveEntity(String publicId, String systemId) throws org.xml.sax.SAXException, java.io.IOException {
			if (publicId.equals("-//JavaPOS//DTD//EN")) {
				InputStream is = getClass().getResourceAsStream(DTD_JAR_FILE_NAME);

				if (is != null) {
					return (new org.xml.sax.InputSource(new InputStreamReader(is)));
				}
			}

			return null;
		}

	}

	//--------------------------------------------------------------------------
	// Instance variables
	//

	private Tracer tracer = TracerFactory.getInstance().createTracer("XercesRegPopulator");

	//--------------------------------------------------------------------------
	// Public constants
	//

	public static final String DTD_JAR_FILE_NAME = "/jpos/res/jcl.dtd";

	public static final String XERCES_REG_POPULATOR_NAME_STRING = "JCL XML Entries Populator";
}