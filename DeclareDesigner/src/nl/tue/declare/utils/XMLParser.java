package nl.tue.declare.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * <p>
 * Title: DECLARE
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: TU/e
 * </p>
 * 
 * @author Maja Pesic
 * @version 1.0
 */
public class XMLParser {
	public static Element getFirstElement(final Element element, final String name) {
		final NodeList nl = element.getElementsByTagName(name);
		if (nl.getLength() < 1) { return null; }
		return (Element) nl.item(0);
	}

	/**
	 * @param node
	 *            Element
	 * @param name
	 *            String
	 * @return String
	 */
	public static String getSimpleElementText(final Element node, final String name) {
		final Element nameEl = XMLParser.getFirstElement(node, name);
		final Node textNode = nameEl.getFirstChild();
		if (textNode instanceof Text) {
			return textNode.getNodeValue();
		} else {
			return null;
		}
	}

	/**
	 * @param node
	 *            Node
	 * @return String
	 */
	public static String toString(final Node node) {
		try {
			final StringWriter stringWriter = new StringWriter();
			final Source source = new DOMSource(node);
			final Result result = new StreamResult(stringWriter);

			transform(source, result);
			return stringWriter.toString();
		} catch (final Exception ex) {
			ex.printStackTrace();
			return "blank";
		}
	}

	/**
	 * @param node
	 *            Node
	 * @param file
	 *            String
	 */
	public static void toFile(final Node node, final String file) {
		try {
			final File outputFile = new File(file);
			final OutputStream ostream = new FileOutputStream(outputFile);

			final Source source = new DOMSource(node);
			final Result result = new StreamResult(ostream);

			transform(source, result);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param XML
	 *            String
	 * @return Node
	 */
	public static Node fromString(final String XML) {
		try {
			final Source source = new StreamSource(new StringReader(XML));
			final DOMResult result = new DOMResult();

			transform(source, result);
			return result.getNode();
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param source
	 *            Source
	 * @param result
	 *            Result
	 */
	private static void transform(final Source source, final Result result) {
		final TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = null;
		try {
			transformer = tf.newTransformer();
			transformer.transform(source, result);
		} catch (final TransformerConfigurationException ex) {
			ex.printStackTrace();
		} catch (final TransformerException ex1) {
			ex1.printStackTrace();
		}
	}

	/**
	 * @param node
	 *            Node
	 */
	public static void print(final Node node) {
		SystemOutWriter.singleton().println("HERE " + XMLParser.toString(node));
	}

	/**
	 * @param schemaSource
	 *            Source
	 * @param xml
	 *            Source
	 * @return boolean
	 */
	public static boolean validate(final Source schemaSource, final Source xml) {
		boolean valid = false;
		final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			final Schema schema = factory.newSchema(schemaSource);
			final Validator validator = schema.newValidator();

			try {
				validator.validate(xml);
				valid = true;
			} catch (final IOException ex1) {
				ex1.printStackTrace();
			} catch (final SAXException ex1) {
				ex1.printStackTrace();
			}
		} catch (final SAXException ex) {
			ex.printStackTrace();
		}
		return valid;
	}

	/**
	 * @param schema
	 *            String
	 * @param xml
	 *            String
	 * @return boolean
	 */
	public static boolean validate(final String schema, final String xml) {
		final Source schemaSource = new StreamSource(new StringReader(schema));
		final Source xmlSource = new StreamSource(new StringReader(xml));
		return validate(schemaSource, xmlSource);
	}
}
