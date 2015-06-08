package nl.tue.declare.datamanagement;

import java.util.*;
import java.util.Map.Entry;

import org.w3c.dom.*;

import nl.tue.declare.domain.*;

/**
 * <p>Title: DECLARE</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: TU/e</p>
 * @author Maja Pesic
 * @version 1.0
 */
public class XMLElementFactory {

  private static final String XML_ID = "id";
  private static final String ATTRIBUTES = "attributes";

  private XMLBroker broker;


  /**
   * XMLElementFactory
   */
  public XMLElementFactory(XMLBroker broker) {
    super();
    this.broker= broker;
  }

  /**
   * XMLElementFactory
   */
  public XMLElementFactory(XMLElementFactory factory) {
    this(factory.broker);
  }

  /**
   * baseToElement
   *
   * @param aBase Base
   * @param broker XMLBroker
   * @return Element
   */
  public Element baseToElement(Base aBase) {
    Element id = getDocument().createElement(XML_ID);
    id.appendChild(getDocument().createTextNode(aBase.getIdString()));
    return id;
  }

  /**
   *
   * @param aBase Base
   * @param name String
   * @param broker XMLBroker
   * @return Element
   */
  public Element baseToElement(Base aBase, String name) {
    Element id = getDocument().createElement(name);
    setAttribute(id, XML_ID, aBase.getIdString());
    return id;
  }

  /**
   *
   * @param element Element
   * @return Base
   */
  public Base elementToBase(Element element) {
    String string = element.getAttribute(XML_ID);
    //String id = getSimpleElementText(element, XML_ID);
    int id = 0;
    try {
      id = Integer.decode(string);
    }
    catch (Exception e) {}
    ;
    Base base = new Base(id);
    return base;
  }

  /**
   *
   * @param attributes Base
   * @param broker XMLBroker
   * @return Element
   */
  public void attributesToElement(HashMap<String, String> attributes, Element element) {
    if (element != null && attributes != null) {
      if (!attributes.isEmpty()){
        Element main = getDocument().createElement(ATTRIBUTES);
        Iterator<Entry<String, String>> entries = attributes.entrySet().iterator();
        while (entries.hasNext()) {
          Map.Entry<String, String> entry = entries.next();
          Element item = getDocument().createElement(entry.getKey());
          Text value = (Text) broker.createTextNode(entry.getValue());
          item.appendChild(value);
          main.appendChild(item);
        }
        element.appendChild(main);
      }
    }
  }

  public void elementToAttributes(Element element, HashMap<String, String> attributes) {
    if (element != null && attributes != null) {
      Element main = this.findFirstElement(element,ATTRIBUTES);
      if (main != null){
        NodeList items = main.getChildNodes();
        for (int i = 0; i < items.getLength(); i++) {
          Element item = (Element) items.item(i);
          String name = item.getNodeName();
          String value = "";
          Node text = item.getFirstChild();
          if (text instanceof Text) {
            value = text.getNodeValue();
          }
          attributes.put(name, value);
        }
      }
    }
  }

  /**
   *
   * @param element Element
   * @param name String
   * @param value String
   */
  protected void setAttribute(Element element, String name, String value) {
    element.setAttribute(name, value);
  }

  /**
   *
   * @param name String
   * @param value String
   * @param broker XMLBroker
   * @return Element
   */
  protected Element createObjectAttribute(String name, String value) {
    Element nameTag = broker.createElement(name);
    Text valueText = broker.createTextNode(value);
    nameTag.appendChild(valueText);
    return nameTag;
  }

  /**
   * updateTag
   *
   * @param elementObject Element
   * @param attrName String
   * @param attrValue String
   * @param broker XMLBroker
   */
  protected void updateObjectAttribute(Element elementObject,
                                              String attrName,
                                              String attrValue) {
    Node attribute = getFirstElement(elementObject, attrName);
    if (attribute != null) {
      Text value = (Text) attribute.getFirstChild();
      if (value != null) {
        value.setNodeValue(attrValue);
      }
      else {
        value = (Text) broker.createTextNode(attrValue);
        attribute.appendChild(value);
      }
    }
    else {
      attribute = broker.createElement(attrName);
      Text value = (Text) broker.createTextNode(attrValue);
      attribute.appendChild(value);
      elementObject.appendChild(attribute);
    }
  }

  /**
   * deleteElement
   *
   * @param elementObject Element
   * @param elementList NodeList
   */
  protected void deleteElement(Element elementObject,
                                      Element elementList) {
    elementList.removeChild(elementObject);
  }

  /**
   * getDocument
   *
   * @param broker XMLBroker
   * @return Document
   */
  protected Document getDocument() {
    return broker.getDocument();
  }

  /**
   * getXMLid
   *
   * @return String
   */
  public String getXMLid() {
    return XML_ID;
  }

  /**
   * removeChildren
   *
   * @param element Element
   */
  public void removeChildren(Element element) {
    while (element.hasChildNodes()) {
      element.removeChild(element.getFirstChild());
    }
  }

  /**
   *
   * @param element Element
   * @param name String
   * @return Element
   */
  public Element getFirstElement(Element element, String name) {
    NodeList nl = element.getChildNodes();
    boolean found = false;
    Node node = null;
    int i = 0;
    while (i < nl.getLength() && !found) {
      node = nl.item(i++);
      found = node.getNodeName().equals(name);
    }
    if (!found) {
      //node = this.createElement(name);
      node = broker.createElement(name);
      element.appendChild(node);
    }
    return (Element) node;
  }

  /**
   *
   * @param element Element
   * @param name String
   * @return Element
   */
  public Element findFirstElement(Element element, String name) {
    NodeList nl = element.getChildNodes();
    boolean found = false;
    Node node = null;
    int i = 0;
    while (i < nl.getLength() && !found) {
      node = nl.item(i++);
      found = node.getNodeName().equals(name);
    }
    return found?((Element) node):null;
  }

  /**
   *
   * @param element Element
   * @param name String
   * @return Element
   */
  protected List<Element> getAllSubElements(Element element, String name) {
    NodeList nl = element.getChildNodes();
    List<Element> list = new ArrayList<Element>();
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
  }

  /**
   *
   * @param element Element
   * @param name String
   * @return String
   */
  protected String getSimpleElementText(Element element, String name) {
    Element nameEl = getFirstElement(element, name);
    Node textNode = nameEl.getFirstChild();
    if (textNode instanceof Text) {
      return textNode.getNodeValue();
    }
    else {
      return "";
    }
  }

  /**
   *
   * @param element Element
   * @return String
   */
  protected String getSimpleElementText(Element element) {
    Node textNode = element.getFirstChild();
    if (textNode instanceof Text) {
      return textNode.getNodeValue();
    }
    else {
      return "";
    }
  }

  public Element createElement(String name) {
    return broker.createElement(name);
  }

  protected Text createTextNode(String text){
    return broker.createTextNode(text);
  }
}
