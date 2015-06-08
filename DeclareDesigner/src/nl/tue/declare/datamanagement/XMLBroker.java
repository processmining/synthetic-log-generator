package nl.tue.declare.datamanagement;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * <p>Title: DECLARE</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: TU/e</p>
 *
 * @author Maja Pesic
 * @version 1.0
 */
public class XMLBroker
    extends Broker {

  private Document document;
  private File file;
  private final String name;

  public XMLBroker(String aConnectionString, String aName) {
    super(aConnectionString);
    file = new File(this.getConnectionString());
    name = aName;
    this.connect();
  }

  protected Document getDocument() {
    return this.document;
  }

  /**
   *
   * @return boolean
   */
  private boolean createDocument() {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;
    try {
      builder = factory.newDocumentBuilder();
    }
    catch (ParserConfigurationException ex) {
      return false;
    }
    DOMImplementation domImpl = builder.getDOMImplementation();
    this.document = domImpl.createDocument(null, name, null);
    return true;
  }

  /**
   *
   * @param name String
   * @return Element
   */
  public Element createElement(String name) {
    return document.createElement(name);
  }

  /**
   *
   * @param text String
   * @return Text
   */
  public Text createTextNode(String text) {
    return document.createTextNode(text);
  }

  /**
   * connect
   */
  protected void connect() {
    // this.document = this.createDocument(getConnectionString());
    createDocument();
  }

  /**
   * createDocument
   *
   * @param aDocumentName String
   * @return Document
   */
  /*  public Document createDocument(String aDocumentName) {
      return null;
    }*/

  /**
   * write
   *
   * @return boolean returns TRUE if succesfull; returns FALSE when exception
   */
  public boolean writeDocument() {

    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer transformer = null;
    try {
      transformer = tf.newTransformer();
    }
    catch (TransformerConfigurationException ex) {
      return false;
    }
    Source source = new DOMSource(document);
    Result output = new StreamResult(file);
    try {
      transformer.transform(source, output);
    }
    catch (TransformerException ex2) {
      return false;
    }
    return true;
  }

  /**
   * read
   *
   * @return boolean
   */
  public boolean readDocument() {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder parser = null;
    try {
      parser = factory.newDocumentBuilder();
    }
    catch (ParserConfigurationException ex) {

      return false;
    }
    try {
      document = parser.parse(new InputSource(this.getConnectionString()));
    }
    catch (IOException ex1) {
      return false;
    }
    catch (SAXException ex1) {
      return false;
    }
    return true;
  }

  /**
   * read
   *
   * @return boolean
   * @param path String
   */
  public boolean readDocument(String path) {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder parser = null;
    try {
      parser = factory.newDocumentBuilder();
    }
    catch (ParserConfigurationException ex) {

      return false;
    }
    try {
      document = parser.parse(new InputSource(path));
    }
    catch (IOException ex1) {
      return false;
    }
    catch (SAXException ex1) {
      return false;
    }
    return true;
  }

  /**
   * read
   *
   * @return boolean
   * @param documentString String
   */
  public boolean readDocumentString(String documentString) {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder parser = null;
    try {
      parser = factory.newDocumentBuilder();
    }
    catch (ParserConfigurationException ex) {

      return false;
    }
    try {
      byte[] myStrBytes = documentString.getBytes();
      ByteArrayInputStream stringIS = new ByteArrayInputStream(myStrBytes);
      document = parser.parse(stringIS);
    }
    catch (IOException ex1) {
      return false;
    }
    catch (SAXException ex1) {
      return false;
    }
    return true;
  }

  /**
   *
   * @param element Element
   * @param name String
   * @return Element
   */
  /*protected Element getFirstElement(Element element, String name) {
    NodeList nl = element.getChildNodes();
    boolean found = false;
    Node node = null;
    int i = 0;
    while (i < nl.getLength() && !found) {
      node = nl.item(i++);
      found = node.getNodeName().equals(name);
    }
    if (!found) {
      node = this.createElement(name);
      element.appendChild(node);
    }
    return (Element) node;
  }*/

  /**
   *
   * @param element Element
   * @param name String
   * @return Element
   */
 /* protected List<Element> getAllSubElements(Element element, String name) {
    NodeList nl = element.getChildNodes();
    List<Element> list = new ArrayList();
    // first check if the element has the name
    if (element.getNodeName().equals(name)) {
      list.add(element);
    }
    Node node = null;
    // then continue search in all children
    for (int i = 0; i < nl.getLength(); i++) {
      node = nl.item(i);
      if (node instanceof Element) {
        list.addAll(getAllSubElements( (Element) node, name));
      }
    }
    return list;
  }*/

  /**
   *
   * @param element Element
   * @param name String
   * @return String
   */
 /* String getSimpleElementText(Element element, String name) {
    Element nameEl = getFirstElement(element, name);
    Node textNode = nameEl.getFirstChild();
    if (textNode instanceof Text) {
      return textNode.getNodeValue();
    }
    else {
      //throw new RuntimeException("No text in " + name);
      return "";
    }
  }*/

  /**
   *
   * @param element Element
   * @return String
   */
  /*String getSimpleElementText(Element element) {
    Node textNode = element.getFirstChild();
    if (textNode instanceof Text) {
      return textNode.getNodeValue();
    }
    else {
      //throw new RuntimeException("No text in " + name);
      return "";
    }
  }*/

  /**
   * getDocumentRoot
   *
   * @return Element
   */
  protected Element getDocumentRoot() {
    return this.getDocument().getDocumentElement();
  }

  protected void clearDocument() {
    this.getDocument().removeChild(getDocumentRoot());
  }

  /**
   * deleteElement
   *
   * @param elementObject Element
   * @param elementList NodeList
   */
  protected void deleteElement(Element elementObject, Element elementList) {
    elementList.removeChild(elementObject);
  }
}
